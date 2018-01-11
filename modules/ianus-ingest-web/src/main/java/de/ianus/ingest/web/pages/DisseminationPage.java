package de.ianus.ingest.web.pages;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;

public class DisseminationPage extends WorkflowPage {

	public static final String PAGE_NAME = "dissemination";

	private static Logger logger = Logger.getLogger(ArchivalPage.class);
	
	
	

	public String gotoSipPage() {
		try {
			
			getSession().getSubmissionPage().load(this.getDip().getSip());
			return SubmissionPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String gotoAipPage() {
		try {
			ArchivalIP ip = getDip().getAip();
			if(ip == null) {
				addMsg("An AIP for this DIP does not exist.");
				return PAGE_NAME;
			}
			getSession().getArchivalPage().load(ip);
			return ArchivalPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public DisseminationIP getDip() {
		return (this.getCurrentIp() instanceof DisseminationIP) ? (DisseminationIP) this.getCurrentIp() : null;
	}

}
