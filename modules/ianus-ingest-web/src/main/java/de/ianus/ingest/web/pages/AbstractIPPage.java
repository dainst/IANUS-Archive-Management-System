package de.ianus.ingest.web.pages;

import java.util.List;

import javax.faces.event.ActionEvent;

import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;

/**
 * 
 * @author jurzua
 *
 */
public abstract class AbstractIPPage extends AbstractBean{

	protected List<WorkflowIP> ipList;
	protected WorkflowIP ip;
	
	public void listenerSaveIp(ActionEvent event){
		try {
			throw new UnsupportedOperationException("This method has not been implemented");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String gotoUploadPage(){
		WorkflowIP ip0 = (WorkflowIP)getRequestBean("ip");
		getSession().getUploadPage().load(ip0);
		return "upload";
	}
	
	public void listenerSelectIp(ActionEvent event){
		this.ip = (WorkflowIP)getRequestBean("ip");
	}
	
	public void listenerCancelEdition(ActionEvent event){
		this.ip = null;
	}
	
	public boolean isShowIpTable(){
		return this.getIpList() != null && !this.getIpList().isEmpty() && this.ip == null;
	}
	
	public String gotoFilesPage(){
		try {
			WorkflowIP ip0 = (WorkflowIP)getRequestBean("ip");
			getSession().getFilesPage().load(ip0, ViewLevel.project);
			return "files";
		} catch (Exception e) {
			addInternalError(e);
		}
		return getPageName();
	}
	
	public String getPageName(){
		if(this instanceof OverviewDCPage)
			return "transfer";
		return null;
	}
	
	public List<WorkflowIP> getIpList() {
		return ipList;
	}
	public void setIpList(List<WorkflowIP> ipList) {
		this.ipList = ipList;
	}
	public WorkflowIP getIp() {
		return ip;
	}
	public void setIp(WorkflowIP ip) {
		this.ip = ip;
	}
	
	
	
}
