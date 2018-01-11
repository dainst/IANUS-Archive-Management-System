package de.ianus.access.web.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.ianus.access.web.SessionBean;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.Time;
import de.ianus.metadata.bo.utils.TimeSpan;
import de.ianus.metadata.utils.RetrievalUtils;


public class MDUtils extends CommonUtils {
	
	
	
	
	
	
	
	public MDUtils(DataCollection dc, SessionBean session){
		super(dc, session);
	}
	
	
	
	
	
	
	
	
	
	
	public String getProjectInformationHeader(){
		StringBuilder sb;
		if(this.getPresentationDate() != null) {
			sb = new StringBuilder(this.getPresentationDate() + " | ");
		}else{
			sb = new StringBuilder("");
		}
		int i = 0;
		for(Actor actor : this.dc.getAuthorList()){
			if(actor instanceof Person){
				if(i > 0) sb.append(", ");
				sb.append(getFullPersonName((Person) actor));
				i++;
			}
		}
		return sb.toString();
	}

	
	public String getFullNameAuthorList() {
		Set<Person> list = this.dc.getAuthorList();
		List<String> result = new ArrayList<String>();
		for(Person person : list) {
			result.add(getActorLink(person));
		}
		return implode(result, "<br>");
	}
	
	
	
	public String getActorsList() {
		String result = "";
		for(ActorRole.Type type : ActorRole.Type.values()) {
			if(type == ActorRole.Type.ianus_PrincipalInvest) {
				result += getRoleActorList(type);
				break;
			}
		}
		for(ActorRole.Type type : ActorRole.Type.values()) {
			if(type == ActorRole.Type.dcms_ContactPerson
			|| type == ActorRole.Type.dcms_HostingInstitution
			|| type == ActorRole.Type.dcms_RightsHolder
			|| type == ActorRole.Type.ianus_PrincipalInvest
			) continue;
			
			result += getRoleActorList(type);
		}
		return result;
	}
	
	
	private String getRoleActorList(ActorRole.Type type) {
		String result = "";
		Set<Actor> actors = this.dc.getActorList(type);
		if(actors != null && actors.size() > 0) {
			result += "<h3>" + type.labelGer + "</h3><p>";
			int n = 0;
			for(Actor actor : actors) {
				String tmp = "";
				if(n > 0) result += "</p><p>";
				if(actor instanceof Person) {
					result += MDUtils.getCompactPerson((Person) actor);
				}
				else if(actor instanceof Institution) {
					result += MDUtils.getCompactInstitution((Institution) actor);
				}
				// time span list
				int i = 0;
				for(ActorRole role : actor.getRoleList()) {
					if(role.getTypeId().equals(type.id)) {
						if(i > 0 && !empty(role.getTimeSpanText())) tmp += ", ";
						tmp += role.getTimeSpanText();
						i++;
					}
				}
				if(!empty(tmp)) result += " (" + tmp + ")";
				
				n++;
			}
			result += "</p>";
		}
		return result;
	} 
	
	
	
	
	
	public static String getActorLink(Actor actor) {
		return getActorLink(actor, true);
	}
	
	
	private static String getActorLink(Actor actor, Boolean printIdList) {
		String result = "";
		if(empty(actor.getUrl())) {
			result = getActorName(actor);
		}else{
			if(actor instanceof Person) {
				Person person = (Person) actor;
				result += "<a href=\"" + person.getUrl() + "\" target=\"_blank\">";
				result += getFullPersonName(person);
				result += "</a>";
			}
			if(actor instanceof Institution) {
				Institution inst = (Institution) actor;
				result += "<a href=\"" + inst.getUrl() + "\" target=\"_blank\">";
		    	result += inst.getName();
		    	result += "</a>";
			}
		}
		
		Set<Identifier> idList = actor.getExternalIdList();
		if(printIdList && !empty(getIdList(idList))) {
			result += " " + getIdList(idList);
		}
		
		return result;
	}
	
	
	public static String getIdList(Set<Identifier> idList) {
		List<String> tmpList = new ArrayList<String>();
		if(idList.size() > 0) for(Identifier id : idList) {
			if(!empty(MDUtils.getIdentifierLink(id)))
				tmpList.add(MDUtils.getIdentifierLink(id));
		}
		if(tmpList.size() > 0)  return "(" + implode(tmpList) + ")";
		return "";
	}
	
	
	public static String getActorName(Actor actor) {
		if(actor instanceof Person) 		return getFullPersonName((Person) actor);
		if(actor instanceof Institution) 	return ((Institution) actor).getName();
		return "";
	}

	
	public static String getPerson(Person person) {
		return getPerson(person, false);
	}
	
