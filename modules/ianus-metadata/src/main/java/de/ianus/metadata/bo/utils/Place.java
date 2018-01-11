package de.ianus.metadata.bo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/*
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
*/
import de.ianus.metadata.bo.Element;
import de.ianus.metadata.bo.utils.Country.Type;
import de.ianus.metadata.utils.RetrievalUtils;
import de.ianus.metadata.xml.XMLObject;


/**
 * 
 * This class is described in
 * <a href="http://confluence:8090/display/IAN/Place">Documentation</a>.
 * 
 * @author jurzua
 *
 */
@Entity(name="Place")
public class Place extends Element{
	
	//multi-lingual: AccuracyDescription, GreaterRegion, Country, RegionProvince, City, FreeDescription, HistoricalName, 
	//TypeOfArea
	@Transient
	private Map<TextAttribute.ContentType, Set<TextAttribute>> attributeMap = new HashMap<TextAttribute.ContentType, Set<TextAttribute>>();

	@Column(name="coordinateSystem")
	private String coordinateSystem;
	
	@Column(name="accuracy")
	private String accuracy;

	//@Transient
	//private Geometry wkt;
	
	@Column(name="wktText", columnDefinition = "TEXT")
	private String wktText;
	
	//Kreis
	@Column(name="district")
	private String district;
	
	//PLZ
	@Column(name="postalCode0")
	private String postalCode;
	
	//Gemeindekennzahl
	//@Column(name="municipalityCode")
	//private Integer municipalityCode;
	
	//Stadtteil
	@Column(name="urbanDistrict")
	private String urbanDistrict;
	
	//Straße
	@Column(name="street")
	private String street;

	
	//Flur_Gemarkung
	@Column(name="parish")
	private String parish;
	
	@Column(name="geometricType")
	private String geometricType;
	
	
	//Gazetteer change type from gazetteer to identifier
	@Transient
	private Set<ElementOfList> identifierList  = new LinkedHashSet<ElementOfList>();
	
	
	private static String GERMAN_CODE = "deu";
	private static String ENGLISH_CODE = "eng";
	
	
	
	public static Place clone(Place other, Long sourceId){
		Place newItem = new Place();
		newItem = (Place)Element.clone(newItem, other, sourceId);
		newItem.coordinateSystem = other.coordinateSystem;
		newItem.accuracy = other.accuracy;
		newItem.wktText = other.wktText;
		newItem.district = other.district;
		newItem.postalCode = other.postalCode;
		newItem.urbanDistrict = other.urbanDistrict;
		newItem.street = other.street;
		newItem.parish = other.parish;
		return newItem;
	}
	
	
	
	/**
	 * Method does not use the generic language fallback handling, 
	 * instead gets *any* language, either GERMAN or ENGLISH or the first available 
	 * 
	 * @return String label
	 */
	// TODO: use generic fallback language handling
	public String getDynamicLabel(){
		String label = null;
		
		Set<String> lanList = getLanguageCodeList();
		if(lanList.contains(GERMAN_CODE)){
			label = getDynamicLabel(GERMAN_CODE);
		}else if(lanList.contains(ENGLISH_CODE)){
			label = getDynamicLabel(ENGLISH_CODE);
		}else if(!lanList.isEmpty()){
			label = getDynamicLabel(lanList.iterator().next());
		}
		return label;
	}
	
	
	
