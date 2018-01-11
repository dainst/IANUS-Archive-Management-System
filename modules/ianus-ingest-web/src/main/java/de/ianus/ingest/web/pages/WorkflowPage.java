package de.ianus.ingest.web.pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.IANUSActivity;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.ingest.web.pages.FilesPage.Right;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.ingest.web.pages.utils.ActivityWrapper;
import de.ianus.metadata.bo.DataCollection;

/**
 * This is an abstract class that can be extended by pages (Java Beans) that are used to manage a particular process instance (like: transfer-, pre-ingest- and ingest-workflow).
 * This class provides the basic methods and the variables that are necessary for the management of a workflow. 
 * Particular features of the workflows should be implemented directly in the concrete classes that extend this class.    
 * 
 * @author jurzua
 *
 */
public class WorkflowPage extends AbstractBean{
	
	static private SimpleDateFormat sm = new SimpleDateFormat("HH:mm:ss");
	
	private WorkflowIP currentIp;
	private List<ActivityWrapper> activities;
	protected Date lastWFUpdate;
	private String userDecision;
	private boolean pollingState = true;
	
	private static Logger logger = Logger.getLogger(WorkflowPage.class);
	
	public String taskId;
	
	
	private List<ArchivalIP> aipList;
	private List<DisseminationIP> dipList;
	
	public String gotoUploadPage(){
		getSession().getUploadPage().load(this.getCurrentIp());
		return UploadPage.PAGE_NAME;
	}
	