	public static String getCompactPerson(Person person) {
		return getPerson(person, true);
	}
	
	public static String getPerson(Person person, Boolean compact) {
		String result = "";
		String tmp = "";
		
		result += getActorLink(person);
		
		if(compact) {
			if(!empty(person.getInstitutionName()))
				tmp += escape(person.getInstitutionName());
			if(!empty(person.getDepartment()))
				tmp += ", " + escape(person.getDepartment());
			if(!empty(person.getWorkingUnit()))
				tmp += ", " + escape(person.getWorkingUnit());
			if(!empty(person.getCity()))
				tmp += ", " + escape(person.getCity());
			if(!empty(tmp)) result += ", " + tmp;
		}
		
		tmp = "";
		if(!compact) {
			if(!empty(person.getInstitutionName()))
				tmp += escape(person.getInstitutionName()) + "<br>";
			if(!empty(person.getDepartment()))
				tmp += escape(person.getDepartment()) + "<br>";
			if(!empty(person.getWorkingUnit()))
				tmp += escape(person.getWorkingUnit()) + "<br>";
			
			if(!empty(person.getStreet()))
				tmp += escape(person.getStreet()) + "<br>";
			if(!empty(person.getPostalCode()))
				tmp += escape(person.getPostalCode()) + " ";
			if(!empty(person.getCity()))
				tmp += escape(person.getCity());
			
			if(!empty(tmp)) result += "<p>" + tmp + "</p>";
			
			if(!empty(person.getEmail()))
				result += "<p>E-Mail: <a href=\"mailto:" + person.getEmail() + "\" class=\"contact-mail\">" + person.getEmail() + "</a></p>";
		}
		
		return result;
	}
	
	public static String getInstitution(Institution inst) {
		return getInstitution(inst, false);
	}
	
	public static String getCompactInstitution(Institution inst) {
		return getInstitution(inst, true);
	}
	
	public static String getInstitution(Institution inst, Boolean compact) {
		String result = "";
		String tmp = "";
		
		result += getActorLink(inst);
    	
    	if(compact) {
    		if(!empty(inst.getDepartment()))
    			result += ", " + escape(inst.getDepartment());
    		if(!empty(inst.getWorkingUnit()))
	    		result += ", " + escape(inst.getWorkingUnit());
	    	if(!empty(inst.getCity()))
	    		result += ", " + escape(inst.getCity());
    	}
    	
    	if(!compact) {
    		if(!empty(inst.getDepartment()))
				tmp += escape(inst.getDepartment()) + "<br>";
	    	if(!empty(inst.getWorkingUnit()))
				tmp += escape(inst.getWorkingUnit()) + "<br>";
	    	if(!empty(inst.getStreet()))
				tmp += escape(inst.getStreet()) + "<br>";
	    	if(!empty(inst.getPostalCode()))
				tmp += escape(inst.getPostalCode()) + " ";
	    	if(!empty(inst.getCity()))
				tmp += escape(inst.getCity());
			
	    	if(!empty(tmp)) result += "<p>" + tmp + "</p>";
	    	if(!MDUtils.empty(inst.getEmail()))
				result += "<p>E-Mail: <a href=\"mailto:" + inst.getEmail() + "\" class=\"contact-mail\">" + inst.getEmail() + "</a></p>";
    	}
    	
		return result;
	}
	
	
	public String getAbbreviatedAuthorList() {
		return getAbbreviatedAuthorList(defaultSeparator);
	}
	
	public String getAbbreviatedAuthorList(String separator) {
		Set<Person> list = this.dc.getAuthorList();
		List<String> result = new ArrayList<String>();
		
		for(Person person : list) {
			result.add(getAbbreviatedPersonName(person));
		}
		
		return implode(result, separator);
	}
	
	
	
