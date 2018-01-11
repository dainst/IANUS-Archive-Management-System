package de.ianus.metadata.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author jurzua
 *
 */
public class ISO639_3 {

	private static String FILE_ISO639_3 = "metadata/iso-639-3_20150505.tab";
	
	private List<ISOLanguage> langList = new ArrayList<ISOLanguage>();
	private static ISO639_3 instance;

	private ISO639_3(){
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(FILE_ISO639_3);
		StringBuilder sb = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    String line="";

		    while((line = reader.readLine()) != null){
		    	String[] split=line.split("\t");
		        //Id	Part2B	Part2T	Part1	Scope	Language_Type	Ref_Name	Comment
		    	ISOLanguage l = new ISOLanguage(split[0], split[4], split[5], split[6]);
		        //System.out.println(l);
		        this.langList.add(l);
		        sb.append(line);
		        //TODO sort
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try { is.close(); } catch (Throwable ignore) {}
		}
	}
	
	public List<ISOLanguage> searchEng0(String term, int maxSize){
		List<ISOLanguage> list = new ArrayList<ISOLanguage>();
		term = term.toLowerCase();
		int counter = 0;
		if(StringUtils.isNotEmpty(term)){
			for(ISOLanguage lan : this.getLangList()){
				if(StringUtils.startsWith(lan.getNameEngLowercase(), term)){
					list.add(lan);
					counter++;
				}
				if(counter == 10){
					break;
				}
			}	
		}
		return list;
	}

	public static ISO639_3 getInstance() {
		if (instance == null) {
			instance = new ISO639_3();
		}
		return instance;
	}

	public ISOLanguage getByNameEng0(String nameEng){
		for(ISOLanguage lan : getLangList()){
			if(StringUtils.equals(nameEng, lan.getNameEng()))
				return lan;
		}
		return null;
	}
	
	public ISOLanguage getById(String id){
		for(ISOLanguage lan : getLangList()){
			if(StringUtils.equals(id, lan.getId()))
				return lan;
		}
		return null;
	}
	
	public List<ISOLanguage> getLangList() {
		return langList;
	}

	public void setLangList(List<ISOLanguage> langList) {
		this.langList = langList;
	}
	
	public static void main(String[] args){
		ISO639_3.getInstance();
	}
}
