package de.ianus.ingest.web.pages.metadata;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.ApplicationBean;
import de.ianus.ingest.web.pages.CollectionFilePage;
import de.ianus.ingest.web.pages.IANUSEntityPage;
import de.ianus.ingest.web.pages.TimePage;
import de.ianus.ingest.web.pages.component.ElementOfListComponent;
import de.ianus.ingest.web.pages.component.TextAttributeComponent;
import de.ianus.metadata.bo.AccessRight;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.utils.Classification;
import de.ianus.metadata.bo.utils.DataCategory;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.Language;
import de.ianus.metadata.bo.utils.Resource;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.Time;
import de.ianus.metadata.bo.utils.TimeSpan;
import de.ianus.metadata.utils.AbstractValueList;
import de.ianus.metadata.utils.DisciplineList;
import de.ianus.metadata.utils.ISOLanguage;
import de.ianus.metadata.utils.LanguageList;
import de.ianus.metadata.utils.MainContentAdexTypeList;
import de.ianus.metadata.utils.MethodList;
import de.ianus.metadata.utils.ValueListUtils;

/**
 * <p>
 * This class contains the objects and the methods that are used in the edition of a DataCollection in the form dataCollection.xhtml
 * </p>
 *  
 * @author jurzua
 *
 */
public class DataCollectionPage extends IANUSEntityPage{
	
	private static final Logger logger = LogManager.getLogger(DataCollectionPage.class);
	
	public static final String PAGE_NAME = "dataCollection";
	
	//the list of all data collection available for the current servlet-session
	//private List<DataCollection> dcList;
	
	//the data collection selected by the user
	//private DataCollection dc;
	
	private TextAttributeComponent title;
	private TextAttributeComponent summary; 
	
	private TextAttributeComponent alternativeTitle;
	private TextAttributeComponent projectDescription;
	private TextAttributeComponent dataCollectionDescription;
	private TextAttributeComponent descriptionDataProtection;
	private TextAttributeComponent motivation;
	
	private String collectionLanguageName;
	private AccessRight accessRight = new AccessRight();
	private TimeSpan dataGeneration;
	private Time time;
	
	//In this variable, we will save temporally the main discipline selected by the user from selectOneMenu
	private ElementOfListComponent mainDiscipline;
	private ElementOfListComponent subDiscipline;
	//private ElementOfListComponent mainContent;
	//private ElementOfListComponent subContent;
	//private ElementOfListComponent mainMethod;
	//private ElementOfListComponent subMethod;
	private String resourceTypeId;
	private String reasonDataProtectionId;
	
	private String subMethodId;
	private String subMethodListNameId;
	private String subMethodValue;
	private String subMethodValueId;
	
	private String mainContentId;
	private String mainContentListNameId;
	private String mainContentValue;
	private String mainContentValueId;
	
	private String subContentId;
	private String subContentListNameId;
	private String subContentValue;
	private String subContentValueId;
	
	
	public String getIPLabel(){
		if(this.ip != null){
			if(ip instanceof TransferP){
				return "Transfer Package";
			}else if(ip instanceof SubmissionIP){
				return "Submission Information Package";
			}else if(ip instanceof DisseminationIP){
				return "Dissemination Information Package";
			}else if(ip instanceof ArchivalIP){
				return "Archival Information Package";
			}
		}
		return "null";
	}
	
	private Set<Actor> actorList = new LinkedHashSet<Actor>();
	
