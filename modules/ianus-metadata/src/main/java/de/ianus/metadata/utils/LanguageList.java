package de.ianus.metadata.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Based on ISO 639-3 (Languages)
 * @author jurzua
 *
 */
public class LanguageList {
	
	private static final List<ISOLanguage> list = new ArrayList<ISOLanguage>();
	
	static{
		
		
		list.add(new ISOLanguage("abk","Abkhazian","Abchasisch"));
		list.add(new ISOLanguage("ace","Achinese","Aceh-Sprache"));
		list.add(new ISOLanguage("ach","Acoli","Acholi-Sprache"));
		list.add(new ISOLanguage("ada","Adangme","Adangme-Sprache"));
		list.add(new ISOLanguage("ady","Adyghe; Adygei","Adygisch"));
		list.add(new ISOLanguage("afh","Afrihili","Afrihili"));
		list.add(new ISOLanguage("afr","Afrikaans","Afrikaans"));
		list.add(new ISOLanguage("egy","Egyptian (Ancient)","Ägyptisch"));
		list.add(new ISOLanguage("ain","Ainu","Ainu-Sprache"));
		list.add(new ISOLanguage("aka","Akan","Akan-Sprache"));
		list.add(new ISOLanguage("akk","Akkadian","Akkadisch"));
		list.add(new ISOLanguage("sqi","Albanian","Albanisch"));
		list.add(new ISOLanguage("ale","Aleut","Aleutisch"));
		list.add(new ISOLanguage("alg","Algonquian languages","Algonkin-Sprachen (Andere)"));
		list.add(new ISOLanguage("nwc","Classical Newari; Old Newari; Classical Nepal Bhasa","Alt-Newari"));
		list.add(new ISOLanguage("alt","Southern Altai","Altaisch"));
		list.add(new ISOLanguage("tut","Altaic languages","Altaische Sprachen (Andere)"));
		list.add(new ISOLanguage("gez","Geez","Altäthiopisch"));
		list.add(new ISOLanguage("ang","English, Old (ca.450-1100)","Altenglisch"));
		list.add(new ISOLanguage("fro","French, Old (842-ca.1400)","Altfranzösisch"));
		list.add(new ISOLanguage("goh","German, Old High (ca.750-1050)","Althochdeutsch"));
		list.add(new ISOLanguage("sga","Irish, Old (to 900)","Altirisch"));
		list.add(new ISOLanguage("non","Norse, Old","Altnorwegisch"));
		list.add(new ISOLanguage("pro","Provençal, Old (to 1500);Occitan, Old (to 1500)","Altokzitanisch"));
		list.add(new ISOLanguage("peo","Persian, Old (ca.600-400 B.C.)","Altpersisch"));
		list.add(new ISOLanguage("amh","Amharic","Amharisch"));
		list.add(new ISOLanguage("anp","Angika","Anga-Sprache"));
		list.add(new ISOLanguage("apa","Apache languages","Apachen-Sprachen"));
		list.add(new ISOLanguage("ara","Arabic","Arabisch"));
		list.add(new ISOLanguage("arg","Aragonese","Aragonesisch"));
		list.add(new ISOLanguage("arc","Official Aramaic (700-300 BCE); Imperial Aramaic (700-300 BCE)","Aramäisch"));
		list.add(new ISOLanguage("arp","Arapaho","Arapaho-Sprache"));
		list.add(new ISOLanguage("arn","Mapudungun; Mapuche","Arauka-Sprachen"));
		list.add(new ISOLanguage("arw","Arawak","Arawak-Sprachen"));
		list.add(new ISOLanguage("hye","Armenian","Armenisch"));
		list.add(new ISOLanguage("rup","Aromanian; Arumanian; Macedo-Romanian","Aromunisch"));
		list.add(new ISOLanguage("aze","Azerbaijani","Aserbeidschanisch"));
		list.add(new ISOLanguage("asm","Assamese","Assamesisch"));
		list.add(new ISOLanguage("ast","Asturian; Bable; Leonese; Asturleonese","Asturisch"));
		list.add(new ISOLanguage("ath","Athapascan languages","Athapaskische Sprachen (Andere)"));
		list.add(new ISOLanguage("aus","Australian languages","Australische Sprachen"));
		list.add(new ISOLanguage("map","Austronesian languages","Austronesische Sprachen (Andere)"));
		list.add(new ISOLanguage("ave","Avestan","Avestisch"));
		list.add(new ISOLanguage("awa","Awadhi","Awadhi"));
		list.add(new ISOLanguage("ava","Avaric","Awarisch"));
		list.add(new ISOLanguage("aym","Aymara","Aymará-Sprache"));
		list.add(new ISOLanguage("ind","Indonesian","Bahasa Indonesia"));
		list.add(new ISOLanguage("ban","Balinese","Balinesisch"));
		list.add(new ISOLanguage("bat","Baltic languages","Baltische Sprachen (Andere)"));
		list.add(new ISOLanguage("bam","Bambara","Bambara-Sprache"));
		list.add(new ISOLanguage("bai","Bamileke languages","Bamileke-Sprachen"));
		list.add(new ISOLanguage("bad","Banda languages","Banda-Sprachen (Ubangi-Sprachen)"));
		list.add(new ISOLanguage("bnt","Bantu languages","Bantusprachen (Andere)"));
		list.add(new ISOLanguage("bas","Basa","Basaa-Sprache"));
		list.add(new ISOLanguage("bak","Bashkir","Baschkirisch"));
		list.add(new ISOLanguage("eus","Basque","Baskisch"));
		list.add(new ISOLanguage("btk","Batak languages","Batak-Sprache"));
		list.add(new ISOLanguage("bis","Bislama","Beach-la-mar"));
		list.add(new ISOLanguage("bej","Beja; Bedawiyet","Bedauye"));
		list.add(new ISOLanguage("bal","Baluchi","Belutschisch"));
		list.add(new ISOLanguage("bem","Bemba","Bemba-Sprache"));
		list.add(new ISOLanguage("ben","Bengali","Bengali"));
		list.add(new ISOLanguage("ber","Berber languages","Berbersprachen (Andere)"));
		list.add(new ISOLanguage("bho","Bhojpuri","Bhojpuri"));
		list.add(new ISOLanguage("bih","Bihari languages","Bihari (Andere)"));
		list.add(new ISOLanguage("bik","Bikol","Bikol-Sprache"));
		list.add(new ISOLanguage("byn","Blin; Bilin","Bilin-Sprache"));
		list.add(new ISOLanguage("mya","Burmese","Birmanisch"));
		list.add(new ISOLanguage("bla","Siksika","Blackfoot-Sprache"));
		list.add(new ISOLanguage("zbl","Blissymbols; Blissymbolics; Bliss","Bliss-Symbol"));
		list.add(new ISOLanguage("nob","Bokmål, Norwegian; Norwegian Bokmål","Bokmål"));
		list.add(new ISOLanguage("bos","Bosnian","Bosnisch"));
		list.add(new ISOLanguage("bra","Braj","Braj-Bhakha"));
		list.add(new ISOLanguage("bre","Breton","Bretonisch"));
		list.add(new ISOLanguage("bug","Buginese","Bugi-Sprache"));
		list.add(new ISOLanguage("bul","Bulgarian","Bulgarisch"));
		list.add(new ISOLanguage("bua","Buriat","Burjatisch"));
		list.add(new ISOLanguage("cad","Caddo","Caddo-Sprachen"));
		list.add(new ISOLanguage("ceb","Cebuano","Cebuano"));
		list.add(new ISOLanguage("cmc","Chamic languages","Cham-Sprachen"));
		list.add(new ISOLanguage("cha","Chamorro","Chamorro-Sprache"));
		list.add(new ISOLanguage("chr","Cherokee","Cherokee-Sprache"));
		list.add(new ISOLanguage("chy","Cheyenne","Cheyenne-Sprache"));
		list.add(new ISOLanguage("chb","Chibcha","Chibcha-Sprachen"));
		list.add(new ISOLanguage("zho","Chinese","Chinesisch"));
		list.add(new ISOLanguage("chn","Chinook jargon","Chinook-Jargon"));
		list.add(new ISOLanguage("chp","Chipewyan; Dene Suline","Chipewyan-Sprache"));
		list.add(new ISOLanguage("cho","Choctaw","Choctaw-Sprache"));
		list.add(new ISOLanguage("cre","Cree","Cree-Sprache"));
		list.add(new ISOLanguage("day","Land Dayak languages","Dajakisch"));
		list.add(new ISOLanguage("dak","Dakota","Dakota-Sprache"));
		list.add(new ISOLanguage("aar","Afar","Danakil-Sprache"));
		list.add(new ISOLanguage("dan","Danish","Dänisch"));
		list.add(new ISOLanguage("dar","Dargwa","Darginisch"));
		list.add(new ISOLanguage("del","Delaware","Delaware-Sprache"));
		list.add(new ISOLanguage("deu","German","Deutsch"));
		list.add(new ISOLanguage("din","Dinka","Dinka-Sprache"));
		list.add(new ISOLanguage("doi","Dogri","Dogri"));
		list.add(new ISOLanguage("dgr","Dogrib","Dogrib-Sprache"));
		list.add(new ISOLanguage("dra","Dravidian languages","Drawidische Sprachen (Andere)"));
		list.add(new ISOLanguage("dua","Duala","Duala-Sprachen"));
		list.add(new ISOLanguage("dyu","Dyula","Dyula-Sprache"));
		list.add(new ISOLanguage("dzo","Dzongkha","Dzongkha"));
		list.add(new ISOLanguage("bin","Bini; Edo","Edo-Sprache"));
		list.add(new ISOLanguage("efi","Efik","Efik"));
		list.add(new ISOLanguage("mis","Uncoded languages","Einzelne andere Sprachen"));
		list.add(new ISOLanguage("eka","Ekajuk","Ekajuk"));
		list.add(new ISOLanguage("elx","Elamite","Elamisch"));
		list.add(new ISOLanguage("tvl","Tuvalu","Elliceanisch"));
		list.add(new ISOLanguage("eng","English","Englisch"));
		list.add(new ISOLanguage("myv","Erzya","Erza-Mordwinisch"));
		list.add(new ISOLanguage("epo","Esperanto","Esperanto"));
		list.add(new ISOLanguage("est","Estonian","Estnisch"));
		list.add(new ISOLanguage("ewe","Ewe","Ewe-Sprache"));
		list.add(new ISOLanguage("ewo","Ewondo","Ewondo"));
		list.add(new ISOLanguage("fat","Fanti","Fante-Sprache"));
		list.add(new ISOLanguage("fao","Faroese","Färöisch"));
		list.add(new ISOLanguage("fij","Fijian","Fidschi-Sprache"));
		list.add(new ISOLanguage("fin","Finnish","Finnisch"));
		list.add(new ISOLanguage("fiu","Finno-Ugrian languages","Finnougrische Sprachen (Andere)"));
		list.add(new ISOLanguage("fon","Fon","Fon-Sprache"));
		list.add(new ISOLanguage("fra","French","Französisch"));
		list.add(new ISOLanguage("fry","Western Frisian","Friesisch"));
		list.add(new ISOLanguage("fur","Friulian","Friulisch"));
		list.add(new ISOLanguage("ful","Fulah","Ful"));
		list.add(new ISOLanguage("gaa","Ga","Ga-Sprache"));
		list.add(new ISOLanguage("glg","Galician","Galicisch"));
		list.add(new ISOLanguage("gla","Gaelic; Scottish Gaelic","Gälisch-Schottisch"));
		list.add(new ISOLanguage("orm","Oromo","Galla-Sprache"));
		list.add(new ISOLanguage("lug","Ganda","Ganda-Sprache"));
		list.add(new ISOLanguage("gay","Gayo","Gayo-Sprache"));
		list.add(new ISOLanguage("gba","Gbaya","Gbaya-Sprache"));
		list.add(new ISOLanguage("kat","Georgian","Georgisch"));
		list.add(new ISOLanguage("gem","Germanic languages","Germanische Sprachen (Andere)"));
		list.add(new ISOLanguage("gil","Gilbertese","Gilbertesisch"));
		list.add(new ISOLanguage("gon","Gondi","Gondi-Sprache"));
		list.add(new ISOLanguage("gor","Gorontalo","Gorontalesisch"));
		list.add(new ISOLanguage("got","Gothic","Gotisch"));
		list.add(new ISOLanguage("grb","Grebo","Grebo-Sprache"));
		list.add(new ISOLanguage("grc","Greek, Ancient (to 1453)","Griechisch"));
		list.add(new ISOLanguage("kal","Kalaallisut; Greenlandic","Grönländisch"));
		list.add(new ISOLanguage("grn","Guarani","Guaraní-Sprache"));
		list.add(new ISOLanguage("guj","Gujarati","Gujarati-Sprache"));
		list.add(new ISOLanguage("hai","Haida","Haida-Sprache"));
		list.add(new ISOLanguage("hat","Haitian; Haitian Creole","Haïtien (Haiti-Kreolisch)"));
		list.add(new ISOLanguage("afa","Afro-Asiatic languages","Hamitosemitische Sprachen (Andere)"));
		list.add(new ISOLanguage("hau","Hausa","Haussa-Sprache"));
		list.add(new ISOLanguage("haw","Hawaiian","Hawaiisch"));
		list.add(new ISOLanguage("heb","Hebrew","Hebräisch"));
		list.add(new ISOLanguage("her","Herero","Herero-Sprache"));
		list.add(new ISOLanguage("hit","Hittite","Hethitisch"));
		list.add(new ISOLanguage("hil","Hiligaynon","Hiligaynon-Sprache"));
		list.add(new ISOLanguage("him","Himachali languages; Western Pahari languages","Himachali"));
		list.add(new ISOLanguage("hin","Hindi","Hindi"));
		list.add(new ISOLanguage("hmo","Hiri Motu","Hiri-Motu"));
		list.add(new ISOLanguage("hup","Hupa","Hupa-Sprache"));
		list.add(new ISOLanguage("iba","Iban","Iban-Sprache"));
		list.add(new ISOLanguage("ibo","Igbo","Ibo-Sprache"));
		list.add(new ISOLanguage("ido","Ido","Ido"));
		list.add(new ISOLanguage("ijo","Ijo languages","Ijo-Sprache"));
		list.add(new ISOLanguage("ilo","Iloko","Ilokano-Sprache"));
		list.add(new ISOLanguage("smn","Inari Sami","Inarisaamisch"));
		list.add(new ISOLanguage("nai","North American Indian languages","Indianersprachen, Nordamerika (Andere)"));
		list.add(new ISOLanguage("sai","South American Indian languages","Indianersprachen, Südamerika (Andere)"));
		list.add(new ISOLanguage("cai","Central American Indian languages","Indianersprachen, Zentralamerika (Andere)"));
		list.add(new ISOLanguage("inc","Indic languages","Indoarische Sprachen (Andere)"));
		list.add(new ISOLanguage("ine","Indo-European languages","Indogermanische Sprachen (Andere)"));
		list.add(new ISOLanguage("inh","Ingush","Inguschisch"));
		list.add(new ISOLanguage("ina","Interlingua (International Auxiliary Language Association)","Interlingua"));
		list.add(new ISOLanguage("ile","Interlingue; Occidental","Interlingue"));
		list.add(new ISOLanguage("iku","Inuktitut","Inuktitut"));
		list.add(new ISOLanguage("ipk","Inupiaq","Inupik"));
		list.add(new ISOLanguage("ira","Iranian languages","Iranische Sprachen (Andere)"));
		list.add(new ISOLanguage("gle","Irish","Irisch"));
		list.add(new ISOLanguage("iro","Iroquoian languages","Irokesische Sprachen"));
		list.add(new ISOLanguage("isl","Icelandic","Isländisch"));
		list.add(new ISOLanguage("ita","Italian","Italienisch"));
		list.add(new ISOLanguage("sah","Yakut","Jakutisch"));
		list.add(new ISOLanguage("jpn","Japanese","Japanisch"));
		list.add(new ISOLanguage("jav","Javanese","Javanisch"));
		list.add(new ISOLanguage("yid","Yiddish","Jiddisch"));
		list.add(new ISOLanguage("lad","Ladino","Judenspanisch"));
		list.add(new ISOLanguage("jrb","Judeo-Arabic","Jüdisch-Arabisch"));
		list.add(new ISOLanguage("jpr","Judeo-Persian","Jüdisch-Persisch"));
		list.add(new ISOLanguage("kbd","Kabardian","Kabardinisch"));
		list.add(new ISOLanguage("kab","Kabyle","Kabylisch"));
		list.add(new ISOLanguage("kac","Kachin; Jingpho","Kachin-Sprache"));
		list.add(new ISOLanguage("xal","Kalmyk; Oirat","Kalmückisch"));
		list.add(new ISOLanguage("kam","Kamba","Kamba-Sprache"));
		list.add(new ISOLanguage("khm","Central Khmer","Kambodschanisch"));
		list.add(new ISOLanguage("kan","Kannada","Kannada"));
		list.add(new ISOLanguage("kau","Kanuri","Kanuri-Sprache"));
		list.add(new ISOLanguage("kaa","Kara-Kalpak","Karakalpakisch"));
		list.add(new ISOLanguage("krc","Karachay-Balkar","Karatschaiisch-Balkarisch"));
		list.add(new ISOLanguage("krl","Karelian","Karelisch"));
		list.add(new ISOLanguage("kar","Karen languages","Karenisch"));
		list.add(new ISOLanguage("car","Galibi Carib","Karibische Sprachen"));
		list.add(new ISOLanguage("kaz","Kazakh","Kasachisch"));
		list.add(new ISOLanguage("kas","Kashmiri","Kaschmiri"));
		list.add(new ISOLanguage("csb","Kashubian","Kaschubisch"));
		list.add(new ISOLanguage("cat","Catalan; Valencian","Katalanisch"));
		list.add(new ISOLanguage("cau","Caucasian languages","Kaukasische Sprachen (Andere)"));
		list.add(new ISOLanguage("kaw","Kawi","Kawi"));
		list.add(new ISOLanguage("zxx","No linguistic content; Not applicable","Kein linguistischer Inhalt"));
		list.add(new ISOLanguage("cel","Celtic languages","Keltische Sprachen (Andere)"));
		list.add(new ISOLanguage("kha","Khasi","Khasi-Sprache"));
		list.add(new ISOLanguage("khi","Khoisan languages","Khoisan-Sprachen (Andere)"));
		list.add(new ISOLanguage("mag","Magahi","Khotta"));
		list.add(new ISOLanguage("kik","Kikuyu; Gikuyu","Kikuyu-Sprache"));
		list.add(new ISOLanguage("kmb","Kimbundu","Kimbundu-Sprache"));
		list.add(new ISOLanguage("chu","Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic","Kirchenslawisch"));
		list.add(new ISOLanguage("kir","Kirghiz; Kyrgyz","Kirgisisch"));
		list.add(new ISOLanguage("tlh","Klingon; tlhIngan-Hol","Klingonisch"));
		list.add(new ISOLanguage("kom","Komi","Komi-Sprache"));
		list.add(new ISOLanguage("kon","Kongo","Kongo-Sprache"));
		list.add(new ISOLanguage("kok","Konkani","Konkani"));
		list.add(new ISOLanguage("cop","Coptic","Koptisch"));
		list.add(new ISOLanguage("kor","Korean","Koreanisch"));
		list.add(new ISOLanguage("cor","Cornish","Kornisch"));
		list.add(new ISOLanguage("cos","Corsican","Korsisch"));
		list.add(new ISOLanguage("kos","Kosraean","Kosraeanisch"));
		list.add(new ISOLanguage("kpe","Kpelle","Kpelle-Sprache"));
		list.add(new ISOLanguage("cpe","Creoles and pidgins, English based","Kreolisch-Englisch (Andere)"));
		list.add(new ISOLanguage("cpf","Creoles and pidgins, French-based","Kreolisch-Französisch (Andere)"));
		list.add(new ISOLanguage("cpp","Creoles and pidgins, Portuguese-based","Kreolisch-Portugiesisch (Andere)"));
		list.add(new ISOLanguage("crp","Creoles and pidgins","Kreolische Sprachen; Pidginsprachen (Andere)"));
		list.add(new ISOLanguage("crh","Crimean Tatar; Crimean Turkish","Krimtatarisch"));
		list.add(new ISOLanguage("hrv","Croatian","Kroatisch"));
		list.add(new ISOLanguage("kro","Kru languages","Kru-Sprachen (Andere)"));
		list.add(new ISOLanguage("kum","Kumyk","Kumükisch"));
		list.add(new ISOLanguage("art","Artificial languages","Kunstsprachen (Andere)"));
		list.add(new ISOLanguage("kur","Kurdish","Kurdisch"));
		list.add(new ISOLanguage("cus","Cushitic languages","Kuschitische Sprachen (Andere)"));
		list.add(new ISOLanguage("gwi","Gwich'in","Kutchin-Sprache"));
		list.add(new ISOLanguage("kut","Kutenai","Kutenai-Sprache"));
		list.add(new ISOLanguage("kua","Kuanyama; Kwanyama","Kwanyama-Sprache"));
		list.add(new ISOLanguage("cym","Welsh","Kymrisch"));
		list.add(new ISOLanguage("lah","Lahnda","Lahnda"));
		list.add(new ISOLanguage("iii","Sichuan Yi; Nuosu","Lalo-Sprache"));
		list.add(new ISOLanguage("lam","Lamba","Lamba-Sprache (Bantusprache)"));
		list.add(new ISOLanguage("lao","Lao","Laotisch"));
		list.add(new ISOLanguage("lat","Latin","Latein"));
		list.add(new ISOLanguage("lez","Lezghian","Lesgisch"));
		list.add(new ISOLanguage("lav","Latvian","Lettisch"));
		list.add(new ISOLanguage("lim","Limburgan; Limburger; Limburgish","Limburgisch"));
		list.add(new ISOLanguage("lin","Lingala","Lingala"));
		list.add(new ISOLanguage("lit","Lithuanian","Litauisch"));
		list.add(new ISOLanguage("jbo","Lojban","Lojban"));
		list.add(new ISOLanguage("lub","Luba-Katanga","Luba-Katanga-Sprache"));
		list.add(new ISOLanguage("lui","Luiseno","Luiseño-Sprache"));
		list.add(new ISOLanguage("smj","Lule Sami","Lulesaamisch"));
		list.add(new ISOLanguage("lua","Luba-Lulua","Lulua-Sprache"));
		list.add(new ISOLanguage("lun","Lunda","Lunda-Sprache"));
		list.add(new ISOLanguage("luo","Luo (Kenya and Tanzania)","Luo-Sprache"));
		list.add(new ISOLanguage("lus","Lushai","Lushai-Sprache"));
		list.add(new ISOLanguage("ltz","Luxembourgish; Letzeburgesch","Luxemburgisch"));
		list.add(new ISOLanguage("mad","Madurese","Maduresisch"));
		list.add(new ISOLanguage("mai","Maithili","Maithili"));
		list.add(new ISOLanguage("mak","Makasar","Makassarisch"));
		list.add(new ISOLanguage("mkd","Macedonian","Makedonisch"));
		list.add(new ISOLanguage("mlg","Malagasy","Malagassi-Sprache"));
		list.add(new ISOLanguage("msa","Malay","Malaiisch"));
		list.add(new ISOLanguage("mal","Malayalam","Malayalam"));
		list.add(new ISOLanguage("div","Divehi; Dhivehi; Maldivian","Maledivisch"));
		list.add(new ISOLanguage("man","Mandingo","Malinke-Sprache"));
		list.add(new ISOLanguage("mlt","Maltese","Maltesisch"));
		list.add(new ISOLanguage("mdr","Mandar","Mandaresisch"));
		list.add(new ISOLanguage("mnc","Manchu","Mandschurisch"));
		list.add(new ISOLanguage("mno","Manobo languages","Manobo-Sprachen"));
		list.add(new ISOLanguage("glv","Manx","Manx"));
		list.add(new ISOLanguage("mri","Maori","Maori-Sprache"));
		list.add(new ISOLanguage("mar","Marathi","Marathi"));
		list.add(new ISOLanguage("mah","Marshallese","Marschallesisch"));
		list.add(new ISOLanguage("mwr","Marwari","Marwari"));
		list.add(new ISOLanguage("mas","Masai","Massai-Sprache"));
		list.add(new ISOLanguage("myn","Mayan languages","Maya-Sprachen"));
		list.add(new ISOLanguage("umb","Umbundu","Mbundu-Sprache"));
		list.add(new ISOLanguage("mul","Multiple languages","Mehrere Sprachen"));
		list.add(new ISOLanguage("mni","Manipuri","Meithei-Sprache"));
		list.add(new ISOLanguage("men","Mende","Mende-Sprache"));
		list.add(new ISOLanguage("hmn","Hmong; Mong","Miao-Sprachen"));
		list.add(new ISOLanguage("mic","Mi'kmaq; Micmac","Micmac-Sprache"));
		list.add(new ISOLanguage("min","Minangkabau","Minangkabau-Sprache"));
		list.add(new ISOLanguage("mwl","Mirandese","Mirandesisch"));
		list.add(new ISOLanguage("enm","English, Middle (1100-1500)","Mittelenglisch"));
		list.add(new ISOLanguage("frm","French, Middle (ca.1400-1600)","Mittelfranzösisch"));
		list.add(new ISOLanguage("gmh","German, Middle High (ca.1050-1500)","Mittelhochdeutsch"));
		list.add(new ISOLanguage("mga","Irish, Middle (900-1200)","Mittelirisch"));
		list.add(new ISOLanguage("dum","Dutch, Middle (ca.1050-1350)","Mittelniederländisch"));
		list.add(new ISOLanguage("pal","Pahlavi","Mittelpersisch"));
		list.add(new ISOLanguage("moh","Mohawk","Mohawk-Sprache"));
		list.add(new ISOLanguage("mdf","Moksha","Mokscha-Sprache"));
		list.add(new ISOLanguage("mkh","Mon-Khmer languages","Mon-Khmer-Sprachen (Andere)"));
		list.add(new ISOLanguage("lol","Mongo","Mongo-Sprache"));
		list.add(new ISOLanguage("mon","Mongolian","Mongolisch"));
		list.add(new ISOLanguage("mos","Mossi","Mossi-Sprache"));
		list.add(new ISOLanguage("mun","Munda languages","Mundasprachen (Andere)"));
		list.add(new ISOLanguage("mus","Creek","Muskogisch"));
		list.add(new ISOLanguage("nqo","N'Ko","N'Ko"));
		list.add(new ISOLanguage("nah","Nahuatl languages","Nahuatl"));
		list.add(new ISOLanguage("nau","Nauru","Nauruanisch"));
		list.add(new ISOLanguage("nav","Navajo; Navaho","Navajo-Sprache"));
		list.add(new ISOLanguage("nde","Ndebele, North; North Ndebele","Ndebele-Sprache (Simbabwe)"));
		list.add(new ISOLanguage("nbl","Ndebele, South; South Ndebele","Ndebele-Sprache (Transvaal)"));
		list.add(new ISOLanguage("ndo","Ndonga","Ndonga"));
		list.add(new ISOLanguage("nap","Neapolitan","Neapel / Mundart"));
		list.add(new ISOLanguage("nep","Nepali","Nepali"));
		list.add(new ISOLanguage("ell","Greek, Modern (1453-)","Neugriechisch"));
		list.add(new ISOLanguage("tpi","Tok Pisin","Neumelanesisch"));
		list.add(new ISOLanguage("syr","Syriac","Neuostaramäisch"));
		list.add(new ISOLanguage("new","Nepal Bhasa; Newari","Newari"));
		list.add(new ISOLanguage("nia","Nias","Nias-Sprache"));
		list.add(new ISOLanguage("und","Undetermined","Nicht zu entscheiden"));
		list.add(new ISOLanguage("nds","Low German; Low Saxon; German, Low; Saxon, Low","Niederdeutsch"));
		list.add(new ISOLanguage("nld","Dutch; Flemish","Niederländisch"));
		list.add(new ISOLanguage("dsb","Lower Sorbian","Niedersorbisch"));
		list.add(new ISOLanguage("nic","Niger-Kordofanian languages","Nigerkordofanische Sprachen (Andere)"));
		list.add(new ISOLanguage("ssa","Nilo-Saharan languages","Nilosaharanische Sprachen (Andere)"));
		list.add(new ISOLanguage("niu","Niuean","Niue-Sprache"));
		list.add(new ISOLanguage("nyn","Nyankole","Nkole-Sprache"));
		list.add(new ISOLanguage("nog","Nogai","Nogaisch"));
		list.add(new ISOLanguage("frr","Northern Frisian","Nordfriesisch"));
		list.add(new ISOLanguage("sme","Northern Sami","Nordsaamisch"));
		list.add(new ISOLanguage("nor","Norwegian","Norwegisch"));
		list.add(new ISOLanguage("nub","Nubian languages","Nubische Sprachen"));
		list.add(new ISOLanguage("nym","Nyamwezi","Nyamwezi-Sprache"));
		list.add(new ISOLanguage("nya","Chichewa; Chewa; Nyanja","Nyanja-Sprache"));
		list.add(new ISOLanguage("nno","Norwegian Nynorsk; Nynorsk, Norwegian","Nynorsk"));
		list.add(new ISOLanguage("nyo","Nyoro","Nyoro-Sprache"));
		list.add(new ISOLanguage("nzi","Nzima","Nzima-Sprache"));
		list.add(new ISOLanguage("hsb","Upper Sorbian","Obersorbisch"));
		list.add(new ISOLanguage("oji","Ojibwa","Ojibwa-Sprache"));
		list.add(new ISOLanguage("oci","Occitan (post 1500)","Okzitanisch"));
		list.add(new ISOLanguage("kru","Kurukh","Oraon-Sprache"));
		list.add(new ISOLanguage("ori","Oriya","Oriya-Sprache"));
		list.add(new ISOLanguage("osa","Osage","Osage-Sprache"));
		list.add(new ISOLanguage("ota","Turkish, Ottoman (1500-1928)","Osmanisch"));
		list.add(new ISOLanguage("oss","Ossetian; Ossetic","Ossetisch"));
		list.add(new ISOLanguage("rap","Rapanui","Osterinsel-Sprache"));
		list.add(new ISOLanguage("frs","Eastern Frisian","Ostfriesisch"));
		list.add(new ISOLanguage("oto","Otomian languages","Otomangue-Sprachen"));
		list.add(new ISOLanguage("pau","Palauan","Palau-Sprache"));
		list.add(new ISOLanguage("pli","Pali","Pali"));
		list.add(new ISOLanguage("pam","Pampanga; Kapampangan","Pampanggan-Sprache"));
		list.add(new ISOLanguage("pan","Panjabi; Punjabi","Pandschabi-Sprache"));
		list.add(new ISOLanguage("pag","Pangasinan","Pangasinan-Sprache"));
		list.add(new ISOLanguage("fan","Fang","Pangwe-Sprache"));
		list.add(new ISOLanguage("pap","Papiamento","Papiamento"));
		list.add(new ISOLanguage("paa","Papuan languages","Papuasprachen (Andere)"));
		list.add(new ISOLanguage("pus","Pushto; Pashto","Paschtu"));
		list.add(new ISOLanguage("nso","Pedi; Sepedi; Northern Sotho","Pedi-Sprache"));
		list.add(new ISOLanguage("fas","Persian","Persisch"));
		list.add(new ISOLanguage("phi","Philippine languages","Philippinisch-Austronesisch (Andere)"));
		list.add(new ISOLanguage("phn","Phoenician","Phönikisch"));
		list.add(new ISOLanguage("fil","Filipino; Pilipino","Pilipino"));
		list.add(new ISOLanguage("pol","Polish","Polnisch"));
		list.add(new ISOLanguage("pon","Pohnpeian","Ponapeanisch"));
		list.add(new ISOLanguage("por","Portuguese","Portugiesisch"));
		list.add(new ISOLanguage("pra","Prakrit languages","Prakrit"));
		list.add(new ISOLanguage("que","Quechua","Quechua-Sprache"));
		list.add(new ISOLanguage("raj","Rajasthani","Rajasthani"));
		list.add(new ISOLanguage("rar","Rarotongan; Cook Islands Maori","Rarotonganisch"));
		list.add(new ISOLanguage("roh","Romansh","Rätoromanisch"));
		list.add(new ISOLanguage("qaa-qtz","Reserved for local use","Reserviert für lokale Verwendung"));
		list.add(new ISOLanguage("rom","Romany","Romani (Sprache)"));
		list.add(new ISOLanguage("roa","Romance languages","Romanische Sprachen (Andere)"));
		list.add(new ISOLanguage("loz","Lozi","Rotse-Sprache"));
		list.add(new ISOLanguage("ron","Romanian; Moldavian; Moldovan","Rumänisch"));
		list.add(new ISOLanguage("run","Rundi","Rundi-Sprache"));
		list.add(new ISOLanguage("rus","Russian","Russisch"));
		list.add(new ISOLanguage("kin","Kinyarwanda","Rwanda-Sprache"));
		list.add(new ISOLanguage("smi","Sami languages","Saamisch"));
		list.add(new ISOLanguage("kho","Khotanese; Sakan","Sakisch"));
		list.add(new ISOLanguage("sal","Salishan languages","Salish-Sprache"));
		list.add(new ISOLanguage("sam","Samaritan Aramaic","Samaritanisch"));
		list.add(new ISOLanguage("smo","Samoan","Samoanisch"));
		list.add(new ISOLanguage("sad","Sandawe","Sandawe-Sprache"));
		list.add(new ISOLanguage("sag","Sango","Sango-Sprache"));
		list.add(new ISOLanguage("san","Sanskrit","Sanskrit"));
		list.add(new ISOLanguage("sat","Santali","Santali"));
		list.add(new ISOLanguage("srd","Sardinian","Sardisch"));
		list.add(new ISOLanguage("sas","Sasak","Sasak"));
		list.add(new ISOLanguage("shn","Shan","Schan-Sprache"));
		list.add(new ISOLanguage("sna","Shona","Schona-Sprache"));
		list.add(new ISOLanguage("sco","Scots","Schottisch"));
		list.add(new ISOLanguage("swe","Swedish","Schwedisch"));
		list.add(new ISOLanguage("gsw","Swiss German; Alemannic; Alsatian","Schweizerdeutsch"));
		list.add(new ISOLanguage("sel","Selkup","Selkupisch"));
		list.add(new ISOLanguage("sem","Semitic languages","Semitische Sprachen (Andere)"));
		list.add(new ISOLanguage("srp","Serbian","Serbisch"));
		list.add(new ISOLanguage("srr","Serer","Serer-Sprache"));
		list.add(new ISOLanguage("sid","Sidamo","Sidamo-Sprache"));
		list.add(new ISOLanguage("snd","Sindhi","Sindhi-Sprache"));
		list.add(new ISOLanguage("sin","Sinhala; Sinhalese","Singhalesisch"));
		list.add(new ISOLanguage("sit","Sino-Tibetan languages","Sinotibetische Sprachen (Andere)"));
		list.add(new ISOLanguage("sio","Siouan languages","Sioux-Sprachen (Andere)"));
		list.add(new ISOLanguage("scn","Sicilian","Sizilianisch"));
		list.add(new ISOLanguage("sms","Skolt Sami","Skoltsaamisch"));
		list.add(new ISOLanguage("den","Slave (Athapascan)","Slave-Sprache"));
		list.add(new ISOLanguage("sla","Slavic languages","Slawische Sprachen (Andere)"));
		list.add(new ISOLanguage("slk","Slovak","Slowakisch"));
		list.add(new ISOLanguage("slv","Slovenian","Slowenisch"));
		list.add(new ISOLanguage("sog","Sogdian","Sogdisch"));
		list.add(new ISOLanguage("som","Somali","Somali"));
		list.add(new ISOLanguage("son","Songhai languages","Songhai-Sprache"));
		list.add(new ISOLanguage("snk","Soninke","Soninke-Sprache"));
		list.add(new ISOLanguage("wen","Sorbian languages","Sorbisch (Andere)"));
		list.add(new ISOLanguage("spa","Spanish; Castilian","Spanisch"));
		list.add(new ISOLanguage("srn","Sranan Tongo","Sranantongo"));
		list.add(new ISOLanguage("sot","Sotho, Southern","Süd-Sotho-Sprache"));
		list.add(new ISOLanguage("sma","Southern Sami","Südsaamisch"));
		list.add(new ISOLanguage("suk","Sukuma","Sukuma-Sprache"));
		list.add(new ISOLanguage("sux","Sumerian","Sumerisch"));
		list.add(new ISOLanguage("sun","Sundanese","Sundanesisch"));
		list.add(new ISOLanguage("sus","Susu","Susu"));
		list.add(new ISOLanguage("swa","Swahili","Swahili"));
		list.add(new ISOLanguage("ssw","Swati","Swasi-Sprache"));
		list.add(new ISOLanguage("syc","Classical Syriac","Syrisch"));
		list.add(new ISOLanguage("tgk","Tajik","Tadschikisch"));
		list.add(new ISOLanguage("tgl","Tagalog","Tagalog"));
		list.add(new ISOLanguage("tah","Tahitian","Tahitisch"));
		list.add(new ISOLanguage("tmh","Tamashek","Tamašeq"));
		list.add(new ISOLanguage("tam","Tamil","Tamil"));
		list.add(new ISOLanguage("tat","Tatar","Tatarisch"));
		list.add(new ISOLanguage("tel","Telugu","Telugu-Sprache"));
		list.add(new ISOLanguage("tem","Timne","Temne-Sprache"));
		list.add(new ISOLanguage("ter","Tereno","Tereno-Sprache"));
		list.add(new ISOLanguage("tet","Tetum","Tetum-Sprache"));
		list.add(new ISOLanguage("tha","Thai","Thailändisch"));
		list.add(new ISOLanguage("tai","Tai languages","Thaisprachen (Andere)"));
		list.add(new ISOLanguage("bod","Tibetan","Tibetisch"));
		list.add(new ISOLanguage("tig","Tigre","Tigre-Sprache"));
		list.add(new ISOLanguage("tir","Tigrinya","Tigrinja-Sprache"));
		list.add(new ISOLanguage("tiv","Tiv","Tiv-Sprache"));
		list.add(new ISOLanguage("tli","Tlingit","Tlingit-Sprache"));
		list.add(new ISOLanguage("tkl","Tokelau","Tokelauanisch"));
		list.add(new ISOLanguage("tog","Tonga (Nyasa)","Tonga (Bantusprache, Sambia)"));
		list.add(new ISOLanguage("ton","Tonga (Tonga Islands)","Tongaisch"));
		list.add(new ISOLanguage("chk","Chuukese","Trukesisch"));
		list.add(new ISOLanguage("chg","Chagatai","Tschagataisch"));
		list.add(new ISOLanguage("ces","Czech","Tschechisch"));
		list.add(new ISOLanguage("chm","Mari","Tscheremissisch"));
		list.add(new ISOLanguage("che","Chechen","Tschetschenisch"));
		list.add(new ISOLanguage("chv","Chuvash","Tschuwaschisch"));
		list.add(new ISOLanguage("tsi","Tsimshian","Tsimshian-Sprache"));
		list.add(new ISOLanguage("tso","Tsonga","Tsonga-Sprache"));
		list.add(new ISOLanguage("tsn","Tswana","Tswana-Sprache"));
		list.add(new ISOLanguage("tum","Tumbuka","Tumbuka-Sprache"));
		list.add(new ISOLanguage("tup","Tupi languages","Tupi-Sprache"));
		list.add(new ISOLanguage("tur","Turkish","Türkisch"));
		list.add(new ISOLanguage("tuk","Turkmen","Turkmenisch"));
		list.add(new ISOLanguage("tyv","Tuvinian","Tuwinisch"));
		list.add(new ISOLanguage("twi","Twi","Twi-Sprache"));
		list.add(new ISOLanguage("udm","Udmurt","Udmurtisch"));
		list.add(new ISOLanguage("uga","Ugaritic","Ugaritisch"));
		list.add(new ISOLanguage("uig","Uighur; Uyghur","Uigurisch"));
		list.add(new ISOLanguage("ukr","Ukrainian","Ukrainisch"));
		list.add(new ISOLanguage("hun","Hungarian","Ungarisch"));
		list.add(new ISOLanguage("urd","Urdu","Urdu"));
		list.add(new ISOLanguage("uzb","Uzbek","Usbekisch"));
		list.add(new ISOLanguage("vai","Vai","Vai-Sprache"));
		list.add(new ISOLanguage("ven","Venda","Venda-Sprache"));
		list.add(new ISOLanguage("vie","Vietnamese","Vietnamesisch"));
		list.add(new ISOLanguage("vol","Volapük","Volapük"));
		list.add(new ISOLanguage("wak","Wakashan languages","Wakash-Sprachen"));
		list.add(new ISOLanguage("wal","Wolaitta; Wolaytta","Walamo-Sprache"));
		list.add(new ISOLanguage("wln","Walloon","Wallonisch"));
		list.add(new ISOLanguage("war","Waray","Waray"));
		list.add(new ISOLanguage("was","Washo","Washo-Sprache"));
		list.add(new ISOLanguage("bel","Belarusian","Weißrussisch"));
		list.add(new ISOLanguage("wol","Wolof","Wolof-Sprache"));
		list.add(new ISOLanguage("vot","Votic","Wotisch"));
		list.add(new ISOLanguage("xho","Xhosa","Xhosa-Sprache"));
		list.add(new ISOLanguage("yao","Yao","Yao-Sprache (Bantusprache)"));
		list.add(new ISOLanguage("yap","Yapese","Yapesisch"));
		list.add(new ISOLanguage("yor","Yoruba","Yoruba-Sprache"));
		list.add(new ISOLanguage("ypk","Yupik languages","Ypik-Sprachen"));
		list.add(new ISOLanguage("znd","Zande languages","Zande-Sprachen"));
		list.add(new ISOLanguage("zap","Zapotec","Zapotekisch"));
		list.add(new ISOLanguage("sgn","Sign Languages","Zeichensprachen"));
		list.add(new ISOLanguage("zen","Zenaga","Zenaga"));
		list.add(new ISOLanguage("zha","Zhuang; Chuang","Zhuang"));
		list.add(new ISOLanguage("zul","Zulu","Zulu-Sprache"));
		list.add(new ISOLanguage("zun","Zuni","Zuñi-Sprache"));
		
		
		//list.add(new ISOLanguage(id, nameEng, nameGer)));
		
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
