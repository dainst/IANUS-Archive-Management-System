package de.ianus.ingest.web.pages;

import java.util.ArrayList;
import java.util.List;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.SubmissionIPTree;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.TransferPTree;
import de.ianus.ingest.core.bo.WorkflowIP;

/**
 * 
 * @author hschmeer
 *
 */
public class OverviewDCPage  extends AbstractIPPage {
	
	public static String PAGE_NAME = "overviewDC";
	
	public void load() {
		
	}
	
	public List<WorkflowIP> getTree() {
		this.loadTree();
		return this.ipList;
	}
	
	private void loadTree() {
		this.ipList = new ArrayList<WorkflowIP>();
		List<WorkflowIP> tpList = Services.getInstance().getDaoService().getTransferPList();
		if(tpList != null) for(InformationPackage item : tpList)
			this.ipList.add(new TransferPTree((TransferP)item));
		
		if(this.ipList != null) for(InformationPackage tp : this.ipList) {
			List<SubmissionIP> sipList = Services.getInstance().getDaoService().getSip(((TransferPTree)tp).getTransferP());
			if(sipList != null) for(SubmissionIP sip : sipList) {
				SubmissionIPTree sipTree = new SubmissionIPTree(sip);
				((TransferPTree)tp).addToSubmissionIPList(sipTree);
				
				List<DisseminationIP> dipList = Services.getInstance().getDaoService().getDipList(sip);
				sipTree.setDisseminationIPList(dipList);
				
				List<ArchivalIP> aipList = Services.getInstance().getDaoService().getAip(sip);
				sipTree.setArchivalIPList(aipList);
			}
		}
	}
}
