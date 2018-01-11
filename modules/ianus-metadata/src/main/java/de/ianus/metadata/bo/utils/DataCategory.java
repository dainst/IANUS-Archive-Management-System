package de.ianus.metadata.bo.utils;

import org.apache.commons.lang.StringUtils;

/**
 * This class contains only the label in English and German of the DataCategories. This element is represented as a ElementOfList
 * @author jurzua
 *
 */
public class DataCategory {
	
	public enum Type{
		
		ianus_pdf("ianus_pdf","pdf-document","PDF-Dokument","",""),
		ianus_docs("ianus_docs","text document","Textdokument","",""),
		ianus_raster("ianus_raster","raster image","Rastergrafik","",""),
		ianus_vector("ianus_vector","vector graphic","Vektorgrafik","",""),
		ianus_spsh("ianus_spsh","spreadsheet","Tabelle","",""),
		ianus_db("ianus_db","database","Datenbank","",""),
		ianus_text("ianus_text","plain text","Strukturierter Text","",""),
		ianus_geo("ianus_geo","geophysics","Geophsyik","",""),
		ianus_gis("ianus_gis","GIS","GIS","",""),
		ianus_vid("ianus_vid","video","Video","",""),
		ianus_aud("ianus_aud","audio","Audio","",""),
		ianus_vr("ianus_3D","3D / virtual reality","3D / Virtual Reality","",""),
		ianus_pres("ianus_pres","presentation","Präsentation","",""),
		ianus_sw("ianus_sw","software","Software","",""),
		ianus_other("ianus_other","other","Sonstiges","","")
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
		
		public static Type getById(String id) {
			for(Type type : Type.values())
				if(StringUtils.equals(id, type.id)) return type;
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
