package de.ianus.ingest.web.pages;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.List;

import de.ianus.ingest.core.utils.MemoryUtil;

public class MemoryUsagePage {
	
	public static String PAGE_NAME = "memoryUsage";
	
	
	private String freeMemory;
	private String maxMemory;
	private String totalMemory;
	private String usedMemory;
	private List<MXBean> memoryMXBeanList;
	
	public String actionRefresh(){
		this.refresh();
		return PAGE_NAME;
	}
	
	private void refresh(){
		Runtime rt = Runtime.getRuntime();
	    this.freeMemory = MemoryUtil.formatBytes(rt.freeMemory());
	    this.maxMemory = MemoryUtil.formatBytes(rt.maxMemory());
	    this.totalMemory = MemoryUtil.formatBytes(rt.totalMemory());
	    this.usedMemory = MemoryUtil.formatBytes(rt.totalMemory() - rt.freeMemory());
	    
	    this.memoryMXBeanList = new ArrayList<MXBean>();
	    
	    for(MemoryPoolMXBean memoryMXBean : ManagementFactory.getMemoryPoolMXBeans()){
	    	this.memoryMXBeanList.add(new MXBean(memoryMXBean));
	    }
	    
	}

	public List<MXBean> getMemoryMXBeanList() {
		return memoryMXBeanList;
	}

	public void setMemoryMXBeanList(List<MXBean> memoryMXBeanList) {
		this.memoryMXBeanList = memoryMXBeanList;
	}

	public String getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	public String getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}

	public String getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}

	public String getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}
	
	public class MXBean{
		
		private String used;
		private String max;
		private String init;
		private String name;
		
		public MXBean(MemoryPoolMXBean item){
			this.name = item.getName();
			this.used = MemoryUtil.formatBytes(item.getUsage().getUsed());
			this.init = MemoryUtil.formatBytes(item.getUsage().getInit());
			this.max = MemoryUtil.formatBytes(item.getUsage().getMax());
		}

		public String getName() {
			return name;
		}
		public String getUsed() {
			return used;
		}
		public String getMax() {
			return max;
		}
		public String getInit() {
			return init;
		}
	}
}
