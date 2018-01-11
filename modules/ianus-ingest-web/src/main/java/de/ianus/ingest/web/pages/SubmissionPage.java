package de.ianus.ingest.web.pages;

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

/**
 * 
 * @author jurzua
 *
 */
public class SubmissionPage extends WorkflowPage {
	
	private static Logger logger = Logger.getLogger(SubmissionPage.class);
	public static final String PAGE_NAME = "submission";
	
	@Override
	public void load(WorkflowIP wfip) throws Exception{
		super.load(wfip);
		SubmissionIP sip = (SubmissionIP)wfip;
		super.setAipList(Services.getInstance().getDaoService().getAip(sip));
		super.setDipList(Services.getInstance().getDaoService().getDip(sip));
	}
	
	
	
	public String actionCreateMets() throws Exception{
		XMLService mets = new XMLService(this.getCurrentIp());
		mets.createMETS();
		return PAGE_NAME;
	}
	
	public String actionStartIngestWF(){
		try {
			ArchivalIP aip = Services.getInstance().getProcessEngineService().startIngestWF(getSip());
			this.getSession().getArchivalPage().load(aip);
			this.load(this.getSip());
			return ArchivalPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionCompleteTask(){
		this.completeTask();
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
	
	@Override
	public String gotoFilesPage(){
		try {
			// override is setting extended permissions
			getSession().getFilesPage().load(getCurrentIp(), ViewLevel.project, Right.update, Right.delete, Right.move, Right.download);
			return FilesPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String gotoSimpleUploadPage(){
		try {
			getSession().getSimpleUploadPage().load(getCurrentIp());
			return SimpleUploadPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String gotoReportPage(){
		try {
			getSession().getPreIngestReportPage().load(getSip());
			return PreIngestReportPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String gotoTpPage(){
		try {
			getSession().getTransferPage().load(getSip().getTp());
			return TransferPage.PAGE_NAME;
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public SubmissionIP getSip() {
		return (getCurrentIp() instanceof SubmissionIP) ? (SubmissionIP)getCurrentIp() : null;
	}
	
	/*
	public void listenerCompleteTask(ActionEvent event){
		//DefaultSqlSession
		//select * from ACT_RU_TASK where ID_ = ?
		//105
		//public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		try {
			ActivityWrapper activityWrapper = (ActivityWrapper)getRequestBean("wrapper");
			HistoricActivityInstance act = activityWrapper.getActivity();
			logger.info("listenerCompleteTask [activityName=" + act.getActivityName() + ", " + act.getActivityId() + ", taskId=" + act.getTaskId() + "]");
			
			Map<String, Object> taskVariables = new HashMap<String, Object>();
			if(StringUtils.isNotEmpty(activityWrapper.getUserDecision())){
				
				//WebServices.getInstance().getProcessEngineService().getProcessEngine().getRuntimeService().setVariable(
				//		activityWrapper.getActivity().getProcessInstanceId(), 
				//		activityWrapper.getTaskFormField().getId(), 
				//		activityWrapper.getUserDecision());
				
				taskVariables.put(activityWrapper.getTaskFormField().getId(), activityWrapper.getUserDecision());
			}
			WebServices.getInstance().getProcessEngineService().completeTask(activityWrapper.getActivity().getTaskId(), taskVariables);
			this.load(this.sip);	
			
			addMsg("The task "+ activityWrapper.getActivity().getActivityName() +" is completed!");
		} catch (Exception e) {
			addInternalError(e);
		}
	}*/
	
	/*
	private String printTask(Task task){
		StringBuilder sb = new StringBuilder("***** Task\n");
		
		sb.append("\tid= " + task.getId() + "\n");
		sb.append("\tname= " + task.getName() + "\n");
		sb.append("\towner= " + task.getOwner() + "\n");
		sb.append("\tcase execution id= " + task.getCaseExecutionId() + "\n");
		sb.append("\tparent task id= " + task.getParentTaskId() + "\n");
		sb.append("\tprocess definition id= " + task.getProcessDefinitionId() + "\n");
		sb.append("\tcase instance id= " + task.getCaseInstanceId() + "\n");
		
		return sb.toString();
	}
	
	public String printHistoricActivity(HistoricActivityInstance a){
		StringBuilder sb = new StringBuilder("***** Historic Activity\n");
		
		sb.append("\tactivity id= " + a.getActivityId() + "\n");
		sb.append("\tactivity name= " + a.getActivityName() + "\n");
		sb.append("\tactivity type= " + a.getActivityType() + "\n");
		sb.append("\ttask id= " + a.getTaskId() + "\n");
		sb.append("\texecution id= " + a.getExecutionId() + "\n");
		sb.append("\tprocess dei id= " + a.getProcessDefinitionId() + "\n");
		sb.append("\tparent activity instance= " + a.getParentActivityInstanceId() + "\n");
		sb.append("\tassignee= " + a.getAssignee() + "\n");
		sb.append("\tprocess def key= " + a.getProcessDefinitionKey() + "\n");
		
		return sb.toString();
	}*/
	
	
}
