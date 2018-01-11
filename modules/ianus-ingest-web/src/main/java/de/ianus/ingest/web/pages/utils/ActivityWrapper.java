package de.ianus.ingest.web.pages.utils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;

import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.IANUSActivity;
import de.ianus.ingest.web.pages.CollectionIdPage;
import de.ianus.ingest.web.pages.FilesPage;
import de.ianus.ingest.web.pages.PreIngestReportPage;
import de.ianus.ingest.web.pages.SimpleUploadPage;
import de.ianus.ingest.web.pages.UploadPage;
import de.ianus.ingest.web.pages.metadata.AssetUploadPage;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.ingest.web.pages.metadata.InitialMDPage;

/**
 * 
 * @author jurzua
 *
 */
public class ActivityWrapper implements Serializable, Comparable<ActivityWrapper> {
	private static final long serialVersionUID = -3748329108682390562L;
	
	private static final Logger logger = LogManager.getLogger(ActivityWrapper.class);
	
	private static SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	private static String COLOR_ERROR = "background-color: #FFC2C2;";//red
	private static String COLOR_COMPLETED = "background-color: #C2C2D6;";//violet
	private static String COLOR_NOT_COMPLETED = "background-color: #99FF99;";//green
	private static String COLOR_WARNING = "background-color: #FFFF99;";
	private static String COLOR_STARTED = "background-color: #FF1010;";
	
	//private HistoricActivityInstance activity;
	private IANUSActivity activity;
	private boolean started;
	private ActivityOutput output;
	private boolean userTaskCompleted = false;
	
	private FormField taskFormField;
	private List<SelectItem> userSuggestList;
	private String userDecision;
	private String optionLabel;
	/*
	public ActivityWrapper(HistoricActivityInstance activity, boolean completed){
		this.activity = activity;
		this.started = this.activity.getStartTime() != null;
		//this.completed = completed; //this.activity.getEndTime() != null;
		this.completed = this.activity.getEndTime() != null;
	}*/
	
	public ActivityWrapper(IANUSActivity activity){
		this.activity = activity;
		this.started = this.activity.getStartTime() != null;
		//this.completed = completed; //this.activity.getEndTime() != null;
	}
	

	public boolean isHasUserSupport(){
		return !isCompleted() && StringUtils.isNotEmpty(getUserSupport());
	}
	
	
	/**
	 * Mapping activity taskDefinitionKeys to page names...
	 * 
	 * Also consider the mapping SessionBean.loadPage(), 
	 * which maps the page names to the according page loader!
	 *  
	 * @return String PAGE_NAME
	 */
	public String getUserSupport(){
		if(this.isUserTask()){
			switch(this.getActivity().getTaskDefinitionKey()) {
			case "data_upload":			// transfer-workflow
			case "user_manage_files":	// pre-ingest 
			case "ut_manage_files_delete_existing_upload_new":	// ingest
				return UploadPage.PAGE_NAME;
			case "files_merge_transfer_packages":
			case "files_evaluation":
			case "ut_data_validation_and_consistency_checks":
			case "us_restructure_and_rename_files_folders":	// pre-ingest
			case "ut_restructure_and_rename_files_folders":	// ingest...
			case "us_file_format_conversion":
				return FilesPage.PAGE_NAME;
			case "metadata_edition_initial":
			case "metadata_edition_initial_evaluation":
				return InitialMDPage.PAGE_NAME;
			case "collection_id_editor":
				return CollectionIdPage.PAGE_NAME;
			case "user_report_review_file_metadata":
			case "user_report_review_collection_metadata":
			case "user_report_check_legal_constraints":
			case "user_report_check_data_readability":
			case "user_report_check_consistency_of_data":
			case "user_report_check_actions_of_microservices":
				return PreIngestReportPage.PAGE_NAME;
			case "UserTask_1":	// assign collection ID
			case "user_define_collection_access_policy":
			case "us_assign_doi":
			case "ut_edit_metadata_for_collections_files":
				return DataCollectionPage.PAGE_NAME;
			case "user_define_scope_and_content_of_DIP":
				return AssetUploadPage.PAGE_NAME;
			case "ut_update_documentation":	// add final documentation
				return SimpleUploadPage.PAGE_NAME;
			//case "ut_transfer_aip_to_external_storage_partner":
				//return render link to bagit creation 
			
			}
		}
		return null;
	}
	
