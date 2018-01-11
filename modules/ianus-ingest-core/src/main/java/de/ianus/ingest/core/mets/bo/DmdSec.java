package de.ianus.ingest.core.mets.bo;

import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.xml.XMLObject;

/*
 * The descriptive metadata section of a METS document consists of one or more <dmdSec> (Descriptive Metadata Section) elements. Each <dmdSec> element may contain a pointer to external metadata (an <mdRef> element), internally embedded metadata (within an <mdWrap> element), or both.
 * 
 * External Descriptive Metadata (mdRef): an mdRef element provides a URI which may be used in retrieving the external metadata. For example, the following metadata reference points to the finding aid for a particular digital library object:
 * 
 *       <dmdSec ID="dmd001">
 *           <mdRef LOCTYPE="URN" MIMETYPE="application/xml" MDTYPE="EAD" 
 *           LABEL="Berol Collection Finding Aid">urn:x-nyu:fales1735</mdRef>
 *       </dmdSec>
 *             
 * The <mdRef> element of this <dmdSec> contains four attributes. 
 * The LOCTYPE attribute specifies the type of locator contained in body of the element; 
 * valid values for LOCTYPE include 'URN,' 'URL,' 'PURL,' 'HANDLE,' 'DOI,' and 'OTHER.' 
 * The MIMETYPE attribute allows you to specify the MIME type for the external descriptive metadata, 
 * and the MDTYPE allows you to indicate what form of metadata is being referenced. 
 * Valid values for the MDTYPE element include MARC, MODS, EAD, VRA (VRA Core), 
 * DC (Dublin Core), NISOIMG (NISO Technical Metadata for Digital Still Images), 
 * LC-AV (Library of Congress Audiovisual Metadata) , TEIHDR (TEI Header), DDI (Data Documentation Initiative), 
 * FGDC (Federal Geographic Data Committee Metadata Standard [FGDC-STD-001-1998] ), and OTHER. 
 * LABEL provides a mechanism for describing this metadata to those viewing a METS document, in a 'Table of Contents' display of the METS document, for example.
 * 
 * Internal Descriptive Metadata (mdWrap): An mdWrap element provides a wrapper around metadata embedded within a METS document. 
 * Such metadata can be in one of two forms: 1. XML-encoded metadata, 
 * with the XML-encoding identifying itself as belonging to a namespace other than the METS document namespace, 
 * or 2. any arbitrary binary or textual form, PROVIDED that the metadata is Base64 encoded and wrapped in a <binData> element within the mdWrap element. 
 * The following examples demonstrate the use of the mdWrap element:
 * 
 * <dmdSec ID="dmd002">
 * 	<mdWrap MIMETYPE="text/xml" MDTYPE="DC" LABEL="Dublin Core Metadata">
 * 	  <xmlData>
 * 	    <dc:title>Alice's Adventures in Wonderland</dc:title>
 * 	    <dc:creator>Lewis Carroll</dc:creator>
 * 	    <dc:date>between 1872 and 1890</dc:date>
 * 	    <dc:publisher>McCloughlin Brothers</dc:publisher>
 * 	    <dc:type>text</dc:type>
 * 	  </xmlData>
 * 	</mdWrap>
 * </dmdSec>
 * <dmdSec ID="dmd003">
 * 	<mdWrap MIMETYPE="application/marc" MDTYPE="MARC" LABEL="OPAC Record">
 *   <binData>MDI0ODdjam0gIDIyMDA1ODkgYSA0NU0wMDAxMDA...(etc.)
 *   </binData>
 * 	</mdWrap>
 * </dmdSec>
 *             
 * Note that all <dmdSec> elements must possess an ID attribute. This attribute provides a unique, internal name for each <dmdSec> element which can be used in the structural map to link a particular division of the document hierarchy to a particular <dmdSec> element. This allows specific sections of descriptive metadata to be linked to specific parts of the digital object.
 * 
 */

