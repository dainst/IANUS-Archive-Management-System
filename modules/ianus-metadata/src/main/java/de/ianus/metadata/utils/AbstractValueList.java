package de.ianus.metadata.utils;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.ianus.metadata.bo.utils.ElementOfList;

public abstract class AbstractValueList {

	public  static String IGNORE_ID = "ignore_id";
	
	private static final Logger logger = LogManager.getLogger(AbstractValueList.class);

	// This list contains all element of the list, no matter if they are main or
	// secondary elements.
	private Map<String, ElementOfList> elementList = new HashMap<String, ElementOfList>();

	// This list contains only the main elements
	private List<String> mainElementList = new ArrayList<String>();

	// This map contains the relation between a main element and a secondary
	// element (Map<main_id, secondary_id>)
	private Map<String, List<String>> hierarchieMap = new HashMap<String, List<String>>();
	
	
	private ValueListUtils.Names listName;
	
	private static Map<ValueListUtils.Names, String> fileNameList = new HashMap<ValueListUtils.Names, String>();
	
	static{
		fileNameList.put(null, "metadata/discipline_list.json");
	}

	public static boolean isIgnoreId(String id){
		return StringUtils.isNotEmpty(id) && id.startsWith(IGNORE_ID);
	}
	
	// This method use to read the data from the Json file
	protected void loadValues(ValueListUtils.Names listName) throws Exception {
		this.listName = listName;
		
		JsonParser parser = new JsonParser();

		// JsonArray array = (JsonArray) parser.parse(new
		// FileReader("values.json"));
		
		String fileName = fileNameList.get(this.listName);
		JsonObject root = (JsonObject) parser
				.parse(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName)));

		JsonArray items = root.getAsJsonArray("items");
		JsonArray dependencies = root.getAsJsonArray("dependencies");

		Iterator<JsonElement> iterator = items.iterator();
		while (iterator.hasNext()) {
			JsonElement element = iterator.next();
			JsonObject object = element.getAsJsonObject();

			ElementOfList eol = new ElementOfList();
			eol.setListId(this.listName.id);
			// Use the Regular Expression to remove the white space from the
			// Json file //.trim().replaceAll("\\s+","")
			eol.setValue(object.get("label").getAsString()/*.trim().replaceAll("\\s+", "")*/);
			eol.setValueId(object.get("id").getAsString().trim().replaceAll("\\s+", ""));
			elementList.put(eol.getValueId(), eol);
			if (object.has("type")) {
				mainElementList.add(eol.getValueId());
			}
		}

		Iterator<JsonElement> iteratorDependencies = dependencies.iterator();
		while (iteratorDependencies.hasNext()) {
			JsonElement element = iteratorDependencies.next();
			JsonObject object = element.getAsJsonObject();
			// Use the Regular Expression to remove the white space from the
			// Json file //.trim().replaceAll("\\s+","")
			String parentId = object.get("parent").getAsString().trim().replaceAll("\\s+", "");
			String childId = object.get("child").getAsString().trim().replaceAll("\\s+", "");

			if (!hierarchieMap.containsKey(parentId)) {
				hierarchieMap.put(parentId, new ArrayList<String>());
			}
			hierarchieMap.get(parentId).add(childId);
		}

		StringBuilder sb = new StringBuilder();
		for (String key : hierarchieMap.keySet()) {
			sb.append(key + "\n");
			for (String item : hierarchieMap.get(key)) {
				sb.append("\t" + item + "\n");
			}
		}
		logger.debug("Dependencies Map:\n" + sb.toString());
	}
	
	public Map<String, ElementOfList> getElementList() {
		return elementList;
	}

	public List<String> getMainElementListX() {
		return mainElementList;
	}

	public List<String> getSubElementList() {
		List<String> list = new ArrayList<String>();
		for (ElementOfList eol : this.elementList.values()) {
			if (!this.mainElementList.contains(eol.getValueId())) {
				list.add(eol.getValueId());
			}
		}
		return list;
	}

	public Map<String, List<String>> getHierarchieMap() {
		return hierarchieMap;
	}

}
