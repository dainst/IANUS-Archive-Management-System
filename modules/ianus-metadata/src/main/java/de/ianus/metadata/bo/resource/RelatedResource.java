package de.ianus.metadata.bo.resource;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.utils.Classification.Type;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.xml.XMLObject;

@Entity
@Table(name="RelatedResource")
public class RelatedResource extends Element {
	
	//@Column(name="identifier")
	//private String identifier = Identifier.ARK.id;
	
	@Column(name="resourceType")
	private String resourceType = ResourceType.analog.toString();
	
	@Column(name="relationType")
	private String relationType = RelationType.dcms_IsSupplementTo.id;
	
	@Column(name="description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name="scope", columnDefinition = "TEXT")
	private String scope;
	
	@Column(name="location", columnDefinition = "TEXT")
	private String location;	
	
	@Column(name="date")
	private String date;
	
	@Transient
	private Institution institution = new Institution();
	
	@Transient
	private Identifier identifier = new Identifier();
	
	public static RelatedResource clone(RelatedResource other, Long sourceId){
		RelatedResource newItem = new RelatedResource();
		newItem = (RelatedResource)Element.clone(newItem, other, sourceId);
		// TODO, this object should be cloned
		//newItem.identifier = other.identifier;
		newItem.resourceType = other.resourceType;
		newItem.relationType = other.relationType;
		newItem.description = other.description;
		newItem.scope = other.scope;
		newItem.location = other.location;
		newItem.date = other.date;
		
		//TODO Institution should be transformed as well
		
		return newItem;
	} 
	
	@Override
	public String getLabel(){
		String label = "Relation: " + this.relationType + ". Location: " + this.location + ", description: " + this.description;
		return StringUtils.substring(label, 0, 100);
	}

	public String getHtmlLabel(){
		StringBuilder sb = new StringBuilder();
		if(this.identifier != null && StringUtils.isNotEmpty(this.identifier.getType())){
			sb.append("<p>" + this.identifier.getId() +  
					(StringUtils.isEmpty(this.resourceType) ? "" : ", " + this.resourceType) + 
					(StringUtils.isEmpty(this.relationType) && !StringUtils.equals(this.relationType, "0") ? "" : ", " + this.relationType) + "</p>");
		}
		if(StringUtils.isNotEmpty(this.location)){
			sb.append("<p>" + this.location + "</p>");
		}
		if(StringUtils.isNotEmpty(this.description)){
			sb.append("<p>" + this.description + "</p>");
		}
		return sb.toString();
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}	

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	/*
	public enum Identifier{
		
		ARK("ARK", "Archival Resource Key"),
		DOI("DOI", "Digital Object Identifier"),
		EAN13("EAN13", "European Article Number"),
		EISSN("EISSN", "Electronic International Standard Serial Number"),
		Handle("Handle", "Handle"),
		iDAI_books("iDAI.books", "Identifier of iDAI.books (= ZENON)"),
		IRI("IRI", "Internationalized Resource Identifier"),
		ISBN("ISBN", "International Standard Book Number"),
		ISSN("ISSN", "International Standard Serial Number"),
		ISTC("ISTC", "International Standard Text Code"),
		LISSN("LISSN", "linking ISSN"),
		LSID("LSID", "Life Science Identifiers"),
		PURL("PURL", "Persistent Uniform Resource Locator"),
		UPC("UPC", "Universal Product Code"),
		URI("URI", "Uniform Resource Identifier"),
		URL("URL", "Uniform Resource Locator"),
		URN("URN", "Uniform Resource Name");
		
		public final String id;
		public final String label;
		private Identifier(String id, String label){
			this.id = id;
			this.label = label;
		}
	}*/
	
	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}



	public enum ResourceType{
		analog, digital	
	}
	
	public enum RelationType {
		dcms_IsSupplementTo("dcms_IsSupplementTo","is supplement to","ist Ergänzung zu","https://doi.org/10.5438/0010",""),
		dcms_IsSupplementedBy("dcms_IsSupplementedBy","is supplemented by","wird ergänzt durch","https://doi.org/10.5438/0010",""),
		dcms_continues("dcms_continues","is continuation of","ist Weiterführung von","https://doi.org/10.5438/0010",""),
		dcms_IsContinuedBy("dcms_IsContinuedBy","is continued by","wird weitergeführt durch","https://doi.org/10.5438/0010",""),
		dcms_IsNewVersionOf("dcms_IsNewVersionOf","is new version of","ist neuere Version von","https://doi.org/10.5438/0010",""),
		dcms_IsPreviousVersionOf("dcms_IsPreviousVersionOf","is previous version of","ist ältere Version von","https://doi.org/10.5438/0010",""),
		dcms_IsPartOf("dcms_IsPartOf","is part of","ist Teil von","https://doi.org/10.5438/0010",""),
		dcms_HasPart("dcms_HasPart","has part","hat als Teil","https://doi.org/10.5438/0010",""),
		dcms_Documents("dcms_Documents","is documentation of","ist Dokumentation zu","https://doi.org/10.5438/0010",""),
		dcms_IsDocumentedBy("dcms_IsDocumentedBy","is documented by","wird dokumentiert von","https://doi.org/10.5438/0010",""),
		dcms_IsVariantFormOf("dcms_IsVariantFormOf","is variant form of","ist Variante von","https://doi.org/10.5438/0010",""),
		dcms_IsOriginalFormOf("dcms_IsOriginalFormOf","is original form of","ist Originalversion von","https://doi.org/10.5438/0010",""),
		dcms_HasMetadata("dcms_HasMetadata","has metadata","enthält Metadaten zu","https://doi.org/10.5438/0010",""),
		dcms_IsMetadataFor("dcms_IsMetadataFor","is metadata for","ist Metadatum für","https://doi.org/10.5438/0010",""),
		dcms_IsIdenticalTo("dcms_IsIdenticalTo","is identical to","ist identisch mit","https://doi.org/10.5438/0010",""),
		dcms_IsSourceOf("dcms_IsSourceOf","is source of","ist Quelle für","https://doi.org/10.5438/0010",""),
		dcms_IsDerivedFrom("dcms_IsDerivedFrom","is derived from","basiert auf","https://doi.org/10.5438/0010",""),
		ianus_IsMainProjectOf("ianus_IsMainProjectOf","is main project of","ist übergeordnetes Projekt zu","",""),
		ianus_IsSubProjectOf("ianus_IsSubProjectOf","is sub-project of","ist untergeordnetes Projekt zu","",""),
		ianus_IsAssociatedArchive("ianus_IsAssociatedArchive","is associated archive to","ist zugehöriges Archiv zu","",""),
		ianus_hasAssociatedArchive("ianus_hasAssociatedArchive","has associated archive","hat zugehöriges Archiv","",""),
		ianus_IsPartlyPublishedAs("ianus_IsPartlyPublishedAs","is partly published as","ist teilweise veröffentlicht als","",""),
		ianus_IsFullyPublishedAs("ianus_IsFullyPublishedAs","is fully available as","ist vollständig veröffentlicht als","","")
		;
	
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String URL;
		public final String description;
	
