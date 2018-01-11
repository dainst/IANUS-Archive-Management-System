package de.ianus.ingest.core.mets.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.xml.XMLObject;

/*
 * **************************************************************************************************************************************
 * The METS Header element allows you to record 																						*
 * minimal descriptive metadata about the METS object itself 																			*
 * within the METS document.  																											*
 * This metadata includes the date of creation for the METS document, 																	*
 * the date of its last modification, and a status for the METS document. 																*
 * You may also record the names of one or more agents who have played some role with respect to the METS document, 					*
 * specify the role they have played, and add a small note regarding their activity.  													*
 * Finally, you may record a variety of alternative identifiers for the METS document 													*
 * to supplement the primary identifier for the METS document recorded in the OBJID attribute on the METS root element.  				*
 * A small example of a METS Header might look like the following:																		*
 * 																																		*
 * 																																		*
 * <metsHdr CREATEDATE="2003-07-04T15:00:00" RECORDSTATUS="Complete">																	*
 *  <agent ROLE="CREATOR" TYPE="INDIVIDUAL">																							*
 *	 <name>Jerome McDonough</name>																										*
 *  </agent>																															*
 *  <agent ROLE="ARCHIVIST" TYPE="INDIVIDUAL">																							*
 *	 <name>Ann Butler</name>																											*
 *  </agent>																															*
 * </metsHdr>																															*
 * 																																		*
 * The fixed names of attributes and elements are not stored in variables but directly passed as strings in the functions creating them.* 
 * The input from our ips is stored in variables to structure the class and provide a better overview. 									*
 * 																																		*
 * @ TODO Ticket IAN-312																												*
 * !! Must be tested with ip and datacollection -> only tested with fake data atm!! 															*																																		*
 * **************************************************************************************************************************************
 */

public class MetsHdr extends XMLObject {

