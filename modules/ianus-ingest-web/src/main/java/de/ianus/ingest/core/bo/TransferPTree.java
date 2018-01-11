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
public class TransferPTree extends TransferP {

	private static final long serialVersionUID = 1L;
	
	private TransferP transferP;
	
	private List<SubmissionIPTree> submissionIPList = new ArrayList<SubmissionIPTree>();
	
	
	public TransferPTree(TransferP tp) {
		this.transferP = tp;
	}
	
	
	public TransferP getTransferP() {
		return this.transferP;
	}
	
	public List<SubmissionIPTree> getSubmissionIPList() {
		return this.submissionIPList;
	}
	
	public void setSubmissionIPList(List<SubmissionIPTree> list) {
		this.submissionIPList = list;
	}
	
	public void addToSubmissionIPList(SubmissionIPTree sip) {
		this.submissionIPList.add(sip);
	}
	
}
