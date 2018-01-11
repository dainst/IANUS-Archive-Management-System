package de.ianus.ingest.web.utils;

import java.util.Comparator;

import javax.faces.model.SelectItem;

public class SelectItemComparator implements Comparator<SelectItem>{
	@Override
	public int compare(SelectItem o1, SelectItem o2) {
		return o1.getLabel().compareTo(o2.getLabel());
	}
}
