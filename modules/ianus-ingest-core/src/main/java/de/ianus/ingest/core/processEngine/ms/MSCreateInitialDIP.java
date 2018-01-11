package de.ianus.ingest.core.processEngine.ms;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.DisseminationIP;

public class MSCreateInitialDIP extends AbstractTask implements ActivityBehavior{

	private static final Logger logger = LogManager.getLogger(MSCreateInitialDIP.class);
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			DisseminationIP dip = (DisseminationIP)this.wfip;
			ArchivalIP aip =  Services.getInstance().getDaoService().getAip(dip.getAipId());
			
			
			
			logger.info("copying archive folder from aip to dip");
			FileUtils.copyDirectory(new File(aip.getArchivalFolder()), new File(dip.getDataFolder()));

			// the copy of WebAssetsFolder is done in ProcessEngineServicestartIngestWF, 
			// because we need to update the actor logo file names, which reference the belonging database IDs
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sf_create_dip_ok");
			finishExecution();
		}
	}
}