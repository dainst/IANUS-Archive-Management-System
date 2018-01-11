package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.bo.files.FileInstanceProperty;
import de.ianus.ingest.core.bo.files.FileInstanceProperty.ValueStatus;
import de.ianus.ingest.core.bo.files.ObjectEventLink;
import de.ianus.ingest.core.processEngine.ms.fits.ExternalIdentifier;
import de.ianus.ingest.core.processEngine.ms.fits.Fits;
import de.ianus.ingest.core.processEngine.ms.fits.FitsIdentity;
import de.ianus.ingest.core.processEngine.ms.fits.FitsMetadataElement;
import de.ianus.ingest.core.processEngine.ms.fits.FitsOutput;
import de.ianus.ingest.core.processEngine.ms.fits.FormatVersion;



public class TechnicalMetadataExtractionProcessor {

	
	
	private WorkflowIP wfip;
	private DAOService dao;
	private Fits fits;
	private Event event;
	private ActivityOutput output;
	
	private static Logger logger = Logger.getLogger(TechnicalMetadataExtractionProcessor.class);
	
	
	public TechnicalMetadataExtractionProcessor(WorkflowIP wfip, ActivityOutput output, Event event) {
		this.wfip = wfip;
		this.dao = Services.getInstance().getDaoService();
		this.fits = new Fits();
		this.event = event;
		this.output = output;
	}
	
	public TechnicalMetadataExtractionProcessor(WorkflowIP wfip, ActivityOutput output) {
		this.wfip = wfip;
		this.dao = Services.getInstance().getDaoService();
		this.fits = new Fits();
		// eg during DROID md extraction, no PREMIS events shall be registered:
		this.event = null;	// if the event is not passed or empty, no EventObjectLinks will be created
		this.output = output;
	}
	
	
	
