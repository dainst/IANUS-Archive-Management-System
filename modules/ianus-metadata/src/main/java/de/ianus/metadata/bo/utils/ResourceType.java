package de.ianus.metadata.bo.utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import de.ianus.metadata.bo.MDEntry;

/**
 * Beschreibung Datensammlung.Allgemein.Ressourcentyp

 * @author jurzua
 *
 */
@Entity
@Table(name="ResourceType")
public class ResourceType extends MDEntry {

	public enum Type{
		raw_data, processed_data, interpretations, results, final_reports, data_for_publication, grey_literature
	}
	
	//Rohdaten, verarbeitete Daten, Interpretationen, Ergebnisse, Abschlussberichte, Daten zu einer  Publikation, Grey Literature, …
	
	//raw_data, processed_data, interpretations, results, final_reports, data_for_publication, grey_literature
	
	@Column(name="dcId")
	private Long dcId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private Type type = Type.raw_data;	

	
	public static ResourceType clone(ResourceType other, Long dcId){
		ResourceType newItem = new ResourceType();
		newItem.dcId = dcId;
		newItem.type = other.type;
		return newItem;
	}
	
	
	public Long getDcId() {
		return dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
