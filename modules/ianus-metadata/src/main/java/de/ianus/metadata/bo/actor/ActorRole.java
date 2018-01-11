package de.ianus.metadata.bo.actor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.ianus.metadata.bo.Element;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.xml.XMLObject;

/**
 * This class represents the role of a person or of a institution for a data collection of for a collection file.
 * @author jurzua
 *
 */
@Entity
@Table(name = "ActorRole")
public class ActorRole extends Element {

	//Identificator of a Person or of an institution
	@Column(name = "actorId")
	private Long actorId;
	
	// TODO: do not set this value initially
	@Column(name = "actorClass")
	private BOUtils.ActorClass actorClass = BOUtils.ActorClass.Person;
	
	//@Enumerated(EnumType.STRING)
	@Column(name = "typeId")
	private String typeId = Type.dcms_ContactPerson.id;
	
	/**
	 * @deprecated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "functionStart")
	private Date functionStart;
	
	/**
	 * @deprecated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "functionEnd")
	private Date functionEnd;

	@Column(name = "functionStartText")
	private String functionStartText;

	@Column(name = "functionEndText")
	private String functionEndText;
	
	public static ActorRole clone(ActorRole other, Long actorId, Long sourceId){
		ActorRole newItem = new ActorRole();
		newItem = (ActorRole)Element.clone(newItem, other, sourceId);
		newItem.actorId = actorId;
		newItem.actorClass = other.actorClass;
		newItem.typeId = other.typeId;
		newItem.functionStart = other.functionStart;
		newItem.functionEnd = other.functionEnd;
		newItem.functionStartText = other.functionStartText;
		newItem.functionEndText = other.functionEndText;
		
		return newItem;
	}
	
	public ActorRole(){}
	
	public ActorRole(Long sourceId, Long actorId, Type type){
		this.sourceId = sourceId;
		this.actorId = actorId;
		this.typeId = type.id;
	}
	
	/**
	 * @deprecated
	 */
	public Date getFunctionStart() {
		return functionStart;
	}
	
	/**
	 * @deprecated
	 */
	public void setFunctionStart(Date functionStart) {
		this.functionStart = functionStart;
	}

	/**
	 * @deprecated
	 */
	public Date getFunctionEnd() {
		return functionEnd;
	}
	
