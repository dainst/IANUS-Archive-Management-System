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

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.pages.OverviewDCPage;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;

public class ActorLogoPage extends DcComponentPage {
	
	
	public static String PAGE_NAME = "actorLogo";
	
	//folder in the class path that contains the default-logos
	private static final String defaultImagePath = "default-assets/actor-logos/";
	
	private Actor actor;

	private String informationPackagePath;
	
	private String target;
	
	
	
	public void load(DataCollection dcBase, IANUSEntity source, Actor actor) throws Exception{
		super.load(dcBase, source);
		this.actor = (actor.isPersistent()) ? Services.getInstance().getMDService().getActor(actor.getId(), actor.getActorClass()) : actor;
		
		DAOService dao = Services.getInstance().getDaoService();
		WorkflowIP wfip = dao.getWorkflowPackageList(this.getDcBase().getId(), null).get(0);
		this.informationPackagePath = wfip.getActorLogosFolder();
		
		if(this.actor != null) this.target = this.informationPackagePath + "/" + this.actor.getId() + ".png";
	}
	
	
	public Boolean getLogoExists() {
		if(this.target != null) return new File(this.target).exists();
		return false;
	}
	
	
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public Actor getActor() {
		return this.actor;
	}
	
	public String getPageTitle(){
		return this.getClass().getSimpleName();
	}
	
	
	
	public String handleFileUpload(FileUploadEvent event) throws IOException {
		if(this.target == null) return OverviewDCPage.PAGE_NAME;
		InputStream stream = event.getFile().getInputstream();
		Path path = new File(this.target).toPath();
		// write the file
		Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
		
		FacesMessage message = new FacesMessage("Succesfully uploaded ", event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return PAGE_NAME;
    }
	
	public String saveDefault() throws IOException {
		if(this.getDefaultLogoAvailable()) {
			String path = this.getDefaultLogoName();
			if(path != null) {
				byte[] data = this.getDefaultLogoByteArray(path);
				FileUtils.writeByteArrayToFile(new File(this.target), data);
			}
		}
		
		return PAGE_NAME;
	}
	
	
	
	public Boolean getDefaultLogoAvailable() {
		String institutionName = this.getDefaultLogoName();
		if(institutionName != null && !institutionName.isEmpty()) {
			try {
				if(this.getDefaultLogo() != null) return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	
	public DefaultStreamedContent getCurrentLogo() {
		if(this.getLogoExists()) {
			byte[] data = this.getLogoByteArray(this.target);
			return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png"); 
		}
		return null;
	}
	
	private byte[] getLogoByteArray(String filename) {
		if(filename == null || filename.isEmpty()) return null;
		// imagery from IP has to be read a file, because the location is not in the classpath
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
	
	private byte[] getDefaultLogoByteArray(String filename) {
		if(filename == null || filename.isEmpty()) return null;
		// default images from resources can only be read from InputStream ()
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
		//System.out.println(this.getClass().getClassLoader().getResource(filename).getPath());
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
	
	private String getInstitutionName() {
		String institutionName = null;
		if(this.actor instanceof Institution) {
			institutionName = ((Institution) this.actor).getName();
		}else if(this.actor instanceof Person) {
			institutionName = ((Person) this.actor).getInstitutionName();
		}
		return institutionName;
	}
	
	private String getDefaultLogoName() {
		String path = null;
		String institutionName = this.getInstitutionName();
		if(institutionName == null || institutionName.isEmpty()) return null;
		if(institutionName.contains("IANUS")) {
			path = defaultImagePath + "IANUS-Logo.png";
		}
		if(	institutionName.contains("DAI")
		||	institutionName.contains("Deutsches Archäologisches Institut")) {
			path = defaultImagePath + "DAI-Logo.png";
		}
		return path;
	}
	
	/**
	 * Method to forward the image's data stream in a primefaces compatible stream format.
	 * @return DefaultStreamedContent (primefaces)
	 * @throws IOException
	 */
	public DefaultStreamedContent getDefaultLogo() throws IOException {
		String path = getDefaultLogoName();
		if(path != null) {
			byte[] data = this.getDefaultLogoByteArray(path);
			if(data != null) return new DefaultStreamedContent(new ByteArrayInputStream(data), "image/png");
		}
		return null;
	}
	
	public String deleteLogo() {
		try {
			Files.deleteIfExists(new File(this.target).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return PAGE_NAME;
	}
	
	
}
