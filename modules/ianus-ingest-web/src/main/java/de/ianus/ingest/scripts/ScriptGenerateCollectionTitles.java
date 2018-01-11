package de.ianus.ingest.scripts;

import java.util.List;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.metadata.bo.DataCollection;

public class ScriptGenerateCollectionTitles {

	
	public static void execute() throws Exception{
		
		List<WorkflowIP> tpList = Services.getInstance().getDaoService().getTransferPList();
		
		for(InformationPackage tp : tpList){
			processTranferP((TransferP)tp);
		}
	}
	
	private static void processTranferP(TransferP tp) throws Exception{
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(tp.getMetadataId());
		tp.setCollectionIdentifier(dc.getCollectionIdentifier());
		tp.setCollectionLabel(dc.getCollectionLabel());
		
		System.out.println("Saving = " + tp.toString());
		System.out.println("\t" + tp.getCollectionIdentifier() + "\t" + tp.getCollectionLabel());
		Services.getInstance().getDaoService().saveDBEntry(tp);
		
		List<SubmissionIP> sipList = Services.getInstance().getDaoService().getSip(tp);
		
		for(SubmissionIP sip : sipList){
			processSubmissionSIP(sip);
		}
	}

	private static void processSubmissionSIP(SubmissionIP sip) throws Exception{
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(sip.getMetadataId());
		sip.setCollectionIdentifier(dc.getCollectionIdentifier());
		sip.setCollectionLabel(dc.getCollectionLabel());
		
		System.out.println("\tSaving = " + sip.toString());
		System.out.println("\t\t" + sip.getCollectionIdentifier() + "\t" + sip.getCollectionLabel());
		Services.getInstance().getDaoService().saveDBEntry(sip);
		
		List<DisseminationIP> dipList = Services.getInstance().getDaoService().getDip(sip);
		
		for(DisseminationIP dip : dipList){
			processSubmissionDIP(dip);
		}
	}
	
	
	private static void processSubmissionDIP(DisseminationIP dip) throws Exception{
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(dip.getMetadataId());
		dip.setCollectionIdentifier(dc.getCollectionIdentifier());
		dip.setCollectionLabel(dc.getCollectionLabel());
		
		System.out.println("\t\tSaving = " + dip.toString());
		System.out.println("\t\t\t" + dip.getCollectionIdentifier() + "\t" + dip.getCollectionLabel());
		Services.getInstance().getDaoService().saveDBEntry(dip);
		
	}
}
