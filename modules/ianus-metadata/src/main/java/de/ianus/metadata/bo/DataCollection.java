package de.ianus.metadata.bo;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.JsonObject;

import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.resource.RelatedResource;
import de.ianus.metadata.bo.utils.AlternativeWRState;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Language;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.SimpleDate;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.Time;
import de.ianus.metadata.bo.utils.TimeSpan;
import de.ianus.metadata.xml.XMLObject;


/**
 * 
 * This class is described in
 * <a href="http://confluence:8090/display/IAN/Data+Collection">Documentation</a>.
 * 
 * ALTER TABLE `ianus_metadata`.`DataCollection`  DROP COLUMN `infPrivacyType`, DROP COLUMN `infPrivacyRelevant`, DROP COLUMN `infPrivacyDescription`;
 * 
 * @author jurzua
 *
 */
@Entity
@Table(name="DataCollection")
public class DataCollection extends IANUSEntity{
	
	public static String GERMAN_CODE = "deu"; // german language code
	public static String ENGLISH_CODE = "eng"; // english language code
		
	public DataCollection(){}
	
	/**
	 * <p>This constructor is used to clone the given DataCollection. 
	 * This method clones only the attributes that are modeled as primitive data types (Long, String, Boolean, etc). 
	 * More complex attributes should be cloned by the MetadataService. 
	 * The reason for this decision is that complex attributes have ids and are stored in the DB.
	 * </p>
	 * 
	 * @param another
	 */
	public DataCollection(DataCollection another){
		this.id = null;
		this.label = another.label;
		this.contentRemarks = another.contentRemarks;
		this.wrState = another.wrState;
		this.projectFinished = another.projectFinished;
		this.projectStatus = another.projectStatus;
		this.projectStart = another.projectStart;
		this.projectEnd = another.projectEnd;
		this.dataProtection = another.dataProtection;
		//this.reasonDataProtection = another.reasonDataProtection;
		this.basisDataProtection = another.basisDataProtection;
				
		this.licenseName = another.licenseName;
		this.licenseVersion = another.licenseVersion;
		this.licenseUrl = another.licenseUrl;
		this.accessability = another.accessability;		
		
		//this.dataDescription = another.dataDescription;
		this.fileNumber = another.fileNumber;
		this.memorySize = another.memorySize;
		this.custodialHistory = another.custodialHistory;
		
		this.lastChangeMetadata = another.lastChangeMetadata;
		this.sipFinalization = another.sipFinalization;
		this.signingContract = another.signingContract;
		this.curationStart = another.curationStart;
		this.curationEnd = another.curationEnd;
		this.presentationDate = another.presentationDate;
		this.lastTest = another.lastTest;
	}
	
	@Transient
	private Map<Long, Actor> actorMap = new HashMap<Long, Actor>();
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//	Titel
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	//Title
	//ALTER TABLE `DataCollection` CHANGE  `title` `label` TEXT
	//-------------------------------------------
	@Column(name="label", columnDefinition = "TEXT")
	private String label;
	
	
	
	// ***** >>Keywords
	@Transient
	private Set<ElementOfList> mainDisciplineList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> subDisciplineList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> mainContentList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> subContentList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> mainMethodList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> subMethodList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> dataCategoryList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> classificationList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> resourceTypeList = new LinkedHashSet<ElementOfList>();
	
	@Transient
	private Set<ElementOfList> reasonDataProtectionList = new LinkedHashSet<ElementOfList>();
	
	@Column(name="contentRemarks", columnDefinition = "TEXT")
	private String contentRemarks;//Inhalt_Erläuterungen
	
	
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//	Bereitstellung Daten
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	@Enumerated(EnumType.STRING)
	@Column(name="wrState")
	private AlternativeWRState wrState;
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//	Erzeugung der Daten
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	//Projekt_abgeschlossen
	@Column(nullable = false, name="projectFinished", columnDefinition = "TINYINT", length = 1)
	private boolean projectFinished;
	
	@Column(name="projectStatus")
	private String projectStatus = ProjectStatus.not_finished.id;
	
	//ALTER TABLE DataCollection DROP COLUMN projectStart;
    @Column(name="projectStart")
    private String projectStart;
	
	//ALTER TABLE DataCollection DROP COLUMN projectEnd;
    @Column(name="projectEnd")
    private String projectEnd;
	
	// ## Datengenerierung ##
	
	//Daten_Datum-Beginn
	//Daten_Datum-Ende
	@Transient
	private Set<TimeSpan> dataGenerationList = new LinkedHashSet<TimeSpan>();
	
	//Datenexport_Export
	@Transient
	private Set<SimpleDate> exportDataDateList = new LinkedHashSet<SimpleDate>();
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//	Rechtliches
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	//Datenschutz
	@Column(nullable = false, name="dataProtection", columnDefinition = "TINYINT", length = 1)
	private boolean dataProtection = false;
	
	@Column(name="basisDataProtection", columnDefinition = "TEXT")
	private String basisDataProtection;	
	
