package de.ianus.ingest.core.processEngine.ms;




import java.util.List;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.CheckEncodingUtils;



public class MSCheckEncoding extends AbstractTask implements ActivityBehavior{
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 

		try {
			List<String> rs = CheckEncodingUtils.scanFileFolderNameEncoding(this.wfip.getDataFolder());
			if(!rs.isEmpty()){
				this.output.print("These file and folder names are not in UTF8 format:");
				for(String path : rs){
					String relativePath = path.replace(this.wfip.getAbsolutePath(), "");
					this.output.print(relativePath);
				}
			}else{
				this.output.print("none");
			}
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("checkEncoding_ok");
			finishExecution();
		}
	}

}
