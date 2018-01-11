package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

/**
 * This MS should save the zip file from the tmp into a backup server.
 * For the transfer, we will use FTP protocol 
 * 
 * @author jurzua
 *
 */
public class MSSIPBackup  extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			this.output.print("TODOOOOO");
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sip_backup_ok");
			finishExecution();
		}
		
	}

}