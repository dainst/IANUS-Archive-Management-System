package de.ianus.ingest.core.bo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.ianus.ingest.core.Services;

/**
 * @author jurzua
 *
 */
@Entity
@Table(name = "ArchivalIP")
public class ArchivalIP  extends WorkflowIP implements Serializable{
	private static final long serialVersionUID = 6499975556415445368L;

	@Column(name="sipId")
	protected Long sipId;
	
	@Column(name="archivalFolderName")
	protected String archivalFolderName;
	
	@Column(name="derivedFolderName")
	protected String derivedFolderName;
	
	@Transient
	protected SubmissionIP sip;
	
	@Transient
	protected PreArchivalIP preAip;
	
	
	public void setSip(SubmissionIP sip) {
		this.sip = sip;
		if(this.sip != null){
			this.sipId = this.sip.getId();
		}
	}
	
	public void generateLocalFolderNames() throws Exception{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.archivalFolderName = df.format(new Date()) + "_archive";
	}
	
	public String getArchivalFolder() throws Exception{
		return getDataFolder()+ "/" + this.archivalFolderName;
	}

	public String getDerivedFolder() throws Exception{
		return getDataFolder()+ "/" + this.derivedFolderName;
	}
	
	public Long getSipId() {
		return sipId;
	}

	public void setSipId(Long sipId) {
		this.sipId = sipId;
	}

	public SubmissionIP getSip() {
		if(this.sip == null) 
			this.sip = Services.getInstance().getDaoService().getSip(this.sipId);
		return sip;
	}

	public String getArchivalFolderName() {
		return archivalFolderName;
	}

	public void setArchivalFolderName(String archivalFolderName) {
		this.archivalFolderName = archivalFolderName;
	}

	public String getDerivedFolderName() {
		return derivedFolderName;
	}

	public void setDerivedFolderName(String derivedFolderName) {
		this.derivedFolderName = derivedFolderName;
	}
	
	
}
