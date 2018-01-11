package de.ianus.ingest.web.pages;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.utils.Identifier;

public class CollectionIdPage extends IANUSEntityPage{
	
	public static final String PAGE_NAME = "collectionId";
	
	public static List<SelectItem> idTypeList = new ArrayList<SelectItem>();
	
	static{
		idTypeList.add(new SelectItem(Identifier.Internal.ianus_collno.id, "IANUS collection number"));
		idTypeList.add(new SelectItem(Identifier.Internal.ianus_mdrecord.id, "IANUS metadata record"));
	}
	
	private String selectedIdType = Identifier.Internal.ianus_collno.id;
	private Identifier collectionId;
	
	public void listenerChangeIdType(){
		System.out.println("########## " + this.selectedIdType);
		this.collectionId = new Identifier();
	}
	
	public void loadDataCollection(WorkflowIP ip, Long dcId) throws Exception{
		this.ip = ip;
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(dcId);
		this.load(dc, dc);
		
		this.collectionId = loadCollectionId();
	}
	
	/**
	 * This method searches for the first collection ID (Internal Identifier, whose Type is: Identifier.Intern.ianus_collno).
	 * @return
	 */
	private Identifier loadCollectionId(){
		for(Identifier item : getDc().getInternalIdList()){
			if(StringUtils.equals(item.getType(), Identifier.Internal.ianus_collno.id) ||
					StringUtils.equals(item.getType(), Identifier.Internal.ianus_mdrecord.id)){
				this.selectedIdType = item.getType();
				return item;
			}
		}
		return new Identifier();
	}
	
	public Identifier getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Identifier collectionId) {
		this.collectionId = collectionId;
	}

	public String actionSave(){
		
		try {
			
			this.collectionId.setInstitution("IANUS");
			this.collectionId.setSource(getSource());
			this.collectionId.setType(this.selectedIdType);
			this.collectionId.setContentType(Identifier.ContentType.internal_id);
			
			Services.getInstance().getMDService().saveEntry(collectionId);
			
			for(Identifier item : getDc().getInternalIdList()){
				if((StringUtils.equals(item.getType(), Identifier.Internal.ianus_collno.id) ||
					StringUtils.equals(item.getType(), Identifier.Internal.ianus_mdrecord.id))
						&&
					!this.collectionId.getId().equals(item.getId())){
					
					Services.getInstance().getMDService().deleteDBEntry(item);
				}
			}
			
			this.getSession().updateCollectionTitelInIP(getDc());
			this.loadDataCollection(this.ip, getDc().getId());
			
		} catch (Exception e) {
			addInternalError(e);
		}
		
		return PAGE_NAME;
	}
	
	public String getSelectedIdType() {
		return selectedIdType;
	}

	public void setSelectedIdType(String selectedIdType) {
		this.selectedIdType = selectedIdType;
	}

	public List<SelectItem> getIdTypeList(){
		return idTypeList;
	}
}
