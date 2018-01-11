package de.ianus.access.web.utils;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.ianus.access.web.SessionBean;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.utils.RetrievalUtils;
import de.ianus.metadata.utils.ValueListUtils;

public class CommonUtils {
	
	
	protected static final Logger logger = Logger.getLogger(CFUtils.class);
	
	protected DataCollection dc;
	
	protected SessionBean session;
	
	protected String languageCode = new String("deu");
	
	protected Map<String, Locale> localeMap;
	
	protected SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", new Locale("de"));
	
	protected static String defaultSeparator = " · ";
	
	
	
	public CommonUtils(DataCollection dc, SessionBean session) {
		this.dc = dc;
		this.session = session;
		this.languageCode = session.getLanguage();
		this.initCountryCodeMapping();
		this.df = new SimpleDateFormat("MMMM yyyy", new Locale(
				getIso2CountryCode(this.session.getLanguage())));
	}
	
	
	
	private void initCountryCodeMapping() {
	    String[] countries = Locale.getISOCountries();
	    localeMap = new HashMap<String, Locale>(countries.length);
	    for (String country : countries) {
	        Locale locale = new Locale("", country);
	        localeMap.put(locale.getISO3Country().toLowerCase(), locale);
	    }
	}
	
	public String getIso2CountryCode(String iso3CountryCode) {
	    return localeMap.get(iso3CountryCode.toLowerCase()).getCountry();
	}
	
	public static String getiso3CountryCode(String iso2CountryCode){
	    Locale locale = new Locale("", iso2CountryCode);
	    return locale.getISO3Country();
	}
	
	public Locale getLocale() {
		return new Locale(this.getIso2CountryCode(this.languageCode));
	}
	
	
	
	public static Boolean empty(String str) {
		if(str == null || str.equals("") || str.equals("null") || str.trim().length() == 0)
			return true;
		return false;		
	}
	
	
	
	public static String escape(String str) {
		if(empty(str)) return "";
		return escapeHtml(str);
	}
	
	
	
	/**
	 * Concatenation method for ElementOfList sets, using default separator " · " (superscript dot?)
	 * 
	 * @param Set<ElementOfList> set
	 * @return html-escaped String
	 */
	public static String implode(Set<ElementOfList> set) {
		return implode(set, defaultSeparator);
	}
	
	/**
	 * Generic concatenation method for ElementOfList sets, using a separator 
	 * 
	 * @param Set<ElementOfList> set
	 * @param String separator
	 * @return html-escaped String
	 */
	public static String implode(Set<ElementOfList> set, String separator) {
		String result = "";
		int i = 0;
		if(set != null) for(ElementOfList element : set) {
			if(empty(element.getValue())) continue;
			if(i > 0) result += separator;
			result += element.getValue();
			i++;
		}
		
		// do not escape the resulting string, in case the separator contains html tags
		return result;
	}
	
	/**
	 * Concatenation method for String lists, using default separator " · "
	 * 
	 * @param List<String> list
	 * @return html-escaped String
	 */
	public static String implode(List<String> list) {
		return implode(list, defaultSeparator);
	}
	
	/**
	 * List of Strings concatenation
	 * 
	 * @param List<String> list
	 * @param String separator
	 * @return html-escaped String
	 */
	public static String implode(List<String> list, String separator) {
		String result = "";
		int i = 0;
		if(list != null) for(String item : list) {
			if(empty(item)) continue; 
			if(i > 0) result += separator;
			result += item;
			i++;
		}
		return result;
	}
	
	
	
	public Boolean hasTextAttribute(TextAttribute.ContentType contentType) {
		return hasTextAttribute(contentType, this.languageCode);
	}
	
	public Boolean hasTextAttribute(TextAttribute.ContentType contentType, String lang) {
		if(this.getTextAttribute(contentType, lang) != null) return true;
		return false;
	}
	
	
	
	public TextAttribute getTextAttribute(TextAttribute.ContentType contentType) {
		return getTextAttribute(contentType, this.languageCode);
	}
	
	protected TextAttribute getTextAttribute(TextAttribute.ContentType contentType, String languageCode) {
		Set<TextAttribute> set = this.dc.getAttributeMap().get(contentType);
		return RetrievalUtils.getTextAttribute(set, languageCode);
	}
	
	
	
