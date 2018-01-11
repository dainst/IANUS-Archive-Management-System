package de.ianus.ingest.web.pages;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.ApplicationBean;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.Time;
import de.ianus.metadata.utils.TimeVocabulary;
import de.ianus.metadata.utils.TimeVocabulary.VlaTimeline2;
import de.ianus.metadata.utils.TimeVocabulary.VlaTimeline3;
import de.ianus.metadata.utils.ValueListUtils;

public class TimePage extends DcComponentPage{
	
	public static String PAGE_NAME = "time";
	
	private static List<SelectItem> mainPeriodNameList = new ArrayList<SelectItem>();
	private static List<SelectItem> subPeriodNameList = new ArrayList<SelectItem>();
	//private static List<SelectItem> subPeriodVocabulary = new ArrayList<SelectItem>();
	
	private static List<SelectItem> mainPeriodVlaTimeline2List = new ArrayList<SelectItem>();
	private static List<SelectItem> subPeriodVlaTimeline3List = new ArrayList<SelectItem>();
	
	static{
		
		mainPeriodNameList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(ValueListUtils.Names item : ValueListUtils.mainPeriodListName){
			mainPeriodNameList.add(new SelectItem(item.id, item.getField("label")));
		}
		
		subPeriodNameList.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(ValueListUtils.Names item : ValueListUtils.subPeriodListName){
			subPeriodNameList.add(new SelectItem(item.id, item.getField("label")));
		}
		
		mainPeriodVlaTimeline2List.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(TimeVocabulary.VlaTimeline2 item : TimeVocabulary.VlaTimeline2.values()){
			mainPeriodVlaTimeline2List.add(new SelectItem(item.id, item.valueGer));
		}
		
		subPeriodVlaTimeline3List.add(new SelectItem(ApplicationBean.NONE_SELECTED_ID, ApplicationBean.NONE_SELECTED_LABEL));
		for(TimeVocabulary.VlaTimeline3 item : TimeVocabulary.VlaTimeline3.values()){
			subPeriodVlaTimeline3List.add(new SelectItem(item.id, item.valueGer));
		}
	}
	
	private Time time;
	//private TextAttributeComponent description = null;
	private String selectedSubPeriodId;
	private Set<TextAttribute> currentCommentList = new LinkedHashSet<TextAttribute>();
	private TextAttribute currentComment = new TextAttribute();
	
	private String mainPeriodVlaTimeline2ValueId;
	private String mainPeriodIdaiChronontologyValue;
	private String mainPeriodIdaiChronontologyValueId;
	private String mainPeriodIdaiChronontologyValueUri;
	
	private String subPeriodVlaTimeline3ValueId = new String();
	private String subPeriodFreeTextValueId = new String();
	