	//13.3 Lizenz 
	
	//Lizenz_Bezeichnung
	//@Column(name="licensor")
	@Transient
	private Person licensor;
	
	//Lizenz_Version
	@Column(name="licenseName")
	private String licenseName;
	
	//Lizenz_URL
	@Column(name="licenseVersion")
	private String licenseVersion;
	
	@Column(name="licenseUrl")
	private String licenseUrl;
	
	//13.4 Zugriffsrechte
	
	@Transient
	private Set<AccessRight> accessRightList = new LinkedHashSet<AccessRight>();
	
	@Column(name="accessability")
	private String accessability = Accessability.open_access.id;
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//	Beschreibung Datensammlung
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	//Sprache
	@Transient
	private Set<Language> collectionLanguageList = new LinkedHashSet<Language>();
	
	//@Column(name="dataDescription", columnDefinition = "TEXT")
	//private String dataDescription;
	
	//Technische Angaben
	//-------------------
	//@Transient
	//private Set<FileFormat> fileFormatList = new LinkedHashSet<FileFormat>();
	
	@Column(name="fileNumber")
	private Integer fileNumber;
	
	@Column(name="memorySize")
	private String memorySize;
	//-------------------
	
	
	@Column(name="custodialHistory", columnDefinition = "TEXT")
	private String custodialHistory;
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	//	Signatur Metadaten
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastChangeMetadata")
    private Date lastChangeMetadata;
	
	
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// Kuratierung & Archivierung
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
	//Protokollierung
	
