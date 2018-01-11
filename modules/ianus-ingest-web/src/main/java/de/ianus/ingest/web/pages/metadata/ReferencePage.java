package de.ianus.ingest.web.pages.metadata;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.Reference;

public class ReferencePage extends DcComponentPage{

	public static String PAGE_NAME = "reference";
	
	private Reference reference;
	private Publication publication;
	
	public void load(DataCollection dcBase, IANUSEntity source, Reference reference, Publication publication) throws Exception{
		this.setDcBase(dcBase);
		this.setSource(source);
		this.reference = reference;
		this.publication = publication;
	}
	
	public void load(DataCollection dcBase, IANUSEntity source, Reference reference) throws Exception{
		this.setDcBase(dcBase);
		this.setSource(source);
		this.reference = reference;
		if(reference.isPersistent()){
			this.publication = (Publication)Services.getInstance().getMDService().getPublication(reference.getPublicationId());
		}else{
			this.publication = null;
		}
	}
	
	public String actionAddReference(){
		try {
			this.load(this.getDcBase(), this.getSource(), new Reference(), null);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCancelReferenceEdition(){
		addMsg("TODO");
		return PAGE_NAME;
	}
	
	public String actionSearchPublication(){
		try {
			getSession().getPublicationPage().load(this.getDcBase(), this.getSource(), this.reference, this.publication);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PublicationPage.PAGE_NAME;
	}
	
	public String actionNewPublication(){
		try {
			this.publication = new Publication();
			getSession().getPublicationPage().load(this.getDcBase(), this.getSource(), this.reference, this.publication);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PublicationPage.PAGE_NAME;
	}
	
	public String actionEditPublication(){
		try {
			getSession().getPublicationPage().load(this.getDcBase(), this.getSource(), this.reference, this.publication);
		} catch (Exception e) {
			addInternalError(e);
		}
		
		return PublicationPage.PAGE_NAME;
	}
	
	public String actionSaveReference(){
		try {
			this.reference.setSource(this.getSource());
			this.reference.setPublication(this.publication);
			if(this.reference.isCompleted()){
				Services.getInstance().getMDService().saveEntry(this.reference);
				this.getSource().getReferenceList().add(this.reference);
			}else{
				addMsg("The reference has no publication associated");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public String actionCancelReference(){
		return PAGE_NAME;
	}
	
	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
	}
}
