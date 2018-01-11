package de.ianus.metadata.bo;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.resource.RelatedResource;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Language;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.TextAttribute;

@Entity
@Table(name="IANUSEntity")
public class IANUSEntity extends Element {
	
	/**
	 * 
	 * Attributes represented as TextAttribute in DataCollection
	 * -title
	 * -alternativeTitle
	 * 
	 * -summary
	 * -projectDescription
	 * -dataCollectionDescription
	 * -classification
	 * 
	 */
	@Transient
	protected Map<TextAttribute.ContentType, Set<TextAttribute>> attributeMap = new HashMap<TextAttribute.ContentType, Set<TextAttribute>>();
	
	//Identifikator
	//-------------------------------------------
	@Transient
	private Set<Identifier> internalIdList = new LinkedHashSet<Identifier>();
		
	@Transient
	private Set<Identifier> externalIdList = new LinkedHashSet<Identifier>();
	
	//Version
	//-------------------------------------------
	@Column(name="version")
	private Integer version;
	
	@Transient
	private Set<Place> placeList = new LinkedHashSet<Place>();
	
	@Transient
	private Set<Reference> referenceList = new LinkedHashSet<Reference>();
	
	@Transient
	private Set<RelatedResource> relatedResourceList = new LinkedHashSet<RelatedResource>();
	
	@Transient
	private Set<ActorRole> roleList = new LinkedHashSet<ActorRole>();
	
	@Transient
	private Set<Language> metadataLanguageList = new LinkedHashSet<Language>();
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.add("internalIdList", Identifier.toJsonArray(this.getInternalIdList()));
		json.add("externalIdList", Identifier.toJsonArray(this.getExternalIdList()));
		
		//-----------------------
		json.addProperty("version", this.version);
		
		for(TextAttribute.ContentType contentType : attributeMap.keySet()){
			Set<TextAttribute> attList = attributeMap.get(contentType);
			JsonArray array = new JsonArray();
			for(TextAttribute att : attList){
				array.add(att.toJsonObject(new JsonObject()));
			}
			json.add(contentType + "List", array);
		}
		
		//##### Ausdehnung ######
		json.add("placeList", Place.toJsonArray(this.getPlaceList()));
		
		//##### References ######
		json.add("referenceList", Reference.toJsonArray(this.getReferenceList()));
		
		json.add("relatedResourceList", RelatedResource.toJsonArray(this.getRelatedResourceList()));
		
		//------------------------
		json.add("metadataLanguageList", Language.toJsonArray(this.getMetadataLanguageList()));
		
		return json;
	}
	
	public Set<TextAttribute> getAttributeList(TextAttribute.ContentType contentType){
		if(!this.attributeMap.containsKey(contentType)){
			this.attributeMap.put(contentType, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(contentType);
	}
	
	public Map<TextAttribute.ContentType, Set<TextAttribute>> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<TextAttribute.ContentType, Set<TextAttribute>> attributeMap) {
		this.attributeMap = attributeMap;
	}
	
	public void setAttribute(TextAttribute.ContentType type, Set<TextAttribute> attributes) {
		this.attributeMap.put(type, attributes);
	}
	

	public Set<Identifier> getInternalIdList() {
		return internalIdList;
	}


	public void setInternalIdList(Set<Identifier> internalIdList) {
		this.internalIdList = internalIdList;
	}


	public Set<Identifier> getExternalIdList() {
		return externalIdList;
	}


	public void setExternalIdList(Set<Identifier> externalIdList) {
		this.externalIdList = externalIdList;
	}
	
	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}

	public Set<Place> getPlaceList() {
		return placeList;
	}

	public void setPlaceList(Set<Place> placeList) {
		this.placeList = placeList;
	}
	
	public Set<Reference> getReferenceList() {
		return referenceList;
	}

	public void setReferenceList(Set<Reference> referenceList) {
		this.referenceList = referenceList;
	}
	
	public Set<RelatedResource> getRelatedResourceList() {
		return relatedResourceList;
	}

	public void setRelatedResourceList(Set<RelatedResource> relatedResourceList) {
		this.relatedResourceList = relatedResourceList;
	}

	public Set<ActorRole> getRoleList() {
		return roleList;
	}
	
	public Set<ActorRole> getRoleList(ActorRole.Type type){
		Set<ActorRole> rs = new LinkedHashSet<ActorRole>();
		for(ActorRole role : this.roleList){
			if(role.getTypeId().equals(type.id)){
				rs.add(role);
			}
		}
		return rs;
	}
	
	public Set<ActorRole> getRoleList(Actor actor){
		Set<ActorRole> rs = new LinkedHashSet<ActorRole>();
		for(ActorRole role : this.roleList){
			if(role.getActorId() != null &&
					actor.getId() != null &&
					role.getActorId().equals(actor.getId())){
				rs.add(role);
			}
		}
		return rs;
	}
	
	public boolean actorHasRole(ActorRole.Type roleType, Actor actor){
		for(ActorRole role : this.getRoleList(actor)){
			if(role.getTypeId().equals(roleType.id)){
				return true;
			}
		}
		return true;
	}

	public void setRoleList(Set<ActorRole> roleList) {
		this.roleList = roleList;
	}
	
	public Set<Language> getMetadataLanguageList() {
		return metadataLanguageList;
	}

	public void setMetadataLanguageList(Set<Language> metadataLanguageList) {
		this.metadataLanguageList = metadataLanguageList;
	}
}
