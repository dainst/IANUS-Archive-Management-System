package de.ianus.ingest.core.xml;

import java.util.List;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.AgentEventLink;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.bo.files.ObjectEventLink;
import de.ianus.ingest.core.mets.bo.DmdSec;
import de.ianus.ingest.core.mets.bo.FileSec;
import de.ianus.ingest.core.mets.bo.MetsFrame;
import de.ianus.ingest.core.mets.bo.MetsHdr;
import de.ianus.ingest.core.mets.bo.StructMap;
import de.ianus.ingest.core.premis.bo.Premis;
import de.ianus.ingest.core.premis.bo.PremisAgent;
import de.ianus.ingest.core.premis.bo.PremisEvent;
import de.ianus.ingest.core.premis.bo.PremisObject;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.xml.XMLObject;





public class XMLService {
	
	
	
	private WorkflowIP ip = null;
	private DataCollection dc = null;
	
	
	
	
	public XMLService(WorkflowIP wip) throws Exception {
		this.ip = wip;
		this.dc = Services.getInstance().getMDService().getDataCollection(this.ip.getMetadataId());
	}
	
	
	// constructor for easier testing (injection of a mocked DataCollection)
	public XMLService(WorkflowIP wfip, DataCollection dc) {
		this.ip = wfip;
		this.dc = dc;
	}
	
	
	public void createMETS() throws Exception {
		
		// ianus.xml will be referenced in DmdSection as descriptive metadata
		XMLObject dc = this.dc.toXMLObject();
		dc.toFile(this.ip.getMetadataFolder() + "/ianus.xml");
		
		XMLObject mets = new MetsFrame(this.dc);
			XMLObject metsHdr = new MetsHdr(this.dc);
			XMLObject fileSec = new FileSec(this.ip);
			XMLObject dmd = new DmdSec(this.dc);
			XMLObject structMap = new StructMap(this.ip, this.dc, "physical");
			XMLObject logicalMap = new StructMap(this.ip, this.dc, "logical");
		mets.addDescendant(metsHdr);
		mets.addDescendant(fileSec);
		mets.addDescendant(dmd);
		mets.addDescendant(structMap);
		mets.addDescendant(logicalMap);
		
		mets.toFile(this.ip.getMetadataFolder() + "/mets.xml");
	}
	
	
	public void createPREMIS() throws Exception {
		Premis premis = new Premis();
		
		List<Event> events = Services.getInstance().getDaoService().getEventList(this.ip);
		if(events != null) for(Event event : events) {
			PremisEvent premisEvent = premis.addEvent(event);
			
			List<ObjectEventLink> objectEventList = Services.getInstance().getDaoService().getObjectEventLinkList(event);
			if(objectEventList != null) for(ObjectEventLink link : objectEventList) {
				PremisObject premisObject = premis.addObject(link);
				
				premisObject.linkEvent(event);
				premisEvent.linkObject(link);
			}
			
			List<AgentEventLink> agentEventList = Services.getInstance().getDaoService().getAgentEventLinkList(event);
			if(agentEventList != null) for(AgentEventLink link : agentEventList) {
				PremisAgent premisAgent = premis.addAgent(link);
				premisAgent.linkEvent(event);
				premisEvent.linkAgent(link);
			}
		}
		
		premis.toFile(this.ip.getMetadataFolder() + "/premis.xml");
	}
	
	
	
	
	
	
	
	
}