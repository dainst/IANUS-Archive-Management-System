package de.ianus.ingest.web.pages;

import javax.faces.event.ActionEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.pages.component.IpComponentPage;
import de.ianus.ingest.web.pages.metadata.ActorPage;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.ingest.web.pages.metadata.InitialMDPage;
import de.ianus.ingest.web.pages.metadata.PlacePage;
import de.ianus.ingest.web.pages.metadata.ReferencePage;
import de.ianus.ingest.web.pages.metadata.RelatedResourcePage;
import de.ianus.metadata.bo.CollectionFile;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.resource.RelatedResource;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Language;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.utils.ISOLanguage;
import de.ianus.metadata.utils.LanguageList;

public class IANUSEntityPage extends IpComponentPage{
	
	private static final Logger logger = LogManager.getLogger(IANUSEntityPage.class);
	
	private DataCollection dcBase;
	private IANUSEntity source;
	
	private Identifier internalId;
	private Identifier externalId;
	
	private Identifier internalPersistentId;
	private Identifier externalPersistentId;
	
	private String metadataLanguageName;
	
	//In this variable, we will save temporally the discipline selected by the user
	//common for DataCollectionPage and InitialMDPage
	protected String classificationId;
	protected String mainDisciplineId;
	protected String subDisciplineId;
	protected String mainMethodId;
	protected String dataCategoryId;

	public void load(DataCollection dcBase, IANUSEntity source){
		this.source = source;
		this.dcBase = dcBase;
		this.internalId = new Identifier();
		this.externalId = new Identifier();
		this.metadataLanguageName = new String();
	}
	
