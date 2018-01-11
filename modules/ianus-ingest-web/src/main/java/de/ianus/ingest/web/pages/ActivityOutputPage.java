package de.ianus.ingest.web.pages;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.WorkflowIP;

/**
 * 
 * @author jurzua
 *
 */
public class ActivityOutputPage {
	public static String pageName = "activityOutput";
	Logger logger = Logger.getLogger(ActivityOutputPage.class);
	
	private ActivityOutput output;
	private WorkflowIP wfip;
	
	public void load(Long ianusActivityId, Long wfipId){
		this.output = Services.getInstance().getDaoService().getActivityOutput(ianusActivityId);
		this.wfip = Services.getInstance().getDaoService().getTransfer(wfipId);
	}
	/*
	public void load(String activityId, Long wfipId){
		WorkflowIP wfip = WebServices.getInstance().getDaoService().getSip(wfipId);
		ActivityOutput output = WebServices.getInstance().getDaoService().getActivityOutput(wfipId, activityId);
		load(output, wfip);
	}*/
	
	public void load(ActivityOutput output, WorkflowIP wfip){
		this.output = output;
		this.wfip = wfip;
	}
	
	public String getExecutionTime(){
		long seconds = this.output.getExecutionTime() / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60 + " [Days:Hours:Minutes:Seconds]"; 
		return time;
	}

	public ActivityOutput getOutput() {
		return output;
	}

	public void setOutput(ActivityOutput output) {
		this.output = output;
	}

	public WorkflowIP getWfip() {
		return wfip;
	}

	public void setWfip(WorkflowIP wfip) {
		this.wfip = wfip;
	}
	
	
}
