package de.ianus.metadata.bo.utils;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.xml.XMLObject;

/**
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name="TimeSpan")
public class TimeSpan extends  Element{

	@Enumerated(EnumType.STRING)
	@Column(name="contentType")
	private ContentType contentType = ContentType.data_generation;

	//ALTER TABLE TimeSpan DROP COLUMN start;
	@Column(name = "start")
	private String start;

	//ALTER TABLE TimeSpan DROP COLUMN end;
	@Column(name = "end")
	private String end;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	public static TimeSpan clone(TimeSpan other, Long sourceId){
		TimeSpan newEntry = new TimeSpan();
		newEntry = (TimeSpan)Element.clone(newEntry, other, sourceId);
		newEntry.contentType = other.contentType;
		newEntry.start = other.start;
		newEntry.end = other.end;
		newEntry.description = other.description;
		return newEntry;
	}
	
	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel(){
		return start + " - " + end;
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("end", this.start);
		json.addProperty("start", this.end);
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<TimeSpan> list){
		JsonArray array = new JsonArray();
		for(TimeSpan item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("TimeSpanDataType");
		xml.addAttribute("ContentType", this.getContentType().toString());
		xml.addAttribute("Start", this.getStart());
		xml.addAttribute("End", this.getEnd());
		xml.addAttribute("Description", this.getDescription());
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<TimeSpan> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(TimeSpan item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	public enum ContentType{
		data_generation
	}
}
