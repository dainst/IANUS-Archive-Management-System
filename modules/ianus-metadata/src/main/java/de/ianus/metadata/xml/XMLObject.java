package de.ianus.metadata.xml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class XMLObject {
	
	
	private String name = null;
	private String value = null;
	private Type type = null;
	private ArrayList<XMLObject> descendants = null;
	
	public enum Type {
		ELEMENT, ATTRIBUTE;
	}
	
	
	
	public XMLObject(String name, String value, Type type) {
		this.name = name;
		this.value = value;
		this.type = type;
		this.descendants = new ArrayList<XMLObject>();
	}
	
	public XMLObject(String name, Type type) {
		this.name = name;
		this.value = null;
		this.type = type;
		this.descendants = new ArrayList<XMLObject>();
	}
	
	public XMLObject(String name, String value) {
		this.name = name;
		this.value = value;
		this.type = Type.ELEMENT;
		this.descendants = new ArrayList<XMLObject>();
	}
	
	public XMLObject(String name) {
		this.name = name;
		this.value = null;
		this.type = Type.ELEMENT;
		this.descendants = new ArrayList<XMLObject>();
	}
	
	
	
	public XMLObject addDescendants(ArrayList<XMLObject> descendants) {
		if(descendants != null) this.descendants.addAll(descendants);
		return this;
	}
	
	public XMLObject addDescendant(XMLObject descendant) {
		if(descendant != null) this.descendants.add(descendant);
		return this;
	}
	
	public XMLObject setDescendants(ArrayList<XMLObject> descendants) {
		this.descendants = new ArrayList<XMLObject>();
		if(descendants != null) this.descendants.addAll(descendants);
		return this;
	}
	
	public XMLObject setDescendant(XMLObject descendant) {
		this.descendants = new ArrayList<XMLObject>();
		this.addDescendant(descendant);
		return this;
	}
	
	public XMLObject addAttribute(String name, String value) {
		this.addDescendant(new XMLObject(name, value, Type.ATTRIBUTE));
		return this;
	}
	
	public XMLObject addAttribute(String name, Boolean value) {
		this.addAttribute(name, Boolean.toString(value));
		return this;
	}
	
	
	
	public String getName() {
		return this.name;
	}
	public String getValue() {
		return this.value;
	}
	public Type getType() {
		return this.type;
	}
	public ArrayList<XMLObject> getDescendants() {
		return this.descendants;
	}
	
	
	public Boolean hasDescendants() {
		if(this.descendants != null && this.descendants.size() > 0) 
			return true;
		return false;
	}
	
	
	
	public void getXML(XMLStreamWriter out) {
		try {
			if(this.type == Type.ELEMENT) {
				
				out.writeStartElement(this.name);
				// attributes
				if(this.descendants != null) {
					for(XMLObject descendant : this.descendants) {
						if(descendant.getType() == Type.ATTRIBUTE) {
							descendant.getXML(out);
						}
					}
				}
				
				// text node
				if(this.value != null && this.value.length() > 0)
					out.writeCharacters(this.value);
				
				// child elements
				if(this.descendants != null) {
					for(XMLObject descendant : this.descendants) {
						if(descendant.getType() == Type.ELEMENT) {
							descendant.getXML(out);
						}
					}
				}
				
				out.writeEndElement();
			}
			else if(this.type == Type.ATTRIBUTE) {
				String value = (this.value == null) ? "" : this.value; 
				out.writeAttribute(this.name, value);
			}
			
		}catch(XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public String toString() {
		StringWriter stringWriter = new StringWriter();
        XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = null;
        String out = null;
        
        try {
			xmlWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
			xmlWriter.writeStartDocument();
			
			this.getXML(xmlWriter);
			
			xmlWriter.writeEndDocument();
			xmlWriter.flush();
	        xmlWriter.close();
        }catch(XMLStreamException e) {
			e.printStackTrace();
		}
        
        out = stringWriter.getBuffer().toString();

        try {
			stringWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return out;
	}
	
	
	
	public String format() {
		return format(this.toString());
	}
	
	
	
	public static String format(String input) {
		try {
		    StringReader reader = new StringReader(input);
		    StringWriter writer = new StringWriter();
		    StreamResult xmlOutput = new StreamResult(writer);
		    
		    TransformerFactory tFactory = TransformerFactory.newInstance();
		    tFactory.setAttribute("indent-number", 4);
		    
		    Transformer transformer = tFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    // this did the trick! (otherwise, indentation will not occur)
		    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");

		    transformer.transform(
		            new javax.xml.transform.stream.StreamSource(reader), 
		            xmlOutput);

		    return xmlOutput.getWriter().toString();
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	}
	
	
	
	public void toFile(String filename) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
        XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = null;
        
        if(fileWriter != null) {
	        try {
				xmlWriter = xMLOutputFactory.createXMLStreamWriter(fileWriter);
				xmlWriter.writeStartDocument();
				
				// The xmlWriter will now write directly to the file, 
				// while the structure is recursively iterated.
				// Downside: the XML will be unformatted
				this.getXML(xmlWriter);
				
				xmlWriter.writeEndDocument();
				xmlWriter.flush();
		        xmlWriter.close();
	        }catch(XMLStreamException e) {
				e.printStackTrace();
			}
	        
	        try {
	        	fileWriter.flush();
	        	fileWriter.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	
}