	/**
	 * Method to retrieve the full person name with academic title 
	 * 
	 * @param Person
	 * @return String
	 */
	public static String getFullPersonName(Person person) {
		return getFullPersonName(person, true);
	}
	
	/**
	 * Method to retrieve the full person name and optional academic title if second arg is TRUE
	 * 
	 * @param Person, Boolean
	 * @return String
	 */
	private static String getFullPersonName(Person person, Boolean printTitle) {
		String out = new String("");
		if(printTitle && !StringUtils.isEmpty(person.getTitle())) out = person.getTitle() + " ";
		if(!StringUtils.isEmpty(person.getFirstName())) out += escape(person.getFirstName()) + " ";
		if(!StringUtils.isEmpty(person.getLastName())) out += escape(person.getLastName());
		return out;
	}
	
	
	public static String getAbbreviatedPersonName(Person person) {
		String out = new String("");
		if(!StringUtils.isEmpty(person.getLastName())) out += escape(person.getLastName());
		if(!StringUtils.isEmpty(person.getFirstName())) {
			out += ", ";
			String[] split = person.getFirstName().split("\\s+");
			int i = 0;
			for(String part : split) {
				if(i > 0) out += " ";
				out += escape(Character.toString(part.charAt(0))) + ".";
				i++;
			}
		}
		
		return out;
	}
	
	
	
	
	
	
	/**
	 * Generic method to truncate text
	 * 
	 * @param value
	 * @param maxLength
	 * @return String
	 */
	// TODO: add handling for word-boundaries and ellipsis
	public static String shortText(String value, int maxLength) {
		if(value == null) return null;
		return (StringUtils.isNotEmpty(value) && value.length() > maxLength) ? value.substring(0, maxLength) : value;	
	}
	
	
	
	

	
	
	/**
	 * Get a html-escaped list of publication-PIDs
	 * 
	 * @param Reference reference
	 * @return html-escaped string
	 */
	
	public static String getPublicationPIDList(Set<Identifier> identifierList) {
		List<String> list = new ArrayList<String>();
		
		String urn = null;
		
		if(identifierList != null) {
			// Zenon first
			for(Identifier identifier : identifierList) {
				if(identifier.getType().equals(Identifier.External.ianus_iDAIbibliography.id)) {
					urn = getIdentifierLink(identifier);
					if(!empty(urn))
						list.add(getIdentifierLink(identifier));
				}
			}
			// all other PIDs	
			for(Identifier identifier : identifierList) {
				
				// skip zenon references
				if(identifier.getType().equals(Identifier.External.ianus_iDAIbibliography.id))
					continue;
				urn = getIdentifierLink(identifier);
				if(!empty(urn))
					list.add(getIdentifierLink(identifier));
			}
		}
		
		if(list.size() == 0) return "";
		return " (" + implode(list) + ")";
	}
	
	/*
	 * This method tries to concatenate an dynamic URL from the prefix pattern 
	 * stored in the various Identifier type list values, then checks for the uri field, 
	 * then for the value, which might also contain URIs in some cases, or bare idValue.
	 */
	private static String getIdentifierUrn(Identifier identifier) {
		String prefix = identifier.getIdentifierTypeListValue("url");
		
		if(!empty(prefix) && !empty(identifier.getValue())) {
			return prefix + identifier.getValue();
		}
		else{
			if(!empty(identifier.getUri())) {
				return identifier.getUri();
			}
			else if(!empty(identifier.getValue())) {
				return identifier.getValue();
			}
		}
		
		return null;
	}
	
	
	public static String getIdentifierLink(Identifier identifier) {
		return getIdentifierLink(identifier, false);
	}
	
