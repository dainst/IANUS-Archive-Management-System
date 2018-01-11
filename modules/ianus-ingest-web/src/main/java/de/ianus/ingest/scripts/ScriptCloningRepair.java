package de.ianus.ingest.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.metadata.bo.DataCollection;

/**
 * 
 * de.ianus.ingest.scripts.ScriptCloningRepair
 * 
 * mvn exec:java -Dexec.mainClass="de.ianus.ingest.scripts.ScriptCloningRepair"
 * 
 * @author jurzua
 *
 */
public class ScriptCloningRepair {

	
	public static void execute() throws Exception{
		List<WorkflowIP> tpList = Services.getInstance().getDaoService().getTransferPList();
		for(InformationPackage tp : tpList){
			processTranferP((TransferP)tp);
		}
	}
	
	private static void processTranferP(TransferP tp) throws Exception{
		List<SubmissionIP> sipList = Services.getInstance().getDaoService().getSip(tp);
		for(SubmissionIP sip : sipList){
			processSubmissionSIP(tp, sip);
		}
	}
	
	private static void processSubmissionSIP(TransferP tp, SubmissionIP sip) throws Exception{
		
		List<DisseminationIP> dipList = Services.getInstance().getDaoService().getDip(sip);
		if(haveCommonMD(tp, sip, dipList)){
			System.out.println("***************************");
			System.out.println("***************************");
			System.out.println("***************************");
			System.out.println("***************************");
			System.out.println("[" + tp.getMetadataId() + "] " + tp + " - " + tp.getCollectionLabel());
			System.out.println("[" + sip.getMetadataId() + "] " + sip + " - " + sip.getCollectionLabel());
			for(DisseminationIP dip : dipList){
				System.out.println("\t[" + dip.getMetadataId() + "] " + dip + " - " + dip.getCollectionLabel());
			}
			processCommonMD(tp, sip, dipList);
			System.out.println();
			System.out.println();
		}
	}
	
	private static void processCommonMD(TransferP tp, SubmissionIP sip, List<DisseminationIP> dipList){
		
		Map<Long, DataCollection> dcToDelete = new HashMap<Long, DataCollection>();
		
		try {
			
			//TP
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(tp.getMetadataId());
			DataCollection cloneDc = Services.getInstance().getMDService().cloneDataCollection0(dc).getDc();
			tp.setMetadataId(cloneDc.getId());
			Services.getInstance().getDaoService().saveDBEntry(tp);
			dcToDelete.put(dc.getId(), dc);
			
			//SIP
			dc = Services.getInstance().getMDService().getDataCollection(sip.getMetadataId());
			cloneDc = Services.getInstance().getMDService().cloneDataCollection0(dc).getDc();
			sip.setMetadataId(cloneDc.getId());
			Services.getInstance().getDaoService().saveDBEntry(sip);
			dcToDelete.put(dc.getId(), dc);
			
			//DIP List
			for(DisseminationIP dip : dipList){
				dc = Services.getInstance().getMDService().getDataCollection(dip.getMetadataId());
				cloneDc = Services.getInstance().getMDService().cloneDataCollection0(dc).getDc();
				dip.setMetadataId(cloneDc.getId());
				Services.getInstance().getDaoService().saveDBEntry(dip);
				dcToDelete.put(dc.getId(), dc);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(DataCollection dc0 : dcToDelete.values()){
			try {
				Services.getInstance().getMDService().deleteDataCollection(dc0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method returns true if at least one the IPs have a common MD identifier 
	 * @return
	 */
	private static boolean haveCommonMD(TransferP tp, SubmissionIP sip, List<DisseminationIP> dipList){
		if(tp.getMetadataId().equals(sip.getMetadataId())){
			return true;
		}
		for(DisseminationIP dip : dipList){
			if(tp.getMetadataId().equals(dip.getMetadataId()) ||
					sip.getMetadataId().equals(dip.getMetadataId())){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args){
		try {
			System.out.println("#######################");
			execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	
}
