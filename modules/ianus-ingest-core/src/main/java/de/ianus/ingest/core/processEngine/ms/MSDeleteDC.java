package de.ianus.ingest.core.processEngine.ms;

import java.io.File;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSDeleteDC extends AbstractTask implements ActivityBehavior{

	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {			
			File file = new File(this.wfip.getAbsolutePath());
			file.delete();
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("to_user_report_b");
			finishExecution();
		}
		
	}
}
