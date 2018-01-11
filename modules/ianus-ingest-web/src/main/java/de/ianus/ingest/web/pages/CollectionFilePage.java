package de.ianus.ingest.web.pages;

import java.util.LinkedHashSet;
import java.util.Set;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.pages.component.ElementOfListComponent;
import de.ianus.ingest.web.pages.component.TextAttributeComponent;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.metadata.bo.CollectionFile;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.Software;
import de.ianus.metadata.bo.utils.TextAttribute;

public class CollectionFilePage extends IANUSEntityPage{
	
	public static String PAGE_NAME = "collectionFile";
	
	private Set<CollectionFile> fileList = new LinkedHashSet<CollectionFile>();
	private Software software = new Software();
	
	//File editing
	private TextAttributeComponent shortDescription;
	private TextAttributeComponent longDescription;
	private ElementOfListComponent keyword;
	
	@Override
	public String toThisPage(){
		return PAGE_NAME;
	}
	
	
	/*
	private Set<Actor> getActorList(ActorRole.Type role){
		Set<Actor> list = new LinkedHashSet<Actor>();
		for(ActorRole r0 : this.getSource().getRoleList()){
			if(role.equals(r0.getType())){
				list.add(this.getDcBase().getActorMap().get(r0.getActorId()));
			}
		}
		return list;
	}*/
	
	public void loadFile(CollectionFile file){
		try {
			this.load(getDcBase(), (IANUSEntity)file);
			this.shortDescription = new TextAttributeComponent(this.getFile(), TextAttribute.ContentType.shortDescription, PAGE_NAME, true, true);
			this.longDescription = new TextAttributeComponent(this.getFile(), TextAttribute.ContentType.longDescription, PAGE_NAME, true, true);
			this.keyword = new ElementOfListComponent(this.getFile(), ElementOfList.ContentType.keyword);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionAddSoftware(){
		try {
			this.software.setSource(this.getFile());
			Services.getInstance().getMDService().saveEntry(software);
			this.getFile().getSoftwareList().add(software);
			this.software = new Software();
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeleteSoftware(){
		try {
			Software software0 = (Software)getRequestBean("software");
			this.getFile().getSoftwareList().remove(software0);
			Services.getInstance().getMDService().deleteDBEntry(software0);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionResetSoftware(){
		this.software = null;
		return PAGE_NAME;
	}

	public void loadDataCollection(DataCollection dc){
		try {
			super.load(dc, dc);
			this.fileList = Services.getInstance().getMDService().getCollectionFileList(getSource().getId());
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionAddFile(){
		try {
			saveFile(new CollectionFile());;
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private void saveFile(CollectionFile file) throws Exception{
		file.setSource(getDcBase());
		Services.getInstance().getMDService().saveEntry(file);
		this.loadFile(file);
	}
	
	public String actionSaveFile(){
		try {
			this.saveFile(this.getFile());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCancelEdition(){
		this.setSource(null);
		return PAGE_NAME;
	}
	
	public String actionSelectFile(){
		CollectionFile file0 = (CollectionFile)getRequestBean("file");
		this.loadFile(file0);
		return PAGE_NAME;
	}
	
	public String actionBack2MetadataDcPage(){
		getSession().getDataCollectionPage().load(getDcBase(), getDcBase());
		return DataCollectionPage.PAGE_NAME;
	}
	
	public boolean isShowFileTable(){
		return getSource() == null && !fileList.isEmpty();
	}
	
	public Set<CollectionFile> getFileList() {
		return fileList;
	}

	public void setFileList(Set<CollectionFile> fileList) {
		this.fileList = fileList;
	}

	public TextAttributeComponent getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(TextAttributeComponent shortDescription) {
		this.shortDescription = shortDescription;
	}

	public TextAttributeComponent getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(TextAttributeComponent longDescription) {
		this.longDescription = longDescription;
	}

	public ElementOfListComponent getKeyword() {
		return keyword;
	}

	public void setKeyword(ElementOfListComponent keyword) {
		this.keyword = keyword;
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}
}
