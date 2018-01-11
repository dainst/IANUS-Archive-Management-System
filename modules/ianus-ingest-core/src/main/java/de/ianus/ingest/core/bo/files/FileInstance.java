package de.ianus.ingest.core.bo.files;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.WorkflowIP;

/**
 * 
 * 
 * @author jurzua, hschmeer
 *
 */
@Entity
@Table(name="FileInstance")
public class FileInstance extends AbstractFile {

	// indicate the previous version file instance after format migrations
	// we are going to define that relation via Event, supporting also n:m conversions
	/*
	@Column(name="predecessorId")
	private Long predecessorId = null;
	*/
	
	@Column(name="fileConceptId", nullable=false)
	private Long fileConceptId;
	
	@Column(name="fileGroupId")
	private Long fileGroupId;
	
	
	//location of the file in the IP. Also known as relative path.
	@Column(name="location")
	private String location;
	
	@Column(name="fileName")
	private String fileName;
	
	@Column(name="ext")
	private String ext;
	
	@Column(name="checksum")
	private String checksum;
	
	@Column(name="fileBytes")
	private Long fileBytes;
	
	@Column(name="fileCreated")
	private String fileCreated;
	
	@Column(name="fileLastModified")
	private String fileLastModified;
	
	
	@Column(name="hasTechMd", nullable = false, columnDefinition = "TINYINT default 0", length = 1)
	private Boolean hasTechMd = false;
	
	
	/**
	 * Following attributes basically are copies from FileInstanceProperties. 
	 * Because their relevance is critical to further processing and curation decisions, 
	 * we store them reduntantly also here. 
	 * As file format identification might return more than one result for each of these properties, 
	 * a curator has to take action to decide, eg. chose one of two detected PUIDs.
	 * The affected field will then hold the value CONFLICT. 
	 * The chosen one will be stored here and determine the conversion plan to follow up. 
	 * 
	 * For validity information, the additional values OK,NO(t ok) are used. 
	 * Of course, there might be conflicts as well.  
	 */
	
	// PRONOM identifier
	@Column(name="puid")
	private String puid = null;
	
	@Column(name="mimeType")
	private String mimeType = null;
	
	
	
	@Column(name="valid")
	private String valid;
	
	@Column(name="wellFormed")
	private String wellFormed;
	
	
	public static enum Status {
		OK,NO,CONFLICT
	}
	
		
	
	
	
	@Transient
	private FileConcept fileConcept;
	
	@Transient
	private FileGroup fileGroup;
	
	
	
	
	public FileInstance(){}
	
	public FileInstance(FileConcept cf, File file, InformationPackage ip) throws Exception{
		this.setFileConcept(cf);
		this.fileName = file.getName();
		this.ext = file.getName().substring(file.getName().lastIndexOf('.'));
		this.location = calculateLocation(ip, file);
		this.ipId = ip.getId();
		this.wfipType = ip.getClass().getName();
		this.checksum = calculateChecksum(file);
		this.fileBytes = file.length();
		BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.fileCreated = df.format(attr.creationTime().toMillis());
		this.fileLastModified = df.format(attr.lastModifiedTime().toMillis());
	}
	
	public static FileInstance clone(FileInstance other, InformationPackage newIp, FileConcept newConcept, FileGroup newGroup){
		FileInstance newInstance = new FileInstance();
		newInstance.fileName = other.fileName;
		newInstance.ext = other.ext;
		newInstance.location = other.location;
		newInstance.checksum = other.checksum;
		newInstance.fileBytes = other.fileBytes;
		newInstance.fileCreated = other.fileCreated;
		newInstance.fileLastModified = other.fileLastModified;
		
		newInstance.ipId = newIp.getId();
		newInstance.wfipType = newIp.getClass().getName();
		
		newInstance.puid = other.puid;
		newInstance.mimeType = other.mimeType;
		newInstance.valid = other.valid;
		newInstance.wellFormed = other.wellFormed;
		
		newInstance.fileConceptId = newConcept.getId();
		newInstance.fileGroupId = (newGroup != null) ? newGroup.getId() : null;
		
		newInstance.hasTechMd = other.hasTechMd;
		
		return newInstance;
	}
	
	
	// implement a save method for testing reasons, so we can mock the save behavior of a mocked FileInstance
	public void save(DAOService dao) throws Exception {
		dao.saveDBEntry(this);
	}
	
	
	public List<FileInstanceProperty> getProperties() {
		return Services.getInstance().getDaoService().getFileInstancePropertyList(this);
	}
	
	
	public List<FileInstanceProperty> getProperties(String name) {
		List<FileInstanceProperty> list = this.getProperties();
		List<FileInstanceProperty> result = new ArrayList<FileInstanceProperty>();
		if(list != null && name != null && !name.equals("")) for(FileInstanceProperty prop : list) {
			if(prop.getName().equals(name)) 
				result.add(prop);
		}else{
			return null;
		}
		if(result.size() > 0) return result;
		return null;
	}
	
	
	public void updateLocation(InformationPackage ip, File file) throws Exception{
		this.location = calculateLocation(ip, file);
		this.fileName = file.getName();
	}
	
