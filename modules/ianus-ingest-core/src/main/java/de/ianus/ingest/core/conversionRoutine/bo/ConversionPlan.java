package de.ianus.ingest.core.conversionRoutine.bo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import de.ianus.ingest.core.bo.DBEntry;

@Entity
@Table(name = "ConversionPlan")
public class ConversionPlan extends DBEntry implements Serializable{
	private static final long serialVersionUID = -235513249302858854L;
	

}
