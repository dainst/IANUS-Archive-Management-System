package de.ianus.metadata.utils;

import org.apache.commons.lang.StringUtils;

public class MainContentAdexTypeList {

	
	public enum Type{
		
		adex_find("adex_find","single find/find collection","Einzelfund/Fundstreuung","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_settl("adex_settl","settlement/fortification/military","Siedlung/Befestigung/Militär","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_grave("adex_grave","grave/funeral","Grab/Bestattung","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_depot("adex_depot","depot","Depot","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_econo("adex_econo","economy/infrastructure","Wirtschaft/Infrastruktur","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_cult("adex_cult","cult/law","Kult/Recht","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_paleogeo("adex_paleogeo","palaeontological/geological objects","Objekt der Paläontologie/Geologie","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_other("adex_other","other","Sonstiges","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/",""),
		adex_unknown("adex_unknown","unknown","Unbekannt","http://www.landesarchaeologen.de/verband/kommissionen/archaeologie-und-informationssysteme/projektearbeitsgruppen/thesaurusfragen/","")
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;
		
		private Type(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static MainContentAdexTypeList.Type getById(String id){
			for(Type type : Type.values()){
				if(StringUtils.equals(id, type.id)){
					return type;
				}
			}
			return null;
		}
		
		public static String getLabelEng(String valueId){
			for(Type type : Type.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(Type type : Type.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}
}
