package de.ianus.ingest.core.processEngine.ms;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.files.Agent;
import de.ianus.ingest.core.bo.files.Agent.AgentType;
import de.ianus.ingest.core.bo.files.AgentEventLink;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.processEngine.ms.utils.TechnicalMetadataExtractionProcessor;


/**
 * This class is not more used by the ingest WF, but it is a good example for the use of agents and events.
 * @author hschmeer
 *
 */

public class MSExtractTechnicalMetadata2  extends AbstractTask implements ActivityBehavior{

	private static final Logger logger = LogManager.getLogger(MSExtractTechnicalMetadata2.class);
	
	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);
		
		try{
			DAOService dao = Services.getInstance().getDaoService();
			
			// create the PREMIS related objects
			Event event = new Event(this.wfip, "technical_metadata_extraction", Event.Outcome.ok);
			dao.saveDBEntry(event);
			Agent agent = dao.getSoftwareAgentByName(this.getClass().getSimpleName());
			if(agent == null) {
				agent = new Agent(this.getClass().getSimpleName(), AgentType.software);
				dao.saveDBEntry(agent);
			}
			AgentEventLink ael = new AgentEventLink(agent, event, AgentEventLink.AgentRole.executing_program);
			dao.saveDBEntry(ael);
			
			// pass the event to the processor, so that an EventObjectLink will be created on each FileInstance
			TechnicalMetadataExtractionProcessor proc = new TechnicalMetadataExtractionProcessor(this.wfip, this.output, event);
			proc.run();
			
			
		}catch (Exception e){
			this.setException(e);
		}finally {
			transition = execution.getActivity().findOutgoingTransition("sf_extract_technical_metadata_ok2");
			this.finishExecution();
		}
	}
}