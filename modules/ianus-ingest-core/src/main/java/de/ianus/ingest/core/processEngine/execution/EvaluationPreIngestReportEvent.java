package de.ianus.ingest.core.processEngine.execution;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;

public class EvaluationPreIngestReportEvent extends AbstractExecution{
	private static Logger logger = Logger.getLogger(EvaluationPreIngestReportEvent.class);
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		super.notify(execution); 
		
		try {
			
			System.out.println("##### EvaluationPreIngestReportEvent");
			Object obj = execution.getVariable("dataMetadataState");
			System.out.println(obj);
			//TODO
			//this.wfip.setState(WorkflowIP.FINISHED);
			//Services.getInstance().getDaoService().saveDBEntry(this.wfip);
			
		} catch (Exception e) {
			setException(e);
		} finally {
			finishExecution();
		}
		
	}
	
}