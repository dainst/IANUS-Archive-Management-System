package de.ianus.ingest.web.pages;

import java.io.File;

import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.xml.XMLService;
import de.ianus.ingest.web.pages.FilesPage.Right;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;
import de.ianus.ingest.web.pages.metadata.AssetUploadPage;
import de.ianus.metadata.bo.DataCollection;

public class ArchivalPage  extends WorkflowPage{
	
	public static final String PAGE_NAME = "archival";
	
	private static Logger logger = Logger.getLogger(ArchivalPage.class);
	
	@Override
	public void load(WorkflowIP aip){
		try {
			super.load(aip);
			super.setDipList(Services.getInstance().getDaoService().getDip((ArchivalIP)aip));
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionCreateMets() throws Exception{
		XMLService mets = new XMLService(this.getCurrentIp());
		mets.createMETS();
		return PAGE_NAME;
	}
	
	public void actionCreateBagit() throws Exception {
		try {
			logger.info("Creating Bagit file of " + getAip().toLogString());
			getAip().createBagit();
			addMsg("The bagit file has been created");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public String actionStartDisseminationWF(){
		try {
			DisseminationIP dip = Services.getInstance().getProcessEngineService().startDisseminationWF(getAip());
			this.getSession().getDisseminationPage().load(dip);
			this.load(this.getAip());
			return DisseminationPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCreateFileInstances4ArchiveFolder(){
		
		StringBuilder sb = new StringBuilder("\n");
		long start = System.currentTimeMillis();
		try {
			
			logger.info("creating concept files and  files instances for archival data folder");
			Services.getInstance().getDaoService().initConceptualFiles4Folder(getAip(), new File(getAip().getArchivalFolder()));
			addMsg("Concept files and  files instances for archive data folder created successfully");
		} catch (Exception e) {
			addInternalError(e);
		} finally {
			long diff = System.currentTimeMillis() - start;
			sb.append("*******************************************************\n");
			sb.append("InitConceptualFiles [archiveFolder] - time execution [ms]" + diff + "\n");
			sb.append("*******************************************************\n");
			logger.info(sb.toString());
		}
		
		return PAGE_NAME;
	}
	
	public String gotoAssetUploadPage() throws Exception {
		WorkflowIP wfip = getCurrentIp();
		wfip.getMetadataId();
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(wfip.getMetadataId());
		if(wfip instanceof SubmissionIP || wfip instanceof DisseminationIP) {
			try {
				getSession().getAssetUploadPage().load(dc, getCurrentIp());
				return AssetUploadPage.PAGE_NAME;
			} catch (Exception e) {
				addInternalError(e);
			}
		}
		return null;
	}
	
	public String gotoSipPage(){
		try {
			getSession().getSubmissionPage().load(getAip().getSip());
			return SubmissionPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	@Override
	public String gotoFilesPage(){
		try {
			// override is setting more permissions
			getSession().getFilesPage().load(getCurrentIp(), ViewLevel.project, Right.update, Right.delete, Right.move, Right.download);
			return FilesPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public ArchivalIP getAip() {
		return (this.getCurrentIp() instanceof ArchivalIP) ? (ArchivalIP)this.getCurrentIp() : null;
	}
	
	

}
