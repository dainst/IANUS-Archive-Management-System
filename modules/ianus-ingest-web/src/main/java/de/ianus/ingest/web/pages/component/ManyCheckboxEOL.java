package de.ianus.ingest.web.pages.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.utils.Classification;
import de.ianus.metadata.bo.utils.DataCategory;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.utils.AbstractValueList;
import de.ianus.metadata.utils.DisciplineList;

/**
 * 
 * @author jurzua
 *
 */
public class ManyCheckboxEOL extends AbstractBean{
	
	private MDEntry source;
	private ElementOfList.ContentType contentType;
	
	private String listNameId;
	//private String value;
	//private String valueId;
	
	private List<String> idList = new ArrayList<String>();
	
	public ManyCheckboxEOL(MDEntry source, String listNameId, ElementOfList.ContentType contentType){
		this.source = source;
		this.listNameId = listNameId;
		this.contentType = contentType;
		this.load();
	}
	
	public void load(){
		this.idList = new ArrayList<String>();
		Set<ElementOfList> eolList = null;
		if(this.source instanceof DataCollection){
			eolList = ((DataCollection)this.source).getEOLList(this.contentType);
		}else if(this.source instanceof Person){
			eolList = ((Person)this.source).getSubDisciplineList();
		}
		
		for(ElementOfList eol : eolList){
			this.idList.add(eol.getValueId());
		}
	}
	
	private String getEOLValueGer(String id) throws Exception{
		if(this.contentType.equals(ElementOfList.ContentType.dataCategory)){
			return DataCategory.Type.getLabelGer(id);
		}else if(this.contentType.equals(ElementOfList.ContentType.classification)){
			return Classification.Type.getLabelGer(id);	
		}else if(this.contentType.equals(ElementOfList.ContentType.mainDiscipline)){
			return DisciplineList.MainDiscipline.getLabelGer(id);	
		}else if(this.contentType.equals(ElementOfList.ContentType.subDiscipline)){
			return DisciplineList.SubDiscipline.getLabelGer(id);	
		}
		
		throw new Exception("Method not implemented for ElementOfList.ContentType = " + this.contentType);
	}
	
	private String getEOLUrl(String id) throws Exception {
		if(this.contentType.equals(ElementOfList.ContentType.dataCategory)){
			return DataCategory.Type.getById(id).url;
		}else if(this.contentType.equals(ElementOfList.ContentType.classification)){
			return Classification.Type.getById(id).url;
		}else if(this.contentType.equals(ElementOfList.ContentType.mainDiscipline)){
			return DisciplineList.MainDiscipline.getById(id).url;
		}else if(this.contentType.equals(ElementOfList.ContentType.subDiscipline)){
			return DisciplineList.SubDiscipline.getById(id).url;
		}
		
		throw new Exception("Method not implemented for ElementOfList.ContentType = " + this.contentType);
	}
	
	public void save() throws Exception{
		for(String id : this.idList){
			//we create the element, if the DC does not contain it
			if(!containsId(id) && !AbstractValueList.isIgnoreId(id)){
				ElementOfList eol = new ElementOfList();
				//eol.setListId(ValueListUtils.Names.ianus_data_category.id);
				eol.setListId(this.listNameId);
				eol.setValueId(id);
				eol.setValue(getEOLValueGer(id));
				eol.setSource(this.source);
				eol.setContentType(this.contentType);
				eol.setUri(this.getEOLUrl(id));
				Services.getInstance().getMDService().saveEntry(eol);
			}
		}
		Set<ElementOfList> eolList = this.getEOLList();
		for(ElementOfList eol : eolList){
			if(!this.idList.contains(eol.getValueId())){
				//delete the eol if the user does not select the value
				Services.getInstance().getMDService().deleteDBEntry(eol);
			}
		}
	}
	
	private Set<ElementOfList> getEOLList() throws Exception{
		if(this.source instanceof DataCollection){
			return ((DataCollection)this.source).getEOLList(this.contentType);
		}else if(source instanceof Person){
			return ((Person)this.source).getSubDisciplineList();
		}
		throw new Exception("Method not implemented for class = " + this.source.getClass().getName());
	}
	
	private boolean containsId(String id){
		if(this.source instanceof DataCollection){
			return ((DataCollection)this.source).containsEOL(this.contentType, this.listNameId, id);
		}else if(this.source instanceof Person){
			return ((Person)this.source).containsSubDiscipline(this.listNameId, id);
		}
		return false;
	}

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}
}

