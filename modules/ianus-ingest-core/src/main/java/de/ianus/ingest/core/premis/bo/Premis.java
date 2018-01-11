package de.ianus.ingest.core.premis.bo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.files.AbstractFile;
import de.ianus.ingest.core.bo.files.Agent;
import de.ianus.ingest.core.bo.files.AgentEventLink;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.bo.files.FileGroup;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.bo.files.ObjectEventLink;
import de.ianus.metadata.xml.XMLObject;

public class Premis extends XMLObject {

	private Map<Long, PremisObject> fileMap = null;
	private Map<Long, PremisObject> groupMap = null;
	private Map<Long, PremisAgent> agentMap = null;

	
	public Premis() {
		super("premis:premis");
		
		this.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		this.addAttribute("xmlns:premis", "http://www.loc.gov/premis/v3");
		this.addAttribute("xsi:schemaLocation", "http://www.loc.gov/premis/v3 https://www.loc.gov/standards/premis/premis.xsd");
		this.addAttribute("version", "3.0");
		
		this.fileMap = new HashMap<Long, PremisObject>();
		this.groupMap = new HashMap<Long, PremisObject>();
		this.agentMap = new HashMap<Long, PremisAgent>();
		
		// must contain minimum 1 descendant of type premis:object
	}
	
	/**
	 * Override the parent toFile method in favor of sequentially printing out the elements by kind
	 */
	@Override
	public void toFile(String filename) {
		for(Entry<Long, PremisObject> entry : this.fileMap.entrySet())
			this.addDescendant(entry.getValue());
		for(Entry<Long, PremisObject> entry : this.groupMap.entrySet())
			this.addDescendant(entry.getValue());
		for(Entry<Long, PremisAgent> entry : this.agentMap.entrySet())
			this.addDescendant(entry.getValue());
		super.toFile(filename);
	}
	
	@Override
	public String toString() {
		for(Entry<Long, PremisObject> entry : this.fileMap.entrySet())
			this.addDescendant(entry.getValue());
		for(Entry<Long, PremisObject> entry : this.groupMap.entrySet())
			this.addDescendant(entry.getValue());
		for(Entry<Long, PremisAgent> entry : this.agentMap.entrySet())
			this.addDescendant(entry.getValue());
		return super.toString();
	}
	
	
	public PremisObject addObject(ObjectEventLink link) {
		PremisObject premisObject = this.getObjectFromMap(link);
		if(premisObject == null) {
			AbstractFile object = Services.getInstance().getDaoService().getObject(link);
			if(object instanceof FileInstance) {
				premisObject = new PremisObject(object, PremisObject.ObjectType.file);
				this.setObjectToMap(object, premisObject);
			}
			else if(object instanceof FileGroup) { 
				premisObject = new PremisObject(object, PremisObject.ObjectType.representation);
				this.setObjectToMap(object, premisObject);
			}
		}
		//this.addDescendant(object);
		// objects will be appended at once before creation of the file or string, 
		// in order to have all elements of same kind in one block
		return premisObject;
	}


	public PremisEvent addEvent(Event event) {
		PremisEvent premisEvent = new PremisEvent(event);
		this.addDescendant(premisEvent);
		return premisEvent;
	}


	public PremisAgent addAgent(AgentEventLink link) {
		PremisAgent premisAgent = this.getAgentFromMap(link);
		if(premisAgent == null) {
			Agent agent = Services.getInstance().getDaoService().getAgentById(link.getAgentId());
			premisAgent = new PremisAgent(agent);
		}
		return premisAgent;
	}
	
	
	private void setObjectToMap(AbstractFile object, PremisObject premisObject) {
		if(object instanceof FileInstance)
			this.fileMap.put(object.getId(), premisObject);
		else if(object instanceof FileGroup)
			this.groupMap.put(object.getId(), premisObject);
	}


	private PremisObject getObjectFromMap(ObjectEventLink link) {
		PremisObject out = null;
		switch(link.getObjectType()) {
		case "FileInstance":
			out = this.fileMap.get(link.getObjectId());
			break;
		case "FileGroup":
			out = this.groupMap.get(link.getObjectId());
		}
		return out;
	}
	
	
	private PremisAgent getAgentFromMap(AgentEventLink link) {
		return this.agentMap.get(link.getAgentId());
	}
		
	
	
	/**
	 * Implementation of stringPlusAuthorityComplexType: all parameters except name are optional (null, empty String)
	 * @param name
	 * @param value		- optional
	 * @param authority - optional
	 * @param authURI	- optional
	 * @param valURI	- optional
	 * @return
	 */
	protected static XMLObject stringPlusAuthority(String name, String value, String authority, String authURI, String valURI) {
		XMLObject xml = new XMLObject(name, value);
			if(authority != null && authority.length() > 0)
				xml.addAttribute("authority", authority);
			if(authURI != null && authURI.length() > 0)
				xml.addAttribute("authorityURI", authURI);
			if(valURI != null && valURI.length() > 0)
				xml.addAttribute("valueURI", valURI);
		return xml;
	}
	
	

}
