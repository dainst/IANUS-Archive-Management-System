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
import de.ianus.metadata.utils.DisciplineList;
import de.ianus.metadata.utils.MainContentAdexTypeList;
import de.ianus.metadata.utils.MethodList;
import de.ianus.metadata.utils.ValueListUtils;
import de.ianus.metadata.xml.XMLObject;

/**
 * Schlagworte.Inhalt ( IANUS/ADS, DAI-Thesaurus, GND, …)
 * @author jurzua
 *
 */
@Entity
@Table(name="ElementOfList")
public class ElementOfList extends Element {
	
	@Enumerated(EnumType.STRING)
	@Column(name="contentType")
	private ContentType contentType;
	
	@Column(name="listId")
	private String listId;
	
	@Column(name="value", columnDefinition = "TEXT")
	private String value;
	
	@Column(name="valueId")
	private String valueId;
	
	@Column(name="uri")
	private String uri;
	
	public ElementOfList(){}
	
	@Override
	public boolean isConsistent(){
		return super.isConsistent() && this.contentType != null;
	}
	
	public String getHtmlValue(){
		if(ContentType.mainDiscipline.equals(contentType)){
			return getHtml(DisciplineList.MainDiscipline.getLabelGer(this.valueId), DisciplineList.MainDiscipline.getLabelEng(this.valueId));
		}else if(ContentType.subDiscipline.equals(contentType)){
			return getHtml(DisciplineList.SubDiscipline.getLabelGer(this.valueId), DisciplineList.SubDiscipline.getLabelEng(this.valueId));
		}else if(ContentType.mainContent.equals(contentType) && StringUtils.equals(ValueListUtils.Names.adex_type_general.id, this.listId)){
			return getHtml(MainContentAdexTypeList.Type.getLabelGer(valueId), MainContentAdexTypeList.Type.getLabelEng(this.valueId));
		}else if(ContentType.classification.equals(contentType)){
			return getHtml(Classification.Type.getLabelGer(valueId), Classification.Type.getLabelEng(this.valueId));
		}else if(ContentType.dataCategory.equals(contentType)){
			return getHtml(DataCategory.Type.getLabelGer(valueId), DataCategory.Type.getLabelEng(this.valueId));
		}else if(ContentType.mainMethod.equals(contentType)){
			return getHtml(MethodList.MainMethod.getLabelGer(valueId), MethodList.MainMethod.getLabelEng(this.valueId));
		}else if(ContentType.subMethod.equals(contentType) && StringUtils.equals(MethodList.LIST_ID_SUB, this.listId)){
			return getHtml(MethodList.SubMethod.getLabelGer(valueId), MethodList.SubMethod.getLabelEng(this.valueId));
		}else if(ContentType.resourceType.equals(contentType)){
			return getHtml(Resource.Type.getLabelGer(valueId), Resource.Type.getLabelEng(this.valueId));
		}
		return "<p>" + this.value + "</p>";
	}
	
	public void reset(){
		this.listId = null;
		this.value = null;
		this.valueId = null;
	}
	
	private String getHtml(String ger, String eng){
		StringBuilder sb = new StringBuilder();
		sb.append("<table border='0'><tbody>");
		sb.append("<tr>");
		sb.append("<td width='20%' style='border: none;'>deu</td>");
		sb.append("<td width='80%' style='border: none;'>" + ger + "</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td width='20%' style='border: none;'>eng</td>");
		sb.append("<td width='80%' style='border: none;'>" + eng + "</td>");
		sb.append("</tr>");
		sb.append("</tbody></table> ");
		return sb.toString();
	}
	
	public ElementOfList(String listId, String valueId, String value, String uri){
		this.listId = listId;
		this.valueId = valueId;
		this.value = value;
		this.uri = uri;
	}
	
	public static ElementOfList clone(ElementOfList other, Long sourceId){
		ElementOfList newItem = new ElementOfList();
		newItem = (ElementOfList)Element.clone(newItem, other, sourceId);
		
		newItem.contentType = other.contentType;
		newItem.listId = other.listId;
		newItem.value = other.value;
		newItem.valueId = other.valueId;
		newItem.uri = other.uri;
		
		return newItem;
	}
	
	public boolean isCompleted(){
		return 
				StringUtils.isNotEmpty(listId) &&
				StringUtils.isNotEmpty(value) &&
				StringUtils.isNotEmpty(valueId);
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("listId", this.listId);
		json.addProperty("value", this.value);
		json.addProperty("valueId", this.valueId);
		json.addProperty("uri", this.uri);
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<ElementOfList> list){
		JsonArray array = new JsonArray();
		for(ElementOfList item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("ElementOfListDataType");
			if(!empty(this.value))		xml.addAttribute("Value", this.value);
			if(!empty(this.valueId))	xml.addAttribute("ValueID", this.valueId);
			if(!empty(this.listId))		xml.addAttribute("ListName", this.listId);
			if(!empty(this.uri))		xml.addAttribute("URI", this.uri);
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<ElementOfList> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(ElementOfList item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	public enum ContentType{
		mainDiscipline,
		subDiscipline,
		mainContent,
		subContent,
		mainMethod,
		subMethod,
		mainPeriod,
		subPeriod,
		discipline,
		identifier, keyword, pid,		
		dataCategory,
		classification,
		resourceType,
		reasonDataProtection
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=" + this.id + 
				", contentType=" + getContentType() + 
				", sourceClass=" + getSourceClass() + 
				", sourceId=" + getSourceId() + 
				", value=" + value + 
				", listId=" + listId + 
				", uri=" + uri + "] ");
		return sb.toString();
	}
}
