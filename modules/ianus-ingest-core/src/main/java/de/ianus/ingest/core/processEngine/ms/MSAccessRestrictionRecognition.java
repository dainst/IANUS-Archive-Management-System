package de.ianus.ingest.core.processEngine.ms;

import java.util.Map;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.AccessRestrictionRecognitionUtils;


/**
 * This class checks all directory files format. It will check one by one, if there are password protected files, it will return the file name with comment "PROTECTED". if not, 
 * the comment should be "FREE" and if, there are files which are not considered at the movement, in that case - it will return "UNABLE TO CHECK".
 * In this micro-service, we are considering (PDF, Doc[.doc, .docx], Excel[.xls, .xlsx] and open office (.odt, .ods)) file format. 
 * 
 * @author MR
 *
 */
public class MSAccessRestrictionRecognition extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			AccessRestrictionRecognitionUtils restrictedFiles = new AccessRestrictionRecognitionUtils(this.wfip.getDataFolder(), this.output);
			Map <String, String> allValueList = restrictedFiles.scanFiles();
			
			if(!allValueList.isEmpty()){
				
				this.output.print("These files have access restrictions:");
				
				for(String restrictedFile: allValueList.keySet()){
					
					String fileName = restrictedFile.toString();
					String relativePath = fileName.replace(this.wfip.getAbsolutePath(), "");
					String result = allValueList.get(restrictedFile).toString();
				
					this.output.print(relativePath + " -> " + result);
				}	
				
			}else{
				this.output.print("Found no restricted files.");
			}	
		
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("accessRestrictionRecognition_ok");
			finishExecution();
		}
	}
}