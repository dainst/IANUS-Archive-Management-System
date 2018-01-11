package de.ianus.metadata.utils;

import org.apache.commons.lang.StringUtils;

import de.ianus.metadata.bo.utils.ElementOfList;

/**
 * this class loads from the file discipline_list.json the elements thta can be used by the dataCollection.discipline
 * @author jurzua
 *
 */
public class DisciplineList extends AbstractValueList{

	private static DisciplineList instance;

	public static String LIST_ID_MAIN = "idai_thesaurus_mainDiscipline";
	public static String LIST_ID_SUB = "idai_thesaurus_subDiscipline";
	
	public static DisciplineList getInstance0() throws Exception{
		if(instance == null){
			//instance = new DisciplineList();
			//instance.loadValues("ianus_discipline");
		}
		return instance;
	}
	
	public enum SubDiscipline{
		
		_aaaaaaaa(IGNORE_ID + "a","- archaeology -","- Archäologie -","",""),
		_1d40b9f0("_1d40b9f0","Egyptian archaeology","Ägyptische Archäologie","http://thesauri.dainst.org/de/concepts/_1d40b9f0.html",""),
		_b189d13f("_b189d13f","African archaeology","Afrikanische Archäologie","http://thesauri.dainst.org/de/concepts/_b189d13f.html",""),
		_5fe5046d("_5fe5046d","Archaeology of the Americas","Altamerikanische Archäologie","http://thesauri.dainst.org/de/concepts/_5fe5046d.html",""),
		_da2f7098("_da2f7098","Biblical archaeology","Biblische Archäologie","http://thesauri.dainst.org/de/concepts/_da2f7098.html",""),
		_d9e23b78("_d9e23b78","Byzantine archaeology","Byzantinische Archäologie","http://thesauri.dainst.org/de/concepts/_d9e23b78.html",""),
		_b6f68133("_b6f68133","Christian archaeology","Christliche Archäologie","http://thesauri.dainst.org/de/concepts/_b6f68133.html",""),
		_82abcc2e("_82abcc2e","Islamic archaeology","Islamische Archäologie","http://thesauri.dainst.org/de/concepts/_b6f68133.html",""),
		_0f84e6e3("_0f84e6e3","Classical archaeology","Klassische Archäologie","http://thesauri.dainst.org/de/concepts/_0f84e6e3.html",""),
		_799d17b8("_799d17b8","Medieval archaeology","Mittelalterarchäologie","http://thesauri.dainst.org/de/concepts/_799d17b8.html",""),
		_4d5f985d("_4d5f985d","Archaeology of the roman province","Provinzialrömische Archäologie","http://thesauri.dainst.org/de/concepts/_4d5f985d.html",""),
		_8dcfed63("_8dcfed63","Protohistoric and historic archaeology","Prähistorische und historische Archäologie","http://thesauri.dainst.org/de/concepts/_8dcfed63.html",""),
		_sudanarchaeology("_sudanarchaeology","Archaeology of Sudan","Sudanarchäologie", "", ""),
		_546deb86("_546deb86","Near Eastern archaeology","Vorderasiatische Archäologie","http://thesauri.dainst.org/de/concepts/_546deb86.html",""),
		_bbbbbbbb(IGNORE_ID + "b","- philology -","- Philologie -","",""),
		_2ff1dafc("_2ff1dafc","Philology of the Americas","Altamerikanistik","http://thesauri.dainst.org/de/concepts/_2ff1dafc.html",""),
		_556df59a("_556df59a","Ancient Oriental Philology","Altorientalistik","http://thesauri.dainst.org/de/concepts/_556df59a.html",""),
		_0b83f301("_0b83f301","Latin philology","Lateinische Philologie","http://thesauri.dainst.org/de/concepts/_0b83f301.html",""),
		_db6b5f2("_db6b5f2","Greek philology","Griechische Philologie","http://thesauri.dainst.org/de/concepts/_db6b5f2.html",""),
		_e410919d("_e410919d","Egyptian philology","Ägyptische Philologie","http://thesauri.dainst.org/de/concepts/_e410919d.html",""),
		_cccccccc(IGNORE_ID + "c","- history -","- Geschichtswissenschaften -","",""),
		_a2a28fb4("_a2a28fb4","Ancient history","Alte Geschichte","http://thesauri.dainst.org/de/concepts/_a2a28fb4.html",""),
		_f20eb2df("_f20eb2df","Byzantine history","Byzantinische Geschichte","http://thesauri.dainst.org/de/concepts/_f20eb2df.html",""),
		_48943c59("_48943c59","epigraphy","Epigraphik","http://thesauri.dainst.org/de/concepts/_48943c59.html",""),
		_032f2d33("_032f2d33","Islamic history","Islamische Geschichte","http://thesauri.dainst.org/de/concepts/_032f2d33.html",""),
		_1f71d5dc("_1f71d5dc","History of Art","Kunstgeschichte","http://thesauri.dainst.org/de/concepts/_1f71d5dc.html",""),
		_f9ef1469("_f9ef1469","Middle Ages and modern history","Mittlere und Neuere Geschichte","http://thesauri.dainst.org/de/concepts/_f9ef1469.html",""),
		_5222c31e("_5222c31e","Numismatics","Numismatik","http://thesauri.dainst.org/de/concepts/_5222c31e.html",""),
		_7df250af("_7df250af","papyrology","Papyrologie","http://thesauri.dainst.org/de/concepts/_7df250af.html",""),
		_6fe45c97("_6fe45c97","History of science","Wissenschaftsgeschichte","http://thesauri.dainst.org/de/concepts/_6fe45c97.html",""),
		_dddddddd(IGNORE_ID + "d","- architecture -","- Architektur -","",""),
		_90b5feb6("_90b5feb6","History of Architecture","Architekturgeschichte","http://thesauri.dainst.org/de/concepts/_90b5feb6.html",""),
		_hhhhhhhh(IGNORE_ID + "h","- natural sciences -","- Naturwissenschaften -","",""),
		_a5d5d015("_a5d5d015","anthropology","Anthropologie","http://thesauri.dainst.org/de/concepts/_a5d5d015.html",""),
		_81040b31("_81040b31","Archaeological science","Archäometrie","http://thesauri.dainst.org/de/concepts/_81040b31.html",""),
		_d613c54c("_d613c54c","Zooarchaeology","Archäozoologie","http://thesauri.dainst.org/de/concepts/_d613c54c.html",""),
		_ada8c3a5("_ada8c3a5","Archaeobotany","Archäobotanik","http://thesauri.dainst.org/de/concepts/_ada8c3a5.html",""),
		_eeeeeeee(IGNORE_ID + "e","- geosciences -","- Geowissenschaften -","",""),
		_geoarchaeology("_geoarchaeology","geoarchaeology","Geoarchäologie", "", ""),
		_249213dd("_249213dd","geography","Geographie","http://thesauri.dainst.org/de/concepts/_249213dd",""),
		_50bbf224("_50bbf224","geology","Geologie","http://thesauri.dainst.org/de/concepts/_50bbf224.html",""),
		_35255410("_35255410","geodesy","Geodäsie","http://thesauri.dainst.org/de/concepts/_35255410.html",""),
		_geophysics("_geophysics","geophysics","Geophysik","", ""),
		_9f4ba96c("_9f4ba96c","cartography","Kartographie","http://thesauri.dainst.org/de/concepts/_9f4ba96c",""),
		_ffffffff(IGNORE_ID + "f","- ethnology -","- Ethnologie -","",""),
		_gggggggg(IGNORE_ID + "g","- sociology -","- Soziologie -","","")
		