	public MetsHdr(DataCollection dc) {

		/*
		 * minOccurs="0" metsHdr: METS Header. Like a TEI Header, the METS
		 * Header element records metadata about the METS document itself (not
		 * the digital library object that the METS document encodes). It has
		 * two possible subsidiary elements, agent (document agent) and
		 * altRecordID (alternative Record ID).
		 * 
		 * may contain | <agent> | <altRecordID> contained within | <mets>
		 */

		// create the <mets:metsHdr> element
		super("mets:metsHdr");

		// get the information for values and input for elements and attributes
		// within <mets:metsHdr>
		getValues(dc);

		// ______Attributes for <metsHdr> Element__________

		// ID: an optional XML ID value. An attribute that uniquely identifies the <metsHdr> element which would allow the element
		// to be referenced unambiguously from another element or document via an IDREF or an XPTR. 
		// ID: xsd:ID optional
		this.addAttribute("ID", MainAttributeIDValue);

		// ADMID: An attribute that provides values for administrative metadata elements which apply to
		// the current descriptive or administrative metadata. 
		// ADMID: xsd:ID optional
		this.addAttribute("ADMID", MainAttributeAdmIDValue);
		
		// CREATEDATE: an optional dateTime specifying when the METS document was created.
		// CREATEDATE: xsd:dateTime optional
		this.addAttribute("CREATEDATE", MainAttributeCreateDateValue);

		// LASTMODDATE: an optional dateTime specifying when the METS document was last modified;
		// LASTMODDATE: xsd:dateTime optional
		this.addAttribute("LASTMODDATE", MainAttributeLastModDateValue);

		// RECORDSTATUS: an optional string indicating the status of the METS
		// document, to be used mainly for internal processing purposes.
		// RECORDSTATUS: xsd:string optional
		this.addAttribute("RECORDSTATUS", MainAttributeRecordStatusValue);

				
		// ______Element within <metsHdr>__________
		
		/*
		 * minOccurs="0" maxOccurs="unbounded" agent: METS agent. The agent
		 * element allows for various parties and their roles with respect to
		 * the METS document to be recorded. may contain | <name> | <note>
		 * contained within | <metsHdr>
		 */

		// ______Attributes <agent>__________

		// ROLE: required | CREATOR | EDITOR | ARCHIVIST | PRESERVATION |
		// DISSEMINATOR | CUSTODIAN | IPOWNER | OTHER
		/*
		 * ROLE: a required attribute specifying the role played by the agent
		 * with respect to the METS object. ROLE must have one of the following
		 * seven values: CREATOR: an agent responsible for creating the METS
		 * document EDITOR: an agent responsible for editing the METS document
		 * ARCHIVIST: an agent with archival responsibility for the METS
		 * document and/or the original sources used to create the METS object
		 * PRESERVATION: an agent responsible for preserving the METS object
		 * and/or the original sources used to create the METS object
		 * DISSEMINATOR: an agent responsible for distributing or publishing the
		 * METS object CUSTODIAN: an agent with custodial responsibility for the
		 * METS object IPOWNER: an agent with intellectual property rights in
		 * the METS object or some of its components. OTHER: an agent with other
		 * rights/responsibilities with respect to the METS object not set forth
		 * above.
		 */

		// *********** CREATOR ***********
		// get all CREATORs with the method "getActors"
		for (XMLObject ElementCreatorAgent : getActors(dc, "CREATOR")) {
			this.addDescendant(ElementCreatorAgent);
		}

		// *********** EDITOR ***********
		XMLObject ElementEditorAgent = new XMLObject("mets:agent");
		ElementEditorAgent.addAttribute("ROLE", AgentAttributeRoleEditorValue);
		ElementEditorAgent.addAttribute("TYPE", AgentAttributeTypeEditorValue);
		ElementEditorAgent.addAttribute("OTHERTYPE", AgentAttributeOtherTypeEditorValue);
		XMLObject ElementAgentEditorName = new XMLObject("mets:name", ElementAgentNameEditorValue);
		ElementEditorAgent.addDescendant(ElementAgentEditorName);
		this.addDescendant(ElementEditorAgent);

		// *********** ARCHIVIST ***********
		// get all ARCHIVISTs with the method "getActors"
		for (XMLObject ElementCreatorAgent : getActors(dc, "ARCHIVIST")) {
			this.addDescendant(ElementCreatorAgent);
		}

		// *********** PRESERVATION ***********
		XMLObject ElementPreservationAgent = new XMLObject("mets:agent");
		ElementPreservationAgent.addAttribute("ROLE", AgentAttributeRolePreservationValue);
		ElementPreservationAgent.addAttribute("TYPE", AgentAttributeTypePreservationValue);
		ElementPreservationAgent.addAttribute("OTHERTYPE", AgentAttributeOtherTypePreservationValue);
		XMLObject ElementAgentPreservationName = new XMLObject("mets:name", ElementAgentNamePreservationValue);
		ElementPreservationAgent.addDescendant(ElementAgentPreservationName);
		this.addDescendant(ElementPreservationAgent);

		// *********** DISSEMINATOR ***********
		XMLObject ElementDisseminatorAgent = new XMLObject("mets:agent");
		ElementDisseminatorAgent.addAttribute("ROLE", AgentAttributeRoleDisseminatorValue);
		ElementDisseminatorAgent.addAttribute("TYPE", AgentAttributeTypeDisseminatorValue);
		ElementDisseminatorAgent.addAttribute("OTHERTYPE", AgentAttributeOtherTypeDisseminatorValue);
		XMLObject ElementAgentDisseminatorName = new XMLObject("mets:name", ElementAgentNameDisseminatorValue);
		ElementDisseminatorAgent.addDescendant(ElementAgentDisseminatorName);
		this.addDescendant(ElementDisseminatorAgent);

		// *********** CUSTODIAN ***********
		XMLObject ElementCustodianAgent = new XMLObject("mets:agent");
		ElementCustodianAgent.addAttribute("ROLE", AgentAttributeRoleCustodianValue);
		ElementCustodianAgent.addAttribute("TYPE", AgentAttributeTypeCustodianValue);
		ElementCustodianAgent.addAttribute("OTHERTYPE", AgentAttributeOtherTypeCustodianValue);
		XMLObject ElementAgentCustodianName = new XMLObject("mets:name", ElementAgentNameCustodianValue);
		ElementCustodianAgent.addDescendant(ElementAgentCustodianName);
		this.addDescendant(ElementCustodianAgent);

		// *********** IPOWNER ***********
		// get all ARCHIVISTs with the method "getIPOwner"
		for (XMLObject ElementIPOwner : getIPOwner(dc)) 
		{
			this.addDescendant(ElementIPOwner);
		}
		

		// ______Element and Attributes within <metsHdr>__________

		// minOccurs="0" maxOccurs="unbounded"
		// This element allows for documentation of alternative ID values for
		// the
		// METS document in addition to the primary ID stored in the OBJID
		// attribute
		// in the root METS element.
		// altRecordID: Alternative Record ID.
		// contained within | <metsHdr>
		/****** Further Elements	
		 *
		 * We do not use alternative IDs for the METS file
		 * 
		 * XMLObject ElementAltRecordID = new XMLObject("mets:altRecordID", ElementAltRecordIDValue);
		 * 
		 * ID: an optional XML ID value
		 * ID: xsd:ID optional
		 * ElementAltRecordID.addAttribute("ID", AltRecordIDAttributeIDValue);

		 * TYPE: an optional string describing the type of identifier (e.g.,
		 * OCLC #,
		 * LCCN, etc.).
		 * TYPE: xsd:string optional
		 * ElementAltRecordID.addAttribute("TYPE", AltRecordIDAttributeIDTypeValue);

		 * this.addDescendant(ElementAltRecordID);
		 */ 

	}
	
