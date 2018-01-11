package de.ianus.metadata.bo.resource;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.xml.XMLObject;

/**
 * @author jurzua
 *
 */
@Entity(name="Publication")
@Indexed
public class Publication extends MDEntry{
	
	//@Enumerated(EnumType.STRING)
	@Column(name="type")
	private String type = Type.ianus_book.id;
	
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name="author")
	private String author;
	
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name="title")
	private String title;
	
	//Reihe_Zeitschrift
	@Column(name="series")
	private String  series;
	
	//Sammelwerk_Titel
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name="collectionTitle")
	private String collectionTitle;
	
	//Sammelwerk_Hrsg
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name="collectionEditor")
	private String collectionEditor;
	
	//Verlag
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name="publisher")
	private String publisher;
	
	//Stadt
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name="city")
	private String city;
	
	@Column(name="year")
	private String year;
	
	//Bandprivate Identifier externalId = new Identifier();
	@Column(name="volume")
	private String volume;
	
	//Zugehörige Quellen?
	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	private boolean analog = false; //or digital
	
	@Transient
	private Set<Identifier> pidList = new LinkedHashSet<Identifier>();

	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("type", this.type);
		json.addProperty("author", this.author);
		json.addProperty("series", this.series);
		json.addProperty("title", this.title);
		json.addProperty("collectionTitle", this.collectionTitle);
		json.addProperty("collectionEditor", this.collectionEditor);
		json.addProperty("publisher", this.publisher);
		json.addProperty("city", this.city);
		json.addProperty("year", this.year);
		json.addProperty("volume", this.volume);
		json.addProperty("analog", this.analog);
		
		JsonArray pidArray = new JsonArray();
		for(Identifier pid : getPidList()){
			pidArray.add(pid.toJsonObject(new JsonObject()));
		}
		json.add("pidList", pidArray);
		
		return json;
	}
	
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("Publication");
			xml.addAttribute("Type", this.type);
			xml.addAttribute("Author", this.author);
			xml.addAttribute("Series", this.series);
			xml.addAttribute("Title", this.title);
			xml.addAttribute("CollectionTitle", this.collectionTitle);
			xml.addAttribute("CollectionEditor", this.collectionEditor);
			xml.addAttribute("Publisher", this.publisher);
			xml.addAttribute("City", this.city);
			xml.addAttribute("Year", this.year);
			xml.addAttribute("Volume", this.volume);
			//juc: this line was pushed, although it does compile. The method xml.addAttribute does not accept boolean as input 
			xml.addAttribute("Analogue", this.analog + "");
			XMLObject pids = new XMLObject("Pid");
				pids.addDescendants(Identifier.toXMLObjects(this.getPidList()));
			if(pids.hasDescendants()) xml.addDescendant(pids);
		return xml;
	}
	
	

    public String getLabel(){

		String label = "";

		if (!this.author.isEmpty() && !this.title.isEmpty()) {
			label += this.author;
			if (!this.title.isEmpty()) {
				label += ", " + this.title;
			}
			// publications in a collection
			if (!this.collectionEditor.isEmpty() && !this.collectionTitle.isEmpty()) {
				label += ", in: " + this.collectionEditor + " (Hrsg.), ";
				label += this.collectionTitle;
			}
			label += ".";
		} else if (!this.collectionEditor.isEmpty() && !this.collectionTitle.isEmpty()) {
			// author title is empty: publication is an entire collection
			label += this.collectionEditor + " (Hrsg.), ";
			label += this.collectionTitle + ".";
		} else if (!this.collectionEditor.isEmpty() && !this.series.isEmpty()) {
			// author title is empty: publication is an entire series
			label += this.collectionEditor + " (Hrsg.),";
		}

		if (!this.series.isEmpty() && !this.volume.isEmpty()) {
			label += " " + this.series + " " + this.volume + ".";
		}

		if (!this.city.isEmpty()) {
			label += " " + this.city;
		}

		if (!this.city.isEmpty() && !this.publisher.isEmpty()) {
			label += ": " + this.publisher;
		}

		if (!this.year.isEmpty()) {
			label += " (" + this.year + ")";
		}

		label += ".";

		return label;
    } 
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getVolume() {
		return volume;
	}


	public void setVolume(String volume) {
		this.volume = volume;
	}


	public boolean isAnalog() {
		return analog;
	}

	public void setAnalog(boolean analog) {
		this.analog = analog;
	}	

	public Set<Identifier> getPidList() {
		return pidList;
	}

	public void setPidList(Set<Identifier> pidList) {
		this.pidList = pidList;
	}

	public String getCollectionTitle() {
		return collectionTitle;
	}

	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}

	public String getCollectionEditor() {
		return collectionEditor;
	}

	public void setCollectionEditor(String collectionEditor) {
		this.collectionEditor = collectionEditor;
	}
	
	/**
	 * Documentation: see http://10.201.0.32:8090/pages/viewpage.action?pageId=23136344
	 * Die folgende Liste beruht in wesentlichen Teilen auf dem DataCite Metadata Schema 3.1:
	 * https://schema.datacite.org/meta/kernel-3.1/doc/DataCite-MetadataKernel_v3.1.pdf Seiten 28-30
	 * @author jurzua
	 *
	 */
	/*
	public enum PidList{
		
		ARK("ARK", "Archival Resource Key", "", "", ""),
		DOI("DOI", "Digital Object Identifier", "", "http://dx.doi.org/", ""),
		EAN13("EAN13", "European Article Number", "", "", ""),
		EISSN("EISSN", "Electronic International Standard Serial Number", "", "", ""),
		Handle("Handle", "Handle", "", "", ""),
		iDAI_books("iDAI.books", "iDAI.bibliography", "", "http://zenon.dainst.org/Record/", "Identifier of iDAI.books (= ZENON)"),
		IRI("IRI", "Internationalized Resource Identifier", "", "", ""),
		ISBN("ISBN", "International Standard Book Number", "", "", ""),
		ISSN("ISSN", "International Standard Serial Number", "", "", ""),
		ISTC("ISTC", "International Standard Text Code", "", "", ""),
		LISSN("LISSN", "linking ISSN", "", "", ""),
		LSID("LSID", "Life Science Identifiers", "", "", ""),
		PURL("PURL", "Persistent Uniform Resource Locator", "", "", ""),
		UPC("UPC", "Universal Product Code", "", "", ""),
		URI("URI", "Uniform Resource Identifier", "", "", ""),
		URL("URL", "Uniform Resource Locator", "", "", ""),
		URN("URN", "Uniform Resource Name", "", "", "");
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;
		private PidList(String id, String labelEng, String labelGer, String url, String desc){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = desc;
		}
		
		public static PidList getById(String id) {
			if(id.equals("")) return null;
			for(PidList item : PidList.values()) {
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
	*/

	public enum Type{
		
		ianus_book("ianus_book","book / monograph","Monographie","",""),
		ianus_edcoll("ianus_edcoll","edited collection","Sammelwerk","",""),
		ianus_journal("ianus_journal","journal","Zeitschrift","",""),
		ianus_jourart("ianus_jourart","journal article","Zeitschriftenartikel","",""),
		ianus_confart("ianus_confart","conference article","Kongressbeitrag","",""),
		ianus_coll("ianus_coll","article in edited collection","Beitrag Sammelwerk","",""),
		ianus_catal("ianus_catal","catalogue","Katalog","",""),
		ianus_review("ianus_review","review","Rezension","",""),
		ianus_newsp("ianus_newsp","newspaper","Zeitung","",""),
		ianus_website("ianus_website","website","Webseite","",""),
		ianus_blog("ianus_blog","blog","Blog","",""),
		ianus_wiki("ianus_wiki","wiki","Wiki","",""),
		ianus_newsl("ianus_newsl","newsletter","Newsletter","","")
		
		/*2017.08.21
		ianus_book("ianus_book","book / monograph","Monographie","",""),
		ianus_journal("ianus_journal","journal","Zeitschrift","",""),
		ianus_jourart("ianus_jourart","journal article","Zeitschriftenartikel","",""),
		ianus_confart("ianus_confart","conference article","Kongressbeitrag","",""),
		ianus_collection("ianus_collection","collection","Sammelwerk","",""),
		ianus_coll("ianus_coll","article in edited collection","Beitrag Sammelwerk","",""),
		ianus_catal("ianus_catal","catalogue","Katalog","",""),
		ianus_review("ianus_review","review","Rezension","",""),
		ianus_newsp("ianus_newsp","newspaper","Zeitung","",""),
		ianus_blog("ianus_blog","blog","Blog","",""),
		ianus_wiki("ianus_wiki","wiki","Wiki","",""),
		ianus_newsl("ianus_newsl","newsletter","Newsletter","","")
		*/
		;

		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;

		private Type(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static Type getById(String id) {
			if(id.equals("")) return null;
			for(Type item : Type.values()) {
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
}
