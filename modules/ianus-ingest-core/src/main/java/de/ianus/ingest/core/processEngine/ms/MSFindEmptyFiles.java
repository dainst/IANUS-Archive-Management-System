package de.ianus.ingest.core.processEngine.ms;

import java.io.File;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class MSFindEmptyFiles extends AbstractTask implements ActivityBehavior{

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			File file = new File(this.wfip.getDataFolder());
			
			if(!file.exists() || file.length() == 0){
				
				this.output.print("There are no empty files (i.e. with file sice 0 KB) and folders within this collection.");
				
			}else{
				
				this.output.print("These files and folders are empty, i.e. contain no files or have size 0 KB:");
				
				checkEmptyFiles(file);
			}
			
//			checkEmptyFiles(new File(this.wfip.getDataFolder()));
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("findEmptyFiles_ok");
			finishExecution();
		}
	}
	
	public void checkEmptyFiles(File file) throws Exception{
		
		for(File child : file.listFiles()){
			if(child.length() == 0){
				String fileRelativePath = child.getAbsolutePath().replace(this.wfip.getAbsolutePath(), "");
				this.output.print(fileRelativePath);
			}else if(child.isDirectory()){
				checkEmptyFiles(child);
			}
		}
	}
	
}