	public void listenerDeleteActor(ActionEvent event){
		try {
			Actor actor0 = (Actor)getRequestBean("actor");
			Services.getInstance().getMDService().deleteActor(actor0);;
			this.getDc().getActorMap().remove(actor0.getId());
			this.actorList.remove(actor0);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionEditActor(){
		try {
			getSession().getActorPage().load(this.getDc(), this.getSource(), (Actor)getRequestBean("actor"));
		} catch (Exception e) {
			addInternalError(e);
		}
		return ActorPage.PAGE_NAME;
	}
	
	public String actionAddLogo() {
		ActorLogoPage logoPage = getSession().getActorLogoPage();
		try {
			logoPage.load(this.getDc(), this.getSource(), (Actor)getRequestBean("actor"));
		} catch (Exception e) {
			addInternalError(e);
		}
		// check if one of the default logos can be used
		if(logoPage.getDefaultLogoAvailable()) {
			// copy over the default logo to the right place
			//logoPage.getDefaultLogo().
		}
		
		return ActorLogoPage.PAGE_NAME;
	}
	
	//*******************************
	
	public String actionEditTime(){
		try {
			Time time = (Time)getRequestBean("time");
			getSession().getTimePage().load(this.getDcBase(), this.getSource(), time);
		} catch (Exception e) {
			addInternalError(e);
		}
		return TimePage.PAGE_NAME;
	}
	
	public void listenerDeleteTime(ActionEvent event){
		try {
			Time time = (Time)getRequestBean("time");
			Services.getInstance().getMDService().deleteDBEntry(time);
			if(time.getMainPeriod() != null){
				Services.getInstance().getMDService().deleteDBEntry(time.getMainPeriod());
			}
			if(time.getSubPeriod() != null){
				Services.getInstance().getMDService().deleteDBEntry(time.getSubPeriod());
			}
			this.load(getDcBase(), getSource());
		} catch (Exception e) {
			addInternalError(e);
		}
	}	
		
	public String actionSaveTitle(){
		String page = this.title.actionAdd();
		try {
			//this method update the collection label in the related IP.
			this.getSession().updateCollectionTitelInIP(getDc());
		} catch (Exception e) {
			addInternalError(e);
		}
		return page;
	}
		
	public String actionAddTime(){
		try {
			getSession().getTimePage().load(this.getDcBase(), this.getSource(), new Time());
		} catch (Exception e) {
			addInternalError(e);
		}
		return TimePage.PAGE_NAME;
	}
	
	//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	//	[ SUB METHODE
	public void listenerSaveSubMethod(ActionEvent event){
		if(isSubMethodFreeText() || (StringUtils.isNotEmpty(this.subMethodId) && !AbstractValueList.isIgnoreId(this.subMethodId) ) ){
			if(isSubMethodFreeText() || !containsSubMethodId(this.subMethodId)){
				try {
					
					ElementOfList eol = new ElementOfList();
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.subMethod);
					
					if(isSubMethodFreeText()){
						eol.setValue(this.subMethodValue);
						eol.setValueId(this.subMethodValueId);
						eol.setListId(this.subMethodListNameId);
						this.subMethodValue = null;
						this.subMethodValueId = null;
					}else if(StringUtils.equals(subMethodListNameId, ValueListUtils.Names.idai_thesaurus_SubMethod.id)){
						ElementOfList tmp = MethodList.SubMethod.getEolById(this.subMethodId);
						eol.setValue(tmp.getValue());
						eol.setValueId(tmp.getValueId());
						eol.setUri(tmp.getUri());
						eol.setListId(MethodList.LIST_ID_SUB);
					}
					
					Services.getInstance().getMDService().saveEntry(eol);
					getDc().getSubMethodList().add(eol);
					this.subMethodId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This sub method has been already selected.");
			}
		}
	}
	
	public void listenerSaveMainDiscipline(ActionEvent event){
		if(!StringUtils.equals(this.mainDisciplineId, ApplicationBean.NONE_SELECTED_ID)){
			if(!containsMainDisciplineId(this.mainDisciplineId)){
				try {
					
					ElementOfList eol = DisciplineList.MainDiscipline.get(this.mainDisciplineId);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.mainDiscipline);
					Services.getInstance().getMDService().saveEntry(eol);
					getDc().getMainDisciplineList().add(eol);
					this.mainDisciplineId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This discipline has been already selected.");
			}
		}
	}
	
	public boolean containsMainDisciplineId(String mainDisciplineId){
		for(ElementOfList eol : this.getDc().getMainDisciplineList()){
			if(StringUtils.equals(eol.getValueId(), mainDisciplineId)){
				return true;
			}
		}
		return false;
	}
	
	public void listenerSaveSubDiscipline(ActionEvent event){
		if(!StringUtils.equals(this.subDisciplineId, ApplicationBean.NONE_SELECTED_ID) && !AbstractValueList.isIgnoreId(this.subDisciplineId)){
			if(!containsSubDisciplineId(this.subDisciplineId)){
				ElementOfList eol = null;
				try {
					
					eol = DisciplineList.SubDiscipline.get(this.subDisciplineId);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.subDiscipline);
					
					Services.getInstance().getMDService().saveEntry(eol);
					getDc().getSubDisciplineList().add(eol);
					this.subDisciplineId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					if(eol == null){
						logger.error("SubDisciplineId no found [ID"  + this.subDisciplineId + "]");
						addInternalError(e);
					}else{
						addInternalError(e);
					}
				}
				
			}else{
				addMsg("This discipline has been already selected.");
			}
		}
	}
	
	public boolean containsSubDisciplineId(String subDisciplineId){
		for(ElementOfList eol : this.getDc().getSubDisciplineList()){
			if(StringUtils.equals(eol.getValueId(), subDisciplineId)){
				return true;
			}
		}
		return false;
	}
	
	//&&&&&&&&&&&&&&&&&&&
	
	public void listenerSaveClassification(ActionEvent event){
		if(!StringUtils.equals(this.classificationId, ApplicationBean.NONE_SELECTED_ID)){
			if(!containsClassificationId(this.classificationId)){
				try {
					
					ElementOfList eol = new ElementOfList();
					eol.setValue(Classification.Type.getLabelGer(this.classificationId));
					eol.setValueId(this.classificationId);
					eol.setListId(ValueListUtils.Names.ianus_classification.id);
					eol.setUri(Classification.Type.getById(this.classificationId).url);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.classification);
					Services.getInstance().getMDService().saveEntry(eol);
					
					getDc().getClassificationList().add(eol);
					this.classificationId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This classification has been already selected.");
			}
		}
	}
	
	public boolean containsClassificationId(String classificationId){
		for(ElementOfList eol : this.getDc().getClassificationList()){
			if(StringUtils.equals(eol.getValueId(), classificationId)){
				return true;
			}
		}
		return false;
	}
	
	//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	public void listenerSaveMainMethod(ActionEvent event){
		if(!StringUtils.equals(this.mainMethodId, ApplicationBean.NONE_SELECTED_ID)){
			if(!containsMainMethodId(this.mainMethodId)){
				try {
					
					ElementOfList eol = MethodList.MainMethod.getEolById(this.mainMethodId);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.mainMethod);
					Services.getInstance().getMDService().saveEntry(eol);
					
					getDc().getMainMethodList().add(eol);
					this.mainMethodId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This main method has been already selected.");
			}
		}
	}
	
