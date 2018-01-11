package de.ianus.ingest.web.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.BagitFile;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.processEngine.ms.utils.BagitUploadProcessor;
import de.ianus.ingest.web.pages.component.IpComponentPage;

/**
 * ## Javaclass (backend functionality) UploadPage in transfer workflow
 * 
 * 
 * ### Summary 
 * This class handles the fileUpload: expected: the selected file
 * and the clientside calculate checksum (compare to client-side
 * checksum) start Thread for unpack, virusscan and bagIt validation
 * 
 * ### Methods 
 * 	Uploadprocess: 1. public void listenerUpload(ActionEvent event) // is called after upload button is pressed 
 * 						-> 2. private void load0() 
 * 						-> 3. private void uploadBagit() 
 * 						-> 4. private void saveInputFile(String uploadFolder, String fileName) 
 *						-> 5. private void startProcceses(String fileName) 
 *	Refresh status table: actionRefresh() // is called after refresh button is pressed 
 *	Delete uploaded file: deleteUploadedFile()
 * 
 * ### Links 
 * Camunda: transfer-workflow.bpmn 
 * Confluence: http://confluence:8090/display/IAN/Transfer+Process+Definition 
 * 
 * @author jurzua
 * @author zoes
 * @see upload.xhtml
 * @see BagitUploadProcessor.java
 * @since 0.1
 *
 * belongs to package de.ianus.ingest.web.pages;
 * TODO: report Button!
 * TODO: delete unused files when finishing the upload: all files in /upload/ ; metadata/metadata-upload/
 * 
 * ### Required 
 * Installation of virus scan software: Install clamAV (manual mode): sudo apt-get install clamav*
 *  * Configure the proxy here: /etc/clamav/freshclam.conf
 * 
 * 
 */

public class UploadPage extends IpComponentPage {

	private static final Logger logger = LogManager.getLogger(UploadPage.class); // logger for output in terminal
	public static final String PAGE_NAME = "upload"; // name of related page

	private boolean showConfirmDialog = false; // dialog/ alert box

	private String checksumClient; // the calculated checksum -> calculated on clientside before upload (backend) has started
	private String checksumServer; // calculated checksum of the received file

	private UploadedFile file; // the uploaded file
	private List<BagitFile> bagitFileList; //= new ArrayList<BagitFile>(); List of all BagIt files of the ip

	private String selectedBagitFile = null; //the selected BagIt file, if user wants to delete it
	
	private DefaultStreamedContent bagItReport;
	private DefaultStreamedContent virusScanReport;
	
	/**
	 * Loads the information package and the list of uploaded BagIt files. The
	 * InformationPackage argument must contain an existing ip.
	 * 
	 * This method always sets the file object to null and loads the given ip.
	 *
	 * @param InformationPackage the information package related to the actual data collection
	 * @see InformationPackage.java
	 */

	public void load(WorkflowIP ip) {
		super.load(ip);
		this.file = null;

		this.bagitFileList = Services.getInstance().getDaoService().getBagitFileList(ip);
	}

	
	/**
	 * Starts the upload process.
	 * 
	 * This method is called after a ActionEvent ("upload" button is pushed) is
	 * triggered. It starts the upload process.
	 *
	 * @param ActionEvent
	 * @see javax.faces.event.ActionEvent
	 */

	public void listenerUpload(ActionEvent event) {
		this.load0();
	}

	
	/**
	 * Checks if ip, clientside checksum and a file exist in this context.
	 * 
	 * This method checks, if a upload is possible (if the necessary objects
	 * exist) and gives appropriate feedback to the user.
	 *
	 * @throws Exception
	 *                 
	 */

	private void load0() {
		addMsg("Starting to upload your file = " + this.file.getFileName());


		if (this.ip == null) {
			addMsg("The information package is null in the context of this page.");
			return;
		}

		if (this.file == null) {
			addMsg("You did not select a file.");
			return;
		}

		try {
			addMsg("The calculated Checksum is = " + this.checksumClient);
			addMsg("Please refresh the page to see the actual state.");
			uploadBagit(); // Starting to save the file and processing it
			this.load(ip);
		} catch (Exception e) {
			addInternalError(e);
		}
	}


	/**
	 * Creates a filename (using the date) for the selected (to be uploaded) file.
	 * Saves the file to disk and starts the initial validation 
	 * (unpack, virusscan, bagIt validation).
	 * 
	 * TODO recognition of format (zip or tar)
	 */
	
	private void uploadBagit() throws Exception {
		// Create the name for the uploaded BagIt file
		String date = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()); 
		String fileName = "bagit_" + date + ".zip";

		// read the inputstream and save the file to disk
		saveInputFile(ip.getUploadFolder(), fileName);

