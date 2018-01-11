/**
 * 
 */
package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.WebDerivativesGenerator;


/**
 * @author Hendrik Schmeer
 *
 */
public class MSCreateWebDerivatives extends AbstractTask implements ActivityBehavior {
	
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		
		WebDerivativesGenerator generator = new WebDerivativesGenerator(
				this.output,
				this.wfip.getDataFolder(),
				this.wfip.getAbsolutePath() + "/web_derivatives");
			
		try {
			this.output.print("Creating thumbs and previews...");
			generator.recurse(null);
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("createThumbs_ok");
			finishExecution();
		}
	}
	
	
	
}
