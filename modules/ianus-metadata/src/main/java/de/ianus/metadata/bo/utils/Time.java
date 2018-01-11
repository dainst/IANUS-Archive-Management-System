package de.ianus.metadata.bo.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.xml.XMLObject;

@Entity
@Table(name="Time")
public class Time extends Element {

	@Column(name="start")
	private String start;
	
	@Column(name="end")
	private String end;
	
	@Transient
	private ElementOfList mainPeriod;
	
	@Transient
	private ElementOfList  subPeriod;
	
	@Transient
	private Set<TextAttribute> commentList = new HashSet<TextAttribute>();
	
	public static Time clone(Time other, Long sourceId){
		Time newItem = new Time();
		newItem = (Time)Element.clone(newItem, other, sourceId);
		newItem.start = other.start;
		newItem.end = other.end;
		return newItem;
	}
	
	public String getDynamicLabel(){
		StringBuilder sb = new StringBuilder();
		
		sb.append((mainPeriod != null) ? mainPeriod.getValue() + " " : "");
		sb.append((subPeriod != null) ? subPeriod.getValue() + ", " : "");
		sb.append(this.start + ((StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) ? " - " : "") + end);
		
		return sb.toString();
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
	
	public ElementOfList getMainPeriod() {
		return mainPeriod;
	}

	public void setMainPeriod(ElementOfList mainPeriod) {
		this.mainPeriod = mainPeriod;
	}

	public ElementOfList getSubPeriod() {
		return subPeriod;
	}

	public void setSubPeriod(ElementOfList subPeriod) {
		this.subPeriod = subPeriod;
	}

	public Set<TextAttribute> getCommentList() {
		return commentList;
	}

	public void setCommentList(Set<TextAttribute> commentList) {
		this.commentList = commentList;
	}

	
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("start", this.getStart());
		json.addProperty("end", this.getEnd());
		json.add("mainPeriod", this.getMainPeriod().toJsonObject(new JsonObject()));
		json.add("subPeriod", this.getSubPeriod().toJsonObject(new JsonObject()));
		json.add("commentList", TextAttribute.toJsonArray(this.getCommentList()));
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<Time> list){
		JsonArray array = new JsonArray();
		for(Time item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("TimeDataType");
			xml.addAttribute("start", this.getStart());
			xml.addAttribute("end", this.getEnd());
			
			XMLObject mainPeriod = new XMLObject("MainPeriod");
			XMLObject subPeriod = new XMLObject("SubPeriod");
			if(this.getMainPeriod() != null) {
				mainPeriod.addDescendant(this.getMainPeriod().toXMLObject());
				xml.addDescendant(mainPeriod);
			}
			if(this.getSubPeriod() != null) { 
				subPeriod.addDescendant(this.getSubPeriod().toXMLObject());
				xml.addDescendant(subPeriod);
			}
			
			XMLObject comment = new XMLObject("Comment");
			comment.addDescendants(TextAttribute.toXMLObjects(this.getCommentList()));
			if(comment.hasDescendants()) xml.addDescendant(comment);
		
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<Time> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Time item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=" + this.id + ", srcId=" + this.sourceId + ", sourceClass=" + sourceClass + ", start=" + start + ", end=" + end + "] ");
		return sb.toString();
	}
	
}