	public boolean containsMainMethodId(String mainMethodId){
		for(ElementOfList eol : this.getDc().getMainMethodList()){
			if(StringUtils.equals(eol.getValueId(), mainMethodId)){
				return true;
			}
		}
		return false;
	}
	
	//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	public void listenerSaveDataCategory(ActionEvent event){
		if(!StringUtils.equals(this.dataCategoryId, ApplicationBean.NONE_SELECTED_ID)){
			if(!containsDataCategoryId(this.dataCategoryId)){
				try {
					
					ElementOfList eol = new ElementOfList();
					eol.setValue(DataCategory.Type.getLabelGer(this.dataCategoryId));
					eol.setUri(DataCategory.Type.getById(this.dataCategoryId).url);
					eol.setValueId(this.dataCategoryId);
					eol.setListId(ValueListUtils.Names.ianus_data_category.id);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.dataCategory);
					Services.getInstance().getMDService().saveEntry(eol);
					
					getDc().getDataCategoryList().add(eol);
					this.dataCategoryId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This data category has been already selected.");
			}
		}
	}
	
	public boolean containsDataCategoryId(String dataCategoryId){
		for(ElementOfList eol : this.getDc().getDataCategoryList()){
			if(StringUtils.equals(eol.getValueId(), dataCategoryId)){
				return true;
			}
		}
		return false;
	}
	
	//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	public void listenerDeleteDataCategory(ActionEvent event){
		try {
			ElementOfList eol = (ElementOfList)getRequestBean("dataCategory");
			this.getDc().getDataCategoryList().remove(eol);
			Services.getInstance().getMDService().deleteDBEntry(eol);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeleteClassification(ActionEvent event){
		try {
			ElementOfList eol = (ElementOfList)getRequestBean("classification");
			this.getDc().getClassificationList().remove(eol);
			Services.getInstance().getMDService().deleteDBEntry(eol);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeleteMainDiscipline(ActionEvent event){
		try {
			ElementOfList eol = (ElementOfList)getRequestBean("mainDiscipline");
			this.getDc().getMainDisciplineList().remove(eol);
			Services.getInstance().getMDService().deleteDBEntry(eol);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeleteSubDiscipline(ActionEvent event){
		try {
			ElementOfList eol = (ElementOfList)getRequestBean("subDiscipline");
			this.getDc().getSubDisciplineList().remove(eol);
			Services.getInstance().getMDService().deleteDBEntry(eol);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	public void listenerDeleteSubMethod(ActionEvent event){
		try {
			ElementOfList item0 = (ElementOfList)getRequestBean("subMethod");
			((DataCollection) this.getSource()).getSubMethodList().remove(item0);
			Services.getInstance().getMDService().deleteDBEntry(item0);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public boolean containsSubMethodId(String subMethodId){
		for(ElementOfList eol : this.getDc().getSubMethodList()){
			if(StringUtils.equals(eol.getValueId(), subMethodId)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isSubMethodFreeText(){
		return StringUtils.equals(ValueListUtils.Names.free_text.id, getSubMethodListNameId()) ||
				StringUtils.equals(ValueListUtils.Names.fish_event_types.id, getSubMethodListNameId());
						
	}
	
	public List<SelectItem> getSubMethodList(){
		List<SelectItem> list = null;
		if(StringUtils.equals(ValueListUtils.Names.idai_thesaurus_SubMethod.id, getSubMethodListNameId())){
			list = getSession().getAppBean().getSubMethodGerList();
		}
		return list;
	}
	
	//	SUB METHODE ]
	
	// [ SUB CONTENT
	
	public void listenerSaveSubContent(ActionEvent event){
		if(isSubContentFreeText() || !StringUtils.equals(this.subContentId, ApplicationBean.NONE_SELECTED_ID)){
			if(isSubContentFreeText() || !containsSubContentId(this.subContentId)){
				try {
					
					ElementOfList eol = new ElementOfList();
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.subContent);
					
					if(isMainContentFreeText()){
						eol.setValue(this.subContentValue);
						eol.setValueId(this.subContentValueId);
						eol.setListId(this.subContentListNameId);
						this.subContentValue = null;
						this.subContentValueId = null;
					}
					
					Services.getInstance().getMDService().saveEntry(eol);
					getDc().getSubContentList().add(eol);
					this.subContentId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This item has been already selected.");
			}
		}
	}
	
	public void listenerDeleteSubContent(ActionEvent event){
		try {
			ElementOfList item0 = (ElementOfList)getRequestBean("subContent");
			((DataCollection) this.getSource()).getSubContentList().remove(item0);
			Services.getInstance().getMDService().deleteDBEntry(item0);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public boolean isSubContentFreeText(){
		return true;	
	}
	
	public boolean containsSubContentId(String subContentId){
		for(ElementOfList eol : this.getDc().getSubContentList()){
			if(StringUtils.equals(eol.getValueId(), subContentId)){
				return true;
			}
		}
		return false;
	}
	
	// SUB CONTEN ]
	
	
	//	[ Main Content
	
	public void listenerSaveMainContent(ActionEvent event){
		if(isMainContentFreeText() || !StringUtils.equals(this.mainContentId, ApplicationBean.NONE_SELECTED_ID)){
			if(isMainContentFreeText() || !containsMainContentId(this.mainContentId)){
				try {
					
					ElementOfList eol = new ElementOfList();
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.mainContent);
					
					if(isMainContentFreeText()){
						eol.setValue(this.mainContentValue);
						eol.setValueId(this.mainContentValueId);
						eol.setListId(this.mainContentListNameId);
						this.mainContentValue = null;
						this.mainContentValueId = null;
					}else if(StringUtils.equals(mainContentListNameId, ValueListUtils.Names.adex_type_general.id)){
						MainContentAdexTypeList.Type tmp = MainContentAdexTypeList.Type.getById(this.mainContentId);
						eol.setValue(tmp.labelGer);
						eol.setValueId(tmp.id);
						eol.setUri(tmp.url);
						eol.setListId(ValueListUtils.Names.adex_type_general.id);
					}
					
					Services.getInstance().getMDService().saveEntry(eol);
					getDc().getMainContentList().add(eol);
					this.mainContentId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This item has been already selected.");
			}
		}
	}
	
	public void listenerDeleteMainContent(ActionEvent event){
		try {
			ElementOfList item0 = (ElementOfList)getRequestBean("mainContent");
			((DataCollection) this.getSource()).getMainContentList().remove(item0);
			Services.getInstance().getMDService().deleteDBEntry(item0);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public boolean containsMainContentId(String mainContentId){
		for(ElementOfList eol : this.getDc().getMainContentList()){
			if(StringUtils.equals(eol.getValueId(), mainContentId)){
				return true;
			}
		}
		return false;
	}
	

	public boolean isMainContentListIdEqualsFreeText(){
		return (StringUtils.equals(this.mainContentListNameId, ValueListUtils.Names.free_text.id));
	}
	
	public boolean isSubContentListIdEqualsFreeText(){
		return (StringUtils.equals(this.subContentListNameId, ValueListUtils.Names.free_text.id));
	}
	
	public boolean isMainContentFreeText(){
		return StringUtils.equals(ValueListUtils.Names.free_text.id, getMainContentListNameId()) ||
				StringUtils.equals(ValueListUtils.Names.gnd.id, getMainContentListNameId()) ||
				StringUtils.equals(ValueListUtils.Names.idai_thesaurus_all.id, getMainContentListNameId()) ||
				StringUtils.equals(ValueListUtils.Names.getty_aat.id, getMainContentListNameId()) ||
				StringUtils.equals(ValueListUtils.Names.wortnetz_kultur.id, getMainContentListNameId()) ||
				StringUtils.equals(ValueListUtils.Names.heritage_data.id, getMainContentListNameId());
						
	}
	
	public List<SelectItem> getMainContentList(){
		List<SelectItem> list = null;
		if(StringUtils.equals(ValueListUtils.Names.adex_type_general.id, getMainContentListNameId())){
			list = getSession().getAppBean().getMainContentAdexTypeGerList();
		}
		return list;
	}
	
	public void listenerDeleteMainMethod(ActionEvent event){
		try {
			ElementOfList item0 = (ElementOfList)getRequestBean("mainMethod");
			((DataCollection) this.getSource()).getMainMethodList().remove(item0);
			Services.getInstance().getMDService().deleteDBEntry(item0);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	//	Main Content ] 
	
	public String actionAddDataGeneration(){
		try {
			this.dataGeneration.setSource(this.getDc());
			this.dataGeneration.setContentType(TimeSpan.ContentType.data_generation);
			Services.getInstance().getMDService().saveEntry(this.dataGeneration);
			this.getDc().getDataGenerationList().add(this.dataGeneration);
			this.dataGeneration = new TimeSpan();
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeleteDataGeneration(){
		try {
			TimeSpan dg = (TimeSpan)getRequestBean("dataGeneration");
			Services.getInstance().getMDService().deleteDBEntry(dg);
			this.getDc().getDataGenerationList().remove(dg);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionResetDataGeneration(){
		this.dataGeneration = new TimeSpan();
		return PAGE_NAME;
	}
	
	public String actionShowFilePage(){
		getSession().getCollectionFilePage().loadDataCollection(getDc());
		return CollectionFilePage.PAGE_NAME;
	}
	
	public void actionListenerAddCollectionLanguage(ActionEvent event){
		ISOLanguage selectedLan = LanguageList.getByNameGer(collectionLanguageName); 
		try {
			Language newItem = new Language();
			newItem.setSource(getDc());
			newItem.setContentType(Language.ContentType.collection_language);
			newItem.setCode(selectedLan.getId());
			newItem.setLabel(selectedLan.getNameEng());
			
			Services.getInstance().getMDService().saveEntry(newItem);
			this.getDc().getCollectionLanguageList().add(newItem);
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeleteCollectionLanguage(ActionEvent event){
		try {
			Language lan = (Language)getRequestBean("collectionLanguage");
			(getDc()).getCollectionLanguageList().remove(lan);
			Services.getInstance().getMDService().deleteDBEntry(lan);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeleteMetadataLanguage(ActionEvent event){
		try {
			Language lan = (Language)getRequestBean("metadataLanguage");
			(getDc()).getMetadataLanguageList().remove(lan);
			Services.getInstance().getMDService().deleteDBEntry(lan);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	
	public void listenerAddNewAccessRight(ActionEvent event){
		this.accessRight = new AccessRight();
	}
	
	public void listenerCancelAccessRightEdition(ActionEvent event){
		this.accessRight = null;
	}
	
	public void listenerSelectAccessRight(ActionEvent event){
		this.accessRight = (AccessRight)getRequestBean("accessRight");
	}
	
	public void listenerDeleteAccessRight(ActionEvent event){
		try {
			AccessRight accessRight0 = (AccessRight)getRequestBean("accessRight");
			(getDc()).getAccessRightList().remove(accessRight0);
			Services.getInstance().getMDService().deleteDBEntry(accessRight0);
			this.accessRight = new AccessRight();
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionDeleteRightsHolder(){
		this.deleteRoles4Actor(ActorRole.Type.dcms_RightsHolder);
		return PAGE_NAME;
	}
	
	public String actionDeleteFunder(){
		this.deleteRoles4Actor(ActorRole.Type.dcms_Funder);
		return PAGE_NAME;
	}
	
	public void listenerSaveAccessRight(ActionEvent event){
		try {
			accessRight.setDcId(getDc().getId());
			
			Services.getInstance().getMDService().saveEntry(accessRight);
			DataCollection dc = getDc();
			if(!dc.getAccessRightList().contains(accessRight)){
				dc.getAccessRightList().add(accessRight);
			}				
			this.accessRight = new AccessRight();
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	/*
	public void listenerRefreshDcTable(ActionEvent event){
		this.loadCollections();
	}*/
	
	/**
	 * This method is used to load a DataCollection from a InformationPackage Page.
	 * One parameters of this method is IP. With the IP parameter, this page can generate the "Back to ..." button.
	 * @param ip
	 * @param dcBase
	 * @param source
	 */
	public void load(WorkflowIP ip, DataCollection dcBase, IANUSEntity source){
		this.ip = ip;
		this.load(dcBase, source);
	}
	
	@Override
	public void load(DataCollection dcBase, IANUSEntity source){
		try {
			if(!(source instanceof DataCollection))
				throw new Exception("The variable source is not an instance of DataCollection " + source.toString());
			
			if(source.isPersistent()){
				DataCollection dc0 = Services.getInstance().getMDService().getDataCollection(source.getId());
				super.load(dc0, dc0);
				
				this.actorList = Services.getInstance().getMDService().getActorList(getDc());
				
				this.title = new TextAttributeComponent(getDc(), TextAttribute.ContentType.title, PAGE_NAME, true, true);
				this.alternativeTitle = new TextAttributeComponent(getDc(), TextAttribute.ContentType.alternativeTitle, PAGE_NAME, true, false);
				this.projectDescription = new TextAttributeComponent(getDc(), TextAttribute.ContentType.projectDescription, PAGE_NAME, true, true);
				this.dataCollectionDescription = new TextAttributeComponent(getDc(), TextAttribute.ContentType.dataCollectionDescription, PAGE_NAME, true, true);
				this.descriptionDataProtection = new TextAttributeComponent(getDc(), TextAttribute.ContentType.descriptionDataProtection, PAGE_NAME, true, true);
				this.motivation = new TextAttributeComponent(getDc(), TextAttribute.ContentType.motivation, PAGE_NAME, true, true);
				this.summary = new TextAttributeComponent(getDc(), TextAttribute.ContentType.summary, PAGE_NAME, true, true);
				
				this.mainDiscipline = new ElementOfListComponent(this.getDc(), ElementOfList.ContentType.mainDiscipline);
				this.subDiscipline = new ElementOfListComponent(this.getDc(), ElementOfList.ContentType.subDiscipline);
				
				this.resourceTypeId = (this.getDc().getReasonDataProtectionList().isEmpty()) ? ApplicationBean.NONE_SELECTED_ID : this.getDc().getReasonDataProtectionList().iterator().next().getValueId();
				
				this.dataGeneration = new TimeSpan();	
				this.time = new Time();
				
				this.mainContentListNameId = ValueListUtils.Names.free_text.id;
				this.subContentListNameId = ValueListUtils.Names.free_text.id;
				
			}else{
				super.load(dcBase, source);
			}
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	
	public void listenerSaveResourceType(ActionEvent event){
		if(!StringUtils.equals(this.resourceTypeId, ApplicationBean.NONE_SELECTED_ID)){
			if(!containsResourceTypeId(this.resourceTypeId)){
				try {
					
					ElementOfList eol = new ElementOfList();
					eol.setValue(Resource.Type.getLabelGer(this.resourceTypeId));
					eol.setValueId(this.resourceTypeId);
					eol.setListId(ValueListUtils.Names.resource_type.id);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.resourceType);
					Services.getInstance().getMDService().saveEntry(eol);
					
					getDc().getResourceTypeList().add(eol);
					this.resourceTypeId = ApplicationBean.NONE_SELECTED_ID;
					
				} catch (Exception e) {
					addInternalError(e);
				}
				
			}else{
				addMsg("This resource type has been already selected.");
			}
		}
	}
	
	public boolean containsResourceTypeId(String resourceTypeId){
		for(ElementOfList eol : this.getDc().getResourceTypeList()){
			if(StringUtils.equals(eol.getValueId(), resourceTypeId)){
				return true;
			}
		}
		return false;
	}
	
	public void listenerDeleteResourceType(ActionEvent event){
		try {
			ElementOfList eol = (ElementOfList)getRequestBean("resourceType");
			this.getDc().getResourceTypeList().remove(eol);
			Services.getInstance().getMDService().deleteDBEntry(eol);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	
	public void listenerSaveDc(ActionEvent event){
		this.saveDc();
	}
	
	private void saveDc(){
		try {
			
			if(ApplicationBean.NONE_SELECTED_ID.equals(this.reasonDataProtectionId)){
				if(!getDc().getReasonDataProtectionList().isEmpty()){
					for(ElementOfList item : getDc().getReasonDataProtectionList()){
						Services.getInstance().getMDService().deleteDBEntry(item);
					}
					getDc().getReasonDataProtectionList().clear();
				}
			}else{
				ElementOfList eol = (getDc().getReasonDataProtectionList().isEmpty()) ? new ElementOfList() : getDc().getReasonDataProtectionList().iterator().next();
				if(!this.reasonDataProtectionId.equals(eol.getValueId())){
					eol.setValue(DataCollection.ReasonDataProtection.getLabelEng(this.reasonDataProtectionId));
					eol.setValueId(this.reasonDataProtectionId);
					eol.setListId(DataCollection.ReasonDataProtection.LIST_ID);
					eol.setSource(getDc());
					eol.setContentType(ElementOfList.ContentType.reasonDataProtection);
					Services.getInstance().getMDService().saveEntry(eol);
					getDc().getReasonDataProtectionList().add(eol);
				}
			}
			
			Services.getInstance().getMDService().saveDataCollection(getDc());
			//this.loadCollections();
			this.load(getDcBase(), getSource());
			addMsg("Data collection saved " + getDc().getId());
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	
	public AccessRight getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(AccessRight accessRight) {
		this.accessRight = accessRight;
	}
	
	public String getCollectionLanguageName() {
		return collectionLanguageName;
	}

	public void setCollectionLanguageName(String collectionLanguageName) {
		this.collectionLanguageName = collectionLanguageName;
	}

	public ElementOfListComponent getSubDiscipline() {
		return subDiscipline;
	}

	public void setSubDiscipline(ElementOfListComponent subDiscipline) {
		this.subDiscipline = subDiscipline;
	}
	/*
	public ElementOfListComponent getMainContent() {
		return mainContent;
	}

	public void setMainContent(ElementOfListComponent mainContent) {
		this.mainContent = mainContent;
	}*/

	/*
	public ElementOfListComponent getMainMethod() {
		return mainMethod;
	}

	public void setMainMethod(ElementOfListComponent mainMethod) {
		this.mainMethod = mainMethod;
	}

	public ElementOfListComponent getSubMethod() {
		return subMethod;
	}

	public void setSubMethod(ElementOfListComponent subMethod) {
		this.subMethod = subMethod;
	}
	*/
	public TextAttributeComponent getAlternativeTitle() {
		return alternativeTitle;
	}

	public void setAlternativeTitle(TextAttributeComponent alternativeTitle) {
		this.alternativeTitle = alternativeTitle;
	}


	public TextAttributeComponent getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(TextAttributeComponent projectDescription) {
		this.projectDescription = projectDescription;
	}	
	
	public TextAttributeComponent getDataCollectionDescription() {
		return dataCollectionDescription;
	}

	public void setDataCollectionDescription(TextAttributeComponent dataCollectionDescription) {
		this.dataCollectionDescription = dataCollectionDescription;
	}
	
	public TextAttributeComponent getDescriptionDataProtection() {
		return descriptionDataProtection;
	}

	public void setDescriptionDataProtection(TextAttributeComponent descriptionDataProtection) {
		this.descriptionDataProtection = descriptionDataProtection;
	}

	public ElementOfListComponent getMainDiscipline() {
		return mainDiscipline;
	}

	public void setMainDiscipline(ElementOfListComponent mainDiscipline) {
		this.mainDiscipline = mainDiscipline;
	}

	public TimeSpan getDataGeneration() {
		return dataGeneration;
	}

	public void setDataGeneration(TimeSpan dataGeneration) {
		this.dataGeneration = dataGeneration;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public WorkflowIP getIp() {
		return ip;
	}

	public void setIp(WorkflowIP ip) {
		this.ip = ip;
	}

	public TextAttributeComponent getTitle() {
		return title;
	}

	public void setTitle(TextAttributeComponent title) {
		this.title = title;
	}

	public TextAttributeComponent getSummary() {
		return summary;
	}

	public void setSummary(TextAttributeComponent summary) {
		this.summary = summary;
	}

	public String getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getReasonDataProtectionId() {
		return reasonDataProtectionId;
	}

	public void setReasonDataProtectionId(String reasonDataProtectionId) {
		this.reasonDataProtectionId = reasonDataProtectionId;
	}
	
	public String getSubMethodValue() {
		return subMethodValue;
	}

	public void setSubMethodValue(String subMethodValue) {
		this.subMethodValue = subMethodValue;
	}

	public String getSubMethodValueId() {
		return subMethodValueId;
	}

	public void setSubMethodValueId(String subMethodValueId) {
		this.subMethodValueId = subMethodValueId;
	}

	public String getSubMethodId() {
		return subMethodId;
	}

	public void setSubMethodId(String subMethodId) {
		this.subMethodId = subMethodId;
	}

	public String getSubMethodListNameId() {
		return subMethodListNameId;
	}

	public void setSubMethodListNameId(String subMethodListNameId) {
		this.subMethodListNameId = subMethodListNameId;
	}

	public String getMainContentId() {
		return mainContentId;
	}

	public void setMainContentId(String mainContentId) {
		this.mainContentId = mainContentId;
	}

	public String getMainContentListNameId() {
		return mainContentListNameId;
	}

	public void setMainContentListNameId(String mainContentListNameId) {
		this.mainContentListNameId = mainContentListNameId;
	}

	public String getMainContentValue() {
		return mainContentValue;
	}

	public void setMainContentValue(String mainContentValue) {
		this.mainContentValue = mainContentValue;
	}

	public String getMainContentValueId() {
		return mainContentValueId;
	}

	public void setMainContentValueId(String mainContentValueId) {
		this.mainContentValueId = mainContentValueId;
	}

	public String getSubContentId() {
		return subContentId;
	}

	public void setSubContentId(String subContentId) {
		this.subContentId = subContentId;
	}

	public String getSubContentListNameId() {
		return subContentListNameId;
	}

	public void setSubContentListNameId(String subContentListNameId) {
		this.subContentListNameId = subContentListNameId;
	}

	public String getSubContentValue() {
		return subContentValue;
	}

	public void setSubContentValue(String subContentValue) {
		this.subContentValue = subContentValue;
	}

	public String getSubContentValueId() {
		return subContentValueId;
	}

	public void setSubContentValueId(String subContentValueId) {
		this.subContentValueId = subContentValueId;
	}

	public Set<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(Set<Actor> actorList) {
		this.actorList = actorList;
	}
	
	public TextAttributeComponent getMotivation() {
		return motivation;
	}

	public void setMotivation(TextAttributeComponent motivation) {
		this.motivation = motivation;
	}
}