	//FinalisierungSIP_Datum
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="sipFinalization")
    private Date sipFinalization;
	
	//UnterzeichnungVertrag_Datum
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="signingContract")
    private Date signingContract;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="curationStart")
    private Date curationStart;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="curationEnd")
    private Date curationEnd;
	
	//Bereitstellung_Datum
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="presentationDate")
    private Date presentationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastTest")
    private Date lastTest;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="dateAvailable")
    private Date dateAvailable;
	
	@Transient
	private Set<Time> timeList;
	
	@Override
	public JsonObject toJsonObject(JsonObject json){

		// external & internal identifiers, etc.
		json = super.toJsonObject(json);
		
		//##### Title #####
		json.addProperty("label", this.label);
		json.addProperty("accessability", this.accessability);
		
		json.addProperty("licenseName", this.licenseName);
		json.addProperty("licenseVersion", this.licenseVersion);
		json.addProperty("licenseUrl", this.licenseUrl);
		
		json.add("titleList", TextAttribute.toJsonArray(this.getTitleList()));
		json.add("alternativeTitleList", TextAttribute.toJsonArray(this.getAlternativeTitleList()));
		
		//##### Description #####
		json.add("projectDescriptionList", TextAttribute.toJsonArray(this.getProjectDescriptionList()));
		json.add("dataCollectionDescriptionList", TextAttribute.toJsonArray(this.getDataCollectionDescriptionList()));
		json.add("summaryList", TextAttribute.toJsonArray(this.getSummaryList()));
		json.add("descriptionDataProtectionList", TextAttribute.toJsonArray(this.getDescriptionDataProtectionList()));
		
		//##### Keywords (Schlagworte) #####
		json.add("mainDisciplineList", ElementOfList.toJsonArray(this.mainDisciplineList));
		json.add("subDisciplineList", ElementOfList.toJsonArray(this.subDisciplineList));
		json.add("mainContentList", ElementOfList.toJsonArray(this.mainContentList));
		json.add("subContentList", ElementOfList.toJsonArray(this.subContentList));
		json.add("mainMethodList", ElementOfList.toJsonArray(this.mainMethodList));
		json.add("subMethodList", ElementOfList.toJsonArray(this.subMethodList));
		json.add("dataCategoryList", ElementOfList.toJsonArray(this.dataCategoryList));
		json.add("classificationList", ElementOfList.toJsonArray(this.classificationList));
		json.add("resourceTypeList", ElementOfList.toJsonArray(this.resourceTypeList));
		json.add("reasonDataProtectionList", ElementOfList.toJsonArray(this.reasonDataProtectionList));
		
		json.addProperty("dataProtection", this.dataProtection);
		//json.addProperty("reasonDataProtection", this.reasonDataProtection);
		json.addProperty("basisDataProtection", this.basisDataProtection);
		
		json.add("accessRightList", AccessRight.toJsonArray(this.accessRightList));
		//json.addProperty("dataDescription", this.dataDescription);
		
		
		json.add("dataGenerationList", TimeSpan.toJsonArray(this.dataGenerationList));
		
		//###### Protokollierung ######
		json.addProperty("sipFinalization", formatDate(sipFinalization));
		json.addProperty("signingContract", formatDate(signingContract));
		json.addProperty("curationStart", formatDate(curationStart));
		json.addProperty("curationEnd", formatDate(curationEnd));
		json.addProperty("presentationDate", formatDate(presentationDate));
		json.addProperty("lastTest", formatDate(lastTest));
		
		//###### Actors ######
		if(getActorMap().values() != null) {
			json.add("actorList", Actor.toJsonArray(new HashSet<Actor>(getActorMap().values())));
		}
		// create a list of actors by role
		for(ActorRole.Type type : ActorRole.Type.values()) {
			if(this.getActorList(type) != null && this.getActorList(type).size() > 0)
			json.add(type.id, Actor.toJsonArray(this.getActorList(type)));
		}
		
		return json;
	}
	
	
	public XMLObject toXMLObject() {
		XMLObject dc = new XMLObject("DataCollection");
		
		// #### Identifier ####
		XMLObject identifierSec = new XMLObject("Identifier");
			XMLObject internal = new XMLObject("InternalID");
			internal.addDescendants(Identifier.toXMLObjects(this.getInternalIdList()));
			if(internal.hasDescendants()) identifierSec.addDescendant(internal);
			
			XMLObject external = new XMLObject("ExternalID");
			external.addDescendants(Identifier.toXMLObjects(this.getExternalIdList()));
			if(external.hasDescendants()) identifierSec.addDescendant(external);
		if(identifierSec.hasDescendants()) dc.addDescendant(identifierSec);
		
		// #### Title ####
		XMLObject titleSec = new XMLObject("Title");
			XMLObject mainTitle = new XMLObject("MainTitle");
			mainTitle.addDescendants(TextAttribute.toXMLObjects(this.getTitleList()));
			if(mainTitle.hasDescendants()) titleSec.addDescendant(mainTitle);
			
			XMLObject alternativeTitle = new XMLObject("AlternativeTitle");
			alternativeTitle.addDescendants(TextAttribute.toXMLObjects(this.getAlternativeTitleList()));
			if(alternativeTitle.hasDescendants()) titleSec.addDescendant(alternativeTitle);
		if(titleSec.hasDescendants()) dc.addDescendant(titleSec);
		
		// #### Subject ####
		XMLObject subjectSec = new XMLObject("Subject");
			XMLObject mainDiscipline = new XMLObject("MainDiscipline");
			mainDiscipline.addDescendants(ElementOfList.toXMLObjects(this.getMainDisciplineList()));
			if(mainDiscipline.hasDescendants()) subjectSec.addDescendant(mainDiscipline);
			
			XMLObject subDiscipline = new XMLObject("SubDiscipline");
			subDiscipline.addDescendants(ElementOfList.toXMLObjects(this.getSubDisciplineList()));
			if(subDiscipline.hasDescendants()) subjectSec.addDescendant(subDiscipline);
			
			XMLObject mainContent = new XMLObject("MainContent");
			mainContent.addDescendants(ElementOfList.toXMLObjects(this.getMainContentList()));
			if(mainContent.hasDescendants()) subjectSec.addDescendant(mainContent);
			
			XMLObject subContent = new XMLObject("SubContent");
			subContent.addDescendants(ElementOfList.toXMLObjects(this.getSubContentList()));
			if(subContent.hasDescendants()) subjectSec.addDescendant(subContent);
			
			XMLObject mainMethod = new XMLObject("MainMethod");
			mainMethod.addDescendants(ElementOfList.toXMLObjects(this.getMainMethodList()));
			if(mainMethod.hasDescendants()) subjectSec.addDescendant(mainMethod);
			
			XMLObject subMethod = new XMLObject("SubMethod");
			subMethod.addDescendants(ElementOfList.toXMLObjects(this.getSubMethodList()));
			if(subMethod.hasDescendants()) subjectSec.addDescendant(subMethod);
		if(subjectSec.hasDescendants()) dc.addDescendant(subjectSec);
		
		// #### Description ####
		XMLObject descriptionSec = new XMLObject("Description");
			XMLObject summary = new XMLObject("Summary");
			summary.addDescendants(TextAttribute.toXMLObjects(this.getSummaryList()));
			if(summary.hasDescendants()) descriptionSec.addDescendant(summary);
			
			XMLObject projectDescription = new XMLObject("ProjectDescription");
			projectDescription.addDescendants(TextAttribute.toXMLObjects(this.getProjectDescriptionList()));
			if(projectDescription.hasDescendants()) descriptionSec.addDescendant(projectDescription);
			
			XMLObject collectionDescription = new XMLObject("DataCollectionDescription");
			collectionDescription.addDescendants(TextAttribute.toXMLObjects(this.getDataCollectionDescriptionList()));
			if(collectionDescription.hasDescendants()) descriptionSec.addDescendant(collectionDescription);
			
			XMLObject classification = new XMLObject("Classification");
			classification.addDescendants(ElementOfList.toXMLObjects(this.getClassificationList()));
			if(classification.hasDescendants()) descriptionSec.addDescendant(classification);
		if(descriptionSec.hasDescendants()) dc.addDescendant(descriptionSec);
		
		// #### Coverage ####
		XMLObject coverage = new XMLObject("Coverage");
			XMLObject time = new XMLObject("Time");
			time.addDescendants(Time.toXMLObjects(this.getTimeList()));
			if(time.hasDescendants()) coverage.addDescendant(time);
			
			XMLObject place = new XMLObject("Place");
			place.addDescendants(Place.toXMLObjects(this.getPlaceList()));
			if(place.hasDescendants()) coverage.addDescendant(place);
		if(coverage.hasDescendants()) dc.addDescendant(coverage);
		
		// #### Publications ####
		XMLObject publications = new XMLObject("RelatedPublication");
			publications.addDescendants(Reference.toXMLObjects(this.getReferenceList()));
		if(publications.hasDescendants()) dc.addDescendant(publications);
		
		// #### RelatedResources ####
		XMLObject relatedResources = new XMLObject("RelatedResource");
			relatedResources.addDescendants(RelatedResource.toXMLObjects(this.getRelatedResourceList()));
		if(relatedResources.hasDescendants()) dc.addDescendant(relatedResources);
		
		// #### DataCreation ####
		XMLObject dataCreation = new XMLObject("DataCreation");
			XMLObject projectPeriod = new XMLObject("ProjectPeriod");
				projectPeriod.addAttribute("ProjectStart", this.getProjectStart());
				projectPeriod.addAttribute("ProjectEnd", this.getProjectEnd());
				projectPeriod.addAttribute("ProjectStatus", this.getProjectStatus());
			if(projectPeriod.hasDescendants()) dataCreation.addDescendant(projectPeriod);
				
			XMLObject dataGeneration = new XMLObject("DataGeneration");
				dataGeneration.addDescendants(TimeSpan.toXMLObjects(this.getDataGenerationList()));			
			if(dataGeneration.hasDescendants()) dataCreation.addDescendant(dataGeneration);
		if(dataCreation.hasDescendants()) dc.addDescendant(dataCreation);
		
		// ##### Actor ####
		XMLObject actors = new XMLObject("Actors");
			actors.addDescendants(Actor.toXMLObjects(this.getActorList()));
		if(actors.hasDescendants()) dc.addDescendant(actors);
		
		// #### DataCollectionDescription ####
		XMLObject description = new XMLObject("DataCollectionDescription");
			XMLObject dcLanguage = new XMLObject("CollectionLanguage");
			dcLanguage.addDescendants(Language.toXMLObjects(this.getCollectionLanguageList()));
			if(dcLanguage.hasDescendants()) description.addDescendant(dcLanguage);
			
			XMLObject resourceType = new XMLObject("RessourceType");
			resourceType.addDescendants(ElementOfList.toXMLObjects(this.getResourceTypeList()));
			if(resourceType.hasDescendants()) description.addDescendant(resourceType);
			
			XMLObject dataCategory = new XMLObject("DataCategory");
			dataCategory.addDescendants(ElementOfList.toXMLObjects(this.getDataCategoryList()));
			if(dataCategory.hasDescendants()) description.addDescendant(dataCategory);
			
			if(!empty(this.custodialHistory)) description.addAttribute("CustodialHistory", this.custodialHistory);
			if(this.fileNumber != null) description.addAttribute("FileNumber", this.fileNumber.toString());
			if(!empty(this.memorySize)) description.addAttribute("MemorySize", this.memorySize);
		if(description.hasDescendants()) dc.addDescendant(description);
		
		// #### SignatureMetadata ####
		XMLObject signature = new XMLObject("SignatureMetadata");
			XMLObject mdLanguage = new XMLObject("MetadataLanguage");
			mdLanguage.addDescendants(Language.toXMLObjects(this.getMetadataLanguageList()));
			if(mdLanguage.hasDescendants()) signature.addDescendant(mdLanguage);
			
			XMLObject editor = new XMLObject("MetadataEditor");
			editor.addDescendants(Person.toXMLObjects0(this.getMetadataEditorList()));
			if(editor.hasDescendants()) signature.addDescendant(editor);
			
			String date;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(this.getLastChangeMetadata() != null) {
				date = formatter.format(this.getLastChangeMetadata());
			}else{
				date = formatter.format(this.getLastChange());
			}
			signature.addAttribute("LastChangeMetadata", date);
		if(signature.hasDescendants()) dc.addDescendant(signature);
		
		return dc;
	}
	
	
	/**
	 * This method returns the concatenation of the titles on English and German.
	 * If the some of them or both are empty, the label will be empty.
	 * @return
	 */
	public String getCollectionLabel(){
		TextAttribute titleDeu = getTextAttribute(TextAttribute.ContentType.title, GERMAN_CODE);
		TextAttribute titleEng = getTextAttribute(TextAttribute.ContentType.title, ENGLISH_CODE);
		
		String label = ( titleEng != null && StringUtils.isNotEmpty(titleEng.getValue())) ? titleEng.getValue() : "";
		
		if(StringUtils.isNotEmpty(label) && titleDeu != null && StringUtils.isNotEmpty(titleDeu.getValue())){
			label += " - " + titleDeu.getValue();
		}else if(StringUtils.isEmpty(label) && titleDeu != null && StringUtils.isNotEmpty(titleDeu.getValue())){
			label = titleDeu.getValue();
		}else{
			label = "Not assigned!";
		}
		return label;	
	}
	
	public String getCollectionIdentifier(){
		for(Identifier item : this.getInternalIdList()){
			if(StringUtils.equals(item.getType(), Identifier.Internal.ianus_collno.id) ||
					StringUtils.equals(item.getType(), Identifier.Internal.ianus_mdrecord.id)){
				return item.getValue();
			}
		}
		return "ianus-identifier-empty";
	}
	
	/**
	 * This method returns true only if this data collection contains at least one title either in German or in English.
	 * @return
	 */
	public boolean validateTitle(){
		TextAttribute titleDeu = getTextAttribute(TextAttribute.ContentType.title, GERMAN_CODE);
		TextAttribute titleEng = getTextAttribute(TextAttribute.ContentType.title, ENGLISH_CODE);
		
		if(listIsNull(titleDeu, titleEng)){
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns true only if this data collection contains already the minimal set of metadata defined in the specification
	 * @return
	 */
	public boolean validateInitialMD(){
		
		if(!validateTitle()){
			return false;
		}
		
		//if this DC does not have neither a title in German nor in English, then this DC is not valid.
		TextAttribute summaryDeu = getTextAttribute(TextAttribute.ContentType.summary, GERMAN_CODE);
		TextAttribute summaryEng = getTextAttribute(TextAttribute.ContentType.summary, ENGLISH_CODE);
		if(listIsNull(summaryDeu, summaryEng)){
			return false;
		}

		if(this.getMainDisciplineList().isEmpty() || 
				this.getMainDisciplineList().isEmpty() || 
				/*this.getSubDisciplineList().isEmpty() ||*/
				this.getClassificationList().isEmpty() ||
				this.getDataCategoryList().isEmpty()
				/*this.getExternalIdList().isEmpty()*/){
			return false;
		}

		if(getContactPersonList().isEmpty() || getPrincipalInvestigatorList().isEmpty() || getLicenseHolderList().isEmpty()){
			return false;
		}
				
		return true;
	}
	
	/**
	 * This method returns false only if the input list contains at least one element that is not empty 
	 * @param atts
	 * @return
	 */
	public boolean listIsNull(TextAttribute ... list){
		for(TextAttribute item : list){
			if(StringUtils.isNotEmpty(item.getValue())){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean containsEOL(ElementOfList.ContentType contentType, String listId, String valueId){
		
		Set<ElementOfList> list = getEOLList(contentType);
		
		for(ElementOfList eol : list){
			if(StringUtils.equals(listId, eol.getListId()) && StringUtils.equals(valueId, eol.getValueId())){
				return true;
			}
		}
		
		return false;
	}
	
	public Set<ElementOfList> getEOLList(ElementOfList.ContentType contentType){
		if(contentType.equals(ElementOfList.ContentType.dataCategory)){
			return this.getDataCategoryList();
		}else if(contentType.equals(ElementOfList.ContentType.classification)){
			return this.getClassificationList();
		}else if(contentType.equals(ElementOfList.ContentType.mainDiscipline)){
			return this.getMainDisciplineList();
		}else if(contentType.equals(ElementOfList.ContentType.subDiscipline)){
			return this.getSubDisciplineList();
		}
		
		return null;
	}
	
	
	/*
	public boolean containsDataCategory(String listId, String valueId){
		for(ElementOfList eol : this.dataCategoryList){
			if(StringUtils.equals(listId, eol.getListId()) && StringUtils.equals(valueId, eol.getValueId())){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsClassification(String listId, String valueId){
		for(ElementOfList eol : this.classificationList){
			if(StringUtils.equals(listId, eol.getListId()) && StringUtils.equals(valueId, eol.getValueId())){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsMainDiscipline(String listId, String valueId){
		for(ElementOfList eol : this.mainDisciplineList){
			if(StringUtils.equals(listId, eol.getListId()) && StringUtils.equals(valueId, eol.getValueId())){
				return true;
			}
		}
		return false;
	}

	public boolean containsSubDiscipline(String listId, String valueId){
		for(ElementOfList eol : this.subDisciplineList){
			if(StringUtils.equals(listId, eol.getListId()) && StringUtils.equals(valueId, eol.getValueId())){
				return true;
			}
		}
		return false;
	}
	*/
	public Set<TextAttribute> getTitleList() {
		if(this.attributeMap.get(TextAttribute.ContentType.title) == null){
			this.attributeMap.put(TextAttribute.ContentType.title, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.title);
	}
	
	public Set<TextAttribute> getAlternativeTitleList() {
		if(this.attributeMap.get(TextAttribute.ContentType.alternativeTitle) == null){
			this.attributeMap.put(TextAttribute.ContentType.alternativeTitle, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.alternativeTitle);
	}
	
	public Set<TextAttribute> getDescriptionDataProtectionList() {
		if(this.attributeMap.get(TextAttribute.ContentType.descriptionDataProtection) == null){
			this.attributeMap.put(TextAttribute.ContentType.descriptionDataProtection, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.descriptionDataProtection);
	}
	
	public Set<TextAttribute> getSummaryList() {
		if(this.attributeMap.get(TextAttribute.ContentType.summary) == null){
			this.attributeMap.put(TextAttribute.ContentType.summary, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.summary);
	}

	public Set<TextAttribute> getProjectDescriptionList() {
		if(this.attributeMap.get(TextAttribute.ContentType.projectDescription) == null){
			this.attributeMap.put(TextAttribute.ContentType.projectDescription, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.projectDescription);
	}
	
	public Set<TextAttribute> getDataCollectionDescriptionList() {
		if(this.attributeMap.get(TextAttribute.ContentType.dataCollectionDescription) == null){
			this.attributeMap.put(TextAttribute.ContentType.dataCollectionDescription, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.dataCollectionDescription);
	}
	
	public Set<TextAttribute> getMotivationList() {
		if(this.attributeMap.get(TextAttribute.ContentType.motivation) == null){
			this.attributeMap.put(TextAttribute.ContentType.motivation, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.motivation);
	}
	
	public Set<ElementOfList> getClassificationList() {
		return classificationList;
	}


	public void setClassificationList(Set<ElementOfList> classificationList) {
		this.classificationList = classificationList;
	}	

	public Set<ElementOfList> getResourceTypeList() {
		return resourceTypeList;
	}


	public void setResourceTypeList(Set<ElementOfList> resourceTypeList) {
		this.resourceTypeList = resourceTypeList;
	}


	public Set<ElementOfList> getMainDisciplineList() {
		return mainDisciplineList;
	}


	public void setMainDisciplineList(Set<ElementOfList> mainDisciplineList) {
		this.mainDisciplineList = mainDisciplineList;
	}


	public Set<ElementOfList> getSubDisciplineList() {
		return subDisciplineList;
	}


	public void setSubDisciplineList(Set<ElementOfList> subDisciplineList) {
		this.subDisciplineList = subDisciplineList;
	}


	public Set<ElementOfList> getMainContentList() {
		return mainContentList;
	}


	public void setMainContentList(Set<ElementOfList> mainContentList) {
		this.mainContentList = mainContentList;
	}


	public Set<ElementOfList> getSubContentList() {
		return subContentList;
	}


	public void setSubContentList(Set<ElementOfList> subContentList) {
		this.subContentList = subContentList;
	}


	public Set<ElementOfList> getMainMethodList() {
		return mainMethodList;
	}


	public void setMainMethodList(Set<ElementOfList> mainMethodList) {
		this.mainMethodList = mainMethodList;
	}


	public Set<ElementOfList> getSubMethodList() {
		return subMethodList;
	}


	public void setSubMethodList(Set<ElementOfList> subMethodList) {
		this.subMethodList = subMethodList;
	}

	public static TextAttribute getTextAttribute(Set<TextAttribute> list, String languageCode){
		if(list != null) {
			for(TextAttribute att : list){
				if(StringUtils.equals(att.getLanguageCode(), languageCode)){
					return att;
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>This method should be used for the relations 1..n between a DataCollection and a TextAttribute list, 
	 * where for each contentType only one item per language is permitted. </p>
	 * <p>For example, the attribute title is multilingual, however the user can insert until one title per language.</p>
	 * 
	 * <p>This method returns the unique TextAttribute for a certain contentType (like: title or summary) in a certain language.
	 * If this DataCollection does not have the searching TextAttribute, this method returns a "fresh" TextAttribute. 
	 * It means a TextAttribute that is not persistent.</p>
	 * 
	 * @param contentType
	 * @param languageCode: a language code according to the ISO 639-3-
	 * @return
	 */
	public TextAttribute getTextAttribute(TextAttribute.ContentType contentType, String languageCode) {
		//Elements like title, summary, etc are save in sets in the map this.attributeMap<TextAttribute.ContentType, Set<TextAttribute>>. 
		//Using the contentType, we can get the set that we are looking for. 
		Set<TextAttribute> set = this.attributeMap.get(contentType);
		
		//If the set if not null, we must find in the set the attribute in the searching language.
		return getTextAttribute(set, languageCode);
	}
	
	
	/**
	 * Returns a list of actors (Person or Institution) that has the role "Rights_Holder" for the data collection.
	 * @return
	 */
	public Set<Actor> getLicenseHolderList() {
		return getActorList(ActorRole.Type.dcms_RightsHolder);
	}
	
	public Set<Person> getMetadataEditorList(){
		return getPersonList(ActorRole.Type.ianus_AuthorMetadata);
	}
	
	public Set<Person> getDataCuratorList(){
		return getPersonList(ActorRole.Type.dcms_DataCurator);
	}
	
	public Set<Person> getAuthorList() {
		return getPersonList(ActorRole.Type.ianus_PrincipalInvest);
	}
	
	public Set<Actor> getRightsHolderList() {
		return getActorList(ActorRole.Type.dcms_RightsHolder);
	}
	
	public Set<Actor> getFunderList() {
		return getActorList(ActorRole.Type.dcms_Funder);
	}
	
	public Set<Actor> getContactPersonList() {
		return getActorList(ActorRole.Type.dcms_ContactPerson);
	}
	
	public Set<Actor> getDataHolderList() {
		return getActorList(ActorRole.Type.dcms_HostingInstitution);
	}
	
	public Set<Person> getPrincipalInvestigatorList() {
		return getAuthorList();
	}
	
	public String getAccessability() {
		return accessability;
	}


	public void setAccessability(String accessability) {
		this.accessability = accessability;
	}

	protected Set<Person> getPersonList(ActorRole.Type role){
		Set<Person> list = new LinkedHashSet<Person>();
		for(ActorRole r0 : this.getRoleList()){
			if(role.id.equals(r0.getTypeId())){
				Actor actor = this.getActorMap().get(r0.getActorId());
				if(actor instanceof Person){
					list.add((Person)actor);
				}
			}
		}
		return list;
	}
	
	public Set<Actor> getActorList(ActorRole.Type role){
		Set<Actor> list = new LinkedHashSet<Actor>();
		for(ActorRole r0 : this.getRoleList()){
			if(role.id.equals(r0.getTypeId())){
				Actor actor = this.getActorMap().get(r0.getActorId());
				if(actor != null){
					list.add(actor);
				}
			}
		}
		return list;
	}
	
	// return all actors regardless of their role
	private Set<Actor> getActorList() {
		Set<Actor> list = new LinkedHashSet<Actor>();
		for(ActorRole r0 : this.getRoleList()){
			Actor actor = this.getActorMap().get(r0.getActorId());
			if(actor != null){
				list.add(actor);
			}
		}
		return list;
	}
	
	public Set<TextAttribute> getUrlList() {
		if(this.attributeMap.get(TextAttribute.ContentType.url) == null){
			this.attributeMap.put(TextAttribute.ContentType.url, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.url);
	}

	public String getContentRemarks() {
		return contentRemarks;
	}

	public void setContentRemarks(String contentRemarks) {
		this.contentRemarks = contentRemarks;
	}	

	public AlternativeWRState getWrState() {
		return wrState;
	}

	public void setWrState(AlternativeWRState wrState) {
		this.wrState = wrState;
	}

	public boolean isProjectFinished() {
		return projectFinished;
	}

	public void setProjectFinished(boolean projectFinished) {
		this.projectFinished = projectFinished;
	}	

	public String getProjectStart() {
		return projectStart;
	}

	public void setProjectStart(String projectStart) {
		this.projectStart = projectStart;
	}

	public String getProjectEnd() {
		return projectEnd;
	}

	public void setProjectEnd(String projectEnd) {
		this.projectEnd = projectEnd;
	}

	public Set<SimpleDate> getExportDataDateList() {
		return exportDataDateList;
	}

	public void setExportDataDateList(Set<SimpleDate> exportDataDateList) {
		this.exportDataDateList = exportDataDateList;
	}

	public Person getLicensor() {
		return licensor;
	}

	public void setLicensor(Person licensor) {
		this.licensor = licensor;
	}	

	public String getLicenseName() {
		return licenseName;
	}


	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}


	public String getLicenseVersion() {
		return licenseVersion;
	}

	public void setLicenseVersion(String licenseVersion) {
		this.licenseVersion = licenseVersion;
	}

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public Set<AccessRight> getAccessRightList() {
		return accessRightList;
	}

	public void setAccessRightList(Set<AccessRight> accessRightList) {
		this.accessRightList = accessRightList;
	}	

	public Set<Language> getCollectionLanguageList() {
		return collectionLanguageList;
	}


	public void setCollectionLanguageList(Set<Language> collectionLanguageList) {
		this.collectionLanguageList = collectionLanguageList;
	}

	/*
	public String getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(String dataDescription) {
		this.dataDescription = dataDescription;
	}*/

	public Integer getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Integer fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}
	
	public String getCustodialHistory() {
		return custodialHistory;
	}

	public void setCustodialHistory(String custodialHistory) {
		this.custodialHistory = custodialHistory;
	}


	public Date getSipFinalization() {
		return sipFinalization;
	}

	public void setSipFinalization(Date sipFinalization) {
		this.sipFinalization = sipFinalization;
	}

	public Date getSigningContract() {
		return signingContract;
	}

	public void setSigningContract(Date signingContract) {
		this.signingContract = signingContract;
	}

	public Date getCurationStart() {
		return curationStart;
	}

	public void setCurationStart(Date curationStart) {
		this.curationStart = curationStart;
	}

	public Date getCurationEnd() {
		return curationEnd;
	}

	public void setCurationEnd(Date curationEnd) {
		this.curationEnd = curationEnd;
	}

	public Date getPresentationDate() {
		return presentationDate;
	}

	public void setPresentationDate(Date presentationDate) {
		this.presentationDate = presentationDate;
	}

	public Date getLastTest() {
		return lastTest;
	}

	public void setLastTest(Date lastTest) {
		this.lastTest = lastTest;
	}	
	
	public boolean isDataProtection() {
		return dataProtection;
	}


	public void setDataProtection(boolean dataProtection) {
		this.dataProtection = dataProtection;
	}

	public Set<ElementOfList> getReasonDataProtectionList() {
		return reasonDataProtectionList;
	}


	public void setReasonDataProtectionList(Set<ElementOfList> reasonDataProtectionList) {
		this.reasonDataProtectionList = reasonDataProtectionList;
	}


	public String getBasisDataProtection() {
		return basisDataProtection;
	}


	public void setBasisDataProtection(String basisDataProtection) {
		this.basisDataProtection = basisDataProtection;
	}


	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}


	public void setActorList(Set<Actor> actorList) {
		this.actorMap = new HashMap<Long, Actor>();
		for(Actor actor : actorList){
			this.actorMap.put(actor.getId(), actor);
		}
	}
	
	

	public Date getLastChangeMetadata() {
		return lastChangeMetadata;
	}


	public void setLastChangeMetadata(Date lastChangeMetadata) {
		this.lastChangeMetadata = lastChangeMetadata;
	}


	public Map<Long, Actor> getActorMap() {
		return actorMap;
	}

	public void setActorMap(Map<Long, Actor> actorMap) {
		this.actorMap = actorMap;
	}	

	public Set<TimeSpan> getDataGenerationList() {
		return dataGenerationList;
	}

	public void setDataGenerationList(Set<TimeSpan> dataGenerationList) {
		this.dataGenerationList = dataGenerationList;
	}	
	
	public Set<Time> getTimeList() {
		return timeList;
	}

	public void setTimeList(Set<Time> timeList) {
		this.timeList = timeList;
	}	
	
	public Set<ElementOfList> getDataCategoryList() {
		return dataCategoryList;
	}

	public void setDataCategoryList(Set<ElementOfList> dataCategoryList) {
		this.dataCategoryList = dataCategoryList;
	}

	public Date getDateAvailable() {
		return dateAvailable;
	}


	public void setDateAvailable(Date dateAvailable) {
		this.dateAvailable = dateAvailable;
	}


	
	public enum ReasonDataProtection {
		
		ianus_persdata("ianus_persdata","personal data","Personenbezogene Angaben","",""),
		ianus_protecsites("ianus_protecsites","protection of sites/monuments","Schutz von Fundstellen/Denkmälern","",""),
		ianus_protecch("ianus_protecch","protection of cultural heritage","Schutz von Kulturgütern","",""),
		ianus_protecother("ianus_protecother","other protection issues","Sonstige Schutzinteresse","","")
		;

		public static String LIST_ID = "ianus_data_protection";
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String URL;
		public final String description;

		private ReasonDataProtection(String id, String labelEng, String labelGer, String URL, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.URL = URL;
			this.description = description;
		}
		
		public static String getLabelEng(String valueId){
			for(ReasonDataProtection type : ReasonDataProtection.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(ReasonDataProtection type : ReasonDataProtection.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}
	
	public enum Accessability {
		
		open_access("open_access", "open access", "offen für alle"),
		authorised_groups("authorised_groups", "restricted access: authorised groups", "begrenzter Zugriff: autorisierte Gruppen"),
		authorised_individuals("authorised_individuals", "restricted access: authorised individuals", "begrenzter Zugriff: autorisierte Individuen"),
		not_accessible("not_accessible", "not accessible", "geschlossen"),
		embargo("embargo", "embargo", "Embargo"),
		
		;
		public final String id;
		public final String labelEng;
		public final String labelGer;
		
		private Accessability(String id, String labelEng, String labelGer){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
		}
		
		public static String getLabelEng(String valueId){
			for(Accessability type : Accessability.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(Accessability type : Accessability.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}
	
	public enum ProjectStatus{
		finished("finished", "finished", "abgeschlossen"),
		not_finished("not_finished", "not finished", "nicht abgeschlossen")
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		
		private ProjectStatus(String id, String labelEng, String labelGer){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
		}
		
		public static String getLabelEng(String valueId){
			for(ProjectStatus type : ProjectStatus.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(ProjectStatus type : ProjectStatus.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}
	
	@Override
	public String toString(){
		
		return ToStringBuilder.reflectionToString(this);
		// return "DataCollection [id=" + id + ", presentationDate=" + formatDate(presentationDate) + "]";
		
	}
}
