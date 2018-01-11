package de.ianus.ingest.web.pages;

import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;
import de.ianus.ingest.web.pages.metadata.InitialMDPage;
import de.ianus.metadata.MetadataService.CloneResult;
import de.ianus.metadata.bo.DataCollection;

/**
 * 
 * This class is used as Bean to manage an instance of a transfer workflow.
 * This class contains a Transfer Package (TransferP) and this object has a reference to an instance of the transfer workflow.
 * 
 * 
 * 
 * @author jurzua
 *
 */
public class TransferPage extends WorkflowPage {
	
	public static final String PAGE_NAME = "transfer";
	private static Logger logger = Logger.getLogger(TransferPage.class);
	
	private List<SubmissionIP> sipList;
	
	private List<SubmissionIP> mdCloneCandidateList;
	
	public String actionLoadMdCandidates(){
		this.mdCloneCandidateList = Services.getInstance().getDaoService().getSipList(-1);
		return PAGE_NAME;
	}
	
	public String actionCancelCloneMd(){
		this.mdCloneCandidateList = null;
		return PAGE_NAME;
	}
	
	public String actionCloneMd(){
		try {
			SubmissionIP sip = (SubmissionIP)getRequestBean("mdCloneCandidate");
			
			DataCollection tpDcOld = Services.getInstance().getMDService().getDataCollection(getTp().getMetadataId());
			
			//Cloning the metadata
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(sip.getMetadataId());
			CloneResult cloneResult = Services.getInstance().getMDService().cloneDataCollection0(dc);
			DataCollection dc0 = Services.getInstance().getMDService().getDataCollection(cloneResult.getDc().getId());
			
			//update TP with the MD values
			getTp().setCollectionIdentifier(dc0.getCollectionIdentifier());
			getTp().setCollectionLabel(dc0.getCollectionLabel());
			getTp().setMetadataId(dc0.getId());
			
			Services.getInstance().getDaoService().saveDBEntry(getTp());
			
			try {
				Services.getInstance().getMDService().deleteDataCollection(tpDcOld);
			} catch (Exception ex) {
				addInternalError(ex);
			}
			
			this.mdCloneCandidateList = null;
			addMsg("The metadata has been cloned from " + sip.toString());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME; 
	}
	
	public String actionCompleteTask(){
		this.completeTask();
		return PAGE_NAME;
	}
	
	public String actionReload(){
		this.reload();
		return PAGE_NAME;
	}
	
	@Override
	public void load(WorkflowIP wfip) throws Exception{
		super.load(wfip);
		TransferP tp = (TransferP)wfip;
		this.sipList = Services.getInstance().getDaoService().getSip(tp);
		this.setAipList(Services.getInstance().getDaoService().getAip(tp));
		this.setDipList(Services.getInstance().getDaoService().getDip(tp));
	}
	
	public String gotoInitialMDPage(){
		getSession().getInitialMDPage().loadDataCollection(this.getTp(), this.getTp().getMetadataId());
		return InitialMDPage.PAGE_NAME;
	}
	
	public String gotoRsyncUploadPage(){
		try {
			getSession().getRsyncUploadPage().load(getCurrentIp(), ViewLevel.project);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RsyncUploadPage.PAGE_NAME;
	}
	
	public String actionViewSip() throws Exception{
		SubmissionIP sip = (SubmissionIP)getRequestBean("sip");
		getSession().getSubmissionPage().load(sip);
		return SubmissionPage.PAGE_NAME;
	}
	
	public boolean isShowSipTable(){
		return sipList != null && !sipList.isEmpty();
	}
	
	public void listenerDeleteSip(ActionEvent event){
		try {
			SubmissionIP sip = (SubmissionIP)getRequestBean("sip");
			Services.getInstance().getDaoService().deleteIPRecursive(sip);
			this.load(this.getTp());
			addMsg("The submission IP has been deleted (recursively) [ID=" + sip.getId() + "]");			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionPreIngestTransfer(){
		try {
			SubmissionIP sip = Services.getInstance().getProcessEngineService().startPreIngestWF(getTp());
			this.getSession().getSubmissionPage().load(sip);
			this.load(this.getTp());
			return SubmissionPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public TransferP getTp() {
		return (getCurrentIp() instanceof TransferP) ? (TransferP)getCurrentIp() : null;
	}

	public List<SubmissionIP> getSipList() {
		return sipList;
	}

	public void setSipList(List<SubmissionIP> sipList) {
		this.sipList = sipList;
	}

	public List<SubmissionIP> getMdCloneCandidateList() {
		return mdCloneCandidateList;
	}

	public void setMdCloneCandidateList(List<SubmissionIP> mdCloneCandidateList) {
		this.mdCloneCandidateList = mdCloneCandidateList;
	}
	
	
}
