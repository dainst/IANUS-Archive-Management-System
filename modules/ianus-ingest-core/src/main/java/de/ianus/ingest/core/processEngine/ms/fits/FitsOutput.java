/**
 * 
 */
package de.ianus.ingest.core.processEngine.ms.fits;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;



/**
 * @author hendrik
 * 
 * Reimplementing the FITS API for better project integration: 
 * this class resembles the FitsOuptput API: https://github.com/harvard-lts/fits/blob/86b92898cb7d0bf8392f052b57790280863531be/src/edu/harvard/hul/ois/fits/FitsOutput.java
 * We are not using the original one, because the genuine FITS project is not a Maven project 
 * and doesn't integrate well with our software. 
 *
 */
public class FitsOutput {
	
	
	public Document fitsXml = null;
	
	private Process process = null;
	
	private Namespace ns = Namespace.getNamespace(Fits.XML_NAMESPACE);
	
	
	private static Logger logger = Logger.getLogger(FitsOutput.class);
	
	
	
	public FitsOutput(Process proc) throws Exception {
		this.process = proc;
		this.writeOutStream();
	}
	
	
	private void writeOutStream() throws Exception {
		String xmlString = "";
		
		BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(this.process.getInputStream()));
		String s = null;
		while ((s = stdInput.readLine()) != null) {
		    xmlString += s;
		}
		
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(xmlString);
		this.fitsXml = builder.build(in);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List buildMetadataList(Element parent) {
		List<FitsMetadataElement> data = new ArrayList<FitsMetadataElement>();
		if(parent == null) {
			return null;
		}
		for(Element child : (List<Element>)parent.getChildren()) {
			data.add(buildMetdataIElements(child));
		}
		return data;
	}
	
	
	private FitsMetadataElement buildMetdataIElements(Element node) {
		FitsMetadataElement element = new FitsMetadataElement();
		element.setName(node.getName());
		element.setValue(node.getValue());
		Attribute toolName = node.getAttribute("toolname");
		if(toolName != null) {
			element.setReportingToolName(toolName.getValue());
		}
		Attribute toolVersion = node.getAttribute("toolversion");
		if(toolVersion != null) {
			element.setReportingToolVersion(toolVersion.getValue());
		}
		Attribute status = node.getAttribute("status");
		if(status != null) {
			element.setStatus(status.getValue());
		}
		return element;
}
	
	
	@SuppressWarnings("unchecked")
	public List<FitsIdentity> getIdentities() {
		List<FitsIdentity> identities = new ArrayList<FitsIdentity>();
		try {
			XPath xpath = XPath.newInstance("//fits:identity");
			Namespace ns = Namespace.getNamespace("fits", Fits.XML_NAMESPACE);
			xpath.addNamespace(ns);
			List<Element> identElements = xpath.selectNodes(this.fitsXml);
			for(Element element : identElements) {
				FitsIdentity fileIdentSect = new FitsIdentity();

				//get the identity attributes
				Attribute formatAttr = element.getAttribute("format");
				Attribute mimetypeAttr = element.getAttribute("mimetype");
				if(formatAttr != null) {
					fileIdentSect.setFormat(formatAttr.getValue());
				}
				if(mimetypeAttr != null) {
					fileIdentSect.setMimetype(mimetypeAttr.getValue());
				}

				//get the tool elements
				List<Element> toolElements = element.getChildren("tool",ns);
				for(Element toolElement : toolElements) {
					ToolInfo toolInfo = new ToolInfo();
					Attribute toolNameAttr = toolElement.getAttribute("toolname");
					Attribute toolVersionAttr = toolElement.getAttribute("toolversion");
					if(toolNameAttr != null) {
						toolInfo.setName(toolNameAttr.getValue());
					}
					if(toolVersionAttr != null) {
						toolInfo.setVersion(toolVersionAttr.getValue());
					}
					fileIdentSect.addReportingTool(toolInfo);
				}

				//get the version elements
				List<Element> versionElements = element.getChildren("version",ns);
				for(Element versionElement : versionElements) {
					ToolInfo toolInfo = new ToolInfo();
					Attribute toolNameAttr = versionElement.getAttribute("toolname");
					Attribute toolVersionAttr = versionElement.getAttribute("toolversion");
					if(toolNameAttr != null) {
						toolInfo.setName(toolNameAttr.getValue());
					}
					if(toolVersionAttr != null) {
						toolInfo.setVersion(toolVersionAttr.getValue());
					}
					String value = versionElement.getText();
					FormatVersion formatVersion = new FormatVersion(value,toolInfo);
					fileIdentSect.addFormatVersion(formatVersion);
				}

				//get the externalIdentifier elements
				List<Element> xIDElements = element.getChildren("externalIdentifier",ns);
				for(Element xIDElement : xIDElements) {
					String type = xIDElement.getAttributeValue("type");
					String value = xIDElement.getText();
					ToolInfo toolInfo = new ToolInfo();
					Attribute toolNameAttr = xIDElement.getAttribute("toolname");
					Attribute toolVersionAttr = xIDElement.getAttribute("toolversion");
					if(toolNameAttr != null) {
						toolInfo.setName(toolNameAttr.getValue());
					}
					if(toolVersionAttr != null) {
						toolInfo.setVersion(toolVersionAttr.getValue());
					}
					ExternalIdentifier xid = new ExternalIdentifier(type,value,toolInfo);
					fileIdentSect.addExternalID(xid);
				}
				identities.add(fileIdentSect);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return identities;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FitsMetadataElement> getFileInfoElements() {
		Element root = fitsXml.getRootElement();
		Element fileInfo = root.getChild("fileinfo", this.ns);
		return buildMetadataList(fileInfo);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FitsMetadataElement> getFileStatusElements() {
		Element root = fitsXml.getRootElement();
		Element fileStatus = root.getChild("filestatus",ns);
		return buildMetadataList(fileStatus);
	}
	
	
	public String getTechMetadataType() {
		Element root = fitsXml.getRootElement();
		Element metadata = (Element)root.getChild("metadata",ns);
		if(metadata.getChildren().size() > 0) {
			Element techMetadata = (Element)root.getChild("metadata",ns).getChildren().get(0);
			return techMetadata.getName();
		}
		else {
			return null;
		}
	}
	
	
	public String getFitsVersion() {
		String fitsVersion = null;
		Element root = fitsXml.getRootElement();
		Attribute fitsVersionAttr = root.getAttribute("version");
		if (fitsVersionAttr != null) {
			fitsVersion = fitsVersionAttr.getValue();
		}
		return fitsVersion;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FitsMetadataElement> getTechMetadataElements() {
		Element root = fitsXml.getRootElement();
		Element metadata = (Element)root.getChild("metadata",ns);
		if(metadata.getChildren().size() > 0) {
			Element techMetadata = (Element)root.getChild("metadata",ns).getChildren().get(0);
			return buildMetadataList(techMetadata);
		}
		else {
			return null;
		}
	}
	
}
