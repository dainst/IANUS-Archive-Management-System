package de.ianus.ingest.web.pages.metadata;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.ApplicationBean;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.ingest.web.pages.component.TextAttributeBufferComponent;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.utils.Country;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.ElementOfList.ContentType;
import de.ianus.metadata.bo.utils.GreatRegion;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.TextAttribute;

/**
 * 
 * <p>
 * This class represents the current place that is editing. The information of
 * the editing place is deployed by the file place.xhtml.
 * </p>
 * <p>
 * The form for the edition of the current place is multi-language, because
 * there are some fields like Place.greaterRegion, Place.country, etc. that can
 * be stored in different languages (as TextAttribute). This form displays the
 * place only for a particular language. The displayed language should be
 * selected by the user, its selection is saved by the variable
 * PlacePage.currentLanguageCode. If the place has no languages, the user can
 * create a new one selecting a language from the list ISO639_3.
 * </p>
 * 
 * 
 * @author Jorge Urzúa
 *
 */
public class PlacePage extends DcComponentPage {
	
	private static final Logger logger = LogManager.getLogger(PlacePage.class);

	public static String PAGE_NAME = "place";

	// suggestion lists
	private static List<SelectItem> countryEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> countryGerList = new ArrayList<SelectItem>();
	private static List<SelectItem> greatRegionEngList = new ArrayList<SelectItem>();
	private static List<SelectItem> greatRegionGerList = new ArrayList<SelectItem>();
	private static List<SelectItem> geometricTypeList = new ArrayList<SelectItem>();
	private static List<SelectItem> typeOfAreaList = new ArrayList<SelectItem>();

	// loading of suggestion lists
	static {

		geometricTypeList.add(new SelectItem("", ApplicationBean.NONE_SELECTED_LABEL));
		for (Place.GeometricType item : Place.GeometricType.values()) {
			geometricTypeList.add(new SelectItem(item.labelGer));
		}
		for (Country.Type item : Country.Type.values()) {
			countryEngList.add(new SelectItem(item.labelEng));
			countryGerList.add(new SelectItem(item.labelGer));
		}
		for (GreatRegion.Type item : GreatRegion.Type.values()) {
			greatRegionEngList.add(new SelectItem(item.labelEng));
			greatRegionGerList.add(new SelectItem(item.labelGer));
		}
		for(Place.TypeOfArea item : Place.TypeOfArea.values()){
			typeOfAreaList.add(new SelectItem(item.id, item.labelGer));
		}
	}

	// current place in edition
	private Place place;

	// component used to collection third attributes
	private TextAttributeBufferComponent accuracyDescription;
	
	private TextAttributeBufferComponent greaterRegion;
	private String greaterRegionSuggestion;
	
	private TextAttributeBufferComponent country;
	private TextAttributeBufferComponent regionProvince;
	private TextAttributeBufferComponent city;
	private TextAttributeBufferComponent typeOfArea;

	private TextAttributeBufferComponent freeDescription;
	private TextAttributeBufferComponent historicalName;

	private ElementOfList currentIdentifier = new ElementOfList();
	private Set<ElementOfList> currentIdentifierList;
	// private ElementOfListComponent identifier;

	public boolean isShowCountryGer() {
		return StringUtils.equals(this.country.getTextAtt().getLanguageCode(), "deu");
	}

	public boolean isShowCountryEng() {
		return StringUtils.equals(this.country.getTextAtt().getLanguageCode(), "eng");
	}

	public boolean isShowCountryFreeText() {
		return !isShowCountryEng() && !isShowCountryGer();
	}

	public boolean isShowGreatRegionGer() {
		return StringUtils.equals(this.greaterRegion.getTextAtt().getLanguageCode(), "deu");
	}

	public boolean isShowGreatRegionEng() {
		return StringUtils.equals(this.greaterRegion.getTextAtt().getLanguageCode(), "eng");
	}

	public boolean isShowGreatRegionFreeText() {
		return !isShowGreatRegionEng() && !isShowGreatRegionGer();
	}
	