		// start the initial validation tasks (unpack, virusscan, bagIt validation)
		startProcceses(fileName);
	}


	/**
	 * Reads file inputstream to save file to disk, calculates checksum 
	 * while doing this. Sets the calculated checksum.
	 *
	 * @param String an absolute URL giving the base location of the upload folder
	 * @param String the name for the uploaded file
	 * @throws Exception
	 * 
	 */

	private void saveInputFile(String uploadFolder, String fileName) {
		OutputStream output = null;
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5"); // MD5 algorithm for calculating the checksum
			InputStream input = file.getInputstream(); // Read file
			logger.info("Uploading into " + uploadFolder + "/" + fileName); // log what happens 
			output = new FileOutputStream(new File(uploadFolder + "/" + fileName)); // Write file

			int read = 0;
			byte[] bytes = new byte[1024];

			// Read and write uploaded file
			while ((read = input.read(bytes)) != -1) {
				md.update(bytes, 0, read);
				output.write(bytes, 0, read);
			}

			byte[] mdbytes = md.digest();

			// calculate checksum
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			logger.info("Backend calculated checksum:: " + sb.toString()); // log what happens 
			this.checksumServer = sb.toString(); // set calculated checksum

		} catch (Exception e) {
			addInternalError(e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					addInternalError(e);
				}
			}
		}
	}


	/**
	 * Compares the client and backend checksum. 
	 * Creates a new BagIt object and pass this and the tp to
	 * start a thread for unpacking, virusscan BagIt validation.
	 *
	 * @param String the name for the uploaded file
	 * @see BagitUploadProcessor.java
	 * @throws Exception
	 * 
	 */
	
	private void startProcceses(String fileName) throws Exception {

		// Compare checksums
		if (this.checksumServer.equals(this.checksumClient)) {
			logger.info("Checksums are compared and correct!"); // log what happens 
			
			// Create BagIt object with all available information and save it in the database
			BagitFile bagitFile = new BagitFile();
			bagitFile.setFileName(fileName);
			bagitFile.setChecksumBagitFileClient(this.checksumClient);
			bagitFile.setChecksumBagitFileServer(this.checksumServer);
			bagitFile.setChecksumEvaluation(BagitFile.State.finished_ok);
			bagitFile.setIpId(this.ip.getId());
			Services.getInstance().getDaoService().saveDBEntry(bagitFile);

			// Start thread for unpacking, virusscan BagIt validation
			Thread thread = new Thread(
					new BagitUploadProcessor(Services.getInstance().getDaoService(), bagitFile.getIpId(), bagitFile.getId()));
			thread.start();

			// load ip to make it available for the page
			this.load(ip);
			addMsg("The file has been uploaded."); // feedback for user
		} else {
			
			// The process should be stopped because the checksum
			// evaluation went wrong -> the file is corrupt: delete it
	
			addMsg("The file has not been processed. The checksum calculation went wrong."); // feedback for user
			addMsg("Please try it again.");
			
			logger.info("The checksum calculation went wrong."); // log what happens 
    		File file = new File(ip.getUploadFolder() + "/" + fileName);
    		if(file.delete()){
    			logger.info(file.getName() + " is deleted!"); // log what happens 
    		}else{
    			logger.info("Delete uploaded file operation has failed."); // log what happens 

    		}
			
		}
	}

	
	/**
	 * Gives user feedback about file and checksum after a FileUploadEvent.
	 *
	 * @param FileUploadEvent event
	 * @param name
	 * 
	 */
	
	public void handleFileUpload(FileUploadEvent event) {
		addMsg("***** File: " + event.getFile().getFileName() + "  Checksum: " + this.checksumClient);
	}

	
	/**
	 * This method is called, if the refresh button is clicked. 
	 * The ip is loaded from the database and the upload page is reloaded.
	 *
	 * @return PAGE_NAME
	 * 
	 */
	
	public String actionRefresh() {
		logger.info(this.ip.getId());
		try {
			WorkflowIP ip = null;
			if(this.ip instanceof TransferP){
				ip = Services.getInstance().getDaoService().getTransfer(this.ip.getId());
			}else if(this.ip instanceof SubmissionIP){
				ip = Services.getInstance().getDaoService().getSip(this.ip.getId());
			}else if(this.ip instanceof DisseminationIP){
				ip = Services.getInstance().getDaoService().getDip(this.ip.getId());
			}else if(this.ip instanceof ArchivalIP){
				ip = Services.getInstance().getDaoService().getAip(this.ip.getId());
			}
			this.load(ip);
		} catch (Exception e) {
			addInternalError(e);
		}
		
		return PAGE_NAME;
	}

	
	/**
	 * TODO: explain method
	 *
	 * @param ActionEvent event
	 * 
	 */

	public void listenerRefresh(ActionEvent event) {
		logger.info(this.ip.getId());
		TransferP tp = Services.getInstance().getDaoService().getTransfer(this.ip.getId());
		this.load(tp);
	}

	
	/**
	 * Deletes BagIt Objects and the files (logs, upload-metadata, and zip file are still there. 
	 * It is only possible to delete after the processing has finished.
	 *
	 * @throws Exception
	 * 
	 */
	public void deleteUploadedFile() {
			logger.info("Want to delete: " + this.selectedBagitFile);
			try {
				// Get the selected BagIt file 
				BagitFile bagit = Services.getInstance().getDaoService().getBagitFileByName(ip.getId(), this.selectedBagitFile);
				// Delete the BagIt object in the database and the corresponding files
				Services.getInstance().getDaoService().deleteBagitFile(bagit, this.ip);
				//reload
				this.load(ip);
				addMsg("Your file: "+ bagit.getFileName() + " has been deleted!"); //feedback to user
			} catch (Exception e) {
				addInternalError(e);
			}
	}
	
	public String actionDeleteUploadedFile(){
		try {
			// Get the selected BagIt file from the request
			BagitFile bagit0 = (BagitFile)getRequestBean("bagitFile");
			// Delete the BagIt object in the database and the corresponding files
			Services.getInstance().getDaoService().deleteBagitFile(bagit0, this.ip);
			//reload
			this.load(ip);
			addMsg("Your file: "+ bagit0.getFileName() + " has been deleted!"); //feedback to user
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	
	/**
	 * Download BagIt report of processing (it is created after processes have finished in logs/BagItName/...) 
	 * It is only possible to download after the processing has finished.
	 *
	 * @throws Exception
	 * 
	 */

	public void prepareDownloadReportBagIt() {
		logger.info("Want to get report BagIt validation of: " + this.selectedBagitFile);
		try {
		// Get the selected BagIt file 
		BagitFile bagit = Services.getInstance().getDaoService().getBagitFileByName(ip.getId(), this.selectedBagitFile);	
		// The name of the BagIt is also the name for some folders created during this process
		String unpackedFileNameAsFolder = bagit.getFileName().substring(0, bagit.getFileName().length() - 4);
		// Get the report file
	    File report = new File(this.ip.getLogsFolder() + "/" + unpackedFileNameAsFolder + "/bagit-validation_output.txt");
	    InputStream input = new FileInputStream(report);
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    // Save the BagIt file for download
	    setBagItReport(new DefaultStreamedContent(input, externalContext.getMimeType(report.getName()), report.getName()));
	} catch (Exception e) {
		addInternalError(e);
	}
	}
	
	
	/**
	 * Download Virus scan report (it is created after processes have finished in logs/BagItName/...) 
	 * It is only possible to download after the processing has finished.
	 *
	 * @throws Exception
	 * 
	 */
	
	public void prepareDownloadReportVirusScan() {
		logger.info("Want to get report BagIt validation of: " + this.selectedBagitFile);
		try {
		// Get the selected BagIt file 
		BagitFile bagit = Services.getInstance().getDaoService().getBagitFileByName(ip.getId(), this.selectedBagitFile);	
		// The name of the BagIt is also the name for some folders created during this process
		String unpackedFileNameAsFolder = bagit.getFileName().substring(0, bagit.getFileName().length() - 4);
		// Get the report file
	    File report = new File(this.ip.getLogsFolder() + "/" + unpackedFileNameAsFolder + "/virus_scan_output.txt");
	    InputStream input = new FileInputStream(report);
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    // Save the BagIt file for download
	    setVirusScanReport(new DefaultStreamedContent(input, externalContext.getMimeType(report.getName()), report.getName()));
		} catch (Exception e) {
			addInternalError(e);
		}
	}


	/**
	 * Getter & Setter methods
	 */
	public void setVirusScanReport(DefaultStreamedContent virusScanReport) {
	    this.virusScanReport = virusScanReport;
	}
	
	public DefaultStreamedContent getvirusScanReport() throws Exception {
	    return virusScanReport;
	}    

    public void setBagItReport(DefaultStreamedContent bagItReport) {
        this.bagItReport = bagItReport;
    }
    
    public DefaultStreamedContent getBagItReport() throws Exception {
        return bagItReport;
    }

	public String getLabel() {
		return (ip instanceof TransferP) ? ((TransferP) ip).getCollectionLabel() : ip.toString();
	}

	public WorkflowIP getIp() {
		return ip;
	}

	public void setIp(WorkflowIP ip) {
		this.ip = ip;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isShowConfirmDialog() {
		return showConfirmDialog;
	}

	public void setShowConfirmDialog(boolean showConfirmDialog) {
		this.showConfirmDialog = showConfirmDialog;
	}

	public List<BagitFile> getBagitFileList() {
		return bagitFileList;
	}

	public void setBagitFileList(List<BagitFile> bagitFileList) {
		this.bagitFileList = bagitFileList;
	}

	public String getChecksumClient() {
		return checksumClient;
	}

	public void setChecksumClient(String checksumClient) {
		logger.info(checksumClient);
		this.checksumClient = checksumClient;
	}
	
    public String getSelectedBagitFile() {
        return selectedBagitFile;
    }
 
    public void setSelectedBagitFile(String selectedBagitFile) {
        this.selectedBagitFile = selectedBagitFile;
    }
}
