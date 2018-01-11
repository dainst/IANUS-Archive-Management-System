package de.ianus.ingest.core.bo.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name = "ObjectEventLink")
public class ObjectEventLink extends DBEntry {

	@Column(name = "objectId")
	private Long objectId;
	
	@Column(name = "objectType")
	private String objectType = FileInstance.class.getSimpleName();

	@Column(name = "eventId")
	private Long eventId;

	@Column(name = "objectRole")
	private String objectRole;
	
	
	public enum ObjectRole {
		input, output, log
	}
	
	
	
	public ObjectEventLink() {}
	
	public ObjectEventLink(AbstractFile object, Event event, ObjectRole role) {
		this.objectId = object.getId();
		this.eventId = event.getId();
		this.objectRole = role.toString();
		this.objectType = object.getClass().getSimpleName();
	}
	
	public String getObjectType() {
		return this.objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public Long getEventId() {
		return eventId;
	}

	public String getObjectRole() {
		return objectRole;
	}
}
