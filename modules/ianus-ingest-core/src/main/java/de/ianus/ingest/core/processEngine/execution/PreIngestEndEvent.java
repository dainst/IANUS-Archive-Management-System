package de.ianus.ingest.core.processEngine.execution;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;

public class PreIngestEndEvent extends AbstractExecution{
	private static Logger logger = Logger.getLogger(PreIngestEndEvent.class);
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		super.notify(execution); 
		
		try {
			this.wfip.setState(WorkflowIP.FINISHED);
			Services.getInstance().getDaoService().saveDBEntry(this.wfip);
			
		} catch (Exception e) {
			setException(e);
		} finally {
			finishExecution();
		}
		
	}
	
}