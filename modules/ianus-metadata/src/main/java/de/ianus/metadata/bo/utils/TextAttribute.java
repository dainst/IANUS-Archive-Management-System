package de.ianus.metadata.bo.utils;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.utils.ISOLanguage;
import de.ianus.metadata.utils.LanguageList;
import de.ianus.metadata.xml.XMLObject;

@Entity
@Table(name="TextAttribute")
public class TextAttribute extends Element{
	
	@Enumerated(EnumType.STRING)
	@Column(name="contentType")
	private ContentType contentType;
	
	@Column(name="label")
	private String label;
	
	@Column(name="value", columnDefinition = "TEXT")
	private String value;
	
	//ISO-639-3
	@Column(name="languageCode")
	private String languageCode;
	
	public TextAttribute(){}
	
	public TextAttribute(TextAttribute.ContentType contentType, String languageCode){
		this.contentType = contentType;
		this.languageCode = languageCode;
	}
	public static TextAttribute clone(TextAttribute other, Long sourceId){
		
		TextAttribute newItem = new TextAttribute();
		newItem = (TextAttribute)Element.clone(newItem, other, sourceId);
		newItem.setContentType(other.getContentType());
		newItem.setLabel(other.getLabel());
		newItem.setValue(other.getValue());
		newItem.setLanguageCode(other.getLanguageCode());
		
		return newItem;
	}
	
	public boolean isCompleted(boolean useLanguage){
		return StringUtils.isNotEmpty(this.value) && (!useLanguage || StringUtils.isNotEmpty(this.languageCode));
	}
	
	public String getLanguage(){
		if(StringUtils.isNotEmpty(languageCode)){
			ISOLanguage lan = LanguageList.getById(languageCode);
			return (lan != null) ? lan.getNameEng() + " [" + lan.getId() + "]" : languageCode + " not found";
		}
		return null;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getLabel() {
		return label;
	}
	
	public String getValueShort(){
		return (StringUtils.length(value) > 50) ? 
				StringUtils.substring(this.value, 0, 50) + "..." : this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		json.addProperty("contentType", this.contentType.toString());
		json.addProperty("value", this.value);
		json.addProperty("languageCode", this.languageCode);
		return json;
	}
	
	public static JsonArray toJsonArray(Set<TextAttribute> list){
		JsonArray array = new JsonArray();
		for(TextAttribute item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("TextAttributeDataType");
			xml.addAttribute("Value", this.getValue());
			xml.addAttribute("LanguageCode", this.getLanguageCode());
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<TextAttribute> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(TextAttribute item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	@Override
	public String toString(){
		return "TextAttr [id="+id+", value="+ getValueShort() +", label=" + label + ", langCode=" + languageCode + "]";
	}

	public enum ContentType{
		email, url, telephone, alternativeTitle, freeDescription, historicalName, projectDescription, title, summary,
		accuracyDescription, greaterRegion, country, regionProvince, city, gazetteer, shortDescription, longDescription, publisher,
		typeOfArea, dataCollectionDescription, description, descriptionDataProtection, motivation, comment
	}
}
