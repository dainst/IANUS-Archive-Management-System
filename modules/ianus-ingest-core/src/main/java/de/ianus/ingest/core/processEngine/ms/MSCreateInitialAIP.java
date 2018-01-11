package de.ianus.ingest.core.processEngine.ms;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.SubmissionIP;

public class MSCreateInitialAIP extends AbstractTask implements ActivityBehavior{

	private static final Logger logger = LogManager.getLogger(MSCreateInitialAIP.class);
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			ArchivalIP aip = (ArchivalIP)this.wfip;
			SubmissionIP sip =  Services.getInstance().getDaoService().getSip(aip.getSipId());
			
			logger.info("copying data folder from sip to aip");
			FileUtils.copyDirectory(new File(sip.getDataFolder()), new File(aip.getDataFolder()));
			FileUtils.copyDirectory(new File(sip.getMetadataFolder()), new File(aip.getMetadataFolder()));
			FileUtils.copyDirectory(new File(sip.getLogsFolder()), new File(aip.getLogsFolder()));
			
			// we are just copying over the files, file instances will be created manually after curational tasks...
			FileUtils.copyDirectory(new File(aip.getOriginalDataFolder()), new File(aip.getArchivalFolder()));

			//the copy of WebAssetsFolder is done in ProcessEngineServicestartIngestWF / adaption of the actor logo IDs
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sf_create_aip_ok");
			finishExecution();
		}
	}
}