	//Attributes for MetsHdr Element
	private String MainAttributeIDValue = null;
	private String MainAttributeAdmIDValue = null;
	private String MainAttributeCreateDateValue = null;
	private String MainAttributeLastModDateValue = null; 
	private String MainAttributeRecordStatusValue = null;

	// Agent with ROLE: | CREATOR | EDITOR | ARCHIVIST | PRESERVATION |
	// DISSEMINATOR | CUSTODIAN | IPOWNER | OTHER

	// Creator -> See getActors method

	// Editor
	private String AgentAttributeRoleEditorValue = null; 
	private String AgentAttributeTypeEditorValue = null; 
	private String AgentAttributeOtherTypeEditorValue = null; 
	private String ElementAgentNameEditorValue = null;

	// Archivist -> See getActors method

	// Preservation
	private String AgentAttributeRolePreservationValue = null; 
	private String AgentAttributeTypePreservationValue = null; 
	private String AgentAttributeOtherTypePreservationValue = null; 
	private String ElementAgentNamePreservationValue = null;

	// Disseminator
	private String AgentAttributeRoleDisseminatorValue = null; 
	private String AgentAttributeTypeDisseminatorValue = null; 
	private String AgentAttributeOtherTypeDisseminatorValue = null; 
	private String ElementAgentNameDisseminatorValue = null;

	// Custodian
	private String AgentAttributeRoleCustodianValue = null; 
	private String AgentAttributeTypeCustodianValue = null; 
	private String AgentAttributeOtherTypeCustodianValue = null; 
	private String ElementAgentNameCustodianValue = null;

	// IPOwner -> See getIPOwner method

	
	/****** Further Elements	
	 *
	 * We do not use alternative IDs for the METS file
	 * 
	 * 	private String ElementAltRecordIDValue = null;
	 * 	private String AltRecordIDAttributeIDValue = null;
	 * 	private String AltRecordIDAttributeIDTypeValue = null;
	 * 
	 */ 
	
	//***************** SET VALUES **************************************
	
	private void getValues(DataCollection dc) {
		this.MainAttributeIDValue = getCollectionNumber(dc);
		this.MainAttributeAdmIDValue = ""; // Do we want to use it?
		this.MainAttributeCreateDateValue = actualTime();
		this.MainAttributeLastModDateValue = actualTime();

		//TODO
		this.MainAttributeRecordStatusValue = "";// here we need the information of our ip is it a: "final SIP" | "final AIP | "final DIP" ?
		
		// CREATOR: "dcms_DataCurator" -> See getActors method
		
		// EDITOR: IANUS
		this.AgentAttributeRoleEditorValue = "EDITOR";
		this.AgentAttributeTypeEditorValue = "OTHER";
		this.AgentAttributeOtherTypeEditorValue = "SOFTWARE AGENT";
		this.ElementAgentNameEditorValue = "IANUS ARCHIVAL SYSTEM";
		
		// ARCHIVIST: "dcms_DataCurator" -> See getActors method
		
		// PRESERVATION: IANUS
		this.AgentAttributeRolePreservationValue = "PRESERVATION";
		this.AgentAttributeTypePreservationValue = "OTHER"; 
		this.AgentAttributeOtherTypePreservationValue = "SOFTWARE AGENT";
		this.ElementAgentNamePreservationValue = "IANUS ARCHIVAL SYSTEM";
		
		
		// DISSEMINATOR: IANUS
		this.AgentAttributeRoleDisseminatorValue = "DISSEMINATOR";
		this.AgentAttributeTypeDisseminatorValue = "OTHER"; 
		this.AgentAttributeOtherTypeDisseminatorValue = "SOFTWARE AGENT";
		this.ElementAgentNameDisseminatorValue = "IANUS ARCHIVAL SYSTEM";
		
		// CUSTODIAN: IANUS
		this.AgentAttributeRoleCustodianValue = "CUSTODIAN";
		this.AgentAttributeTypeCustodianValue = "OTHER"; 
		this.AgentAttributeOtherTypeCustodianValue = "SOFTWARE AGENT";
		this.ElementAgentNameCustodianValue = "IANUS ARCHIVAL SYSTEM";
		
		// IPOWNER: "dcms_RightsHolder" -> See getIPOwner method
		

	}

