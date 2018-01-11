package de.ianus.metadata.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Based on ISO 639-3 (Languages)
 * 
 * @author jurzua
 *
 */
public class LanguageShortList {

	
private static final List<ISOLanguage> list = new ArrayList<ISOLanguage>();
	
	static{
		
		list.add(new ISOLanguage("egy","Egyptian (Ancient)","Ägyptisch"));
		list.add(new ISOLanguage("sqi","Albanian","Albanisch"));
		list.add(new ISOLanguage("ara","Arabic","Arabisch"));
		list.add(new ISOLanguage("grc","Greek, Ancient (to 1453)","Altgriechisch"));
		list.add(new ISOLanguage("eus","Basque","Baskisch"));
		list.add(new ISOLanguage("bul","Bulgarian","Bulgarisch"));
		list.add(new ISOLanguage("zho","Chinese","Chinesisch"));
		list.add(new ISOLanguage("deu","German","Deutsch"));
		list.add(new ISOLanguage("eng","English","Englisch"));
		list.add(new ISOLanguage("fra","French","Französisch"));
		list.add(new ISOLanguage("glg","Galician","Galicisch"));
		list.add(new ISOLanguage("ita","Italian","Italienisch"));
		list.add(new ISOLanguage("cat","Catalan; Valencian","Katalanisch"));
		list.add(new ISOLanguage("hrv","Croatian","Kroatisch"));
		list.add(new ISOLanguage("lat","Latin","Latein"));
		list.add(new ISOLanguage("ell","Greek, Modern (1453-)","Neugriechisch"));
		list.add(new ISOLanguage("nld","Dutch; Flemish","Niederländisch"));
		list.add(new ISOLanguage("fas","Persian","Persisch"));
		list.add(new ISOLanguage("pol","Polish","Polnisch"));
		list.add(new ISOLanguage("por","Portuguese","Portugiesisch"));
		list.add(new ISOLanguage("rus","Russian","Russisch"));
		list.add(new ISOLanguage("swe","Swedish","Schwedisch"));
		list.add(new ISOLanguage("srp","Serbian","Serbisch"));
		list.add(new ISOLanguage("spa","Spanish; Castilian","Spanisch"));
		list.add(new ISOLanguage("tur","Turkish","Türkisch"));
		list.add(new ISOLanguage("vie","Vietnamese","Vietnamesisch"));
	}
	

	public static List<ISOLanguage> searchEng(String term, int maxSize){
		List<ISOLanguage> list0 = new ArrayList<ISOLanguage>();
		term = term.toLowerCase();
		int counter = 0;
		if(StringUtils.isNotEmpty(term)){
			for(ISOLanguage lan : list){
				if(StringUtils.startsWith(lan.getNameEngLowercase(), term)){
					list0.add(lan);
					counter++;
				}
				if(counter == maxSize){
					break;
				}
			}	
		}
		return list0;
	}
	
	public static List<ISOLanguage> searchGer(String term, int maxSize){
		List<ISOLanguage> list0 = new ArrayList<ISOLanguage>();
		term = term.toLowerCase();
		int counter = 0;
		if(StringUtils.isNotEmpty(term)){
			for(ISOLanguage lan : list){
				if(StringUtils.startsWith(lan.getNameGerLowercase(), term)){
					list0.add(lan);
					counter++;
				}
				if(counter == maxSize){
					break;
				}
			}	
		}
		return list0;
	}
	
	
	public static ISOLanguage getByNameGer(String nameGer){
		for(ISOLanguage lan : list){
			if(StringUtils.equals(nameGer, lan.getNameGer()))
				return lan;
		}
		return null;
	}
	
	public static ISOLanguage getByNameEng(String nameEng){
		for(ISOLanguage lan : list){
			if(StringUtils.equals(nameEng, lan.getNameEng()))
				return lan;
		}
		return null;
	}
	
	public static ISOLanguage getById(String id){
		for(ISOLanguage lan : list){
			if(StringUtils.equals(id, lan.getId()))
				return lan;
		}
		return null;
	}
}
