package de.ianus.metadata.bo.resource;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.ianus.metadata.bo.MDEntry;

@Entity(name="Resource")
public abstract class Resource extends MDEntry{
	
	@Column(name="dcId")
	private Long dcId;

	public Long getDcId() {
		return dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}
}
