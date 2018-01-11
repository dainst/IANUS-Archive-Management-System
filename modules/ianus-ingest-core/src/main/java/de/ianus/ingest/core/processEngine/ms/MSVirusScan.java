package de.ianus.ingest.core.processEngine.ms;

import java.io.PrintWriter;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.ClamAVOutput;
import de.ianus.ingest.core.processEngine.ms.utils.ClamAVUtils;


public class MSVirusScan extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			ClamAVOutput cmdOutput = ClamAVUtils.execute(wfip.getAbsolutePath());
			
			PrintWriter out = new PrintWriter(wfip.getLogsFolder() + "/virus_scan_standard_output.txt");
			out.print(cmdOutput.standardOutput);
			out.close();
			
			out = new PrintWriter(wfip.getLogsFolder() + "/virus_scan_error_output.txt");
			out.print(cmdOutput.errorOutput);
			out.close();
			
			this.output.print(cmdOutput.standardOutput);
			this.output.errorPrint(cmdOutput.errorOutput);
			
			if(cmdOutput.exitValue == 0){
				//No virus found.
				transition = execution.getActivity().findOutgoingTransition("virus_scan_ok");
			}else if(cmdOutput.exitValue == 1 || cmdOutput.exitValue == 2){
				//Virus(es) found. || Some error(s) occured.
				output.setHasErrors(true);
				transition = execution.getActivity().findOutgoingTransition("virus_scan_error");
			}
			
		} catch (Exception e) {
			setException(e);
			transition = execution.getActivity().findOutgoingTransition("virus_scan_error");
		} finally {
			finishExecution();
		}
		
	}

}
