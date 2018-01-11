package de.ianus.ingest.core.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.text.DecimalFormat;

public class MemoryUtil {

	public static DecimalFormat decimalFormatter = new DecimalFormat("##.00");
	
	public static String printMemoryUsage(String message){
		
		Runtime rt = Runtime.getRuntime();
	    
	    String heapFreeMemory = formatBytes(rt.freeMemory());
	    String heapMaxMemory = formatBytes(rt.maxMemory());
	    String heapTotalMemory = formatBytes(rt.totalMemory());
	    String heapUsedMemory = formatBytes(rt.totalMemory() - rt.freeMemory());
	    
	    String mdInitMemory = null;
	    String mdMaxMemory = null;
	    String mdUsedMemory = null;
	    
	    for (MemoryPoolMXBean memoryMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
            if ("Metaspace".equals(memoryMXBean.getName())){
            	mdInitMemory = formatBytes(memoryMXBean.getUsage().getInit());
            	mdMaxMemory = formatBytes(memoryMXBean.getUsage().getMax());
            	mdUsedMemory = formatBytes(memoryMXBean.getUsage().getUsed());
            }
	    }
	    
	    
	    String result = null;
	    result = "#*# " + message + "\n"
	    		+ "Heap\t[freeMemory=" + heapFreeMemory + ", maxMemory=" + heapMaxMemory + ", totalMemory=" + heapTotalMemory +", usedMemory=" +heapUsedMemory+ "]\n"
	    		+ "Meta\t[mdInitMemory=" + mdInitMemory + ", mdMaxMemory=" + mdMaxMemory + ", mdUsedMemory=" + mdUsedMemory + "]\n";
	    
	    return result;
	}
	
	public void printMemoryMXBean(MemoryPoolMXBean memoryMXBean){
		memoryMXBean.getName();
		memoryMXBean.getUsage().getUsed();
		memoryMXBean.getUsage().getMax();
		memoryMXBean.getUsage().getInit();
	} 
	
	public static String formatBytes(long bytes){
		String output = null;
		
		if(bytes < 1024){
			output = bytes + " bytes";
		}else if(bytes <  1048576){
			double value = bytes / 1024.0;
			output = decimalFormatter.format(value) + " KB";
		}else if(bytes < 1073741824){
			double value = bytes / 1048576.0;
			output = decimalFormatter.format(value) + " MB";
		}else{
			double value = bytes / 1073741824.0;
			output = decimalFormatter.format(value) + " GB";
		}
		
		return output;
	}
	
	
	
}
