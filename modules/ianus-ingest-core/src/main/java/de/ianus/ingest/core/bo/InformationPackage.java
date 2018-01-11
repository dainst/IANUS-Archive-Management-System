package de.ianus.ingest.core.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.utils.CoreProperties;
import de.ianus.metadata.bo.DataCollection;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.hash.SupportedAlgorithm;

/**
 * @author jurzua
 *
 */
@Entity
@Table(name = "InformationPackage")
public abstract class InformationPackage  extends DBEntry implements Serializable{
	private static final long serialVersionUID = -2231956084293618004L;

	@Column(name="collectionLabel")
    protected String collectionLabel;
	
	@Column(name="collectionIdentifier")
    private String collectionIdentifier;
	
	@Column(name = "locationId", nullable = false)
	private Long locationId;
	
	@Column(name = "metadataId")
	private Long metadataId;
	
	@Transient
	private Location location;
	
	public void setLocation(Location location) {
		this.location = location;
		if(location != null){
			this.locationId = location.getId();
		}
	}

	public Location getLocation() throws Exception {
		if(this.locationId == null){
			this.setLocation(Services.getInstance().getStorageService().assignLocation(this));
			if(location == null){
				throw new Exception("The storage service could not assign a location to this information package");
			}
		}else if(this.location == null){
			this.location = Services.getInstance().getDaoService().getLocation(locationId);
			if(location == null){
				throw new Exception("The location id=" + this.locationId + " could not be loaded");
			}
		}
		return location;
	}
	
	/**
	 * For SIP uses this schema (see: IANUS Konzept, Chaper 13):
	 * <Jahr (YYYY)>_<5-stellige Nummer (#####)>_SIP z.B. 2015_00001_SIP
	 * @return
	 * @throws Exception 
	 */
	public String getPackageFolderName() throws Exception{
		
		if(!this.isPersistent()){
			throw new Exception("Generating the 'package folder name' the IP was not persistent.");
		}
		
		String year = (new SimpleDateFormat("YYYY")).format(getCreationTime()); //Calendar.getInstance().get(Calendar.YEAR);
		String month = (new SimpleDateFormat("MM")).format(getCreationTime()); //Calendar.getInstance().get(Calendar.MONTH);
		String folderName = getPrefix() + "_" + year + "_" + month + "_" + String.format("%010d", getId());
		return folderName;
	}
	
	/*
	public String getFolderSize() throws Exception{
		return Services.getInstance().getStorageService().getSizeOfIP(this);
	}*/
	
	
	public String getAbsolutePath() throws Exception{
		return Services.getInstance().getStorageService().getAbsolutePath(this);
	}
	
	public String getUploadFolder() throws Exception{
		String uploadPathName = getAbsolutePath() + "/upload";
		if(!Services.getInstance().getStorageService().exists(uploadPathName)){
			Services.getInstance().getStorageService().createFolder(this, "/upload");
		}
		return uploadPathName;
	}
	
	/**
	 * Returns the absolute path of the logs folder for this IP.
	 * @return
	 * @throws Exception
	 */
	public String getLogsFolder() throws Exception{
		return getAbsolutePath() + "/logs";
	}
	
	/**
	 * Returns the absolute path of the data folder for this IP.
	 * @return
	 * @throws Exception
	 */
	public String getDataFolder() throws Exception{
		return getAbsolutePath() + "/data";
	}
	
	/**
	 * Returns the absolute path of the metadata folder for this IP.
	 * @return
	 * @throws Exception
	 */
	public String getMetadataFolder() throws Exception{
		return getAbsolutePath() + "/metadata";
	}
	
	public String getMetadataUploadFolder() throws Exception{
		return getAbsolutePath() + "/metadata/metadata-upload";
	}
	
	public String getTmpFolder() throws Exception{
		return getAbsolutePath() + "/tmp";
	}
	
	public String getWebDerivativesFolder() throws Exception{
		return getAbsolutePath() + "/web_derivatives";
	}
	
	public String getWebAssetsFolder() throws Exception{
		return getAbsolutePath() + "/web_assets";
	}
	
