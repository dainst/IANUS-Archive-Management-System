/**
 * 
 */
package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.xml.XMLService;

/**
 * @author hendrik
 *
 */
public class MSCreateMETS extends AbstractTask implements ActivityBehavior {
	
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		
		XMLService xmlService = new XMLService(this.wfip);
		
		try {
			this.output.print("Creating METS file");
			xmlService.createMETS();
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("createMETS_ok");
			finishExecution();
		}
	}
}