		private RelationType(String id, String labelEng, String labelGer, String URL, String description) {
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.URL = URL;
			this.description = description;
		}
		
		public static String getLabelEng(String valueId){
			for(Type type : Type.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
	}
	
	/*
	
	public enum RelationType{
		is_supplement_to		("is_supplement_to", "is supplement to", "ist Ergänzung zu"),
		is_supplemented_by		("is_supplemented_by", "is supplemented by", "wird ergänzt durch"),
		is_continuation_of		("is_continuation_of", "is continuation of", "ist Weiterführung von"),
		is_continued_by			("is_continued_by", "is continued by", "wird weitergeführt durch"),
		is_new_version_of		("is_new_version_of", "is new version of", "ist neuere Version von"),
		is_previous_version_of	("is_previous_version_of", "is previous version of", "ist ältere Version von"),
		is_part_of				("is_part_of", "is part of", "ist Teil von"),
		has_part				("has_part", "has part", "hat als Teil"),
		is_documentation_of		("is_documentation_of", "is documentation of", "ist Dokumentation zu"),
		is_documented_by		("is_documented_by", "is documented by", "wird dokumentiert von"),
		is_variant_form_of		("is_variant_form_of", "is variant form of", "ist Variante von"),
		is_original_form_of		("is_original_form_of", "is original form of", " ist Originalversion von"),
		has_metadata			("has_metadata", "has metadata", "enthält Metadaten zu"),
		is_metadata_for			("is_metadata_for", "is metadata for", "ist Metadatum für"),
		is_main_project_of		("is_main_project_of", "is main project of", " ist übergeordnetes Projekt zu"),
		is_subproject_of		("is_subproject_of", "is sub-project of", " ist untergeordnetes Projekt zu"),
		is_associated_archive_to("is_associated_archive_to", "is associated archive to", "ist zugehöriges Archiv zu"),
		has_associated_archive	("has_associated_archive", "has associated archive", "hat zugehöriges Archiv"),
		is_source_of			("is_source_of", "is source of", "ist Quelle für"),
		is_derived_from			("is_derived_from", "is derived from", "basiert auf"),
		is_partly_published_as	("is_partly_published_as", "ist teilweise veröffentlicht als", "ist teilweise veröffentlicht als"),
		is_fully_available_as	("is_fully_available_as", "is fully available as", "ist vollständig veröffentlicht als"),
		is_identical_to			("is_identical_to", "is identical to", "ist identisch mit")
		;
		
		public final String id;
		public final String labelEng;
		public final String labelDeu;
		
		private RelationType(String id, String labelEng, String labelDeu){
			this.id = id;
			this.labelEng = labelEng;
			this.labelDeu = labelDeu;
		}
		
		public static String getLabelEng(String valueId){
			for(Type type : Type.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
	}
	*/
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		json.addProperty("date", this.date);
		json.addProperty("description", this.description);
		json.add("identifier", this.identifier.toJsonObject(new JsonObject()));
		json.add("institution", this.institution.toJsonObject(new JsonObject()));
		json.addProperty("location", this.location);
		json.addProperty("relationType", this.relationType);
		json.addProperty("resourceType", this.resourceType);
		json.addProperty("scope", this.scope);
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<RelatedResource> list){
		JsonArray array = new JsonArray();
		for(RelatedResource item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("RelatedResourceDataType");
			xml.addAttribute("Date", this.date);
			xml.addAttribute("Description", this.description);
			xml.addAttribute("Location", this.location);
			xml.addAttribute("RelationType", this.relationType);
			xml.addAttribute("ResourceType", this.resourceType);
			xml.addAttribute("Scope", this.scope);
			XMLObject identifier = new XMLObject("Identifier");
				if(this.getIdentifier() != null)
					identifier.addDescendant(this.getIdentifier().toXMLObject());
			if(identifier.hasDescendants()) xml.addDescendant(identifier);
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<RelatedResource> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(RelatedResource item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	@Override
	public String toString(){
		return "RelatedResource [id="+id+", description=" + this.description + "]";
	}
}
