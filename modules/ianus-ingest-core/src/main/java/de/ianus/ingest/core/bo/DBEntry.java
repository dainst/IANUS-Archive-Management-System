package de.ianus.ingest.core.bo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

/**
 * @author Jorge Urzua
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class DBEntry {
	
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
	
	public DBEntry(){}
	
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
	
	public String getFormattedCreationTime(){
		if(creationTime != null){
			return DateFormat.getDateTimeInstance(
		            DateFormat.MEDIUM, DateFormat.SHORT).format(creationTime);	
		}
		return null;
	}
	
	public String getFormattedLastChange(){
		if(lastChange != null){
			return DateFormat.getDateTimeInstance(
		            DateFormat.MEDIUM, DateFormat.SHORT).format(lastChange);	
		}
		return null;
	}
	
	public String getFormattedCreationDate(){
		if(creationTime != null){
			return new SimpleDateFormat("yyyy-MM-dd").format(creationTime);	
		}
		return null;
	}
	
	public String getFormattedLastChangeDate(){
		if(lastChange != null){
			return new SimpleDateFormat("yyyy-MM-dd").format(lastChange);	
		}
		return null;
	}
	
	public Date getCreationTime() {
		return creationTime;
	}
	
	// TODO: shouldn't this method be at least protected?
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastChange() {
		return lastChange;
	}
	
	// TODO: shouldn't this method be at least protected?
	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set. Solution: throws an exception (no persistent...)
        if (!(object instanceof DBEntry)) {
            return false;
        }
        DBEntry other = (DBEntry) object;
        
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