	public void onChangeCountryLanguage() {
		/*
		if(this.isShowCountryEng()){
			this.country.getTextAtt().setValue(this.getCountryEngList().get(0).getLabel());
		}else if(this.isShowCountryGer()){
			this.country.getTextAtt().setValue(this.getCountryGerList().get(0).getLabel());
		}else{
			this.country.getTextAtt().setValue(new String());
		}*/
	}
	/*
	public void onChangeCountrySuggestion(){
		this.country.getTextAtt().setValue(this.countrySuggestion);
	}*/
	
	public void onChangeGreaterRegionLanguage() {
		if(this.isShowGreatRegionEng()){
			this.greaterRegion.getTextAtt().setValue(this.getGreatRegionEngList().get(0).getLabel());
		}else if(this.isShowGreatRegionGer()){
			this.greaterRegion.getTextAtt().setValue(this.getGreatRegionGerList().get(0).getLabel());
		}else{
			this.greaterRegion.getTextAtt().setValue(new String());
		}
	}
	
	public void onChangeGreaterRegionSuggestion(){
		this.greaterRegion.getTextAtt().setValue(this.greaterRegionSuggestion);
	}
	
	public void loadPlace(DataCollection dcBase, IANUSEntity source, Place place0) throws Exception {
		super.load(dcBase, source);
		this.place = (place0.isPersistent()) ? Services.getInstance().getMDService().getPlace(place0.getId()) : place0;

		// this.historicalName = new TextAttributeComponent(place,
		// TextAttribute.ContentType.historicalName, PAGE_NAME);
		// this.identifier = new ElementOfListComponent(place,
		// ContentType.identifier);

		this.accuracyDescription = new TextAttributeBufferComponent(place, TextAttribute.ContentType.accuracyDescription, true, true);
		this.country = new TextAttributeBufferComponent(place, TextAttribute.ContentType.country, true, true);
		this.greaterRegion = new TextAttributeBufferComponent(place, TextAttribute.ContentType.greaterRegion, true, true);
		this.regionProvince = new TextAttributeBufferComponent(place, TextAttribute.ContentType.regionProvince, true, true);
		this.city = new TextAttributeBufferComponent(place, TextAttribute.ContentType.city, true, true);
		this.typeOfArea = new TextAttributeBufferComponent(place, TextAttribute.ContentType.typeOfArea, true, false);
		this.freeDescription = new TextAttributeBufferComponent(place, TextAttribute.ContentType.freeDescription, true, true);
		this.historicalName = new TextAttributeBufferComponent(place, TextAttribute.ContentType.historicalName, true, true);
		
		this.greaterRegionSuggestion = null;
		
		this.loadIdentifierList();
	}

	private void loadIdentifierList() {
		this.currentIdentifierList = new LinkedHashSet<ElementOfList>(this.place.getIdentifierList());
	}

	public void listenerAddIdentifier(ActionEvent event) {
		this.currentIdentifierList.add(this.currentIdentifier);
		this.currentIdentifier = new ElementOfList();
	}

	public void listenerEditIdentifier(ActionEvent event) {
		ElementOfList eol = (ElementOfList) getRequestBean("identifier");
		this.currentIdentifier = eol;
	}
	
	public void listenerCancelIdentifierEdition(ActionEvent event) {
		this.currentIdentifier = new ElementOfList();
	}
	
	public void listenerDeleteIdentifier(ActionEvent event) {
		ElementOfList eol = (ElementOfList) getRequestBean("identifier");
		this.currentIdentifierList.remove(eol);
	}

	private void saveIdentifier() throws Exception {
		for (ElementOfList item : this.currentIdentifierList) {
			item.setSource(this.place);
			item.setContentType(ContentType.identifier);
			Services.getInstance().getMDService().saveEntry(item);
		}

		for (ElementOfList oldItem : this.place.getIdentifierList()) {
			if (!this.currentIdentifierList.contains(oldItem)) {
				Services.getInstance().getMDService().deleteDBEntry(oldItem);
			}
		}
	}

