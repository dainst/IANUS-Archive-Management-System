package de.ianus.metadata.utils;

import org.apache.commons.lang.StringUtils;

import de.ianus.metadata.bo.utils.ElementOfList;

public class MethodList {

	public static String LIST_ID_MAIN = "idai_thesaurus_mainMethod";
	public static String LIST_ID_SUB = "idai_thesaurus_subMethod";

	
	public enum MainMethod{
	
		_6f2f1990("_6f2f1990","excavation","Ausgrabung","http://thesauri.dainst.org/de/concepts/_6f2f1990.html",""),
		_ec45464a("_ec45464a","fieldwalking survey","Oberflächenbegehung (Survey)","http://thesauri.dainst.org/de/concepts/_ec45464a.html",""),
		_704af0f8("_704af0f8","geophysical survey","Geophysikalische Untersuchungen","http://thesauri.dainst.org/de/concepts/_704af0f8.html",""),
		_ec0ef733("_ec0ef733","soil examination","Bodenuntersuchungen","http://thesauri.dainst.org/de/concepts/_ec0ef733.html",""),
		_3f254a06("_3f254a06","building history research","Bauforschung","http://thesauri.dainst.org/de/concepts/_3f254a06.html",""),
		_1c80ccc7("_1c80ccc7","material examination (anorganic)","Materialuntersuchungen (anorganisch)","http://thesauri.dainst.org/de/concepts/_1c80ccc7.html",""),
		_9d8c9587("_9d8c9587","material examination (organic)","Materialuntersuchungen (organisch)","http://thesauri.dainst.org/de/concepts/_9d8c9587.html",""),
		_440058d2("_440058d2","survey and geodesy","Vermessung und Geodäsie","http://thesauri.dainst.org/de/concepts/_440058d2.html",""),
		_fbd19e6("_fbd19e6","digital documentation","Digitale Dokumentation","http://thesauri.dainst.org/de/concepts/_fbd19e6.html",""),
		_1044ea9("_1044ea9","analog documentation","Analoge Dokumentation","http://thesauri.dainst.org/de/concepts/_1044ea9.html",""),
		_fd386087("_fd386087","spatial analysis","Räumliche Auswertungen","http://thesauri.dainst.org/de/concepts/_fd386087.html",""),
		_1cbc6e54("_1cbc6e54","remote sensing","Fernerkundung","http://thesauri.dainst.org/de/concepts/_1cbc6e54.html",""),
		_57dfb12d("_57dfb12d","experimental archaeology","Experimentelle Archäologie","http://thesauri.dainst.org/de/concepts/_57dfb12d.html",""),
		_e9cce57c("_e9cce57c","finds analysis","Fundanalyse","http://thesauri.dainst.org/de/concepts/_e9cce57c.html",""),
		_790b25d2("_790b25d2","arthistorical concepts","Kunsttheoretische Konzepte","http://thesauri.dainst.org/de/concepts/_790b25d2.html",""),
		_evaltext("_evaltext","evaluation of texts","Auswertung von Texten","",""),
		_577b1bc0("_577b1bc0","retro digitization","Retrodigitalisierung","http://thesauri.dainst.org/de/concepts/_577b1bc0.html",""),
		_aa237fc0("_aa237fc0","data re-use","Datennachnutzung","http://thesauri.dainst.org/de/concepts/_aa237fc0.html","")
		;
	
		public String id;
		public String labelEng;
		public String labelGer;
		public String url;
		public String description;
		
