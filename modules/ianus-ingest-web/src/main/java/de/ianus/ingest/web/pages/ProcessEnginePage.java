package de.ianus.ingest.web.pages;

import java.util.List;

import javax.faces.event.ActionEvent;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import de.ianus.ingest.core.ProcessEngineService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.AbstractBean;


/**
 * 
 * @author jurzua
 *
 */
public class ProcessEnginePage extends AbstractBean{

	private List<HistoricProcessInstance> historicProcessInstanceList; 
	
	private List<ProcessInstance> processInstanceList; 
	
	public void listenerUpdateHistoricProcessInstanceList(ActionEvent event){
		try {
			this.historicProcessInstanceList = Services.getInstance().getProcessEngineService().getHistoricProcessInstanceList();
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerUpdateProcessInstanceList(ActionEvent event){
		try {
			this.processInstanceList = Services.getInstance().getProcessEngineService().getProcessInstanceList();
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeployPreIngestWF(ActionEvent event){
		try {
			Services.getInstance().getProcessEngineService().deployPreIngestWF();
			addMsg("Pre-ingest workflow deployed successfully");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeployTransferWF(ActionEvent event){
		try {
			Services.getInstance().getProcessEngineService().deployTransferWF();
			addMsg("Transfer workflow deployed successfully");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void listenerDeployIngestWF(ActionEvent event){
		try {
			Services.getInstance().getProcessEngineService().deployIngestWF();
			addMsg("Ingest workflow deployed successfully");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerDeployDisseminationWF(ActionEvent event){
		try {
			Services.getInstance().getProcessEngineService().deployDisseminationWF();
			addMsg("Pre-ingest workflow deployed successfully");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerCreateSchema(ActionEvent event){
		try {
			Services.getInstance().getTesting().createCamundaDB();
			addMsg("Database created");
			ProcessEngineService.createDBSchema();
			addMsg("Database schema created");
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerRemoveSubmissions(ActionEvent event){
		try {
			Services.getInstance().getTesting().removeAllSubmissions();
		} catch (Exception e) {
			
		}
	}

	public List<HistoricProcessInstance> getHistoricProcessInstanceList() {
		return historicProcessInstanceList;
	}

	public void setHistoricProcessInstanceList(List<HistoricProcessInstance> historicProcessInstanceList) {
		this.historicProcessInstanceList = historicProcessInstanceList;
	}

	public List<ProcessInstance> getProcessInstanceList() {
		return processInstanceList;
	}

	public void setProcessInstanceList(List<ProcessInstance> processInstanceList) {
		this.processInstanceList = processInstanceList;
	}
	
	
}
