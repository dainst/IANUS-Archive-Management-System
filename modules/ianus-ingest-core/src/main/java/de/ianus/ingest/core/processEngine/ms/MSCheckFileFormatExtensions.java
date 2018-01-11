package de.ianus.ingest.core.processEngine.ms;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.CheckFileFormatExtensionsUtils;


/**
 * This class checks all directory files extension. It will check one by one, then returns 
 * the file relative path with relative comment "ACCEPTED / PREFERED". 
 * there are files which are not considered at the movement, in that case - 
 * it will return the comment "NOT ACCEPTED".
 * In this micro-service, we are considering list of file format that are listed in the Utils class. 
 * 
 * @author MR
 *
 */
public class MSCheckFileFormatExtensions extends AbstractTask implements ActivityBehavior{
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			CheckFileFormatExtensionsUtils getFiles = new CheckFileFormatExtensionsUtils(this.wfip.getDataFolder());
			
			Map<String, List<String>> valueList = getFiles.scanFileList();
			
			if(!valueList.isEmpty()){
				
				//this.output.print("There are files, that needs to check the format extensions");
					
					for(String fileList : valueList.keySet()){
						
						String key = fileList.toString();
						
						//System.out.println(key);
						this.output.print(key);
							
						for(String value : valueList.get(key)){
							
							String fileName = value.toString();
							
							String relativePath = fileName.replace(this.wfip.getAbsolutePath(), "");
							
							//System.out.println(relativePath);
							this.output.print(relativePath);
							
						}
						//System.out.println();	
						this.output.print("\n");
					}
					
					
				}
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("checkFileFormatExtension_ok");
			finishExecution();
		}
		
	}

}
