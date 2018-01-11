package de.ianus.ingest.web.pages;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.Location;
import de.ianus.ingest.core.bo.LocationPurpose;
import de.ianus.ingest.core.utils.CoreInitialization;
import de.ianus.ingest.web.AbstractBean;

/**
 * 
 * @author jurzua
 *
 */
public class StoragePage extends AbstractBean{
	
	public static String PAGE_NAME = "storage";
	
	private static final Logger logger = LogManager.getLogger(StoragePage.class);
	
	private List<Location> locationList = new ArrayList<Location>();
	private Location location;
	private String testConnection;
	
	private static List<SelectItem> purposeList = new ArrayList<SelectItem>();
	static{
		purposeList.add(new SelectItem(LocationPurpose.PROCESSING_STORAGE, "Working storage"));
		purposeList.add(new SelectItem(LocationPurpose.TIP_STORAGE, "Transfer storage"));
		purposeList.add(new SelectItem(LocationPurpose.SIP_STORAGE, "SIP storage"));
		purposeList.add(new SelectItem(LocationPurpose.AIP_STORAGE, "AIP storage"));
		purposeList.add(new SelectItem(LocationPurpose.DIP_STORAGE, "DIP storage"));
	}
	
	public String actionInitCore(){
		try {
			CoreInitialization.initializeCore(Services.getInstance().getDaoService(), Services.getInstance().getStorageService(), getAppBean().getContentPath());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public StoragePage(){
		logger.info("Creating Storage Page...");
	}
	
	public void load(){
		logger.info("Loading locations");
		this.locationList = Services.getInstance().getDaoService().getLocations();
	}
	
	public void listenerSelectLocation(ActionEvent event){
		this.location = (Location)getRequestBean("location");
	}
	
	public void listenerSaveLocation(ActionEvent event){
		try {
			Services.getInstance().getDaoService().saveDBEntry(location);
			this.addMsg("The location has been saved");
			this.location = null;
			this.load();
		} catch (Exception e) {
			addInternalError(e);
		}
	}
	
	public void listenerNewLocation(ActionEvent event){
		this.location = new Location();
		this.testConnection = null;
	}
	
	public void listenerTestConnection(ActionEvent event){
		testConnection = (location != null && location.getCanConnect()) ? "OK" : "Connection failed"; 
	}
	
	public void listenerCancelEdition(ActionEvent event){
		this.location = null;
	}

	public boolean isShowLocationTable(){
		return !this.locationList.isEmpty() && this.location == null;
	}
	
	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public Location getLocation() {
		return location;
	}

	public List<SelectItem> getPurposeList() {
		return purposeList;
	}

	public String getTestConnection() {
		return testConnection;
	}

	public void setTestConnection(String testConnection) {
		this.testConnection = testConnection;
	}
}
