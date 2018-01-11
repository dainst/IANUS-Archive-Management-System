package de.ianus.ingest.core.mets.bo;

import java.util.Set;

import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.utils.RetrievalUtils;
import de.ianus.metadata.xml.XMLObject;

/*
 * **************************************************************************************************************************************
 * METS: Metadata Encoding and Transmission Standard.																					*
 * METS is intended to provide a standardized XML format for transmission of complex digital library objects between systems. 			*
 * As such, it can be seen as filling a role similar to that defined for the Submission Information Package (SIP), 						*
 * Archival Information Package (AIP) and Dissemination Information Package (DIP) 														*
 * in the Reference Model for an Open Archival Information System.																		*
 * 																																		*
 * The fixed names of attributes and elements are not stored in variables but directly passed as strings in the functions creating them.* 
 * The input from our ips is stored in variables to structure the class and provide a better overview. 									*
 * 																																		*
 * **************************************************************************************************************************************
 */

public class MetsFrame extends XMLObject {

	public MetsFrame(DataCollection dc) {

		/*
		 * The main Element <mets> is the root element and is needed to create a
		 * mets document. It may contain <metsHdr> | <dmdSec> | <amdSec> |
		 * <fileSec> | <structMap> | <structLink> | <behaviorSec> At least
		 * (mandatory) <metsHdr>| <structMap> are needed to have a valid METS
		 * document
		 */

		// create the <mets:mets> element
		super("mets:mets");

		// xmlns
		this.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		this.addAttribute("xmlns:mets", "http://www.loc.gov/METS/");
		this.addAttribute("xmlns:xlink", "http://www.w3.org/TR/xlink");

		// xsi
		this.addAttribute("xsi:schemaLocation", "http://www.loc.gov/standards/METS/");

		// ______Specific Attributes for <mets> Element (from the standard)__________

		// ID: an optional XML ID value
		// ID: xsd:ID optional
		this.addAttribute("ID", getCollectionNumber(dc));

		// OBJID: an optional attribute recording a primary identifier assigned
		// to the METS document.
		// OBJID: xsd:string optional
		this.addAttribute("OBJID", getCollectionNumber(dc));

		// LABEL: an optional attribute providing a title/text string
		// identifying the document for users.
		// LABEL: xsd:string optional
		this.addAttribute("LABEL", RetrievalUtils.getTextAttribute(dc.getTitleList(), "eng").getValue());

		// TYPE: an optional string attribute describing the type of object,
		// e.g., book, journal, stereograph, etc.
		// TYPE: xsd:string optional
		this.addAttribute("TYPE", "data collection");

		// PROFILE: an optional attribute providing a URI or other identifier
		// for a METS profile to which this METS document conforms.
		// PROFILE: xsd:string optional
		//
		// NOT USED at the moment
		// this.addAttribute("AttributeProfile", AttributeProfileValue);

	}

	
	
	//***************** SET VALUES **************************************
	
	

		
	// Helper method to get the collection number
	public String getCollectionNumber(DataCollection dc) {
		Set<Identifier> idList = dc.getInternalIdList();
		for (Identifier id : idList) {
			if(Identifier.Internal.getById(id.getType()) == Identifier.Internal.ianus_collno)
				return id.getValue();
		}
		return null;
	}

}