package de.ianus.ingest.core.bo.files;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.ianus.ingest.core.bo.InformationPackage;

/**
 * 
 * 
 * 
 * 
 * drop table ConceptFile;
 * ALTER TABLE FileInstance DROP conceptFileId;
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name="FileConcept")
public class FileConcept extends AbstractFile {

	@Column(name="type")
	private String type = Type.created_automatically.toString();
	
	
	@Transient
	private List<FileInstance> fileInstanceList = new ArrayList<FileInstance>();

	public FileConcept(){}
	
	public FileConcept(InformationPackage ip){
		this.ipId = ip.getId();
		this.wfipType = ip.getClass().getName();
	}
	
	public static FileConcept clone(FileConcept other, InformationPackage ip){
		FileConcept clone = new FileConcept(ip);
		return clone;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FileInstance> getFileInstanceList() {
		return fileInstanceList;
	}

	public void setFileInstanceList(List<FileInstance> fileInstanceList) {
		this.fileInstanceList = fileInstanceList;
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
	
	public String toString(){
		return this.getClass().getSimpleName() + " [id=" + getId() + ", type=" + type + "]";
	}
	
	public enum Type{
		created_automatically,
		file_dependencies,
		representation_versions
	}
}