	/**
	 * @deprecated
	 */
	public void setFunctionEnd(Date functionEnd) {
		this.functionEnd = functionEnd;
	}
	
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}

	public BOUtils.ActorClass getActorClass() {
		return actorClass;
	}

	public void setActorClass(BOUtils.ActorClass actorClass) {
		this.actorClass = actorClass;
	}
	
	// TODO: move this into constructor?
	public void setActor(Actor actor) throws Exception{
		this.actorId = actor.getId();
		if(actor instanceof Person){
			this.actorClass = BOUtils.ActorClass.Person;
		}else if(actor instanceof Institution){
			this.actorClass = BOUtils.ActorClass.Institution;
		}else{
			throw new Exception("");
		}
	}
	
	/**
	 * @deprecated
	 */
	public String getFormattedFunctionEnd(){
		return formatDate(functionEnd);
	}
	
	/**
	 * @deprecated
	 */
	public String getFormattedFunctionStart(){
		return formatDate(functionStart);
	}
	
	public String getTimeSpanText(){
		String text = (StringUtils.isNotEmpty(functionStartText) && StringUtils.isNotEmpty(functionEndText)) ? 
				functionStartText + " - " + functionEndText : 
					((StringUtils.isNotEmpty(functionStartText)) ? functionStartText : "") + 
					((StringUtils.isNotEmpty(functionEndText)) ? functionEndText : "");
		return text;
	}
	
	/**
	 * @deprecated
	 */
	public String getTimeSpan(){
		return getFormattedFunctionStart() + " - " + getFormattedFunctionEnd();
	}
	
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("actorId", this.actorId);
		json.addProperty("sourceId", this.sourceId);
		json.addProperty("role", this.typeId.toString());
		json.addProperty("functionEnd", this.getFormattedFunctionEnd());
		json.addProperty("functionStart", this.getFormattedFunctionStart());
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<ActorRole> list){
		JsonArray array = new JsonArray();
		for(ActorRole item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("ActorRole");
		xml.addAttribute("RoleValue", this.typeId.toString());
		xml.addAttribute("FunctionStart", this.functionStartText);
		xml.addAttribute("FunctionEnd", this.functionEndText);
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<ActorRole> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(ActorRole item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}
	
	public String getFunctionStartText() {
		return functionStartText;
	}

	public void setFunctionStartText(String functionStartText) {
		this.functionStartText = functionStartText;
	}

	public String getFunctionEndText() {
		return functionEndText;
	}

	public void setFunctionEndText(String functionEndText) {
		this.functionEndText = functionEndText;
	}
	
	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set. Solution: throws an exception (no persistent...)
        if (!(object instanceof ActorRole)) {
            return false;
        }
        ActorRole other = (ActorRole) object;
        
        if(this.id != null && other.id != null && this.id.longValue() == other.id.longValue())
        	return true;
        
        return false;
    }
	
	// contact_person, author, licensor, metadata_editor, curator
	
	public enum Type {
		
		dcms_ContactPerson("dcms_ContactPerson","Contact Person","Kontaktperson","https://doi.org/10.5438/0010","Person with knowledge of how to access, troubleshoot, or otherwise field issues related to the resource"),
		dcms_Distributor("dcms_Distributor","Distributor","Datenbereitsteller","https://doi.org/10.5438/0010","Institution tasked with responsibility to generate/disseminate copies of the resource in either electronic or print form."),
		dcms_DataCurator("dcms_DataCurator","Data Curator","Datenkurator","https://doi.org/10.5438/0010","Person tasked with reviewing, enhancing, cleaning, or standardizing metadata and the associated data submitted for storage, use, and maintenance within a data center or repository"),
		dcms_DataCollector("dcms_DataCollector","Data Collector","Datenersteller","https://doi.org/10.5438/0010","Person/institution responsible for finding, gathering/collecting data under the guidelines of the author(s) or Principal Investigator (PI)"),
		dcms_Funder("dcms_Funder","Funder","Förderer","https://doi.org/10.5438/0010","Institution that provided financial support for the development of the resource."),
		dcms_HostingInstitution("dcms_HostingInstitution","Hosting Institution","Datenhaltende Institution","https://doi.org/10.5438/0010","Typically, the organization allowing the resource to be available on the internet through the provision of its hardware/software/operating support."),
		dcms_Editor("dcms_Editor","Editor","Herausgeber","https://doi.org/10.5438/0010","A person who oversees the details related to the publication format of the resource."),
		dcms_ProjectLeader("dcms_ProjectLeader","Project Leader","Projektleiter","https://doi.org/10.5438/0010","Person officially designated as head of project team or sub‐project team instrumental in the work necessary to development of the resource."),
		dcms_ProjectManager("dcms_ProjectManager","Project Manager","Projektkoordinator","https://doi.org/10.5438/0010","Person officially designated as manager of a project. Project may consist of one or many project teams and sub‐teams."),
		dcms_ProjectMember("dcms_ProjectMember","Project Member","Projektmitarbeiter","https://doi.org/10.5438/0010","Person on the membership list of a designated project/project team."),
		dcms_RelatedPerson("dcms_RelatedPerson","Related Person","zugehörige Person","https://doi.org/10.5438/0010","A person without a specifically defined role in the development of the resource, but who is someone the author wishes to recognize."),
		dcms_Researcher("dcms_Researcher","Researcher","Wissenschaftler","https://doi.org/10.5438/0010","A person involved in analyzing data or the results of an experiment or formal study. May indicate an intern or assistant to one of the authors who helped with research but who was not so “key” as to be listed as an author."),
		dcms_ResearchGroup("dcms_ResearchGroup","Research Group","Forschungsgruppe","https://doi.org/10.5438/0010","Typically refers to a group of individuals with a lab, department, or division; the group has a particular, defined focus of activity."),
		dcms_RightsHolder("dcms_RightsHolder","Rights Holder","Rechteinhaber","https://doi.org/10.5438/0010","Person or institution owning or managing property rights, including intellectual property rights over the resource."),
		dcms_DataManager("dcms_DataManager","Data Manager","Daten/IT-Beauftragter","https://doi.org/10.5438/0010","Person (or organization with a staff of data managers, such as a data centre) responsible for maintaining the finished resource."),
		dcms_Other("dcms_Other","Other","Andere","https://doi.org/10.5438/0010","Any person or institution making a significant contribution to the development and/or maintenance of the resource, but whose contribution does not “fit” other controlled vocabulary for contributorType."),
		ianus_cooppartner("ianus_cooppartner","cooperation partner","Kooperationspartner","",""),
		ianus_Signee("ianus_Signee","Signee Deposit Licence","Unterzeichner Übergabevertrag","",""),
		ianus_Depositor("ianus_Depositor","Depositor","Datengeber","",""),
		ianus_PrincipalInvest("ianus_PrincipalInvest","Principal Investigator","Primärforscher","",""),
		ianus_Coauthor("ianus_Coauthor","Co-author","Co-Autor","",""),
		ianus_Lecturer("ianus_Lecturer","Lecturer","Dozent","",""),
		ianus_AuthorMetadata("ianus_AuthorMetadata","Author Metadata","Autor Metadaten","",""),
		ianus_GeneralDirector("ianus_GeneralDirector","General Director","Institutsleiter","",""),
		ianus_Director("ianus_Director","Director","Direktor","",""),
		ianus_TechnicalAssist("ianus_TechnicalAssist","Technical Assistant","technischer Mitarbeiter","",""),
		ianus_FieldWorkStaff("ianus_FieldWorkStaff","Field Work Staff","Mitarbeiter Feldforschung","","spezifiziert die Rolle 'Projektmitarbeiter', sofern dieser nicht zu einer Ausgrabung, Survey, Bauaufnahme oder Fundbearbeitung gehört."),
		ianus_FieldWorkDirector("ianus_FieldWorkDirector","Field Work Director","Leiter Feldforschung","","spezifiziert die Rolle 'Projektleiter', sofern dieser nicht zu einer Ausgrabung, Survey, Bauaufnahme oder Fundbearbeitung gehört."),
		ianus_ExcavationStaff("ianus_ExcavationStaff","Excavation Staff","Mitarbeiter Ausgrabung","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für Ausgrabungen"),
		ianus_ExcavationLeader("ianus_ExcavationLeader","Excavation Leader","Leiter Ausgrabung","","spezifiziert die Rolle 'Projektleiter', dezidiert für Ausgrabungen"),
		ianus_SurveyStaff("ianus_SurveyStaff","Survey Staff","Mitarbeiter Survey","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für Surveys"),
		ianus_SurveyLeader("ianus_SurveyLeader","Survey Leader","Leiter Survey","","spezifiziert die Rolle 'Projektleiter', dezidiert für Surveys"),
		ianus_ArchitectSurveyStaff("ianus_ArchitectSurveyStaff","Architectural Survey Staff","Mitarbeiter Bauaufnahme","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für Bauaufnahmen"),
		ianus_ArchitectSurveyLeader("ianus_ArchitectSurveyLeader","Architectural Survey Leader","Leiter Bauaufnahme","","spezifiziert die Rolle 'Projektleiter', dezidiert für Bauaufnahmen"),
		ianus_FindsProcessor("ianus_FindsProcessor","Finds Processor","Fundbearbeiter","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für die Fundbearbeitung"),
		ianus_FindsProcessSupervisor("ianus_FindsProcessSupervisor","Finds Processing Supervisor","Leitung Fundbearbeitung","","spezifiziert die Rolle 'Projektleiter', dezidiert für die Fundbearbeitung"),
		ianus_Fotographer("ianus_Fotographer","Fotographer","Fotograf","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_FieldSurveyor("ianus_FieldSurveyor","Field Surveyor","Vermesser","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_SiteTechnician("ianus_SiteTechnician","Site Technician","Grabungstechniker","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_Draughtsman("ianus_Draughtsman","Draughtsman","Zeichner","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_SectionSupervisor("ianus_SectionSupervisor","Section Supervisor","Schnittleiter","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_Conservator("ianus_Conservator","Conservator","Konservator","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller")
		
		/*2017.08.21
		dcms_ContactPerson("dcms_ContactPerson","Contact Person","Kontaktperson","https://doi.org/10.5438/0010","Person with knowledge of how to access, troubleshoot, or otherwise field issues related to the resource"),
		dcms_Distributor("dcms_Distributor","Distributor","Datenbereitsteller","https://doi.org/10.5438/0010","Institution tasked with responsibility to generate/disseminate copies of the resource in either electronic or print form."),
		dcms_DataCurator("dcms_DataCurator","Data Curator","Datenkurator","https://doi.org/10.5438/0010","Person tasked with reviewing, enhancing, cleaning, or standardizing metadata and the associated data submitted for storage, use, and maintenance within a data center or repository"),
		dcms_DataCollector("dcms_DataCollector","Data Collector","Datenersteller","https://doi.org/10.5438/0010","Person/institution responsible for finding, gathering/collecting data under the guidelines of the author(s) or Principal Investigator (PI)"),
		dcms_Funder("dcms_Funder","Funder","Förderer","https://doi.org/10.5438/0010","Institution that provided financial support for the development of the resource."),
		dcms_HostingInstitution("dcms_HostingInstitution","Hosting Institution","Datenhaltende Institution","https://doi.org/10.5438/0010","Typically, the organization allowing the resource to be available on the internet through the provision of its hardware/software/operating support."),
		dcms_Editor("dcms_Editor","Editor","Herausgeber","https://doi.org/10.5438/0010","A person who oversees the details related to the publication format of the resource."),
		dcms_ProjectLeader("dcms_ProjectLeader","Project Leader","Projektleiter","https://doi.org/10.5438/0010","Person officially designated as head of project team or sub‐project team instrumental in the work necessary to development of the resource."),
		dcms_ProjectManager("dcms_ProjectManager","Project Manager","Projektkoordinator","https://doi.org/10.5438/0010","Person officially designated as manager of a project. Project may consist of one or many project teams and sub‐teams."),
		dcms_ProjectMember("dcms_ProjectMember","Project Member","Projektmitarbeiter","https://doi.org/10.5438/0010","Person on the membership list of a designated project/project team."),
		dcms_RelatedPerson("dcms_RelatedPerson","Related Person","zugehörige Person","https://doi.org/10.5438/0010","A person without a specifically defined role in the development of the resource, but who is someone the author wishes to recognize."),
		dcms_Researcher("dcms_Researcher","Researcher","Wissenschaftler","https://doi.org/10.5438/0010","A person involved in analyzing data or the results of an experiment or formal study. May indicate an intern or assistant to one of the authors who helped with research but who was not so “key” as to be listed as an author."),
		dcms_ResearchGroup("dcms_ResearchGroup","Research Group","Forschungsgruppe","https://doi.org/10.5438/0010","Typically refers to a group of individuals with a lab, department, or division; the group has a particular, defined focus of activity."),
		dcms_RightsHolder("dcms_RightsHolder","Rights Holder","Rechteinhaber","https://doi.org/10.5438/0010","Person or institution owning or managing property rights, including intellectual property rights over the resource."),
		dcms_DataManager("dcms_DataManager","Data Manager","Daten/IT-Beauftragter","https://doi.org/10.5438/0010","Person (or organization with a staff of data managers, such as a data centre) responsible for maintaining the finished resource."),
		dcms_Other("dcms_Other","Other","Andere","https://doi.org/10.5438/0010","Any person or institution making a significant contribution to the development and/or maintenance of the resource, but whose contribution does not “fit” other controlled vocabulary for contributorType."),
		ianus_Signee("ianus_Signee","Signee Deposit Licence","Unterzeichner Übergabevertrag","",""),
		ianus_Depositor("ianus_Depositor","Depositor","Datengeber","",""),
		ianus_PrincipalInvest("ianus_PrincipalInvest","Principal Investigator","Primärforscher","",""),
		ianus_Coauthor("ianus_Coauthor","Co-author","Co-Autor","",""),
		ianus_Lecturer("ianus_Lecturer","Lecturer","Dozent","",""),
		ianus_AuthorMetadata("ianus_AuthorMetadata","Author Metadata","Autor Metadaten","",""),
		ianus_GeneralDirector("ianus_GeneralDirector","General Director","Institutsleiter","",""),
		ianus_Director("ianus_Director","Director","Direktor","",""),
		ianus_TechnicalAssist("ianus_TechnicalAssist","Technical Assistant","technischer Mitarbeiter","",""),
		ianus_FieldWorkStaff("ianus_FieldWorkStaff","Field Work Staff","Mitarbeiter Feldforschung","","spezifiziert die Rolle 'Projektmitarbeiter', sofern dieser nicht zu einer Ausgrabung, Survey, Bauaufnahme oder Fundbearbeitung gehört."),
		ianus_FieldWorkDirector("ianus_FieldWorkDirector","Field Work Director","Leiter Feldforschung","","spezifiziert die Rolle 'Projektleiter', sofern dieser nicht zu einer Ausgrabung, Survey, Bauaufnahme oder Fundbearbeitung gehört."),
		ianus_ExcavationStaff("ianus_ExcavationStaff","Excavation Staff","Mitarbeiter Ausgrabung","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für Ausgrabungen"),
		ianus_ExcavationLeader("ianus_ExcavationLeader","Excavation Leader","Leiter Ausgrabung","","spezifiziert die Rolle 'Projektleiter', dezidiert für Ausgrabungen"),
		ianus_SurveyStaff("ianus_SurveyStaff","Survey Staff","Mitarbeiter Survey","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für Surveys"),
		ianus_SurveyLeader("ianus_SurveyLeader","Survey Leader","Leiter Survey","","spezifiziert die Rolle 'Projektleiter', dezidiert für Surveys"),
		ianus_ArchitectSurveyStaff("ianus_ArchitectSurveyStaff","Architectural Survey Staff","Mitarbeiter Bauaufnahme","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für Bauaufnahmen"),
		ianus_ArchitectSurveyLeader("ianus_ArchitectSurveyLeader","Architectural Survey Leader","Leiter Bauaufnahme","","spezifiziert die Rolle 'Projektleiter', dezidiert für Bauaufnahmen"),
		ianus_FindsProcessor("ianus_FindsProcessor","Finds Processor","Fundbearbeiter","","spezifiziert die Rolle 'Projektmitarbeiter', dezidiert für die Fundbearbeitung"),
		ianus_FindsProcessSupervisor("ianus_FindsProcessSupervisor","Finds Processing Supervisor","Leitung Fundbearbeitung","","spezifiziert die Rolle 'Projektleiter', dezidiert für die Fundbearbeitung"),
		ianus_Fotographer("ianus_Fotographer","Fotographer","Fotograf","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_FieldSurveyor("ianus_FieldSurveyor","Field Surveyor","Vermesser","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_SiteTechnician("ianus_SiteTechnician","Site Technician","Grabungstechniker","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_Draughtsman("ianus_Draughtsman","Draughtsman","Zeichner","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_SectionSupervisor("ianus_SectionSupervisor","Section Supervisor","Schnittleiter","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller"),
		ianus_Conservator("ianus_Conservator","Conservator","Konservator","","spezifiziert die Rolle 'Projektmitarbeiter' bzw. 'Datenersteller")
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
	
	@Override
	public String toString(){
		return "ActorRole [id=" + getId() + ", " + super.getLabel() + ", typeId=" + typeId + ", actorId=" + actorId + ", actorClass=" + actorClass + "]";
	}
}