	private String subPeriodIdaiChronontologyValue = new String();
	private String subPeriodIdaiChronontologyValueId = new String();
	private String subPeriodIdaiChronontologyUri = new String();
	
	
	public void load(DataCollection dcBase, IANUSEntity source, Time time) throws Exception{
		
		this.setDcBase(dcBase);
		this.setSource(source);
		
		this.time = (time.isPersistent()) ? Services.getInstance().getMDService().getTime(time.getId()) : time;
		
		//loading mainPeriod
		this.time.setMainPeriod((time.getMainPeriod() != null) ? time.getMainPeriod() : new ElementOfList());
		this.mainPeriodIdaiChronontologyValue = (StringUtils.isNotEmpty(this.time.getMainPeriod().getValue())) ? this.time.getMainPeriod().getValue() : "";
		this.mainPeriodIdaiChronontologyValueId = (StringUtils.isNotEmpty(this.time.getMainPeriod().getValueId())) ? this.time.getMainPeriod().getValueId() : "";
		this.mainPeriodIdaiChronontologyValueUri = (StringUtils.isNotEmpty(this.time.getMainPeriod().getUri())) ? this.time.getMainPeriod().getUri() : "";
		this.mainPeriodVlaTimeline2ValueId = (StringUtils.isNotEmpty(this.time.getMainPeriod().getValueId())) ? this.time.getMainPeriod().getValueId() : ApplicationBean.NONE_SELECTED_ID;
		
		//loading subPeriod
		this.time.setSubPeriod((time.getSubPeriod() != null) ? time.getSubPeriod() : new ElementOfList());
		this.subPeriodFreeTextValueId = (StringUtils.isNotEmpty(this.time.getSubPeriod().getValueId())) ? this.time.getSubPeriod().getValueId() : "";
		this.subPeriodVlaTimeline3ValueId = (StringUtils.isNotEmpty(this.time.getSubPeriod().getValueId())) ? this.time.getSubPeriod().getValueId() : ApplicationBean.NONE_SELECTED_ID;
		this.subPeriodIdaiChronontologyValue = (StringUtils.isNotEmpty(this.time.getSubPeriod().getValue())) ? this.time.getSubPeriod().getValue() : "";
		this.subPeriodIdaiChronontologyValueId = (StringUtils.isNotEmpty(this.time.getSubPeriod().getValueId())) ? this.time.getSubPeriod().getValueId() : "";
		this.subPeriodIdaiChronontologyUri = (StringUtils.isNotEmpty(this.time.getSubPeriod().getUri())) ? this.time.getSubPeriod().getUri() : "";
		
		
		if(StringUtils.isEmpty(this.time.getSubPeriod().getValueId())){
			this.time.getSubPeriod().reset();
			this.time.getSubPeriod().setValueId(ApplicationBean.NONE_SELECTED_ID);
		}
		
		//loading description list
		this.currentCommentList = new LinkedHashSet<TextAttribute>(this.time.getCommentList());
		//this.description = new TextAttributeComponent(time, TextAttribute.ContentType.description, PAGE_NAME, true, true);
	}
	
	public void onChangeMainPeriodList(){
		System.out.println("**** " + this.time.getMainPeriod().getListId());
	}
	
	public String actionAddSubPeriod(){
		System.out.println(this.selectedSubPeriodId);
		return PAGE_NAME;
	}
	
	public boolean isSelectedMainPeriodVlaTimeline2(){
		return StringUtils.equals(ValueListUtils.Names.vla_timeline_2.id, this.time.getMainPeriod().getListId());
	}
	
	public boolean isSelectedMainPeriodIdaiChronontology(){
		return StringUtils.equals(ValueListUtils.Names.idai_chronontology.id, this.time.getMainPeriod().getListId());
	}
	
	public boolean isSelectedSubPeriodVlaTimeline3(){
		return StringUtils.equals(ValueListUtils.Names.vla_timeline_3.id, this.time.getSubPeriod().getListId());
	}
	
	public boolean isSelectedSubPeriodFreeText(){
		return StringUtils.equals(ValueListUtils.Names.free_text.id, this.time.getSubPeriod().getListId());
	}
	
	public boolean isSelectedSubPeriodIdaiChronontology(){
		return StringUtils.equals(ValueListUtils.Names.idai_chronontology.id, this.time.getSubPeriod().getListId());
	}
	
