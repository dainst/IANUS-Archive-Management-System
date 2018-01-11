package de.ianus.ingest.core.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;


/**
 * 
 * This class represents an activity. An activity can be either a userTask or a serviceTask.
 * This class is used in the GUI to deploy all relevant attributes related to all kind of activities.
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name = "IANUSActivity")
public class IANUSActivity  extends DBEntry implements Serializable{
	private static final long serialVersionUID = -2651791111233466942L;
	private static String ACT_SERVICE_TASK = "serviceTask";
	private static String ACT_USER_TASK = "userTask";
	
	
	public IANUSActivity(){}
	
	/**
	 * If this class represents a serviceTask, this constructor should be call.
	 * In this case, the IanusActivity has no task identifier, 
	 * because the class ExecutionEntity does not consider this attribute.
	 * @param execution
	 */
	public IANUSActivity(ExecutionEntity execution){
		this.taskId = null;
		this.executionId = execution.getId();
		this.type = ACT_SERVICE_TASK;
		this.assignee = null;
		this.taskDefinitionKey = null;
		this.name = execution.getCurrentActivityName();
		this.processDefinitionId = execution.getProcessDefinitionId();
		this.sequenceCounter = execution.getSequenceCounter();
		this.startTime = null;
		this.endTime = null;
		this.processInstanceId = execution.getProcessInstanceId();
	}
	
	/**
	 * If this class represents a userTask, this constructor should be call.
	 * @param task
	 * @param executionEnd
	 */
	public IANUSActivity(TaskEntity task, Date executionEnd){
		this.priority = task.getPriority();
		this.taskId = task.getId();
		this.executionId = task.getExecutionId();
		this.type = ACT_USER_TASK;
		this.assignee = task.getAssignee();
		this.taskDefinitionKey = task.getTaskDefinitionKey();
		this.name = task.getName();
		this.processDefinitionId = task.getProcessDefinitionId();
		this.sequenceCounter = null;
		this.startTime = task.getCreateTime();
		this.endTime = executionEnd;
		this.processInstanceId = task.getProcessInstanceId();
	}
	
	public IANUSActivity(HistoricActivityInstance activity, Date endTime){
		this.taskId = activity.getTaskId();
		this.executionId = activity.getExecutionId();
		this.type = ACT_USER_TASK;
		this.assignee = activity.getAssignee();
		//this.taskDefinitionKey = activity.getTaskDefinitionKey();
		this.name = activity.getActivityName();
		this.processDefinitionId = activity.getProcessDefinitionId();
		this.sequenceCounter = null;
		this.startTime = activity.getStartTime();
		this.endTime = endTime;
		this.processInstanceId = activity.getProcessInstanceId();
		this.taskDefinitionKey = activity.getTaskId();
	}

	
	@Column(name="assignee")
	private String assignee;
	
	@Column(name="processInstanceId")
	private String processInstanceId;
	
	@Column(name="taskDefinitionKey")
	private String taskDefinitionKey;
	
	@Column(name="sequenceCounter")
	private Long sequenceCounter;
	
	@Column(name="priority")
	private Integer priority;
	
	@Column(name="type")
	private String type;
	
	@Column(name="taskId")
	private String taskId;
	
	@Column(name="processDefinitionId")
	private String processDefinitionId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="startTime")
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="endTime")
	private Date endTime;
	
	@Column(name="executionId")
	private String executionId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="userComment", columnDefinition = "TEXT")
	private String userComment;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public Long getSequenceCounter() {
		return sequenceCounter;
	}

	public void setSequenceCounter(Long sequenceCounter) {
		this.sequenceCounter = sequenceCounter;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}
	
	public String getActivityType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	@Override
	public String toString(){
		return "IANUSActivity [name="+ this.name +", taskId=" + this.taskDefinitionKey + ", processInstId=" + this.processInstanceId + " (" + this.startTime +  "-" + this.endTime + ")]";
	}
}
