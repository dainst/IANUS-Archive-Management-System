package de.ianus.ingest.core.processEngine.ms.utils;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.TaskService;

/**
 * <p>
 * This class is responsible for completing a task. The execution is done by a thread (a runnable class).
 * We use a thread, because when we complete a task (and this task is followed by a Service Task), we must start a Service Task. 
 * A service task is particular kind of task that is executed automatically on the server side. Its execution could take long time. 
 * For this reason and in order to avoid that a user wait for the execution of these tasks, we delegate this job to a thread.
 * </p>
 * 
 * @author jurzua
 *
 */
public class MSThread implements Runnable {
	
	private String taskId;
	private Map<String, Object> taskVariables;
	private TaskService taskService;
	
	public MSThread(TaskService taskService, String taskId, Map<String, Object> taskVariables){
		this.taskService = taskService;
		this.taskId = taskId;
		this.taskVariables = (taskVariables == null) ? new HashMap<String, Object>() : new HashMap<String, Object>(taskVariables) ;
		this.taskVariables.put("taskUser", "jurzua");
	}
	
	@Override
    public void run() {
    	this.taskService.complete(taskId, this.taskVariables);
    }
}