package de.ianus.metadata.bo.utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.ianus.metadata.bo.MDEntry;

/**
 * Beschreibung Datensammlung.Technische Angaben.Dateiformate
 * @author jurzua
 *
 */
@Entity(name="FileFormat")
public class FileFormat extends MDEntry{
	
	@Column(name="sourceId")
	private Long sourceId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="sourceClass")
	private BOUtils.SourceClass sourceClass;
	
	@Enumerated(EnumType.STRING)
	@Column(name="format")
	private Type format;

	@Column(name="version")
	private String version;
	
	@Column(name="pronomId")
	private Integer pronomId;
	
	
	public static FileFormat clone(FileFormat other, Long sourceId){
		FileFormat newEntry = new FileFormat();
		newEntry.sourceId = sourceId;
		newEntry.sourceClass = other.sourceClass;
		newEntry.format = other.format;
		newEntry.version = other.version;
		newEntry.pronomId = other.pronomId;
		return newEntry;
	}
	
	public Type getFormat() {
		return format;
	}

	public void setFormat(Type format) {
		this.format = format;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getPronomId() {
		return pronomId;
	}

	public void setPronomId(Integer pronomId) {
		this.pronomId = pronomId;
	}
	
	public BOUtils.SourceClass getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(BOUtils.SourceClass sourceClass) {
		this.sourceClass = sourceClass;
	}



	public enum Type{
		PDF_Dokumente, Textdokumente, Rastergrafiken, Vektorgrafiken, Tabellen, Datenbanken, GIS, Statistische_Daten, Bewegte_Bilder, 
		Videos, Audio, A3_D, Virtual_Reality, Präsentationen
	}
}
