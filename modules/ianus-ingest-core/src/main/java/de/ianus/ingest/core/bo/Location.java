package de.ianus.ingest.core.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.utils.MemoryUtil;

/**
 * 
 * This class represents a directory on the server side that is accesses using the storage service.
 * The storage services has several methods (move, copy, remove files/directories) that allow the interaction with locations.
 * The attribute called purpose indicates the storage service the for which activities a location cab be used.
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name = "Location")
public class Location   extends DBEntry implements Serializable{
	private static final long serialVersionUID = 3414200007782701304L;
	
	@Column(name="absolutePath")
	private String absolutePath;
	
	@Enumerated(EnumType.STRING)
	@Column(name="purpose")
	private LocationPurpose purpose;
	
	@Column(name="description")
	private String description;
	
	public Long getFreeSpaceInBytes(){
		return Services.getInstance().getStorageService().getFreeSpaceInBytes(this);
	}
	
	public String getFreeSpaceFormatted(){
		Long spaceInBytes = Services.getInstance().getStorageService().getFreeSpaceInBytes(this); 
		return MemoryUtil.formatBytes(spaceInBytes);
	}

	public boolean getCanConnect(){
		return Services.getInstance().getStorageService().getCanConnectToLocation(this);
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public LocationPurpose getPurpose() {
		return purpose;
	}

	public void setPurpose(LocationPurpose purpose) {
		this.purpose = purpose;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}