	public String gotoFilesPage() {
		try {
			getSession().getFilesPage().load(getCurrentIp(), ViewLevel.project, Right.download);
			return FilesPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return getPageName();
	}
	
	public String gotoDataCollectionPage(){
		try {
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(this.getCurrentIp().getMetadataId());
			if(dc == null){
				throw new Exception("No found DataCollection with id = " + getCurrentIp().getMetadataId());
			}
			getSession().getDataCollectionPage().load(this.getCurrentIp(), dc, dc);
			return DataCollectionPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return getPageName();
	}
	
	public void listenerDeleteDip(ActionEvent event){
		try {
			DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
			Services.getInstance().getDaoService().deleteIPRecursive(dip);
			this.load(this.currentIp);
			addMsg("The submission IP has been deleted (recursively) [ID=" + dip.getId() + "]");			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeleteAip(ActionEvent event){
		try {
			ArchivalIP aip = (ArchivalIP)getRequestBean("aip");
			Services.getInstance().getDaoService().deleteIPRecursive(aip);
			this.load(this.currentIp);
			addMsg("The submission IP has been deleted (recursively) [ID=" + aip.getId() + "]");			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void load(WorkflowIP ip) throws Exception{
		this.currentIp = ip;
		this.loadActivities();
	}
	
	public boolean isShowAipTable(){
		return aipList != null && !aipList.isEmpty();
	}
	
	public boolean isShowDipTable(){
		return dipList != null && !dipList.isEmpty();
	}
	
	public void changeUserDecision(ValueChangeEvent event){
		this.userDecision = (String)event.getNewValue();
	}
	
	
	public String actionViewAip(){
		ArchivalIP aip = (ArchivalIP)getRequestBean("aip");
		getSession().getArchivalPage().load(aip);
		return ArchivalPage.PAGE_NAME;
	}
	
	public String actionViewDip() throws Exception{
		DisseminationIP dip = (DisseminationIP)getRequestBean("dip");
		getSession().getDisseminationPage().load(dip);
		return DisseminationPage.PAGE_NAME;
	}
	
	
	public String getPageName(){
		if(this instanceof TransferPage){
			return TransferPage.PAGE_NAME;
		}else if(this instanceof SubmissionPage){
			return SubmissionPage.PAGE_NAME;
		}else if(this instanceof ArchivalPage) {
			return ArchivalPage.PAGE_NAME;
		}else if(this instanceof DisseminationPage) {
			return DisseminationPage.PAGE_NAME;
		}
		return null;
	}
	
	public void completeTask(){
		try {
			IANUSActivity activity = Services.getInstance().getDaoService().getIANUSActivitiesByTaskId(taskId);
			if(activity != null){
				Services.getInstance().getProcessEngineService().completeTask(activity, new HashMap<String, Object>());
			}else{
				addMsg("No found an activity with the taskId = " + this.taskId);
			}
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void reload(){
		System.out.println("*");
		//logger.info("*****" + this.currentIp.toString());
		try {
			if(this.currentIp instanceof TransferP){
				((TransferPage)this).load(this.currentIp);
			}else if(this.currentIp instanceof SubmissionIP){
				((SubmissionPage)this).load(this.currentIp);
			}else if(this.currentIp instanceof ArchivalIP){
				((ArchivalPage)this).load(this.currentIp);
			}else if(this.currentIp instanceof DisseminationIP){
				((DisseminationPage)this).load(this.currentIp);
			}
			this.lastWFUpdate = new Date();
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public boolean isPollingStop(){
		//logger.info("this.currentIp= " + this.currentIp);
		return StringUtils.equals(this.currentIp.getState(), WorkflowIP.FINISHED) ||
				StringUtils.equals(this.currentIp.getState(), WorkflowIP.FINISHED_ERRORS);
	}
	
	public String getStateIcon(){
		if(StringUtils.equals(this.currentIp.getState(), WorkflowIP.NOT_STARTED)){
			return getAppBean().getImgWFNoStarted();
		}else if(StringUtils.equals(this.currentIp.getState(), WorkflowIP.IN_PROGRESS)){
			return getAppBean().getImgWorking();
		}else if(StringUtils.equals(this.currentIp.getState(), WorkflowIP.FINISHED)){
			return getAppBean().getImgWFFinished();
		}else if(StringUtils.equals(this.currentIp.getState(), WorkflowIP.FINISHED_ERRORS)){
			return getAppBean().getImgWFFinishedWithErrors();
		}	
		return null;
	}
	
	public void listenerCompleteTask(ActionEvent event){

		try {
			ActivityWrapper activityWrapper = (ActivityWrapper)getRequestBean("wrapper");
			IANUSActivity a0 = activityWrapper.getActivity();
			logger.info(a0.toString());
			
			if(!validateInitialMD(a0)){
				addMsg("The initial metadata is not completed");
			}else{
				Map<String, Object> taskVariables = new HashMap<String, Object>();
				if(StringUtils.isNotEmpty(activityWrapper.getUserDecision())){
					taskVariables.put(activityWrapper.getTaskFormField().getId(), this.getUserDecision());
				}
				//assertVariables(activityWrapper, taskVariables);
				Services.getInstance().getProcessEngineService().completeTask(activityWrapper.getActivity(), taskVariables);
				this.load(this.currentIp);	
			}	
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	private boolean validateInitialMD(IANUSActivity a0) throws Exception{
		if(StringUtils.equals("metadata_edition_initial", a0.getTaskDefinitionKey())){
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(this.currentIp.getMetadataId());
			return dc.validateInitialMD();
		}
		return true;
	}
	
	
	public void actionChangePollingState(){
		this.pollingState = !pollingState;
		this.reload();
	}
	
	public String getPollingButtonLabel(){
		return (pollingState) ? "deactivate" : "activate";
	}
	
	public boolean isWfInProgressState(){
		return (StringUtils.equals(this.currentIp.getState(), WorkflowIP.IN_PROGRESS));
	}
	
	public boolean isPollingState() {
		return pollingState;
	}

	public void setPollingState(boolean pollingState) {
		this.pollingState = pollingState;
	}

	public String getLastWFUpdate(){
		if(this.lastWFUpdate != null){
			return sm.format(lastWFUpdate);	
		}
		return null;
	}
	
	public void loadActivities() throws Exception{
		this.activities = new ArrayList<ActivityWrapper>();
		List<IANUSActivity> list = Services.getInstance().getProcessEngineService().getActivities(this.currentIp);
		if(list != null) for(IANUSActivity ianusActivity : list){
			ActivityWrapper activity = new ActivityWrapper(ianusActivity);
			ActivityOutput output = Services.getInstance().getDaoService().getActivityOutput(ianusActivity.getId());
			activity.setOutput(output);
			try{
				if(!activity.isCompleted() && activity.isUserTask()){
					TaskFormData taskFormData = 
							Services.getInstance().getProcessEngineService().getProcessEngine().getFormService().getTaskFormData(ianusActivity.getTaskId());
					List<FormField> formFields = taskFormData.getFormFields();
					for(FormField field : formFields){
						if(field.getType() instanceof EnumFormType){
							activity.loadTaskFormField(field);
						}
					}	
				}
			}catch(Exception ex){
				System.out.println(ianusActivity.toString());
				ex.printStackTrace();
			}
			
			this.activities.add(activity);
		}
		Collections.sort(activities);
		Collections.reverse(activities);
	}
	
	
	
	
	
	/**
	 * This method is used only for user tasks.
	 * It returns the name of the page that facilitates the execution of the task for user.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String actionToUserSupportPage() throws Exception{
		ActivityWrapper wrapper = (ActivityWrapper)getRequestBean("wrapper");
		String pageName = wrapper.getUserSupport();
		logger.info("PageName= " + pageName);
		this.getSession().loadPage(pageName, this.currentIp);
		return pageName;
	}
	
	public List<ActivityWrapper> getActivities() {
		return activities;
	}
	
	public void setActivities(List<ActivityWrapper> activities) {
		this.activities = activities;
	}

	public WorkflowIP getCurrentIp() {
		return currentIp;
	}

	public void setCurrentIp(WorkflowIP currentIp) {
		this.currentIp = currentIp;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserDecision() {
		return userDecision;
	}

	public void setUserDecision(String userDecision) {
		this.userDecision = userDecision;
	}
	public List<ArchivalIP> getAipList() {
		return aipList;
	}

	public void setAipList(List<ArchivalIP> aipList) {
		this.aipList = aipList;
	}

	public List<DisseminationIP> getDipList() {
		return dipList;
	}

	public void setDipList(List<DisseminationIP> dipList) {
		this.dipList = dipList;
	}	
}