		/*
		//_e7ad4447("_e7ad4447","archaeology","Archäologie","http://thesauri.dainst.org/de/concepts/_e7ad4447.html",""),
		_1d40b9f0("_1d40b9f0","Egyptian archaeology","Ägyptische Archäologie","http://thesauri.dainst.org/de/concepts/_1d40b9f0.html",""),
		_b189d13f("_b189d13f","African archaeology","Afrikanische Archäologie","http://thesauri.dainst.org/de/concepts/_b189d13f.html",""),
		_5fe5046d("_5fe5046d","Archaeology of the Americas","Altamerikanische Archäologie","http://thesauri.dainst.org/de/concepts/_5fe5046d.html",""),
		_da2f7098("_da2f7098","Biblical archaeology","Biblische Archäologie","http://thesauri.dainst.org/de/concepts/_da2f7098.html",""),
		_d9e23b78("_d9e23b78","Byzantine archaeology","Byzantinische Archäologie","http://thesauri.dainst.org/de/concepts/_d9e23b78.html",""),
		_b6f68133("_b6f68133","Christian archaeology","Christliche Archäologie","http://thesauri.dainst.org/de/concepts/_b6f68133.html",""),
		_82abcc2e("_82abcc2e","Islamic archaeology","Islamische Archäologie","http://thesauri.dainst.org/de/concepts/_b6f68133.html",""),
		_0f84e6e3("_0f84e6e3","Classical archaeology","Klassische Archäologie","http://thesauri.dainst.org/de/concepts/_0f84e6e3.html",""),
		_799d17b8("_799d17b8","Medieval archaeology","Mittelalterarchäologie","http://thesauri.dainst.org/de/concepts/_799d17b8.html",""),
		_4d5f985d("_4d5f985d","Archaeology of the roman province","Provinzialrömische Archäologie","http://thesauri.dainst.org/de/concepts/_4d5f985d.html",""),
		_8dcfed63("_8dcfed63","Protohistoric and historic archaeology","Prähistorische und historische Archäologie","http://thesauri.dainst.org/de/concepts/_8dcfed63.html",""),
		_546deb86("_546deb86","Near Eastern archaeology","Vorderasiatische Archäologie","http://thesauri.dainst.org/de/concepts/_546deb86.html",""),
		//_e46572f1("_e46572f1","philology","Philologie","http://thesauri.dainst.org/de/concepts/_e46572f1.html",""),
		_2ff1dafc("_2ff1dafc","Philology of the Americas","Altamerikanistik","http://thesauri.dainst.org/de/concepts/_2ff1dafc.html",""),
		_556df59a("_556df59a","Ancient Oriental Philology","Altorientalistik","http://thesauri.dainst.org/de/concepts/_556df59a.html",""),
		_0b83f301("_0b83f301","Latin philology","Lateinische Philologie","http://thesauri.dainst.org/de/concepts/_0b83f301.html",""),
		_db6b5f2("_db6b5f2","Greek philology","Griechische Philologie","http://thesauri.dainst.org/de/concepts/_db6b5f2.html",""),
		_e410919d("_e410919d","Egyptian philology","Ägyptische Philologie","http://thesauri.dainst.org/de/concepts/_e410919d.html",""),
		//_a4ef7591("_a4ef7591","History","Geschichtswissenschaften","http://thesauri.dainst.org/de/concepts/_a4ef7591.html",""),
		_a2a28fb4("_a2a28fb4","Ancient history","Alte Geschichte","http://thesauri.dainst.org/de/concepts/_a2a28fb4.html",""),
		_f20eb2df("_f20eb2df","Byzantine history","Byzantinische Geschichte","http://thesauri.dainst.org/de/concepts/_f20eb2df.html",""),
		_f9ef1469("_f9ef1469","Middle Ages and modern history","Mittlere und Neuere Geschichte","http://thesauri.dainst.org/de/concepts/_f9ef1469.html",""),
		_1f71d5dc("_1f71d5dc","History of Art","Kunstgeschichte","http://thesauri.dainst.org/de/concepts/_1f71d5dc.html",""),
		_032f2d33("_032f2d33","Islamic history","Islamische Geschichte","http://thesauri.dainst.org/de/concepts/_032f2d33.html",""),
		_6fe45c97("_6fe45c97","History of science","Wissenschaftsgeschichte","http://thesauri.dainst.org/de/concepts/_6fe45c97.html",""),
		_7df250af("_7df250af","papyrology","Papyrologie","http://thesauri.dainst.org/de/concepts/_7df250af.html",""),
		_48943c59("_48943c59","epigraphy","Epigraphik","http://thesauri.dainst.org/de/concepts/_48943c59.html",""),
		_5222c31e("_5222c31e","Numismatics","Numismatik","http://thesauri.dainst.org/de/concepts/_5222c31e.html",""),
		_90b5feb6("_90b5feb6","History of Architecture","Architekturgeschichte","http://thesauri.dainst.org/de/concepts/_90b5feb6.html",""),
		//_5677c98f("_5677c98f","natural sciences","Naturwissenschaften","http://thesauri.dainst.org/de/concepts/_5677c98f.html",""),
		_a5d5d015("_a5d5d015","anthropology","Anthropologie","http://thesauri.dainst.org/de/concepts/_a5d5d015.html",""),
		_81040b31("_81040b31","Archaeological science","Archäometrie","http://thesauri.dainst.org/de/concepts/_81040b31.html",""),
		_d613c54c("_d613c54c","Zooarchaeology","Archäozoologie","http://thesauri.dainst.org/de/concepts/_d613c54c.html",""),
		_ada8c3a5("_ada8c3a5","Archaeobotany","Archäobotanik","http://thesauri.dainst.org/de/concepts/_ada8c3a5.html",""),
		//_1138a290("_1138a290","geosciences","Geowissenschaften","http://thesauri.dainst.org/de/concepts/_1138a290.html",""),
		_249213dd("_249213dd","geography","Geographie","http://thesauri.dainst.org/de/concepts/_249213dd",""),
		_50bbf224("_50bbf224","geology","Geologie","http://thesauri.dainst.org/de/concepts/_50bbf224.html",""),
		_35255410("_35255410","geodesy","Geodäsie","http://thesauri.dainst.org/de/concepts/_35255410.html",""),
		_geophysics("_geophysics","geophysics","Geophysik","", ""),
		_geoarchaeology("_geoarchaeology","geoarchaeology","Geoarchäologie", "", "")
		//_4aab6eb7("_4aab6eb7","ethnology","Ethnologie","http://thesauri.dainst.org/de/concepts/_4aab6eb7.html","")
		//_e93c0b3e("_e93c0b3e","sociology","Soziologie","http://thesauri.dainst.org/de/concepts/_e93c0b3e.html","")
		 
		 */
		;
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;
		
