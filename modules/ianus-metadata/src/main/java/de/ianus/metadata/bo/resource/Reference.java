package de.ianus.metadata.bo.resource;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.xml.XMLObject;

@Entity(name="Reference")
public class Reference extends Element{
	
	@Column(name = "publicationId")
	private Long publicationId;
	
	//@Enumerated(EnumType.STRING)
	@Column(name="relationType")
	private String relationType = RelationType.ianus_basis.id;
	
	//Seitenzahl
	@Column(name="pagesNumber")
	private String pagesNumber;
	
	@Transient
	private Publication publication;
	
	@Transient
	private String sourceLabel;
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("publicationId", this.publicationId);
		json.addProperty("relationType", this.relationType);
		json.addProperty("pagesNumber", this.pagesNumber);
		
		json.add("publication", this.getPublication().toJsonObject(new JsonObject()));
		
		return json;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("RelatedPublicationDataType");
			XMLObject ref = new XMLObject("Reference");
				ref.addAttribute("RelationType", this.relationType);
				ref.addAttribute("PagesNumber", this.pagesNumber);
		xml.addDescendant(ref);
		xml.addDescendant(this.getPublication().toXMLObject());
		
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<Reference> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Reference item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	public String getSourceLabel() {
		return sourceLabel;
	}
	
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	public static JsonArray toJsonArray(Set<Reference> list){
		JsonArray array = new JsonArray();
		for(Reference item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public static Reference clone(Reference other, Long sourceId){
		Reference newItem = new Reference();
		newItem = (Reference)Element.clone(newItem, other, sourceId);
		
		newItem.publicationId = other.publicationId;
		newItem.relationType = other.relationType;
		newItem.pagesNumber = other.pagesNumber;
		
		return newItem;
	}
	
	public String getLabel(){
		String label = (this.publication != null) ? this.publication.getLabel() : new String();
		
		if(!label.isEmpty() && !this.pagesNumber.isEmpty()) {
			label = label + " S. " + this.pagesNumber + ".";
		}
		return label;
		
	}
	

	
	public String getLabelWithPublication(){
		return  this.publication.getLabel() + " " + this.pagesNumber + ".";
	}
	
	public boolean isCompleted(){
		return this.sourceId != null && this.publicationId != null;
	}	

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getPagesNumber() {
		return pagesNumber;
	}

	public void setPagesNumber(String pagesNumber) {
		this.pagesNumber = pagesNumber;
	}

	public Long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
		this.publicationId = (publication == null) ? null:publication.getId();
	}

	public enum RelationType {
		
		ianus_firstpubl("ianus_firstpubl","first publication","Erstpublikation","",""),
		ianus_finalrep("ianus_finalrep","final results","Endergebnisse","",""),
		ianus_partres("ianus_partres","partial results","Teilergebnisse","",""),
		ianus_prelimrep("ianus_prelimrep","preliminary report","Vor-/Zwischenbericht","",""),
		ianus_basis("ianus_basis","basis of research","Forschungsgrundlage","",""),
		ianus_other("ianus_other","other","Sonstiges","","")
		;
	
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String URL;
		public final String description;
	
		private RelationType(String id, String labelEng, String labelGer, String URL, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.URL = URL;
			this.description = description;
		}
	}
	
	@Override
	public String toString(){
		return "Reference [id=" + id + 
				", sourceClass=" + getSourceClass() + 
				", sourceId=" + getSourceId() +
				", publicationId=" + publicationId +
				", pagesNumber=" + pagesNumber + "]";
	}
}
