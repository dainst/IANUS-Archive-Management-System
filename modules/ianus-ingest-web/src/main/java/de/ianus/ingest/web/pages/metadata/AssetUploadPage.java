package de.ianus.ingest.web.pages.metadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.pages.component.IpComponentPage;
import de.ianus.metadata.bo.DataCollection;

public class AssetUploadPage extends IpComponentPage {
	
	
	public static final String PAGE_NAME = "assetUpload";
	
	private static final String defaultImagePath = "default-assets/";
	
	private DataCollection dc;
	
	
	public void load(DataCollection dc, WorkflowIP ip) throws Exception{
		super.load(ip);
		this.dc = dc;
	}
	
	public String getPageTitle(){
		return this.getClass().getSimpleName();
	}
	
	public DataCollection getDc() {
		return this.dc;
	}
	
	public Boolean getHeaderImageExists() {
		try {
			return new File(this.getWfip().getWebAssetsFolder() + "/header.png").exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean getPreviewImageExists() {
		try {
			return new File(this.getWfip().getWebAssetsFolder() + "/preview.png").exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean getLicenseImageExists() {
		try {
			return new File(this.getWfip().getWebAssetsFolder() + "/license_logo.png").exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String handleHeaderUpload(FileUploadEvent event) throws Exception {
		InputStream stream = event.getFile().getInputstream();
		Path path = new File(this.getWfip().getWebAssetsFolder() + "/header.png").toPath();
		// write the file
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
		
		FacesMessage message = new FacesMessage("Succesfully uploaded ", event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return PAGE_NAME;
    }
	
	public String handlePreviewUpload(FileUploadEvent event) throws Exception {
		InputStream stream = event.getFile().getInputstream();
		Path path = new File(this.getWfip().getWebAssetsFolder() + "/preview.png").toPath();
		// write the file
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
		
		FacesMessage message = new FacesMessage("Succesfully uploaded ", event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return PAGE_NAME;
    }
	
	public String handleLicenseUpload(FileUploadEvent event) throws Exception {
		InputStream stream = event.getFile().getInputstream();
		Path path = new File(this.getWfip().getWebAssetsFolder() + "/license_logo.png").toPath();
		// write the file
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
		
		FacesMessage message = new FacesMessage("Succesfully uploaded ", event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return PAGE_NAME;
    }
	
	public String saveDefaultHeader() throws Exception {
		if(this.getDefaultImagesAvailable()) {
			String path = defaultImagePath + "header.png";
			byte[] data = getDefaultImageByteArray(path);
			FileUtils.writeByteArrayToFile(new File(this.getWfip().getWebAssetsFolder() + "/header.png"), data);
		}
		
		return PAGE_NAME;
	}
	
	public String saveDefaultPreview() throws Exception {
		if(this.getDefaultImagesAvailable()) {
			String path = defaultImagePath + "preview.png";
			byte[] data = getDefaultImageByteArray(path);
			FileUtils.writeByteArrayToFile(new File(this.getWfip().getWebAssetsFolder() + "/preview.png"), data);
		}
		
		return PAGE_NAME;
	}
	
	public String saveDefaultLicense() throws IOException, Exception {
		if(this.getDefaultLicenseImageAvailable()) {
			String path = defaultImagePath + "license-logos/" + this.getDefaultLicenseName();
			byte[] data = getDefaultImageByteArray(path);
			FileUtils.writeByteArrayToFile(new File(this.getWfip().getWebAssetsFolder() + "/license_logo.png"), data);
		}
		return PAGE_NAME;
	}
	
	
	
	public Boolean getDefaultImagesAvailable() throws Exception {
		if(this.getWfip() == null) return false;
		return !this.getWfip().hasFiles();
	}
	
	public Boolean getDefaultLicenseImageAvailable() {
		String name = this.getDefaultLicenseName();
		if(name == null || name.isEmpty()) return false; 
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(defaultImagePath + "license-logos/" + name);
		if(is != null) {
			try {
				if(is != null && is.available() > 0) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private String getDefaultLicenseName() {
		if(this.dc.getLicenseName() == null || this.dc.getLicenseName().isEmpty()) return null;
		return this.dc.getLicenseName().toUpperCase() + ".png";
	}
	
	
	
	/**
	 * Method to forward the image's data stream in a primefaces compatible stream format.
	 * @return DefaultStreamedContent (primefaces)
	 * @throws Exception 
	 */
	public DefaultStreamedContent getDefaultHeader() throws Exception {
		if(this.getDefaultImagesAvailable()) {
			String path = defaultImagePath + "header.png";
			if(path != null) {
				byte[] data = this.getDefaultImageByteArray(path);
				if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png");
			}
		}
		return null;
	}
	
	public DefaultStreamedContent getDefaultPreview() throws Exception {
		if(this.getDefaultImagesAvailable()) {
			String path = defaultImagePath + "preview.png";
			if(path != null) {
				byte[] data = this.getDefaultImageByteArray(path);
				if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png");
			}
		}
		return null;
	}
	
	public DefaultStreamedContent getDefaultLicense() throws Exception {
		if(this.getDefaultLicenseImageAvailable()) {
			String path = defaultImagePath + "license-logos/" + this.getDefaultLicenseName();
			if(path != null) {
				byte[] data = this.getDefaultImageByteArray(path);
				if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png");
			}
		}
		return null;
	}
	
	public DefaultStreamedContent getCurrentHeaderImage() throws Exception {
		if(this.getHeaderImageExists()) {
			byte[] data = this.getImageByteArray(this.getWfip().getWebAssetsFolder() + "/header.png");
			if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png"); 
		}
		return null;
	}
	
	public DefaultStreamedContent getCurrentPreviewImage() throws Exception {
		if(this.getPreviewImageExists()) {
			byte[] data = this.getImageByteArray(this.getWfip().getWebAssetsFolder() + "/preview.png");
			if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png"); 
		}
		return null;
	}
	
	public DefaultStreamedContent getCurrentLicenseImage() throws Exception {
		if(this.getLicenseImageExists()) {
			byte[] data = this.getImageByteArray(this.getWfip().getWebAssetsFolder() + "/license_logo.png");
			if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png"); 
		}
		return null;
	}
	
	private byte[] getImageByteArray(String filename) {
		if(filename == null || filename.isEmpty()) return null;
		File file = new File(filename);
		if(file != null && file.exists()) {
			try {
				return Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private byte[] getDefaultImageByteArray(String filename) {
		if(filename == null || filename.isEmpty()) return null;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
		if(is != null) {
			try {
				if(is != null && is.available() > 0) {
					return IOUtils.toByteArray(is);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String deleteHeaderImage() throws Exception {
		Files.deleteIfExists(new File(this.getWfip().getWebAssetsFolder() + "/header.png").toPath());
		return PAGE_NAME;
	}
	
	public String deletePreviewImage() throws Exception {
		Files.deleteIfExists(new File(this.getWfip().getWebAssetsFolder() + "/preview.png").toPath());
		return PAGE_NAME;
	}
	
	public String deleteLicenseImage() throws Exception {
		Files.deleteIfExists(new File(this.getWfip().getWebAssetsFolder() + "/license_logo.png").toPath());
		return PAGE_NAME;
	}
	
	
}
