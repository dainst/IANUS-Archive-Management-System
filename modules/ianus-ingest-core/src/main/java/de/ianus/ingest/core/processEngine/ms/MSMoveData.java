package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSMoveData  extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			output.print("MSMoveData!!!!");
			transition = execution.getActivity().findOutgoingTransition("move_data_ok");
			
		} catch (Exception e) {
			setException(e);
			//transition = execution.getActivity().findOutgoingTransition("virus_scan_error");
		} finally {
			finishExecution();
		}
		
	}

}