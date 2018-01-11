package de.ianus.ingest.core.processEngine.ms;

import java.io.File;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

/**
 * This Microservice replace all spaces with underscore in folder and files name in the data folder of an information package. 
 * If there are many spaces together, they will be replaced by an unique underscore.
 * 
 * @author jurzua
 *
 */
public class MSReplaceSpacesWithUnderscore extends AbstractTask implements ActivityBehavior{

	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			File dataFolder = new File(this.wfip.getDataFolder());
			replaceSpacesWithUnderscore(dataFolder);
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("replaceSpaces_ok");
			finishExecution();
		}	
	}
	
	public static void replaceSpacesWithUnderscore(File root){
		
		for(File child : root.listFiles()){
			if(child.isDirectory()){
				replaceSpacesWithUnderscore(child);
			}
			String newFileName = child.getName().replaceAll("\\s", "_");
			child.renameTo(new File(child.getParent() + "/" + newFileName));
		}
		
	}
}