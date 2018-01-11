package de.ianus.ingest.web.pages.component;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.AbstractBean;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.TextAttribute;

public class PlaceLanguage extends AbstractBean{

	private Place place;
	private String languageCode;
	
	private TextAttribute accuracyDescription;
	private TextAttribute greaterRegion;
	private TextAttribute country;
	private TextAttribute regionProvince;
	private TextAttribute city;
	private TextAttribute freeDescription;
	
	public void loadPlaceLanguage(Place place, String languageCode) throws Exception{
		this.place = place;
		this.accuracyDescription = place.getTextAttribute(TextAttribute.ContentType.accuracyDescription, languageCode);
		this.greaterRegion = place.getTextAttribute(TextAttribute.ContentType.greaterRegion, languageCode);
		this.country = place.getTextAttribute(TextAttribute.ContentType.country, languageCode);
		this.regionProvince = place.getTextAttribute(TextAttribute.ContentType.regionProvince, languageCode);
		this.city = place.getTextAttribute(TextAttribute.ContentType.city, languageCode);
		this.freeDescription = place.getTextAttribute(TextAttribute.ContentType.freeDescription, languageCode);
		
		// if some of the attributes is null, we will create a new one and we will do it/them persistent
		this.accuracyDescription = (accuracyDescription == null) ? createTextAttribute(TextAttribute.ContentType.accuracyDescription, languageCode) : this.accuracyDescription;
		this.greaterRegion  = (greaterRegion == null) ? createTextAttribute(TextAttribute.ContentType.greaterRegion, languageCode) : this.greaterRegion;
		this.country  = (country == null) ? createTextAttribute(TextAttribute.ContentType.country, languageCode) : this.country;
		this.regionProvince  = (regionProvince == null) ? createTextAttribute(TextAttribute.ContentType.regionProvince, languageCode) : this.regionProvince;
		this.city  = (city == null) ? createTextAttribute(TextAttribute.ContentType.city, languageCode) : this.city;
		this.freeDescription  = (freeDescription == null) ? createTextAttribute(TextAttribute.ContentType.freeDescription, languageCode) : this.freeDescription;
		
		getSession().getPlacePage().getPlace().getAccuracyDescriptionList().add(accuracyDescription);
		getSession().getPlacePage().getPlace().getGreaterRegionList().add(greaterRegion);
		getSession().getPlacePage().getPlace().getCountryList().add(country);
		getSession().getPlacePage().getPlace().getRegionProvinceList().add(regionProvince);
		getSession().getPlacePage().getPlace().getCityList().add(city);
		getSession().getPlacePage().getPlace().getFreeDescriptionList().add(freeDescription);
	}
	
	public TextAttribute createTextAttribute(TextAttribute.ContentType contentType, String languageCode) throws Exception{
		TextAttribute att = new TextAttribute();
		att.setSourceId(place.getId());
		att.setContentType(contentType);
		att.setSourceClass(BOUtils.SourceClass.Place);
		att.setLanguageCode(languageCode);
		Services.getInstance().getMDService().saveEntry(att);
		return att;
	}
	
	
	public void save() throws Exception{
		Services.getInstance().getMDService().saveEntry(accuracyDescription);
		Services.getInstance().getMDService().saveEntry(greaterRegion);
		Services.getInstance().getMDService().saveEntry(country);
		Services.getInstance().getMDService().saveEntry(regionProvince);
		Services.getInstance().getMDService().saveEntry(city);
		Services.getInstance().getMDService().saveEntry(freeDescription);
		
		getSession().getPlacePage().getPlace().getAccuracyDescriptionList().add(accuracyDescription);
		getSession().getPlacePage().getPlace().getGreaterRegionList().add(greaterRegion);
		getSession().getPlacePage().getPlace().getCountryList().add(country);
		getSession().getPlacePage().getPlace().getRegionProvinceList().add(regionProvince);
		getSession().getPlacePage().getPlace().getCityList().add(city);
		getSession().getPlacePage().getPlace().getFreeDescriptionList().add(freeDescription);
	}
	
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public TextAttribute getAccuracyDescription() {
		return accuracyDescription;
	}
	public void setAccuracyDescription(TextAttribute accuracyDescription) {
		this.accuracyDescription = accuracyDescription;
	}
	public TextAttribute getGreaterRegion() {
		return greaterRegion;
	}
	public void setGreaterRegion(TextAttribute greaterRegion) {
		this.greaterRegion = greaterRegion;
	}
	public TextAttribute getCountry() {
		return country;
	}
	public void setCountry(TextAttribute country) {
		this.country = country;
	}
	public TextAttribute getRegionProvince() {
		return regionProvince;
	}
	public void setRegionProvince(TextAttribute regionProvince) {
		this.regionProvince = regionProvince;
	}
	public TextAttribute getCity() {
		return city;
	}
	public void setCity(TextAttribute city) {
		this.city = city;
	}

	public TextAttribute getFreeDescription() {
		return freeDescription;
	}

	public void setFreeDescription(TextAttribute freeDescription) {
		this.freeDescription = freeDescription;
	}
}
