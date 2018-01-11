package de.ianus.metadata.bo.utils;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.xml.XMLObject;

@Entity(name="Language")
public class Language extends Element{
	
	@Enumerated(EnumType.STRING)
	@Column(name="contentType")
	private ContentType contentType;
	
	@Column(name="code")
	private String code;

	@Column(name="label")
	private String label;
	
	public static Language clone(Language other, Long sourceId){
		Language newItem = new Language();
		newItem = (Language)Element.clone(newItem, other, sourceId);
		newItem.contentType = other.contentType;
		newItem.code = other.code;
		newItem.label = other.label;
		return newItem;
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("contentType", this.contentType.toString());
		json.addProperty("code", this.code);
		json.addProperty("label", this.label);
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<Language> list){
		JsonArray array = new JsonArray();
		for(Language item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("LanguageDataType");
			if(!empty(this.code))			xml.addAttribute("ISO_639_3", this.code);
			if(!empty(this.label))			xml.addAttribute("Label", this.label);
			if(this.contentType != null)	xml.addAttribute("ContentType", this.contentType.toString());
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<Language> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Language item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=" + this.id + ", srcId=" + this.sourceId + ", sourceClass=" + sourceClass + ", contentType=" + contentType + ", label=" + label + ", code=" + code + "] ");
		return sb.toString();
	}

	public enum ContentType{
		metadata_language, collection_language
	}
}
