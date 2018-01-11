package de.ianus.ingest.web.pages;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.PreIngestReport;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.AbstractBean;

public class PreIngestReportPage extends AbstractBean {

	private static final Logger logger = LogManager.getLogger(PreIngestReportPage.class);
	
	public static final String PAGE_NAME = "preIngestReport";
	
	private WorkflowIP sip;
	private PreIngestReport report;
	
	public void load(WorkflowIP sip) throws Exception{
		this.sip = sip;
		this.report = Services.getInstance().getDaoService().getPreIngestReport(this.sip.getId());
	}
	
	public String actionSaveReport(){
		try {
			Services.getInstance().getDaoService().saveDBEntry(report);
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public String gotoIPPage(){
		try {
			getSession().getSubmissionPage().load(this.sip);
			return SubmissionPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public WorkflowIP getSip() {
		return sip;
	}

	public void setSip(SubmissionIP sip) {
		this.sip = sip;
	}

	public PreIngestReport getReport() {
		return report;
	}

	public void setReport(PreIngestReport report) {
		this.report = report;
	}
}
