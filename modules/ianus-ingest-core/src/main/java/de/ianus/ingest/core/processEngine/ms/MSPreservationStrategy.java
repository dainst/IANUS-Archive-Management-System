package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSPreservationStrategy extends AbstractTask implements ActivityBehavior{

	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {			
			
			this.output.print("TODO no yet implemented");
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sf_to_restructure_files_folders");
			finishExecution();
		}
		
	}
}
