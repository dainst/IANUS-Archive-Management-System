package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.xml.XMLService;

public class MSFinalizeAIP extends AbstractTask implements ActivityBehavior{

	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {			
			
			this.output.print("Generating METS file.");
			XMLService xmlService = new XMLService(this.wfip);
			xmlService.createMETS();
			xmlService.createPREMIS();
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sf_finalize_aip_ok");
			finishExecution();
		}
		
	}
}
