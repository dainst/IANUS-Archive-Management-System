package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSUnzipMultipleBagit   extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			output.print("MSUnzipMultipleBagit!!!!");
		} catch (Exception e) {
			setException(e);
			//transition = execution.getActivity().findOutgoingTransition("virus_scan_error");
		} finally {
			transition = execution.getActivity().findOutgoingTransition("unzip_multiple_ok");
			finishExecution();
		}
		
	}

}