package de.ianus.metadata.bo.utils;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.ianus.metadata.bo.Element;

@Entity
@Table(name="PreservationEvent")
public class PreservationEvent extends Element{

	@Column(name="processId")
	private String processId;
	
	@Column(name="type")
	private String type;
	
	@Column(name="description")
	private String description;
	
	@Column(name="outputFormat")
	private String outputFormat;
	
	@Column(name="intermediateFormat")
	private String intermediateFormat;
	
	@Column(name="targetFormat")
	private String targetFormat;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="start")
    private Date start;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="end")
    private Date end;
	
	@Transient
	private Set<TextAttribute> actorList = new LinkedHashSet<TextAttribute>();
	
	@Transient
	private Set<Software> softwareList = new LinkedHashSet<Software>();

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getIntermediateFormat() {
		return intermediateFormat;
	}

	public void setIntermediateFormat(String intermediateFormat) {
		this.intermediateFormat = intermediateFormat;
	}

	public String getTargetFormat() {
		return targetFormat;
	}

	public void setTargetFormat(String targetFormat) {
		this.targetFormat = targetFormat;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Set<TextAttribute> getActorList() {
		return actorList;
	}

	public void setActorList(Set<TextAttribute> actorList) {
		this.actorList = actorList;
	}

	public Set<Software> getSoftwareList() {
		return softwareList;
	}

	public void setSoftwareList(Set<Software> softwareList) {
		this.softwareList = softwareList;
	}
}
