package de.ianus.ingest.core.bo;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;

/**
 * 
 * 
 * <Jahr (YYYY)>_<5-stellige Nummer (#####)>_SIP
 * |--data

 *    \-- <YYYY>-<MM>-<DD>_Original (Tag des Eingangs)
 *        \-- digital objects...
 *    \-- <YYYY>-<MM>-<DD> (AIP V1)
 *    \-- <YYYY>-<MM>-<DD> (AIP V2)
 * |--metadata
 * |  \-- premis_<YYYY>-<MM>-<DD>.xml
 * |  \-- premis_<YYYY>-<MM>-<DD>.xml
 * |  \-- premis.xml 
 *    \-- IANUS.xml
 *    \-- documentation
 *        \-- 
 *    
 *    
 *    
 *    
 * 
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name = "SubmissionIP")
public class SubmissionIP  extends WorkflowIP implements Serializable{
	private static final long serialVersionUID = 2948405352915542300L;
	
	private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	@Column(name="tpId")
	protected Long tpId;
	
	@Transient
	protected TransferP tp;
	
	
	
	public void setTp(TransferP tp) {
		this.tp = tp;
		if(this.tp != null){
			this.tpId = this.tp.getId();
		}
	}

	public Long getTpId() {
		return tpId;
	}

	public void setTpId(Long tpId) {
		this.tpId = tpId;
	}

	public TransferP getTp() {
		if(this.tp == null)
			this.tp = Services.getInstance().getDaoService().getTransfer(this.tpId);
		return tp;
	}

	public String getWebAssetsFolder() throws Exception{
		return getAbsolutePath() + "/web_assets";
	}
	
	public String getActorLogosFolder() throws Exception{
		return getAbsolutePath() + "/web_assets/actor_logos";
	}
	
	public List<String> getBagitFiles() throws Exception {
		List<String> bagitFiles = new ArrayList<String>();
		List<String> files = Services.getInstance().getStorageService().getFilesNameInUploadFolder(this);
		String uploadFolder = this.getUploadFolder();
		for(String file : files){
			if(file.endsWith(".zip")){
				bagitFiles.add(uploadFolder + "/" + file);
			}	
		}
		return bagitFiles;
	}
	
	public List<File> getChecksumManifestFiles() throws Exception{
		List<File> rs = new ArrayList<File>();
		List<String> fileNameList = Services.getInstance().getStorageService().getFilesNameInMetadataFolder(this);
		for(String fileName : fileNameList){
			if(StringUtils.startsWith(fileName, "manifest-")){
				File file = new File(getMetadataFolder() + "/" + fileName);
				rs.add(file);
			}
		}
		return rs;
	}
	
	/**
	 * Returns absolute path of temporal data folder. If it does not exist, this method creates it.
	 * @return
	 * @throws Exception
	 */
	public String getTmpFolderInData() throws Exception{
		String temporalFolder = getTmpFolderInData0();
		if(StringUtils.isEmpty(temporalFolder)){
			createTemporalDataFolder();
			temporalFolder = getTmpFolderInData0();
			if(StringUtils.isEmpty(temporalFolder)){
				throw new Exception("Temporal folder could not be created for the SIP " + this.getId());
			}
		}
		return temporalFolder;
	}
	
	/**
	 * Returns absolute path of temporal data folder
	 * @return
	 * @throws Exception
	 */
	private String getTmpFolderInData0() throws Exception{
		for(String folder : Services.getInstance().getStorageService().getFoldersNameInDataFolder(this)){
			if(folder.endsWith("_TP")){
				return getDataFolder() + "/" + folder;
			}
		}
		return null;
	}
	
	private void createTemporalDataFolder() throws Exception{
		String folderName = YYYY_MM_DD.format(this.getCreationTime()) + "_TP";
		Services.getInstance().getStorageService().createFolder(this, "data/" + folderName);
	}
	
	/*
	private String getFinalSIPDataFolder0() throws Exception{
		for(String folder : Services.getInstance().getStorageService().getFoldersNameInDataFolder(this)){
			if(folder.endsWith("_SIP")){
				return getDataFolder() + "/" + folder;
			}
		}
		return null;
	}*/
	

	/*
	public String getFinalSIPDataFolder() throws Exception{
		String finalFolder = getFinalSIPDataFolder0();
		if(StringUtils.isEmpty(finalFolder)){
			createFinalSIPDataFolder();
			finalFolder = getFinalSIPDataFolder0();
		}
		return finalFolder;
	}*/
	

	/*
	private void createFinalSIPDataFolder() throws Exception{
		String folderName = YYYY_MM_DD.format(new Date()) + "_SIP";
		Services.getInstance().getStorageService().createFolder(this, "data/" + folderName);
	}*/
	
	

	
	
	/**
	 * For SIP uses this schema (see: IANUS Konzept, Chaper 13):
	 * <Jahr (YYYY)>_<5-stellige Nummer (#####)>_SIP z.B. 2015_00001_SIP
	 * @throws Exception
	 */
	/*
	public void generateInternalLabel() throws Exception{
		if(this.isPersistent()){
			Calendar cal = Calendar.getInstance();
		    cal.setTime(getCreationTime());
			int year = Calendar.getInstance().get(Calendar.YEAR);
			this.collectionLabel = year + "_" + String.format("%05d", getId()) + "_SIP";
		}else{
			throw new Exception("The submission IP must be persistent to be able to generate its label");
		}
	}*/
	/*
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	*/
	
}
