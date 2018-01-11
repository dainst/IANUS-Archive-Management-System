package de.ianus.metadata.bo.sort;

import java.util.Comparator;
import java.util.Date;

import de.ianus.metadata.bo.DataCollection;

/**
 * <p>Sort collection considering only presentationDate from newest to oldest.
 * If the attribute presentationDate is null of a collection, it will be considered the oldest one.</p>
 * @author jurzua
 *
 */
public class SortDataCollection implements Comparator<DataCollection>{

	@Override
	public int compare(DataCollection o1, DataCollection o2) {
		
		if(o1.getPresentationDate() != null && o2.getPresentationDate() != null){
			Date now = o1.getPresentationDate();
			Date yesterday = o2.getPresentationDate();
			return now.compareTo(yesterday) * -1;
			
		}else if(o1.getPresentationDate() != null){
			return -1;
		}else if(o2.getPresentationDate() != null){
			return 1;
		}else{
			return 0;
		}	
	}
}
