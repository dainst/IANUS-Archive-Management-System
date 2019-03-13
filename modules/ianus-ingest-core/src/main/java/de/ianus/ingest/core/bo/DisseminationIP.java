package de.ianus.ingest.core.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.ianus.ingest.core.Services;

/**
 * @author jurzua
 *
 */
@Entity
@Table(name = "DisseminationIP")
public class DisseminationIP  extends WorkflowIP implements Serializable{
	private static final long serialVersionUID = -2050973479865053238L;
	
	@Column(name="aipId")
	protected Long aipId;
	
	@Column(name="sipId")
	protected Long sipId;
	
	@Transient
	protected SubmissionIP sip;
	
	@Transient
	protected ArchivalIP aip;
	
	public void setSip(SubmissionIP sip) {
		this.sip = sip;
		if(this.sip != null){
			this.sipId = this.sip.getId();
		}
	}

	public void setAip(ArchivalIP aip) {
		this.aip = aip;
		if(aip != null){
			this.aipId = aip.getId();
		}
	}
	
	public Long getAipId() {
		return aipId;
	}

	public void setAipId(Long aipId) {
		this.aipId = aipId;
	}

	public ArchivalIP getAip() {
		if(this.aip == null) 
			this.aip = Services.getInstance().getDaoService().getAip(this.aipId);
		return aip;
	}

	public Long getSipId() {
		return sipId;
	}

	public void setSipId(Long sipId) {
		this.sipId = sipId;
	}

	public SubmissionIP getSip() {
		if(this.sip == null) {
			this.sip = Services.getInstance().getDaoService().getSip(this.sipId);
		}
		return sip;
	}
	
	@Override
	public String toString() {
	   return ToStringBuilder.reflectionToString(this);
	}
}
