package de.ianus.ingest.web.pages.component;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.metadata.bo.CollectionFile;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.Time;

public class TextAttributeComponent extends AbstractBean{
	
	private TextAttribute textAtt = new TextAttribute();
	
	//This attribute is used when we want to edit a textAtt. 
	//The persistent instance will not be touched, but it will be stored in this variable
	private TextAttribute textAttPersistent = null;
	
	private MDEntry source;
	private TextAttribute.ContentType contentType;
	private String pageName;
	private boolean uniqueLanguage = false;
	private boolean useLanguage = true; 
	
	
	public TextAttributeComponent(MDEntry source, TextAttribute.ContentType contentType, String pageName){
		this.source = source;
		this.contentType = contentType;
		this.pageName = pageName;
		this.uniqueLanguage = false;
	}
	
	public TextAttributeComponent(MDEntry source, TextAttribute.ContentType contentType, String pageName, boolean useLanguage, boolean uniqueLanguage){
		this.source = source;
		this.contentType = contentType;
		this.pageName = pageName;
		this.uniqueLanguage = uniqueLanguage;
		this.useLanguage = useLanguage;
	}
	
	private BOUtils.SourceClass getSourceClass() throws Exception{
		if(source instanceof Place)
			return BOUtils.SourceClass.Place;
		if(source instanceof DataCollection)
			return BOUtils.SourceClass.DataCollection;
		if(source instanceof Person)
			return BOUtils.SourceClass.Person;
		if(source instanceof Institution)
			return BOUtils.SourceClass.Institution;
		if(source instanceof Time)
			return BOUtils.SourceClass.Time;
		throw new Exception("The class " + this.source.getClass().getSimpleName() + " is not supported by this method");
	}
	
	public void listenerAdd(ActionEvent event){
		this.actionAdd();
	}
	
	public void saveTextAttribute(String currentLanguage){
		this.textAtt.setLanguageCode(currentLanguage);
		try {
			if(this.textAtt.isCompleted(useLanguage)){
				Set<TextAttribute> attList = getAttList();
				if(this.uniqueLanguage && existLang(attList, this.textAtt) && !this.textAtt.isPersistent()){
					addMsg("There is already an element in this language");
				}else{
					this.textAtt.setSourceClass(getSourceClass());
					this.textAtt.setSourceId(this.source.getId());
					this.textAtt.setContentType(this.contentType);
					Services.getInstance().getMDService().saveEntry(this.textAtt);
					this.addTextAtt2Source();						
				}
			}	
		} catch (Exception e) {
			addInternalError(e);
		}
	}

	public String actionAdd(){
		try {
			if(this.textAtt.isCompleted(useLanguage)){
				Set<TextAttribute> attList = getAttList();
				if(this.textAttPersistent == null && this.uniqueLanguage && existLang(attList, this.textAtt)){
					addMsg("There is already an element in this language");
				}else{
					
					if(this.textAttPersistent != null){
						this.textAttPersistent.setLanguageCode(this.textAtt.getLanguageCode());
						this.textAttPersistent.setValue(this.textAtt.getValue());
						this.textAtt = this.textAttPersistent;
						this.textAttPersistent = null;
					}
					
					this.textAtt.setSourceClass(getSourceClass());
					this.textAtt.setSourceId(this.source.getId());
					this.textAtt.setContentType(this.contentType);
					Services.getInstance().getMDService().saveEntry(this.textAtt);
					this.addTextAtt2Source();
					this.textAtt = new TextAttribute();
						
				}
			}else{
				addMsg("The language attribute is mandatory!");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
		
		return pageName;
	}
	
	public Set<TextAttribute> getAttList() throws Exception{
		if(source instanceof DataCollection){
			return ((DataCollection) source).getAttributeMap().get(contentType);
		}else if(source instanceof Place){
			return ((Place) source).getAttributeMap().get(contentType);
		}else if(source instanceof CollectionFile){
			return ((CollectionFile) source).getAttributeMap().get(contentType);
		}else if(source instanceof Time){
			return ((Time) source).getCommentList();
		}else {
			throw new Exception("This method does not support the class " + source.getClass().getName());
		}
	}
	
	private void addTextAtt2Source() throws Exception{
		if(source instanceof Place){
			if(((Place) source).getAttributeMap().get(this.contentType) == null)
				((Place) source).getAttributeMap().put(this.contentType, new LinkedHashSet<TextAttribute>());
			((Place) source).getAttributeMap().get(this.contentType).add(textAtt);
			
		}else if(source instanceof DataCollection){
			if(((DataCollection) source).getAttributeMap().get(this.contentType) == null)
				((DataCollection) source).getAttributeMap().put(this.contentType, new LinkedHashSet<TextAttribute>());
			((DataCollection) source).getAttributeMap().get(this.contentType).add(textAtt);
			
		}else if(source instanceof CollectionFile){
			if(((CollectionFile) source).getAttributeMap().get(this.contentType) == null)
				((CollectionFile) source).getAttributeMap().put(this.contentType, new LinkedHashSet<TextAttribute>());
			((CollectionFile) source).getAttributeMap().get(this.contentType).add(textAtt);
		
		}else if(source instanceof Time){
			((Time) source).getCommentList().add(textAtt);
		
		}else {
			throw new Exception("This method does not support the class " + source.getClass().getName());
		}
	}
	
	public String actionDelete(){
		try {
			TextAttribute textAtt0 = (TextAttribute)getRequestBean(this.contentType.toString());
			Services.getInstance().getMDService().deleteDBEntry(textAtt0);
			this.removeTextAttFromSource(textAtt0);
		} catch (Exception e) {
			addInternalError(e);
		}
		return pageName;
	}
	
	public String actionEdit(){
		try {
			
			this.textAttPersistent = (TextAttribute)getRequestBean(this.contentType.toString());
			this.textAtt = TextAttribute.clone(this.textAttPersistent, this.source.getId());
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return pageName;
	}
	
	public String actionCancelEdition(){
		this.textAttPersistent = null;
		this.textAtt = new TextAttribute();
		return pageName;
	}
	
	private void removeTextAttFromSource(TextAttribute toRemove) throws Exception{
		if(source instanceof Place){
			((Place) source).getAttributeMap().get(this.contentType).remove(toRemove);
		}else if(source instanceof DataCollection){
			((DataCollection) source).getAttributeMap().get(this.contentType).remove(toRemove);
		}else if(source instanceof CollectionFile){
			((CollectionFile) source).getAttributeMap().get(this.contentType).remove(toRemove);
		}else if(source instanceof Time){
			((Time) source).getCommentList().remove(toRemove);
		}else {
			throw new Exception("This method does not support the class " + source.getClass().getName());
		}
	}
	
	/**
	 * This method returns true, 
	 * if and only if the parameter <i>list</i> contains already a item with the same language of the parameter <i>att</i>. 
	 * @param list
	 * @param att
	 * @return
	 */
	private boolean existLang(Set<TextAttribute> list, TextAttribute att){
		for(TextAttribute att0 : list){
			if(StringUtils.equals(att0.getLanguageCode(), att.getLanguageCode()))
				return true;
		}
		return false;
	}
	
	public String actionResetAccuracyDescription(){
		this.textAtt = new TextAttribute();
		return pageName;
	}

	public TextAttribute getTextAtt() {
		return textAtt;
	}

	public void setTextAtt(TextAttribute textAtt) {
		this.textAtt = textAtt;
	}

	public TextAttribute getTextAttPersistent() {
		return textAttPersistent;
	}

	public void setTextAttPersistent(TextAttribute textAttPersistent) {
		this.textAttPersistent = textAttPersistent;
	}
	
}
