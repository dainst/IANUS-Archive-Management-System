package de.ianus.metadata.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.ianus.metadata.bo.utils.TextAttribute;

public class RetrievalUtils {
	
	
	/********************************************************************
	 * Language-Fallback handling and translated TextAttribute retrieval
	 ********************************************************************/
	
	public static final String[] languageFallbackOrder = new String[] {	"eng", "deu", "fra", "fre", "spa" };
	
	
	/**
	 * This Method will return the TextAttribute for the selected language, if present. 
	 * Otherwise, the returned language will be one of the list "languageFallbackOrder" 
	 * or the first available language.
	 */
	public static TextAttribute getTextAttribute(Set<TextAttribute> set, String languageCode) {
		// check the available languages for the requested, optionally use a fallback language
		languageCode = getFallbackLanguage(set, languageCode);
		
		//If the set if not null, we must find in the set the attribute in the searching language.
		return getTextAttributeByLanguage(set, languageCode);
	}
	
	public static TextAttribute getTextAttributeByLanguage(Set<TextAttribute> set, String languageCode) {
		if(set != null) {
			for(TextAttribute att : set){
				if(StringUtils.equals(att.getLanguageCode(), languageCode)){
					return att;
				}
			}
		}
		return null;
	}
	
	public static Set<String> getUtilizedLanguageCodes(Set<TextAttribute> attributeList){
		Set<String> codeList = new HashSet<String>();
		for(TextAttribute element : attributeList) {
			if(StringUtils.isNotEmpty(element.getLanguageCode()))
				codeList.add(element.getLanguageCode());
		}
		return codeList;
	}
	
	public static String getFallbackLanguage(Set<TextAttribute> list, String lang) {
		if(list == null || list.size() == 0)
			return lang;
		Set<String> available = getUtilizedLanguageCodes(list);
		int i = 0;
		while(!available.contains(lang)) {
			lang = languageFallbackOrder[i];
			i++;
			if(i >= languageFallbackOrder.length) break;
		}
		
		if(available.contains(lang)) {
			return lang;
		}else if(available.iterator().hasNext()) {
			return available.iterator().next();
		}
		
		return null;
	}
	
	
	
	
}