		private SubDiscipline(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		
		public static SubDiscipline getById(String id) {
			for(SubDiscipline type : SubDiscipline.values())
				if(StringUtils.equals(id, type.id)) return type;
			return null;
		}
		
		public static String getLabelEng(String valueId){
			for(SubDiscipline type : SubDiscipline.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(SubDiscipline type : SubDiscipline.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
		
		public static ElementOfList get(String id){
			for(SubDiscipline item : SubDiscipline.values()){
				if(StringUtils.equals(id, item.id)){
					ElementOfList eof = new ElementOfList(LIST_ID_SUB, item.id, item.labelGer, item.url);
					return eof;
				}
			}
			return null;
		}
	}
	
	public enum MainDiscipline{
		
		_e7ad4447("_e7ad4447","archaeology","Archäologie","http://thesauri.dainst.org/de/concepts/_e7ad4447.html",""),
		_e46572f1("_e46572f1","philology","Philologie","http://thesauri.dainst.org/de/concepts/_e46572f1.html",""),
		_a4ef7591("_a4ef7591","history","Geschichtswissenschaften","http://thesauri.dainst.org/de/concepts/_a4ef7591.html",""),
		_499426a1("_499426a1","architecture","Architektur","http://thesauri.dainst.org/de/concepts/_499426a1.html",""),
		_5677c98f("_5677c98f","natural sciences","Naturwissenschaften","http://thesauri.dainst.org/de/concepts/_5677c98f.html",""),
		_1138a290("_1138a290","geosciences","Geowissenschaften","http://thesauri.dainst.org/de/concepts/_1138a290.html",""),
		_4aab6eb7("_4aab6eb7","ethnology","Ethnologie","http://thesauri.dainst.org/de/concepts/_4aab6eb7.html",""),
		_e93c0b3e("_e93c0b3e","sociology","Soziologie","http://thesauri.dainst.org/de/concepts/_e93c0b3e.html","")
		
		/*
		_6f2f1990	("_6f2f1990", "excavation", "Ausgrabung", "http://thesauri.dainst.org/de/concepts/_6f2f1990.html"),
		_ec45464a	("_ec45464a", "fieldwalking survey", "Oberflächenbegehung (Survey)", "http://thesauri.dainst.org/de/concepts/_6f2f1990.html"),
		_704af0f8	("_704af0f8", "geophysical survey", "Geophysikalische Untersuchungen", "http://thesauri.dainst.org/de/concepts/_ec45464a.html"),
		_ec0ef733	("_ec0ef733", "soil examination", "Bodenuntersuchungen", "http://thesauri.dainst.org/de/concepts/_6f2f1990.html"),
		_669ddb20	("_669ddb20", "building survey", "Architektonische Bauaufnahme", "http://thesauri.dainst.org/de/concepts/_669ddb20.html"),
		_39ae6301	("_39ae6301", "reconstruction", "Rekonstruktionen", "http://thesauri.dainst.org/de/concepts/_39ae6301.html"),
		_1c80ccc7	("_1c80ccc7", "material examination (anorganic)", "Materialuntersuchungen (anorganisch)", "http://thesauri.dainst.org/de/concepts/_1c80ccc7.html"),
		_9d8c9587	("_9d8c9587", "material examination (organic)", "Materialuntersuchungen (organisch)", "http://thesauri.dainst.org/de/concepts/_9d8c9587.html"),
		_440058d2	("_440058d2", "survey and geodesy", "Vermessung und Geodäsie", "http://thesauri.dainst.org/de/concepts/_440058d2.html"),
		_fbd19e6	("_fbd19e6", "digital documentation", "Digitale Dokumentation", "http://thesauri.dainst.org/de/concepts/_fbd19e6.html"),
		_1044ea9	("_1044ea9", "analog documentation", "Analoge Dokumentation", "http://thesauri.dainst.org/de/concepts/_1044ea9.html"),
		_fd386087	("_fd386087", "spatial analysis", "Räumliche Auswertungen", "http://thesauri.dainst.org/de/concepts/_fd386087.html"),
		_1cbc6e54	("_1cbc6e54", "remote sensing", "Fernerkundung", "http://thesauri.dainst.org/de/concepts/_1cbc6e54.html"),
		_57dfb12d	("_57dfb12d", "experimental archaeology", "Experimentelle Archäologie", "http://thesauri.dainst.org/de/concepts/_57dfb12d.html"),
		_e9cce57c	("_e9cce57c", "finds analysis", "Fundanalyse", "http://thesauri.dainst.org/de/concepts/_e9cce57c.html"),
		_790b25d2	("_790b25d2", "arthistorical concepts", "Kunsttheoretische Konzepte", "http://thesauri.dainst.org/de/concepts/_790b25d2.html"),
		_9f06301d	("_9f06301d", "bibliographical research", "Literaturrecherche", "http://thesauri.dainst.org/de/concepts/_9f06301d.html"),
		_577b1bc0	("_577b1bc0", "retro digitization", "Retrodigitalisierung", "http://thesauri.dainst.org/de/concepts/_577b1bc0.html"),
		_c9372069	("_c9372069", "assessment of sources", "Quellenkritik", "http://thesauri.dainst.org/de/concepts/_c9372069.html"),
		_aa237fc0	("_aa237fc0", "data re-use", "Datennachnutzung", "http://thesauri.dainst.org/de/concepts/_aa237fc0.html")
		*/
		;
		public final String id;
		public final String labelEng;
		public final String labelGer;
		public final String url;
		public final String description;
		
		private MainDiscipline(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		
		public static MainDiscipline getById(String id) {
			for(MainDiscipline type : MainDiscipline.values())
				if(StringUtils.equals(id, type.id)) return type;
			return null;
		}
		
		public static String getLabelEng(String valueId){
			for(MainDiscipline type : MainDiscipline.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(MainDiscipline type : MainDiscipline.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
		
		public static ElementOfList get(String id){
			for(MainDiscipline item : MainDiscipline.values()){
				if(StringUtils.equals(id, item.id)){
					ElementOfList eof = new ElementOfList(LIST_ID_MAIN, item.id, item.labelGer, item.url);
					return eof;
				}
			}
			return null;
		}
	}	
	
}
