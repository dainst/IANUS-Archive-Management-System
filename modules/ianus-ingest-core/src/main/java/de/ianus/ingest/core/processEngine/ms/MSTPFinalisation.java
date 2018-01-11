package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSTPFinalisation extends AbstractTask implements ActivityBehavior{
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		//this.output.print("standart TP finalisation");
		//this.output.errorPrint("error TP finalisation");
		
		try {
			/*
			for(int i=0; i<=10; i++){
				Thread.sleep(500);
				System.out.println(i + "%");
			}*/
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("to_user_report_a");
			finishExecution();
		}
		
	}
}
