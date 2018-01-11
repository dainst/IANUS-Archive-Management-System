package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.processEngine.ms.utils.ZipSIPUtils;

/**
 * This class will zip the sip and save into the (new folder) tmp.
 * @author jurzua
 *
 */
public class MSSIPCompression extends  AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {

				ZipSIPUtils zu = new ZipSIPUtils((SubmissionIP) this.wfip, this.output);
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sip_compression_ok");
			finishExecution();
		}
		
	}

}