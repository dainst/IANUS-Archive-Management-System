package de.ianus.ingest.web.pages;

import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.pages.metadata.InitialMDPage;
import de.ianus.metadata.bo.DataCollection;

/**
 * 
 * @author jurzua
 *
 */
public class TransferListPage  extends AbstractIPPage{
	
	public static String PAGE_NAME = "transferList";
	
	private static final Logger logger = LogManager.getLogger(TransferListPage.class);
	
	public void load(){
		this.ipList = Services.getInstance().getDaoService().getTransferPList();
	}
	
	@Override
	public List<WorkflowIP> getIpList() {
		return Services.getInstance().getDaoService().getTransferPList();
	}
	
	public String actionGotoTransferPage(){
		try {
			TransferP tp = (TransferP)getRequestBean("ip");
			getSession().getTransferPage().load(tp);
			return TransferPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	@Deprecated
	public String actionReload(){
		this.ipList = Services.getInstance().getDaoService().getTransferPList();
		return PAGE_NAME;	
	}
	
	public String actionStartTransferWF(){
		try {
			
			TransferP tp = new TransferP();
			String label = "TP_" + System.currentTimeMillis();
			
			DataCollection dc = new DataCollection();
			dc.setLabel(label);
			tp.setCollectionLabel(label);
			Services.getInstance().getMDService().saveEntry(dc);
			tp.setMetadataId(dc.getId());
			tp = Services.getInstance().getProcessEngineService().startTransferWF(tp);
			getSession().getInitialMDPage().loadDataCollection(tp, dc.getId());
			this.load();
			
			return InitialMDPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	/**
	 * @deprecated
	 * @param event
	 */
	public void listenerRegisterDC(ActionEvent event){
		this.ip = new TransferP();
	}
	
	/**
	 * @deprecated
	 * @param event
	 */
	public String actionStartTransferWFXX(){
		try {
			DataCollection dc = new DataCollection();
			dc.setLabel(((TransferP)this.ip).getCollectionLabel());
			Services.getInstance().getMDService().saveEntry(dc);
			((TransferP)this.ip).setMetadataId(dc.getId());
			this.ip = Services.getInstance().getProcessEngineService().startTransferWF((TransferP)this.ip);
			this.load();
			Long ipId = ip.getId();
			this.ip = null;
			return TransferPage.PAGE_NAME + "?faces-redirect=true&tpId=" + ipId;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public void listenerDeleteTransfer(ActionEvent event){
		try {
			TransferP tp = (TransferP)getRequestBean("ip");
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(tp.getMetadataId()); 
			Services.getInstance().getDaoService().deleteIP(tp);
			try {
				Services.getInstance().getMDService().deleteDataCollection(dc);
			} catch (Exception ex) {
				addInternalError(ex);
			}
			this.load();
			addMsg("The data collection has been deleted [ID=" + tp.getId() + "]");			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
}
