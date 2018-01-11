package de.ianus.metadata.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.ianus.metadata.MetadataService;
import de.ianus.metadata.bo.CollectionFile;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;

public class JSONUtils {
	
	public static String getString(JsonObject json, String name){
		JsonElement e = json.get(name);
		return (e != null) ? e.getAsString() : null;
	}
	
	public static Long getLong(JsonObject json, String name){
		JsonElement e = json.get(name);
		return (e != null) ? e.getAsLong() : null;
	}
	
	public static Boolean getBoolean(JsonObject json, String name){
		JsonElement e = json.get(name);
		return (e != null) ? e.getAsBoolean() : null;
	}
	
	public static void jsonToDc(MetadataService mdService, DataCollection dc, JsonObject json) throws Exception{
		
		DataCollection newDc = null;
		
		dc.setLabel(getString(json, "label"));
		
		//##### Title #####
		//json.put("titleList", JSONUtils.getArray4TextAttribute(this.getTitleList()));
		//json.put("alternativeTitleList", JSONUtils.getArray4TextAttribute(this.getAlternativeTitleList()));
				
		//##### Description #####
		//json.put("projectDescriptionList", JSONUtils.getArray4TextAttribute(this.getProjectDescriptionList()));
		//json.put("summaryList", JSONUtils.getArray4TextAttribute(this.getSummaryList()));
		//json.put("classificationList", JSONUtils.getArray4TextAttribute(this.getClassificationList()));
				
		//##### Keywords (Schlagworte) #####
		//json.put("disciplineList", JSONUtils.getArray4ElementOfList(this.disciplineList));
		//json.put("contentRoughList", JSONUtils.getArray4ElementOfList(this.contentRoughList));
		//json.put("contentFineList", JSONUtils.getArray4ElementOfList(this.contentFineList));
		//json.put("methodRoughList", JSONUtils.getArray4ElementOfList(this.methodRoughList));
		//json.put("methodFineList", JSONUtils.getArray4ElementOfList(this.methodFineList));
		
			
		/*		
		JsonArray accessRightList = new JsonArray();
		for(AccessRight item : this.accessRightList){
			JsonObject json0 = item.toJsonObject(new JsonObject());
			accessRightList.add(json0);
		}
		json.put("accessRightList", accessRightList);
		*/
		
		/*
		JsonArray dataGenerationArray = new JsonArray();
		for(TimeSpan timeSpan : this.dataGenerationList){
			dataGenerationArray.add(timeSpan.toJsonObject(new JsonObject()));
		}
		json.put("dataGenerationList", dataGenerationArray);
		*/
				
		//Protokollierung
		/*
		json.put("sipFinalization", formatDate(sipFinalization));
		json.put("signingContract", formatDate(signingContract));
		json.put("curationStart", formatDate(curationStart));
		json.put("curationEnd", formatDate(curationEnd));
		json.put("presentationDate", formatDate(presentationDate));
		json.put("lastTest", formatDate(lastTest));
		*/
		
		
		List<Long> roleOldIdList = new ArrayList<Long>();
		
		//Map<oldId, newId>, this map is used to be able to reconstruct the roles
		Map<Long, Long> mapActorId = new HashMap<Long, Long>();
		JsonArray actorList = json.getAsJsonArray("actorList");
		Iterator<JsonElement> actorIterator = actorList.iterator();
		while (actorIterator.hasNext()) {
			JsonElement element = actorIterator.next();
			JsonObject object = element.getAsJsonObject();
			
			//here we create a new actor
			Long oldActorId = getLong(json, "id");
			Actor actor = null;	
			if(Person.class.toString().equals(getString(object, "actorType"))){
				actor = Person.createFromJsonObject(object);
			}else if(Institution.class.toString().equals(getString(object, "actorType"))){
				actor = Institution.createFromJsonObject(object);
			}
			//actor.setDcId(newDc.getId());
			actor.setSource(newDc);
			mdService.saveEntry(actor);
			mapActorId.put(oldActorId, actor.getId());
			
			//here we reconstruct the roles
			//@TODO we do not consider the relation between an actor and a CollectionFile
			JsonArray roleList = json.getAsJsonArray("roleList");
			Iterator<JsonElement> roleIterator = roleList.iterator();
			while (roleIterator.hasNext()) {
				JsonElement e1 = roleIterator.next();
				JsonObject roleObject = e1.getAsJsonObject();
				
				Long oldRoleId = getLong(roleObject, "id");
				if(!roleOldIdList.contains(oldRoleId)){
					ActorRole role = new ActorRole();
					role.setTypeId(getString(roleObject, "typeId"));
					role.setActor(actor);
					role.setSource(newDc);
				}
			}
		}
	}
	
	
	public static JsonObject collectionFileListToJsonObject(MetadataService service, Long dcId) throws Exception{
		DataCollection dc = service.getDataCollection(dcId);
		JsonObject output = new JsonObject();
		if(dc != null){
			Set<CollectionFile> fileList = service.getCollectionFileList(dcId);
			output.add("collectionFileList", CollectionFile.toJsonArray(fileList, dc));
		}
		return output;
		
	}
	
	public static JsonObject collectionFileToJsonObject(MetadataService service, Long fileId) throws Exception{
		CollectionFile file = service.getCollectionFile(fileId);
		DataCollection dc = service.getDataCollection(file.getSourceId());
		JsonObject output = new JsonObject();
		if(file != null){
			output = file.toJsonObject(output, dc);
		}
		return output;
	}
	
	
	
	
	
}
