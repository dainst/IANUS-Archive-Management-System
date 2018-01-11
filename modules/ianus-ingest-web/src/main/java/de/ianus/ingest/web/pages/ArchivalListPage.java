package de.ianus.ingest.web.pages;

import java.util.List;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.web.AbstractBean;

public class ArchivalListPage   extends AbstractBean{
	
	public static String PAGE_NAME = "archivalList";
	
	private static final Logger logger = Logger.getLogger(DisseminationListPage.class);
	
	public List<ArchivalIP> getAipList(){
		return Services.getInstance().getDaoService().getAipList();
	} 
	
	public String actionGotoAIPPage(){
		try {
			ArchivalIP aip = (ArchivalIP)getRequestBean("aip");
			getSession().getArchivalPage().load(aip);
			return ArchivalPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public void actionCreateBagit() throws Exception {
		try {
			ArchivalIP aip = (ArchivalIP)getRequestBean("aip");
			logger.info("Creating Bagit file of " + aip.toLogString());
			aip.createBagit();
			addMsg("The bagit file has been created");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionDeleteAIP(){
		try {
			
			ArchivalIP aip = (ArchivalIP)getRequestBean("aip");
			Services.getInstance().getDaoService().deleteIPRecursive(aip);
			addMsg("The AIP has been deleted (recursively) [ID=" + aip.getId() + "]");
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	} 
}