	private void updateFileTime() {
		try {
			DAOService dao = Services.getInstance().getDaoService();
			WorkflowIP ip = dao.getWorkflowIP(this.ipId, Class.forName(this.wfipType));
			File file = new File(ip.getDataFolder() + "/" + this.location);
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.fileCreated = df.format(attr.creationTime().toMillis());
			this.fileLastModified = df.format(attr.lastModifiedTime().toMillis());
			this.save(dao);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String calculateLocation(InformationPackage ip, File file) throws Exception{
		String absolutePathFile = file.getAbsolutePath();
		return absolutePathFile.replace(ip.getDataFolder() + "/", "");
	}
	
	private String calculateChecksum(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			fis.close();
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	public Long getFileConceptId() {
		return fileConceptId;
	}

	public void setFileConceptId(Long fileConceptId) {
		this.fileConceptId = fileConceptId;
	}
	
	public FileConcept getFileConcept() {
		return fileConcept;
	}

	public void setFileConcept(FileConcept fileConcept) {
		this.fileConcept = fileConcept;
		if(fileConcept != null){
			this.fileConceptId = this.fileConcept.getId();
		}
	}
	
	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}
	
	public String getWfipType() {
		return this.wfipType;
	}
	
	public void setWfipType(String type) {
		this.wfipType = type;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getExt() {
		return this.ext;
	}
	
	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public Long getFileBytes() {
		return fileBytes;
	}

	public void setFileBytes(Long fileBytes) {
		this.fileBytes = fileBytes;
	}
	
	public String getFileCreated() {
		if(this.fileCreated == null)
			this.updateFileTime();
		return this.fileCreated;
	}
	
	public void setFileCreated(String date) {
		this.fileCreated = date;
	}
	
	public String getFileLastModified() {
		if(this.fileLastModified == null)
			this.updateFileTime();
		return this.fileLastModified;
	}
	
	public void setFileLastModified(String date) {
		this.fileLastModified = date;
	}
	
	
	
	public void setHasTechMd(boolean b) {
		this.hasTechMd = b;
	}
	
	public Boolean getHasTechMd() {
		return this.hasTechMd;
	}
	
	
	
	public String getPuid() {
		return puid;
	}

	public void setPuid(String puid) {
		this.puid = puid;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	
	
	public void setValid(Status stat) {
		if(stat != null) this.valid = stat.name();
	}
	
	public String getValid() {
		return this.valid;
	}
	
	public void setWellFormed(Status stat) {
		if(stat != null) this.wellFormed = stat.name();
	}
	
	public String getWellFormed() {
		return this.wellFormed;
	}
	
	
	
	public Long getFileGroupId() {
		return fileGroupId;
	}

	public void setFileGroupId(Long fileGroupId) {
		this.fileGroupId = fileGroupId;
	}
	
	public FileGroup getFileGroup() {
		return fileGroup;
	}

	public void setFileGroup(FileGroup fileGroup) {
		this.fileGroup = fileGroup;
		if(fileGroup != null){
			this.fileGroupId = this.fileGroup.getId();
		}
	}
	
	public String toStringShort(){
		return "FileInstance [id=" + getId() + ", name="+ fileName +  ", fileConceptId=" + fileConceptId + ", fileGroupId=" + fileGroupId + "]";
	}

	@Override
	public String toString(){
		return "FileInstance [id=" + getId() + ", name="+ fileName +  ", fileConceptId=" + fileConceptId + ", fileGroupId=" + fileGroupId + "]";
		//return "FileInstance [id=" + getId() + ", name="+ fileName +  ", fileConceptId=" + fileConceptId + ", fileGroupId=" + fileGroupId + ", location=" + location + "]";
	}
}