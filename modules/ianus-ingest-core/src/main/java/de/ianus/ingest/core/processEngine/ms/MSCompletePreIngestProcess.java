package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.xml.XMLService;

public class MSCompletePreIngestProcess extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			this.output.print("Generating METS file.");
			XMLService mets = new XMLService(this.wfip);
			mets.createMETS();
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("MSCompletePreIngestProcess_ok");
			finishExecution();
		}
	}
}