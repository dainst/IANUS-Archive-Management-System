package de.ianus.ingest.core.bo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author hschmeer, jurzua
 *
 */
@Entity
@Table(name = "TransferP")
public class TransferP extends WorkflowIP  implements Serializable{
	private static final long serialVersionUID = -4048051896967815058L;
	
	
}