	/**
	 * Method does *not* use language fallback handling.
	 * 
	 * @param languageCode
	 * @return
	 */
	public String getDynamicLabel(String languageCode){
		TextAttribute greaterRegion = RetrievalUtils.getTextAttributeByLanguage(getGreaterRegionList(), languageCode);
		TextAttribute country = RetrievalUtils.getTextAttributeByLanguage(getCountryList(), languageCode);
		TextAttribute regionProvince = RetrievalUtils.getTextAttributeByLanguage(getRegionProvinceList(), languageCode);
		TextAttribute city = RetrievalUtils.getTextAttributeByLanguage(getCityList(), languageCode);
		
		String label = new String();
		
		if(greaterRegion != null && StringUtils.isNotEmpty(greaterRegion.getValue())){
			label = greaterRegion.getValue();
		}
		if(country != null && StringUtils.isNotEmpty(country.getValue())){
			label = (StringUtils.isEmpty(label)) ? country.getValue() : label + ", " + country.getValue(); 
		}
		if(regionProvince != null && StringUtils.isNotEmpty(regionProvince.getValue())){
			label = (StringUtils.isEmpty(label)) ? regionProvince.getValue() : label + ", " + regionProvince.getValue();
		}
		if(city != null && StringUtils.isNotEmpty(city.getValue())){
			label = (StringUtils.isEmpty(label)) ? city.getValue() : label + ", " + city.getValue();
		}
		if(StringUtils.isNotEmpty(getUrbanDistrict())){
			label = (StringUtils.isEmpty(label)) ? getUrbanDistrict() : label + ", " + getUrbanDistrict();
		}
		
		
		return label;
	}
	
	
	
	/**
	 * Method does *not* use language fallback handling.
	 *
	 * @param contentType
	 * @param languageCode
	 * @return
	 */
	public TextAttribute getTextAttribute(TextAttribute.ContentType contentType, String languageCode){
		if(TextAttribute.ContentType.accuracyDescription == contentType){
			return RetrievalUtils.getTextAttributeByLanguage(getAccuracyDescriptionList(), languageCode);
		}else if(TextAttribute.ContentType.greaterRegion == contentType){
			return RetrievalUtils.getTextAttributeByLanguage(getGreaterRegionList(), languageCode);
		}else if(TextAttribute.ContentType.country == contentType){
			return RetrievalUtils.getTextAttributeByLanguage(getCountryList(), languageCode);
		}else if(TextAttribute.ContentType.regionProvince == contentType){
			return RetrievalUtils.getTextAttributeByLanguage(getRegionProvinceList(), languageCode);
		}else if(TextAttribute.ContentType.city == contentType){
			return RetrievalUtils.getTextAttributeByLanguage(getCityList(), languageCode);
		}else if(TextAttribute.ContentType.freeDescription == contentType){
			return RetrievalUtils.getTextAttributeByLanguage(getFreeDescriptionList(), languageCode);
		}
		return null;
	}
	
	
	
	public Set<String> getLanguageCodeList(){
		Set<String> list = new LinkedHashSet<String>();
		for(TextAttribute att : this.getAccuracyDescriptionList()){
			if(StringUtils.isNotEmpty(att.getLanguageCode()))
				list.add(att.getLanguageCode());
		} 
		for(TextAttribute att : this.getGreaterRegionList()){
			if(StringUtils.isNotEmpty(att.getLanguageCode()))
				list.add(att.getLanguageCode());		
		}
		for(TextAttribute att : this.getCountryList()){
			if(StringUtils.isNotEmpty(att.getLanguageCode()))
				list.add(att.getLanguageCode());
		}
		for(TextAttribute att : this.getRegionProvinceList()){
			if(StringUtils.isNotEmpty(att.getLanguageCode()))
				list.add(att.getLanguageCode());
		}
		for(TextAttribute att : this.getCityList()){
			if(StringUtils.isNotEmpty(att.getLanguageCode()))
				list.add(att.getLanguageCode());
		}
		for(TextAttribute att : this.getFreeDescriptionList()){
			if(StringUtils.isNotEmpty(att.getLanguageCode()))
				list.add(att.getLanguageCode());
		}
		return list;
	}
	
	
	
	public Exception validateWkt(){
		/*
		if(StringUtils.isNotEmpty(this.wktText)){
			try {
				GeometryFactory geometryFactory = new GeometryFactory();
				WKTReader reader = new WKTReader( geometryFactory );
				reader.read(this.wktText);
			} catch (Exception e) {
				return e;
			}	
		}
		*/
		return null;
	}
	
