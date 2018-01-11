/**
 * 
 */
package de.ianus.ingest.core.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hendrik
 *
 */
public class SubmissionIPTree extends SubmissionIP {
	
	
	private static final long serialVersionUID = 1L;
	
	private List<DisseminationIP> disseminationIPList = new ArrayList<DisseminationIP>();
	
	private List<ArchivalIP> archivalIPList = new ArrayList<ArchivalIP>();
	
	private SubmissionIP submissionIP = null;
	
	
	public SubmissionIPTree(SubmissionIP sip) {
		this.submissionIP = sip;
	}
	
	
	public SubmissionIP getSubmissionIP() {
		return this.submissionIP;
	}
	
	public List<DisseminationIP> getDisseminationIPList() {
		return this.disseminationIPList;
	}
	
	public void setDisseminationIPList(List<DisseminationIP> list) {
		this.disseminationIPList = list;
	}
	
	public List<ArchivalIP> getArchivalIPList() {
		return this.archivalIPList;
	}
	
	public void setArchivalIPList(List<ArchivalIP> list) {
		this.archivalIPList = list;
	}
	
}