	public String getTextAttributeValue(TextAttribute.ContentType contentType) {
		return getTextAttributeValue(contentType, this.languageCode);
	}
	
	protected String getTextAttributeValue(TextAttribute.ContentType contentType, String languageCode) {
		TextAttribute att = getTextAttribute(contentType, languageCode);
		if(att != null) return att.getValue();
		return null;
	}
	
	
	
	
	public static List<String> getElementOfListList(Set<ElementOfList> list) {
		return getElementOfListList(list, false);
	}
	
	public static List<String> getPrioritizedElementOfListList(Set<ElementOfList> list) {
		// this is only used for localisation section
		return getElementOfListList(list, true);
	}
	
	private static List<String> getElementOfListList(Set<ElementOfList> list, Boolean prioritized) {
		if(list != null && list.size() > 0) {
			List<String> result = new ArrayList<String>();
			if(prioritized) for(ElementOfList element : list) {
				if(checkPriority(element)) {
					String temp = getElementOfListLink(element);
					if(!empty(temp))
						result.add(temp);
					break;
				}
			}
			for(ElementOfList element : list) {
				if(!prioritized || !checkPriority(element)) {
					String temp = element.getValue();
					if(prioritized) temp = "";	// hide the value (label) for localisation listing
					if(!empty(temp)) temp = escape(temp);
					if(!empty(getElementOfListLink(element))) 
						if(!empty(temp)) {
							if(prioritized) {
								temp += " " + getElementOfListLink(element);
							}
							else {
								String listLink = getElementOfListLink(element);
								if(!empty(listLink)) temp += " (" + listLink + ")";
							}
						}
					if(!empty(temp))
						result.add(temp);
				}
			}
			if(result.size() > 0)
				return result;
		}
		return null;
	}
	
	public static List<String> getElementOfListList(List<ElementOfList> list) {
		// assume we have a pre-sorted list, that does not require sorting for priority categories,
		// ie. time list
		if(list != null && list.size() > 0) {
			List<String> result = new ArrayList<String>();
			for(ElementOfList element : list) {
				String temp = element.getValue();	// the string name/label
				if(!empty(temp)) temp = escape(temp);
				if(!empty(getElementOfListLink(element))) 
					if(!empty(temp)) {
						String listLink = getElementOfListLink(element);
						if(!empty(listLink)) temp += " (" + listLink + ")";
					}
				if(!empty(temp))
					result.add(temp);
			}
			if(result.size() > 0)
				return result;
		}
		return null;
	}
	
	protected static String getElementOfListLink(ElementOfList element) {
		String result = null;
		String urn = getElementOfListUrn(element); 
		if(!empty(urn)) {
			String label = ValueListUtils.Names.getFieldById(element.getListId(), "label");
			if(urn.startsWith("http")) {
				result = "<a href=\"" + urn + "\" target=\"_blank\">";
				result += escape(label);
				result += "</a>";
			}
			// else: do not show non-linkable identifiers like this "listId: valueId"
		}
		return result;
	}
	
	private static String getElementOfListUrn(ElementOfList element) {
		String prefix = ValueListUtils.Names.getFieldById(element.getListId(), "url");
		
		if(!empty(prefix) && !empty(element.getValueId())) {
			return prefix + element.getValueId();
		}
		else{
			if(!empty(element.getUri())) {
				return element.getUri();
			}
			else if(!empty(element.getValueId())) {
				return element.getValueId();
			}
		}
		
		return null;
	}
	
	private static Boolean checkPriority(ElementOfList element) {
		if(	element.getListId().equals(ValueListUtils.Names.idai_chronontology.id)
				||	element.getListId().equals(ValueListUtils.Names.idai_gazetteer.id)
				||	element.getListId().equals(ValueListUtils.Names.idai_thesaurus_all.id)
				||	element.getListId().equals(ValueListUtils.Names.idai_thesaurus_mainMethod.id)
				||	element.getListId().equals(ValueListUtils.Names.idai_thesaurus_SubMethod.id)
				|| 	element.getListId().equals("idai_thesaurus_mainDiscipline")
				||	element.getListId().equals("idai_thesaurus_subDiscipline")
				) return true;
		return false;
	}
	
	
}