		private MainMethod(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static String getLabelEng(String valueId){
			for(MainMethod type : MainMethod.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(MainMethod type : MainMethod.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
		
		public static ElementOfList getEolById(String id){
			for(MainMethod type : MainMethod.values()){
				if(StringUtils.equals(id, type.id)){
					return new ElementOfList(LIST_ID_MAIN, type.id, type.labelGer, type.url);
				}
			}
			return null;
		}
	}
	
	public enum SubMethod{
		
		_aaaaaaaa(AbstractValueList.IGNORE_ID + "a","- excavation -","- Ausgrabung -","",""),
		_3e16c140("_3e16c140","wet soil archaeology","Feuchtbodenarchäologie","http://thesauri.dainst.org/de/concepts/_3e16c140.html",""),
		_7c9cedaf("_7c9cedaf","underwater excavation","Unterwasserarchäologie","http://thesauri.dainst.org/de/concepts/_7c9cedaf.html",""),
		_c5a8310c("_c5a8310c","mining archaeology","Montanarchäologie","http://thesauri.dainst.org/de/concepts/_c5a8310c.html",""),
		_bbbbbbbb(AbstractValueList.IGNORE_ID + "b","- fieldwalking survey -","- Oberflächenbegehung (Survey) -","",""),
		_archaesurinten("_archaesurinten","archaeological survey (intensive)","Archäologischer Survey (intensiv)","",""),
		_archaesurexten("_archaesurexten","archaeological survey (extensive)","Archäologischer Survey (extensiv)","",""),
		_architecsur("_architecsur","architectural survey","Architektonischer Survey","",""),
		_cccccccc(AbstractValueList.IGNORE_ID + "c","- geophysical survey -","- Geophysikalische Untersuchungen -","",""),
		_fb2b810d("_fb2b810d","electrical resistivity tomography","Geoelektrik","http://thesauri.dainst.org/de/concepts/_fb2b810d.html",""),
		_09252694("_09252694","ground penetrating radar survey","Georadar","http://thesauri.dainst.org/de/concepts/_09252694.html",""),
		_ec54380e("_ec54380e","electromagnetic survey","Geomagnetik","http://thesauri.dainst.org/de/concepts/_ec54380e.html",""),
		_dddddddd(AbstractValueList.IGNORE_ID + "d","- soil examination -","- Bodenuntersuchungen -","",""),
		_34e4b949("_34e4b949","borehole survey","Bohrungen","http://thesauri.dainst.org/de/concepts/_34e4b949.html",""),
		_eeeeeeee(AbstractValueList.IGNORE_ID + "e","- building history research -","- Bauforschung -","",""),
		_669ddb20("_669ddb20","building survey","Architektonische Bauaufnahme","http://thesauri.dainst.org/de/concepts/_669ddb20.html",""),
		_39ae6301("_39ae6301","reconstruction","Rekonstruktionen","http://thesauri.dainst.org/de/concepts/_39ae6301.html",""),
		_ffffffff(AbstractValueList.IGNORE_ID + "f","- material examination (anorganic) -","- Materialuntersuchungen (anorganisch) -","",""),
		_05952178("_05952178","isotope analysis","Isotopenanalyse","http://thesauri.dainst.org/de/concepts/_05952178.html",""),
		_4e43a667("_4e43a667","thermoluminescence","Thermolumineszenz","http://thesauri.dainst.org/de/concepts/_4e43a667.html",""),
		_76b63098("_76b63098","thin section analysis","Dünnschliffuntersuchungen","http://thesauri.dainst.org/de/concepts/_76b63098.html",""),
		_gggggggg(AbstractValueList.IGNORE_ID + "g","- material examination (organic) -","- Materialuntersuchungen (organisch) -","",""),
		_1859492d("_1859492d","animal remains analysis","Tierrestanalyse","http://thesauri.dainst.org/de/concepts/_1859492d.html",""),
		_662bd15("_662bd15","human bone analysis","Menschenknochenanalyse","http://thesauri.dainst.org/de/concepts/_662bd15.html",""),
		_6c8262e5("_6c8262e5","macrobotanical analysis","Botanische Großrestanalyse","http://thesauri.dainst.org/de/concepts/_6c8262e5.html",""),
		_5dfee7d0("_5dfee7d0","poll analysis","Pollenanalyse","http://thesauri.dainst.org/de/concepts/_5dfee7d0.html",""),
		_be76d5f1("_be76d5f1","dendrochronological","Dendrochronologie","http://thesauri.dainst.org/de/concepts/_be76d5f1.html",""),
		_c9bca8ee("_c9bca8ee","radiocarbon dating","Radiokarbondatierung","http://thesauri.dainst.org/de/concepts/_c9bca8ee.html",""),
		_38af3256("_38af3256","DNA analysis","DNA-Analysen","http://thesauri.dainst.org/de/concepts/_38af3256.html",""),
		_hhhhhhhh(AbstractValueList.IGNORE_ID + "h","- survey and geodesy -","- Vermessung und Geodäsie -","",""),
		_7518c874("_7518c874","topographical survey","Topografische Aufnahme","http://thesauri.dainst.org/de/concepts/_7518c874.html",""),
		_iiiiiiii(AbstractValueList.IGNORE_ID + "i","- digital documentation -","- Digitale Dokumentation -","",""),
		_4127655a("_4127655a","laser scanning","Laserscanning","http://thesauri.dainst.org/de/concepts/_4127655a.html",""),
		_6f3228e7("_6f3228e7","structure from motion","Structure from motion","http://thesauri.dainst.org/de/concepts/_6f3228e7.html",""),
		_63364f59("_63364f59","RTI","RTI","http://thesauri.dainst.org/de/concepts/_63364f59.html",""),
		_b048af75("_b048af75","photogrammetry","Fotogrammetrie","http://thesauri.dainst.org/de/concepts/_b048af75.html",""),
		_8f5f2cd0("_8f5f2cd0","digital photography","Digital Fotografie","http://thesauri.dainst.org/de/concepts/_8f5f2cd0.html",""),
		_46100c83("_46100c83","digital graphical documentation","Digitale grafische Dokumentation","http://thesauri.dainst.org/de/concepts/_46100c83.html",""),
		_jjjjjjjj(AbstractValueList.IGNORE_ID + "j","- analog documentation -","- Analoge Dokumentation -","",""),
		_c7730639("_c7730639","analog photography","Analoge Fotografie","http://thesauri.dainst.org/de/concepts/_c7730639.html",""),
		_1c054458("_1c054458","analog graphical documentation","Analoge zeichnerische Dokumentation","http://thesauri.dainst.org/de/concepts/_1c054458.html",""),
		_kkkkkkkk(AbstractValueList.IGNORE_ID + "k","- spatial analysis -","- Räumliche Auswertungen -","",""),
		_ad19f182("_ad19f182","landscape archaeology","Landschaftsarchäologie","http://thesauri.dainst.org/de/concepts/_ad19f182.html",""),
		_e76be976("_e76be976","GIS analysis","GIS-Analysen","http://thesauri.dainst.org/de/concepts/_e76be976.html",""),
		_28a1769c("_28a1769c","topographical analysis","Topographische Untersuchung","http://thesauri.dainst.org/de/concepts/_28a1769c.html",""),
		_llllllll(AbstractValueList.IGNORE_ID + "l","- remote sensing -","- Fernerkundung -","",""),
		_98a244bb("_98a244bb","aerial photo analysis","Luftbildauswertung","http://thesauri.dainst.org/de/concepts/_98a244bb.html",""),
		_c3cc7247("_c3cc7247","LiDAR","LiDAR","http://thesauri.dainst.org/de/concepts/_c3cc7247.html",""),
		_4968e2b1("_4968e2b1","satellite scanning","Satellitenaufnahmen","http://thesauri.dainst.org/de/concepts/_4968e2b1.html",""),
		_mmmmmmmm(AbstractValueList.IGNORE_ID + "m","- experimental archaeology -","- Experimentelle Archäologie -","",""),
		_nnnnnnnn(AbstractValueList.IGNORE_ID + "n","- finds analysis -","- Fundanalyse -","",""),
		_7b53c80f("_7b53c80f","typology","Typologie","http://thesauri.dainst.org/de/concepts/_7b53c80f.html",""),
		_3d00f437("_3d00f437","functional analysis","Funktionsanalyse","http://thesauri.dainst.org/de/concepts/_3d00f437.html",""),
		_pppppppp(AbstractValueList.IGNORE_ID + "p","- arthistorical concepts -","- Kunsttheoretische Konzepte -","",""),
		_8f6d6a4a("_8f6d6a4a","critique of style","Stilkritik","http://thesauri.dainst.org/de/concepts/_8f6d6a4a.html",""),
		_e0dea232("_e0dea232","iconography","Ikonographie","http://thesauri.dainst.org/de/concepts/_e0dea232.html",""),
		_qqqqqqqq(AbstractValueList.IGNORE_ID + "q","- evaluation of texts -","- Auswertung von Texten -","",""),
		_c9372069("_c9372069","assessment of sources","Quellenkritik","http://thesauri.dainst.org/de/concepts/_c9372069.html",""),
		_archivres("_archivres","archival research","Archivrecherche","",""),
		_9f06301d("_9f06301d","bibliographical research","Literaturrecherche","http://thesauri.dainst.org/de/concepts/_9f06301d.html",""),
		_rrrrrrrr(AbstractValueList.IGNORE_ID + "r","- retro digitization -","- Retrodigitalisierung -","",""),
		_ssssssss(AbstractValueList.IGNORE_ID + "s","- data re-use -","- Datennachnutzung -","","")
		
		/*
		_6f2f1990("_6f2f1990","excavation","Ausgrabung","http://thesauri.dainst.org/de/concepts/_6f2f1990.html",""),
		_ec45464a("_ec45464a","fieldwalking survey","Oberflächenbegehung (Survey)","http://thesauri.dainst.org/de/concepts/_ec45464a.html",""),
		_3e16c140("_3e16c140","wet soil archaeology","Feuchtbodenarchäologie","http://thesauri.dainst.org/de/concepts/_3e16c140.html",""),
		_7c9cedaf("_7c9cedaf","underwater excavation","Unterwasserarchäologie","http://thesauri.dainst.org/de/concepts/_7c9cedaf.html",""),
		_c5a8310c("_c5a8310c","mining archaeology","Montanarchäologie","http://thesauri.dainst.org/de/concepts/_c5a8310c.html",""),
		_fb2b810d("_fb2b810d","electrical resistivity tomography","Geoelektrik","http://thesauri.dainst.org/de/concepts/_fb2b810d.html",""),
		_09252694("_09252694","ground penetrating radar survey","Georadar","http://thesauri.dainst.org/de/concepts/_09252694.html",""),
		_ec54380e("_ec54380e","electromagnetic survey","Geomagnetik","http://thesauri.dainst.org/de/concepts/_ec54380e.html",""),
		_34e4b949("_34e4b949","borehole survey","Bohrungen","http://thesauri.dainst.org/de/concepts/_34e4b949.html",""),
		_05952178("_05952178","isotope analysis","Isotopenanalyse","http://thesauri.dainst.org/de/concepts/_05952178.html",""),
		_4e43a667("_4e43a667","thermoluminescence","Thermolumineszenz","http://thesauri.dainst.org/de/concepts/_4e43a667.html",""),
		_76b63098("_76b63098","thin section analysis","Dünnschliffuntersuchungen","http://thesauri.dainst.org/de/concepts/_76b63098.html",""),
		_1859492d("_1859492d","animal remains analysis","Tierrestanalyse","http://thesauri.dainst.org/de/concepts/_1859492d.html",""),
		_662bd15("_662bd15","human bone analysis","Menschenknochenanalyse","http://thesauri.dainst.org/de/concepts/_662bd15.html",""),
		_6c8262e5("_6c8262e5","macrobotanical analysis","Botanische Großrestanalyse","http://thesauri.dainst.org/de/concepts/_6c8262e5.html",""),
		_5dfee7d0("_5dfee7d0","poll analysis","Pollenanalyse","http://thesauri.dainst.org/de/concepts/_5dfee7d0.html",""),
		_be76d5f1("_be76d5f1","dendrochronological","Dendrochronologie","http://thesauri.dainst.org/de/concepts/_be76d5f1.html",""),
		_c9bca8ee("_c9bca8ee","radiocarbon dating","Radiokarbondatierung","http://thesauri.dainst.org/de/concepts/_c9bca8ee.html",""),
		_38af3256("_38af3256","DNA analysis","DNA-Analysen","http://thesauri.dainst.org/de/concepts/_38af3256.html",""),
		_7518c874("_7518c874","topographical survey","Topografische Aufnahme","http://thesauri.dainst.org/de/concepts/_7518c874.html",""),
		_4127655a("_4127655a","laser scanning","Laserscanning","http://thesauri.dainst.org/de/concepts/_4127655a.html",""),
		_6f3228e7("_6f3228e7","structure from motion","Structure from motion","http://thesauri.dainst.org/de/concepts/_6f3228e7.html",""),
		_63364f59("_63364f59","RTI","RTI","http://thesauri.dainst.org/de/concepts/_63364f59.html",""),
		_b048af75("_b048af75","photogrammetry","Fotogrammetrie","http://thesauri.dainst.org/de/concepts/_b048af75.html",""),
		_8f5f2cd0("_8f5f2cd0","digital photography","Digital Fotografie","http://thesauri.dainst.org/de/concepts/_8f5f2cd0.html",""),
		_46100c83("_46100c83","digital graphical documentation","Digitale grafische Dokumentation","http://thesauri.dainst.org/de/concepts/_46100c83.html",""),
		_c7730639("_c7730639","analog photography","Analoge Fotografie","http://thesauri.dainst.org/de/concepts/_c7730639.html",""),
		_1c054458("_1c054458","analog graphical documentation","Analoge zeichnerische Dokumentation","http://thesauri.dainst.org/de/concepts/_1c054458.html",""),
		_ad19f182("_ad19f182","landscape archaeology","Landschaftsarchäologie","http://thesauri.dainst.org/de/concepts/_ad19f182.html",""),
		_e76be976("_e76be976","GIS analysis","GIS-Analysen","http://thesauri.dainst.org/de/concepts/_e76be976.html",""),
		_28a1769c("_28a1769c","topographical analysis","Topographische Untersuchung","http://thesauri.dainst.org/de/concepts/_28a1769c.html",""),
		_98a244bb("_98a244bb","aerial photo analysis","Luftbildauswertung","http://thesauri.dainst.org/de/concepts/_98a244bb.html",""),
		_c3cc7247("_c3cc7247","LiDAR","LiDAR","http://thesauri.dainst.org/de/concepts/_c3cc7247.html",""),
		_4968e2b1("_4968e2b1","satellite scanning","Satellitenaufnahmen","http://thesauri.dainst.org/de/concepts/_4968e2b1.html",""),
		_7b53c80f("_7b53c80f","typology","Typologie","http://thesauri.dainst.org/de/concepts/_7b53c80f.html",""),
		_3d00f437("_3d00f437","functional analysis","Funktionsanalyse","http://thesauri.dainst.org/de/concepts/_3d00f437.html",""),
		_8f6d6a4a("_8f6d6a4a","critique of style","Stilkritik","http://thesauri.dainst.org/de/concepts/_8f6d6a4a.html",""),
		_e0dea232("_e0dea232","iconography","Ikonographie","http://thesauri.dainst.org/de/concepts/_e0dea232.html","")
		*/
		;
	
		public String id;
		public String labelEng;
		public String labelGer;
		public String url;
		public String description;
		
		private SubMethod(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
			this.description = description;
		}
		
		public static String getLabelEng(String valueId){
			for(SubMethod type : SubMethod.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(SubMethod type : SubMethod.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
		
		public static ElementOfList getEolById(String id){
			for(SubMethod type : SubMethod.values()){
				if(StringUtils.equals(id, type.id)){
					return new ElementOfList(LIST_ID_SUB, type.id, type.labelGer, type.url);
				}
			}
			return null;
		}
	}
}
