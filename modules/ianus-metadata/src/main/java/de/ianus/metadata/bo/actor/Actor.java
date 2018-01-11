package de.ianus.metadata.bo.actor;

import java.util.ArrayList;
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

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.json.JSONUtils;
import de.ianus.metadata.xml.XMLObject;

@Entity
@Table(name="Actor")
public class Actor extends Element{
	
	//email, telephone, url
	//@Transient
	//private Map<TextAttribute.ContentType, Set<TextAttribute>> attributeMap = new HashMap<TextAttribute.ContentType, Set<TextAttribute>>();
	
	//@Column(name="dcId")
	//private Long dcId;
	
	@Column(name="street")
	private String street;
	
	@Column(name="postalCode")
	private String postalCode;
	
	@Column(name="city")
	private String city;
	
	@Column(name="country")
	private String country;
	
	@Column(name="email")
	private String email;
	
	@Column(name="telephone")
	private String telephone;
	
	@Column(name="url")
	private String url;
	
	@Column(name="department")
	private String department;
	
	@Column(name="workingUnit")
	private String workingUnit;
	
	@Transient
	private Set<Identifier> externalIdList = new LinkedHashSet<Identifier>();
	
	@Transient
	private Set<ActorRole> roleList = new LinkedHashSet<ActorRole>();
	
	public static Actor createFromJsonObject(Actor actor, JsonObject json){
		
		actor.setStreet(JSONUtils.getString(json, "street"));
		actor.setPostalCode(JSONUtils.getString(json, "postalCode"));
		actor.setCity(JSONUtils.getString(json, "city"));
		actor.setCountry(JSONUtils.getString(json, "country"));
		actor.setEmail(JSONUtils.getString(json, "email"));
		actor.setTelephone(JSONUtils.getString(json, "telephone"));
		actor.setUrl(JSONUtils.getString(json, "url"));
		actor.setDepartment(JSONUtils.getString(json, "department"));
		actor.setWorkingUnit(JSONUtils.getString(json, "workingUnit"));
		
		return actor;
	}
	
	public static Actor clone(Actor newItem, Actor other, MDEntry source) throws Exception{
		
		newItem.setSource(source);
		
		newItem.street = other.street;
		newItem.postalCode = other.postalCode;
		newItem.city = other.city;
		newItem.country = other.country;
		newItem.email = other.email;
		newItem.telephone = other.telephone;
		newItem.url = other.url;
		newItem.department = other.department;
		newItem.workingUnit = other.workingUnit;
		
		return newItem;
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		//json.addProperty("dcId", this.dcId);
		json.addProperty("sourceId", this.sourceId);
		json.addProperty("sourceClass", this.sourceClass.toString());
		json.addProperty("actorType", this.getClass().toString());
		
		json.addProperty("street", this.street);
		json.addProperty("postalCode", this.postalCode);
		json.addProperty("city", this.city);
		json.addProperty("country", this.country);
		
		json.addProperty("email", email);
		json.addProperty("telephone", telephone);
		json.addProperty("url", url);
		
		json.addProperty("department", this.department);
		json.addProperty("workingUnit", this.workingUnit);
		
		json.add("roleList", ActorRole.toJsonArray(this.getRoleList()));
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<Actor> list){
		JsonArray array = new JsonArray();
		for(Actor item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("Actor");
			if(!empty(this.street))		xml.addAttribute("Street", this.street);
			if(!empty(this.postalCode)) xml.addAttribute("Postalcode", this.postalCode);
			if(!empty(this.city))		xml.addAttribute("City", this.city);
			if(!empty(this.country))	xml.addAttribute("Country", this.country);
			if(!empty(this.url))		xml.addAttribute("URL", this.url);
			if(!empty(this.email))		xml.addAttribute("EMail", this.email);
			if(!empty(this.telephone))	xml.addAttribute("Telephone", this.telephone);
			if(!empty(this.getDepartment()))
				xml.addAttribute("Department", this.getDepartment());
			if(!empty(this.getWorkingUnit()))
				xml.addAttribute("WorkingUnit", this.getWorkingUnit());
			
		String tmp = null;
		try {
			tmp = this.getActorClass().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!empty(tmp)) xml.addAttribute("ActorType", tmp);
			
		XMLObject identifier = new XMLObject("ActorIdentifier");
			identifier.addDescendants(Identifier.toXMLObjects(this.getExternalIdList()));
		if(identifier.hasDescendants()) xml.addDescendant(identifier);
		
		XMLObject roles = new XMLObject("ActorRoles");
			roles.addDescendants(ActorRole.toXMLObjects(this.getRoleList()));
		if(roles.hasDescendants()) xml.addDescendant(roles);
		
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<Actor> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Actor item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	/**
	 * <p>This method prints the list of roles as a html table.
	 * If an actor has twice the same role, it will be displayed only one time.</p>
	 * @return
	 */
	public String getHtmlRoleList(){
		StringBuilder sb = new StringBuilder();
		sb.append("<table style='border: none;'><tbody>");
		List<String> list = new ArrayList<String>();
		for(ActorRole role : this.roleList){
			if(!list.contains(role.getTypeId())){
				sb.append("<tr>");
				sb.append("<td style='border: none;'>" + role.getTypeId() + "</td>");
				if(StringUtils.isNotEmpty(role.getFunctionStartText()) || StringUtils.isNotEmpty(role.getFunctionEndText())){
					String timeRange = role.getFunctionStartText();
					timeRange += (StringUtils.isNotEmpty(timeRange) && StringUtils.isNotEmpty(role.getFunctionEndText())) ? " - " : "";
					timeRange += role.getFunctionEndText();
					sb.append("<td style='border: none;'>(" +  timeRange +")</td>");
				}
				sb.append("</tr>");
				list.add(role.getTypeId());
			}
		}
		sb.append("</tbody></table> ");
		return sb.toString();
	}
	
	/**
	 * Helper method to render the "edit-logo" button or not
	 * @return
	 */
	public Boolean getHasLogo() {
		for(ActorRole role : this.roleList){
			if(	ActorRole.Type.getById(role.getTypeId()) == ActorRole.Type.dcms_ContactPerson
			||	ActorRole.Type.getById(role.getTypeId()) == ActorRole.Type.dcms_HostingInstitution
			||	ActorRole.Type.getById(role.getTypeId()) == ActorRole.Type.dcms_RightsHolder) {
				return true;
			}
		}
		return false;
	}
	
	public Set<Identifier> getExternalIdList() {
		return externalIdList;
	}

	public void setExternalIdList(Set<Identifier> externalIdList) {
		this.externalIdList = externalIdList;
	}	
	
	public String getLabel(){
		return "Not implemented";
	}
	
	public String getHtmlLabel(){
		return "Not implemented";
	}
	
	public BOUtils.ActorClass getActorClass() throws Exception{
		if(this instanceof Person){
			return BOUtils.ActorClass.Person;
		}else if(this instanceof Institution){
			return BOUtils.ActorClass.Institution;
		}else{
			throw new Exception("This class is not suported");
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getWorkingUnit() {
		return workingUnit;
	}

	public void setWorkingUnit(String workingUnit) {
		this.workingUnit = workingUnit;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<ActorRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<ActorRole> roleList) {
		this.roleList = roleList;
	}
}
