package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.File;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.BagitFile;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.TransferP;

/**
 * ## Javaclass BagitUploadProcessor Helper of upload process in transfer workflow
 * 
 * 
 * ### Summary   
 * In the scope of the transfer workflow (also called pre-ingest part I), the data-provider can upload his collection files.
 * One way to upload files is using the bagit-format as zip. Therefore the file has to be unpacked.
 * When a user uploads a bagit file, the server should check following properties: Virus in the file and Bagit consistency.
 * This class is responsible for the execution of this checking.
 * The execution of this checking can take long time, for this reason this class is execution as a parallel thread. 
 * The method run starts the checking process and updates the variable BagitFile with the progress.
 * 
 * ### Methods 
 * 	Unpack the compressed file: private void unpack()
 * 	Validations: 1. private void virusScan() 
 * 				 2. private void bagitConsistency()
 * 
 * ### Links 
 * Camunda: transfer-workflow.bpmn 
 * Confluence: http://confluence:8090/display/IAN/Transfer+Process+Definition 
 * 
 * @author jurzua
 * @author zoes
 * @see UploadPage.java // Handling the upload process and calling this class
 * @see ZipUtils.java // For unpacking the file
 * @see ClamAVUtils.java // Virus scan
 * @see ClamAVOutput.java // Virus scan output
 * @see BagItValidation.java //BagIt validation
 * @since 0.1
 *
 * package de.ianus.ingest.core.processEngine.ms.utils;
 * 
 */

public class BagitUploadProcessor  implements Runnable {
	
	private static final Logger logger = Logger.getLogger(BagitUploadProcessor.class); // logger for output in terminal
	
	// Save all the necessary services and objects
	private DAOService daoService;
	private Long bagitFileId;
	private Long tpId;
	
	
	/**
	 * This constructor collects all the necessary services and objects for the processing of the bagit file.
	 *
	 * @param daoService: this service is used to update in the DB the class BagitFile.
	 * @param bagitFileId: object that indicates progress of the checking process.
	 * 
	 */
	
	public BagitUploadProcessor(DAOService daoService, Long tpId, Long bagitFileId){
		this.daoService = daoService;
		this.bagitFileId = bagitFileId;
		this.tpId = tpId;
	}
	
	
	/**
	 * Run all methods (the order is important)
	 *
	 * @throws Exception
	 * 
	 */
	
