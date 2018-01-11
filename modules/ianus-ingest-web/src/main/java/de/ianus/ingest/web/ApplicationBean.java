package de.ianus.ingest.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.bo.PreIngestReport;
import de.ianus.ingest.core.bo.ums.User;
import de.ianus.ingest.web.utils.SelectItemComparator;
import de.ianus.metadata.bo.AccessRight;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.actor.Person.Gender;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.Classification;
import de.ianus.metadata.bo.utils.DataCategory;
import de.ianus.metadata.bo.utils.FileFormat;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Resource;
import de.ianus.metadata.utils.DisciplineList;
import de.ianus.metadata.utils.ISOLanguage;
import de.ianus.metadata.utils.LanguageList;
import de.ianus.metadata.utils.LanguageShortList;
import de.ianus.metadata.utils.MainContentAdexTypeList;
import de.ianus.metadata.utils.MethodList;
import de.ianus.metadata.utils.ValueListUtils;
import de.ianus.metadata.utils.ValueListUtils.Names;



public class ApplicationBean {

	private static final Logger logger = LogManager.getLogger(ApplicationBean.class);
	
	private static String PROPERTIES_FILES = "ianus-ingest-web.properties";
	
	private Properties properties = new Properties();
	
	private boolean debugMode = false;
	
	public  static String NONE_SELECTED_ID = "0";
	public static String NONE_SELECTED_LABEL = "none selected";
	
	//suggestion list for variable gender
	private static List<SelectItem> genderEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> genderGerList = new ArrayList<SelectItem>();
	
	//suggestion list for variable role
	//private static List<SelectItem> roleSuggestion = new ArrayList<SelectItem>();
	
	private static List<SelectItem> referenceRelationTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> referenceRelationTypeGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> publicationTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> publicationTypeGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> relatedSourceRepresentationSuggestion = new ArrayList<SelectItem>();
	
	private static List<SelectItem> daraRelationSuggestion = new ArrayList<SelectItem>();
	
	private static List<SelectItem> alternativeRepresentationType = new ArrayList<SelectItem>();
	
	private static List<SelectItem> projectStatusEngSuggestion = new ArrayList<SelectItem>();
	private static List<SelectItem> projectStatusGerSuggestion = new ArrayList<SelectItem>();
	
	//suggestion list for variable file format
	private static List<SelectItem> fileFormatSuggestList = new ArrayList<SelectItem>();
	
	//suggestion list for variable resource availability
	private static List<SelectItem> accessabilityEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> accessabilityGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> externalIdEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> externalIdGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> internalIdEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> internalIdGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> identifierPersonSuggestList = new ArrayList<SelectItem>();
	
	
	
	private static List<SelectItem> personTitleSuggestList = new ArrayList<SelectItem>();
	
	//private static List<SelectItem> subDisciplineList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> mainMethodEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> mainMethodGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> subMethodEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> subMethodGerList = new ArrayList<SelectItem>();
	
	//private static List<SelectItem> ianusSubMethodList = new ArrayList<SelectItem>();
	
	//private static List<SelectItem> publicationPidList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> placeIdentifierList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> reportStateList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> dataCategoryEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> dataCategoryGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> mainDisciplineEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> mainDisciplineGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> subDisciplineEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> subDisciplineGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> classificationEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> classificationGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> mainContentAdexTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> mainContentAdexTypeGerList = new ArrayList<SelectItem>();
	
	//suggestion list for variable access right type
	private static List<SelectItem> rightTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> rightTypeGerList = new ArrayList<SelectItem>();
		
	//suggestion list for variable access right group
	private static List<SelectItem> groupTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> groupTypeGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> resourceTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> resourceTypeGerList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> reasonDataProtectionEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> reasonDataProtectionGerList = new ArrayList<SelectItem>();
	
	
	//List of List Names
	private static List<SelectItem> subMethodNameList = new ArrayList<SelectItem>();
	private static List<SelectItem> mainContentNameList = new ArrayList<SelectItem>();
	private static List<SelectItem> subContentNameList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> userRoleList = new ArrayList<SelectItem>();
	