	public boolean transformWkt(){
		/*
		try {
			GeometryFactory geometryFactory = new GeometryFactory();
			WKTReader reader = new WKTReader( geometryFactory );
			this.wkt = reader.read(this.wktText); 
		} catch (ParseException e) {
			return false;
		}
		*/
		return true;
	}
	
	
	
	
	
	
	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("wkt", this.wktText);
		json.addProperty("coordinateSystem", this.coordinateSystem);
		json.addProperty("accuracy", this.accuracy);
		json.addProperty("district", this.district);
		json.addProperty("postalCode", this.postalCode);
		//json.addProperty("municipalityCode", this.municipalityCode);
		json.addProperty("urbanDistrict", this.urbanDistrict);
		json.addProperty("street", this.street);
		json.addProperty("parish", this.parish);
		
		json.add("accuracyDescriptionList", TextAttribute.toJsonArray(this.getAccuracyDescriptionList()));
		json.add("greaterRegionList", TextAttribute.toJsonArray(this.getGreaterRegionList()));
		json.add("countryList", TextAttribute.toJsonArray(this.getCountryList()));
		json.add("regionProvinceList", TextAttribute.toJsonArray(this.getRegionProvinceList()));
		json.add("cityList", TextAttribute.toJsonArray(this.getCityList()));
		json.add("freeDescriptionList", TextAttribute.toJsonArray(this.getFreeDescriptionList()));
		json.add("typeOfAreaList", TextAttribute.toJsonArray(this.getTypeOfAreaList()));
		json.add("historicalNameList", TextAttribute.toJsonArray(this.getHistoricalNameList()));
		json.add("identifierList", ElementOfList.toJsonArray(this.getIdentifierList()));
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<Place> list){
		JsonArray array = new JsonArray();
		for(Place item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public XMLObject toXMLObject() {
		XMLObject xml = new XMLObject("PlaceDataType");
			if(!empty(this.wktText))			xml.addAttribute("WKT", this.wktText);
			if(!empty(this.coordinateSystem))	xml.addAttribute("CoordinateSystem", this.coordinateSystem);
			if(!empty(this.accuracy)) 			xml.addAttribute("Accuracy", this.accuracy);
			if(!empty(this.district)) 			xml.addAttribute("District", this.district);
			if(!empty(this.postalCode)) 		xml.addAttribute("PostalCode", this.postalCode);
			if(!empty(this.urbanDistrict))		xml.addAttribute("UrbanDistrict", this.urbanDistrict);
			if(!empty(this.street))				xml.addAttribute("Street", this.street);
			if(!empty(this.parish))				xml.addAttribute("Parish", this.parish);
			if(!empty(this.geometricType))		xml.addAttribute("GeometricType", this.geometricType);
			
			XMLObject accuracy = new XMLObject("AccurracyDescription");
			accuracy.addDescendants(TextAttribute.toXMLObjects(this.getAccuracyDescriptionList()));
			if(accuracy.hasDescendants()) xml.addDescendant(accuracy);
			
			XMLObject region = new XMLObject("GreaterRegion");
			region.addDescendants(TextAttribute.toXMLObjects(this.getGreaterRegionList()));
			if(region.hasDescendants()) xml.addDescendant(region);
			
			XMLObject country = new XMLObject("Country");
			country.addDescendants(TextAttribute.toXMLObjects(this.getCountryList()));
			if(country.hasDescendants()) xml.addDescendant(country);
			
			XMLObject province = new XMLObject("RegionProvince");
			province.addDescendants(TextAttribute.toXMLObjects(this.getRegionProvinceList()));
			if(province.hasDescendants()) xml.addDescendant(province);
			
			XMLObject city = new XMLObject("City");
			city.addDescendants(TextAttribute.toXMLObjects(this.getCityList()));
			if(city.hasDescendants()) xml.addDescendant(city);
			
			XMLObject name = new XMLObject("HistoricalName");
			name.addDescendants(TextAttribute.toXMLObjects(this.getHistoricalNameList()));
			if(name.hasDescendants()) xml.addDescendant(name);
			
			XMLObject type = new XMLObject("TypeOfArea");
			type.addDescendants(TextAttribute.toXMLObjects(this.getTypeOfAreaList()));
			if(type.hasDescendants()) xml.addDescendant(type);
			
			XMLObject description = new XMLObject("FreeDescription");
			description.addDescendants(TextAttribute.toXMLObjects(this.getFreeDescriptionList()));
			if(description.hasDescendants()) xml.addDescendant(description);
			
		return xml;
	}
	
	public static ArrayList<XMLObject> toXMLObjects(Set<Place> list) {
		ArrayList<XMLObject> out = new ArrayList<XMLObject>();
		if(list != null) {
			for(Place item : list) {
				out.add(item.toXMLObject());
			}
		}
		return out;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}	

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getUrbanDistrict() {
		return urbanDistrict;
	}

	public void setUrbanDistrict(String urbanDistrict) {
		this.urbanDistrict = urbanDistrict;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getParish() {
		return parish;
	}

	public void setParish(String parish) {
		this.parish = parish;
	}

	public String getCoordinateSystem() {
		return coordinateSystem;
	}

	public void setCoordinateSystem(String coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public Set<TextAttribute> getAccuracyDescriptionList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.accuracyDescription)){
			this.attributeMap.put(TextAttribute.ContentType.accuracyDescription, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.accuracyDescription);
	}

	public Set<TextAttribute> getGreaterRegionList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.greaterRegion)){
			this.attributeMap.put(TextAttribute.ContentType.greaterRegion, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.greaterRegion);
	}
	
	public Set<TextAttribute> getCountryList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.country)){
			this.attributeMap.put(TextAttribute.ContentType.country, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.country);
	}
	
	public Set<TextAttribute> getRegionProvinceList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.regionProvince)){
			this.attributeMap.put(TextAttribute.ContentType.regionProvince, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.regionProvince);
	}
	
	public Set<TextAttribute> getCityList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.city)){
			this.attributeMap.put(TextAttribute.ContentType.city, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.city);
	}
	
	public Set<TextAttribute> getFreeDescriptionList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.freeDescription)){
			this.attributeMap.put(TextAttribute.ContentType.freeDescription, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.freeDescription);
	}
	
	public Set<TextAttribute> getHistoricalNameList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.historicalName)){
			this.attributeMap.put(TextAttribute.ContentType.historicalName, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.historicalName);
	}
	
	public Set<TextAttribute> getTypeOfAreaList() {
		if(!this.attributeMap.containsKey(TextAttribute.ContentType.typeOfArea)){
			this.attributeMap.put(TextAttribute.ContentType.typeOfArea, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(TextAttribute.ContentType.typeOfArea);
	}
	
	
	/*
	public Geometry getWkt() {
		return wkt;
	}

	public void setWkt(Geometry wkt) {
		this.wkt = wkt;
	}	*/

	public Set<ElementOfList> getIdentifierList() {
		return identifierList;
	}

	public void setIdentifierList(Set<ElementOfList> identifierList) {
		this.identifierList = identifierList;
	}

	public String getWktText() {
		return wktText;
	}

	public void setWktText(String wktText) {
		this.wktText = wktText;
	}	
	
	public Set<TextAttribute> getAttributeList(TextAttribute.ContentType contentType){
		if(!this.attributeMap.containsKey(contentType)){
			this.attributeMap.put(contentType, new LinkedHashSet<TextAttribute>());
		}
		return this.attributeMap.get(contentType);
	}
	
	public Map<TextAttribute.ContentType, Set<TextAttribute>> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<TextAttribute.ContentType, Set<TextAttribute>> attributeMap) {
		this.attributeMap = attributeMap;
	}
	
	
	public enum GeometricType{
		ianus_point("ianus_point","point","Punkt","",""),
		ianus_line("ianus_line","line","Linie","",""),
		ianus_polygon("ianus_polygon","polygon","Polygon","",""),
		ianus_mpoint("ianus_mpoint","multipoints","Multipunkte","",""),
		ianus_mline("ianus_mline","multilinen","Multilinien","",""),
		ianus_mpolygon("ianus_mpolygon","multipolygon","Multipolygone","",""),
		ianus_geocoll("ianus_geocoll","geoemtry collection","Geometriesammlung","","")
		;
	
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String URL;
		public final String description;
	
		private GeometricType(String id, String labelEng, String labelGer, String URL, String description){
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
	/*
	public enum GeometricType{
		
		A1 ("", "", "Punkt", ""),
		A2 ("", "", "Linie", ""),
		A3 ("", "", "Polygon", ""),
		A4 ("", "", "Multipunkte", ""),
		A5 ("", "", "Multilinien", ""),
		A6 ("", "", "Multipolygone", ""),
		A7 ("", "", "Geometriesammlung", "")
		;
		
		public String id;
		public String labelEng;
		public String labelGer;
		public String description;
		
		private GeometricType(String id, String labelEng, String labelGer, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
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
	}*/
	
	
	public enum TypeOfArea {
		
		adex_archaeolArea("adex_archaeolArea","area of archaeological remains","Archäologiefläche","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_ADeX/ADeX_2-0_Doku.pdf","Eine  Archäologiefläche  ist  eine  Fläche  (s.o.),  an  der  mindestens  ein  archäologisch  qualifiziertes bzw. relevantes Ergebnis vorliegt/vorgelegen hat oder vermutet wird."), 
		adex_researchArea("adex_researchArea","area of research","Untersuchungsfläche","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_ADeX/ADeX_2-0_Doku.pdf","Eine Untersuchungsfläche umschließt den Bereich, der archäologisch beobachtet wurde."), 
		adex_protectArea("adex_protectArea","area of protection","Schutzfläche","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_ADeX/ADeX_2-0_Doku.pdf","Die  Schutzfläche  ist  eine  Fläche,  die  nach  dem  jeweiligen  Landesgesetz  unter  Schutz  steht oder  für  die  ein  Antrag  auf  Unterschutzstellung  erfolgt  ist,  eventuell  ist  dieser  jedoch  noch nicht (vollständig) bearbeitet worden."),
		adex_planningArea("adex_planningArea","area of planning","(externe) Planungsfläche","http://www.landesarchaeologen.de/fileadmin/Dokumente/Dokumente_Kommissionen/Dokumente_Archaeologie-Informationssysteme/Dokumente_AIS_ADeX/ADeX_2-0_Doku.pdf","") 
		;

		public static String LIST_ID = "adex_area_type";
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String URL;
		public final String description;
	
		private TypeOfArea(String id, String labelEng, String labelGer, String URL, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.URL = URL;
			this.description = description;
		}
		
		public static TypeOfArea get(String id){
			for(TypeOfArea item : values()){
				if(StringUtils.equals(id, item.id)){
					return item;
				}
			}
			return null;
		}
	}
	
	/*
	public enum TypeOfArea{
		
		A1 ("", "", "Archäologiefläche", ""),
		A2 ("", "", "Untersuchungsfläche", ""),
		A3 ("", "", "Schutzfläche", ""),
		A4 ("", "", "(externe) Planungsfläche", "")
		;
		
		public String id;
		public String labelEng;
		public String labelGer;
		public String description;
		
		private TypeOfArea(String id, String labelEng, String labelGer, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
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
	*/
	@Override
	public String toString(){
		return "Place [id=" + id + "]";
		//return "Place [id=" + id + ", greaterRegion=" + greaterRegion + ", country=" + country + ", street="+ street +"]";
	}

	public String getGeometricType() {
		return geometricType;
	}
	
	public void setGeometricType(String geometricType) {
		this.geometricType = geometricType;
	}
}
