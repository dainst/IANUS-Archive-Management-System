package de.ianus.metadata.bo;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.JsonObject;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class MDEntry {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="id")
    protected Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_time")
    private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_change")
    private Date lastChange;
	
	public MDEntry(){}
	
	public boolean isPersistent(){
    	if(this.getId() == null){
    		return false;
    	}else{
    		return true;
    	}	
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	protected String formatDate(Date date){
		if(date != null){
			return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
		}
		return new String();
	}
	
	public String getFormattedCreationTime(){
		return formatDate(creationTime);
	}
	
	public String getFormattedLastChange(){
		return formatDate(lastChange);
	}
	
	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}
	
	public JsonObject toJsonObject(JsonObject json){
		json.addProperty("id", this.id);
		json.addProperty("lastChange", this.getFormattedLastChange());
		json.addProperty("creationTime", this.getFormattedCreationTime());
		return json;
	}

	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set. Solution: throws an exception (no persistent...)
        if (!(object instanceof MDEntry)) {
            return false;
        }
        MDEntry other = (MDEntry) object;
        
        if(this.id != null && other.id != null && this.id.longValue() == other.id.longValue())
        	return true;
        
        return false;
    }
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append(" [id=" + this.id + "] ");
		return sb.toString();
	}
}
