package de.ianus.metadata.utils;

import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.ianus.metadata.bo.utils.ElementOfList;

/**
 * List Identifiers:
 * ZVL_Ebene_0
 * ZVL_Ebene_1
 * ZVL_Ebene_2
 * ZVL_Ebene_3
 * 
 * ZVL describes list comming from Zeitstrahl_Verband_der_Landesarchäologen.
 * 
 * @author jurzua
 *
 */
public class TimeVocabulary {
	
	private static String FILE_ZVL = "metadata/TimeList_Zeitstrahl_Verband_der_Landesarchäologen.json";
	
	public static String ZVL_Ebene_0 = "ZVL_Ebene_0";
	public static String ZVL_Ebene_1 = "ZVL_Ebene_1";
	public static String ZVL_Ebene_2 = "ZVL_Ebene_2";
	public static String ZVL_Ebene_3 = "ZVL_Ebene_3";

	private Map<String, Map<String, ElementOfList>> mapList = new HashMap<String, Map<String, ElementOfList>>();
	
	private static TimeVocabulary instance;
	
	public static TimeVocabulary getInstance(){
		if(instance == null){
			instance = new TimeVocabulary();
		}
		return instance;
	}
	
	public TimeVocabulary(){
		this.loadZVL();
	}
	
	/**
	 * This method loads the vocabulary of the Zeitstrahl_Verband_der_Landesarchäologen from a json file.
	 * This list considers for different level: Ebene 0, Ebene 1, Ebene 2 and Ebene 3.
	 * Each of this list will be treaded as a independent list. 
	 * 
	 */
	private void loadZVL(){
		JsonParser parser = new JsonParser();
		JsonObject json = (JsonObject) parser.parse(new InputStreamReader( this.getClass().getClassLoader().getResourceAsStream(FILE_ZVL)));
		this.mapList.put(ZVL_Ebene_0, loadListInZVL(json, ZVL_Ebene_0));
		this.mapList.put(ZVL_Ebene_1, loadListInZVL(json, ZVL_Ebene_1));
		this.mapList.put(ZVL_Ebene_2, loadListInZVL(json, ZVL_Ebene_2));
		this.mapList.put(ZVL_Ebene_3, loadListInZVL(json, ZVL_Ebene_3));
	} 
	
	private Map<String, ElementOfList> loadListInZVL(JsonObject json, String listName){
		Map<String, ElementOfList> rs = new HashMap<String, ElementOfList>();
		
		JsonArray list = json.getAsJsonArray(listName);
		Iterator<JsonElement> iterator = list.iterator();
		while (iterator.hasNext()) {
			JsonElement element = iterator.next();
			JsonObject object = element.getAsJsonObject();
			ElementOfList eol = new ElementOfList();
			eol.setValue(object.get("value").getAsString());
			eol.setValueId(object.get("id").getAsString());
			eol.setListId(listName);		
			rs.put(eol.getValueId(), eol);
		}
		return rs;
	}
	
	public Collection<ElementOfList> getListById(String listId){
		return (this.mapList.get(listId) != null) ? this.mapList.get(listId).values() : null;
	}
	
	public ElementOfList getEOL(String listId, String valueId){
		Map<String, ElementOfList> list = this.mapList.get(listId);
		if(list != null){
			return list.get(valueId);
		}
		return null;
	}
	
	
	public enum VlaTimeline2{
		
		vla_time_EG("vla_time_EG","","Erdgeschichte","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_PL("vla_time_PL","","Paläolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_ML("vla_time_ML","","Mesolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_NL("vla_time_NL","","Neolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_BZ("vla_time_BZ","","Bronzezeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_VEZ("vla_time_VEZ","","Vorrömische Eisenzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_RKZ("vla_time_RKZ","","Römische Kaiserzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_VWZ("vla_time_VWZ","","Völkerwanderungszeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_MA("vla_time_MA","","Mittelalter","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_NZ("vla_time_NZ","","Neuzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf","")
		;
	
		public final String id;
		public final String valueEng;
		public final String valueGer;
		public final String url;
		public final String description;
	
		private VlaTimeline2(String id, String valueEng, String valueGer, String url, String description){
			this.id = id;
			this.valueEng = valueEng;
			this.valueGer = valueGer;
			this.url = url;
			this.description = description;
		}
		
		public static String getValueGer(String id){
			for(VlaTimeline2 item : VlaTimeline2.values()){
				if(StringUtils.equals(item.id, id)){
					return item.valueGer;
				}
			}
			return null;
		}
	}
	
	public enum VlaTimeline3 {
		vla_time_APL("vla_time_APL","","Altpaläolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_MPL("vla_time_MPL","","Mittelpaläolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_JPL("vla_time_JPL","","Jungpaläolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SPL("vla_time_SPL","","Spät-/ Endpaläolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_FML("vla_time_FML","","Frühmesolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SML("vla_time_SML","","Spätmesolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_FNL("vla_time_FNL","","Frühneolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_MNL("vla_time_MNL","","Mittelneolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_JNL("vla_time_JNL","","Jungneolithikum ","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SNL("vla_time_SNL","","Spätneolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_ENL("vla_time_ENL","","Endneolithikum","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_äBZ("vla_time_äBZ","","Ältere Bronzezeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_mBZ("vla_time_mBZ","","Mittlere Bronzezeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SBZ("vla_time_SBZ","","Späte Bronzezeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SBU("vla_time_SBU","","Späte Bronzezeit, Urnenfelderzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_äEZ("vla_time_äEZ","","Ältere Eisenzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_äEH("vla_time_äEH","","Ältere Eisenzeit, Hallstattzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SEZ("vla_time_SEZ","","Späte Eisenzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_LT("vla_time_LT","","Späte Eisenzeit, Latènezeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_äRK("vla_time_äRK","","Ältere römische Kaiserzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_mRK("vla_time_mRK","","Mittlere römische Kaiserzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_sRK("vla_time_sRK","","Späte römische Kaiserzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_VWZ("vla_time_VWZ","","Völkerwanderungszeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_FMA("vla_time_FMA","","Frühmittelalter","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_HMA("vla_time_HMA","","Hochmittelalter","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_SMA("vla_time_SMA","","Spätmittelalter","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_fNZ("vla_time_fNZ","","Frühe Neuzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf",""),
		vla_time_sNZ("vla_time_sNZ","","Späte Neuzeit","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_Thesauri/Zeitstrahl_V01.pdf","")
		;

		public final String id;
		public final String valueEng;
		public final String valueGer;
		public final String URL;
		public final String description;
	
		private VlaTimeline3(String id, String valueEng, String valueGer, String URL, String description){
			this.id = id;
			this.valueEng = valueEng;
			this.valueGer = valueGer;
			this.URL = URL;
			this.description = description;
		}
		
		public static String getValueGer(String id){
			for(VlaTimeline3 item : VlaTimeline3.values()){
				if(StringUtils.equals(item.id, id)){
					return item.valueGer;
				}
			}
			return null;
		}
	}
}
