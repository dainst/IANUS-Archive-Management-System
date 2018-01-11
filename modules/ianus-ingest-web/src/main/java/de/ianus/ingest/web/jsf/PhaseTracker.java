package de.ianus.ingest.web.jsf;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.web.SessionBean;

public class PhaseTracker  implements PhaseListener {
	private static final long serialVersionUID = -353196272745250517L;

	@Override
	public void beforePhase(PhaseEvent event) {
		
		//System.out.println("***** beforePhase: " + event.getPhaseId());
		
		Map<String, String> parameters = event.getFacesContext().getExternalContext().getRequestParameterMap();
		
		String servletPath = event.getFacesContext().getExternalContext().getRequestServletPath();
		
		DAOService dao = Services.getInstance().getDaoService();
		
		if(StringUtils.isNotEmpty(servletPath) && event.getPhaseId() == PhaseId.RESTORE_VIEW){
			if(servletPath.equals("/pages/storage.xhtml")){
				getSessionBean(event.getFacesContext()).getStoragePage().load();
			}else if(servletPath.equals("/pages/transferList.xhtml")){
				getSessionBean(event.getFacesContext()).getOverviewDCPage().load();
			}else if(servletPath.equals("/pages/transfer.xhtml")){
				try {
					Long tpId = Long.parseLong(parameters.get("tpId"));
					if(tpId != null){
						WorkflowIP tp = dao.getTransfer(tpId);
						getSessionBean(event.getFacesContext()).getTransferPage().load(tp);	
					}
				} catch (Exception e) {
					//getSessionBean(event.getFacesContext()).addMsg("The parameter tpId is mandatory");
				}
			}else if(servletPath.equals("/pages/submission.xhtml")){
				try {
					Long sipId = Long.parseLong(parameters.get("sipId"));
					if(sipId != null){
						WorkflowIP sip = dao.getSip(sipId);
						getSessionBean(event.getFacesContext()).getSubmissionPage().load(sip);	
					}
				} catch (Exception e) {
					//getSessionBean(event.getFacesContext()).addMsg("The parameter tpId is mandatory");
				}
			}else if(servletPath.equals("/pages/archival.xhtml")){
				try {
					Long aipId = Long.parseLong(parameters.get("aipId"));
					if(aipId != null){
						WorkflowIP aip = dao.getAip(aipId);
						getSessionBean(event.getFacesContext()).getArchivalPage().load(aip);	
					}
				} catch (Exception e) {
					//getSessionBean(event.getFacesContext()).addMsg("The parameter tpId is mandatory");
				}
			}else if(servletPath.equals("/pages/dissemination.xhtml")){
				try {
					Long dipId = Long.parseLong(parameters.get("dipId"));
					if(dipId != null){
						WorkflowIP dip = dao.getDip(dipId);
						getSessionBean(event.getFacesContext()).getDisseminationPage().load(dip);	
					}
				} catch (Exception e) {
					//getSessionBean(event.getFacesContext()).addMsg("The parameter tpId is mandatory");
				}
			}else if(servletPath.equals("/pages/activityOutput.xhtml")){
				try {
					Long wfipId = Long.parseLong(parameters.get("wfipId"));
					String ianusActivityId0 = parameters.get("ianusActivityId");
					Long ianusActivityId  = Long.parseLong(ianusActivityId0);
					getSessionBean(event.getFacesContext()).getActivityOutputPage().load(ianusActivityId, wfipId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Override
	public void afterPhase(PhaseEvent event) {}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	private SessionBean getSessionBean(FacesContext context){
		SessionBean bean = (SessionBean)context.getExternalContext().getSessionMap().get("sessionBean");
		if(bean == null){
			bean = new SessionBean();
			context.getExternalContext().getSessionMap().put("sessionBean", bean);
		}
		return bean;
	}
}