	/**
	 * Used at external identifiers section
	 * 
	 * @param identifier
	 * @param showUrl
	 * @return
	 */
	public static String getIdentifierLink(Identifier identifier, Boolean showUrl) {
		String result = null; 
		String urn = getIdentifierUrn(identifier);
		
		String label = identifier.getIdentifierTypeListValue("labelGer");
		if(Identifier.External.getById(identifier.getType()) == Identifier.External.ianus_local) {
			label = identifier.getInstitution();
		}
		
		if(!empty(urn) && urn.startsWith("http")) {
			result = "<a href=\"" + urn + "\" target=\"_blank\">";
			result += label;
			result += "</a>";
			
			if(showUrl && !empty(urn)) {
				result = label + ": ";
				result += "<a href=\"" + urn + "\" target=\"_blank\">";
				result += urn;
				result += "</a>";
			}
		}
		else if(!empty(urn)) {
			result = label + ": " + urn;
		}
		
		
			
		return result;
	}
	
	/**
	 * Used at relatedResource.jsp
	 * 
	 * @param identifier
	 * @return
	 */
	public static String getLongIdentifierLink(Identifier identifier) {
		String result = null; 
		String urn = getIdentifierUrn(identifier);
		
		if(!empty(urn) && urn.startsWith("http")) {
			result = "<a href=\"" + urn + "\" target=\"_blank\">";
			result += urn;
			result += "</a>";
		}
		else if(!empty(urn)) {
			result = urn;
		}
		
		if(!empty(result)) result = "(" + result + ")";
		
		return result;
	}
	
	
	
	/**
	 * Method to get Localization from the Place and ElementOfList Object (Lokalisierung)
	 * 
	 * @return String
	 * @throws 
	 */
	public String getLocalization(Integer limit){
		List<String> result = new ArrayList<String>();
		String item = new String();
		Set<Place> places = this.dc.getPlaceList();
		Integer n = 0;
		
		String out = "";
		for(Place place : places) {
			item = getPlaceLabel(place);	// already escaped
			List<String> temp = getPrioritizedElementOfListList(place.getIdentifierList());
			if(temp != null)
				item += " " + "(" + implode(temp) + ")"; 
			
			result.add(item);
			n++;
			if(limit != null && n >= limit) {
				break;
			}
		}
		
		out += implode(result);
		
		if(!empty(out)) out = "<p>" + out + "</p>";
		
		return out;
	}
	
	
	
	/**
	 * Method to get MainPeriod Value from the ElementOfList Object (Zeitstellung)
	 * 
	 * @return html-escaped String
	 * @throws 
	 */
	public String getMainPeriod() {
		return implode(getTimeList("mainPeriod"));
	}
	
	/**
	 * Method to get SubPeriod Value from the ElementOfList Object (Zeitstellung)
	 * 
	 * @return html-escaped String
	 * @throws 
	 */
	public String getSubPeriod(){
		return implode(getTimeList("subPeriod"));		
	}
	
	
	
	private List<String> getTimeList(String attribute) {
		//List<String> result = new ArrayList<String>(); 
		List<ElementOfList> temp = new ArrayList<ElementOfList>();
		for(Time time : this.dc.getTimeList()) {
			switch(attribute) {
			case "mainPeriod":
				if(time.getMainPeriod() != null)
					temp.add(time.getMainPeriod());
				break;
			case "subPeriod":
				if(time.getSubPeriod() != null)
					temp.add(time.getSubPeriod());
				break;
			}
		}
		
		// sort by ID
		Collections.sort(temp, new Comparator<ElementOfList>() {
		    @Override
			public int compare(ElementOfList o1, ElementOfList o2) {
				return (o1.getId() < o2.getId() ? -1 : (o1.getId() == o2.getId() ? 0 : 1));
			}
		});
		
		return getElementOfListList(temp);
	}
	
	
	
	/**
	 * Method to get Project Duration from the Time Object (Zeitstellung)
	 * 
	 * @param Time
	 * @return html-escaped String
	 * @throws 
	 */
	public String getProjectDuration(){
		String result = "";
		result = (empty(this.dc.getProjectStart())) ? "" : this.dc.getProjectStart();
		result += ((!empty(this.dc.getProjectStart()) && !empty(this.dc.getProjectEnd())) ? " - " : "");
		result += (empty(this.dc.getProjectEnd())) ? "" : this.dc.getProjectEnd();
		
		return result;
	}
	
	
	public String getDataGeneration() {
		List<String> list = new ArrayList<String>();
		for(TimeSpan span : this.dc.getDataGenerationList()) {
			String temp = "";
			if(!empty(span.getStart()))
				temp = span.getStart();
			if(!empty(span.getStart()) && !empty(span.getEnd())) 
				temp += " - ";
			if(!empty(span.getEnd()))
				temp += span.getEnd();
			if(!empty(span.getDescription()))
				temp += ": " + escape(span.getDescription());
			
			if(!empty(temp)) list.add(temp);
		}
		return implode(list);
	}

	
	
