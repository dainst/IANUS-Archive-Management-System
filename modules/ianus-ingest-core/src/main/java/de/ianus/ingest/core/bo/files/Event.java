package de.ianus.ingest.core.bo.files;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;
import de.ianus.ingest.core.bo.WorkflowIP;

@Entity
@Table(name="Event")
public class Event extends DBEntry{

	public static enum Outcome {
		ok, conflicts, failure
	}
	
	public Event(WorkflowIP wfip, String name, Outcome outcome){
		this.wfipId = wfip.getId();
		this.wfipType = wfip.getClass().getSimpleName();
		this.outcome = outcome.toString();
		this.eventName = name;
	}
	
	
	public Event() {}
	
	/**
	 * clone constructor
	 * 
	 * @param wfip
	 * @param name
	 * @param outcome
	 * @param description
	 * @param date
	 */
	public Event(WorkflowIP wfip, String name, String outcome, String description, Date created, Date modified) {
		this.wfipId = wfip.getId();
		this.wfipType = wfip.getClass().getSimpleName();
		this.outcome = outcome;
		this.eventName = name;
		this.outcomeDescription = description;
		this.setLastChange(modified);
		this.setCreationTime(created);
	}
	
	public Event clone(WorkflowIP newIp) {
		return new Event(newIp, this.getEventName(), this.getOutcome(), 
				this.getOutcomeDescription(), this.getCreationTime(), this.getLastChange());
		// TODO: replicate object relations (FileInstances, FileGroups, FileConcepts, Agents)!!!
	}
	
	
	// Assoziate Event to WorkflowIP. 
	// During IP transitions, the existing events have to be cloned to the new WorkflowIP! 
	@Column(name="wfipId")
	private Long wfipId;
	
	@Column(name="wfipType")
	private String wfipType;
	
	//derivation, evaluation, validation, migration, ingestion
	@Column(name="eventName")
    private String eventName;
	
	//success, failure, etc.
	@Column(name="outcome")
	private String outcome;
	
	@Column(name="outcomeDescription")
	private String outcomeDescription;
	
	
	
	public Long getWfipId() {
		return this.wfipId;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public String getOutcome() {
		return outcome.toString();
	}

	public void setOutcome(Outcome outcome) {
		this.outcome = outcome.toString();
	}
	
	public String getOutcomeDescription() {
		return outcomeDescription;
	}

	public void setOutcomeDescription(String outcomeDescription) {
		this.outcomeDescription = outcomeDescription;
	}
	
	
}
