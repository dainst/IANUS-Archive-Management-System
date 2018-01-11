/**
 * 
 */
package de.ianus.ingest.web.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.pages.component.IpComponentPage;

/**
 * @author hendrik
 *
 */
public class SimpleUploadPage extends IpComponentPage {
	
	private static final Logger logger = LogManager.getLogger(SimpleUploadPage.class);
	
	public static final String PAGE_NAME = "simpleUpload";
	
	private UploadedFile file;
	
	private List<String> fileList;
	
	
	public List<String> getFileList() {
		return this.fileList;
	}
	
	public UploadedFile getFile() {
		return this.file;
	}
	
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	
	
	public void load(WorkflowIP ip) {
		super.load(ip);
		this.file = null;

		try {
			this.fileList = Services.getInstance().getStorageService().getFilesNameInUploadFolder(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String actionUpload() {
		if(this.ip == null) {
			addMsg("The information package is null in the context of this page.");
			return PAGE_NAME;
		}
		if(this.file == null) {
			addMsg("You did not select a file.");
			return PAGE_NAME;
		}

		try {
			this.saveInputFile();
		}catch (Exception e) {
			addInternalError(e);
		}finally{
			this.load(this.ip);
		}
		
		return PAGE_NAME;
	}
	
	private void saveInputFile() throws Exception {
		String uploadFolder = this.ip.getUploadFolder();
		String fileName = this.file.getFileName();
		
		OutputStream output = null;
		try {
			InputStream input = this.file.getInputstream();
			logger.info("Uploading " + uploadFolder + "/" + fileName);
			output = new FileOutputStream(new File(uploadFolder + "/" + fileName));
			
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = input.read(bytes)) != -1)
				output.write(bytes, 0, read);

		}catch (Exception e) {
			addInternalError(e);
		}finally {
			if(output != null) {
				try {
					output.close();
				}catch (IOException e) {
					addInternalError(e);
				}
			}
		}
	}
	
	public String actionDeleteUploadedFile() {
		try {
			String fileName = (String)getRequestBean("fileName");
			Services.getInstance().getStorageService().deleteFile(this.ip, "upload/" + fileName);
		} catch (Exception e) {
			addInternalError(e);
		}finally{
			this.load(this.ip);
		}
		return PAGE_NAME;
	}
	
	
}
