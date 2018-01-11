package de.ianus.ingest.core.processEngine.ms;


import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.bo.files.FileInstanceProperty;
import de.ianus.ingest.core.processEngine.ms.utils.TechnicalMetadataExtractionProcessor;

/**
 * @author hendrik
 *
 */

public class MSFileFormatIdentification extends AbstractTask implements ActivityBehavior {
	
	
	private static final Logger logger = LogManager.getLogger(MSFileFormatIdentification.class);
	
	private DAOService dao;
	
	// the values to go for in the FITS output
	private String puid = null;
	private String mimeType = null;
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		this.dao = Services.getInstance().getDaoService();
				
		try {
			
			// extract technical metadata beforehand
			TechnicalMetadataExtractionProcessor proc = new TechnicalMetadataExtractionProcessor(this.wfip, this.output);
			proc.run();
			
			List<FileInstance> list = this.dao.getFileInstanceListWithTechMd(this.wfip);
			if(list != null) for(FileInstance fi : list) {
				this.puid = null;
				this.mimeType = null;
				
				String msg = null;
				List<FileInstanceProperty> props = this.dao.getFileInstancePropertyList(fi);
				if(props != null) for(FileInstanceProperty prop : props) {
					FileInstanceProperty.ValueStatus status =  FileInstanceProperty.ValueStatus.getByString(prop.getStatus());
					switch(prop.getName()) {
					case "puid": 
						if(this.puid != null && status == FileInstanceProperty.ValueStatus.OK
						&& this.puid.equals(prop.getValue())) continue;
						
						if(this.puid == null && status == FileInstanceProperty.ValueStatus.OK) {
							this.puid = prop.getValue();
						}else{
							this.puid = FileInstance.Status.CONFLICT.name();
							this.output.print("PUID Conflict: " + fi.getLocation());
						}
						break;
					case "mimeType": 
						if(this.mimeType != null && status == FileInstanceProperty.ValueStatus.OK 
						&& this.mimeType.equals(prop.getValue())) continue;
						
						if(this.mimeType == null && status == FileInstanceProperty.ValueStatus.OK) {
							this.mimeType = prop.getValue();
						}else{
							this.mimeType = FileInstance.Status.CONFLICT.name();
							if(msg == null) msg = "";
							msg += "\n" + prop.getValue();
						}
						break;
					}
				}
				
				if(msg != null) this.output.print("mimeType Conflict: " + fi.getLocation() + msg); 
				fi.setPuid(this.puid);
				fi.setMimeType(this.mimeType);
				this.dao.saveDBEntry(fi);
			}
		}catch (Exception e){
			this.setException(e);
		}finally {
			transition = execution.getActivity().findOutgoingTransition("fileFormatIdentification_ok");
			this.finishExecution();
		}
	}
}