	public void listenerAddMetadataLanguage(ActionEvent event){
		ISOLanguage selectedLan = LanguageList.getByNameGer(getMetadataLanguageName()); 
		try {
			Language newItem = new Language();
			newItem.setSource(getSource());
			newItem.setContentType(Language.ContentType.metadata_language);
			newItem.setCode(selectedLan.getId());
			newItem.setLabel(selectedLan.getNameEng());
			
			Services.getInstance().getMDService().saveEntry(newItem);
			this.getSource().getMetadataLanguageList().add(newItem);
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionAddPlace(){
		try {
			getSession().getPlacePage().loadPlace(getDcBase(), getSource(), new Place());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PlacePage.PAGE_NAME;
	}
	
	public String actionEditPlace(){
		try {
			Place place = (Place)getRequestBean("place");
			getSession().getPlacePage().loadPlace(getDcBase(), getSource(), place);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PlacePage.PAGE_NAME;
	}
	
	public String actionDeletePlace(){
		try {
			Place place = (Place)getRequestBean("place");
			Services.getInstance().getMDService().deletePlace(place);
			this.getDc().getPlaceList().remove(place);
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	
	public String actionEditActor(){
		try {
			Actor actor0 = (Actor)getRequestBean("actor");
			getSession().getActorPage().load(getDcBase(), getSource(), actor0);
			return ActorPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionAddPerson(){
		try {
			getSession().getActorPage().load(getDcBase(), getSource(), new Person());
		} catch (Exception e) {
			addInternalError(e);
		}
		return ActorPage.PAGE_NAME;
	}
	
	public String actionAddInstitution(){
		try {
			getSession().getActorPage().load(getDcBase(), getSource(), new Institution());
		} catch (Exception e) {
			addInternalError(e);
		}
		return ActorPage.PAGE_NAME;
	} 
	
	public String actionDeleteAuthor(){
		this.deleteRoles4Actor(ActorRole.Type.ianus_PrincipalInvest);
		return toThisPage();
	}
	
	public String actionDeleteDataCurator(){
		this.deleteRoles4Actor(ActorRole.Type.dcms_DataCurator);
		return toThisPage();
	}
	
	public String actionDeleteMetadataEditor(){
		this.deleteRoles4Actor(ActorRole.Type.ianus_AuthorMetadata);
		return toThisPage();
	}
	
	protected void deleteRoles4Actor(ActorRole.Type roleType){
		try {
			Actor actor0 = (Actor)getRequestBean("actor");
			logger.info(actor0.toString() + ", RoleType: " + roleType);
			Services.getInstance().getMDService().deleteRoles4SourceAndActor(this.getSource(), actor0, roleType);
			/*
			if(!existActorWithOtherRole(dc0, actor0, roleType)){
				WebServices.getInstance().getMDService().deleteDBEntry(actor0);
			}*/
			this.load(getDcBase(), getSource());
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	//*******************************

	public String actionEditRelatedResource(){
		try {
			RelatedResource rs = (RelatedResource)getRequestBean("relatedResource");
			getSession().getRelatedResourcePage().load(this.getDcBase(), this.getSource(), rs);
		} catch (Exception e) {
			addInternalError(e);
		}
		return RelatedResourcePage.PAGE_NAME;
	}
	
	public String actionDeleteRelatedResource(){
		try {
			RelatedResource rs = (RelatedResource)getRequestBean("relatedResource");
			Services.getInstance().getMDService().deleteDBEntry(rs);
			this.getSource().getRelatedResourceList().remove(rs);
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionAddRelatedResource(){
		try {
			getSession().getRelatedResourcePage().load(this.getDcBase(), this.getSource(), new RelatedResource());
		} catch (Exception e) {
			addInternalError(e);
		}
		return RelatedResourcePage.PAGE_NAME;
	}
	
	
	
	public String actionEditReference(){
		try {
			Reference ref = (Reference)getRequestBean("reference");
			getSession().getReferencePage().load(this.getDcBase(), this.getSource(), ref, ref.getPublication());
		} catch (Exception e) {
			addInternalError(e);
		}
		return ReferencePage.PAGE_NAME;
	}
	
	public String actionDeleteReference(){
		try {
			Reference ref = (Reference)getRequestBean("reference");
			Services.getInstance().getMDService().deleteDBEntry(ref);
			this.getSource().getReferenceList().remove(ref);
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionAddReference(){
		try {
			getSession().getReferencePage().load(this.getDcBase(), this.getSource(), new Reference());
		} catch (Exception e) {
			addInternalError(e);
		}
		return ReferencePage.PAGE_NAME;
	}
	
	//*******************************
	
	
	public String actionDeleteExternalId(){
		try {
			Identifier item = (Identifier)getRequestBean("externalId");
			Services.getInstance().getMDService().deleteDBEntry(item);
			this.source.getExternalIdList().remove(item);
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionAddExternalId(){
		try {
			
			if(externalPersistentId != null){
				this.externalPersistentId.setType(this.externalId.getType());
				this.externalPersistentId.setInstitution(this.externalId.getInstitution());
				this.externalPersistentId.setValue(this.externalId.getValue());
				this.externalPersistentId.setUri(this.externalId.getUri());
				
				this.externalId = this.externalPersistentId;
				this.externalPersistentId = null;
			}
			
			this.externalId.setSource(this.source);
			this.externalId.setContentType(Identifier.ContentType.external_id);
			Services.getInstance().getMDService().saveEntry(this.externalId);
			this.source.getExternalIdList().add(this.externalId);
			this.externalId = new Identifier();
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionEditExternalId(){
		try {
			this.externalPersistentId = (Identifier)getRequestBean("externalId");
			this.externalId = Identifier.clone(this.externalPersistentId, this.source.getId());
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionCancelEditionInternalId(){
		this.internalId = new Identifier();
		this.internalPersistentId = null;
		return toThisPage();
	}
	
	public String actionCancelEditionExternalId(){
		this.externalId = new Identifier();
		this.externalPersistentId = null;
		return toThisPage();
	}
	
	public String actionAddInternalId(){
		try {
			
			if(internalPersistentId != null){
				this.internalPersistentId.setType(this.internalId.getType());
				this.internalPersistentId.setInstitution(this.internalId.getInstitution());
				this.internalPersistentId.setValue(this.internalId.getValue());
				this.internalPersistentId.setUri(this.internalId.getUri());
				
				this.internalId = this.internalPersistentId;
				this.internalPersistentId = null;
			}
			
			this.internalId.setSource(this.source);
			this.internalId.setContentType(Identifier.ContentType.internal_id);
			this.internalId.setInstitution("IANUS");
			Services.getInstance().getMDService().saveEntry(this.internalId);
			this.source.getInternalIdList().add(this.internalId);
			this.internalId = new Identifier();
			//this method update the collection identifier in the related IP.
			this.getSession().updateCollectionTitelInIP(getDc());
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionEditInternalId(){
		try {
			this.internalPersistentId = (Identifier)getRequestBean("internalId");
			this.internalId = Identifier.clone(this.internalPersistentId, this.source.getId());
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String actionDeleteInternalId(){
		try {
			Identifier item = (Identifier)getRequestBean("internalId");
			Services.getInstance().getMDService().deleteDBEntry(item);
			this.source.getInternalIdList().remove(item);
		} catch (Exception e) {
			addInternalError(e);
		}
		return toThisPage();
	}
	
	public String toThisPage(){
		
		if(this instanceof DataCollectionPage){
			return DataCollectionPage.PAGE_NAME;
		}else if(this instanceof InitialMDPage){
			return InitialMDPage.PAGE_NAME;
		} 
		return null;
	}
	
	public DataCollection getDc(){
		return (source instanceof DataCollection) ? (DataCollection) source : null;
	}
	
	public CollectionFile getFile(){
		return (source instanceof CollectionFile) ? (CollectionFile) source : null;
	}

	public DataCollection getDcBase() {
		return dcBase;
	}

	public void setDcBase(DataCollection dcBase) {
		this.dcBase = dcBase;
	}

	public IANUSEntity getSource() {
		return source;
	}

	public void setSource(IANUSEntity source) {
		this.source = source;
	}

	public Identifier getInternalId() {
		return internalId;
	}

	public void setInternalId(Identifier internalId) {
		this.internalId = internalId;
	}

	public Identifier getExternalId() {
		return externalId;
	}

	public void setExternalId(Identifier externalId) {
		this.externalId = externalId;
	}
	
	public String getMetadataLanguageName() {
		return metadataLanguageName;
	}

	public void setMetadataLanguageName(String metadataLanguageName) {
		this.metadataLanguageName = metadataLanguageName;
	}
	
	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public String getMainDisciplineId() {
		return mainDisciplineId;
	}

	public void setMainDisciplineId(String mainDisciplineId) {
		this.mainDisciplineId = mainDisciplineId;
	}
	public String getSubDisciplineId() {
		return subDisciplineId;
	}

	public void setSubDisciplineId(String subDisciplineId) {
		this.subDisciplineId = subDisciplineId;
	}
	
	public String getMainMethodId() {
		return mainMethodId;
	}

	public void setMainMethodId(String mainMethodId) {
		this.mainMethodId = mainMethodId;
	}
	
	public String getDataCategoryId() {
		return dataCategoryId;
	}

	public void setDataCategoryId(String dataCategoryId) {
		this.dataCategoryId = dataCategoryId;
	}

	public Identifier getInternalPersistentId() {
		return internalPersistentId;
	}

	public void setInternalPersistentId(Identifier internalPersistentId) {
		this.internalPersistentId = internalPersistentId;
	}

	public Identifier getExternalPersistentId() {
		return externalPersistentId;
	}

	public void setExternalPersistentId(Identifier externalPersistentId) {
		this.externalPersistentId = externalPersistentId;
	}
}
