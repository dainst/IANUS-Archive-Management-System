package de.ianus.ingest.web.pages.metadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.ToggleEvent;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.ApplicationBean;
import de.ianus.ingest.web.pages.IANUSEntityPage;
import de.ianus.ingest.web.pages.OverviewDCPage;
import de.ianus.ingest.web.pages.TransferPage;
import de.ianus.ingest.web.pages.component.ElementOfListComponent;
import de.ianus.ingest.web.pages.component.ManyCheckboxEOL;
import de.ianus.ingest.web.pages.component.TextAttributeComponent;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.FileFormat;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.utils.DisciplineList;
import de.ianus.metadata.utils.ValueListUtils;

/**
 * ## Javaclass (backend functionality) Initial metadata form in transfer workflow
 * 
 * 
 * ### Summary 
 * This class handles the functionality of the initial metadata form.
 * It is a subset of the large metadata form and gives the user the opportunity
 * to add/ save and edit the mandatory fields. 
 * 
 * ### Methods 
 * 	Uploadprocess: 		public void loadDataCollection(Long dcId)
 * 					 	public String actionSave()
 * 						public void listenerAddFileFormat(ActionEvent event)
 * 						public void listenerDeleteFileFormat(ActionEvent event)
 * 
 * ### Links 
 * Camunda: transfer-workflow.bpmn 
 * Confluence: http://confluence:8090/display/IAN/Transfer+Process+Definition 
 * 
 * @author jurzua
 * @author zoes
 * @see InitialMD.xhtml
 * @since 0.1
 * @extends IANUSEntityPage
 *
 * belongs to package de.ianus.ingest.web.pages;
 * TODO: transmit to curator and check mandatory fields
 * 
 */

public class InitialMDPage extends IANUSEntityPage {

	private static final Logger logger = LogManager.getLogger(DataCollectionPage.class); // logger for output in terminal

	public static final String PAGE_NAME = "initialMD"; // name of related page

	private String currentLanguage = DataCollection.GERMAN_CODE; // selected language, initially german
	
	// titles of the datacollection in German and English
	private TextAttribute titleEng;
	private TextAttribute titleGer;
	
	// summaries of the datacollection in German and English
	private TextAttribute summaryEng;
	private TextAttribute summaryGer;
	
	private Set<Actor> actorList = new LinkedHashSet<Actor>();

	private Person contactPerson;
	private Person principalInvestigatorPerson;
	private Person rightsHolderPerson;
	private Institution rightsHolderInstitution;

	//Checkbox input
	private ManyCheckboxEOL dataCategoryCheckbox;
	private ManyCheckboxEOL classificationCheckbox;
	private ManyCheckboxEOL mainDisciplineCheckbox;
	private ManyCheckboxEOL subDisciplineCheckbox;
	
	private Actor actor;
	private ActorRole role;
	private Set<ActorRole> roleList;
	
	protected FileFormat.Type fileFormatType; // the selected file format
	
	//private ElementOfListComponent discipline;

	private static String NO_CHECKED = "no checked";
	private static String OK = "ok";
	private static String REVISION = "revision";
	private Map<String, String> validationMap;
	
	/**
	 * This method is used to load a DataCollection from a InformationPackage Page.
	 * One parameters of this method is IP. With the IP parameter, this page can generate the "Back to ..." button.
	 * @param ip
	 * @param dcBase
	 * @param source
	 */
	public void loadDataCollection(WorkflowIP ip, Long dcId){
		this.ip = ip;
		this.loadDataCollection(dcId);
	}
	
