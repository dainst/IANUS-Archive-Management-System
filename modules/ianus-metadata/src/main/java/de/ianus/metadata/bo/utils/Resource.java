package de.ianus.metadata.bo.utils;

import org.apache.commons.lang.StringUtils;


public class Resource {
	
	public enum Type{
		
		ianus_rawd("ianus_rawd","raw data","Rohdaten","",""),
		ianus_procd("ianus_procd","processed data","verarbeitete Daten","",""),
		ianus_results("ianus_results","interpretations / results","Interpretationen / Ergebnisse","",""),
		ianus_suppl("ianus_suppl","supplementary data to a publication","Daten zu einer Publikation","",""),
		ianus_finrep("ianus_finrep","final reports","Abschlussberichte","",""),
		ianus_prelimrep("ianus_prelimrep","preliminary reports","Vor-/Zwischenberichte","",""),
		ianus_lect("ianus_lect","lectures / talks","Vorträge","",""),
		ianus_imagcoll("ianus_imagcoll","still image collection","Sammlung von Bildern","",""),
		ianus_doccoll("ianus_doccoll","document collection","Sammlung von Texten","",""),
		ianus_dscoll("ianus_dscoll","dataset collection","Sammlung von strukturierten Informationen","",""),
		ianus_drawcoll("ianus_drawcoll","drawing collection","Sammlung von Zeichnungen","",""),
		ianus_3dcoll("ianus_3dcoll","3d collection","Sammlung von 3D-Objekten","",""),
		ianus_audiocoll("ianus_audiocoll","audio collection","Sammlung von Audio-Inhalten","",""),
		ianus_movcoll("ianus_movcoll","moving image collection","Sammlung von Video-Inhalten","",""),
		ianus_intercoll("ianus_intercoll","interactive resource collection","Sammlung mit interaktiven Inhalten","",""),
		ianus_sw("ianus_sw","software","Software","","")
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String URL;
		public final String description;
		
		private Type(String id, String labelEng, String labelGer, String URL, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.URL = URL;
			this.description = description;
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
