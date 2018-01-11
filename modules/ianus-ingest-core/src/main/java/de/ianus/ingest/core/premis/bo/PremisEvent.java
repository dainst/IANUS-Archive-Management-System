package de.ianus.ingest.core.premis.bo;

import de.ianus.ingest.core.bo.files.AgentEventLink;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.bo.files.ObjectEventLink;
import de.ianus.metadata.xml.XMLObject;

public class PremisEvent extends XMLObject {
	
	
	public PremisEvent(Event event) {
		super("premis:event");
		this.addAttribute("version", "3.0");
		this.addAttribute("xmlID", "EVENT_" + event.getId());
		XMLObject identifier = new XMLObject("premis:eventIdentifier");
			identifier.addDescendant(Premis.stringPlusAuthority("premis:eventIdentifierType", 
					"internal " + event.getEventName() + " Event.id", 
					"IANUS", "https://www.ianus-fdz.de", null));
			identifier.addDescendant(new XMLObject("premis:eventIdentifierValue", event.getId().toString()));
			//TODO: identifier.addAttribute("simpleLink", "?"); - eg workflow diagram?
		this.addDescendant(identifier);
		this.addDescendant(Premis.stringPlusAuthority("premis:eventType", 
				event.getEventName(), 
				"IANUS", "https://www.ianus-fdz.de", null));
		this.addDescendant(new XMLObject("premis:eventDateTime", event.getFormattedLastChange()));
		if(event.getOutcomeDescription() != null && !event.getOutcomeDescription().equals("")) {
			XMLObject detail = new XMLObject("premis:eventDetailInformation");
			detail.addDescendant(new XMLObject("premis:eventDetail", event.getOutcomeDescription()));
			this.addDescendant(detail);
		}
		XMLObject outcome = new XMLObject("premis:eventOutcomeInformation");
			outcome.addDescendant(Premis.stringPlusAuthority("premis:eventOutcome", 
					event.getOutcome(), 
					"IANUS", "https://www.ianus-fdz.de", null));
		this.addDescendant(outcome);
	}
	
	
	public void linkObject(ObjectEventLink link) {
		XMLObject identifier = new XMLObject("premis:linkingObjectIdentifier");
			identifier.addDescendant(Premis.stringPlusAuthority("premis:linkingObjectIdentifierType", 
					"internal " + link.getObjectType() + ".id", 
					"IANUS", "https://www.ianus-fdz.de", null));
			identifier.addDescendant(new XMLObject("premis:linkingObjectIdentifierValue", link.getObjectId().toString()));
			identifier.addDescendant(Premis.stringPlusAuthority("premis:linkingObjectRole", 
					link.getObjectRole(), 
					"IANUS", "https://www.ianus-fdz.de", null));
			String id = (link.getObjectType() == "FileInstance") 
					? "FILE_" + link.getObjectId() 
					: "GROUP_" + link.getObjectId();
			identifier.addAttribute("LinkObjectXmlId", id);
			//identifier.addAttribute("simpleLink", // TODO: file resolver link...);
		this.addDescendant(identifier);
	}
	
	
	public void linkAgent(AgentEventLink link) {
		XMLObject identifier = new XMLObject("premis:linkingAgentIdentifier");
			identifier.addDescendant(Premis.stringPlusAuthority("premis:linkingAgentIdentifierType", 
					"internal " + link.getAgentRole() + " Agent.id", 
					"IANUS", "https://www.ianus-fdz.de", null));
			identifier.addDescendant(new XMLObject("premis:linkingAgentIdentifierValue", link.getAgentId().toString()));
			identifier.addDescendant(Premis.stringPlusAuthority("premis:linkingAgentRole", 
					link.getAgentRole(), 
					"IANUS", "https://www.ianus-fdz.de", null));
			identifier.addAttribute("LinkAgentXmlId", "AGENT_" + link.getAgentId());
			//identifier.addAttribute("simpleLink", // TODO: agent resolver link...);
		this.addDescendant(identifier);
	}
	
	
	
	
	
	
}