	static{
		
		for(User.UserRole item : User.UserRole.values()){
			userRoleList.add(new SelectItem(item.toString()));
		}
		
		reasonDataProtectionEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		reasonDataProtectionGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(DataCollection.ReasonDataProtection item : DataCollection.ReasonDataProtection.values()){
			reasonDataProtectionGerList.add(new SelectItem(item.id, item.labelGer));
			reasonDataProtectionEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		resourceTypeEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		resourceTypeGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(Resource.Type item : Resource.Type.values()){
			resourceTypeGerList.add(new SelectItem(item.id, item.labelGer));
			resourceTypeEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		//rightTypeEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		//rightTypeGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(AccessRight.RightType item : AccessRight.RightType.values()){
			rightTypeGerList.add(new SelectItem(item.id, item.labelGer));
			rightTypeEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		//groupTypeEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		//groupTypeGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(AccessRight.GroupType item : AccessRight.GroupType.values()){
			groupTypeGerList.add(new SelectItem(item.id, item.labelGer));
			groupTypeEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		//Classification
		//classificationGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		//classificationEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(Classification.Type item : Classification.Type.values()){
			classificationGerList.add(new SelectItem(item.id, item.labelGer));
			classificationEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		//MainContent
		//mainContentAdexTypeEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		//mainContentAdexTypeGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(MainContentAdexTypeList.Type item : MainContentAdexTypeList.Type.values()){
			mainContentAdexTypeEngList.add(new SelectItem(item.id, item.labelEng));
			mainContentAdexTypeGerList.add(new SelectItem(item.id, item.labelGer));
		}
		
		for(DataCategory.Type item : DataCategory.Type.values()){
			dataCategoryGerList.add(new SelectItem(item.id, item.labelGer));
			dataCategoryEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		for(DisciplineList.MainDiscipline item : DisciplineList.MainDiscipline.values()){
			mainDisciplineGerList.add(new SelectItem(item.id, item.labelGer));
			mainDisciplineEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		for(DisciplineList.SubDiscipline item : DisciplineList.SubDiscipline.values()){
			subDisciplineGerList.add(new SelectItem(item.id, item.labelGer));
			subDisciplineEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		for(PreIngestReport.State item : PreIngestReport.State.values()){
			reportStateList.add(new SelectItem(item.toString(), item.toString()));
		}
		
		placeIdentifierList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(Names item : ValueListUtils.gazetteerListName){
			placeIdentifierList.add(new SelectItem(item.id, item.getField("label")));
		} 

		try {
			for(MethodList.MainMethod item : MethodList.MainMethod.values()){
				mainMethodEngList.add(new SelectItem(item.id, item.labelEng));
				mainMethodGerList.add(new SelectItem(item.id, item.labelGer));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			for(MethodList.SubMethod item : MethodList.SubMethod.values()){
				subMethodEngList.add(new SelectItem(item.id, item.labelEng));
				subMethodGerList.add(new SelectItem(item.id, item.labelGer));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ***** [ List of List Names
		for(ValueListUtils.Names item : ValueListUtils.subMethodNameList){
			subMethodNameList.add(new SelectItem(item.id, item.getField("label")));
		}
		
		for(ValueListUtils.Names item : ValueListUtils.mainContentListName){
			mainContentNameList.add(new SelectItem(item.id, item.getField("label")));
		}
		
		for(ValueListUtils.Names item : ValueListUtils.subContentListName){
			subContentNameList.add(new SelectItem(item.id, item.getField("label")));
		}
		// ***** List of List Names ]
		
		personTitleSuggestList.add(new SelectItem("", ApplicationBean.NONE_SELECTED_LABEL));
		for(String title : Person.getTitleList()){
			personTitleSuggestList.add(new SelectItem(title, title));
		}
		
		
		for(Identifier.External item : Identifier.External.values()){
			externalIdEngList.add(new SelectItem(item.id, item.labelEng));
			externalIdGerList.add(new SelectItem(item.id, item.labelGer));
		}
		
		for(Identifier.PersonType item : Identifier.PersonType.values()){
			identifierPersonSuggestList.add(new SelectItem(item.id, item.labelEng));	
		}
		
		for(Identifier.Internal item : Identifier.Internal.values()){
			internalIdEngList.add(new SelectItem(item.id, item.labelEng));
			internalIdGerList.add(new SelectItem(item.id, item.labelGer));	
		}
		
		accessabilityEngList.add(new SelectItem("", ApplicationBean.NONE_SELECTED_LABEL));
		accessabilityEngList.add(new SelectItem("", ApplicationBean.NONE_SELECTED_LABEL));
		for(DataCollection.Accessability item : DataCollection.Accessability.values()){
			accessabilityEngList.add(new SelectItem(item.id, item.labelEng));
			accessabilityGerList.add(new SelectItem(item.id, item.labelGer));
		}

		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.PDF_Dokumente));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Textdokumente));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Rastergrafiken));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Vektorgrafiken));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Tabellen));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Datenbanken));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.GIS));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Statistische_Daten));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Bewegte_Bilder));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Videos));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Audio));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Videos));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.A3_D));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Virtual_Reality));
		fileFormatSuggestList.add(new SelectItem(FileFormat.Type.Präsentationen));
		/*
		for(ActorRole.Type role : ActorRole.Type.values()){
			roleSuggestion.add(new SelectItem(role));
		}
		Collections.sort(roleSuggestion,new SelectItemComparator());
		*/
		for(Gender gender : Person.Gender.values()){
			genderEngList.add(new SelectItem(gender.id, gender.labelEng));
			genderGerList.add(new SelectItem(gender.id, gender.labelGer));
		}
		
		for(Publication.Type item : Publication.Type.values()){
			publicationTypeEngList.add(new SelectItem(item.id, item.labelEng));
			publicationTypeGerList.add(new SelectItem(item.id, item.labelGer));
		}
		
		for(Reference.RelationType item : Reference.RelationType.values()){
			referenceRelationTypeEngList.add(new SelectItem(item.id, item.labelEng));
			referenceRelationTypeGerList.add(new SelectItem(item.id, item.labelGer));
		}
		
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_cited_by.toString(), BOUtils.DaraRelations.is_cited_by.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.cites.toString(), BOUtils.DaraRelations.cites.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_supplement_to.toString(), BOUtils.DaraRelations.is_supplement_to.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_supplemented_by.toString(), BOUtils.DaraRelations.is_supplemented_by.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_continued_by.toString(), BOUtils.DaraRelations.is_continued_by.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.continues.toString(), BOUtils.DaraRelations.continues.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_a_new_version_of.toString(), BOUtils.DaraRelations.is_a_new_version_of.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_a_previous_version_of.toString(), BOUtils.DaraRelations.is_a_previous_version_of.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_a_part_of.toString(), BOUtils.DaraRelations.is_a_part_of.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.has_part.toString(), BOUtils.DaraRelations.has_part.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_referenced_by.toString(), BOUtils.DaraRelations.is_referenced_by.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.references.toString(), BOUtils.DaraRelations.references.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_documented_by.toString(), BOUtils.DaraRelations.is_documented_by	.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.documents.toString(), BOUtils.DaraRelations.documents.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_compiled_by.toString(), BOUtils.DaraRelations.is_compiled_by.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.compiles.toString(), BOUtils.DaraRelations.compiles.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_a_variant_form_of.toString(), BOUtils.DaraRelations.is_a_variant_form_of.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_a_original_form_of.toString(), BOUtils.DaraRelations.is_a_original_form_of.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.has_metadata.toString(), BOUtils.DaraRelations.has_metadata.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_metadata_for.toString(), BOUtils.DaraRelations.is_metadata_for.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_identical_to.toString(), BOUtils.DaraRelations.is_identical_to.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_reviewed_by.toString(), BOUtils.DaraRelations.is_reviewed_by.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.reviews.toString(), BOUtils.DaraRelations.reviews.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_derived_from.toString(), BOUtils.DaraRelations.is_derived_from.toString()));
		daraRelationSuggestion.add(new SelectItem(BOUtils.DaraRelations.is_source_of.toString(), BOUtils.DaraRelations.is_source_of.toString()));
		
		Collections.sort(daraRelationSuggestion,new SelectItemComparator());
		
		for(DataCollection.ProjectStatus item : DataCollection.ProjectStatus.values()){
			projectStatusEngSuggestion.add(new SelectItem(item.id, item.labelEng));
			projectStatusGerSuggestion.add(new SelectItem(item.id, item.labelGer));
		}
	}
	
	private static ApplicationBean app;
	
	public static ApplicationBean getInstance(){
		if(app == null){
			app = new ApplicationBean();
		}
		return app;
	}
	
	public String actionChangeDebugMode(){
		this.debugMode = !this.debugMode;
		return "admin";
	}
	
	public ApplicationBean(){
		logger.info("##### Starting ApplicationBean #####");
		this.loadProperties();
	}
	
	public List<ISOLanguage> searchLanguageInShortListGer(String query){
		//return ISO639_3.getInstance().searchEng(query, 15);
		return LanguageShortList.searchGer(query, 15);
	}
	
	public List<ISOLanguage> searchLanguageInListGer(String query){
		return LanguageList.searchGer(query, 15);
	}
	
	public String getContextRoot(){
		return properties.getProperty("home.url");
	}
	
	public String getDataPortalUrl(){
		return properties.getProperty("dataportal.url");
	}
	
	private String getImageFolder(){
		return getContextRoot() + "/resources/images/";
	} 
	
	public String getContentPath(){
		return properties.getProperty("content.path");
	}
	
	public boolean isDebugMode(){
		return this.debugMode;
	}
	
	public String getStyleButtonCollection(){
		return getStyleButton(getImgCollection());
	}
	
	public String getStyleButtonDelete(){
		return getStyleButton(getImgDelete());
	}
	
	public String getStyleButtonFile(){
		return getStyleButton(getImgFile());
	}
	
	public String getStyleButtonFiles(){
		return getStyleButton(getImgFiles());
	}
	
	public String getStyleButtonUpload(){
		return getStyleButton(getImgUpload());
	}
	
	public String getStyleButtonEdit(){
		return getStyleButton(getImgEdit());
	}
	
	public String getStyleButtonShow(){
		return getStyleButton(getImgShow());
	}
	
	public String getStyleButtonDisplay(){
		return getStyleButton(getImgDisplay());
	}
	
	public String getStyleButtonInformation(){
		return getStyleButton(getImgInformation());
	}
	
	public String getStyleButtonFolder(){
		return getStyleButton(getImgFolder());
	}
	
	private String getStyleButton(String image){
		return "background: transparent url(" + image + ") no-repeat center !important;";
	}
	
	public String getImgWFFinished(){
		return getImageFolder() + "wf_finished_16X16.png";
	}
	
	public String getImgWFFinishedWithErrors(){
		return getImageFolder() + "wf_finished_with_errors_16X16.png";
	}
	
	public String getImgWFNoStarted(){
		return getImageFolder() + "wf_no_started_16X16.png";
	}
	
	public String getImgWFWarning(){
		return getImageFolder() + "wf_warning_16X16.png";
	}
	
	public String getImgWorking(){
		return getImageFolder() + "working.gif";
	}
	
	public String getImgFile(){
		return getImageFolder() + "file_16X16.png";
	}
	
	public String getImgFiles(){
		return getImageFolder() + "files_16X16.png";
	}
	
	public String getImgCollection(){
		return getImageFolder() + "collection_16X16.png";
	}
	
	public String getImgUpload(){
		return getImageFolder() + "upload_16X16.png";
	}
	
	public String getImgDelete(){
		return getImageFolder() + "delete_16X16.png";
	}
	
	public String getImgEdit(){
		return getImageFolder() + "edit_16X16.png";
	}
	
	public String getImgShow(){
		return getImageFolder() + "show_16X16.png";
	}
	
	public String getImgDisplay(){
		return getImageFolder() + "display_20X20.png";
	}
	
	public String getImgHelp(){
		return getImageFolder() + "help_24X24.png";
	}
	
	public String getImgInformation(){
		return getImageFolder() + "information_20X20.png";
	}
	
	public String getImgFolder(){
		return getImageFolder() + "folder_16X16.png";
	}
	
	private void loadProperties(){
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILES);
			properties.load(in);
			in.close();
			
			StringBuilder sb = new StringBuilder("Property's List");
			
			for(Object key : properties.keySet()){
				if(StringUtils.equals(key.toString(), "debug.mode")){
					this.debugMode = Boolean.parseBoolean(properties.getProperty(key.toString()));
				}
				sb.append("\n" + key + "=\t" + properties.getProperty(key.toString()));
			}
			sb.append("\n");
			logger.info(sb.toString());
		} catch (IOException e) {
			logger.error("Properties file \'" + PROPERTIES_FILES + " no found.", e);
			e.printStackTrace();
		}
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
	
	public String getJSConfirmationSave(){
		return "if(!confirm('Do you really want to save the changes?')){ return false; };";
	}
	
	public String getJSConfirmationDelete(){
		return "if(!confirm('Do you really want to detele this?')){ return false; };";
	}
	
	public String getJSConfirmationSaveAsNew(){
		return "if(!confirm('Do you really want to save the entity as new?')){ return false;};";
	}
	
	public String getJSConfirmationCleanForm(){
		return "if(!confirm('Do you really want to clear the form?')){ return false;};";
	}
	
	public List<SelectItem> getGenderEngList() {
		return genderEngList;
	}

	public List<SelectItem> getGenderGerList() {
		return genderGerList;
	}

	public List<SelectItem> getPublicationTypeEngList(){
		return publicationTypeEngList;
	}
	
	public List<SelectItem> getPublicationTypeGerList(){
		return publicationTypeGerList;
	}
	
	public List<SelectItem> getReferenceRelationTypeEngList(){
		return referenceRelationTypeEngList;
	}
	
	public List<SelectItem> getReferenceRelationTypeGerList(){
		return referenceRelationTypeGerList;
	}
	
	public List<SelectItem> getRelatedSourceRepresentationSuggestion(){
		return relatedSourceRepresentationSuggestion;
	}
	
	public List<SelectItem> getDaraRelationSuggestion(){
		return daraRelationSuggestion;
	}
	
	public List<SelectItem> getAlternativeRepresentationType(){
		return alternativeRepresentationType;
	}
	
	public List<SelectItem> getProjectStatusEngSuggestion(){
		return projectStatusEngSuggestion;
	}
	
	public List<SelectItem> getProjectStatusGerSuggestion(){
		return projectStatusGerSuggestion;
	}
	
	public List<SelectItem> getFileFormatSuggestList(){
		return fileFormatSuggestList;
	}
	
	public List<SelectItem> getAccessabilityEngList(){
		return accessabilityEngList;
	}
	
	public List<SelectItem> getAccessabilityGerList(){
		return accessabilityGerList;
	}
	
	public List<SelectItem> getExternalIdEngList(){
		return externalIdEngList;
	}
	
	public List<SelectItem> getExternalIdGerList(){
		return externalIdGerList;
	}
	
	public List<SelectItem> getIdentifierPersonSuggestList(){
		return identifierPersonSuggestList;
	}
	
	public List<SelectItem> getPersonTitleSuggestList(){
		return personTitleSuggestList;
	}
	/*
	public List<SelectItem> getPublicationPidList(){
		return publicationPidList;
	}
	*/
	public List<SelectItem> getPlaceIdentifierList(){
		return placeIdentifierList;
	}
	
	public List<SelectItem> getReportStateList(){
		return reportStateList;
	}
	/*
	public List<SelectItem> getSubDisciplineList(){
		return subDisciplineList;
	}*/
	
	public List<SelectItem> getClassificationEngList(){
		return classificationEngList;
	}
	
	public List<SelectItem> getClassificationGerList(){
		return classificationGerList;
	}
	
	public List<SelectItem> getDataCategoryEngList(){
		return dataCategoryEngList;
	}
	
	public List<SelectItem> getDataCategoryGerList(){
		return dataCategoryGerList;
	}

	public  List<SelectItem> getRightTypeEngList() {
		return rightTypeEngList;
	}

	public  List<SelectItem> getRightTypeGerList() {
		return rightTypeGerList;
	}

	public  List<SelectItem> getGroupTypeEngList() {
		return groupTypeEngList;
	}

	public List<SelectItem> getGroupTypeGerList() {
		return groupTypeGerList;
	}

	public List<SelectItem> getResourceTypeEngList() {
		return resourceTypeEngList;
	}

	public List<SelectItem> getResourceTypeGerList() {
		return resourceTypeGerList;
	}

	public List<SelectItem> getReasonDataProtectionEngList() {
		return reasonDataProtectionEngList;
	}

	public List<SelectItem> getReasonDataProtectionGerList() {
		return reasonDataProtectionGerList;
	}

	public List<SelectItem> getMainMethodEngList() {
		return mainMethodEngList;
	}
	
	public List<SelectItem> getMainMethodGerList() {
		return mainMethodGerList;
	}
	/*
	public List<SelectItem> getIanusSubMethodList() {
		return ianusSubMethodList;
	}*/
	
	public List<SelectItem> getSubMethodNameList() {
		return subMethodNameList;
	}
	
	public List<SelectItem> getMainContentAdexTypeEngList(){
		return mainContentAdexTypeEngList;
	}
	
	public List<SelectItem> getMainContentAdexTypeGerList(){
		return mainContentAdexTypeGerList;
	}
	
	public List<SelectItem> getMainContentNameList(){
		return mainContentNameList;
	}
	
	public List<SelectItem> getSubContentNameList(){
		return subContentNameList;
	}
	
	public List<SelectItem> getMainDisciplineEngList(){
		return mainDisciplineEngList;
	}
	
	public List<SelectItem> getMainDisciplineGerList(){
		return mainDisciplineGerList;
	}
	
	public List<SelectItem> getSubDisciplineEngList(){
		return subDisciplineEngList;
	}
	
	public List<SelectItem> getSubDisciplineGerList(){
		return subDisciplineGerList;
	}
	
	public List<SelectItem> getInternalIdEngList(){
		return internalIdEngList;
	}
	
	public List<SelectItem> getInternalIdGerList(){
		return internalIdGerList;
	}

	public List<SelectItem> getSubMethodEngList() {
		return subMethodEngList;
	}

	public List<SelectItem> getSubMethodGerList() {
		return subMethodGerList;
	}
	
	public List<SelectItem> getUserRoleList(){
		return userRoleList;
	}
	
}
