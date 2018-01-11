package de.ianus.access.web;



import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.metadata.bo.DataCollection;



public class SessionBean extends JSPWrapper{
	
	private static final Logger logger = Logger.getLogger(SessionBean.class);
	
	private DataCollection dc = null;
	private WorkflowIP wfip = null;
	private Class<?> wfipClass = DisseminationIP.class;
	
	private String languageCode = null;
	
	public SessionBean(){
		// set the default language
		if(this.languageCode == null)
			this.languageCode = "deu";
		
		// try retrieving the language from the browser locale
		if(this.getLocale() != null)
			this.languageCode = this.getLocale();
		
		// go for a user-selected language, if available
		if(this.getSelectedLanguage() != null)
			this.languageCode = this.getSelectedLanguage();
	}
	
	public boolean isLogged(){
		return true;
	}
	
	private static Boolean empty(String str) {
		if(str == null || str.isEmpty() || str.equals("null")) return true;
		return false;
	}
	
	
	
	public void loadInformationPackage(){
		try {
			String paramDipId = getPageContext().getRequest().getParameter("dipId");
			String paramCollNo = getPageContext().getRequest().getParameter("collNo");
			String paramSipId = getPageContext().getRequest().getParameter("sipId");
			String paramAipId = getPageContext().getRequest().getParameter("aipId");
			
			WorkflowIP tmpDip = null;
			
			Long dipId = null;
			Long sipId = null;
			Long aipId = null;
			
			if(!empty(paramDipId)) {
				dipId = Long.parseLong(paramDipId);
			}
			else if(!empty(paramCollNo)) {
				tmpDip = this.getDipByCollectionNumber(paramCollNo);
				dipId =  tmpDip.getId();
			}
			else if(!empty(paramSipId)) {
				sipId = Long.parseLong(paramSipId);
				this.wfip = Services.getInstance().getDaoService().getSip(sipId);
				this.wfipClass = SubmissionIP.class;
				
				this.dc = Services.getInstance().getMDService().getDataCollection(this.wfip.getMetadataId());
				logger.info("Loading " + wfip);
				logger.info("Loading " + dc);
			}
			else if(!empty(paramAipId)) {
				aipId = Long.parseLong(paramAipId);
				this.wfip = Services.getInstance().getDaoService().getAip(aipId);
				this.wfipClass = ArchivalIP.class;
				
				this.dc = Services.getInstance().getMDService().getDataCollection(this.wfip.getMetadataId());
				logger.info("Loading " + wfip);
				logger.info("Loading " + dc);
			}
			
			if(sipId == null && aipId == null && (this.wfip == null || this.wfip.getId() != dipId)) {
				if(tmpDip != null) this.wfip = tmpDip;
				else this.wfip = Services.getInstance().getDaoService().getDip(dipId);
				
				this.dc = Services.getInstance().getMDService().getDataCollection(this.wfip.getMetadataId());
				logger.info("Loading " + wfip);
				logger.info("Loading " + dc);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private WorkflowIP getDipByCollectionNumber(String collectionId){
		Set<WorkflowIP> dlist = Services.getInstance().getDaoService().getDipByIanusIdentifier(collectionId);
		if(!dlist.isEmpty())
			return dlist.iterator().next();
		
		return null;
	}

	public DataCollection getDc() {
		// for loading tabs using AJAX, we need to reload the wfip if we were 
		// browsing a different collection in a second browser tab before
		if(this.dc == null) this.loadInformationPackage();
		return this.dc;
	}

	public void setDc(DataCollection dc) {
		this.dc = dc;
	}

	public WorkflowIP getWfip() {
		return wfip;
	}
	
	public Class<?> getWfipClass() {
		return this.wfipClass;
	}

	public void setWfip(WorkflowIP dip) {
		this.wfip = dip;
	}
	
	
	public String getLanguage() {
		return this.languageCode;
	}
	
	private String getLocale() {

		String result = new String();
		PageContext context = this.getPageContext();
		if(context != null) {
			ServletRequest request = context.getRequest();
			Locale locale = request.getLocale();
			result = locale.getISO3Country();
			if(result != null && !result.equals("")) {
				return result;
			}
		}

		return null;
	}
	
	private String getSelectedLanguage() {
		// TODO: check language selection form
		return null;
	}
}
