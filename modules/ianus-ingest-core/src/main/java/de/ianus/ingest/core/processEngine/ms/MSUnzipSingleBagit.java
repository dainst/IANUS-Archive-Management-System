package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSUnzipSingleBagit  extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			//ZipUtils.unZipBagIt((SubmissionIP)this.wfip, output);
			//transition = execution.getActivity().findOutgoingTransition("unzip_single_ok");
			
			
			/*
			this.wfip.setState(WorkflowIP.FINISHED);
			Services.getInstance().getDaoService().saveDBEntry(this.wfip);
			
			transition = execution.getActivity().findOutgoingTransition("finish_ok");
			
			*/
			
		} catch (Exception e) {
			//transition = execution.getActivity().findOutgoingTransition("unzip_single_error");
			setException(e);
		} finally {
			finishExecution();
		}
		
	}
	
}