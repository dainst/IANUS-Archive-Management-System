package de.ianus.ingest.web.pages;

import java.util.List;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.web.AbstractBean;

/**
 * 
 * @author jurzua
 *
 */
public class SubmissionListPage extends AbstractBean{

	private static final Logger logger = Logger.getLogger(SubmissionListPage.class);
	

	public static String PAGE_NAME = "submissionList";
	
	
	public List<SubmissionIP> getSipList(){
		return Services.getInstance().getDaoService().getSipList(-1);
	} 
	
	public String actionGotoSIPPage(){
		try {
			SubmissionIP sip = (SubmissionIP)getRequestBean("sip");
			getSession().getSubmissionPage().load(sip);
			return SubmissionPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeleteSIP(){
		try {
			SubmissionIP sip = (SubmissionIP)getRequestBean("sip");
			Services.getInstance().getDaoService().deleteIPRecursive(sip);
			
			addMsg("The submission IP has been deleted (recursively) [ID=" + sip.getId() + "]");			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	} 
	
	
	/*
	
	public String actionGotoMD(){
		
		try {
			DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
			DataCollection dc = WebServices.getInstance().getMDService().getDataCollection(dip.getMetadataId());
			
			getSession().getDataCollectionPage().load(dc, dc);
		} catch (Exception e) {
			addInternalError(e);
			return PAGE_NAME;
		}
		return DataCollectionPage.PAGE_NAME;
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
	*/
	
}
