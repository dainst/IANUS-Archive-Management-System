package de.ianus.metadata.utils;

import java.util.ArrayList;
import java.util.List;

public class ValueListUtils {

	public static List<Names> subMethodNameList = new ArrayList<ValueListUtils.Names>();
	public static List<Names> mainContentListName = new ArrayList<ValueListUtils.Names>();
	public static List<Names> subContentListName = new ArrayList<ValueListUtils.Names>();
	
	
	public static List<Names> gazetteerListName = new ArrayList<ValueListUtils.Names>();//OK
	//this list contains information for TextAttributes. This is different
	//public static List<Names> relatedResourceListName = new ArrayList<ValueListUtils.Names>();
	//TODO
	public static List<Names> mainPeriodListName = new ArrayList<ValueListUtils.Names>();
    public static List<Names> subPeriodListName = new ArrayList<ValueListUtils.Names>();
    public static List<Names> actorIDListName = new ArrayList<ValueListUtils.Names>();
	
	static {
		
		mainPeriodListName.add(Names.vla_timeline_2);
	    mainPeriodListName.add(Names.idai_chronontology);

	    subPeriodListName.add(Names.free_text);
	    subPeriodListName.add(Names.vla_timeline_3);
	    subPeriodListName.add(Names.idai_chronontology);
		
		gazetteerListName.add(Names.idai_gazetteer);
		gazetteerListName.add(Names.geonames);
		gazetteerListName.add(Names.pleiades);
		gazetteerListName.add(Names.gtgn);
		gazetteerListName.add(Names.rgzm_GeoNameService);
		gazetteerListName.add(Names.ags);
		
		subMethodNameList.add(Names.free_text);
		subMethodNameList.add(Names.idai_thesaurus_SubMethod);
		subMethodNameList.add(Names.fish_event_types);
		
		mainContentListName.add(Names.free_text);
        mainContentListName.add(Names.gnd);
        mainContentListName.add(Names.adex_type_general);
        mainContentListName.add(Names.idai_thesaurus_all);
        mainContentListName.add(Names.getty_aat);
        mainContentListName.add(Names.wortnetz_kultur);
        mainContentListName.add(Names.heritage_data);
		
		subContentListName.add(Names.free_text);
		subContentListName.add(Names.idai_thesaurus_all);
		subContentListName.add(Names.idai_vocab);
		subContentListName.add(Names.getty_aat);
		subContentListName.add(Names.wortnetz_kultur);
		subContentListName.add(Names.heritage_data);
	}
	
	public enum Names{
		
		//Period General
		vla_timeline_2("vla_timeline_2", "VLA Timeline 07/2014-2","VLA Zeitstrahl 07/2014-2", "", ""), //has valuelist
		idai_chronontology("idai_chronontology", "iDAI.chronontology", "iDAI.chronontology", "http://chronontology.dainst.org/period/", ""),

		//Period Detail
		free_text("free_text", "Free Text", "Freitext", "", ""),
		vla_timeline_3("vla_timeline_3", "VLA Timeline 07/2014-3", "VLA Zeitstrahl 07/2014-3", "", ""), //has valuelist
		//idai_chronontology("idai_chronontology","iDAI.chronontology"),
		
		//Gazetteer		
		idai_gazetteer("idai_gazetteer", "iDAI.gazetteer", "iDAI.gazetteer", "https://gazetteer.dainst.org/place/", ""),
		geonames("geonames", "Geonames", "Geonames", "http://www.geonames.org/", ""),
		pleiades("pleiades", "Pleiades", "Pleiades", "", ""),
		gtgn("gtgn", "Getty Thesaurus of Geographical Names", "Getty Thesaurus Geographischer Namen", "", ""),
		rgzm_GeoNameService("rgzm_GeoNameService", "RGZM GeoNameService", "RGZM GeoNameService", "", ""),
		ags("ags", "", "Amtliche Gemeindeschlüssel (AGS)", "", ""),
		
		resource_type ("ianus-list_datacollectiontype", "Resource type list", "Ressourcetypenliste", "", ""),
		
