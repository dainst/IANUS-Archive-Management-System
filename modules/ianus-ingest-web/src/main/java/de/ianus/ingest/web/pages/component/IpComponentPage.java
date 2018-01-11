package de.ianus.ingest.web.pages.component;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.ingest.web.pages.ArchivalPage;
import de.ianus.ingest.web.pages.DisseminationPage;
import de.ianus.ingest.web.pages.SubmissionPage;
import de.ianus.ingest.web.pages.TransferPage;

public class IpComponentPage extends AbstractBean{

	private static final Logger logger = LogManager.getLogger(IpComponentPage.class);

	protected WorkflowIP ip;
	
	public void load(WorkflowIP ip) {
		this.ip = ip;
	}
	
	public String getStyleClass(){
		if(this.ip != null){
			if(this.ip instanceof SubmissionIP){
				return "mainControlPanelSIP";
			}else if(this.ip instanceof DisseminationIP){
				return "mainControlPanelDIP";
			}else if(this.ip instanceof ArchivalIP) {
				return "mainControlPanelAIP";
			}
		}
		return "mainControlPanel";
	}
	
	public String gotoIPPage(){
		logger.info(this.ip.toString());
		if(this.ip == null){
			addMsg("The information package is null");
		}else if(this.ip instanceof TransferP){
			try {
				getSession().getTransferPage().load(this.ip);
			} catch (Exception e) {
				addInternalError(e);
			}
			return TransferPage.PAGE_NAME;
		}else if(this.ip instanceof SubmissionIP){
			try {
				getSession().getSubmissionPage().load(this.ip);
			} catch (Exception e) {
				addInternalError(e);
			}
			return SubmissionPage.PAGE_NAME;
		}else if(this.ip instanceof ArchivalIP){
			try {
				getSession().getArchivalPage().load(this.ip);
			} catch (Exception e) {
				addInternalError(e);
			}
			return ArchivalPage.PAGE_NAME;
		}else if(this.ip instanceof DisseminationIP) {
			try {
				getSession().getDisseminationPage().load(this.ip);
			} catch (Exception e) {
				addInternalError(e);
			}
			return DisseminationPage.PAGE_NAME;
		}  
		return null;
	}
	
	public WorkflowIP getWfip(){
		return (ip != null && ip instanceof WorkflowIP) ? (WorkflowIP)ip : null;
	}

	public WorkflowIP getIp() {
		return ip;
	}

	public void setIp(WorkflowIP ip) {
		this.ip = ip;
	}
}
