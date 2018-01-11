package de.ianus.ingest.core.bo;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

/**
 * 
 * This class contains the output of the execution of a particular task. 
 * The output of an execution is composed of three parts: the standard output, the standard error output and the warnings and they are stored as plain text (as a String).
 * This class is mapped to the database and it is always related to an instance of a workflow information package.
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name = "ActivityOutput")
public class ActivityOutput extends DBEntry implements Serializable {
	private static final long serialVersionUID = 6066367858539263268L;
	private static Logger logger = Logger.getLogger(ActivityOutput.class);
	
	public static String TYPE_SIP = "sip";
	public static String TYPE_PAIP = "paip";
	
	public static String ACT_TYPE_START_EVENT = "startEvent";
	public static String ACT_TYPE_USER_TASK = "userTask";
	public static String ACT_TYPE_EXCLUSIVE_GATEWAY = "exclusiveGateway";
	public static String ACT_TYPE_SERVICE_TASK = "serviceTask";
	public static String ACT_TYPE_END_EVENT = "endEvent";

	@Column(name = "wfipId")
	private Long wfipId;
	
	@Column(name = "ianusActivityId")
	private Long ianusActivityId;
	
	// name of the activiy in the definition (it comes from the xml-definition)
	@Column(name = "activityId")
	private String activityId;

	@Column(name = "activityName")
	private String activityName;

	@Column(name = "activityType")
	private String activityType;

	@Column(name = "outputType")
	private String outputType;

	@Column(name = "processDefinitionId")
	private String processDefinitionId;

	@Column(name = "processInstanceId")
	private String processInstanceId;

	@Column(name = "executionTime")
	private Long executionTime = 0L;
	
	// only if this output comes from a userTask, this field has a value.
	@Column(name = "taskId")
	private String taskId;

	@Column(name = "msName")
	private String msName;
	
	@Lob
	@Column(name = "stdOut")
	private String stdOut = new String();

	@Lob
	@Column(name = "strErr")
	private String strErr = new String();

	@Lob
	@Column(name = "warning")
	private String warning = new String();
	
	@Column(name = "hasErrors", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean hasErrors = false;
	
	@Column(name = "hasWarnings", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean hasWarnings = false;
	
	public static ActivityOutput getActivityOutput4JavaTask(ActivityExecution execution, Long wfipId){
		ActivityOutput output = new ActivityOutput();
		
		output.setWfipId(wfipId);
		output.setActivityId(execution.getActivity().getId());
		output.setActivityName(execution.getCurrentActivityName());
		output.setProcessInstanceId(execution.getProcessInstanceId());
		output.setProcessDefinitionId(execution.getProcessDefinitionId());
		output.setActivityType(ACT_TYPE_SERVICE_TASK);
		output.setOutputType(TYPE_SIP);
		return output;
	}
	
	public static ActivityOutput getActivityOutput4UserTask(HistoricActivityInstance activity, Long wfipId){
		ActivityOutput output = new ActivityOutput();
		
		output.setWfipId(wfipId);
		output.setActivityId(activity.getActivityId());
		output.setActivityName(activity.getActivityName());
		output.setProcessInstanceId(activity.getProcessInstanceId());
		output.setProcessDefinitionId(activity.getProcessDefinitionId());
		output.setActivityType(activity.getActivityType());
		output.setOutputType(TYPE_SIP);
		
		return output;
	}
	
	public void print(String text){
		this.stdOut += (text + "\n");
	}
	
	public void warningPrint(String text){
		this.warning += (text + "\n");
	}
	
	public void errorPrint(String text){
		this.strErr += (text + "\n");
	}
	
	public void printException(Exception e){
		if(e != null){
			this.errorPrint(e.getMessage());
			this.errorPrint(exceptionStacktraceToString(e));
			this.hasErrors = true;
		}
	}
	
	public static String exceptionStacktraceToString(Exception e){
	    return Arrays.toString(e.getStackTrace());
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStdOut() {
		return stdOut;
	}

	public void setStdOut(String stdOut) {
		this.stdOut = stdOut;
	}

	public String getStrErr() {
		return strErr;
	}

	public void setStrErr(String strErr) {
		this.strErr = strErr;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public Boolean getHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(Boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public Long getWfipId() {
		return wfipId;
	}

	public void setWfipId(Long wfipId) {
		this.wfipId = wfipId;
	}

	public Boolean getHasWarnings() {
		return hasWarnings;
	}

	public void setHasWarnings(Boolean hasWarnings) {
		this.hasWarnings = hasWarnings;
	}

	public Long getIanusActivityId() {
		return ianusActivityId;
	}

	public void setIanusActivityId(Long ianusActivityId) {
		this.ianusActivityId = ianusActivityId;
	}

	public String getMsName() {
		return msName;
	}

	public void setMsName(String msName) {
		this.msName = msName;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	
	
}