		//Main Content
        gnd ("GND", "", "Gemeinsame Normdatei (GND)", "", ""),
		adex_type_general ("adex_type_general", "ADeX-Type", "ADeX-Typ", "", ""), //has valuelist
		idai_thesaurus_all ("idai_thesaurus_all", "iDAI.thesaurus", "iDAI.thesaurus", "http://thesauri.dainst.org/de/concepts/", ""),
        getty_aat ("getty_aat", "Getty AAT", "", "", ""),
        wortnetz_kultur ("wortnetz_kultur", "", "Wortnetz Kultur", "", ""),
        heritage_data ("heritage_data", "English Heritage Data", "", "", ""),

		
		/*
		 * 	    //Content General
        gnd ("GND", "Gemeinsame Normdatei (GND)"),
		adex_type_general ("adex_type_general", "ADeX-Liste (Typ grob)"), //has valuelist
		idai_thesaurus_all ("idai_thesaurus_all", "iDAI.thesaurus.weltweit"),
        getty_aat ("getty_aat", "Getty AAT"),
        wortnetz_kultur ("wortnetz_kultur", "Wortnetz Kultur"),
        heritage_data ("heritage_data", "English Heritage Data"),
		 */
		
		//Sub Content
		//idai_thesaurus_weltweit ("iDAI.thesaurus.weltweit", "iDAI.thesaurus.weltweit"),
		idai_vocab ("iDAI.vocab", "iDAI.vocab", "iDAI.vocab", "", ""),
		//getty_aat ("Getty AAT", "Getty AAT"),
		//wortnetz_kultur ("Wortnetz Kultur", "Wortnetz Kultur"),
		//heritage_data ("Heritage Data", "Heritage Data (eng)"),
		
		
		//Main Method
		idai_thesaurus_mainMethod ("idai_thesaurus_mainMethod", "iDAI.thesaurus", "iDAI.thesaurus", "http://thesauri.dainst.org/de/concepts/", ""),
		
		
		//Sub Method
		//free_text ("free_text", "Free text"),
        idai_thesaurus_SubMethod ("idai_thesaurus_subMethod", "iDAI.thesaurus", "iDAI.thesaurus", "http://thesauri.dainst.org/de/concepts/", ""),
        //idai_thesaurus_all ("iDAI.thesaurus", "iDAI.thesaurus"),
		fish_event_types ("fish_event_types", "FISH-Event-Types (eng)", "", "", ""),
		
		
		// Disciplines
		idai_thesaurus_subDiscipline ("idai_thesaurus_subDiscipline", "iDAI.thesaurus", "iDAI.thesaurus", "http://thesauri.dainst.org/de/concepts/", ""),
		idai_thesaurus_mainDiscipline ("idai_thesaurus_mainDiscipline", "iDAI.thesaurus", "iDAI.thesaurus", "http://thesauri.dainst.org/de/concepts/", ""),
        
		
		
		//ianus_content ("ianus_content", "ianus_content"),
		
		ianus_identifier_types ("ianus-list_identifiertypes", "IANUS identifier types", "", "", ""),
		ianus_data_category ("ianus-list_dataCategory", "IANUS data category", "", "", ""),
		ianus_classification ("ianus-list_classification", "IANUS classification", "", "", "")
		
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;

		private Names(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static Names getById(String id) {
			if(id == null || id.equals("")) return null;
			for(Names item : Names.values()) {
				if(item.id.equals(id)) return item;
			}
			return null;
		}
		
		public String getField(String fieldname) {
			switch(fieldname) {
			case "id": return this.id;
			case "label":
				if(!this.labelGer.equals("")) return this.labelGer;
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "labelEng":
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "labelGer":
				if(!this.labelGer.equals("")) return this.labelGer;
				if(!this.labelEng.equals("")) return this.labelEng;
				else return this.id;
			case "url": return this.url;
			case "description": return this.description;
			}
			return null;
		}
		
		public static String getFieldById(String id, String fieldname) {
			if(getById(id) == null) return null;
			return getById(id).getField(fieldname);
		}
	}
	
}
