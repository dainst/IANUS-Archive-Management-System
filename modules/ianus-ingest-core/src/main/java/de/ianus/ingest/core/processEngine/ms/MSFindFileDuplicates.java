package de.ianus.ingest.core.processEngine.ms;

import java.util.List;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.CheckFileContentUtils;
import de.ianus.ingest.core.processEngine.ms.utils.CheckFileContentUtils.ResultSetDuplicate;

public class MSFindFileDuplicates extends AbstractTask implements ActivityBehavior{
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 

		try {
			ResultSetDuplicate rs =  CheckFileContentUtils.scanDuplicateFiles(this.wfip.getDataFolder());
			
			if(!rs.rsFileContentComparison.isEmpty()){
				
				this.output.print("These files have duplicate content but different file names:");
				
				for(List<String> duplicateFile: rs.rsFileContentComparison.values()){
					if(duplicateFile.size()>1){
						for(String path : duplicateFile){
							String relativePath = path.replace(this.wfip.getAbsolutePath(), "...");
							this.output.print(relativePath);
						}
						this.output.print("\n");
					}	
				}
			}else{
				this.output.print("none");
			}		
		
			if(!rs.rsFileNameWithExtensionComparison.isEmpty()){
				
				this.output.print("\nThese files have duplicate file names (inc. extension) but different locations:");
				
				for(List<String> duplicateFile: rs.rsFileNameWithExtensionComparison.values()){
					if(duplicateFile.size()>1){
						for(String path : duplicateFile){
							String relativePath = path.replace(this.wfip.getAbsolutePath(), "...");
							this.output.print(relativePath);
						}
						this.output.print("\n");
					}	
				}
			}else{
				this.output.print("none");
			}
			
			if(!rs.rsFileNameWithoutExtensionComparison.isEmpty()){
				
				this.output.print("\nThese files have duplicate file names but different file format extensions:");
				
				for(List<String> duplicateFile: rs.rsFileNameWithoutExtensionComparison.values()){
					if(duplicateFile.size()>1){
						for(String path : duplicateFile){
							String relativePath = path.replace(this.wfip.getAbsolutePath(), "...");
							this.output.print(relativePath);
						}
						this.output.print("\n");
					}
				}
			}else{
				this.output.print("none");
			}
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("findFileDuplicates_ok");
			finishExecution();
		}

	}

}
