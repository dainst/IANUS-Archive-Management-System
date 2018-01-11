package de.ianus.ingest.core.processEngine.ms;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.IANUSActivity;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;


/**
 *  <p>In order to be a microservice, a class must meet two requirements:
 *  the implementation of the class ActivityBehavior and
 *  the extension of the class AbstractTask.
 *  </p>
 *  <p>
 *  The first class is part of the camunda framework. The second class is this class.
 *  This class has two methods: prepareExecution and finishExecution.
 *  <p>-The method prepareExecution is responsible for the initialization of variables like wfip (IP related to the workflow) and the output (ActivityOutput).
 *  In the object output, we can save the logs of the execution, there we can put errors, exceptions and just tracking information.</p>
 *  </p>-The method finishExecution is used to made persistent the change  in the wfip and in the output objects. </p>
 * 
 * 
 * @author jurzua
 *
 */
public class AbstractTask implements ActivityBehavior {
	
	private static final Logger logger = LogManager.getLogger(DAOService.class);
	
	//the information package associated to the 
	protected WorkflowIP wfip;
	protected String wfClass;
	protected ActivityOutput output;
	protected PvmTransition transition;
	protected ActivityExecution execution;
	protected long startExecution;
	private IANUSActivity activity;
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		logger.info("Starting " +  this.getClass().getSimpleName());
		this.startExecution = System.currentTimeMillis();
		this.execution = execution;
		this.prepareExecution();
	}
	
	public void prepareExecution() throws Exception{
		this.transition = null;
		
		//getting the identifier of the ip from the global variables
		Object input0 = execution.getVariable("wfipId");
		String input = input0.toString();
		Long wfip = Long.parseLong(input);
		
		//getting the class of the ip from the global variables
		Object wfClass0 = execution.getVariable("wfClass");
		this.wfClass = wfClass0.toString();
		
		//here we initialize the variable wfip with the current instance of the information package.
		//We get this instance using the DaoService
		if(StringUtils.equals(this.wfClass, TransferP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getTransfer(wfip);
		}else if(StringUtils.equals(this.wfClass, SubmissionIP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getSip(wfip);
		}else if(StringUtils.equals(this.wfClass, ArchivalIP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getAip(wfip);
		}else if(StringUtils.equals(this.wfClass, DisseminationIP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getDip(wfip);
		}else{
			throw new Exception("An object of class " + this.wfClass + " can not be part of a workflow");
		}
		this.output = ActivityOutput.getActivityOutput4JavaTask(execution, wfip);
		this.output.setMsName(this.getClass().getSimpleName());
		
		this.activity = new IANUSActivity((ExecutionEntity)this.execution);
		this.activity.setStartTime(new Date());
		Services.getInstance().getDaoService().saveDBEntry(this.activity);
		this.output.setIanusActivityId(this.activity.getId());
	}
	
	public void finishExecution() throws Exception{
		output.print("Microservice finished.");
		output.setExecutionTime(System.currentTimeMillis() - startExecution);
		output.print("Execution time [ms]=" + output.getExecutionTime());
		Services.getInstance().getDaoService().saveDBEntry(output);
		//TODO how to do this???
		//execution.take(transition);
		
		this.activity.setEndTime(new Date());
		Services.getInstance().getDaoService().saveDBEntry(this.activity);
		
		execution.leaveActivityViaTransition(transition);
		logger.info("Finish " +  this.getClass().getSimpleName());
	}

	/**
	 * This method should be used, when the whole process throws an exception and it can not continue.
	 * @param e
	 * @throws Exception
	 */
	protected void setException(Exception e) throws Exception{
		e.printStackTrace();
		this.output.printException(e);
		this.output.errorPrint("Finished with errors.");
		this.wfip.setState(WorkflowIP.FINISHED_ERRORS);
		Services.getInstance().getDaoService().saveDBEntry(wfip);
	}
	
	/**
	 * This method does not finalize this microservice, but add a new exception to the standard error output.
	 * @param e
	 * @throws Exception
	 */
	public void addException(Exception e) throws Exception{
		e.printStackTrace();
		this.output.errorPrint("/---------------------------------------------------------------------/\n");
		this.output.printException(e);
		this.output.errorPrint("/---------------------------------------------------------------------/\n");
	}

}
