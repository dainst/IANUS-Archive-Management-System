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
import de.ianus.ingest.core.processEngine.ms.fits.BoolString;
import de.ianus.ingest.core.processEngine.ms.utils.TechnicalMetadataExtractionProcessor;

public class MSValidateAndIdentifyFileFormat2  extends AbstractTask implements ActivityBehavior{

	private static final Logger logger = LogManager.getLogger(MSValidateAndIdentifyFileFormat2.class);
	
	private DAOService dao;
	
	// the values to go for in the FITS output
	private FileInstance.Status valid = null;
	private FileInstance.Status wellFormed = null;
	private String puid = null;
	private String mimeType = null;
	
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try{
			this.dao = Services.getInstance().getDaoService();
			
			// extract technical metadata beforehand
			TechnicalMetadataExtractionProcessor proc = new TechnicalMetadataExtractionProcessor(this.wfip, this.output);
			proc.run();
			
			List<FileInstance> list = this.dao.getFileInstanceListWithTechMd(this.wfip);
			if(list != null) for(FileInstance fi : list) {
				
				this.valid = null;
				this.wellFormed = null;
				this.puid = null;
				this.mimeType = null;
				
				String msg = null;
				
				List<FileInstanceProperty> props = this.dao.getFileInstancePropertyList(fi);
				if(props != null) for(FileInstanceProperty prop : props) {
					FileInstanceProperty.ValueStatus status =  FileInstanceProperty.ValueStatus.getByString(prop.getStatus());
					switch(prop.getName()) {
					case "valid": 
						if(this.valid == null && status == FileInstanceProperty.ValueStatus.OK) {
							this.valid = (BoolString.eval(prop.getValue())) ? FileInstance.Status.OK : FileInstance.Status.NO;
						}else{
							this.valid = FileInstance.Status.CONFLICT;
							this.output.print("Validity Conflict: " + fi.getLocation());
						}
						break;
					case "well-formed":
						if(this.wellFormed == null && status == FileInstanceProperty.ValueStatus.OK) {
							this.wellFormed = (BoolString.eval(prop.getValue())) ? FileInstance.Status.OK : FileInstance.Status.NO;
						}else{
							this.wellFormed = FileInstance.Status.CONFLICT;
							this.output.print("WellFormed Conflict: " + fi.getLocation());
						}
						break;
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
						break;					}
				}
				
				if(msg != null) this.output.print("mimeType Conflict: " + fi.getLocation() + msg);
				fi.setValid(this.valid);
				fi.setWellFormed(this.wellFormed);
				fi.setPuid(this.puid);
				fi.setMimeType(this.mimeType);
				this.dao.saveDBEntry(fi);
				
				if(this.valid == FileInstance.Status.NO) 
					this.output.print("Invalid File: " + fi.getLocation());
				if(this.wellFormed == FileInstance.Status.NO)
					this.output.print("Malformed File: " + fi.getLocation());
			}
		}catch (Exception e){
			this.setException(e);
		}finally {
			transition = execution.getActivity().findOutgoingTransition("sf_validate_file_format_ok2");
			this.finishExecution();
		}
	}
}
