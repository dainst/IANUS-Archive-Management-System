package de.ianus.metadata.bo.collectionFile;

import javax.persistence.Column;
import javax.persistence.Entity;

import de.ianus.metadata.bo.MDEntry;

@Entity(name="DatabaseTable")
public class DatabaseTable extends MDEntry{

	@Column(name="name")
	private String name;
	
	@Column(name="description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name="dsNumber")
	private Integer dsNumber;
	
	@Column(name="attributeNumber")
	private Integer attributeNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDsNumber() {
		return dsNumber;
	}

	public void setDsNumber(Integer dsNumber) {
		this.dsNumber = dsNumber;
	}

	public Integer getAttributeNumber() {
		return attributeNumber;
	}

	public void setAttributeNumber(Integer attributeNumber) {
		this.attributeNumber = attributeNumber;
	}
}
