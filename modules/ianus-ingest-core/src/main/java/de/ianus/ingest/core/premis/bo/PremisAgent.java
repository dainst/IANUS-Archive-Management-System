package de.ianus.ingest.core.premis.bo;

import de.ianus.ingest.core.bo.files.Agent;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.metadata.xml.XMLObject;

public class PremisAgent extends XMLObject {

	public PremisAgent(Agent agent) {
		super("premis:agent");
		
		this.addAttribute("version", "3.0");
		this.addAttribute("xmlID", "AGENT_" + agent.getId());
		
		XMLObject identifier = new XMLObject("premis:eventIdentifier");
			identifier.addDescendant(Premis.stringPlusAuthority("premis:agentIdentifierType", 
					"internal Agent.id", 
					"IANUS", "https://www.ianus-fdz.de", null));
			identifier.addDescendant(new XMLObject("premis:agentIdentifierValue", agent.getId().toString()));
			//TODO: identifier.addAttribute("simpleLink", "?");
		this.addDescendant(identifier);
		this.addDescendant(new XMLObject("premis:agentName", agent.getAgentName()));
	}

	public void linkEvent(Event event) {
		XMLObject identifier = new XMLObject("premis:linkingEventIdentifier");
			identifier.addDescendant(Premis.stringPlusAuthority("premis:linkingEventIdentifierType",
					"internal " + event.getEventName() + " Event.id", 
					"IANUS", "https://www.ianus-fdz.de", null));
			identifier.addDescendant(new XMLObject("premis:linkingEventIdentifierValue", event.getId().toString()));
			identifier.addAttribute("LinkObjectXmlId", "EVENT_" + event.getId());
		this.addDescendant(identifier);
	}

}
