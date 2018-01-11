package de.ianus.ingest.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;

import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.IANUSActivity;
import de.ianus.ingest.core.bo.PreIngestReport;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.processEngine.ms.utils.MSThread;
import de.ianus.metadata.MetadataService.CloneResult;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Actor;

/**
 * @author Jorge Urzua
 *
 */
public class ProcessEngineService {

	private static final Logger logger = Logger.getLogger(ProcessEngineService.class);

	private static String TRANSFER_WF = "transfer-workflow";
	private static String PRE_INGEST_WF = "pre-ingest-workflow";
	private static String INGEST_WF = "ingest-workflow";
	private static String DISSEMINATION_WF = "dissemination-workflow";
	
	private ProcessEngine processEngine;

	public ProcessEngine getProcessEngine() throws Exception {
		if (processEngine == null) {
			processEngine = ProcessEngines.getDefaultProcessEngine();
			if(processEngine == null){
				throw new Exception("The camunda processEngine could not be created. It could be that file camunda.cfg.xml does not exist or it is not correctly configured.");
			}
		}
		return processEngine;
	}

	public void deployTransferWF() throws Exception {
		deployWF(TRANSFER_WF);
	}

	public void deployIngestWF() throws Exception {
		deployWF(INGEST_WF);
	}

	public void deployPreIngestWF() throws Exception {
		deployWF(PRE_INGEST_WF);
	}
	
	public void deployDisseminationWF() throws Exception {
		deployWF(DISSEMINATION_WF);
	}
	
	private void deployWF(String wf) throws Exception {

		RepositoryService repositoryService = getProcessEngine().getRepositoryService();
		Deployment deployment = repositoryService.createDeployment().addClasspathResource(wf + ".bpmn").deploy();

		logger.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());

