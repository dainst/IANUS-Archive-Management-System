package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSTest extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		
		try {
			
			
			for(int i=0; i<=5; i++){
				Thread.sleep(5000);
				System.out.println(i + "%");
			}
			transition = execution.getActivity().findOutgoingTransition("test_ok");
			
		} catch (Exception e) {
			setException(e);
		} finally {
			finishExecution();
		}
	}
}