	/**
	 * Create a dynamic label.  
	 * Method *does* use language fallback handling, as opposed to 
	 * the method in class Place.
	 * 
	 * @param Place place
	 * @param (optional) String languageCode
	 * @return
	 */
	public String getPlaceLabel(Place place) {
		return this.getPlaceLabel(place, this.languageCode);
	}
	
	public String getPlaceLabel(Place place, String languageCode){
		// RetrievalUtils perform the proper fallback language handling
		TextAttribute greaterRegion = RetrievalUtils.getTextAttribute(place.getGreaterRegionList(), languageCode);
		TextAttribute country = RetrievalUtils.getTextAttribute(place.getCountryList(), languageCode);
		TextAttribute regionProvince = RetrievalUtils.getTextAttribute(place.getRegionProvinceList(), languageCode);
		TextAttribute city = RetrievalUtils.getTextAttribute(place.getCityList(), languageCode);
		
		String label = new String();
		
		if(greaterRegion != null && !empty(greaterRegion.getValue())){
			label = greaterRegion.getValue();
		}
		if(country != null && StringUtils.isNotEmpty(country.getValue())){
			label = (empty(label)) ? country.getValue() : label + ", " + country.getValue(); 
		}
		if(regionProvince != null && StringUtils.isNotEmpty(regionProvince.getValue())){
			label = (empty(label)) ? regionProvince.getValue() : label + ", " + regionProvince.getValue();
		}
		if(city != null && StringUtils.isNotEmpty(city.getValue())){
			label = (empty(label)) ? city.getValue() : label + ", " + city.getValue();
		}
		return escape(label);
	}
	
	
	
	public String getGeometries() {
		Set<Place> places = this.dc.getPlaceList();
		String wkt = new String();
		TextAttribute type = null;
		List<JsonObject> result = new ArrayList<JsonObject>();
		JsonObject json;
		
		if(places != null) {
			for(Place place : places) {
				if(place == null) continue;
				
				wkt = place.getWktText();
				
				if(wkt != null && !wkt.equals("")) {
					json = new JsonObject();
					json = place.toJsonObject(json);
					
					type = RetrievalUtils.getTextAttribute(place.getTypeOfAreaList(), this.languageCode);
					if(type != null && !empty(type.getValue()))
						json.addProperty("type", type.getValue());
					
					result.add(json);
				}
			}
			
			if(result.size() > 0) {
				return new Gson().toJson(result);
			}
		}
		
		return null;
	}
	
	
	
	
	
	/**
	 * Method to get External Identifier list (Externe Projektkennungen)
	 * 
	 * @param Set<Identifier> list
	 * @return html-escaped String
	 * @throws 
	 */
	public String getExternalIdentifierList(){
		Set<Identifier> list = this.dc.getExternalIdList();
		List<String> result = new ArrayList<String>();
		
		String temp = "";
		for(Identifier identifier : list) {
			temp = getIdentifierLink(identifier, true);
			if(!empty(temp)) result.add(temp);
		}
		
		return implode(result);
	}
	
	
	