	public String getActorLogosFolder() throws Exception{
		return getAbsolutePath() + "/web_assets/actor_logos";
	}
	
	
	public String getOriginalDataFolder() throws Exception {
		String data = this.getDataFolder();
		File root = new File(data);
		if(root.exists()) {
			for(String item : root.list()) {
				if(item.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}_original")) {
					return data + "/" + item;
				}
			}
		}
		return null;
	}
	
	public String getArchiveDataFolder() throws Exception {
		String data = this.getDataFolder();
		File root = new File(data);
		if(root.exists()) {
			for(String item : root.list()) {
				if(item.matches("\\d{4}-\\d{2}-\\d{2}_archive")) {
					return data + "/" + item;
				}
			}
		}
		return null;
	}
	
	public String getPrefix(){
		String prefix = "";
		
		if(this instanceof TransferP){
			prefix = "tp";
		}else if(this instanceof SubmissionIP){
			prefix = "sip";
		}else if(this instanceof PreArchivalIP){
			prefix = "paip";
		}else if(this instanceof ArchivalIP){
			prefix = "aip";
		}else if(this instanceof DisseminationIP){
			prefix = "dip";
		}
		return prefix;
	}


	public Long getMetadataId() {
		return metadataId;
	}
	
	public DataCollection getDataCollection() throws Exception {
		return Services.getInstance().getMDService().getDataCollection(this.getMetadataId());
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	public String getCollectionLabel() {
		return collectionLabel;
	}

	public void setCollectionLabel(String collectionLabel) {
		this.collectionLabel = collectionLabel;
	}

	public String getCollectionIdentifier() {
		return collectionIdentifier;
	}

	public void setCollectionIdentifier(String collectionIdentifier) {
		this.collectionIdentifier = collectionIdentifier;
	}
	
	public String toLogString(){
		return this.getClass().getSimpleName() + "[id=" + this.id + "]";
	}
	
	
	
	public void createBagit() throws Exception {
		String targetDirectory = new CoreProperties().get("bagit.output.path");
		if(!targetDirectory.endsWith("/")) targetDirectory += "/";
		
		// copy package to intermediary location for bagging
		String target = targetDirectory + this.getBagitName();
		FileUtils.copyDirectory(new File(this.getAbsolutePath()), new File (target));
		
		// create the bag (basically just adding the .bagit directory with manifest files)
		List<SupportedAlgorithm> algorithms = new ArrayList<SupportedAlgorithm>();
		algorithms.add(StandardSupportedAlgorithms.MD5);
		boolean includeHiddenFiles = false;
		// Documentation on bagit v 2.0 is very rare: bagInPlace produces a bag of v 0.97, 
		// createDotBagit will add the .bagit directory of v 2.0 and not manipulate the directory structure
		BagCreator.createDotBagit(Paths.get(target), algorithms, includeHiddenFiles);
		
		// create a tarball
		OutputStream tarOutput = new FileOutputStream(new File(target + ".tar"));
		TarArchiveOutputStream tarball = new TarArchiveOutputStream(tarOutput);
		// enable long filenames
		tarball.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
		InformationPackage.recursiveAddToTar(new File(target), null, tarball);
		tarball.finish();
		tarOutput.close();
		
		// remove the intermediary untarred directory
		FileUtils.deleteDirectory(new File(target));
	}
	
	private String getBagitName() {
		// spec: IANUS-2016-00001_AIP-YYYY-MM-DD.bagit.tar
		String date = (new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return "IANUS-" + this.getCollectionIdentifier() + "_" 
				+ this.getPrefix().toUpperCase() + "-" + date + ".bagit";
	}
	
	private static void recursiveAddToTar(File item, String relativePath, TarArchiveOutputStream tarball) throws IOException {
		if(item != null && item.exists() && item.canRead()) {
			relativePath = (relativePath == null) ? item.getName() : relativePath + "/" + item.getName(); 
			if(item.isDirectory()) {
				// recurse
				for(File descendant : item.listFiles())
					InformationPackage.recursiveAddToTar(descendant, relativePath, tarball);
			}
			else if(item.isFile()) {
				TarArchiveEntry tarFile = new TarArchiveEntry(item, relativePath);
				//tarFile.setSize(item.length());		// size is already set?
				tarball.putArchiveEntry(tarFile);
				IOUtils.copy(new FileInputStream(item), tarball);
				tarball.closeArchiveEntry();
			}
		}
	}
	
	
}