	public void run() throws Exception {
		List<FileInstance> list = this.dao.getFileInstanceListWithoutTechMd(this.wfip);
		int i = 0;
		int max = 4000;
		if(list != null && list.size() <= max) {
			for(FileInstance fi : list) {
				if(i > max) {
					if(this.output != null)
						this.output.print("####### Aborting metadata extraction due to huge archive size! (over " + max + " files) ########");
					break;
				}
				this.dao.deleteFileInstanceProperties(fi);
				this.storeFileInfo(fi);
				i++;
			}
		}else if(list.size() > max) {
			if(this.output != null)
				this.output.print("####### Aborting metadata extraction due to huge archive size! (over " + max + " files) ########");
		}
	}
	
	
	public void storeFileInfo(FileInstance fi) throws Exception {
		String location = fi.getLocation();
		String path = this.wfip.getDataFolder() + "/" + location;
		Long fid = fi.getId();
		File filepath = new File(path);
		
		logger.info("FITS examining " + path);
		if(!filepath.exists()) {
			this.output.errorPrint("File not found! (Fileinstance of non-existing file. ID: " + fi.getId() + ")");
			if(this.event != null) {
				this.event.setOutcome(Event.Outcome.failure);
		        try {
					dao.saveDBEntry(event);
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		FitsOutput fitsOutput = this.fits.examine(filepath);
		
		// flip the switch that techMD will not be extracted for this FileInstance a second time
		fi.setHasTechMd(true);
		fi.save(this.dao);
		
		if(this.event != null) {
			// create the EventObjectLink
			ObjectEventLink oel = new ObjectEventLink(fi, this.event, ObjectEventLink.ObjectRole.input);
			this.dao.saveDBEntry(oel);
		}
		
		// identification section
		Set<String> puids = new HashSet<String>();
		Boolean puidConflict = false;
		Boolean mimeConflict = false;
		Set<String> mimes = new HashSet<String>();
		List<FitsIdentity> identities = fitsOutput.getIdentities();
		if(identities != null) for(FitsIdentity identity : identities) {
			List<ExternalIdentifier> identifiers = identity.getExternalIdentifiers();
			if(identifiers != null) for(ExternalIdentifier identifier : identifiers) {
				if(identifier.getName().equals("puid")) {
					puids.add(identifier.getValue());
				}
			}
			String mimeType = identity.getMimetype(); 
			if(mimeType != null && !mimeType.equals("")) {
				mimes.add(mimeType);
			}
		}
		if(puids.size() > 1) puidConflict = true;
		if(mimes.size() > 1) mimeConflict = true;
		
		
		if(identities != null) for(FitsIdentity identity : identities) {
			List<ExternalIdentifier> identifiers = identity.getExternalIdentifiers();
			if(identifiers != null) for(ExternalIdentifier identifier : identifiers) {
				// TODO: not storing some info (date, note) returned by identifier.getToolInfo(), 
					// maybe do more string concatenation?
					// identifier.getToolInfo().[date,name,note,version]
				// TODO: not storing identity.getReportingTools - as string concatenation? 
					// identifier.getToolInfo().getName() is probably just the first from the list of tool reporting this identifier...  
				FileInstanceProperty prop1 = new FileInstanceProperty(fid, 
						identifier.getName(), identifier.getValue(),
						identifier.getToolInfo().getName(), identifier.getToolInfo().getVersion(),
						(puidConflict) ? ValueStatus.CONFLICT : ValueStatus.OK);
				prop1.save(this.dao);
			}
			
			String mimeType = identity.getMimetype(); 
			if(mimeType != null && !mimeType.equals("")) {
				FileInstanceProperty prop2 = new FileInstanceProperty(fid, 
						"mimeType", mimeType,
						identity.getToolName(), identity.getToolVersion(),
						(mimeConflict) ? ValueStatus.CONFLICT : ValueStatus.OK);
				prop2.save(this.dao);
			}
			
			String formatName = identity.getFormat();
			if(formatName != null && !formatName.equals("")) {
				List<FormatVersion> formatVersions = identity.getFormatVersions();
				String versionString = "";
				int i = 0;
				if(formatVersions != null) for(FormatVersion fv : formatVersions) {
					if(i == 0) versionString = ""; else versionString += ", ";
					versionString += fv.getValue();
					i++;
				}
				if(!versionString.equals("")) formatName += " v: " + versionString;
				
				FileInstanceProperty prop3 = new FileInstanceProperty(fid, 
						"formatName", formatName,
						identity.getToolName(), identity.getToolVersion(),	// FITS
						ValueStatus.OK);
				prop3.save(this.dao);
			}
			
			
		}
		
		// fileinfo section
		List<FitsMetadataElement> elements = fitsOutput.getFileInfoElements();
		if(elements != null) for(FitsMetadataElement element : elements) {
			FileInstanceProperty prop = new FileInstanceProperty(fid, 
					element.getName(), element.getValue(),
					element.getReportingToolName(), element.getReportingToolVersion(),
					ValueStatus.getByString(element.getStatus()));
			prop.save(this.dao);
		}
		
		// filestatus section
		elements = fitsOutput.getFileStatusElements();
		if(elements != null) for(FitsMetadataElement element : elements) {
			FileInstanceProperty prop = new FileInstanceProperty(fid, 
					element.getName(), element.getValue(),
					element.getReportingToolName(), element.getReportingToolVersion(),
					ValueStatus.getByString(element.getStatus()));
			prop.save(this.dao);
		}
		
		// technical md section
		// techMdType will be one of [image,text,audio,document,...], null(?) if not a supported file format
		String techMdType = fitsOutput.getTechMetadataType();
		if(techMdType != null) {
			FileInstanceProperty prop = new FileInstanceProperty(fid, 
				"technicalMedataDataType", techMdType,
				"FITS", fitsOutput.getFitsVersion(),
				ValueStatus.OK);
			prop.save(this.dao);
		}
		
		elements = fitsOutput.getTechMetadataElements();
		if(elements != null) for(FitsMetadataElement element : elements) {
			FileInstanceProperty prop = new FileInstanceProperty(fid, 
					element.getName(), element.getValue(),
					element.getReportingToolName(), element.getReportingToolVersion(),
					ValueStatus.getByString(element.getStatus()));
			prop.save(this.dao);
		}
		
		
	}
	
	
	

}