		logger.info("Deployment [id=" + deployment.getId() + ", name=" + deployment.getName() + ", source="
				+ deployment.getSource() + "]");
	}

	/**
	 * You must drop the database before.
	 */
	public static void createDBSchema() {
		logger.info("Creating the database schema for Camunda");
		try {
			ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault()
					.setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_CREATE)
					.buildProcessEngine();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}	
	
	public Boolean isProccesInstanceSuspended(String piId) throws Exception{
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().processInstanceId(piId);
		ProcessInstance pi = query.singleResult();
		return (pi != null) ? pi.isSuspended() : null;
	}
	
	public void activeProcessInstance(String piId) throws Exception{
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		runtimeService.activateProcessInstanceById(piId);
	}
	
	public List<HistoricProcessInstance> getHistoricProcessInstanceList() throws Exception{
		 return getProcessEngine().getHistoryService().createHistoricProcessInstanceQuery().list();
	}
	
	public List<ProcessInstance> getProcessInstanceList() throws Exception{
		 return getProcessEngine().getRuntimeService().createProcessInstanceQuery().list();
	}
	
	public TransferP startTransferWF(TransferP tp) throws Exception {
		if(tp.getMetadataId() == null){
			throw new Exception("The metadata Id of the TP starting the transfer-wf is null");
		}
		tp.getLocation();
		tp.setState(WorkflowIP.IN_PROGRESS);
		tp.setCollectionLabel("not-assigned");
		tp.setCollectionIdentifier("not-assigned");
		Services.getInstance().getDaoService().saveDBEntry(tp);
		

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("wfipId", tp.getId());
		variables.put("wfClass", tp.getClass().getName());

		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(TRANSFER_WF, variables);

		tp.setProcessInstanceId(processInstance.getId());
		Services.getInstance().getDaoService().saveDBEntry(tp);
		
		logger.info("################ " + tp);
		
		return tp;
	}
	
	public SubmissionIP startPreIngestWF(TransferP tp) throws Exception {

		//creating an instance of SubmissionIP 
		SubmissionIP sip = new SubmissionIP();
		sip.getLocation();
		sip.setState(WorkflowIP.IN_PROGRESS);
		sip.setTp(tp);

		//Cloning the metadata
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(tp.getMetadataId());
		CloneResult cloneResult = Services.getInstance().getMDService().cloneDataCollection0(dc);
		sip.setMetadataId(cloneResult.getDc().getId());
		
		sip.setCollectionIdentifier(tp.getCollectionIdentifier());
		sip.setCollectionLabel(tp.getCollectionLabel());
		Services.getInstance().getDaoService().saveDBEntry(sip);
		

				
		//creation an instance of PreIngestReport 
		PreIngestReport report = new PreIngestReport();
		report.setSipId(sip.getId());
		Services.getInstance().getDaoService().saveDBEntry(report);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("wfipId", sip.getId());
		variables.put("wfClass", sip.getClass().getName());
		
		//variables.put("correctionsCurators", "NO");
		//variables.put("correctionsDataProvider", "NO");
		variables.put("correctionsMicroservices", "NO");
		variables.put("dataChanged", "NO");
		variables.put("dataMetadataState", PreIngestReport.State.OK);
		variables.put("secondCuratorCheck", "ianusCorrection");
		
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PRE_INGEST_WF, variables);

		sip = Services.getInstance().getDaoService().getSip(sip.getId());
		sip.setProcessInstanceId(processInstance.getId());
		Services.getInstance().getDaoService().saveDBEntry(sip);
		
		//The copying of the data folder is done by the Microservice: MSCreateInitialSIP
		
		return sip;
	}

	public ArchivalIP startIngestWF(SubmissionIP sip) throws Exception {
		logger.info(">>>>> starting ingest WF [SIP=" + sip.toString() + "]");
		
		//creating an instance of ArchivalIP 
		ArchivalIP aip = new ArchivalIP();
		aip.generateLocalFolderNames();
		aip.getLocation();
		aip.setState(WorkflowIP.IN_PROGRESS);
		//aip.setTp(null);
		//aip.setTpId(sip.getTpId());
		aip.setSip(sip);
		
		//Cloning the metadata
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(sip.getMetadataId());
		CloneResult cloneResult = Services.getInstance().getMDService().cloneDataCollection0(dc);
		aip.setMetadataId(cloneResult.getDc().getId());
				
		aip.setCollectionIdentifier(sip.getCollectionIdentifier());
		aip.setCollectionLabel(sip.getCollectionLabel());
		Services.getInstance().getDaoService().saveDBEntry(aip);
		
		//clone file concepts
		Services.getInstance().getDaoService().cloneFileConcepts(sip, aip);
		
		//data_consistent = YES, NO
		//result_conversion_ok = YES, NO
		//any_changes_needed = NO, CHANGE_METADATA, CHANGE_DOCUMENTATION, CHANGE_FORMATS
		//transfer_ok = YES, NO
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("wfipId", aip.getId());
		variables.put("wfClass", aip.getClass().getName());
		
		variables.put("dataConsistent", "YES");
		variables.put("resultMicroservicesOk", "YES");
		variables.put("anyChangesNeeded", "NO");
		variables.put("transferOk", "YES");
		variables.put("resultConversionOk", "YES");
		
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(INGEST_WF, variables);
		
		

		aip = Services.getInstance().getDaoService().getAip(aip.getId());
		aip.setProcessInstanceId(processInstance.getId());
		Services.getInstance().getDaoService().saveDBEntry(aip);
		
		//The copying of the data folder is done by the Microservice: MSCreateInitialAIP
		
		//copy and clone the actor's logos
		copyWebAssetsFolder(sip, aip, cloneResult.getActorMap());
		
		
		return aip;
	}
	
	public DisseminationIP startDisseminationWF(ArchivalIP aip) throws Exception {
		
		logger.info(">>>>> starting dissemination WF [AIP=" + aip.toString() + "]");
		
		//creating an instance of SubmissionIP 
		DisseminationIP dip = new DisseminationIP();
		dip.getLocation();
		dip.setState(WorkflowIP.IN_PROGRESS);
		//dip.setTp(null);
		//dip.setTpId(aip.getTpId());
		dip.setSip(null);
		dip.setSipId(aip.getSipId());
		dip.setAip(aip);
		
		//Cloning the metadata
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(aip.getMetadataId());
		CloneResult cloneResult = Services.getInstance().getMDService().cloneDataCollection0(dc);
		dip.setMetadataId(cloneResult.getDc().getId());
		
		dip.setCollectionIdentifier(aip.getCollectionIdentifier());
		dip.setCollectionLabel(aip.getCollectionLabel());
		Services.getInstance().getDaoService().saveDBEntry(dip);
		
		//clone file concepts, but adjust the changed file pathes
		Services.getInstance().getDaoService().cloneDIPFileConcepts(aip, dip);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("wfipId", dip.getId());
		variables.put("wfClass", dip.getClass().getName());
		
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(DISSEMINATION_WF, variables);

		dip = Services.getInstance().getDaoService().getDip(dip.getId());
		dip.setProcessInstanceId(processInstance.getId());
		Services.getInstance().getDaoService().saveDBEntry(dip);
		
		// The copying of the data folder is done by the Microservice: MSCreateInitialDIP
		
		// copy and clone the actor's logos, maintaining the filenames referencing the new Actor IDs
		this.copyWebAssetsFolder(aip, dip, cloneResult.getActorMap());
		
		logger.info("<<<<<< ending dissemination WF [DIP=" + dip.toString() + "]");
		
		return dip;
	}
	
	private void copyWebAssetsFolder(WorkflowIP source, WorkflowIP target, Map<Actor, Actor> cloningMap) throws IOException, Exception{
		FileUtils.copyDirectory(new File(source.getWebAssetsFolder()), new File(target.getWebAssetsFolder()));
		Services.getInstance().getStorageService().cloneActorLogos(target, cloningMap);
	}
	
	
	public List<IANUSActivity> getActivities(WorkflowIP wfip) throws Exception{
		List<IANUSActivity> activityList = new ArrayList<IANUSActivity>();
		List<Task> taskList = null;
		String processInstanceId = wfip.getProcessInstanceId();
		if(processInstanceId != null) {
			activityList = Services.getInstance().getDaoService().getIANUSActivitiesByProcessInstanceId(processInstanceId);
			taskList = Services.getInstance().getProcessEngineService().getProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
		}
		
		if(taskList != null) for(Task task : taskList){
			if(!listContainsTask(activityList, task)){
				IANUSActivity activity = new IANUSActivity((TaskEntity)task, null);
				activityList.add(activity);
			}
		}
		return activityList;
	}
	
	private boolean listContainsTask(List<IANUSActivity> list, Task task){
		
		for(IANUSActivity act : list){
			if(StringUtils.equals(act.getTaskId(), task.getId())){
				return true;
			}
		}
		
		return false;
	}
	
	public void completeTask(IANUSActivity activity, Map<String, Object> taskVariables/*, IANUSUser user*/) throws Exception{
		activity.setEndTime(new Date());
		Services.getInstance().getDaoService().saveDBEntry(activity);
		for(String variableName : taskVariables.keySet()){
			Object obj = taskVariables.get(variableName);
			Services.getInstance().getProcessEngineService().getProcessEngine().getRuntimeService().setVariable(activity.getProcessInstanceId(), variableName, obj);
		}
		
		Thread t = new Thread(new MSThread(getProcessEngine().getTaskService(), activity.getTaskId(), new HashMap<String, Object>()));
		t.start();
	}

	public static void main(String[] args) {
	
	}

	
	
}
