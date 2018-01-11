package de.ianus.metadata.bo.utils;

import org.apache.commons.lang.StringUtils;


public class Country {
	
	public enum Type{
		
		AFG("AFG","Afghanistan","Afghanistan","https://www.iso.org/obp/ui/#iso:code:3166:AF",""),
		EGY("EGY","Egypt","Ägypten","https://www.iso.org/obp/ui/#iso:code:3166:EG",""),
		ALA("ALA","Åland Islands","Ålandinseln","https://www.iso.org/obp/ui/#iso:code:3166:AX",""),
		ALB("ALB","Albania","Albanien","https://www.iso.org/obp/ui/#iso:code:3166:AL",""),
		DZA("DZA","Algeria","Algerien","https://www.iso.org/obp/ui/#iso:code:3166:DZ",""),
		ASM("ASM","American Samoa","Amerikanisch Samoa","https://www.iso.org/obp/ui/#iso:code:3166:AS",""),
		AND("AND","Andorra","Andorra","https://www.iso.org/obp/ui/#iso:code:3166:AD",""),
		AGO("AGO","Angola","Angola","https://www.iso.org/obp/ui/#iso:code:3166:AO",""),
		AIA("AIA","Anguilla","Anguilla","https://www.iso.org/obp/ui/#iso:code:3166:AI",""),
		ATA("ATA","Antarctica","Antarktis","https://www.iso.org/obp/ui/#iso:code:3166:AQ",""),
		ATG("ATG","Antigua and Barbuda","Antigua und Barbuda","https://www.iso.org/obp/ui/#iso:code:3166:AG",""),
		GNQ("GNQ","Equatorial Guinea","Äquatorialguinea","https://www.iso.org/obp/ui/#iso:code:3166:GQ",""),
		ARG("ARG","Argentina","Argentinien","https://www.iso.org/obp/ui/#iso:code:3166:AR",""),
		ARM("ARM","Armenia","Armenien","https://www.iso.org/obp/ui/#iso:code:3166:AM",""),
		ABW("ABW","Aruba","Aruba","https://www.iso.org/obp/ui/#iso:code:3166:AW",""),
		AZE("AZE","Azerbaijan","Aserbaidschan","https://www.iso.org/obp/ui/#iso:code:3166:AZ",""),
		ETH("ETH","Ethiopia","Äthiopien","https://www.iso.org/obp/ui/#iso:code:3166:ET",""),
		AUS("AUS","Australia","Australien","https://www.iso.org/obp/ui/#iso:code:3166:AU",""),
		BHS("BHS","Bahamas","Bahamas","https://www.iso.org/obp/ui/#iso:code:3166:BS",""),
		BHR("BHR","Bahrain","Bahrain","https://www.iso.org/obp/ui/#iso:code:3166:BH",""),
		BGD("BGD","Bangladesh","Bangladesch","https://www.iso.org/obp/ui/#iso:code:3166:BD",""),
		BRB("BRB","Barbados","Barbados","https://www.iso.org/obp/ui/#iso:code:3166:BB",""),
		BLR("BLR","Belarus","Belarus","https://www.iso.org/obp/ui/#iso:code:3166:BY",""),
		BEL("BEL","Belgium","Belgien","https://www.iso.org/obp/ui/#iso:code:3166:BE",""),
		BLZ("BLZ","Belize","Belize","https://www.iso.org/obp/ui/#iso:code:3166:BZ",""),
		BEN("BEN","Benin","Benin","https://www.iso.org/obp/ui/#iso:code:3166:BJ",""),
		BMU("BMU","Bermuda","Bermuda","https://www.iso.org/obp/ui/#iso:code:3166:BM",""),
		BTN("BTN","Bhutan","Bhutan","https://www.iso.org/obp/ui/#iso:code:3166:BT",""),
		BOL("BOL","Bolivia","Bolivien","https://www.iso.org/obp/ui/#iso:code:3166:BO",""),
		BIH("BIH","Bosnia and Herzegovina","Bosnien und Herzegowina","https://www.iso.org/obp/ui/#iso:code:3166:BA",""),
		BWA("BWA","Botswana","Botswana","https://www.iso.org/obp/ui/#iso:code:3166:BW",""),
		BVT("BVT","Bouvet Island","Bouvetinsel","https://www.iso.org/obp/ui/#iso:code:3166:BV",""),
		BRA("BRA","Brazil","Brasilien","https://www.iso.org/obp/ui/#iso:code:3166:BR",""),
		IOT("IOT","British Indian Ocean Territory","Britisches Territorium im Indischen Ozean","https://www.iso.org/obp/ui/#iso:code:3166:IO",""),
		BRN("BRN","Brunei","Brunei","https://www.iso.org/obp/ui/#iso:code:3166:BN",""),
		BGR("BGR","Bulgaria","Bulgarien","https://www.iso.org/obp/ui/#iso:code:3166:BG",""),
		BFA("BFA","Burkina Faso","Burkina Faso","https://www.iso.org/obp/ui/#iso:code:3166:BF",""),
		BDI("BDI","Burundi","Burundi","https://www.iso.org/obp/ui/#iso:code:3166:BI",""),
		CHL("CHL","Chile","Chile","https://www.iso.org/obp/ui/#iso:code:3166:CL",""),
		CHN("CHN","China","China","https://www.iso.org/obp/ui/#iso:code:3166:CN",""),
		COK("COK","Cook Islands","Cookinseln","https://www.iso.org/obp/ui/#iso:code:3166:CK",""),
		CRI("CRI","Costa Rica","Costa Rica","https://www.iso.org/obp/ui/#iso:code:3166:CR",""),
		CUW("CUW","Curacao","Curaçao","https://www.iso.org/obp/ui/#iso:code:3166:CW",""),
		DNK("DNK","Denmark","Dänemark","https://www.iso.org/obp/ui/#iso:code:3166:DK",""),
		COD("COD","Democratic Republic of the Congo","Demokratische Republik Kongo","https://www.iso.org/obp/ui/#iso:code:3166:CD",""),
		DEU("DEU","Germany","Deutschland","https://www.iso.org/obp/ui/#iso:code:3166:DE",""),
		DMA("DMA","Dominica","Dominica","https://www.iso.org/obp/ui/#iso:code:3166:DM",""),
		DOM("DOM","Dominican Republic","Dominikanische Republik","https://www.iso.org/obp/ui/#iso:code:3166:DO",""),
		DJI("DJI","Djibouti","Dschibuti","https://www.iso.org/obp/ui/#iso:code:3166:DJ",""),
		ECU("ECU","Ecuador","Ecuador","https://www.iso.org/obp/ui/#iso:code:3166:EC",""),
		SLV("SLV","El Salvador","El Salvador","https://www.iso.org/obp/ui/#iso:code:3166:SV",""),
		CIV("CIV","Ivory Coast","Elfenbeinküste","https://www.iso.org/obp/ui/#iso:code:3166:CI",""),
		ERI("ERI","Eritrea","Eritrea","https://www.iso.org/obp/ui/#iso:code:3166:ER",""),
		EST("EST","Estonia","Estland","https://www.iso.org/obp/ui/#iso:code:3166:EE",""),
		FLK("FLK","Falkland Islands","Falklandinseln","https://www.iso.org/obp/ui/#iso:code:3166:FK",""),
		FRO("FRO","Faroe Islands","Färöer-Inseln","https://www.iso.org/obp/ui/#iso:code:3166:FO",""),
		FJI("FJI","Fiji","Fidschi","https://www.iso.org/obp/ui/#iso:code:3166:FJ",""),
		FIN("FIN","Finland","Finnland","https://www.iso.org/obp/ui/#iso:code:3166:FI",""),
		FRA("FRA","France","Frankreich","https://www.iso.org/obp/ui/#iso:code:3166:FR",""),
		GUF("GUF","French Guiana","Französisch-Guayana","https://www.iso.org/obp/ui/#iso:code:3166:GF",""),
		PYF("PYF","French Polynesia","Französisch-Polynesien","https://www.iso.org/obp/ui/#iso:code:3166:PF",""),
		ATF("ATF","French Southern and Antarctic Lands","Französische Süd- und Antarktisgebiete","https://www.iso.org/obp/ui/#iso:code:3166:TF",""),
		GAB("GAB","Gabon","Gabun","https://www.iso.org/obp/ui/#iso:code:3166:GA",""),
		GMB("GMB","Gambia","Gambia","https://www.iso.org/obp/ui/#iso:code:3166:GM",""),
		GEO("GEO","Georgia","Georgien","https://www.iso.org/obp/ui/#iso:code:3166:GE",""),
		GHA("GHA","Ghana","Ghana","https://www.iso.org/obp/ui/#iso:code:3166:GH",""),
		GIB("GIB","Gibraltar","Gibraltar","https://www.iso.org/obp/ui/#iso:code:3166:GI",""),
		GRD("GRD","Grenada","Grenada","https://www.iso.org/obp/ui/#iso:code:3166:GD",""),
		GRC("GRC","Greece","Griechenland","https://www.iso.org/obp/ui/#iso:code:3166:GR",""),
		GRL("GRL","Greenland","Grönland","https://www.iso.org/obp/ui/#iso:code:3166:GL",""),
		GLP("GLP","Guadeloupe","Guadeloupe","https://www.iso.org/obp/ui/#iso:code:3166:GP",""),
		GUM("GUM","Guam","Guam","https://www.iso.org/obp/ui/#iso:code:3166:GU",""),
		GTM("GTM","Guatemala","Guatemala","https://www.iso.org/obp/ui/#iso:code:3166:GT",""),
		GGY("GGY","Guernsey","Guernsey","https://www.iso.org/obp/ui/#iso:code:3166:GG",""),
		GIN("GIN","Guinea","Guinea","https://www.iso.org/obp/ui/#iso:code:3166:GN",""),
		GNB("GNB","Guinea-Bissau","Guinea-Bissau","https://www.iso.org/obp/ui/#iso:code:3166:GW",""),
		GUY("GUY","Guyana","Guyana","https://www.iso.org/obp/ui/#iso:code:3166:GY",""),
		HTI("HTI","Haiti","Haiti","https://www.iso.org/obp/ui/#iso:code:3166:HT",""),
		HMD("HMD","Heard Island and McDonald Islands","Heard und McDonaldinseln","https://www.iso.org/obp/ui/#iso:code:3166:HM",""),
		HND("HND","Honduras","Honduras","https://www.iso.org/obp/ui/#iso:code:3166:HN",""),
		HKG("HKG","Hong Kong","Hongkong","https://www.iso.org/obp/ui/#iso:code:3166:HK",""),
		IND("IND","India","Indien","https://www.iso.org/obp/ui/#iso:code:3166:IN",""),
		IDN("IDN","Indonesia","Indonesien","https://www.iso.org/obp/ui/#iso:code:3166:ID",""),
		IMN("IMN","Isle of Man","Insel Man","https://www.iso.org/obp/ui/#iso:code:3166:IM",""),
		IRQ("IRQ","Iraq","Irak","https://www.iso.org/obp/ui/#iso:code:3166:IQ",""),
		IRN("IRN","Iran","Iran","https://www.iso.org/obp/ui/#iso:code:3166:IR",""),
		IRL("IRL","Ireland","Irland","https://www.iso.org/obp/ui/#iso:code:3166:IE",""),
		ISL("ISL","Iceland","Island","https://www.iso.org/obp/ui/#iso:code:3166:IS",""),
		ISR("ISR","Israel","Israel","https://www.iso.org/obp/ui/#iso:code:3166:IL",""),
		ITA("ITA","Italy","Italien","https://www.iso.org/obp/ui/#iso:code:3166:IT",""),
		JAM("JAM","Jamaica","Jamaika","https://www.iso.org/obp/ui/#iso:code:3166:JM",""),
		JPN("JPN","Japan","Japan","https://www.iso.org/obp/ui/#iso:code:3166:JP",""),
		YEM("YEM","Yemen","Jemen","https://www.iso.org/obp/ui/#iso:code:3166:YE",""),
		JEY("JEY","Jersey","Jersey","https://www.iso.org/obp/ui/#iso:code:3166:JE",""),
		JOR("JOR","Jordan","Jordanien","https://www.iso.org/obp/ui/#iso:code:3166:JO",""),
		VGB("VGB","British Virgin Islands","Jungferninseln (UK)","https://www.iso.org/obp/ui/#iso:code:3166:VG",""),
		VIR("VIR","Virgin Islands","Jungferninseln (US)","https://www.iso.org/obp/ui/#iso:code:3166:VI",""),
		CYM("CYM","Cayman Islands","Kaimaninseln","https://www.iso.org/obp/ui/#iso:code:3166:KY",""),
		KHM("KHM","Cambodia","Kambodscha","https://www.iso.org/obp/ui/#iso:code:3166:KH",""),
		CMR("CMR","Cameroon","Kamerun","https://www.iso.org/obp/ui/#iso:code:3166:CM",""),
		CAN("CAN","Canada","Kanada","https://www.iso.org/obp/ui/#iso:code:3166:CA",""),
		CPV("CPV","Cape Verde","Kap Verde","https://www.iso.org/obp/ui/#iso:code:3166:CV",""),
		KAZ("KAZ","Kazakhstan","Kasachstan","https://www.iso.org/obp/ui/#iso:code:3166:KZ",""),
		QAT("QAT","Qatar","Katar","https://www.iso.org/obp/ui/#iso:code:3166:QA",""),
		KEN("KEN","Kenya","Kenia","https://www.iso.org/obp/ui/#iso:code:3166:KE",""),
		KGZ("KGZ","Kyrgyzstan","Kirgisistan","https://www.iso.org/obp/ui/#iso:code:3166:KG",""),
		KIR("KIR","Kiribati","Kiribati","https://www.iso.org/obp/ui/#iso:code:3166:KI",""),
		CCK("CCK","Cocos Islands","Kokosinseln","https://www.iso.org/obp/ui/#iso:code:3166:CC",""),
		COL("COL","Colombia","Kolumbien","https://www.iso.org/obp/ui/#iso:code:3166:CO",""),
		COM("COM","Comoros","Komoren","https://www.iso.org/obp/ui/#iso:code:3166:KM",""),
		COG("COG","Republic of the Congo","Kongo","https://www.iso.org/obp/ui/#iso:code:3166:CG",""),
		KOS("KOS","Kosovo","Kosovo","https://www.iso.org/obp/ui/#iso:code:3166:XK",""),
		HRV("HRV","Croatia","Kroatien","https://www.iso.org/obp/ui/#iso:code:3166:HR",""),
		CUB("CUB","Cuba","Kuba","https://www.iso.org/obp/ui/#iso:code:3166:CU",""),
		KWT("KWT","Kuwait","Kuwait","https://www.iso.org/obp/ui/#iso:code:3166:KW",""),
		LAO("LAO","Laos","Laos","https://www.iso.org/obp/ui/#iso:code:3166:LA",""),
		LSO("LSO","Lesotho","Lesotho","https://www.iso.org/obp/ui/#iso:code:3166:LS",""),
		LVA("LVA","Latvia","Lettland","https://www.iso.org/obp/ui/#iso:code:3166:LV",""),
		LBN("LBN","Lebanon","Libanon","https://www.iso.org/obp/ui/#iso:code:3166:LB",""),
		LBR("LBR","Liberia","Liberia","https://www.iso.org/obp/ui/#iso:code:3166:LR",""),
		LBY("LBY","Libya","Libyen","https://www.iso.org/obp/ui/#iso:code:3166:LY",""),
		LIE("LIE","Liechtenstein","Liechtenstein","https://www.iso.org/obp/ui/#iso:code:3166:LI",""),
		LTU("LTU","Lithuania","Litauen","https://www.iso.org/obp/ui/#iso:code:3166:LT",""),
		LUX("LUX","Luxembourg","Luxemburg","https://www.iso.org/obp/ui/#iso:code:3166:LU",""),
		MAC("MAC","Macau","Macau","https://www.iso.org/obp/ui/#iso:code:3166:MO",""),
		MDG("MDG","Madagascar","Madagaskar","https://www.iso.org/obp/ui/#iso:code:3166:MG",""),
		MWI("MWI","Malawi","Malawi","https://www.iso.org/obp/ui/#iso:code:3166:MW",""),
		MYS("MYS","Malaysia","Malaysia","https://www.iso.org/obp/ui/#iso:code:3166:MY",""),
		MDV("MDV","Maldives","Malediven","https://www.iso.org/obp/ui/#iso:code:3166:MV",""),
		MLI("MLI","Mali","Mali","https://www.iso.org/obp/ui/#iso:code:3166:ML",""),
		MLT("MLT","Malta","Malta","https://www.iso.org/obp/ui/#iso:code:3166:MT",""),
		MAR("MAR","Morocco","Marokko","https://www.iso.org/obp/ui/#iso:code:3166:MA",""),
		MHL("MHL","Marshall Islands","Marshallinseln","https://www.iso.org/obp/ui/#iso:code:3166:MH",""),
		MTQ("MTQ","Martinique","Martinique","https://www.iso.org/obp/ui/#iso:code:3166:MQ",""),
		MRT("MRT","Mauritania","Mauretanien","https://www.iso.org/obp/ui/#iso:code:3166:MR",""),
		MUS("MUS","Mauritius","Mauritius","https://www.iso.org/obp/ui/#iso:code:3166:MU",""),
		MYT("MYT","Mayotte","Mayotte","https://www.iso.org/obp/ui/#iso:code:3166:YT",""),
		MKD("MKD","Macedonia","Mazedonien","https://www.iso.org/obp/ui/#iso:code:3166:MK",""),
		MEX("MEX","Mexico","Mexiko","https://www.iso.org/obp/ui/#iso:code:3166:MX",""),
		FSM("FSM","Micronesia, Federated States of","Mikronesien","https://www.iso.org/obp/ui/#iso:code:3166:FM",""),
		MDA("MDA","Moldova","Moldawien","https://www.iso.org/obp/ui/#iso:code:3166:MD",""),
		MCO("MCO","Monaco","Monaco","https://www.iso.org/obp/ui/#iso:code:3166:MC",""),
		MNG("MNG","Mongolia","Mongolei","https://www.iso.org/obp/ui/#iso:code:3166:MN",""),
		MNE("MNE","Montenegro","Montenegro","https://www.iso.org/obp/ui/#iso:code:3166:ME",""),
		MSR("MSR","Montserrat","Montserrat","https://www.iso.org/obp/ui/#iso:code:3166:MS",""),
		MOZ("MOZ","Mozambique","Mosambik","https://www.iso.org/obp/ui/#iso:code:3166:MZ",""),
		MMR("MMR","Burma","Myanmar","https://www.iso.org/obp/ui/#iso:code:3166:MM",""),
		NAM("NAM","Namibia","Namibia","https://www.iso.org/obp/ui/#iso:code:3166:NA",""),
		NRU("NRU","Nauru","Nauru","https://www.iso.org/obp/ui/#iso:code:3166:NR",""),
		NPL("NPL","Nepal","Nepal","https://www.iso.org/obp/ui/#iso:code:3166:NP",""),
		NCL("NCL","New Caledonia","Neukaledonien","https://www.iso.org/obp/ui/#iso:code:3166:NC",""),
		NZL("NZL","New Zealand","Neuseeland","https://www.iso.org/obp/ui/#iso:code:3166:NZ",""),
		NIC("NIC","Nicaragua","Nicaragua","https://www.iso.org/obp/ui/#iso:code:3166:NI",""),
		NLD("NLD","Netherlands","Niederlande","https://www.iso.org/obp/ui/#iso:code:3166:NL",""),
		ANT("ANT","Netherlands Antilles","Niederländische Antillen","https://www.iso.org/obp/ui/#iso:code:3166:AN",""),
		NER("NER","Niger","Niger","https://www.iso.org/obp/ui/#iso:code:3166:NE",""),
		NGA("NGA","Nigeria","Nigeria","https://www.iso.org/obp/ui/#iso:code:3166:NG",""),
		NIU("NIU","Niue","Niue","https://www.iso.org/obp/ui/#iso:code:3166:NU",""),
		PRK("PRK","North Korea","Nordkorea","https://www.iso.org/obp/ui/#iso:code:3166:KP",""),
		MNP("MNP","Northern Mariana Islands","Nördliche Marianen","https://www.iso.org/obp/ui/#iso:code:3166:MP",""),
		NFK("NFK","Norfolk Island","Norfolkinsel","https://www.iso.org/obp/ui/#iso:code:3166:NF",""),
		NOR("NOR","Norway","Norwegen","https://www.iso.org/obp/ui/#iso:code:3166:NO",""),
		OMN("OMN","Oman","Oman","https://www.iso.org/obp/ui/#iso:code:3166:OM",""),
		AUT("AUT","Austria","Österreich","https://www.iso.org/obp/ui/#iso:code:3166:AT",""),
		TLS("TLS","Timor-Leste","Osttimor","https://www.iso.org/obp/ui/#iso:code:3166:TL",""),
		PAK("PAK","Pakistan","Pakistan","https://www.iso.org/obp/ui/#iso:code:3166:PK",""),
		PSE("PSE","Palestine","Palästina","https://www.iso.org/obp/ui/#iso:code:3166:PS",""),
		PLW("PLW","Palau","Palau","https://www.iso.org/obp/ui/#iso:code:3166:PW",""),
		PAN("PAN","Panama","Panama","https://www.iso.org/obp/ui/#iso:code:3166:PA",""),
		PNG("PNG","Papua New Guinea","Papua-Neuguinea","https://www.iso.org/obp/ui/#iso:code:3166:PG",""),
		PRY("PRY","Paraguay","Paraguay","https://www.iso.org/obp/ui/#iso:code:3166:PY",""),
		PER("PER","Peru","Peru","https://www.iso.org/obp/ui/#iso:code:3166:PE",""),
		PHL("PHL","Philippines","Philippinen","https://www.iso.org/obp/ui/#iso:code:3166:PH",""),
		PCN("PCN","Pitcairn Islands","Pitcairninseln","https://www.iso.org/obp/ui/#iso:code:3166:PN",""),
		POL("POL","Poland","Polen","https://www.iso.org/obp/ui/#iso:code:3166:PL",""),
		PRT("PRT","Portugal","Portugal","https://www.iso.org/obp/ui/#iso:code:3166:PT",""),
		PRI("PRI","Puerto Rico","Puerto Rico","https://www.iso.org/obp/ui/#iso:code:3166:PR",""),
		REU("REU","Reunion","Réunion","https://www.iso.org/obp/ui/#iso:code:3166:RE",""),
		RWA("RWA","Rwanda","Ruanda","https://www.iso.org/obp/ui/#iso:code:3166:RW",""),
		ROU("ROU","Romania","Rumänien","https://www.iso.org/obp/ui/#iso:code:3166:RO",""),
		RUS("RUS","Russia","Russland","https://www.iso.org/obp/ui/#iso:code:3166:RU",""),
		SLB("SLB","Solomon Islands","Salomonen","https://www.iso.org/obp/ui/#iso:code:3166:SB",""),
		ZMB("ZMB","Zambia","Sambia","https://www.iso.org/obp/ui/#iso:code:3166:ZM",""),
		WSM("WSM","Samoa","Samoa","https://www.iso.org/obp/ui/#iso:code:3166:WS",""),
		SMR("SMR","San Marino","San Marino","https://www.iso.org/obp/ui/#iso:code:3166:SM",""),
		BLM("BLM","Saint Barthelemy","Sankt Bartholomäus","https://www.iso.org/obp/ui/#iso:code:3166:BL",""),
		STP("STP","Sao Tome and Principe","São Tomé und Príncipe","https://www.iso.org/obp/ui/#iso:code:3166:ST",""),
		SAU("SAU","Saudi Arabia","Saudi-Arabien","https://www.iso.org/obp/ui/#iso:code:3166:SA",""),
		SWE("SWE","Sweden","Schweden","https://www.iso.org/obp/ui/#iso:code:3166:SE",""),
		CHE("CHE","Switzerland","Schweiz","https://www.iso.org/obp/ui/#iso:code:3166:CH",""),
		SEN("SEN","Senegal","Senegal","https://www.iso.org/obp/ui/#iso:code:3166:SN",""),
		SRB("SRB","Serbia","Serbien","https://www.iso.org/obp/ui/#iso:code:3166:RS",""),
		SYC("SYC","Seychelles","Seychellen","https://www.iso.org/obp/ui/#iso:code:3166:SC",""),
		SLE("SLE","Sierra Leone","Sierra Leone","https://www.iso.org/obp/ui/#iso:code:3166:SL",""),
		ZWE("ZWE","Zimbabwe","Simbabwe","https://www.iso.org/obp/ui/#iso:code:3166:ZW",""),
		SGP("SGP","Singapore","Singapur","https://www.iso.org/obp/ui/#iso:code:3166:SG",""),
		SXM("SXM","Saint Martin","Sint Maarten","https://www.iso.org/obp/ui/#iso:code:3166:SX",""),
		SVK("SVK","Slovakia","Slowakei","https://www.iso.org/obp/ui/#iso:code:3166:SK",""),
		SVN("SVN","Slovenia","Slowenien","https://www.iso.org/obp/ui/#iso:code:3166:SI",""),
		SOM("SOM","Somalia","Somalia","https://www.iso.org/obp/ui/#iso:code:3166:SO",""),
		ESP("ESP","Spain","Spanien","https://www.iso.org/obp/ui/#iso:code:3166:ES",""),
		LKA("LKA","Sri Lanka","Sri Lanka","https://www.iso.org/obp/ui/#iso:code:3166:LK",""),
		SHN("SHN","'Saint Helena',' Ascension and Tristan da Cunha'","St. Helena","https://www.iso.org/obp/ui/#iso:code:3166:SH",""),
		KNA("KNA","Saint Kitts and Nevis","St. Kitts und Nevis","https://www.iso.org/obp/ui/#iso:code:3166:KN",""),
		LCA("LCA","Saint Lucia","St. Lucia","https://www.iso.org/obp/ui/#iso:code:3166:LC",""),
		SPM("SPM","Saint Pierre and Miquelon","St. Pierre und Miquelon","https://www.iso.org/obp/ui/#iso:code:3166:PM",""),
		VCT("VCT","Saint Vincent and the Grenadines","St. Vincent und die Grenadinen","https://www.iso.org/obp/ui/#iso:code:3166:VC",""),
		ZAF("ZAF","South Africa","Südafrika","https://www.iso.org/obp/ui/#iso:code:3166:ZA",""),
		SDN("SDN","Sudan","Sudan","https://www.iso.org/obp/ui/#iso:code:3166:SD",""),
		SGS("SGS","South Georgia and South Sandwich Islands","Südgeorgien und die Südlichen Sandwichinseln","https://www.iso.org/obp/ui/#iso:code:3166:GS",""),
		KOR("KOR","South Korea","Südkorea","https://www.iso.org/obp/ui/#iso:code:3166:KR",""),
		SSD("SSD","South Sudan","Südsudan","https://www.iso.org/obp/ui/#iso:code:3166:SS",""),
		SUR("SUR","Suriname","Suriname","https://www.iso.org/obp/ui/#iso:code:3166:SR",""),
		SJM("SJM","Svalbard","Svalbard und Jan Mayen","https://www.iso.org/obp/ui/#iso:code:3166:SJ",""),
		SWZ("SWZ","Swaziland","Swasiland","https://www.iso.org/obp/ui/#iso:code:3166:SZ",""),
		SYR("SYR","Syria","Syrien","https://www.iso.org/obp/ui/#iso:code:3166:SY",""),
		TJK("TJK","Tajikistan","Tadschikistan","https://www.iso.org/obp/ui/#iso:code:3166:TJ",""),
		TWN("TWN","Taiwan","Taiwan","https://www.iso.org/obp/ui/#iso:code:3166:TW",""),
		TZA("TZA","Tanzania","Tansania","https://www.iso.org/obp/ui/#iso:code:3166:TZ",""),
		THA("THA","Thailand","Thailand","https://www.iso.org/obp/ui/#iso:code:3166:TH",""),
		TGO("TGO","Togo","Togo","https://www.iso.org/obp/ui/#iso:code:3166:TG",""),
		TKL("TKL","Tokelau","Tokelau","https://www.iso.org/obp/ui/#iso:code:3166:TK",""),
		TON("TON","Tonga","Tonga","https://www.iso.org/obp/ui/#iso:code:3166:TO",""),
		TTO("TTO","Trinidad and Tobago","Trinidad und Tobago","https://www.iso.org/obp/ui/#iso:code:3166:TT",""),
		TCD("TCD","Chad","Tschad","https://www.iso.org/obp/ui/#iso:code:3166:TD",""),
		CZE("CZE","Czech Republic","Tschechien","https://www.iso.org/obp/ui/#iso:code:3166:CZ",""),
		TUN("TUN","Tunisia","Tunesien","https://www.iso.org/obp/ui/#iso:code:3166:TN",""),
		TUR("TUR","Turkey","Türkei","https://www.iso.org/obp/ui/#iso:code:3166:TR",""),
		TKM("TKM","Turkmenistan","Turkmenistan","https://www.iso.org/obp/ui/#iso:code:3166:TM",""),
		TCA("TCA","Turks and Caicos Islands","Turks und Caicosinseln","https://www.iso.org/obp/ui/#iso:code:3166:TC",""),
		TUV("TUV","Tuvalu","Tuvalu","https://www.iso.org/obp/ui/#iso:code:3166:TV",""),
		UGA("UGA","Uganda","Uganda","https://www.iso.org/obp/ui/#iso:code:3166:UG",""),
		UKR("UKR","Ukraine","Ukraine","https://www.iso.org/obp/ui/#iso:code:3166:UA",""),
		HUN("HUN","Hungary","Ungarn","https://www.iso.org/obp/ui/#iso:code:3166:HU",""),
		URY("URY","Uruguay","Uruguay","https://www.iso.org/obp/ui/#iso:code:3166:UY",""),
		UZB("UZB","Uzbekistan","Usbekistan","https://www.iso.org/obp/ui/#iso:code:3166:UZ",""),
		VUT("VUT","Vanuatu","Vanuatu","https://www.iso.org/obp/ui/#iso:code:3166:VU",""),
		VAT("VAT","Holy See (Vatican City)","Vatikanstadt","https://www.iso.org/obp/ui/#iso:code:3166:VA",""),
		VEN("VEN","Venezuela","Venezuela","https://www.iso.org/obp/ui/#iso:code:3166:VE",""),
		ARE("ARE","United Arab Emirates","Vereinigte Arabische Emirate","https://www.iso.org/obp/ui/#iso:code:3166:AE",""),
		USA("USA","United States","Vereinigte Staaten von Amerika","https://www.iso.org/obp/ui/#iso:code:3166:US",""),
		GBR("GBR","United Kingdom","Vereinigtes Königreich","https://www.iso.org/obp/ui/#iso:code:3166:GB",""),
		VNM("VNM","Vietnam","Vietnam","https://www.iso.org/obp/ui/#iso:code:3166:VN",""),
		WLF("WLF","Wallis and Futuna","Wallis und Futuna","https://www.iso.org/obp/ui/#iso:code:3166:WF",""),
		CXR("CXR","Christmas Island","Weihnachtsinsel","https://www.iso.org/obp/ui/#iso:code:3166:CX",""),
		ESH("ESH","Western Sahara","Westsahara","https://www.iso.org/obp/ui/#iso:code:3166:EH",""),
		CAF("CAF","Central African Republic","Zentralafrikanische Republik","https://www.iso.org/obp/ui/#iso:code:3166:CF",""),
		CYP("CYP","Cyprus","Zypern","https://www.iso.org/obp/ui/#iso:code:3166:CY","")
		
		;
		
		public String id;
		public String labelEng;
		public String labelGer;
		public String url;
		public String description;
		
		private Type(String id, String labelEng, String labelGer, String url, String description){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
			this.url = url;
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
}
