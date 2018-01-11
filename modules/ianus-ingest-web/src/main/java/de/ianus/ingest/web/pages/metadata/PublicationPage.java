package de.ianus.ingest.web.pages.metadata;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.utils.BOUtils.SourceClass;
import de.ianus.metadata.bo.utils.Identifier;

public class PublicationPage extends DcComponentPage{

	private static final Logger logger = LogManager.getLogger(PublicationPage.class);
	public static String PAGE_NAME = "publication";
	
	
	private String searchTerm;
	private List<Publication> publicationList;
	
	
	private Reference reference = null;
	private Publication publication;
	
	private Identifier pid = new Identifier();
	private Set<Identifier> currentPidList = new LinkedHashSet<Identifier>();
	
	
	private Publication deletePublication;
	private List<Reference> relatedReferenceList;
	
	
	public String actionWantToDeletePublication(){
		try {
			this.deletePublication = (Publication) getRequestBean("publication");
			if(this.deletePublication != null){
				this.relatedReferenceList = Services.getInstance().getMDService().getReferenceListRelated2Publication(this.deletePublication.getId());
				for(Reference ref : this.relatedReferenceList){
					if(ref.getSourceClass().equals(SourceClass.DataCollection)){
						//DataCollection dc = Services.getInstance().getMDService().getDataCollection(ref.getSourceId());
						List<WorkflowIP> list = Services.getInstance().getDaoService().getWorkflowPackageList(ref.getSourceId(), null);
						if(!list.isEmpty() && list.get(0) instanceof TransferP){
							ref.setSourceLabel(((TransferP)list.get(0)).getCollectionLabel());
						}
					}else if(ref.getSourceClass().equals(SourceClass.CollectionFile)){
						logger.error("This method should implemented");
					}
				}
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeletePublication(){
		try {
			Services.getInstance().getMDService().deleteDBEntry(deletePublication);
			for(Reference ref : this.relatedReferenceList){
				Services.getInstance().getMDService().deleteDBEntry(ref);
			}
			this.deletePublication = null;
			this.relatedReferenceList = null;
			
			this.reload();
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCancelDeletePublication(){
		this.deletePublication = null;
		this.relatedReferenceList = null;
		return PAGE_NAME;
	}
	
	public boolean isShowPublicationTable(){
		return this.publicationList != null && !this.publicationList.isEmpty() && this.publication == null;
	}
	
	public String actionUpdateIndices(){
		Services.getInstance().getMDService().updateIndices();
		return PAGE_NAME;
	}
	
	public String actionSearch(){
		try {
			this.publicationList = Services.getInstance().getMDService().searchPublication(this.searchTerm);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionGetAllPublications(){
		try {
			this.publicationList = Services.getInstance().getMDService().getPublicationList();
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionBack2ReferencePage(){
		try {
			getSession().getReferencePage().load(this.getDcBase(), this.getSource(), this.reference);
		} catch (Exception e) {
			addInternalError(e);
		}
		return ReferencePage.PAGE_NAME;
	}
	
	public String actionBack2MetadataReferencePage(){
		try {
			getSession().getReferencePage().load(getDcBase(), getSource(), getReference());
			return ReferencePage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private void reload() throws Exception{
		this.load(getDcBase(), getSource(), getReference(), getPublication());
	}
	
	public void load(DataCollection dcBase, IANUSEntity source, Reference reference, Publication publication) throws Exception{
		logger.info("dcBase=" + dcBase + ", source=" + source + ", reference=" + reference);
		this.publication = publication;
		this.reference = reference;
		this.loadSource(source);
		this.setDcBase(dcBase);
		this.searchTerm = new String();
		this.pid = new Identifier();
		this.publicationList = new ArrayList<Publication>();
		
		if(publication != null){
			this.currentPidList = (publication.isPersistent()) ? 
					new LinkedHashSet<Identifier>(this.publication.getPidList()) :
						new LinkedHashSet<Identifier>();
		}
	}
	
	public String actionAddPid(){
		this.getCurrentPidList().add(this.pid);
		this.pid = new Identifier();
		return PAGE_NAME;
	}
	
	public void listenerDeletePid(ActionEvent event){
		Identifier identifier = (Identifier)getRequestBean("pid");
		this.currentPidList.remove(identifier);
	}
	
	public void listenerEditPid(ActionEvent event){
		Identifier identifier = (Identifier)getRequestBean("pid");
		this.pid = identifier;
	}
	
	public void listenerCancelPidEdition(ActionEvent event){
		this.pid = new Identifier();
	}
	
	private void loadSource(IANUSEntity source) throws Exception{
		this.setSource(source);
		//this.pid = new ElementOfListComponent(this.publication, ElementOfList.ContentType.pid);
	}
	
	public String actionAddPublication(){
		try {
			this.load(this.getDcBase(), this.getSource(), this.reference, new Publication());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionLoadIntoReference(){
		try {
			if(publication.isPersistent()){
				getSession().getReferencePage().load(this.getDcBase(), this.getSource(), this.reference, this.publication);
			}else{
				addMsg("You should be saved the publication to be used by a reference");
				return PAGE_NAME;
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return ReferencePage.PAGE_NAME;
	}
	
	public String actionLoadIntoReferenceFromList(){
		try {
			Publication pub = (Publication)getRequestBean("publication");
			getSession().getReferencePage().load(this.getDcBase(), this.getSource(), this.reference, pub);
		} catch (Exception e) {
			addInternalError(e);
		}
		return ReferencePage.PAGE_NAME; 
	}
	
	public String actionSavePublication(){
		try {
			
			Services.getInstance().getMDService().saveEntry(publication);
			this.savePidList();
			addMsg("The publication has been saved [id=" + this.publication.getId() + "]");
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private void savePidList() throws Exception{
		for(Identifier id : this.currentPidList){
			id.setContentType(Identifier.ContentType.pid);
			id.setSource(this.publication);
			Services.getInstance().getMDService().saveEntry(id);
			this.publication.getPidList().add(id);
		}
		
		for(Identifier id : this.publication.getPidList()){
			if(!this.currentPidList.contains(id)){
				Services.getInstance().getMDService().deleteDBEntry(id);
			}
		}
	}
	
	public String actionEditPublication(){
		try {
			Publication pub0 = (Publication)getRequestBean("publication");
			this.publication = Services.getInstance().getMDService().getPublication(pub0.getId());
			this.load(getDcBase(), getSource(), getReference(), getPublication());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public Publication getPublication() {
		return publication;
	}
	
	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public List<Publication> getPublicationList() {
		return publicationList;
	}

	public void setPublicationList(List<Publication> publicationList) {
		this.publicationList = publicationList;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public Set<Identifier> getCurrentPidList() {
		return currentPidList;
	}

	public void setCurrentPidList(Set<Identifier> currentPidList) {
		this.currentPidList = currentPidList;
	}

	public Identifier getPid() {
		return pid;
	}

	public void setPid(Identifier pid) {
		this.pid = pid;
	}

	public Publication getDeletePublication() {
		return deletePublication;
	}

	public void setDeletePublication(Publication deletePublication) {
		this.deletePublication = deletePublication;
	}

	public List<Reference> getRelatedReferenceList() {
		return relatedReferenceList;
	}

	public void setRelatedReferenceList(List<Reference> relatedReferenceList) {
		this.relatedReferenceList = relatedReferenceList;
	}

	
}
