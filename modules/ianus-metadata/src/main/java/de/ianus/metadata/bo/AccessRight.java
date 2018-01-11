package de.ianus.metadata.bo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Rechtliches.Zugriffsrechte
 * @author jurzua
 *
 */
@Entity
@Table(name="AccessRight")
public class AccessRight extends MDEntry{

	@Column(name="dcId")
	private Long dcId;
	
	@Column(name="rightType")
	public String right = RightType.read.id;
	
	@Column(name="accessGroup")
	public String group = GroupType.anonymous.id;
	
	public static AccessRight clone(AccessRight other, Long dcId){
		AccessRight newItem = new AccessRight();
		newItem.dcId = dcId;
		newItem.right = other.right;
		newItem.group = other.group;
		return newItem;
	}

	public Long getDcId() {
		return dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
	}	

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public JsonObject toJsonObject(JsonObject json){
		json = super.toJsonObject(json);
		
		json.addProperty("dcId", this.dcId);
		json.addProperty("right", this.right);
		json.addProperty("group", this.group);
		
		return json;
	}
	
	public static JsonArray toJsonArray(Set<AccessRight> list){
		JsonArray array = new JsonArray();
		for(AccessRight item : list){
			JsonObject item0 = item.toJsonObject(new JsonObject());
			array.add(item0);
		}
		return array;
	}
	
	public enum RightType {
		read("read", "read", "Ansehen"),
		edit("edit", "edit", "Bearbeiten"),
		delete("delete", "delete", "Löschen"),
		download("download", "download", "Herunterladen")
		
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		
		private RightType(String id, String labelEng, String labelGer){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
		}
		
		public static String getLabelEng(String valueId){
			for(RightType type : RightType.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(RightType type : RightType.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}
	
	public enum GroupType {

		anonymous("anonymous", "anonymous users", "anonyme Nutzer"),
		users("users", "registerd users", "registrierte Nutzer"),
		groups("groups", "definded groups", "festgelegte Gruppen"),
		individuals("individuals", "definded individuals", "festgelegte Individueen")
		;
		
		public final String id;
		public final String labelEng;
		public final String labelGer;
		
		private GroupType(String id, String labelEng, String labelGer){
			this.id = id;
			this.labelEng = labelEng;
			this.labelGer = labelGer;
		}
		
		public static String getLabelEng(String valueId){
			for(GroupType type : GroupType.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelEng;
				}
			}
			return null;
		}
		
		public static String getLabelGer(String valueId){
			for(GroupType type : GroupType.values()){
				if(StringUtils.equals(valueId, type.id)){
					return type.labelGer;
				}
			}
			return null;
		}
	}
	
}
