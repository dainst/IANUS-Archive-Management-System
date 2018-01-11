package de.ianus.metadata.bo.actor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.gson.JsonObject;

import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.json.JSONUtils;
import de.ianus.metadata.xml.XMLObject;

@Entity
@Table(name="Institution")
public class Institution extends Actor{
	
	@Column(name="name")
	private String name;
	
	public static Institution createFromJsonObject(JsonObject json){
		Institution institution = new Institution();
		institution = (Institution)Actor.createFromJsonObject(institution, json);
		institution.setName(JSONUtils.getString(json, "name"));
		return institution;
	}
	
	public static Institution clone(Institution other, MDEntry source) throws Exception{
		Institution newItem = (Institution)Actor.clone(new Institution(), (Actor)other, source);
		newItem.name = other.name;
		return newItem;
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		json.addProperty("name", this.name);
		return json;
	}
	
	@Override
	public XMLObject toXMLObject() {
		XMLObject xml = super.toXMLObject();
			if(!empty(this.name)) xml.addAttribute("InstitutionName", this.name);
		return xml;
	}
	
	@Override
	public String getLabel(){
		return "Institution: " + name + ", " + getDepartment();
	}
	
	@Override
	public String getHtmlLabel(){
		StringBuilder sb = new StringBuilder();
		sb.append("<p>" + this.name + "</p>");
		sb.append("<p>" + this.getCity() + "</p>");
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