public class DmdSec extends XMLObject {
	
	
	/*
	 * Creates a <mets:dmdSec> element, that links to an external resource.
	 * 
	 * @param String href	the location of the linked descriptive metadata file
	 */
	public DmdSec(DataCollection dc) {
		
		/*
		 * type="mdSecType" minOccurs="0" maxOccurs="unbounded"
		 * dmdSec: Description Metadata Section.
		 * This section records all of the descriptive metadata for all items in the METS object 
		 * (including both structural map divs and descriptive metadata for data files). 
		 * Metadata can be either included in the METS hub document (mdWrap) or referenced via an identifier/locator (mdRef), 
		 * a la Warwick Framework. Multiple dmdSec elements are allowed so that descriptive metadata 
		 * can be recorded for each separate item within the METS object.
		 * 
		 * may contain |	<mdRef> |	<mdWrap>
		 * contained within |	<mets>
		 */
		
		super("mets:dmdSec");
		getValues(dc);
		
		
		// ______Attributes for <DmdSec> Element__________
		
		// ID: a required XML ID value
		// ID: xsd:ID   required  
		this.addAttribute("ID", MainAttributeIDValue);

		/*
		 *  GROUPID: an optional string attribute providing an identifier that may be used to indicate 
		 *  that different metadata sections may be considered as part of a single group. 
		 *  Two metadata sections with the same GROUPID value are to be considered part of the same group. 
		 *  This facility might be used, for example, to group changed versions of the same metadata if previous versions are maintained in a file for tracking purposes;
		 *  
		 *  GROUPID: xsd:string   optional  
		 */
		this.addAttribute("GROUPID", MainAttributeGroupIDValue);

		/*
		 *  ADMID: an optional attribute providing the XML ID attribute values for administrative metadata elements 
		 *  which apply to the current descriptive or administrative metadata. 
		 *  Typically used to reference preservation metadata which applies to the current metadata.
		 *  
		 *  ADMID: xsd:IDREFS   optional  
		 */
		this.addAttribute("ADMID", MainAttributeADMIDValue);

		// CREATED: an optional dateTime attribute specifying the date and time of creation for the metadata.
		// CREATED: xsd:dateTime   optional    
		this.addAttribute("CREATED", MainAttributeCreatedValue);

		//STATUS: xsd:string   optional  
		//STATUS: an optional string attribute indicating the status of this metadata (e.g., superceded, current, etc.).
		this.addAttribute("STATUS", MainAttributeStatusValue);

		
		/* ***********					 ***********
		// *______Element within <DmdSec>__________*
		// *									   *
		// **********					 ***********
		
		
		 * mdRef: metadata reference. 
		 * The mdRef element is a generic element used throughout the METS schema 
		 * to provide a pointer to metadata which resides outside the METS document. 
		 * NB: mdRef is an empty element. The location of the metadata must be recorded in the xlink:href attribute, 
		 * supplemented by the XPTR attribute as needed.
		 */
		XMLObject mdRef = new XMLObject("mdRef");
		
		
		// ****______Attributes <mdRef>__________****
		
		// ID: xsd:ID   optional  
		// ID: an optional XML ID value
		mdRef.addAttribute("ID", ElementMdRefIDValue);

		
		// MIMETYPE: xsd:string   optional  
		// MIMETYPE: an optional string attribute providing the MIME type for the metadata being pointed at
		
		// LABEL: xsd:string   optional  
		// LABEL: an optional string attribute providing a label to display to the viewer of the METS document identifying the metadata 
		mdRef.addAttribute("LABEL", "TEI Header");

		// XPTR: xsd:string   optional  
		// XPTR: an optional string attribute for providing an xptr to a location within the file pointed to by the mdRef element, if applicable.

		
		// *************
		// attributeGroup ref:LOCATION
		//
		// LOCTYPE:   required   | ARK | URN | URL | PURL | HANDLE | DOI | OTHER
		// LOCTYPE: Location Type
		// the type of locator used to reference a file. It must have one of the following values:
		// URN: Uniform Resource Name
		// URL: Uniform Resource Locator
		// PURL: Persistent URL
		// HANDLE: a CNRI Handle
		// DOI: A Digital Object Identifier
		// OTHER: a form of locator not specified above.
		mdRef.addAttribute("LOCTYPE", "URL");

		//
		// OTHERLOCTYPE: xsd:string   optional  
		// OTHERLOCTYPE: an optional string attribute used to indicate an alternative LOCTYPE if the LOCTYPE attribute itself has a value of "OTHER";


		
		
		// *************
		// attributeGroup ref:xlink:simpleLink
		// attributeGroup ref:METADATA
		//
		// MDTYPE:   required   | MARC | MODS | EAD | DC | NISOIMG | LC-AV | VRA | TEIHDR | DDI | FGDC | LOM | PREMIS | OTHER
		// MDTYPE: a required attribute specifying the yype of metadata being pointed at (e.g., MARC, EAD, etc.). It must have one of the following values:
		// MARC: any form of MARC record
		// MODS: metadata in the Library of Congress MODS format
		//		EAD: Encoded Archival Description finding aid
		//		DC: Dublin Core
		//		NISOIMG: NISO Technical Metadata for Digital Still Images
		//		LC-AV: technical metadata specified in the Library of Congress A/V prototyping project
		//		VRA: Visual Resources Association Core
		//		TEIHDR: Text Encoding Initiative Header
		//		DDI: Data Documentation Initiative
		//		FGDC: Federal Geographic Data Committee metadata
		//		OTHER: metadata in a format not specified above.
		//		OTHERMDTYPE: xsd:string   optional  
		//		OTHERMDTYPE: Other Metadata Type
		//		an optional string which may be used to record the particular form of metadata referenced by this element when the MDTYPE attribute has a value of "OTHER".


		// *************
		// attributeGroup ref:LOCATION
		//
		//		LOCTYPE:   required   | ARK | URN | URL | PURL | HANDLE | DOI | OTHER
		//		LOCTYPE: Location Type
		//		the type of locator used to reference a file. It must have one of the following values:
		//		URN: Uniform Resource Name
		//		URL: Uniform Resource Locator
		//		PURL: Persistent URL
		//		HANDLE: a CNRI Handle
		//		DOI: A Digital Object Identifier
		//		OTHER: a form of locator not specified above.
		//		OTHERLOCTYPE: xsd:string   optional  
		//		OTHERLOCTYPE: an optional
		//		string attribute used to indicate an alternative LOCTYPE if the LOCTYPE attribute itself has a value of "OTHER";
		// *************
		// attributeGroup ref:xlink:simpleLink
		//
		// attributeGroup ref:METADATA
		//
		// MDTYPE:   required   | MARC | MODS | EAD | DC | NISOIMG | LC-AV | VRA | TEIHDR | DDI | FGDC | LOM | PREMIS | OTHER
		// MDTYPE: a required attribute specifying the yype of metadata being pointed at (e.g., MARC, EAD, etc.). It must have one of the following values:
		// MARC: any form of MARC record
		// MODS: metadata in the Library of Congress MODS format
		// EAD: Encoded Archival Description finding aid
		// DC: Dublin Core
		// NISOIMG: NISO Technical Metadata for Digital Still Images
		// LC-AV: technical metadata specified in the Library of Congress A/V prototyping project
		// VRA: Visual Resources Association Core
		// TEIHDR: Text Encoding Initiative Header
		// DDI: Data Documentation Initiative
		// FGDC: Federal Geographic Data Committee metadata
		// OTHER: metadata in a format not specified above.
		// OTHERMDTYPE: xsd:string   optional  
		// OTHERMDTYPE: Other Metadata Type
		// an optional string which may be used to record the particular form of metadata referenced by this element when the MDTYPE attribute has a value of "OTHER".


		mdRef.addAttribute("xmlns:ns1", "http://www.w3.org/1999/xlink");
		mdRef.addAttribute("MDTYPE", "TEIHDR");
		mdRef.addAttribute("ns1:href", "ianus.xml");


			
		this.addDescendant(mdRef);

	}
	
	private String MainAttributeIDValue = null;
	private String MainAttributeGroupIDValue = null;
	private String MainAttributeADMIDValue = null;
	private String MainAttributeCreatedValue = null;
	private String MainAttributeStatusValue = null;
	private String ElementMdRefIDValue = null;
	
	private void getValues(DataCollection dc) {
		this.MainAttributeIDValue = "";

	}
}