	public static String getPublication(Reference ref) {
			String label = "";
			if(ref.getPublication() != null) {
				Publication pub = ref.getPublication();
				
				if(!pub.getAuthor().isEmpty() && !pub.getTitle().isEmpty()) {
					label += escape(pub.getAuthor()).replaceAll("\\s*;\\s*", " &#8210; ");
					
					if(!pub.getYear().isEmpty()){
						label += " ("+ pub.getYear()+").";
					}
					if(!pub.getTitle().isEmpty()){
						label += " " + escape(pub.getTitle());
					}
					// publications in a collection
					if(!pub.getCollectionEditor().isEmpty() && !pub.getCollectionTitle().isEmpty()){
						label += ", in: "+ escape(pub.getCollectionEditor()) + " (Hrsg.), ";
						label += escape(pub.getCollectionTitle());
					}
					label += ".";
				}
				else if(!pub.getCollectionEditor().isEmpty() && !pub.getCollectionTitle().isEmpty()){
					// author title is empty: publication is an entire collection
					label += escape(pub.getCollectionEditor()) + "(Hrsg.), ";
					label += escape(pub.getCollectionTitle()) + ".";
				}
				
				if(!pub.getSeries().isEmpty() && !pub.getVolume().isEmpty()){
					label += " " + escape(pub.getSeries()) + " " + escape(pub.getVolume()) + ".";
				}
							
				if(!pub.getCity().isEmpty() && !pub.getPublisher().isEmpty()){
					if(!pub.getCollectionEditor().isEmpty() && pub.getAuthor().isEmpty()){
						label += " "+ escape(pub.getCity()) + ": ";
						label += escape(pub.getPublisher()) + ", " + escape(pub.getYear());
					}else{
						label += " "+ escape(pub.getCity()) + ": " + escape(pub.getPublisher());
					}
					label += ". ";
				}
				
				// add the reference information
				if(!label.isEmpty() && !ref.getPagesNumber().isEmpty()) {
					label += "S. " + ref.getPagesNumber() + ".";
				}
			}
			
			return label;
	}
	
	
	
	public String getDOI() {
		Set<Identifier> idList = this.dc.getInternalIdList();
		for(Identifier id : idList) {
			if(Identifier.Internal.getById(id.getType()) == Identifier.Internal.dcms_DOI)
				return id.getValue();
		}
		return null;
	}
	
	
	public String getCollectionNumber() {
		// Why not use this? this.dc.getCollectionIdentifier();
		// Because the above would also return ianus_mdrecord type, which we don't 
		// want in citation section. 
		Set<Identifier> idList = this.dc.getInternalIdList();
		for(Identifier id : idList) {
			if(Identifier.Internal.getById(id.getType()) == Identifier.Internal.ianus_collno)
				return id.getValue();
		}
		return null;
	}
	
	
	
	public String getPresentationDate() {
		if(this.dc.getPresentationDate() == null) return "";
		return df.format(this.dc.getPresentationDate());
	}
	
	
	
	public String getDataUriImage(Long wfipId, String filePath, String alt) {
		WorkflowIP wfip = Services.getInstance().getDaoService().getWorkflowIP(wfipId, this.session.getWfipClass());
		if(wfip == null){
			return null;
		}
		
		String absolutePath = null;
		try {
			absolutePath = (!filePath.startsWith("/")) ? 
					wfip.getAbsolutePath() + "/" + filePath :
					wfip.getAbsolutePath() + filePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File f = new File(absolutePath);
		if(f.exists() && !f.isDirectory()) {

			Path path = Paths.get(absolutePath);
			byte[] fileContent = null;
			try {
				fileContent = Files.readAllBytes(path);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			return "<img alt=\"" + alt + "\" src=\"data:image/png;base64," + DatatypeConverter.printBase64Binary(fileContent) + "\">";
		}
		
		return null;
	}
	
	
	
	public String getResourceTypeList() {
		List<String> result = new ArrayList<String>();
		Set<ElementOfList> resourceTypes = this.dc.getResourceTypeList();
		if(resourceTypes != null) for(ElementOfList item : resourceTypes) {
			if(!empty(item.getValue())) result.add(item.getValue());
		}
		if(result.size() > 0) 
			return "<p><span>Art der Daten:</span> " + implode(result) + "</p>";
		return null;
	}
	
	
	
	public String getDataCategoryList() {
		List<String> result = new ArrayList<String>();
		Set<ElementOfList> dataCategories = this.dc.getDataCategoryList();
		if(dataCategories != null) for(ElementOfList item : dataCategories) {
			if(!empty(item.getValue())) result.add(item.getValue());
		}
		if(result.size() > 0) 
			return "<p><span>Dateitypen:</span> " + implode(result) + "</p>";
		return null;
	}
	
	

	

	
	
}

