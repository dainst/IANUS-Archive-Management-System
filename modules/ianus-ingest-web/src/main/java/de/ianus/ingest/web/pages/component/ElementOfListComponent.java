package de.ianus.ingest.web.pages.component;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.ingest.web.ApplicationBean;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.Place;

/**
 * 
 * @author jurzua
 *
 */
public class ElementOfListComponent extends AbstractBean{

	private ElementOfList item = new ElementOfList();
	
	private MDEntry source;
	private ElementOfList.ContentType contentType;
	
	public ElementOfListComponent(MDEntry source, ElementOfList.ContentType contentType){
		this.source = source;
		this.contentType = contentType;
	}
	
	public void listenerSave(ActionEvent event){
		if(this.item.isCompleted() && !StringUtils.equals(this.item.getListId(), ApplicationBean.NONE_SELECTED_ID)){
			
			try {
				
				this.item.setSource(source);
				this.item.setContentType(contentType);
				Services.getInstance().getMDService().saveEntry(this.item);
				
				if(source instanceof DataCollection){
					
					if(ElementOfList.ContentType.mainDiscipline.equals(this.contentType)){
						((DataCollection) this.source).getMainDisciplineList().add(this.item);
					}else if(ElementOfList.ContentType.subDiscipline.equals(this.contentType)){
						((DataCollection) this.source).getSubDisciplineList().add(this.item);
					}else if(ElementOfList.ContentType.mainContent.equals(this.contentType)){
						((DataCollection) this.source).getMainContentList().add(this.item);
					}else if(ElementOfList.ContentType.subContent.equals(this.contentType)){
						((DataCollection) this.source).getSubContentList().add(this.item);
					}else if(ElementOfList.ContentType.mainMethod.equals(this.contentType)){
						((DataCollection) this.source).getMainMethodList().add(this.item);
					}else if(ElementOfList.ContentType.subMethod.equals(this.contentType)){
						((DataCollection) this.source).getSubMethodList().add(this.item);
					}
					/*
					if(ElementOfList.ContentType.discipline.equals(this.contentType)){
						((DataCollection) this.source).getDisciplineList().add(this.item);
					}else if(ElementOfList.ContentType.contentFine.equals(this.contentType)){
						((DataCollection) this.source).getContentFineList().add(this.item);
					}else if(ElementOfList.ContentType.contentRough.equals(this.contentType)){
						((DataCollection) this.source).getContentRoughList().add(this.item);
					}else if(ElementOfList.ContentType.methodFine.equals(this.contentType)){
						((DataCollection) this.source).getMethodFineList().add(this.item);
					}else if(ElementOfList.ContentType.methodRough.equals(this.contentType)){
						((DataCollection) this.source).getMethodRoughList().add(this.item);
					}	*/
				}else if(source instanceof Place){
					if(ElementOfList.ContentType.identifier.equals(this.contentType)){
						((Place) this.source).getIdentifierList().add(this.item);
					}
				/*}else if(source instanceof Publication){
					if(ElementOfList.ContentType.pid.equals(this.contentType)){
						((Publication) this.source).getPidList().add(this.item);
					}*/
				}else if(source instanceof Person){
					if(ElementOfList.ContentType.subDiscipline.equals(this.contentType)){
						((Person) this.source).getSubDisciplineList().add(this.item);
					}
				}else {
					throw new Exception("This method has not be implemented for the SourceClass " + item.getSourceClass().toString());
				}
				
				
				this.item = new ElementOfList();
				this.item.setListId(ApplicationBean.NONE_SELECTED_ID);
			} catch (Exception e) {
				addInternalError(e);
			}
		}else{
			addMsg("Neither the value, the value id nor the list id can be empty");
		}
	}
	
	public void listenerReset(ActionEvent event){
		this.item = new ElementOfList();
		this.item.setListId(ApplicationBean.NONE_SELECTED_ID);
	}
	
	public void listenerDelete(ActionEvent event){
		this.delete();
	}
	
	private void delete(){
		try {
			
			ElementOfList item0 = (ElementOfList)getRequestBean(contentType.toString());
			
			if((BOUtils.SourceClass.DataCollection.equals( item0.getSourceClass()))){
				
				if(ElementOfList.ContentType.mainDiscipline.equals(this.contentType)){
					((DataCollection) this.source).getMainDisciplineList().remove(item0);
				}else if(ElementOfList.ContentType.subDiscipline.equals(this.contentType)){
					((DataCollection) this.source).getSubDisciplineList().remove(item0);
				}else if(ElementOfList.ContentType.mainContent.equals(this.contentType)){
					((DataCollection) this.source).getMainContentList().remove(item0);
				}else if(ElementOfList.ContentType.subContent.equals(this.contentType)){
					((DataCollection) this.source).getSubContentList().remove(item0);
				}else if(ElementOfList.ContentType.mainMethod.equals(this.contentType)){
					((DataCollection) this.source).getMainMethodList().remove(item0);
				}else if(ElementOfList.ContentType.subMethod.equals(this.contentType)){
					((DataCollection) this.source).getSubMethodList().remove(item0);
				}
				
			}else if((BOUtils.SourceClass.Place.equals( item0.getSourceClass()))){
				if(ElementOfList.ContentType.identifier.equals(this.contentType)){
					((Place) this.source).getIdentifierList().remove( item0);
				}
			}else if((BOUtils.SourceClass.Publication.equals( item0.getSourceClass()))){
				if(ElementOfList.ContentType.pid.equals(this.contentType)){
					((Publication) this.source).getPidList().remove( item0);
				}
			}else if((BOUtils.SourceClass.Person.equals( item0.getSourceClass()))){
				if(ElementOfList.ContentType.subDiscipline.equals(this.contentType)){
					((Person) this.source).getSubDisciplineList().remove( item0);
				}
			}else {
				throw new Exception("This method has not be implemented for the SourceClass " +  item0.getSourceClass().toString());
			}
			Services.getInstance().getMDService().deleteDBEntry(item0);
			
		} catch (Exception e) {
			addInternalError(e);
		}
	}

	public ElementOfList getItem() {
		return item;
	}

	public void setItem(ElementOfList item) {
		this.item = item;
	}	
}
