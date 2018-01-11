package de.ianus.ingest.core.utils;

import java.util.Comparator;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricActivityInstanceEntity;

public class SortHistoricActivityInstance implements Comparator<HistoricActivityInstance> {
	 
	  @Override
	  public int compare(HistoricActivityInstance o1, HistoricActivityInstance o2) {
		  HistoricActivityInstanceEntity oo1 = (HistoricActivityInstanceEntity)o1;
		  HistoricActivityInstanceEntity oo2 = (HistoricActivityInstanceEntity)o2;
		  long diff = oo1.getSequenceCounter() - oo2.getSequenceCounter();
		  return (int)diff;
	  }
}