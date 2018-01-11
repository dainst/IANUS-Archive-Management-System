package de.ianus.ingest.core.processEngine.ms;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.xml.XMLService;

public class MSCreateInitialSIP extends AbstractTask implements ActivityBehavior{

	private static final Logger logger = LogManager.getLogger(MSCreateInitialSIP.class);
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			SubmissionIP sip = (SubmissionIP)this.wfip;
			TransferP tp =  Services.getInstance().getDaoService().getTransfer(sip.getTpId());
			
			logger.info("copying data folder from tp to sip");
			FileUtils.copyDirectory(new File(tp.getDataFolder()), new File(sip.getDataFolder()));
			FileUtils.copyDirectory(new File(tp.getMetadataFolder()), new File(sip.getMetadataFolder()));
			FileUtils.copyDirectory(new File(tp.getLogsFolder()), new File(sip.getLogsFolder()));
			
			
			long start = System.currentTimeMillis();
			logger.info("creating concept files and  files instances");
			//IANUSFileUtils.initConceptualFiles(sip);
			Services.getInstance().getDaoService().initConceptualFiles(sip);
			long diff = System.currentTimeMillis() - start;
			
			StringBuilder sb = new StringBuilder("\n");
			sb.append("*******************************************************\n");
			sb.append("InitConceptualFiles - time execution [ms]" + diff + "\n");
			sb.append("*******************************************************\n");
			logger.info(sb.toString());
			
			this.output.print("Generating METS file.");
			XMLService mets = new XMLService(this.wfip);
			mets.createMETS();
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("CreateInitialSIP_ok");
			finishExecution();
		}
	}
}
