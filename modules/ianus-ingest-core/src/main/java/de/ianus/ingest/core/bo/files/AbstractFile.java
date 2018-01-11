package de.ianus.ingest.core.bo.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name="AbstractFile")
public class AbstractFile extends DBEntry{
	
	
	@Column(name="ipId")
	protected Long ipId;	
	
	// the InformationPackage class name
	@Column(name="wfipType")
	protected String wfipType;
	
	
}
