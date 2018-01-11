package de.ianus.ingest.web.pages.metadata;

import de.ianus.ingest.web.pages.component.DcComponentPage;

/**
 * TODO: this page should be removed, because AlternativeRepresentation is not more part of the datamodel
 * 
 * @author jurzua
 *
 */
public class AlternativeRepresentationPage extends DcComponentPage{

	public static String PAGE_NAME = "alternativeRepresentation";
	/*
	private AlternativeRepresentation alternativeRepresentation;

	public void load(DataCollection dcBase, IANUSEntity source, AlternativeRepresentation rs){
		super.load(dcBase, source);
		this.alternativeRepresentation = rs;
	}
	
	public String actionShowAll(){
		addMsg("TODO");
		return PAGE_NAME;
	}
	
	public String actionAdd(){
		this.load(this.getDcBase(), this.getSource(), new AlternativeRepresentation());
		return PAGE_NAME;
	}
	
	public String actionSave(){
		try {
			this.alternativeRepresentation.setSource(getDcSource());
			WebServices.getInstance().getMDService().saveEntry(this.alternativeRepresentation);
			this.getDcSource().getAlternativeRepresentationList().add(this.alternativeRepresentation);
			this.load(this.getDcBase(), this.getSource(), this.alternativeRepresentation);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public AlternativeRepresentation getAlternativeRepresentation() {
		return alternativeRepresentation;
	}

	public void setAlternativeRepresentation(AlternativeRepresentation alternativeRepresentation) {
		this.alternativeRepresentation = alternativeRepresentation;
	}
	*/
}