	// Method to get the "ianus_collno" as Main Attribute ID Value
	private String getCollectionNumber(DataCollection dc) {
		Set<Identifier> idList = dc.getInternalIdList();
		for (Identifier id : idList) {
			if (id.getType().toLowerCase().equals("ianus_collno"))
				return id.getValue();
		}
		return null;
	}

	// Get the actual point in time for Main Attribute CreateDate Value and Main Attribute LastModDate Value
	private String actualTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date.getTime());
		return time;
	}

	
	// Get all actors with the role DataCurator
	private ArrayList<XMLObject> getActors(DataCollection dc, String role) {
		// Array List to save the results
		ArrayList<XMLObject> actors = new ArrayList<XMLObject>();
		//Iterate through list of DataCurators
		for(Person cp : dc.getDataCuratorList()){
			// Create XMLObject representing a data curator
			XMLObject ElementCreatorAgent = new XMLObject("mets:agent");
			
			// ID: an optional XML ID
			// ID: xsd:ID optional
			ElementCreatorAgent.addAttribute("ID", String.valueOf(cp.getId())); // ID aus Datenbank (Systemgenerierte ID));

			ElementCreatorAgent.addAttribute("ROLE", role);

			// NOT USED here
			// OTHERROLE: an optional string attribute to specify the particular
			// role an
			// agent plays with respect to a METS object or its sources,
			// to be used if the ROLE attribute has a value of OTHER.
			// OTHERROLE: xsd:string optional
			
			/*
			 * TYPE: optional | INDIVIDUAL | ORGANIZATION | OTHER TYPE: an optional
			 * attribute to specify the nature of the agent acting with respect to
			 * the METS object. It can take one of three values: INDIVIDUAL: a
			 * single, human agent ORGANIZATION: a collective entity OTHER: some
			 * other form of agent (e.g., a software agent)
			 */
			ElementCreatorAgent.addAttribute("TYPE", "INDIVIDUAL");
			
			// NOT USED here -> only for IANUS SYSTEM as actor
			// OTHERTYPE: an optional string to indicate the particular type of
			// agent,
			// to be used if a value of OTHER is indicated in the agent's TYPE
			// attribute.
			// OTHERTYPE: xsd:string optional
			

			// ______Elements and Attributes within <agent>__________

			// name:The full name of the METS document agent.
			// type="xsd:string"
			// may contain
			// contained within | <agent>
			XMLObject ElementAgentCreatorName = new XMLObject("mets:name", cp.getFirstName() + " " + cp.getLastName());
			ElementCreatorAgent.addDescendant(ElementAgentCreatorName);
		
			// NOT USED
			// note: Any additional information regarding the agent's activities
			// with
			// respect to the METS document.
			// type="xsd:string" minOccurs="0" maxOccurs="unbounded"
			// may contain
			// contained within | <agent>

			// add agent
			actors.add(ElementCreatorAgent);
		}
		return actors;
	}
		
	// Get all RightsHolder
	private ArrayList<XMLObject> getIPOwner(DataCollection dc) {
		// Save the results in an array list
		ArrayList<XMLObject> IPOwners = new ArrayList<XMLObject>();
		// Iterate through the rights holder list
		for(Actor a : dc.getRightsHolderList()){
			// Create XMLObject representing a rights holder
			XMLObject IPOwner = new XMLObject("mets:agent");
			// Add attributes
			IPOwner.addAttribute("ID", String.valueOf(a.getId())); // ID aus Datenbank (Systemgenerierte ID));
			IPOwner.addAttribute("ROLE", "IPOWNER");

			// Rights holder is a person
			if(a instanceof Person)
			{
				// Add Attribute
				IPOwner.addAttribute("TYPE", "INDIVIDUAL");
				// Add XMLObject for name
				XMLObject ElementAgentCreatorName = new XMLObject("mets:name", ((Person) a).getFirstName() + " " + ((Person) a).getLastName());
				// Add XMLObject name to the agent object
				IPOwner.addDescendant(ElementAgentCreatorName);
			}
			// Rights holder is an institution
			if(a instanceof Institution){
				// Add Attribute
				IPOwner.addAttribute("TYPE", "ORGANIZATION");
				// Add XMLObject for name
				XMLObject ElementName = new XMLObject("mets:name", ((Institution) a).getName());
				// Add XMLObject name to the agent object
				IPOwner.addDescendant(ElementName);
			}
			// Add Agent to resulting list	
			IPOwners.add(IPOwner);
		}
		return IPOwners;
	}	

}