package de.ianus.ingest.web.pages.component;

import de.ianus.ingest.web.AbstractBean;
import de.ianus.ingest.web.pages.CollectionFilePage;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.metadata.bo.CollectionFile;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;

public class DcComponentPage extends AbstractBean{

	private DataCollection dcBase;
	private IANUSEntity source;
	
	public void load(DataCollection dcBase, IANUSEntity source) throws Exception{
		this.dcBase = dcBase;
		this.source = source;
	}
	
	public String getSourceLabel(){
		if(this.source instanceof DataCollection){
			return "data collection";
		}else if(this.source instanceof CollectionFile){
			return "collection file";
		}
		return null;
	}
	
	public String actionBack2MetadataDcPage(){
		if(source instanceof DataCollection){
			getSession().getDataCollectionPage().load(getDcBase(), getSource());
			return DataCollectionPage.PAGE_NAME;
		}else if(source instanceof CollectionFile){
			getSession().getCollectionFilePage().load(getDcBase(), getSource());
			return CollectionFilePage.PAGE_NAME;
		} 
		return null;
	}

	public IANUSEntity getSource() {
		return source;
	}

	public void setSource(IANUSEntity source) {
		this.source = source;
	}
	
	public DataCollection getDcBase() {
		return dcBase;
	}

	public void setDcBase(DataCollection dcBase) {
		this.dcBase = dcBase;
	}

	public DataCollection getDcSource(){
		return (this.source instanceof DataCollection) ? (DataCollection) source : null;
	}
	
	public CollectionFile getFileSource(){
		return (this.source instanceof CollectionFile) ? (CollectionFile) source : null;
	}

}
