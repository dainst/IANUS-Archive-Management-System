package de.ianus.metadata.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.google.gson.JsonObject;

import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.RelatedResource;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.Time;

@Entity
@Table(name="Element")
public class Element extends MDEntry {
	
	@Column(name="sourceId")
	protected Long sourceId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="sourceClass")
	protected BOUtils.SourceClass sourceClass;
	
	/*
	@Enumerated(EnumType.STRING)
	@Column(name="sourceType")
	protected BOUtils.SourceType sourceType;
	*/
	
	/**
	 * This method checks if this object has already values in the attributes
	 * sourceId and sourceClass. If not, this object is not consistent.
	 */
	public boolean isConsistent(){
		return this.sourceId != null && this.sourceClass != null;
	}
	
	public static Element clone(Element newItem, Element other, Long sourceId){
		
		newItem.setSourceId(sourceId);
		newItem.setSourceClass(other.getSourceClass());
		//newItem.setSourceType(other.getSourceType());
		
		return newItem;
	}
	
	public void setSource(MDEntry source) throws Exception{
		this.sourceId = source.getId();
		
		if(source instanceof DataCollection){
			this.sourceClass = BOUtils.SourceClass.DataCollection;
		}else if(source instanceof Person){
			this.sourceClass = BOUtils.SourceClass.Person;
		}else if(source instanceof Institution){
			this.sourceClass = BOUtils.SourceClass.Institution;
		}else if(source instanceof Place){
			this.sourceClass = BOUtils.SourceClass.Place;
		}else if(source instanceof CollectionFile){
			this.sourceClass = BOUtils.SourceClass.CollectionFile;
		}else if(source instanceof Publication){
			this.sourceClass = BOUtils.SourceClass.Publication;
		}else if(source instanceof Time){
			this.sourceClass = BOUtils.SourceClass.Time;
		}else if(source instanceof RelatedResource){
			this.sourceClass = BOUtils.SourceClass.RelatedResource;
		}else{
			throw new Exception("The class " + source.getClass().getName() + " is not supported as source.");
		}   
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		if(this.sourceId != null){
			json.addProperty("sourceId", this.sourceId);
		}
		if(this.sourceClass != null){
			json.addProperty("sourceClass", this.sourceClass.toString());
		}
		return json;
	}
	
	protected static Boolean empty(String str) {
		return (str == null || str.equals(""));
	}
	
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public BOUtils.SourceClass getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(BOUtils.SourceClass sourceClass) {
		this.sourceClass = sourceClass;
	}
	
	public String getLabel(){
		return "sourceId=" + this.sourceId + ", sourceClass=" + this.sourceClass;
	}
}
