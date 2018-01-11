package de.ianus.ingest.core.premis.bo;

import de.ianus.ingest.core.bo.files.AbstractFile;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.bo.files.FileGroup;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.metadata.xml.XMLObject;

public class PremisObject extends XMLObject {
	
	public enum ObjectType {
		file, representation, intellectualEntity, bitstream
		/* The smallest unit we're dealing with will be files (ignore bitstream).
		 * One or many files (FileInstance) belong to a representation (FileConcept). 
		 * Both are included by the intellectual entity (DataCollection).
		 * Though each file instance may be considered as an intellectual entity, 
		 * we should not do so in order to keep things simple ;)
		 * As currently there are no events that technically alter the DataCollection 
		 * in a way that needs to be documented, neither the DataCollectionno nor any 
		 * intellectual entity will be recorded at all.
		 */
	}


	public PremisObject(AbstractFile file, PremisObject.ObjectType type) {
		super("premis:object");
		this.addAttribute("xsi:type", type.toString());
		
		// id construction analogue to METS
		String id = "FILE_" + file.getId();
		if(file instanceof FileGroup)
			id = "GROUP_" + file.getId();
		this.addAttribute("xmlID", id);
		
		// PREMIS version info (mandatory)
		this.addAttribute("version", "3.0");
		
		XMLObject identifier = new XMLObject("premis:identifier");
			// TODO: link METS?
		this.addDescendant(identifier);
		
		switch(type) {
		case file:
			XMLObject characteristics = PremisObject.objectCharacteristics(file);
			this.addDescendant(characteristics);
			break;
		case representation:
			// no more mandatory information to add
			break;
		case bitstream:
			break;
		case intellectualEntity:
			break;
		}
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

	
	public static XMLObject objectCharacteristics(AbstractFile file) {
		XMLObject xml = new XMLObject("premis:objectCharacteristics");
			// mandatory: format (formatDesignation &| formatRegistry)
			XMLObject format = new XMLObject("premis:format");
				XMLObject registry = new XMLObject("premis:formatRegistry");	// formatRegistryComplexType
				if(file instanceof FileInstance) {
					XMLObject name = Premis.stringPlusAuthority("formatRegistryName", "PRONOM", 
							"The National Archives", 
							"http://www.nationalarchives.gov.uk/",
							"http://www.nationalarchives.gov.uk/PRONOM/Default.aspx");
					// value URI not available for PRONOM identifiers, due to lack of REST interface on their end
					XMLObject key = Premis.stringPlusAuthority("premis:formatRegistryKey", 
							((FileInstance) file).getPuid(), 
							"PRONOM", 
							"http://www.nationalarchives.gov.uk/PRONOM/PUID/proPUIDSearch.aspx?status=new", null);
					//optional: XMLObject role ...
					registry.addDescendant(name);
					registry.addDescendant(key);
					//registry.addAttribute("simpleLink", "");
				}
				else if(file instanceof FileGroup) {
					//TODO
				}
				
			format.addDescendant(registry);
		xml.addDescendant(format);
		
		return xml;
	}
}
