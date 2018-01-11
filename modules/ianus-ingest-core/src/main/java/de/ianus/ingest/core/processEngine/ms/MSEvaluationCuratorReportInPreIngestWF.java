package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.PreIngestReport;

public class MSEvaluationCuratorReportInPreIngestWF   extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			this.output.print("Variable dataMetadataState");
			this.output.print("\tAfter=" + execution.getVariable("dataMetadataState"));
			
			PreIngestReport report = Services.getInstance().getDaoService().getPreIngestReport(this.wfip.getId());
			execution.setVariable("dataMetadataState", (report.isReporOk()) ? PreIngestReport.State.OK : PreIngestReport.State.REVISION);
			
			this.output.print("\tBefore=" + execution.getVariable("dataMetadataState"));
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("evaluationReport_ok");
			finishExecution();
		}
	}
}