	/**
	 * Loading the form's content on page (re)load. Sets defaults if form is empty.
	 *
	 * @param Long dcId the ID of the datacollection
	 * @see TextAttributeComponent.java
	 * @see ElementOfListComponent.java
	 * @throws Exception
	 */
	public void loadDataCollection(Long dcId) {
		try {
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(dcId);
			this.load(dc, dc);
			
			this.titleEng = getTextAttribute(TextAttribute.ContentType.title, DataCollection.ENGLISH_CODE);
			this.titleGer = getTextAttribute(TextAttribute.ContentType.title, DataCollection.GERMAN_CODE);
			
			this.summaryEng = getTextAttribute(TextAttribute.ContentType.summary, DataCollection.ENGLISH_CODE);
			this.summaryGer = getTextAttribute(TextAttribute.ContentType.summary, DataCollection.GERMAN_CODE);
			
			this.subDisciplineId = ApplicationBean.NONE_SELECTED_ID;
			this.mainDisciplineId = ApplicationBean.NONE_SELECTED_ID;
			this.classificationId = ApplicationBean.NONE_SELECTED_ID;

			this.contactPerson = new Person();
			this.principalInvestigatorPerson = new Person ();
			this.rightsHolderPerson = new Person ();
			this.rightsHolderInstitution = new Institution();
			
			this.role = new ActorRole();
			
			this.validationMap = new HashMap<String, String>();
			this.validationMap.put("title", NO_CHECKED);
			this.validationMap.put("summary", NO_CHECKED);
			this.validationMap.put("mainDiscipline", NO_CHECKED);
			this.validationMap.put("subDiscipline", NO_CHECKED);
			this.validationMap.put("classification", NO_CHECKED);
			this.validationMap.put(ElementOfList.ContentType.dataCategory.toString(), NO_CHECKED);
			this.validationMap.put("externalId", NO_CHECKED);
			this.validationMap.put("contactPerson", NO_CHECKED);
			this.validationMap.put("principalInvestigatorPerson", NO_CHECKED);
			this.validationMap.put("rightsHolder", NO_CHECKED);
			
			this.dataCategoryCheckbox = new ManyCheckboxEOL(dc, ValueListUtils.Names.ianus_data_category.id, ElementOfList.ContentType.dataCategory);
			this.classificationCheckbox = new ManyCheckboxEOL(dc, ValueListUtils.Names.ianus_classification.id, ElementOfList.ContentType.classification); 
			this.mainDisciplineCheckbox = new ManyCheckboxEOL(dc, DisciplineList.LIST_ID_MAIN, ElementOfList.ContentType.mainDiscipline);
			this.subDisciplineCheckbox = new ManyCheckboxEOL(dc, DisciplineList.LIST_ID_SUB, ElementOfList.ContentType.subDiscipline); 
			this.load0();
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	private TextAttribute getTextAttribute(TextAttribute.ContentType contentType, String langCode){
		TextAttribute textAtt = getDc().getTextAttribute(contentType, langCode);
		if(textAtt == null){
			textAtt = new TextAttribute(contentType, langCode);
		}
		return textAtt;
	}
	/*
	private  void loadDataCategory(){
		this.dataCategoryList = new ArrayList<String>();
		for(ElementOfList eol : getDc().getDataCategoryList()){
			this.dataCategoryList.add(eol.getValueId());
		}
	}
	
	private void loadMainDiscipline(){
		this.mainDisciplineList = new ArrayList<String>();
		for(ElementOfList eol : getDc().getMainDisciplineList()){
			this.mainDisciplineList.add(eol.getValueId());
		}
	}
	
	private void loadSubDiscipline(){
		this.subDisciplineList = new ArrayList<String>();
		for(ElementOfList eol : getDc().getSubDisciplineList()){
			this.subDisciplineList.add(eol.getValueId());
		}
	}
	
	private void loadClassifcation(){
		this.classificationList = new ArrayList<String>();
		for(ElementOfList eol : getDc().getClassificationList()){
			this.classificationList.add(eol.getValueId());
		}
	}*/
	
	//NEW
	private void load0() throws Exception{
		if(actor != null && actor.isPersistent()){
			this.roleList = Services.getInstance().getMDService().getRoleList4Actor(actor);
		}
		this.actorList = Services.getInstance().getMDService().getActorList(getDcBase());
		this.role = (this.role == null) ? new ActorRole() : role;
		if(actor instanceof Institution){
			//this.email = new TextAttributeComponent(this.actor, TextAttribute.ContentType.email, PAGE_NAME, false, false);
			//this.telephone = new TextAttributeComponent(this.actor, TextAttribute.ContentType.telephone, PAGE_NAME, false, false);
			//this.url = new TextAttributeComponent(this.actor, TextAttribute.ContentType.url, PAGE_NAME, false, false);
			//this.discipline = null;
		}else if(actor instanceof Person){
			//this.email = null;
			//this.telephone = null;
			//this.url = null;
			//this.discipline = new ElementOfListComponent(actor, ElementOfList.ContentType.discipline);
		} 
	}
	
	/**
	 * Save the form's content on click on button "save".
	 *
	 * @see TextAttributeComponent.java
	 * @throws Exception
	 * @return PAGE_NAME
	 */
	public String actionSave() {
		if(this.validateTitle()){
			try {
				this.saveTitle();
				this.saveSummary();
				//this.classification.saveTextAttribute(this.classification);
				
				this.dataCategoryCheckbox.save();
				this.classificationCheckbox.save();
				this.mainDisciplineCheckbox.save();
				this.subDisciplineCheckbox.save();
				//this.saveDataCategory();
				//this.saveMainDiscipline();
				//this.saveSubDiscipline();
				//this.saveClassification();
				Services.getInstance().getMDService().saveDataCollection(this.getDc());
				
				ip.setCollectionLabel(this.getDc().getCollectionLabel());
				ip.setCollectionIdentifier(this.getDc().getCollectionIdentifier());
				
				Services.getInstance().getDaoService().saveDBEntry(ip);
				
				this.loadDataCollection(this.ip, this.getDc().getId());
				this.validate();
				
				getSession().getOverviewDCPage().load();
				
				//addMsg("Data collection saved = " + this.getDc().getId());
				addMsg(this.getText("saveCollection"));
			} catch (Exception e) {
				addInternalError(e);
			}	
		}else{
			addMsg("The attribute 'title' is mandatory.");
		}
		return PAGE_NAME;
	}
	
	private void saveTitle() throws Exception{
		if(StringUtils.isNotEmpty(this.titleEng.getValue())){
			this.titleEng.setSource(getDc());
			this.titleEng.setContentType(TextAttribute.ContentType.title);
			this.titleEng.setLanguageCode(DataCollection.ENGLISH_CODE);
			Services.getInstance().getMDService().saveEntry(this.titleEng);
			
			getDc().getTitleList().add(titleEng);
		}
		
		if(StringUtils.isNotEmpty(this.titleGer.getValue())){
			this.titleGer.setSource(getDc());
			this.titleGer.setContentType(TextAttribute.ContentType.title);
			this.titleGer.setLanguageCode(DataCollection.GERMAN_CODE);
			Services.getInstance().getMDService().saveEntry(this.titleGer);
			getDc().getTitleList().add(titleGer);
		}
	}
	
	private void saveSummary() throws Exception{
		if(StringUtils.isNotEmpty(this.summaryEng.getValue()))
		{
		this.summaryEng.setSource(getDc());
		this.summaryEng.setContentType(TextAttribute.ContentType.summary);
		this.summaryEng.setLanguageCode(DataCollection.ENGLISH_CODE);
		Services.getInstance().getMDService().saveEntry(this.summaryEng);
		getDc().getSummaryList().add(summaryEng);
		
		}
		if(StringUtils.isNotEmpty(this.summaryGer.getValue()))
		{
		this.summaryGer.setSource(getDc());
		this.summaryGer.setContentType(TextAttribute.ContentType.summary);
		this.summaryGer.setLanguageCode(DataCollection.GERMAN_CODE);
		Services.getInstance().getMDService().saveEntry(this.summaryGer);
		getDc().getSummaryList().add(summaryGer);
		}
		
		
	}
	
	private boolean validateTitle(){
		return StringUtils.isNotEmpty(this.titleEng.getValue()) || StringUtils.isNotEmpty(this.titleGer.getValue());
	}
	
	private boolean validateSummary(){
		return StringUtils.isNotEmpty(this.summaryEng.getValue()) || StringUtils.isNotEmpty(this.summaryGer.getValue());
	}
	
	/**
	 * This method deletes the data collection and the associated transfer package if the title of the DC is empty.
	 * In other case, the page "transferPage" is loaded with the current IP and DC.
	 * @return
	 */
	public String actionCancel(){
		try {
			if(this.validateTitle()){
				getSession().getTransferPage().load(this.ip);
				return TransferPage.PAGE_NAME;
			}else{
				TransferP tp = (TransferP)this.ip;
				DataCollection dc = Services.getInstance().getMDService().getDataCollection(tp.getMetadataId()); 
				Services.getInstance().getDaoService().deleteIP(tp);
				try {
					Services.getInstance().getMDService().deleteDataCollection(dc);
					getSession().getOverviewDCPage().load();
				} catch (Exception ex) {
					addInternalError(ex);
				}
				return OverviewDCPage.PAGE_NAME;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PAGE_NAME;
	}
	
	
	private void validate(){
		this.validationMap.put("title", (!validateTitle()) ? REVISION : OK);
		this.validationMap.put("summary", (!validateSummary()) ? REVISION : OK);
		
		this.validationMap.put("mainDiscipline", (this.getDc().getMainDisciplineList().isEmpty() ? REVISION : OK));
		//this.validationMap.put("subDiscipline", (this.getDc().getSubDisciplineList().isEmpty() ? REVISION : OK));
		this.validationMap.put("classification", (this.getDc().getClassificationList().isEmpty() ? REVISION : OK));
		
		this.validationMap.put(ElementOfList.ContentType.dataCategory.toString(), (this.getDc().getDataCategoryList().isEmpty() ? REVISION : OK));
		
		//externalId is not mandatory
		//this.validationMap.put("externalId", (getDc().getExternalIdList().isEmpty() ? REVISION : OK));
		
		this.validationMap.put("contactPerson", (getDc().getContactPersonList().isEmpty() ? REVISION : OK));
		this.validationMap.put("principalInvestigatorPerson", (getDc().getPrincipalInvestigatorList().isEmpty() ? REVISION : OK));
		this.validationMap.put("rightsHolder", (getDc().getLicenseHolderList().isEmpty() ? REVISION : OK));
	}
	
	/**
	 * This method returns the color
	 * @param attName
	 * @return
	 * @throws Exception 
	 */
	public String getValidationColor(String attName) throws Exception{
		if(this.validationMap.containsKey(attName)){
			if(this.validationMap.get(attName).equals(OK)){			
				return "green";
			} else if (this.validationMap.get(attName).equals(REVISION)){
				return "red";
			} else if (this.validationMap.get(attName).equals(NO_CHECKED)){
				return null;
			}	
		}else{
			throw new Exception("The validation map does not contain the value = '" + attName + "'");
		}
		return null;
	}
	
	public void listenerAddContactPerson(ActionEvent event){
		try {
			
			//this.contactPerson.setDcId(this.getDcBase().getId());
			this.contactPerson.setSource(this.getDcBase());
			Services.getInstance().getMDService().saveEntry(contactPerson);
			this.getDc().getActorMap().put(this.contactPerson.getId(), this.contactPerson);
			
			if(getDc().actorHasRole(ActorRole.Type.dcms_ContactPerson, this.contactPerson)){
				ActorRole role = new ActorRole();
				role.setActor(this.contactPerson);
				role.setSource(this.getSource());
				role.setTypeId(ActorRole.Type.dcms_ContactPerson.id);
				Services.getInstance().getMDService().saveEntry(role);
				this.getDc().getRoleList().add(role);
			}
			
			//reset the variable person
			this.contactPerson = new Person();
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerAddPrincipalInvestigatorPerson(ActionEvent event){
		try {
			
			//this.principalInvestigatorPerson.setDcId(this.getDcBase().getId());
			this.principalInvestigatorPerson.setSource(this.getDcBase());
			Services.getInstance().getMDService().saveEntry(principalInvestigatorPerson);
			this.getDc().getActorMap().put(this.principalInvestigatorPerson.getId(), this.principalInvestigatorPerson);
			
			if(getDc().actorHasRole(ActorRole.Type.ianus_PrincipalInvest, this.principalInvestigatorPerson)){
				ActorRole role = new ActorRole();
				role.setActor(this.principalInvestigatorPerson);
				role.setSource(this.getSource());
				role.setTypeId(ActorRole.Type.ianus_PrincipalInvest.id);
				Services.getInstance().getMDService().saveEntry(role);
				this.getDc().getRoleList().add(role);
			}
			
			//reset the variable person
			this.principalInvestigatorPerson = new Person();
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerEditContactPerson(ActionEvent event){
		try {
			this.contactPerson = (Person)getRequestBean("contactPerson");
			System.out.println(contactPerson);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
    public void toggleResetContactPerson(ToggleEvent event) {
    	this.contactPerson = new Person();
    }
	

    

	/**
	 * Remove a contact person from the data collection.
	 *
	 * @param ActionEvent event is fired onclick
	 * @see FileFormat.java
	 * @throws Exception
	 */
	
	public void listenerDeleteContactPerson(ActionEvent event){
		try {
			Person contactPerson0 = (Person)getRequestBean("contactPerson");
			
			for(ActorRole role : this.getDcBase().getRoleList(contactPerson0)){
				this.getDcBase().getRoleList().remove(role);
			}
			
			Services.getInstance().getMDService().deleteActor(contactPerson0);
			this.getDcBase().getActorMap().remove(contactPerson0.getId());
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerEditPrincipalInvestigatorPerson(ActionEvent event){
		try {
			this.principalInvestigatorPerson = (Person)getRequestBean("principalInvestigatorPerson");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
    public void toggleResetprincipalInvestigatorPerson(ToggleEvent event) {
    	logger.info("***********************");
    	this.principalInvestigatorPerson = new Person();
    }
	

    

	/**
	 * Remove a contact person from the data collection.
	 *
	 * @param ActionEvent event is fired onclick
	 * @see FileFormat.java
	 * @throws Exception
	 */
	
	public void listenerDeletePrincipalInvestigatorPerson(ActionEvent event){
		try {
			Person principalInvestigatorPerson0 = (Person)getRequestBean("principalInvestigatorPerson");
			
			for(ActorRole role : this.getDcBase().getRoleList(principalInvestigatorPerson0)){
				this.getDcBase().getRoleList().remove(role);
			}
			
			Services.getInstance().getMDService().deleteActor(principalInvestigatorPerson0);
			this.getDcBase().getActorMap().remove(principalInvestigatorPerson0.getId());
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	
	public void listenerAddRightsHolderPerson(ActionEvent event){
		try {
			
			//this.rightsHolderPerson.setDcId(this.getDcBase().getId());
			this.rightsHolderPerson.setSource(this.getDcBase());
			Services.getInstance().getMDService().saveEntry(rightsHolderPerson);
			this.getDc().getActorMap().put(this.rightsHolderPerson.getId(), this.rightsHolderPerson);
			
			if(getDc().actorHasRole(ActorRole.Type.dcms_RightsHolder, this.rightsHolderPerson)){
				ActorRole role = new ActorRole();
				role.setActor(this.rightsHolderPerson);
				role.setSource(this.getSource());
				role.setTypeId(ActorRole.Type.dcms_RightsHolder.id);
				Services.getInstance().getMDService().saveEntry(role);
				this.getDc().getRoleList().add(role);
			}
			
			//reset the variable person
			this.rightsHolderPerson = new Person();
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerEditRightsHolderPerson(ActionEvent event){
		try {
			
			Actor actor = (Actor)getRequestBean("rightsHolder");
			
			if(actor instanceof Person){
				this.rightsHolderPerson = (Person)actor;
				//this.rightsholderInstitution = null;
			}else if(actor instanceof Institution){
				this.rightsHolderInstitution = (Institution)actor;
				//this.rightsHolderPerson = null;
			}
			
		
			
			

		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
    public void toggleResetRightsHolderPerson(ToggleEvent event) {
    	logger.info("+++++++++++++++++++++++++++++++");
    	this.rightsHolderPerson = new Person();
    }    

	/**
	 * Remove a contact person from the data collection.
	 *
	 * @param ActionEvent event is fired onclick
	 * @see FileFormat.java
	 * @throws Exception
	 */
	
	public void listenerDeleteRightsHolderPerson(ActionEvent event){
		try {
			Actor actor = (Actor)getRequestBean("rightsHolder");
			
			for(ActorRole role : this.getDcBase().getRoleList(actor)){
				this.getDcBase().getRoleList().remove(role);
			}
			
			Services.getInstance().getMDService().deleteActor(actor);
			this.getDcBase().getActorMap().remove(actor.getId());
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerAddRightsHolderInstitution(ActionEvent event){
		try {
			
			//this.rightsHolderInstitution.setDcId(this.getDcBase().getId());
			this.rightsHolderInstitution.setSource(this.getDcBase());
			Services.getInstance().getMDService().saveEntry(rightsHolderInstitution);
			this.getDc().getActorMap().put(this.rightsHolderInstitution.getId(), this.rightsHolderInstitution);
			
			if(getDc().actorHasRole(ActorRole.Type.dcms_RightsHolder, this.rightsHolderInstitution)){
				ActorRole role = new ActorRole();
				role.setActor(this.rightsHolderInstitution);
				role.setSource(this.getSource());
				role.setTypeId(ActorRole.Type.dcms_RightsHolder.id);
				Services.getInstance().getMDService().saveEntry(role);
				this.getDc().getRoleList().add(role);
			}
			
			//reset the variable person
			this.rightsHolderInstitution = new Institution();
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerEditRightsHolderInstitution(ActionEvent event){
		try {
			this.rightsHolderInstitution = (Institution)getRequestBean("rightsHolderInstitution");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
    public void toggleResetRightsHolderInstitution(ToggleEvent event) {
    	this.rightsHolderInstitution = new Institution();
    }
	

    

	/**
	 * Remove a contact person from the data collection.
	 *
	 * @param ActionEvent event is fired onclick
	 * @see FileFormat.java
	 * @throws Exception
	 */
	
	public void listenerDeleteRightsHolderInstitution(ActionEvent event){
		try {
			Institution rightsholderInstitution0 = (Institution)getRequestBean("rightsholderInstitution");
			
			for(ActorRole role : this.getDcBase().getRoleList(rightsholderInstitution0)){
				this.getDcBase().getRoleList().remove(role);
			}
			
			Services.getInstance().getMDService().deleteActor(rightsholderInstitution0);
			this.getDcBase().getActorMap().remove(rightsholderInstitution0.getId());
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public boolean isCurrentLanguageEnglish(){
		return StringUtils.equals(this.currentLanguage, DataCollection.ENGLISH_CODE);
	}
	
	public boolean isCurrentLanguageGerman(){
		return StringUtils.equals(this.currentLanguage, DataCollection.GERMAN_CODE);
	}
	
	public String getCurrentLanguage() {
		return this.currentLanguage;
	}

	public void setCurrentLanguage(String language) {
		this.currentLanguage = language;
	}
	
	public FileFormat.Type getFileFormatType() {
		return fileFormatType;
	}

	public void setFileFormatType(FileFormat.Type fileFormatType) {
		this.fileFormatType = fileFormatType;
	}

	public String actionDeleteRightsHolder() {
		this.deleteRoles4Actor(ActorRole.Type.dcms_RightsHolder);
		return PAGE_NAME;
	}

	public String actionDeleteContactPerson() {
		this.deleteRoles4Actor(ActorRole.Type.dcms_ContactPerson);
		return PAGE_NAME;
	}
	
	public String actionDeleteprincipalInvestigatorPerson() {
		this.deleteRoles4Actor(ActorRole.Type.ianus_PrincipalInvest);
		return PAGE_NAME;
	}
	
	public String actionDeleteRightsHolderPerson() {
		this.deleteRoles4Actor(ActorRole.Type.dcms_RightsHolder);
		return PAGE_NAME;
	}
	
	public String actionDeleteRightsHolderInstitution() {
		this.deleteRoles4Actor(ActorRole.Type.dcms_RightsHolder);
		return PAGE_NAME;
	}
		
	public String listenerSetLanguage(ValueChangeEvent event) {
		this.addMsg("Vor jedem Wechsel der Sprache müssen die aktuellen Eingaben gespeichert werden.");
		this.currentLanguage = (String) event.getNewValue();
		this.loadDataCollection(this.getDc().getId());
		return PAGE_NAME;
	}
	
	public String listenerSetLanguage() {
		//this.currentLanguage = (String) event.getNewValue();
		//System.out.println(this.currentLanguage);
		this.loadDataCollection(this.getDc().getId());
		return PAGE_NAME;
	}
	
	/**
	 * Get the label in the right language.
	 */
	
	public String getText(String key ){
	String r = key + "." + currentLanguage;	
	ResourceBundle rb = ResourceBundle.getBundle("initialMDLabels");
	return rb.getString(r);
	}
	
	/**
	 * Change the labels after choosing another language.
	 */
	public void change(ValueChangeEvent event) throws IOException {
	    FacesContext.getCurrentInstance().getExternalContext().redirect(PAGE_NAME);
	}
	
	public List<SelectItem> getMainDisciplineSuggestionList(){
		if(StringUtils.equals(this.currentLanguage, DataCollection.GERMAN_CODE)){
			return getAppBean().getMainDisciplineGerList();
		}else if(StringUtils.equals(this.currentLanguage, DataCollection.ENGLISH_CODE)){
			return getAppBean().getMainDisciplineEngList();
		}
		return null;
	}
	
	public List<SelectItem> getSubDisciplineSuggestionList(){
		if(StringUtils.equals(this.currentLanguage, DataCollection.GERMAN_CODE)){
			return getAppBean().getSubDisciplineGerList();
		}else if(StringUtils.equals(this.currentLanguage, DataCollection.ENGLISH_CODE)){
			return getAppBean().getSubDisciplineEngList();
		}
		return null;
	}
	
	public List<SelectItem> getSubDisciplineSuggestionListAsGroups(){
		if(StringUtils.equals(this.currentLanguage, DataCollection.GERMAN_CODE)){
			return getAppBean().getSubDisciplineGerList();
		}else if(StringUtils.equals(this.currentLanguage, DataCollection.ENGLISH_CODE)){
			return getAppBean().getSubDisciplineEngList();
		}
		return null;
	}
	
	public List<SelectItem> getDataCategorySuggestionList(){
		if(StringUtils.equals(this.currentLanguage, DataCollection.GERMAN_CODE)){
			return getAppBean().getDataCategoryGerList();
		}else if(StringUtils.equals(this.currentLanguage, DataCollection.ENGLISH_CODE)){
			return getAppBean().getDataCategoryEngList();
		}
		return null;
	}
	
	public List<SelectItem> getClassificationSuggestionList(){
		if(StringUtils.equals(this.currentLanguage, DataCollection.GERMAN_CODE)){
			return getAppBean().getClassificationGerList();
		}else if(StringUtils.equals(this.currentLanguage, DataCollection.ENGLISH_CODE)){
			return getAppBean().getClassificationEngList();
		}
		return null;
	}
	
	//NEW
	public Set<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(Set<Actor> actorList) {
		this.actorList = actorList;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public ActorRole getRole() {
		return role;
	}

	public void setRole(ActorRole role) {
		this.role = role;
	}

	public Set<ActorRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<ActorRole> roleList) {
		this.roleList = roleList;
	}
	
	public Person getPerson(){
		return (this.actor instanceof Person) ? (Person)actor : null;
	}
	
	public Institution getInstitution(){
		return (this.actor instanceof Institution) ? (Institution)actor : null;
	}

	public Person getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(Person contactPerson) {
		this.contactPerson = contactPerson;
	}	

	public Person getPrincipalInvestigatorPerson() {
		return principalInvestigatorPerson;
	}

	public void setPrincipalInvestigatorPerson(Person principalInvestigatorPerson) {
		this.principalInvestigatorPerson = principalInvestigatorPerson;
	}

	public Person getRightsHolderPerson() {
		return rightsHolderPerson;
	}

	public void setRightsHolderPerson(Person rightsHolderPerson) {
		this.rightsHolderPerson = rightsHolderPerson;
	}

	public Institution getRightsHolderInstitution() {
		return rightsHolderInstitution;
	}

	public void setRightsHolderInstitution(Institution rightsHolderInstitution) {
		this.rightsHolderInstitution = rightsHolderInstitution;
	}	

	public ManyCheckboxEOL getDataCategoryCheckbox() {
		return dataCategoryCheckbox;
	}

	public void setDataCategoryCheckbox(ManyCheckboxEOL dataCategoryCheckbox) {
		this.dataCategoryCheckbox = dataCategoryCheckbox;
	}

	public TextAttribute getTitleEng() {
		return titleEng;
	}

	public void setTitleEng(TextAttribute titleEng) {
		this.titleEng = titleEng;
	}

	public TextAttribute getTitleGer() {
		return titleGer;
	}

	public void setTitleGer(TextAttribute titleGer) {
		this.titleGer = titleGer;
	}

	public TextAttribute getSummaryEng() {
		return summaryEng;
	}

	public void setSummaryEng(TextAttribute summaryEng) {
		this.summaryEng = summaryEng;
	}

	public TextAttribute getSummaryGer() {
		return summaryGer;
	}

	public void setSummaryGer(TextAttribute summaryGer) {
		this.summaryGer = summaryGer;
	}
	
	public ManyCheckboxEOL getMainDisciplineCheckbox() {
		return mainDisciplineCheckbox;
	}

	public void setMainDisciplineCheckbox(ManyCheckboxEOL mainDisciplineCheckbox) {
		this.mainDisciplineCheckbox = mainDisciplineCheckbox;
	}

	public ManyCheckboxEOL getSubDisciplineCheckbox() {
		return subDisciplineCheckbox;
	}

	public void setSubDisciplineCheckbox(ManyCheckboxEOL subDisciplineCheckbox) {
		this.subDisciplineCheckbox = subDisciplineCheckbox;
	}

	public ManyCheckboxEOL getClassificationCheckbox() {
		return classificationCheckbox;
	}

	public void setClassificationCheckbox(ManyCheckboxEOL classificationCheckbox) {
		this.classificationCheckbox = classificationCheckbox;
	}
	
}