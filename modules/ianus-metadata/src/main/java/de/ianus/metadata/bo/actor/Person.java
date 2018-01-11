package de.ianus.metadata.bo.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.utils.Country.Type;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.json.JSONUtils;
import de.ianus.metadata.xml.XMLObject;

@Entity
@Table(name="Person")
public class Person extends Actor{

	@Column(name="firstName")
	private String firstName;
	
	@Column(name="lastName")
	private String lastName;
	
	@Column(name="institutionName")
	private String institutionName;
	
	//@Enumerated(EnumType.STRING)
	@Column(name="title")
	private String title;
	
	//@Enumerated(EnumType.STRING)
	@Column(name="gender")
	private String gender = Gender.masculine.toString();
	
	/**
	 * @deprecated
	 */
	@Column(nullable = false, name="authorData", columnDefinition = "TINYINT", length = 1)
	private boolean authorData = false;
	
	/**
	 * @deprecated
	 */
	@Column(nullable = false, name="authorMetadata", columnDefinition = "TINYINT", length = 1)
	private boolean authorMetadata = false;
	
	@Transient
	private Set<ElementOfList> subDisciplineList = new LinkedHashSet<ElementOfList>();
	
	public static Person createFromJsonObject(JsonObject json){
		Person person = new Person();
		person = (Person)Actor.createFromJsonObject(person, json);
		
		person.setFirstName(JSONUtils.getString(json, "firstName"));
		person.setLastName(JSONUtils.getString(json, "lastName"));
		person.setInstitutionName(JSONUtils.getString(json, "institutionName"));
		person.setAuthorData(JSONUtils.getBoolean(json, "authorData"));
		person.setAuthorMetadata(JSONUtils.getBoolean(json, "authorMetadata"));
		person.setTitle(JSONUtils.getString(json, "title"));
		person.setGender(JSONUtils.getString(json, "gender"));
		
		return person;
	}
	
	/**
	 * <p>This method is used to clone the given DataCollection. This method clones only the attributes that are modeled as primitive data types (Long, String, Boolean, etc). 
	 * More complex attributes should be cloned by the MetadataService. The reason for this decision is that complex attributes have ids and are stored in the DB.
	 * </p>
	 * @param other
	 * @param dcId
	 * @return
	 * @throws Exception 
	 */
	public static Person clone(Person other, MDEntry source) throws Exception{
		Person newItem = (Person)Actor.clone(new Person(), (Actor)other, source);
		//Person newItem = new Person();
		//newItem.setDcId(dcId);
		//newItem.setSource(source);

		newItem.firstName = other.firstName;
		newItem.lastName = other.lastName;
		newItem.authorData = other.authorData;
		newItem.authorMetadata = other.authorMetadata;
		newItem.gender = other.gender;
		newItem.title = other.title;
		newItem.institutionName = other.institutionName;
		
		return newItem;
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("firstName", this.firstName);
		json.addProperty("lastName", this.lastName);
		json.addProperty("title", this.title);
		json.addProperty("gender", this.gender);
		json.addProperty("authorData", this.authorData);
		json.addProperty("authorMetadata", this.authorMetadata);
		
		json.addProperty("institutionName", this.institutionName);
		
		JsonArray disciplineArray = new JsonArray();
		for(ElementOfList item : getSubDisciplineList()){
			disciplineArray.add(item.toJsonObject(new JsonObject()));
		}
		json.add("subDisciplineList", disciplineArray);
		
		return json;
	}
	
	@Override
	public XMLObject toXMLObject() {
		XMLObject xml = super.toXMLObject();
			if(!empty(this.gender))		xml.addAttribute("Gender", this.gender);
			if(!empty(this.title))		xml.addAttribute("Title", this.title);
			if(!empty(this.firstName))	xml.addAttribute("FirstName", this.firstName);
			if(!empty(this.lastName))	xml.addAttribute("LastName", this.lastName);
			if(!empty(this.institutionName))
				xml.addAttribute("InstitutionName", this.institutionName);
			
		XMLObject disciplines = new XMLObject("Discipline");
			disciplines.addDescendants(ElementOfList.toXMLObjects(this.getSubDisciplineList()));
		if(disciplines.hasDescendants()) xml.addDescendant(disciplines);
		
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects0(Set<Person> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Person item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	public boolean containsSubDiscipline(String listId, String valueId){
		for(ElementOfList eol : this.getSubDisciplineList()){
			if(StringUtils.equals(listId, eol.getListId()) && StringUtils.equals(valueId, eol.getValueId())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getHtmlLabel(){
		StringBuilder sb = new StringBuilder();
		sb.append("<p>" + this.title + " " + this.firstName + " " + this.lastName + "</p>");
		sb.append("<p>" + this.institutionName + "</p>");
		sb.append("<p>" + this.getDepartment() + " " + this.getWorkingUnit() + "</p>");
		sb.append("<p>" + this.getCity() + "</p>");
		return sb.toString();
	}
	
	@Override
	public String getLabel(){
		return "Person: " + firstName + " " + lastName + ", gender: " + this.gender + ", title: " + this.title;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	/**
	 * @deprecated
	 */
	public boolean isAuthorData() {
		return authorData;
	}
	
	/**
	 * @deprecated
	 */
	public void setAuthorData(boolean authorData) {
		this.authorData = authorData;
	}
	
	/**
	 * @deprecated
	 */
	public boolean isAuthorMetadata() {
		return authorMetadata;
	}

	/**
	 * @deprecated
	 */
	public void setAuthorMetadata(boolean authorMetadata) {
		this.authorMetadata = authorMetadata;
	}

	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}
	
	public Set<ElementOfList> getSubDisciplineList() {
		return subDisciplineList;
	}

	public void setSubDisciplineList(Set<ElementOfList> subDisciplineList) {
		this.subDisciplineList = subDisciplineList;
	}



	public enum Gender{
		
		masculine 	("Herr", "masculine", "Herr"), 
		female		("Frau", "female", "Frau") 
		
		;
		public String id;
		public String labelEng;
		public String labelGer;
		
		private Gender(String id, String labelEng, String labelGer){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
		}
		
		public static String getLabelEng(String valueId){
			for(Type type : Type.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(Type type : Type.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}

	@Override
	public String toString(){
		return "Person [id="+ id +", firstName="+ firstName +", secondName=" + lastName + ", institutionName=" + this.institutionName + "]";
	}
	
	public static List<String> getTitleList(){
		return new ArrayList<String>(Arrays.asList(
				"Prof. Dr.",
				"PD Dr.",
				"Dr.",
				"M.A.",
				"M.Sc.",
				"B.A.",
				"B.Sc.",
				"Dr.-Ing.",
				"Dipl.-Ing.",
				"Dipl."));
	}
}

