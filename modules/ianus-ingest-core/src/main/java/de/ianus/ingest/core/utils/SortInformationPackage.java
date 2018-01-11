package de.ianus.ingest.core.utils;

import java.util.Comparator;

import de.ianus.ingest.core.bo.InformationPackage;


public class SortInformationPackage  implements Comparator<InformationPackage>{
	
	@Override
	public int compare(InformationPackage o1, InformationPackage o2) {
		return o1.getCreationTime().compareTo(o2.getCreationTime()) * -1;
	}

}
