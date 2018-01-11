package de.ianus.ingest.core.bo;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;

/**
 * @author jurzua
 *
 */
@Entity
@Table(name = "WorkflowIP")
public abstract class WorkflowIP  extends InformationPackage implements Serializable{
	private static final long serialVersionUID = -6435484695623008132L;

	public static String NOT_STARTED = "Not started";
	public static String IN_PROGRESS = "In progress";
	public static String FINISHED = "Finished";
	public static String FINISHED_ERRORS = "Finished with errors";
	
	@Column(name="state")
	private String state = NOT_STARTED;

	@Column(name="processInstanceId")
	public String processInstanceId;
	
	public String getSizeFormatted(){
		return Services.getInstance().getStorageService().getIPSizeFormatted(this);
	}
	
	public String getDataSizeFormatted() {
		return Services.getInstance().getStorageService().getDataSizeFormatted(this);
	}
	
	public Integer getDataFilesAmount(){
		return Services.getInstance().getStorageService().getDataFilesAmount(this);
	}
	
	public Long getDataSize() {
		return Services.getInstance().getStorageService().getDataSize(this);
	}
	
	public String getState() {
		return state;
	}
	
	public boolean isFinishedOk(){
		return StringUtils.equals(FINISHED, this.state);
	}
	
	public boolean isInProgress(){
		return StringUtils.equals(IN_PROGRESS, this.state);
	}

	public boolean isNotStarted(){
		return StringUtils.equals(NOT_STARTED, this.state);
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
	
	public boolean hasFiles() throws Exception{
		File dataFolder = new File(this.getDataFolder());
		if(!dataFolder.exists()) return false;
		return dataFolder.list().length > 0;
	}
	
	
	
	
	
	
	
	
}

