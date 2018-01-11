package de.ianus.ingest.core.processEngine.execution;

import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;

public class AbstractExecution implements ExecutionListener {

	protected WorkflowIP wfip;
	protected String wfClass;
	//protected ActivityOutput output;
	protected PvmTransition transition;
	protected DelegateExecution execution;
	protected long startExecution;
	//private IANUSActivity activity;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		this.startExecution = System.currentTimeMillis();
		this.execution = execution;
		this.prepareExecution();
	}
	
	public void prepareExecution() throws Exception{
		this.transition = null;
		
		//getting the identifier of the ip
		Object input0 = execution.getVariable("wfipId");
		String input = input0.toString();
		Long wfip = Long.parseLong(input);
		
		//getting the class of the ip
		Object wfClass0 = execution.getVariable("wfClass");
		this.wfClass = wfClass0.toString();
		
		if(StringUtils.equals(this.wfClass, TransferP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getTransfer(wfip);
		}else if(StringUtils.equals(this.wfClass, SubmissionIP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getSip(wfip);
		}else if(StringUtils.equals(this.wfClass, DisseminationIP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getDip(wfip);
		}else if(StringUtils.equals(this.wfClass, ArchivalIP.class.getName())){
			this.wfip = Services.getInstance().getDaoService().getAip(wfip);
		}else{
			throw new Exception("An object of class " + this.wfClass + " can not be part of a workflow");
		}
		//this.output = ActivityOutput.getActivityOutput4JavaTask(execution, wfip);
		/*
		this.activity = new IANUSActivity((ExecutionEntity)this.execution);
		this.activity.setStartTime(new Date());
		Services.getInstance().getDaoService().saveDBEntry(this.activity);
		*/
	}
	
	public void finishExecution() throws Exception{
		//output.print("Microservice finished.");
		//long diff = System.currentTimeMillis() - startExecution;
		//output.print("Execution time [ms]=" + diff);
		//Services.getInstance().getDaoService().saveDBEntry(output);
		//TODO how to do this???
		//execution.take(transition);
		/*
		this.activity.setEndTime(new Date());
		Services.getInstance().getDaoService().saveDBEntry(this.activity);
		*/
	}

	protected void setException(Exception e) throws Exception{
		/*
		this.output.printException(e);
		this.output.errorPrint("Finished with errors.");
		this.wfip.setState(WorkflowIP.FINISHED_ERRORS);
		Services.getInstance().getDaoService().saveDBEntry(wfip);
		*/
	}
}
