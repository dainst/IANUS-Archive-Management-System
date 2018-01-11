package de.ianus.ingest.core.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author jurzua
 *
 */
@Entity
@Table(name = "PreArchivalIP")
public class PreArchivalIP  extends WorkflowIP implements Serializable{
	private static final long serialVersionUID = -98929245250235485L;
	
	@Column(name="sipId")
	protected Long sipId;
	
	@Transient
	protected SubmissionIP sip;

	public Long getSipId() {
		return sipId;
	}

	public void setSipId(Long sipId) {
		this.sipId = sipId;
	}

	public SubmissionIP getSip() {
		return sip;
	}

	public void setSip(SubmissionIP sip) {
		if(sip != null){
			this.sipId = sip.getId();
		}
		this.sip = sip;
	}
}
