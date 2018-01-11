package de.ianus.ingest.web.pages.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.ApplicationBean;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.resource.RelatedResource;
import de.ianus.metadata.bo.utils.Identifier;

public class RelatedResourcePage extends DcComponentPage{
	public static String PAGE_NAME = "relatedResource";
	
	public static String FREE_TEXT_GER = "freier Text";
	
	private RelatedResource relatedResource;
	
	private static List<SelectItem> identifierList = new ArrayList<SelectItem>();
	private static List<SelectItem> resourceTypeList = new ArrayList<SelectItem>();
	
	private static List<SelectItem> relationTypeEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> relationTypeGerList = new ArrayList<SelectItem>();
	
	public String selectedRelationType = RelatedResource.RelationType.dcms_IsSupplementTo.labelGer;
	public String freeTextRelationType;
	
	static{
		relationTypeEngList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		relationTypeGerList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		
		relationTypeGerList.add(new SelectItem(FREE_TEXT_GER));
		for(RelatedResource.RelationType item : RelatedResource.RelationType.values()){
			relationTypeGerList.add(new SelectItem(item.id, item.labelGer));
			relationTypeEngList.add(new SelectItem(item.id, item.labelEng));
		}
		
		identifierList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(Identifier.External item : Identifier.External.values()){
			identifierList.add(new SelectItem(item.id, item.labelGer));
		}
		
		resourceTypeList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(RelatedResource.ResourceType item : RelatedResource.ResourceType.values()){
			resourceTypeList.add(new SelectItem(item.toString()));
		}
	}
	
	
	public void load(DataCollection dcBase, IANUSEntity source, RelatedResource relatedResource) throws Exception{
		super.load(dcBase, source);
		this.relatedResource = relatedResource;
	}
	
	public String actionSave(){
		try {
			
			this.relatedResource.setRelationType((StringUtils.equals(this.selectedRelationType, FREE_TEXT_GER))? this.freeTextRelationType : this.selectedRelationType);
			this.relatedResource.setSource(this.getSource());
			Services.getInstance().getMDService().saveEntry(relatedResource);
			
			this.relatedResource.getIdentifier().setSource(this.relatedResource);
			this.relatedResource.getIdentifier().setContentType(Identifier.ContentType.id);
			Services.getInstance().getMDService().saveEntry(relatedResource.getIdentifier());
			
			this.relatedResource.getInstitution().setSource(this.relatedResource);
			Services.getInstance().getMDService().saveEntry(relatedResource.getInstitution());
			
			this.getSource().getRelatedResourceList().add(this.relatedResource);
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public boolean isShowRelationTypeFreeText(){
		return StringUtils.equals(this.selectedRelationType, FREE_TEXT_GER);
	}
	
	public RelatedResource getRelatedResource() {
		return relatedResource;
	}

	public void setRelatedResource(RelatedResource relatedResource) {
		this.relatedResource = relatedResource;
	}
	
	public List<SelectItem> getRelationTypeEngList(){
		return relationTypeEngList;
	}
	
	public List<SelectItem> getRelationTypeGerList(){
		return relationTypeGerList;
	}
	
	public List<SelectItem> getIdentifierList(){
		return identifierList;
	}
	
	public List<SelectItem> getResourceTypeList(){
		return resourceTypeList;
	}

	public String getSelectedRelationType() {
		return selectedRelationType;
	}

	public void setSelectedRelationType(String selectedRelationType) {
		this.selectedRelationType = selectedRelationType;
	}

	public String getFreeTextRelationType() {
		return freeTextRelationType;
	}

	public void setFreeTextRelationType(String freeTextRelationType) {
		this.freeTextRelationType = freeTextRelationType;
	}
	
	
	
}
