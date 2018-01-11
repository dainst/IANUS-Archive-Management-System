package de.ianus.metadata.bo.utils;

import org.apache.commons.lang.StringUtils;

/**
 * This class contains only the label in English and German of the DataCategories. This element is represented as a ElementOfList
 * @author jurzua
 *
 */
public class Classification {

	public enum Type{
		
		ianus_ddc("ianus_ddc","digital data collection","Digitale Datensammlung","",""),
		ianus_aad("ianus_aad","analog archival resources","Analoges Archivmaterial","",""),
		ianus_gl("ianus_gl","grey literature","Graue Literatur","",""),
		ianus_publ("ianus_publ","publication","Publikation","",""),
		ianus_tm("ianus_tm","teaching materials","Lehrmaterialien","",""),
		ianus_rp("ianus_rp","(research) project","(Forschungs-)Projekt","",""),
		ianus_thesis("ianus_thesis","thesis","Qualifizierungsarbeit","",""),
		ianus_act("ianus_act","activity","Aktivität","",""),
		ianus_site("ianus_site","site","Fundstelle","",""),
		ianus_mon("ianus_mon","monument","Monument","","")
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
