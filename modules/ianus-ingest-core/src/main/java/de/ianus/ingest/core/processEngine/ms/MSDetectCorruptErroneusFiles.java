package de.ianus.ingest.core.processEngine.ms;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.files.FileInstance;

public class MSDetectCorruptErroneusFiles  extends AbstractTask implements ActivityBehavior {
	
	private Integer scannedFiles = 0;
	private Integer corruptFiles = 0;
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		
		try{
			List<FileInstance> list = Services.getInstance().getDaoService().getFileInstanceList(this.wfip);
			if(list != null) for(FileInstance fi : list) {
				File file = new File(this.wfip.getDataFolder() + "/" + fi.getLocation());
				this.scannedFiles++;
				if(file.exists()) {
					if(file.canRead()) {
						// According to https://en.wikipedia.org/wiki/List_of_file_signatures,
						// also container files and archives can be read this way.
						FileInputStream is = new FileInputStream(file);
						byte[] buffer = new byte[100];
						if(is.read(buffer) == -1) {
			                this.output.print("File not readable: " + fi.getLocation());
			                this.corruptFiles++;
			            }
						is.close();
					}else{
						this.output.print("File not accessible: " + fi.getLocation());
						this.corruptFiles++;
					}
				}else{
					this.output.print("File not existant: " + fi.getLocation());
					this.corruptFiles++;
				}
			}
			this.output.print("Scanned files for " + this.scannedFiles + " FileInstances, " + this.corruptFiles + " corrupted.");
		}catch (Exception e){
			this.setException(e);
		}finally {
			transition = execution.getActivity().findOutgoingTransition("detectCorruptErroneusFiles_ok");
			this.finishExecution();
		}
	}
}