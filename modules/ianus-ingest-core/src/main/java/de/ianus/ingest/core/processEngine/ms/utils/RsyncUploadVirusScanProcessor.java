package de.ianus.ingest.core.processEngine.ms.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.bo.RsyncUpload;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;

public class RsyncUploadVirusScanProcessor implements Runnable {
	
	private DAOService daoService;
	private Long uploadId;
	private Long wfipId;
	private Class<?> wfipType;
	
	private static final Logger logger = LogManager.getLogger(RsyncUploadVirusScanProcessor.class);
	
	
	/**
	 * This constructor collects all the necessary services and objects for the processing of the bagit file.
	 *
	 * @param daoService: this service is used to update in the DB the class BagitFile.
	 * @param bagitFileId: object that indicates progress of the checking process.
	 * 
	 */
	
	public RsyncUploadVirusScanProcessor(DAOService daoService, Long uploadId, Long wfipId, Class<?> wfipType) {
		this.daoService = daoService;
		this.uploadId = uploadId;
		this.wfipId = wfipId;
		this.wfipType = wfipType;
	}
	
	
	@Override
	public void run() {
		// basically we're just performing the virus scan, as the actual upload can only be issued clientside...
		
		RsyncUpload upload = daoService.getRsyncUpload(uploadId);
		WorkflowIP wfip = null;
		
		try {
			// get package
			if(this.wfipType == TransferP.class)
				wfip = this.daoService.getTransfer(this.wfipId);
			else if(this.wfipType == SubmissionIP.class)
				wfip = this.daoService.getSip(this.wfipId);
			
			String directory = wfip.getAbsolutePath() + upload.getTarget();
			String logfile = wfip.getLogsFolder() + "/" + upload.getVirusLogFileName();
			
			// Scan the uploaded data (folder or file) - file name will be an absolute path, 
			// therefore we do not need the wfip base path. 
			String[] cmd = {"clamscan", "-r", "--remove=yes", directory};
			
			logger.info("Executing: clamscan on " + directory);
			
		    Process process = Runtime.getRuntime().exec(cmd);
		    
		    // outsource writing the logfile to a new thread
		    Thread thread = new Thread(
		    		new ClamAVOutputWriter(this.daoService, upload, process, logfile));
		    thread.start();
		    
		    String progress = null;
		    while(process.isAlive()) {
				progress = upload.getScanProgress();
		    	if(progress != null) {
		    		logger.info("virus scan status: " + progress);
		    		if(progress.contains("100%")) break;
		    	}
				Thread.sleep(10000);
			}
			
			// set upload state depending on the results
			if(process.waitFor() != 0)
				upload.setStatus(RsyncUpload.State.scan_errors);
			else
				upload.setStatus(RsyncUpload.State.scan_ok);
			
			daoService.saveDBEntry(upload);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
