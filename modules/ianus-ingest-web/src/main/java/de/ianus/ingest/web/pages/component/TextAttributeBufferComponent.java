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

public class TextAttributeBufferComponent extends AbstractBean{
	
	private TextAttribute textAtt = new TextAttribute();
	//used for valueList, which have identifier
	private String textAttId;
	
	private MDEntry source;
	private TextAttribute.ContentType contentType;
	private boolean uniqueLanguage = false;
	private boolean useLanguage = true; 

	private Set<TextAttribute> currentList = new LinkedHashSet<TextAttribute>();
	
	public TextAttributeBufferComponent(MDEntry source, TextAttribute.ContentType contentType) throws Exception{
		this.source = source;
		this.contentType = contentType;
		this.uniqueLanguage = false;
		this.load();
	}
	
	public TextAttributeBufferComponent(MDEntry source, TextAttribute.ContentType contentType, boolean useLanguage, boolean uniqueLanguage) throws Exception{
		this.source = source;
		this.contentType = contentType;
		this.uniqueLanguage = uniqueLanguage;
		this.useLanguage = useLanguage;
		this.load();
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
	
	public void load() throws Exception{
		this.currentList = new LinkedHashSet<TextAttribute>(getAttList());
		this.textAtt = new TextAttribute();
	}
	
	public void save() throws Exception{
		
		for(TextAttribute item : this.currentList){
			item.setSourceClass(getSourceClass());
			item.setSourceId(this.source.getId());
			item.setContentType(this.contentType);
			Services.getInstance().getMDService().saveEntry(item);
		}
		
		Set<TextAttribute> oldList = getAttList();
		for(TextAttribute oldItem : oldList){
			if(!this.currentList.contains(oldItem)){
				Services.getInstance().getMDService().deleteDBEntry(oldItem);
			}
		}
	}

	public void listenerAdd(ActionEvent event){
		try {
			if(source instanceof Place && TextAttribute.ContentType.typeOfArea.equals(contentType)){
				Place.TypeOfArea item = Place.TypeOfArea.get(textAttId);
				
				TextAttribute ger = new TextAttribute(TextAttribute.ContentType.typeOfArea, DataCollection.GERMAN_CODE);
				TextAttribute eng = new TextAttribute(TextAttribute.ContentType.typeOfArea, DataCollection.ENGLISH_CODE);
				
				ger.setValue(item.labelGer);
				ger.setLabel(item.labelGer);
				ger.setSource(source);
				eng.setValue(item.labelEng);
				eng.setLabel(item.labelEng);
				eng.setSource(source);
				
				if(!containsTextAtt(ger.getValue(), ger.getLanguageCode())){
					this.currentList.add(ger);
				}
				if(!containsTextAtt(eng.getValue(), eng.getLanguageCode())){
					this.currentList.add(eng);
				}
				
				this.textAttId = null;
				
			}else if(this.textAtt.isCompleted(useLanguage)){
				if(this.uniqueLanguage && existLang(this.currentList, this.textAtt)){
					addMsg("There is already an element in this language");
				}else{
					this.textAtt.setSource(source);
					this.currentList.add(this.textAtt);
					this.textAtt = new TextAttribute();
				}
			}else{
				addMsg("The language attribute is mandatory!");
			}
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	private boolean containsTextAtt(String value, String languageCode){
		for(TextAttribute item : this.currentList){
			if(StringUtils.equals(item.getValue(), value) &&
					StringUtils.equals(item.getLanguageCode(), languageCode)){
				return true;
			}
		}
		return false;
	}
	
	public void listenerDelete(ActionEvent event){
		try {
			TextAttribute item = (TextAttribute)getRequestBean(this.contentType.toString());
			this.currentList.remove(item);
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public Set<TextAttribute> getAttList() throws Exception{
		if(source instanceof DataCollection){
			return ((DataCollection) source).getAttributeList(contentType);  //getAttributeMap().get(contentType);
		}else if(source instanceof Place){
			return ((Place) source).getAttributeList(contentType);//getAttributeMap().get(contentType);
		}else if(source instanceof CollectionFile){
			return ((CollectionFile) source).getAttributeList(contentType);//getAttributeMap().get(contentType);
		}else if(source instanceof Time){
			return ((Time) source).getCommentList();
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

	public TextAttribute getTextAtt() {
		return textAtt;
	}

	public void setTextAtt(TextAttribute textAtt) {
		this.textAtt = textAtt;
	}

	public Set<TextAttribute> getCurrentList() {
		return currentList;
	}

	public void setCurrentList(Set<TextAttribute> currentList) {
		this.currentList = currentList;
	}

	public String getTextAttId() {
		return textAttId;
	}

	public void setTextAttId(String textAttId) {
		this.textAttId = textAttId;
	}
}
