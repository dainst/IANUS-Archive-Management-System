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

@Entity
@Table(name="Identifier")
public class Identifier extends Element{
	
	@Enumerated(EnumType.STRING)
	@Column(name="contentType")
	private ContentType contentType;
	
	@Column(name="value")
	private String value;
	
	@Column(name="type")
	private String type = External.ianus_local.id;
	
	@Column(name="institution")
	private String institution;
	
	@Column(name="uri")
	private String uri;
	
	public static Identifier clone(Identifier other, Long sourceId){
		Identifier newEntry = new Identifier();
		newEntry = (Identifier)Element.clone(newEntry, other, sourceId);
		newEntry.contentType = other.contentType;
		newEntry.value = other.value;
		newEntry.type = other.type;
		newEntry.institution = other.institution;
		newEntry.uri = other.uri;
		return newEntry;
	}
	
	@Override
	public boolean isConsistent(){
		return super.isConsistent() && this.contentType != null;
	}
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		json.addProperty("value", this.value);
		json.addProperty("type", this.type);
		json.addProperty("institution", this.institution);
		json.addProperty("uri", this.uri);
		return json;
	}
	
	public static JsonArray toJsonArray(Set<Identifier> list){
		JsonArray array = new JsonArray();
		for(Identifier item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("IdentifierDataType");
			xml.addAttribute("Value", this.getValue());
			xml.addAttribute("Type", this.getType());
			xml.addAttribute("Institution", this.getInstitution());
			xml.addAttribute("URI", this.getUri());
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<Identifier> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Identifier item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	
	
	/**
	 * This methods resembles the Identifier data model 
	 * and tries to retrieve a field value from the used enum object. 
	 * 
	 * Next reviews should involve the harmonization 
	 * of Publication.PidList and Identifier.External enum lists, 
	 * finally the usage of ElementOfList for all value lists. 
	 */
	public static String getIdentifierTypeListValue(Identifier identifier, String fieldname) {
		String value = null;
		String type = identifier.getType();
		String contentType = identifier.getContentType().toString();
		String source = identifier.getSourceClass().toString();
		
		if(source.equals("DataCollection")) {
			if(contentType.equals(Identifier.ContentType.external_id.toString())) {
				// use Identifier.External
				value = Identifier.External.getFieldById(type, fieldname);
			}
			else if(contentType.equals(Identifier.ContentType.internal_id.toString())) {
				// use Identifier.Internal
				value = Identifier.Internal.getFieldById(type, fieldname);
			}
		}
		else if(source.equals("Publication")) {
			// use Publication.PidList
			value = Identifier.External.getFieldById(type, fieldname);
		}
		else if(source.equals("Person")) {
			// use Identifier.PersonType
			value = Identifier.PersonType.getFieldById(type, fieldname);
		}
		else if(source.equals("RelatedResource")) {
			value = Identifier.External.getFieldById(type, fieldname);
		}
		else{
			value = identifier.getType();
		}
		
		return value;
	}
	
	
	public String getIdentifierTypeListValue(String fieldname) {
		return getIdentifierTypeListValue(this, fieldname);
	}


	public enum ContentType{
		internal_id, external_id, id, pid
	}
	
	
	public enum Internal {
		ianus_collno("ianus_collno","Collection No.","Sammlung Nr.","https://datenportal.ianus-fdz.de/collection/","Von IANUS vergebene Sammlungsnummer"),
		ianus_mdrecord("ianus_mdrecord","Metadata record No.","Metadatennachweis Nr.","https://datenportal.ianus-fdz.de/collection/","Von IANUS vergebene Nummer für Metadatennachweise externer Datenbestände"),
		dcms_DOI("dcms_DOI","DOI","DOI","http://dx.doi.org/","Digital Object Identifier"),
		dcms_URL("dcms_URL","URL","URL","","Uniform Resource Locator"),
		dcms_URN("dcms_URN","URN","URN","","Uniform Resource Name"),
		ianus_uuid("ianus_uuid","UUID","UUID","","Universally Unique Identifier innerhalb des IANUS-Archiv-Systems")
		;
	
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;

		private Internal(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static Internal getById(String id) {
			if(id.equals("")) return null;
			for(Internal item : Internal.values()) {
				if(item.id.equals(id)) return item;
			}
			return null;
		}
		
		public String getField(String fieldname) {
			switch(fieldname) {
			case "id": return this.id;
			case "labelEng":
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "labelGer":
				if(!this.labelGer.equals("")) return this.labelGer;
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "url": return this.url;
			case "description": return this.description;
			}
			return null;
		}
		
		public static String getFieldById(String id, String fieldname) {
			if(getById(id) == null) return null;
			return getById(id).getField(fieldname);
		}
	}
	
	
	
	public enum PersonType {
		
		FREE		("FREE", "Free text", "Freitext", "", ""),
		GND			("GND", "Integrated Authority File (GND)", "GND", "http://d-nb.info/gnd/", "Gemeinsamme Normdatai"),
		ORCID		("ORCID", "ORCID", "ORCID", "http://orcid.org/", "Open Researcher and Contributor ID"),
		VIAF		("VIAF", "VIAF", "", "", ""),
		ISNI		("ISNI", "ISNI", "", "", ""),
		ISO_27729	("ISO_27729", "ISO 27729", "", "", "")
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;
		
		private PersonType(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static PersonType getById(String id) {
			if(id.equals("")) return null;
			for(PersonType item : PersonType.values()) {
				if(item.id.equals(id)) return item;
			}
			return null;
		}
		
		public String getField(String fieldname) {
			switch(fieldname) {
			case "id": return this.id;
			case "labelEng":
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "labelGer":
				if(!this.labelGer.equals("")) return this.labelGer;
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "url": return this.url;
			case "description": return this.description;
			}
			return null;
		}
		
		public static String getFieldById(String id, String fieldname) {
			if(getById(id) == null) return null;
			return getById(id).getField(fieldname);
		}
	}
	
	
	
	public enum External {
		
		ianus_local("ianus_local","Local","Lokal","","Lokale Kennung (Projekt, Institution, ...)"),
		ianus_iDAIbibliography("ianus_iDAIbibliography","iDAI.bibliography","iDAI.bibliography","http://zenon.dainst.org/Record/","Identifikator iDAI.bibliography/ZENON"),
		ianus_DFG("ianus_DFG", "DFG project identifier", "DFG Projektkennung", "", ""),
		dcms_ARK("dcms_ARK","ARK","ARK","","Archival Resource Key"),
		dcms_DOI("dcms_DOI","DOI","DOI","http://dx.doi.org/","Digital Object Identifier"),
		dcms_EAN13("dcms_EAN13","EAN13","EAN13","","European Article Number"),
		dcms_EISSN("dcms_EISSN","EISSN","EISSN","","Electronic International Standard Serial Number"),
		dcms_Handle("dcms_Handle","Handle","Handle","http://hdl.handle.net/","Handle System Identifier"),
		dcms_ISBN("dcms_ISBN","ISBN","ISBN","","International Standard Book Number"),
		dcms_ISSN("dcms_ISSN","ISSN","ISSN","","International Standard Serial Number"),
		dcms_ISTC("dcms_ISTC","ISTC","ISTC","","International Standard Text Code"),
		dcms_LISSN("dcms_LISSN","LISSN","LISSN","","linking ISSN"),
		dcms_PURL("dcms_PURL","PURL","PURL","","Persistent Uniform Resource Locator"),
		dcms_URL("dcms_URL","URL","URL","","Uniform Resource Locator"),
		dcms_URN("dcms_URN","URN","URN","","Uniform Resource Name")
		;
		
		//TODO: review
		/* from the old Publication.PidList enumeration:
		IRI("IRI", "Internationalized Resource Identifier", "", "", ""),
		LSID("LSID", "Life Science Identifiers", "", "", ""),
		UPC("UPC", "Universal Product Code", "", "", ""),
		URI("URI", "Uniform Resource Identifier", "", "", ""),
		URL("URL", "Uniform Resource Locator", "", "", "")
		*/

		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;

		private External(String id, String valueEng, String valueGer, String url, String description){
			this.id = id;
			this.labelEng = valueEng;
			this.labelGer = valueGer;
			this.url = url;
			this.description = description;
		}
		
		public static External getById(String id) {
			if(id.equals("")) return null;
			for(External item : External.values()) {
				if(item.id.equals(id)) return item;
			}
			return null;
		}
		
		public String getField(String fieldname) {
			switch(fieldname) {
			case "id": return this.id;
			case "labelEng":
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "labelGer":
				if(!this.labelGer.equals("")) return this.labelGer;
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "url": return this.url;
			case "description": return this.description;
			}
			return null;
		}
		
		public static String getFieldById(String id, String fieldname) {
			if(getById(id) == null) return null;
			return getById(id).getField(fieldname);
		}
	}
	
	
	
	
	
	@Override
	public String toString(){
		return "Identifier [id=" + id + 
				", value=" + value +
				", type=" + type +
				", institution=" + institution + 
				", sourceClass=" + getSourceClass() + 
				", sourceId=" + getSourceId() + "]";
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}	
}
