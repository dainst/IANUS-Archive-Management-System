package de.ianus.metadata.utils;

import java.io.Serializable;

/**
 * 
 * @author jurzua
 *
 */
public class ISOLanguage implements Serializable{

	//Id	Part2B	Part2T	Part1	Scope	Language_Type	Ref_Name	Comment
	private static final long serialVersionUID = 1015808973741964981L;
	
	private String id;
	private String scope;
	private String type;
	private String nameEng;
	private String nameEngLowercase;
	private String nameGer;
	private String nameGerLowercase;
	
	@Deprecated
	public ISOLanguage(String id, String scope, String type, String nameEng){
		this.id = id;
		this.scope = scope;
		this.type = type;
		this.nameEng = nameEng;
		this.nameEngLowercase = nameEng.toLowerCase();
	}
	
	
	public ISOLanguage(String id, String nameEng, String nameGer){
		this.id = id;
		this.nameEng = nameEng;
		this.nameEngLowercase = nameEng.toLowerCase();
		this.nameGer = nameGer;
		this.nameGerLowercase = nameGer.toLowerCase();
	}
	
	public String getId() {
		return id;
	}

	public String getScope() {
		return scope;
	}

	public String getType() {
		return type;
	}
	
	public String getNameEng() {
		return nameEng;
	}


	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}


	public String getNameGer() {
		return nameGer;
	}


	public void setNameGer(String nameGer) {
		this.nameGer = nameGer;
	}	
	
	public String getNameEngLowercase() {
		return nameEngLowercase;
	}


	public void setNameEngLowercase(String nameEngLowercase) {
		this.nameEngLowercase = nameEngLowercase;
	}


	public String getNameGerLowercase() {
		return nameGerLowercase;
	}


	public void setNameGerLowercase(String nameGerLowercase) {
		this.nameGerLowercase = nameGerLowercase;
	}


	@Override
	public String toString(){
		return nameGer;
		//return "Lang [id="+id+", name="+this.name+"]";
	}
	
}
