package de.ianus.metadata.bo;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.FileFormat;
import de.ianus.metadata.bo.utils.Software;
import de.ianus.metadata.bo.utils.TextAttribute;

@Entity
@Table(name="CollectionFile")
public class CollectionFile extends IANUSEntity{
	
	//Formatangaben
	//-------------------------------------------
	//Dateikategorie
	@Column(name="category")
	private String category;
	
	//Dateierweiterung
	@Column(name="extension")
	private String extension;
	
	@Transient
	private Set<FileFormat> fileFormatList = new LinkedHashSet<FileFormat>();
	
	//Soft- & Hardware
	//-------------------------------------------
	@Transient
	private Set<Software> softwareList = new LinkedHashSet<Software>();
	
	//Fachlich
	//-------------------------------------------
	
	//Speicherort
	@Column(name="storePlace")
	private String storePlace;
	
	//Speichergröße in KB
	@Column(name="memorySize")
	private String memorySize;
	
	//Datum_Erstellung
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="fileCreation")
    private Date fileCreation;
	
	//Datum_Aenderung
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="fileModification")
    private Date fileModification;
	
	//Prüfsumme
	@Column(name="checksum")
	private String checksum;

	//Verfahren
	@Column(name="checksumAlgorithm")
	private String checksumAlgorithm;
	
	//IANUS-ID_Ref
	@Column(name="ianusId")
	private String ianusId;
	
	//Geographisch
	//-------------------------------------------
	//Beschreibung_kurz
	//@Transient
	//private Set<TextAttribute> shortDescriptionList = new LinkedHashSet<TextAttribute>(); 
	
	//Schlagwort
	@Transient
	private Set<ElementOfList> keywordList = new LinkedHashSet<ElementOfList>();
		
	//PublikationID
	//-------------------------------------------
	
	//Zugehörige Quellen
	
	//Alternative Bereitstellung
	
	//IANUS

	//Urheber

	//Datenschutz
	
	//Zugriffsrechte

	//Sprache
	
	//Bearbeiter

	//Erhaltungsmaßnahmen

	
	public JsonObject toJsonObject(JsonObject json, DataCollection dc){
		
		json = super.toJsonObject(json);
		
		
		json.addProperty("category", this.category);
		json.addProperty("extension", this.extension);
		json.addProperty("storePlace", this.storePlace);
		json.addProperty("memorySize", this.memorySize);
		
		json.addProperty("fileCreation", this.getFormattedFileCreation());
		json.addProperty("fileModification", this.getFormattedFileModification());
		
		json.addProperty("checksumAlgorithm", this.checksumAlgorithm);
		json.addProperty("checksum", this.checksum);
		
		
		if(dc.getActorMap().values() != null) {
			json.add("actorList", Actor.toJsonArray(new HashSet<Actor>(dc.getActorMap().values())));
		}
		// create a list of actors by role
		for(ActorRole.Type type : ActorRole.Type.values()) {
			if(dc.getActorList(type) != null && dc.getActorList(type).size() > 0)
			json.add(type.id, Actor.toJsonArray(dc.getActorList(type)));
		}
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<CollectionFile> list, DataCollection dc){
		JsonArray array = new JsonArray();
		for(CollectionFile item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject(), dc);
			array.add(item0);
		}
		return array;
	}
	
	public String getFormattedFileCreation(){
		if(fileCreation != null){
			return DateFormat.getDateTimeInstance(
		            DateFormat.MEDIUM, DateFormat.SHORT).format(fileCreation);	
		}
		return null;
	}
	
	public String getFormattedFileModification(){
		if(fileModification != null){
			return DateFormat.getDateTimeInstance(
		            DateFormat.MEDIUM, DateFormat.SHORT).format(fileModification);	
		}
		return null;
	}
	
	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getExtension() {
		return extension;
	}


	public void setExtension(String extension) {
		this.extension = extension;
	}


	public Set<FileFormat> getFileFormatList() {
		return fileFormatList;
	}


	public void setFileFormatList(Set<FileFormat> fileFormatList) {
		this.fileFormatList = fileFormatList;
	}


	public Set<Software> getSoftwareList() {
		return softwareList;
	}


	public void setSoftwareList(Set<Software> softwareList) {
		this.softwareList = softwareList;
	}


	public String getStorePlace() {
		return storePlace;
	}


	public void setStorePlace(String storePlace) {
		this.storePlace = storePlace;
	}

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public Date getFileCreation() {
		return fileCreation;
	}

	public void setFileCreation(Date fileCreation) {
		this.fileCreation = fileCreation;
	}

	public Date getFileModification() {
		return fileModification;
	}

	public void setFileModification(Date fileModification) {
		this.fileModification = fileModification;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getChecksumAlgorithm() {
		return checksumAlgorithm;
	}

	public void setChecksumAlgorithm(String checksumAlgorithm) {
		this.checksumAlgorithm = checksumAlgorithm;
	}

	public String getIanusId() {
		return ianusId;
	}

	public void setIanusId(String ianusId) {
		this.ianusId = ianusId;
	}

	public Set<TextAttribute> getShortDescriptionList() {
		return this.attributeMap.get(TextAttribute.ContentType.shortDescription);
	}

	public Set<TextAttribute> getLongDescriptionList() {
		return this.attributeMap.get(TextAttribute.ContentType.longDescription);
	}

	public Set<ElementOfList> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(Set<ElementOfList> keywordList) {
		this.keywordList = keywordList;
	}
}
