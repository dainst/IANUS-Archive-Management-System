package de.ianus.ingest.web.pages;

import java.util.List;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.metadata.bo.DataCollection;

public class DisseminationListPage  extends AbstractBean{
	
	public static String PAGE_NAME = "disseminationList";
	
	private static final Logger logger = Logger.getLogger(DisseminationListPage.class);
	
	public List<DisseminationIP> getDipList(){
		return Services.getInstance().getDaoService().getDipList(null, 50);
	} 
	
	public String actionGotoMD(){
		
		try {
			DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(dip.getMetadataId());
			
			getSession().getDataCollectionPage().load(dc, dc);
		} catch (Exception e) {
			addInternalError(e);
			return PAGE_NAME;
		}
		return DataCollectionPage.PAGE_NAME;
	}
	
	public String actionDeleteDIP(){
		try {
			DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
			Services.getInstance().getDaoService().deleteIPRecursive(dip);
			
			addMsg("The dissemination IP has been deleted (recursively) [ID=" + dip.getId() + "]");			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	} 
	
	public String gotoFilesPage(){
		try {
			DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
			getSession().getFilesPage().load(dip, ViewLevel.project);
			return FilesPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionGotoDIPPage(){
		try {
			DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
			getSession().getDisseminationPage().load(dip);
			return DisseminationPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	

}