	public void loadTaskFormField(FormField taskFormField) {
		this.taskFormField = taskFormField;
		this.userDecision = (String)this.taskFormField.getDefaultValue();
		this.optionLabel = this.taskFormField.getLabel();
		this.userSuggestList = new ArrayList<SelectItem>();
		EnumFormType values = (EnumFormType)this.taskFormField.getType();
		for(Entry<String, String> entry : values.getValues().entrySet()){
			this.userSuggestList.add(new SelectItem(entry.getKey(), entry.getValue()));
		}
	}
	
	@Override
    public int compareTo(ActivityWrapper another) {
        if((this.activity.getEndTime() == null && another.activity.getEndTime() == null)){
        	return this.activity.getStartTime().compareTo(another.activity.getStartTime());
        }else if(this.activity.getEndTime() == null){
        	return 1;
        }else if(another.activity.getEndTime() == null){
        	return -1;
        }
        
        int endTimeDiff = this.activity.getEndTime().compareTo(another.activity.getEndTime());
        if(endTimeDiff != 0){
        	return endTimeDiff;
        }
        
        return this.activity.getStartTime().compareTo(another.activity.getStartTime());
    }
	
	public boolean getHasFormField(){
		return this.taskFormField != null;
	}
	
	public boolean isUserTask(){
		return StringUtils.equals(activity.getActivityType(), "userTask");
	}
	
	public boolean isServiceTask(){
		return StringUtils.equals(activity.getActivityType(), "serviceTask");
	}
	
	public String getStartTime(){
		if(this.activity.getStartTime() != null){
			return dt.format(this.activity.getStartTime());
		}
		return null;
	}
	
	public String getEndTime(){
		if(this.activity.getEndTime() != null){
			return dt.format(this.activity.getEndTime());
		}
		return null;
	}
	
	public IANUSActivity getActivity() {
		return activity;
	}

	public void setActivity(IANUSActivity activity) {
		this.activity = activity;
	}

	public boolean getShowCompleteButton(){
		return !isCompleted() && isUserTask();
	}
	
	public boolean getShowWorkingIcon(){
		return !isCompleted() && isServiceTask();  
	}
	
	public boolean isCompleted() {
		return this.getEndTime() != null;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public String getBackgroundColor(){
		if(isCompleted()){
			if(getHasErrors()){
				return COLOR_ERROR;
			}else if(getHasWarnings()){
				return COLOR_WARNING;
			}
			return COLOR_COMPLETED;
		}
		return COLOR_NOT_COMPLETED;
	}
	
	public boolean getHasErrors(){
		boolean hasErrors = (output != null && output.getHasErrors()) ? true : false;
		return hasErrors;
	}
	
	public boolean getHasWarnings(){
		boolean hasWarnings = (output != null && output.getHasWarnings()) ? true : false;
		return hasWarnings;
	}

	public ActivityOutput getOutput() {
		return output;
	}

	public void setOutput(ActivityOutput output) {
		this.output = output;
	}

	public List<SelectItem> getUserSuggestList() {
		return userSuggestList;
	}

	public void setUserSuggestList(List<SelectItem> userSuggestList) {
		this.userSuggestList = userSuggestList;
	}

	public String getUserDecision() {
		return userDecision;
	}

	public void setUserDecision(String userDecision) {
		this.userDecision = userDecision;
	}

	public FormField getTaskFormField() {
		return taskFormField;
	}

	public void setTaskFormField(FormField taskFormField) {
		this.taskFormField = taskFormField;
	}

	public boolean isUserTaskCompleted() {
		return userTaskCompleted;
	}

	public void setUserTaskCompleted(boolean userTaskCompleted) {
		this.userTaskCompleted = userTaskCompleted;
	}


	public String getOptionLabel() {
		return optionLabel;
	}


	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}
	
}