	@Override
    public void run() {
    	try {
        	unpack();
        	virusScan();
        	bagitConsistency();
        	createConceptualFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	/**
	 * Unpacks the BagIt file, that has been saved in upload folder of the tp. 
	 * The data folder is saved in tp-id/data/BagItname/
	 * The BagIt metadata files are stored in tp-id/metadata/metadata-upload/BagItname/
	 * The log file is saved in logs/BagItname/unzip_output.txt
	 *
	 * @see ZipUtils.java
	 * @throws Exception
	 * @TODO: 	- Unpack .tar files
	 */
	
	private void unpack() throws Exception{
		BagitFile bagit = null;
		InformationPackage ip = null;
		
		try {
			// Get BagIt object
			bagit = daoService.getBagitFile(bagitFileId);
			// Change the BagIt state
			bagit.setUnpack(BagitFile.State.in_progress);
			daoService.saveDBEntry(bagit);
			// Get tp object
			ip = this.daoService.getIp(this.tpId);
			
			//Unpack the ziped BagIt in data/bagItxyz/ and metadata/metadata-upload/bagItxyz/
			String resultUnpack = ZipUtils.unZipBagIt(ip, bagit.getFileName());
			// The name of the BagIt is also the name for some folders created during this process
			String unpackedFileNameAsFolder = bagit.getFileName().substring(0, bagit.getFileName().length() - 4);
			// Path to logs/BagItFileName
			String logsPathName = ip.getLogsFolder() + "/" + unpackedFileNameAsFolder;
			
			// Create specific logs folder, if it is not existing
			if(!Services.getInstance().getStorageService().exists(logsPathName)){
				Services.getInstance().getStorageService().createFolder(ip, "/logs/" + unpackedFileNameAsFolder);
			}
			// Create the log file
			PrintWriter out = new PrintWriter(ip.getLogsFolder() + "/" + unpackedFileNameAsFolder + "/unzip_output.txt");
			out.print(resultUnpack);
			out.close();
			
			// Change the BagIt status
			bagit.setUnpack(BagitFile.State.finished_ok);
			daoService.saveDBEntry(bagit);
			
			logger.info(resultUnpack);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(bagit != null){
				// Change the BagIt status
				bagit.setUnpack(BagitFile.State.finished_errors);
				daoService.saveDBEntry(bagit);	
			}
		}
	}
	

	/**
	 * Proceeds a virus scan in the tp-id/data/BagItname/ folder 
	 * and the tp-id/metadata/metadata-upload/BagItname/ of the TP
	 * The log files are saved in logs/BagItname/virus_scan_output_data.txt
	 * 
	 * 
	 * @see ClamAVOutput.java
	 * @see ClamAVUtils.java
	 * @throws Exception
	 * @TODO: 	- Handle exceptions
	 */
	
	private void virusScan() throws Exception{
		
		BagitFile bagit = null;
		InformationPackage ip = null;
	
		try {
			// Get BagIt object
			bagit = daoService.getBagitFile(bagitFileId);
			// Change the BagIt state
			bagit.setVirusScan(BagitFile.State.in_progress);
			daoService.saveDBEntry(bagit);
			// Get tp object
			ip = this.daoService.getIp(this.tpId);
			
			// String containing all logs
			String result = "";
			
			// The name of the BagIt is also the name for some folders created during this process
			String unpackedFileNameAsFolder = bagit.getFileName().substring(0, bagit.getFileName().length() - 4);
	
			// Scan the data folder of the tp
			String directory = ip.getDataFolder() + "/" + unpackedFileNameAsFolder + "/";
			
			logger.info("Executing: clamscan on" + directory);
			
		    ClamAVOutput cmdOutput = ClamAVUtils.execute(directory);
			
			// Save for log file
			result += "Virus scan standard Output for folder /data/ \n" + cmdOutput.standardOutput + "\n";
			result += "Virus scan Error Output for folder /data/ \n" + cmdOutput.errorOutput + "\n";
			
			// scan the metadatadata folder of the tp 
			ClamAVOutput cmdOutputM = ClamAVUtils.execute(ip.getMetadataUploadFolder() + "/" + unpackedFileNameAsFolder + "/");
			
			// Save for log file
			result += "Virus scan standard Output for folder /metadata-upload/ \n" + cmdOutputM.standardOutput + "\n";
			result += "Virus scan Error Output for folder /metadata-upload/ \n" + cmdOutputM.errorOutput + "\n";
			
			
			// Create the log file
			String logfilepath = ip.getLogsFolder() + "/" + unpackedFileNameAsFolder + "/virus_scan_output.txt";
			File logfile = new File(logfilepath);
			logfile.getParentFile().mkdirs();
			logfile.createNewFile();
			PrintWriter out = new PrintWriter(logfilepath);
			out.print(result);
			out.close();
			
			//Set BagitFile State depending on the results
		 if(cmdOutput.exitValue != 0 || cmdOutputM.exitValue != 0){
				logger.info("Virus detected!");
				bagit.setVirusScan(BagitFile.State.finished_errors);
				daoService.saveDBEntry(bagit);
		}
		 else{
				logger.info("No viruses detected!");
				bagit.setVirusScan(BagitFile.State.finished_ok);
				daoService.saveDBEntry(bagit);
		 }
			
		} catch (Exception e) {
			e.printStackTrace();
			bagit.setVirusScan(BagitFile.State.finished_errors);
			daoService.saveDBEntry(bagit);
		} 
		
		
	}
	
	
	/**
	 * Checks BagIt consistency: checks the given checksum and the path of all files.
	 * This information are stored in the BagIt metadata, that have been unpacked to 
	 * tp-id/metadata/metadata-upload/BagItname/
	 * The log file is saved in logs/BagItname/bagit-validation_output.txt
	 * 
	 * 
	 * @see BagItValidation
	 * @throws Exception
	 * @TODO: 	Handle exceptions/ parse result
	 */
	
	private void bagitConsistency() throws Exception{
		
		BagitFile bagit = null;
		InformationPackage ip = null;

		try {
			// Get BagIt object
			bagit = daoService.getBagitFile(bagitFileId);
			// Change the BagIt state
			bagit.setBagitConsistency(BagitFile.State.in_progress);
			daoService.saveDBEntry(bagit);
			// Get tp object
			ip = this.daoService.getIp(this.tpId);

			String resultbagitConsistency = "";
			resultbagitConsistency = BagItValidation.validate(ip, bagit.getFileName());
			logger.info(resultbagitConsistency);

			// The name of the BagIt is also the name for some folders created during this process
			String unpackedFileNameAsFolder = bagit.getFileName().substring(0, bagit.getFileName().length() - 4);

			// Create the log file
			PrintWriter out = new PrintWriter(ip.getLogsFolder() + "/" + unpackedFileNameAsFolder + "/bagit-validation_output.txt");
			out.print(resultbagitConsistency);
			out.close();
			// Change the BagIt status
			bagit.setBagitConsistency(BagitFile.State.finished_ok);
			daoService.saveDBEntry(bagit);
		} catch (Exception e) {
			e.printStackTrace();
			if(bagit != null){
				bagit.setBagitConsistency(BagitFile.State.finished_errors);
				daoService.saveDBEntry(bagit);	
			}
		}
	}
	
	private void createConceptualFiles() throws Exception{
		
		BagitFile bagit = null;
		InformationPackage ip = null;

		try {
			
			// Get BagIt object
			bagit = daoService.getBagitFile(bagitFileId);
			
			// Get tp object
			ip = this.daoService.getIp(this.tpId);
			
			if(ip instanceof TransferP){
				bagit.setConceptualFiles(BagitFile.State.no_required);
			}else{
				
				// Change the BagIt state
				bagit.setConceptualFiles(BagitFile.State.in_progress);
				daoService.saveDBEntry(bagit);

				// The name of the BagIt is also the name for some folders created during this process
				String unpackedFileNameAsFolder = bagit.getFileName().substring(0, bagit.getFileName().length() - 4);

				Services.getInstance().getDaoService().initConceptualFiles4Folder(ip, new File(ip.getDataFolder() + "/" + unpackedFileNameAsFolder));
				
				// Create the log file
				PrintWriter out = new PrintWriter(ip.getLogsFolder() + "/" + unpackedFileNameAsFolder + "/concept-files_output.txt");
				out.print("to implement");
				out.close();
				
				// Change the BagIt status
				bagit.setConceptualFiles(BagitFile.State.finished_ok);
			}
			
			daoService.saveDBEntry(bagit);
		} catch (Exception e) {
			e.printStackTrace();
			if(bagit != null){
				bagit.setConceptualFiles(BagitFile.State.finished_errors);
				daoService.saveDBEntry(bagit);	
			}
		}
	}
	
}
