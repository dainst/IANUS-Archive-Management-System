package de.ianus.metadata.bo.utils;

import org.apache.commons.lang.StringUtils;

public class GreatRegion {

	public enum Type{
		
		GR_2044223("2044223","Europe","Europa","https://gazetteer.dainst.org/place/2044223",""),
		GR_2042601("2042601","Africa","Afrika","https://gazetteer.dainst.org/place/2042601",""),
		GR_2042932("2042932","Asia","Asien","https://gazetteer.dainst.org/place/2042932",""),
		GR_2180506("2180506","Alps","Alpen","https://gazetteer.dainst.org/place/2180506",""),
		GR_2044231("2044231","Balkan Peninsula","Balkan","https://gazetteer.dainst.org/place/2044231",""),
		GR_2044232("2044232","Baltic States","Baltikum","https://gazetteer.dainst.org/place/2044232",""),
		GR_2043065("2043065","Near East","Vorderer Orient","https://gazetteer.dainst.org/place/2043065",""),
		GR_2042694("2042694","Maghreb","Maghreb","https://gazetteer.dainst.org/place/2042694",""),
		GR_2042602("2042602","North Africa","Nordafrika","https://gazetteer.dainst.org/place/2042602",""),
		GR_2044273("2044273","Middle Europe","Mitteleuropa","https://gazetteer.dainst.org/place/2044273",""),
		GR_2044261("2044261","Scandinavia","Skandinavien","https://gazetteer.dainst.org/place/2044261",""),
		GR_2181124("2181124","Mediterranean Region","Mittelmeerregion","https://gazetteer.dainst.org/place/2181124",""),
		GR_2181127("2181127","Black Sea Region","Schwarzmeerregion","https://gazetteer.dainst.org/place/2181127",""),
		GR_2411637("2411637","Magna Graecia","Magna Graecia","https://gazetteer.dainst.org/place/2411637",""),
		GR_2359913("2359913","Imperium Romanum","Imperium Romanum","https://gazetteer.dainst.org/place/2359913",""),
		GR_2277479("2277479","Levante","Levante","https://gazetteer.dainst.org/place/2277479",""),
		GR_2043218("2043218","Mesopotamia","Mesopotamien","https://gazetteer.dainst.org/place/2043218","")
		
		;

		public String id;
		public String labelEng;
		public String labelGer;
		public String url;
		public String description;
	
		private Type(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
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