	public String actionSave(){
		try {
			this.time.setSource(getSource());
			Services.getInstance().getMDService().saveEntry(this.time);
			
			this.time.getMainPeriod().setSource(this.time);
			this.time.getMainPeriod().setContentType(ElementOfList.ContentType.mainPeriod);
			
			this.time.getSubPeriod().setSource(this.time);
			this.time.getSubPeriod().setContentType(ElementOfList.ContentType.subPeriod);
			
			//saving mainPeriod
			boolean resetMainPeriod = false;
			if(!StringUtils.equals(ApplicationBean.NONE_SELECTED_ID, this.time.getMainPeriod().getListId())){
				if(this.isSelectedMainPeriodVlaTimeline2()){
					if(!StringUtils.equals(ApplicationBean.NONE_SELECTED_ID, this.mainPeriodVlaTimeline2ValueId)){
						this.time.getMainPeriod().setValueId(this.mainPeriodVlaTimeline2ValueId);
						this.time.getMainPeriod().setValue(VlaTimeline2.getValueGer(this.mainPeriodVlaTimeline2ValueId));
						this.time.getMainPeriod().setUri("");
					}else{
						resetMainPeriod = true;
					}
					
				}else if(this.isSelectedMainPeriodIdaiChronontology()){
					this.time.getMainPeriod().setValueId(this.mainPeriodIdaiChronontologyValueId);
					this.time.getMainPeriod().setValue(this.mainPeriodIdaiChronontologyValue);
					this.time.getMainPeriod().setUri(this.mainPeriodIdaiChronontologyValueUri);
				}
				Services.getInstance().getMDService().saveEntry(this.time.getMainPeriod());
			}else{
				resetMainPeriod = true;
			}
			
			if(resetMainPeriod){
				if(this.time.getMainPeriod().isPersistent()){
					Services.getInstance().getMDService().deleteDBEntry(this.time.getMainPeriod());
				}
				this.time.setMainPeriod(new ElementOfList());
			}
			
			//saving subPeriod
			boolean resetSubPeriod = false;
			if(!StringUtils.equals(ApplicationBean.NONE_SELECTED_ID, this.time.getSubPeriod().getListId())){
				
				if(this.isSelectedSubPeriodFreeText()){
					this.time.getSubPeriod().setValueId(this.subPeriodFreeTextValueId);
					this.time.getSubPeriod().setValue(this.subPeriodFreeTextValueId);
					this.time.getSubPeriod().setUri("");
				}else if(this.isSelectedSubPeriodIdaiChronontology()){
					this.time.getSubPeriod().setValueId(this.subPeriodIdaiChronontologyValueId);
					this.time.getSubPeriod().setValue(this.subPeriodIdaiChronontologyValue);
					this.time.getSubPeriod().setUri(this.subPeriodIdaiChronontologyUri);
				}else if(this.isSelectedSubPeriodVlaTimeline3()){
					if(!StringUtils.equals(ApplicationBean.NONE_SELECTED_ID, this.subPeriodVlaTimeline3ValueId)){
						this.time.getSubPeriod().setValueId(this.subPeriodVlaTimeline3ValueId);
						this.time.getSubPeriod().setValue(VlaTimeline3.getValueGer(this.subPeriodVlaTimeline3ValueId));
						this.time.getSubPeriod().setUri("");
					}else{
						resetSubPeriod = true;
					}
				}
				Services.getInstance().getMDService().saveEntry(this.time.getSubPeriod());
			}else{
				resetSubPeriod = true;
			}
			
			if(resetSubPeriod){
				if(this.time.getSubPeriod().isPersistent()){
					Services.getInstance().getMDService().deleteDBEntry(this.time.getSubPeriod());
				}
				this.time.setSubPeriod(new ElementOfList());
			}
			
		
			this.saveDescriptionList();
			this.load(getDcBase(), getSource(), this.time);
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private void saveDescriptionList() throws Exception{
		
		for(TextAttribute newItem : this.currentCommentList){
			
			newItem.setContentType(TextAttribute.ContentType.comment);
			newItem.setSource(this.time);
			
			Services.getInstance().getMDService().saveEntry(newItem);
		}
	
		for(TextAttribute oldItem : this.time.getCommentList()){
			if(!this.currentCommentList.contains(oldItem)){
				Services.getInstance().getMDService().deleteDBEntry(oldItem);
			}
		}
	}
	
	public boolean isMainPeriodListEmpty(){
		return this.time.getMainPeriod().getListId() == null || StringUtils.equals(ApplicationBean.NONE_SELECTED_ID, this.time.getMainPeriod().getListId());
	}
	
	public String actionAddComment(){
		try {
			this.currentCommentList.add(currentComment);
			this.currentComment = new TextAttribute();
			
		} catch (Exception e) {
			addInternalError(e);
		}
		
		
		return PAGE_NAME;
	}
	
	public String actionDeleteComment(){
		TextAttribute item = (TextAttribute)getRequestBean("comment");
		this.currentCommentList.remove(item);
		return PAGE_NAME;
	}

	public List<SelectItem> getMainPeriodNameList() {
		return mainPeriodNameList;
	}

	public List<SelectItem> getSubPeriodNameList() {
		return subPeriodNameList;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}


	public Set<TextAttribute> getCurrentCommentList() {
		return currentCommentList;
	}

	public void setCurrentCommentList(Set<TextAttribute> currentCommentList) {
		this.currentCommentList = currentCommentList;
	}
	
	
	
	public TextAttribute getCurrentComment() {
		return currentComment;
	}

	public void setCurrentComment(TextAttribute currentComment) {
		this.currentComment = currentComment;
	}

	public String getMainPeriodVlaTimeline2ValueId() {
		return mainPeriodVlaTimeline2ValueId;
	}

	public void setMainPeriodVlaTimeline2ValueId(String mainPeriodVlaTimeline2ValueId) {
		this.mainPeriodVlaTimeline2ValueId = mainPeriodVlaTimeline2ValueId;
	}

	public String getMainPeriodIdaiChronontologyValueId() {
		return mainPeriodIdaiChronontologyValueId;
	}

	public void setMainPeriodIdaiChronontologyValueId(String mainPeriodIdaiChronontologyValueId) {
		this.mainPeriodIdaiChronontologyValueId = mainPeriodIdaiChronontologyValueId;
	}

	public List<SelectItem> getMainPeriodVlaTimeline2List(){
		return mainPeriodVlaTimeline2List;
	}
	
	public List<SelectItem> getSubPeriodVlaTimeline3List(){
		return subPeriodVlaTimeline3List;
	}

	public String getSubPeriodVlaTimeline3ValueId() {
		return subPeriodVlaTimeline3ValueId;
	}

	public void setSubPeriodVlaTimeline3ValueId(String subPeriodVlaTimeline3ValueId) {
		this.subPeriodVlaTimeline3ValueId = subPeriodVlaTimeline3ValueId;
	}

	public String getSubPeriodFreeTextValueId() {
		return subPeriodFreeTextValueId;
	}

	public void setSubPeriodFreeTextValueId(String subPeriodFreeTextValueId) {
		this.subPeriodFreeTextValueId = subPeriodFreeTextValueId;
	}

	public String getSubPeriodIdaiChronontologyValueId() {
		return subPeriodIdaiChronontologyValueId;
	}

	public void setSubPeriodIdaiChronontologyValueId(String subPeriodIdaiChronontologyValueId) {
		this.subPeriodIdaiChronontologyValueId = subPeriodIdaiChronontologyValueId;
	}

	public String getMainPeriodIdaiChronontologyValue() {
		return mainPeriodIdaiChronontologyValue;
	}

	public void setMainPeriodIdaiChronontologyValue(String mainPeriodIdaiChronontologyValue) {
		this.mainPeriodIdaiChronontologyValue = mainPeriodIdaiChronontologyValue;
	}

	public String getSubPeriodIdaiChronontologyValue() {
		return subPeriodIdaiChronontologyValue;
	}

	public void setSubPeriodIdaiChronontologyValue(String subPeriodIdaiChronontologyValue) {
		this.subPeriodIdaiChronontologyValue = subPeriodIdaiChronontologyValue;
	}

	public String getMainPeriodIdaiChronontologyValueUri() {
		return mainPeriodIdaiChronontologyValueUri;
	}

	public void setMainPeriodIdaiChronontologyValueUri(String mainPeriodIdaiChronontologyValueUri) {
		this.mainPeriodIdaiChronontologyValueUri = mainPeriodIdaiChronontologyValueUri;
	}

	public String getSubPeriodIdaiChronontologyUri() {
		return subPeriodIdaiChronontologyUri;
	}

	public void setSubPeriodIdaiChronontologyUri(String subPeriodIdaiChronontologyUri) {
		this.subPeriodIdaiChronontologyUri = subPeriodIdaiChronontologyUri;
	}	
}
