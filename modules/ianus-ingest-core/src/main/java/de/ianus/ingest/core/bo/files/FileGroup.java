package de.ianus.ingest.core.bo.files;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.ianus.ingest.core.bo.InformationPackage;

@Entity
@Table(name="FileGroup")
public class FileGroup extends AbstractFile {
	
	
	@Transient
	private List<FileInstance> fileInstanceList = new ArrayList<FileInstance>();
	
	
	
	public FileGroup(){}
	
	public FileGroup(InformationPackage ip){
		this.ipId = ip.getId();
		this.wfipType = ip.getClass().getName();
	}
	
	public static FileGroup clone(FileGroup other, InformationPackage ip){
		FileGroup clone = new FileGroup(ip);
		return clone;
	}
	
	public Long getIpId() {
		return this.ipId;
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

	public List<FileInstance> getFileInstanceList() {
		return fileInstanceList;
	}

	public void setFileInstanceList(List<FileInstance> fileInstanceList) {
		this.fileInstanceList = fileInstanceList;
	}
	
	public String toString(){
		return this.getClass().getSimpleName() + " [id=" + id + "]";
	}
}