	public String actionTransformWkt() {
		try {
			place.transformWkt();
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}

	public void listenerValidateWkt(ActionEvent event) {
		System.out.println("actionValidateWkt");
		Exception ex = place.validateWkt();
		if (ex != null) {
			addMsg(ex.toString());
		} else {
			addMsg("The Wkt is valid");
		}
	}

	public String actionSelectPlace() {
		return PAGE_NAME;
	}

	public String actionSavePlace() {
		Exception ex = place.validateWkt();
		if (ex == null) {
			try {
				this.place.setSource(this.getSource());
				Services.getInstance().getMDService().saveEntry(this.place);
				getSource().getPlaceList().add(place);
				
				//for the case, that before the call of this method, the place was not persistent,
				//then some attributeTexts can have no value on TextAttribute.sourceId
				// here we refresh the source attribute (id+class)
				/*
				for(Set<TextAttribute> set : this.place.getAttributeMap().values()){
					for(TextAttribute item : set){
						item.setSource(place);
					}
				}*/

				this.accuracyDescription.save();
				this.greaterRegion.save();
				this.country.save();
				this.regionProvince.save();
				this.city.save();
				this.freeDescription.save();
				this.historicalName.save();
				this.typeOfArea.save();
				this.saveIdentifier();

				this.loadPlace(getDcBase(), getSource(), getPlace());

			} catch (Exception e) {
				addInternalError(e);
			}
		} else {
			addMsg("The place could not be saved, because the Wkt is not valid.");
			addMsg(ex.getMessage());
		}
		return PAGE_NAME;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public TextAttributeBufferComponent getCountry() {
		return country;
	}

	public void setCountry(TextAttributeBufferComponent country) {
		this.country = country;
	}

	public TextAttributeBufferComponent getAccuracyDescription() {
		return accuracyDescription;
	}

	public void setAccuracyDescription(TextAttributeBufferComponent accuracyDescription) {
		this.accuracyDescription = accuracyDescription;
	}

	public TextAttributeBufferComponent getGreaterRegion() {
		return greaterRegion;
	}

	public void setGreaterRegion(TextAttributeBufferComponent greaterRegion) {
		this.greaterRegion = greaterRegion;
	}

	public TextAttributeBufferComponent getRegionProvince() {
		return regionProvince;
	}

	public void setRegionProvince(TextAttributeBufferComponent regionProvince) {
		this.regionProvince = regionProvince;
	}

	public TextAttributeBufferComponent getCity() {
		return city;
	}

	public void setCity(TextAttributeBufferComponent city) {
		this.city = city;
	}

	public List<SelectItem> getCountryEngList() {
		return countryEngList;
	}

	public List<SelectItem> getCountryGerList() {
		return countryGerList;
	}

	public List<SelectItem> getGreatRegionEngList() {
		return greatRegionEngList;
	}

	public List<SelectItem> getGreatRegionGerList() {
		return greatRegionGerList;
	}

	public List<SelectItem> getGeometricTypeList() {
		return geometricTypeList;
	}

	public TextAttributeBufferComponent getFreeDescription() {
		return freeDescription;
	}

	public void setFreeDescription(TextAttributeBufferComponent freeDescription) {
		this.freeDescription = freeDescription;
	}

	public TextAttributeBufferComponent getHistoricalName() {
		return historicalName;
	}

	public void setHistoricalName(TextAttributeBufferComponent historicalName) {
		this.historicalName = historicalName;
	}

	public Set<ElementOfList> getCurrentIdentifierList() {
		return currentIdentifierList;
	}

	public void setCurrentIdentifierList(Set<ElementOfList> currentIdentifierList) {
		this.currentIdentifierList = currentIdentifierList;
	}

	public ElementOfList getCurrentIdentifier() {
		return currentIdentifier;
	}

	public void setCurrentIdentifier(ElementOfList currentIdentifier) {
		this.currentIdentifier = currentIdentifier;
	}

	public TextAttributeBufferComponent getTypeOfArea() {
		return typeOfArea;
	}

	public void setTypeOfArea(TextAttributeBufferComponent typeOfArea) {
		this.typeOfArea = typeOfArea;
	}

	public List<SelectItem> getTypeOfAreaList() {
		return typeOfAreaList;
	}

	public String getGreaterRegionSuggestion() {
		return greaterRegionSuggestion;
	}

	public void setGreaterRegionSuggestion(String greaterRegionSuggestion) {
		this.greaterRegionSuggestion = greaterRegionSuggestion;
	}
	
	
}
