package de.ianus.metadata.bo.resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.ianus.metadata.bo.utils.ExternalResourceRelation;


/**
 * Relevante Ressourcen.Zugehörige Quellen
 * @author jurzua
 *
 */
@Entity(name="ExternalResource")
public class ExternalResource extends Resource{

	enum Type{
		raw_data, processed_data, interpretations, results, final_reports, data_for_publication, grey_literature
	}
	
	//Ressource_Beziehung
	//Code List
	@Enumerated(EnumType.STRING)
	@Column(name="relation")
	private ExternalResourceRelation relation;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private Type type = Type.raw_data;
	
	//Beschreibung
	@Column(name="description")
	private String description;
	
	//Umfang
	@Column(name="scope")
	private String scope;
	
	//Aufbewahrungsort
	@Column(name="placeStorage")
	private String placeStorage;

	public ExternalResourceRelation getRelation() {
		return relation;
	}

	public void setRelation(ExternalResourceRelation relation) {
		this.relation = relation;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getPlaceStorage() {
		return placeStorage;
	}

	public void setPlaceStorage(String placeStorage) {
		this.placeStorage = placeStorage;
	}
}
