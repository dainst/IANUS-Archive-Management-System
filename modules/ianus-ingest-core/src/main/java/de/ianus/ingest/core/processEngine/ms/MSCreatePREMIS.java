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
public class MSCreatePREMIS extends AbstractTask implements ActivityBehavior {
	
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		
		XMLService xmlService = new XMLService(this.wfip);
		
		try {
			this.output.print("Creating PREMIS file");
			xmlService.createPREMIS();
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("createPREMIS_ok");
			finishExecution();
		}
	}
}
