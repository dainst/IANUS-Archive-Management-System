package de.ianus.ingest.core;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.RuntimeService;

import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.BagitFile;
import de.ianus.ingest.core.bo.DBEntry;
import de.ianus.ingest.core.bo.DisseminationIP;
import de.ianus.ingest.core.bo.IANUSActivity;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.Location;
import de.ianus.ingest.core.bo.PreArchivalIP;
import de.ianus.ingest.core.bo.PreIngestReport;
import de.ianus.ingest.core.bo.RsyncUpload;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.AbstractFile;
import de.ianus.ingest.core.bo.files.Agent;
import de.ianus.ingest.core.bo.files.AgentEventLink;
import de.ianus.ingest.core.bo.files.AgentEventLink.AgentRole;
import de.ianus.ingest.core.bo.files.Event;
import de.ianus.ingest.core.bo.files.FileConcept;
import de.ianus.ingest.core.bo.files.FileGroup;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.bo.files.FileInstanceProperty;
import de.ianus.ingest.core.bo.files.ObjectEventLink;
import de.ianus.ingest.core.bo.files.ObjectEventLink.ObjectRole;
import de.ianus.ingest.core.bo.ums.User;
import de.ianus.ingest.core.conversionRoutine.bo.ConversionAction;
import de.ianus.ingest.core.conversionRoutine.bo.ConversionRoutine;
import de.ianus.ingest.core.utils.SortInformationPackage;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.sort.SortDataCollection;

/**
 * @author Jorge Urzua
 *
 */
public class DAOService {

	private static final Logger logger = LogManager.getLogger(DAOService.class);
	
	private DecimalFormat df = new DecimalFormat();
	
	public DAOService(){
		df.setMaximumFractionDigits(2);
	}
	
	public List<ConversionAction> getConversionActionList(Comparator<ConversionAction> comparator){
		long start = System.currentTimeMillis();
		List<ConversionAction> list = new ArrayList<ConversionAction>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ConversionAction");
				list = (List<ConversionAction>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		Collections.sort(list, comparator);
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public ConversionAction getConversionAction(long id){
		ConversionAction action = null;
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ConversionAction where id = :id");
				query.setParameter("id", id);
				List<ConversionAction> list = (List<ConversionAction>)query.getResultList();
				action = (list.isEmpty()) ? null : list.get(0);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return action;
	}
	
	public List<ConversionAction> getConversionAction(String actionName){
		List<ConversionAction> list = new ArrayList<ConversionAction>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ConversionAction where name = :name");
				query.setParameter("name", actionName);
				list = (List<ConversionAction>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list;
	}
	
	
	public List<ConversionRoutine> getConversionRoutineList(Comparator<ConversionRoutine> comparator){
		long start = System.currentTimeMillis();
		List<ConversionRoutine> list = new ArrayList<ConversionRoutine>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ConversionRoutine");
				list = (List<ConversionRoutine>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		Collections.sort(list, comparator);
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	/**
	 * Saving an event together with its actors and objects
	 *	
	 * @param event
	 * @param agents
	 * @param objects
	 * @throws Exception 
	 */
	public void saveEvent(Event event, Map<Agent, AgentRole> agents, Map<AbstractFile, ObjectRole> objects) throws Exception{
		this.saveDBEntry(event);
		if(agents != null) for(Entry<Agent, AgentRole> entry : agents.entrySet()) {
			Agent agent = entry.getKey();
			this.saveDBEntry(agent);
			AgentEventLink agevLink = new AgentEventLink(agent, event, entry.getValue());
			this.saveDBEntry(agevLink);
		}
		if(objects != null) for(Entry<AbstractFile, ObjectRole> entry : objects.entrySet()) {
			AbstractFile object = entry.getKey();
			this.saveDBEntry(object);
			ObjectEventLink obevLink = new ObjectEventLink(object, event, entry.getValue());
			this.saveDBEntry(obevLink);
		}
	}

	public void saveDBEntry(DBEntry entity) throws Exception {
		logger.debug("Prepering to save " + entity.toString());
		if(entity instanceof TransferP){
			((TransferP) entity).getLocation();
		}
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {		
				saveDBEntry0(em, entity);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		//logger.info("Saving " + entity.toString());
	}
	
	@Deprecated
	public void cloneConceptualFiles(InformationPackage ip) throws Exception{
		
	}
	
	public void initConceptualFiles(InformationPackage ip) throws Exception{
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				File dataFolder = new File(ip.getDataFolder()); 
				createConceptualFiles(em, dataFolder, ip);
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
	}
	
	public void initConceptualFiles4Folder(InformationPackage ip, File folder) throws Exception{
		logger.info("for folder: " + folder.getAbsolutePath());
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				createConceptualFiles(em, folder, ip);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
	}
	
	private void createConceptualFiles(EntityManager em, File directory, InformationPackage ip) throws Exception{
		
		for(File child : directory.listFiles()){
			if(child.isDirectory()){
				createConceptualFiles(em, child, ip);
			}else{
				FileConcept cf1 = new FileConcept(ip);
				saveDBEntry0(em, cf1);
				
				FileInstance if1 = new FileInstance(cf1, child, ip);
				saveDBEntry0(em, if1);		
			}
		}
	}
	
	
	public void cloneDIPFileConcepts(InformationPackage source, InformationPackage target) throws Exception {
		logger.info("#### DIP-clone from= " + source.toLogString() + ", to= " + target.toLogString());
		
		long start = System.currentTimeMillis();
		
		if(!target.isPersistent()){
			throw new Exception("While cloning the file concepts, the target was null");
		}
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		
		// don't start a transaction, cause we rely on the resulting database entries in the following loop
		
		try {
				
			Map<Long, FileInstance> clonedInstancesMap = new HashMap<Long, FileInstance>();
			
			for(FileConcept concept : getFileConceptList0(em, source)){
				FileConcept newConcept = FileConcept.clone(concept, target);
				Boolean saveConcept = false;
				
				if(!em.getTransaction().isActive()) em.getTransaction().begin();
				saveDBEntry0(em, newConcept);
				em.getTransaction().commit();
				
				// FileInstance list will only contain instances with locations within the xxxx-xx-xx_archive folder
				List<FileInstance> instances = getDIPFileInstances0(em, concept);
				if(instances != null) for(FileInstance instance : instances){
					FileInstance newInstance = FileInstance.clone(instance, target, newConcept, null);
					newInstance.setLocation(instance.getLocation().replaceFirst("^[0-9]{4}-[0-9]{2}-[0-9]{2}_archive/", ""));
					
					em.getTransaction().begin();
					saveDBEntry0(em, newInstance);
					clonedInstancesMap.put(instance.getId(), newInstance);
					
					List<FileInstanceProperty> props = this.getFileInstancePropertyList(instance);
					if(props != null) for(FileInstanceProperty prop : props) {
						this.saveDBEntry0(em, prop.clone(newInstance.getId()));
					}
					// save the concept only, if there are FileInstances
					saveConcept = true;
					em.getTransaction().commit();
				}
				
				if(!saveConcept) {
					em.getTransaction().begin();
					this.deleteDBEntry(newConcept);
					em.getTransaction().commit();
				}
			}
			
			for(FileGroup group : getFileGroupList0(em, source)){
				FileGroup newGroup = FileGroup.clone(group, target);
				saveDBEntry0(em, newGroup);
				
				List<FileInstance> instances = getFileInstances0(em, group);
				for(FileInstance instance : instances){
					FileInstance newInstance = clonedInstancesMap.get(instance.getId());
					newInstance.setFileGroupId(newGroup.getId());
					saveDBEntry0(em, newInstance);
				}
				
			}
			
			
		} catch (Exception e) {
			/* there's no transaction...
			if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        */
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
	}
	
	private List<FileInstance> getDIPFileInstances0(EntityManager em, FileConcept concept){
		Query query = em.createQuery("from FileInstance where fileConceptId = :fileConceptId and location like '____-__-__\\_archive/%'");
		query.setParameter("fileConceptId", concept.getId());
		List<FileInstance> list = (List<FileInstance>)query.getResultList();
		return list;
	}
	
	
	public void cloneFileConcepts(InformationPackage source, InformationPackage target) throws Exception{
		
		logger.info("$$$$$ from= " + source.toLogString() + ", to= " + target.toLogString());
		
		long start = System.currentTimeMillis();
		
		if(!target.isPersistent()){
			throw new Exception("Cloning the file concepts the target was null");
		}
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				Map<Long, FileInstance> clonedInstancesMap = new HashMap<Long, FileInstance>();
				
				for(FileConcept concept : getFileConceptList0(em, source)){
					FileConcept newConcept = FileConcept.clone(concept, target);
					saveDBEntry0(em, newConcept);
					
					List<FileInstance> instances = getFileInstances0(em, concept);
					for(FileInstance instance : instances){
						FileInstance newInstance = FileInstance.clone(instance, target, newConcept, null);
						saveDBEntry0(em, newInstance);
						clonedInstancesMap.put(instance.getId(), newInstance);
						
						List<FileInstanceProperty> props = this.getFileInstancePropertyList(instance);
						if(props != null) for(FileInstanceProperty prop : props) {
							this.saveDBEntry0(em, prop.clone(newInstance.getId()));
						}
					}
				}
				
				for(FileGroup group : getFileGroupList0(em, source)){
					FileGroup newGroup = FileGroup.clone(group, target);
					saveDBEntry0(em, newGroup);
					
					List<FileInstance> instances = getFileInstances0(em, group);
					for(FileInstance instance : instances){
						FileInstance newInstance = clonedInstancesMap.get(instance.getId());
						newInstance.setFileGroupId(newGroup.getId());
						saveDBEntry0(em, newInstance);
					}
					
				}
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		
	}
	
	public Agent getAgentById(Long id){
		long start = System.currentTimeMillis();
		List<Agent> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from Agent where id = :id");
				query.setParameter("id", id);
				list = (List<Agent>)query.getResultList();
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return (list.isEmpty()) ? null : list.get(0);
	}
	
	public Agent getSoftwareAgentByName(String agentName){
		long start = System.currentTimeMillis();
		Agent agent = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from Agent where agentName = :agentName and agentType = :agentType");
				query.setParameter("agentName", agentName);
				query.setParameter("agentType", "software");
				List<Agent> list = query.getResultList();
				agent = (list.isEmpty()) ? null : list.get(0);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return agent;
	}
	
	public List<FileInstance> getFileInstanceList(InformationPackage ip){
		long start = System.currentTimeMillis();
		List<FileInstance> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				list = getFileInstance0(em, ip);
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return list;
	}
	
	private List<FileInstance> getFileInstance0(EntityManager em, InformationPackage ip){
		List<FileInstance> list;
		Query query = em.createQuery("from FileInstance where ipId = :ipId");
		// TODO:
		//Query query = em.createQuery("from FileInstance where ipId = :ipId and wfipType = :wfipType");
		//query.setParameter("wfipType", ip.getClass().getName());
		query.setParameter("ipId", ip.getId());
		list = (List<FileInstance>)query.getResultList();
		return list;
	}
	
	



	public List<FileInstance> getFileInstanceListWithoutTechMd(WorkflowIP ip) {
		if(ip != null) {
			List<FileInstance> list = null;
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					
					Query query = em.createQuery("from FileInstance where ipId = :ipId and hasTechMd = 0");
					// TODO:
					//Query query = em.createQuery("from FileInstance where ipId = :ipId and wfipType = :wfipType and hasTechMd = 0");
					//query.setParameter("wfipType", ip.getClass().getName());
					query.setParameter("ipId", ip.getId());
					list = (List<FileInstance>)query.getResultList();
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				em.close();
			}
			return list;
		}
		return null;
	}
	
	
	public List<FileInstance> getFileInstanceListWithTechMd(WorkflowIP ip) {
		if(ip != null) {
			List<FileInstance> list = null;
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					
					Query query = em.createQuery("from FileInstance where ipId = :ipId and hasTechMd = 1");
					// TODO:
					//Query query = em.createQuery("from FileInstance where ipId = :ipId and wfipType = :wfipType and hasTechMd = 0");
					//query.setParameter("wfipType", ip.getClass().getName());
					query.setParameter("ipId", ip.getId());
					list = (List<FileInstance>)query.getResultList();
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				em.close();
			}
			return list;
		}
		return null;
	}
	
	
	public List<FileInstanceProperty> getFileInstancePropertyList(FileInstance fi) {
		if(fi != null) {
			List<FileInstanceProperty> list = null;
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					
					Query query = em.createQuery("from FileInstanceProperty where fileInstanceId = :fileInstanceId");
					query.setParameter("fileInstanceId", fi.getId());
					list = (List<FileInstanceProperty>)query.getResultList();
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				em.close();
			}
			return list;
		}
		return null;
	}
	
	
	public List<ObjectEventLink> getObjectEventLinkList(Event event) {
		if(event != null) {
			List<ObjectEventLink> list = null;
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					
					Query query = em.createQuery("from ObjectEventLink where eventId = :eventId");
					query.setParameter("eventId", event.getId());
					list = (List<ObjectEventLink>)query.getResultList();
					
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				em.close();
			}
			
			return list;
		}
		return null;
	}


	public List<AgentEventLink> getAgentEventLinkList(Event event) {
		if(event != null) {
			List<AgentEventLink> list = null;
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					
					Query query = em.createQuery("from AgentEventLink where eventId = :eventId");
					query.setParameter("eventId", event.getId());
					list = (List<AgentEventLink>)query.getResultList();
					
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				em.close();
			}
			
			return list;
		}
		return null;
	}


	public List<Event> getEventList(WorkflowIP ip) {
		if(ip != null) {
			long start = System.currentTimeMillis();
			List<Event> list = null;
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					
					Query query = em.createQuery("from Event where wfipId = :wfipId");
					query.setParameter("wfipId", ip.getId());
					list = (List<Event>)query.getResultList();
					
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				em.close();
			}
			
			long diff = System.currentTimeMillis() - start;  
			logger.debug("[time(ms)=" + diff + "]");
			return list;
		}
		return null;
	}
	
	
	public Event getEvent(Long id) {
		if(id != null) {
			long start = System.currentTimeMillis();
			Event event = null;
			event = PersistenceManagerCore.getEntityManager().find(Event.class, id);
			long diff = System.currentTimeMillis() - start;  
			logger.debug("[time(ms)=" + diff + "]");
			return event;
		}
		return null;
	}
	
	
	/**
	 * 
	 * The the input parameter file is a folder, this method will return null.
	 * 
	 * @param ip
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public FileInstance getFileInstance(InformationPackage ip, File file) throws Exception{
		long start = System.currentTimeMillis();
		List<FileInstance> list = null;
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			String location = FileInstance.calculateLocation(ip, file);
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from FileInstance where ipId = :ipId AND location = :location");
				// TODO:
				//Query query = em.createQuery("from FileInstance where ipId = :ipId and wfipType = :wfipType and location = :location");
				//query.setParameter("wfipType", ip.getClass().getName());
				query.setParameter("ipId", ip.getId());
				query.setParameter("location", location);
				list = (List<FileInstance>)query.getResultList();
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return (list.isEmpty()) ? null : list.get(0);
	}
	
	
	/**
	 * 
	 * For recursive deletion of folders, list the contained file instances in the root folder
	 * 
	 * @param ip
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	public List<FileInstance> getFileInstancesInFolder(InformationPackage ip, File folder) throws Exception{
		long start = System.currentTimeMillis();
		List<FileInstance> list = null;
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			String location = FileInstance.calculateLocation(ip, folder);
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from FileInstance where ipId = :ipId AND location like :location");
				// TODO:
				//Query query = em.createQuery("from FileInstance where ipId = :ipId and wfipType = :wfipType and location like :location%");
				//query.setParameter("wfipType", ip.getClass().getName());
				query.setParameter("ipId", ip.getId());
				query.setParameter("location", location + "%");
				list = (List<FileInstance>)query.getResultList();
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return list;
	}
	
	public void deleteFileInstanceProperties(FileInstance fi) {
		if(fi != null) {
			EntityManager em = PersistenceManagerCore.getEntityManager();
			try {
				em.getTransaction().begin();
				try {
					Query query = em.createQuery("delete from FileInstanceProperty where fileInstanceId = :fileInstanceId");
					query.setParameter("fileInstanceId", fi.getId());
					query.executeUpdate();
					
					em.getTransaction().commit();
				} catch (Exception e) {
					if (em.getTransaction().isActive()) {
			            em.getTransaction().rollback();
			        }
					throw e;
				}
			}catch(Exception e) {
				throw e;
			}finally {
				em.close();
			}
		}
	}
	
	private void saveDBEntry0(EntityManager em, DBEntry entity) throws Exception {
		boolean isPersistent = entity.isPersistent();
		Date timeStamp = new Date();
		if(!isPersistent){
			entity.setCreationTime(timeStamp);	
		}
		entity.setLastChange(timeStamp);
		
		saveOrUpdate(em, entity);

		logger.info("Saved: " + entity.toString());
	}
	
	private void saveOrUpdate(EntityManager em, DBEntry entry){
		if(entry.isPersistent()){
			em.merge(entry);
		}else{
			em.persist(entry);
		}
	}
	
	
	/**
	 * This method remove an DBEntry from the database.
	 * @param entity
	 */
	public void deleteDBEntry(DBEntry entry) {
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				em.remove(em.contains(entry) ? entry : em.merge(entry));
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		logger.info("Entity removed: " + entry.toString());
	}
	
	public User getUser(String userName, String password){
		long start = System.currentTimeMillis();
		User user = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from User where userName = :userName AND password = :password");
				query.setParameter("userName", userName);
				query.setParameter("password", password);
				List<User> list = (List<User>)query.getResultList();
				user = (list.isEmpty()) ? null :list.get(0);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return user;
	}
	
	public List<User> getUserList(){
		long start = System.currentTimeMillis();
		List<User> list = new ArrayList<User>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from User");
				list = (List<User>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	/*******************************************************/
	/* Information Packages */
	/*******************************************************/
	
	public List<WorkflowIP> getTransferPList(){
		long start = System.currentTimeMillis();
		List<WorkflowIP> list = new ArrayList<WorkflowIP>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from TransferP");
				list = (List<WorkflowIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		Collections.sort(list, new SortInformationPackage());
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public List<ArchivalIP> getAipList(){
		long start = System.currentTimeMillis();
		List<ArchivalIP> list = new ArrayList<ArchivalIP>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ArchivalIP");
				list = (List<ArchivalIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		Collections.sort(list, new SortInformationPackage());
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public List<IANUSActivity> getIANUSActivitiesByProcessInstanceId(String  processInstanceId){
		List<IANUSActivity> list = new ArrayList<IANUSActivity>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from IANUSActivity where processInstanceId = :processInstanceId");
				query.setParameter("processInstanceId", processInstanceId);
				list = (List<IANUSActivity>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		//return list.isEmpty() ? null : list.get(0);
		return list;
	}
	
	public IANUSActivity getIANUSActivitiesByTaskId(String  taskId){
		List<IANUSActivity> list = new ArrayList<IANUSActivity>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from IANUSActivity where taskId = :taskId");
				query.setParameter("taskId", taskId);
				list = (List<IANUSActivity>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list.isEmpty() ? null : list.get(0);
	}
	
	/**
	 * This method search for workflow packages that are related to the given DataCollection identifier 'metadataId'.
	 * The second parameters 'ipClass' is used to filter the search by class (TransferP, SubmissionIP, DisseminationIP, ArchivalIP).
	 * If 'ipClass' is null, this method returns all possible workflow packages.
	 * 
	 * @param metadataId
	 * @param ipClass
	 * @return
	 */
	public List<WorkflowIP> getWorkflowPackageList(Long metadataId, Class<?> ipClass){
		List<WorkflowIP> list = new ArrayList<WorkflowIP>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				if(ipClass == null || ipClass.equals(TransferP.class)){
					Query query = em.createQuery("from TransferP where metadataId = :metadataId");
					query.setParameter("metadataId", metadataId);
					List<WorkflowIP> list0 = query.getResultList();
					list.addAll(list0);
				}
				
				if(ipClass == null || ipClass.equals(SubmissionIP.class)){
					Query query = em.createQuery("from SubmissionIP where metadataId = :metadataId");
					query.setParameter("metadataId", metadataId);
					List<WorkflowIP> list0 = query.getResultList();
					list.addAll(list0);
				}
				
				if(ipClass == null || ipClass.equals(DisseminationIP.class)){
					Query query = em.createQuery("from DisseminationIP where metadataId = :metadataId");
					query.setParameter("metadataId", metadataId);
					List<WorkflowIP> list0 = query.getResultList();
					list.addAll(list0);
				}
				
				if(ipClass == null || ipClass.equals(ArchivalIP.class)){
					Query query = em.createQuery("from ArchivalIP where metadataId = :metadataId");
					query.setParameter("metadataId", metadataId);
					List<WorkflowIP> list0 = query.getResultList();
					list.addAll(list0);
				}
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list;
	}
	
	
	public WorkflowIP getWorkflowIP(Long wfipId, Class<?> ipClass) {
		WorkflowIP result = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = null;
				if(ipClass == null || ipClass.equals(TransferP.class))
					query = em.createQuery("from TransferP where id = :id");
				if(ipClass == null || ipClass.equals(SubmissionIP.class))
					query = em.createQuery("from SubmissionIP where id = :id");
				if(ipClass == null || ipClass.equals(DisseminationIP.class))
					query = em.createQuery("from DisseminationIP where id = :id");
				if(ipClass == null || ipClass.equals(ArchivalIP.class))
					query = em.createQuery("from ArchivalIP where id = :id");
				
				if(query != null) { 
					query.setParameter("id", wfipId);
					List<WorkflowIP> list = query.getResultList();
					if(list.size() > 0) result = list.get(0);
				}
				em.getTransaction().commit();
			}catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		}catch (Exception e) {
			throw e;
		}finally {
			em.close();
		}
		return result;
	}
	
	
	public PreIngestReport getPreIngestReport(Long sipId){
		List<PreIngestReport> list = new ArrayList<PreIngestReport>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from PreIngestReport where sipId = :sipId");
				query.setParameter("sipId", sipId);
				list = (List<PreIngestReport>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list.isEmpty() ? null : list.get(0);
	}


	public AbstractFile getObject(ObjectEventLink link) {
		AbstractFile out = null;
		if(link != null) {
			EntityManager em = PersistenceManagerCore.getEntityManager();
			switch(link.getObjectType()) {
			case "FileInstance":
				out = em.find(FileInstance.class, link.getObjectId());
				break;
			case "FileGroup":
				out = em.find(FileGroup.class, link.getObjectId());
			}
		}
		return out;
	}
	
	
	/**
	 * Returns a ConceptFile. The input is the identifier of the ConceptFile. 
	 * 
	 * @param id
	 * @return
	 */
	public FileConcept getFileConcept(Long id){
		FileConcept concept = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				concept = getFileConcept0(em, id);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		if(concept != null){
			concept.setFileInstanceList(getFileInstances(concept));
		}
		return concept;
	}
	
	private FileConcept getFileConcept0(EntityManager em, Long id){
		Query query = em.createQuery("from FileConcept where id = :id");
		query.setParameter("id", id);
		List<FileConcept> list = (List<FileConcept>)query.getResultList();
		FileConcept concept = (list.isEmpty()) ? null : list.get(0);
		return concept;
	}
	
	public List<FileConcept> getFileConceptList(InformationPackage ip){
		List<FileConcept> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getFileConceptList0(em, ip);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list;
	}
	
	private List<FileConcept> getFileConceptList0(EntityManager em, InformationPackage ip){
		Query query = em.createQuery("from FileConcept where ipId = :ipId");
		// TODO:
		//Query query = em.createQuery("from FileConcept where ipId = :ipId and wfipType = :wfipType");
		//query.setParameter("wfipType", ip.getClass().getName());
		query.setParameter("ipId", ip.getId());
		List<FileConcept> list = (List<FileConcept>)query.getResultList();
		return list;
	}
	
	/**
	 * Returns a FileGroup. The input is the identifier of the FileGroup. 
	 * 
	 * @param id
	 * @return
	 */
	public FileGroup getFileGroup(Long id){
		FileGroup group = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from FileGroup where id = :id");
				query.setParameter("id", id);
				List<FileGroup> list = (List<FileGroup>)query.getResultList();
				group = (list.isEmpty()) ? null : list.get(0);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		if(group != null){
			group.setFileInstanceList(getFileInstances(group));
		}
		return group;
	}
	
	public List<FileGroup> getFileGroupList(InformationPackage ip){
		List<FileGroup> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getFileGroupList0(em, ip);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list;
	}
	
	private FileGroup getFileGroup0(EntityManager em, Long id){
		Query query = em.createQuery("from FileGroup where id = :id");
		query.setParameter("id", id);
		List<FileGroup> list = (List<FileGroup>)query.getResultList();
		FileGroup group = (list.isEmpty()) ? null : list.get(0);
		return group;
	}
	
	private List<FileGroup> getFileGroupList0(EntityManager em, InformationPackage ip){
		Query query = em.createQuery("from FileGroup where ipId = :ipId");
		// TODO:
		//Query query = em.createQuery("from FileGroup where ipId = :ipId and wfipType = :wfipType");
		//query.setParameter("wfipType", ip.getClass().getName());
		query.setParameter("ipId", ip.getId());
		List<FileGroup> list = (List<FileGroup>)query.getResultList();
		return list;
	}
	
	public List<FileInstance> getFileInstances(FileConcept fileConcept){
		List<FileInstance> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getFileInstances0(em, fileConcept);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list;
	}
	
	private List<FileInstance> getFileInstances0(EntityManager em, FileConcept fileConcept){
		Query query = em.createQuery("from FileInstance where fileConceptId = :fileConceptId");
		query.setParameter("fileConceptId", fileConcept.getId());
		List<FileInstance> list = (List<FileInstance>)query.getResultList();
		return list;
	}
	
	public List<FileInstance> getFileInstances(FileGroup fileGroup){
		List<FileInstance> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getFileInstances0(em, fileGroup);
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list;
	}
	
	private List<FileInstance> getFileInstances0(EntityManager em, FileGroup fileGroup){
		Query query = em.createQuery("from FileInstance where fileGroupId = :fileGroupId");
		query.setParameter("fileGroupId", fileGroup.getId());
		List<FileInstance> list = (List<FileInstance>)query.getResultList();
		return list;
	}
	
	public TransferP getTransfer(Long id){
		List<TransferP> list = new ArrayList<TransferP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from TransferP where id = :id");
				query.setParameter("id", id);
				list = (List<TransferP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return list.isEmpty() ? null : list.get(0);
	}
	
	public InformationPackage getIp(Long id){
		InformationPackage ip = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				List<InformationPackage> list = null;
				
				Query query = em.createQuery("from TransferP where id = :id");
				query.setParameter("id", id);
				list = (List<InformationPackage>)query.getResultList();
				
				if(list.isEmpty()){
					query = em.createQuery("from SubmissionIP where id = :id");
					query.setParameter("id", id);
					list = (List<InformationPackage>)query.getResultList();
					
					if(list.isEmpty()){
						query = em.createQuery("from DisseminationIP where id = :id");
						query.setParameter("id", id);
						list = (List<InformationPackage>)query.getResultList();
					}
					
					if(list.isEmpty()){
						query = em.createQuery("from ArchivalIP where id = :id");
						query.setParameter("id", id);
						list = (List<InformationPackage>)query.getResultList();
					}
				}
				
				ip = (list.isEmpty()) ? null : list.get(0);
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		return ip;
	}
	
	public BagitFile getBagitFile(Long bagitFileId){
		long start = System.currentTimeMillis();
		BagitFile bagit = null;
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from BagitFile where id = :id");
				query.setParameter("id", bagitFileId);
				List<BagitFile> list = (List<BagitFile>)query.getResultList();
				bagit = (!list.isEmpty()) ? list.get(0) : null;
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return bagit;
	}
	
	public RsyncUpload getRsyncUpload(Long id){
		long start = System.currentTimeMillis();
		RsyncUpload upload = null;
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from RsyncUpload where id = :id");
				query.setParameter("id", id);
				List<RsyncUpload> list = (List<RsyncUpload>)query.getResultList();
				upload = (!list.isEmpty()) ? list.get(0) : null;
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return upload;
	}
	
	public List<RsyncUpload> getOpenRsyncUploadList(InformationPackage ip) {
		if(ip == null) return null;
		List<RsyncUpload> result = null;
		long start = System.currentTimeMillis();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from RsyncUpload where wfipId = :wfipId and wfipType = :wfipType and status != 'closed'");
				query.setParameter("wfipId", ip.getId());
				query.setParameter("wfipType", ip.getClass().getName());
				result = (List<RsyncUpload>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		
		return result;
	}
	
	
	
	/**
	 * Query the BagIt file with a specific name, belonging to a specific tp
	 * 
	 * @param Long the id of the tp in BagIt
	 * @param String the name for the BagIt file
	 * @throws Exception
	 * 
	 * @return BagitFile
	 * 
	 * @author zoes
	 * @see UploadPage.java
	 */
	
	public BagitFile getBagitFileByName(Long tpId, String fileName){
		long start = System.currentTimeMillis();
		BagitFile bagit = null;
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {				
				Query query = em.createQuery("from BagitFile where fileName = :fileName and tpId = :tpId")
				.setParameter("fileName", fileName)
				.setParameter("tpId", tpId);
				List<BagitFile> list = (List<BagitFile>)query.getResultList();
				bagit = (!list.isEmpty()) ? list.get(0) : null;
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[time(ms)=" + diff + "]");
		return bagit;
	}
	
	
	public void deleteBagitFile(BagitFile bagIt, InformationPackage ip) throws Exception{
		
		if(bagIt != null){
			try {
				Services.getInstance().getStorageService().deleteUploadedBagItFolderInIP(ip, bagIt.getFileName());
				this.deleteDBEntry(bagIt);
				}	
			 catch (Exception e) {
				e.printStackTrace();
			}	
		}
		

	}
	
	public List<BagitFile> getBagitFileList(InformationPackage ip){
		long start = System.currentTimeMillis();
		List<BagitFile> list = new ArrayList<BagitFile>();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from BagitFile where tpId = :tpId");
				query.setParameter("tpId", ip.getId());
				list = (List<BagitFile>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public List<DisseminationIP> getDipList(SubmissionIP sip){
		List<DisseminationIP> list = new ArrayList<DisseminationIP>();
		long start = System.currentTimeMillis();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from DisseminationIP where sipId = :sipId");
				query.setParameter("sipId", sip.getId());
				list = (List<DisseminationIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getSipByTransferPId [transferPId=" + sip.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public List<SubmissionIP> getSip(TransferP tp){
		List<SubmissionIP> list = new ArrayList<SubmissionIP>();
		long start = System.currentTimeMillis();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from SubmissionIP where tpId = :tpId");
				query.setParameter("tpId", tp.getId());
				list = (List<SubmissionIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getSipByTransferPId [transferPId=" + tp.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public SubmissionIP getSip(Long id){
		List<SubmissionIP> list = new ArrayList<SubmissionIP>();
		long start = System.currentTimeMillis();
		
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from SubmissionIP where id = :id");
				query.setParameter("id", id);
				list = (List<SubmissionIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getSip [id=" + id + ", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	public List<PreArchivalIP> getPreAip(TransferP tp){
		
		long start = System.currentTimeMillis();
		List<PreArchivalIP> list = new ArrayList<PreArchivalIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from PreArchivalIP where tpId = :tpId");
				query.setParameter("tpId", tp.getId());
				list = (List<PreArchivalIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getPreAipByTpId [tpId=" + tp.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	
	
	public List<ArchivalIP> getAip(TransferP tp){
		long start = System.currentTimeMillis();
		List<ArchivalIP> list = new ArrayList<ArchivalIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ArchivalIP where tpId = :tpId");
				query.setParameter("tpId", tp.getId());
				list = (List<ArchivalIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[tpId=" + tp.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list;
	}
	
	public List<ArchivalIP> getAip(SubmissionIP sip) {
		long start = System.currentTimeMillis();
		List<ArchivalIP> list = new ArrayList<ArchivalIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ArchivalIP where sipId = :sipId");
				query.setParameter("sipId", sip.getId());
				list = (List<ArchivalIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[sipId=" + sip.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list;
	}
	
	public List<DisseminationIP> getDip(ArchivalIP aip) {
		long start = System.currentTimeMillis();
		List<DisseminationIP> list = new ArrayList<DisseminationIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from DisseminationIP where aipId = :aipId");
				query.setParameter("aipId", aip.getId());
				list = (List<DisseminationIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[aipId=" + aip.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list;
	}
	
	public Set<WorkflowIP> getDipByIanusIdentifier(String ianusIdentifier){
		Set<WorkflowIP> rs = new LinkedHashSet<WorkflowIP>();
		List<DataCollection> dcList = Services.getInstance().getMDService().getLWDataCollectionByIanusIdentifier(ianusIdentifier);
		
		if(!dcList.isEmpty()){
			for(DataCollection dc : dcList){
				List<WorkflowIP> list = getWorkflowPackageList(dc.getId(), DisseminationIP.class);
				for(WorkflowIP item : list){
					rs.add(item);
				}
			}
		}
		return rs;
	}
	
	public DisseminationIP getDip(Long dipId){
		long start = System.currentTimeMillis();
		List<DisseminationIP> list = new ArrayList<DisseminationIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from DisseminationIP where id = :dipId");
				query.setParameter("dipId", dipId);
				list = (List<DisseminationIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getDip [id=" + dipId + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return (!list.isEmpty()) ? list.get(0) : null;
	}
	
	public ArchivalIP getAip(Long aipId){
		long start = System.currentTimeMillis();
		List<ArchivalIP> list = new ArrayList<ArchivalIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from ArchivalIP where id = :aipId");
				query.setParameter("aipId", aipId);
				list = (List<ArchivalIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getAip [id=" + aipId + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return (!list.isEmpty()) ? list.get(0) : null;
	}
	
	public List<DisseminationIP> getDip(TransferP tp){
		long start = System.currentTimeMillis();
		List<DisseminationIP> list = new ArrayList<DisseminationIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from DisseminationIP where tpId = :tpId");
				query.setParameter("tpId", tp.getId());
				list = (List<DisseminationIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getDip [tpId=" + tp.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public List<DisseminationIP> getDip(SubmissionIP sip){
		long start = System.currentTimeMillis();
		List<DisseminationIP> list = new ArrayList<DisseminationIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from DisseminationIP where sipId = :sipId");
				query.setParameter("sipId", sip.getId());
				list = (List<DisseminationIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getDip [sipId=" + sip.getId() + ", size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public List<SubmissionIP> getSipList(int maxResults){
		long start = System.currentTimeMillis();
		List<SubmissionIP> list = null;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = null;
				//ORDER by lastChange DESC
				query = em.createQuery("from SubmissionIP");
				
				if(maxResults > 1){
					query.setMaxResults(maxResults);
				}
				list = (List<SubmissionIP>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		Collections.sort(list, new SortInformationPackage());
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	/**
	 * Returns a list of DIP sorted by the attribute dataPresentation of the associated DataCollection.
	 * @param ipState
	 * @param maxResults
	 * @return
	 */
	public List<DisseminationIP> getDipList(String ipState, int maxResults){
		long start = System.currentTimeMillis();
		
		List<DataCollection> dcList = Services.getInstance().getMDService().getLWDataCollectionList();
		//printDcList(dcList);
		Collections.sort(dcList, new SortDataCollection());
		//printDcList(dcList);
		
		List<DisseminationIP> resultList = new ArrayList<DisseminationIP>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				//getting the list of DIP filtered by state
				Query queryDIP = null;
				if (StringUtils.isEmpty(ipState)) {
					queryDIP = em.createQuery("from DisseminationIP");
				}else{
					queryDIP = em.createQuery("from DisseminationIP where state = :state ");
					//queryDIP = em.createQuery("from DisseminationIP where state = :state ORDER by presentationDate DESC");
					queryDIP.setParameter("state", ipState);
				}
				List<DisseminationIP> dipList = (List<DisseminationIP>)queryDIP.getResultList();
				
				
				for(DataCollection dc : dcList){
					DisseminationIP dip = getDipFromDc(dc, dipList);
					if(dip != null && !resultList.contains(dip)){
						resultList.add(dip);
					}
				}
				
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("[size="+ resultList.size() +", time(ms)=" + diff + "]");
		return resultList;
	}
	
	private static void printDcList(List<DataCollection> list){
		System.out.println();
		System.out.println("**************************+");
		for(DataCollection dc : list){
			System.out.println("\t" + dc);
		}
		System.out.println();
	}
	
	public static DisseminationIP getDipFromDc(DataCollection dc, List<DisseminationIP> dipList){
		for(DisseminationIP dip : dipList){
			if(dip.getMetadataId().equals(dc.getId())){
				return dip;
			}
		}
		return null;
	} 
	
	public void deleteIPRecursive(InformationPackage ip) throws Exception{
		/*
		 * As agreed with fschafer on 2018-01-08, the related IPs shall not be deleted recursively any more
		if(ip instanceof TransferP){
			for(SubmissionIP sip : getSip((TransferP)ip)){
				deleteIPRecursive(sip);
			}
		}else if(ip instanceof SubmissionIP){
			for(ArchivalIP aip : getAip((SubmissionIP)ip)){
				deleteIPRecursive(aip);
			}
		}else if(ip instanceof ArchivalIP){
			for(DisseminationIP dip : getDip((ArchivalIP)ip)){
				deleteIPRecursive(dip);
			}
		}
		*/
		deleteIP(ip);
	}
	
	public void deleteIP(InformationPackage ip) throws Exception{
		if(ip instanceof WorkflowIP){
			WorkflowIP wfIp = (WorkflowIP) ip;
			RuntimeService runtimeService = Services.getInstance().getProcessEngineService().getProcessEngine().getRuntimeService();
			try {
				if(wfIp.getProcessInstanceId() != null && (wfIp.isInProgress() || wfIp.isNotStarted()) ){
					runtimeService.deleteProcessInstance(
							((WorkflowIP) ip).getProcessInstanceId(), 
							ip.getClass().getSimpleName() + " " + ip.getId() + " related to this process is deleted.");	
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		logger.info("Deleting " + ip.toString());
		Services.getInstance().getStorageService().deletePhysicalInformationPackage(ip);
		this.deleteFileInstances(ip);
		this.deleteDBEntry(ip);
	}
	
	/**
	 * This method remove all FileInstances associated to the given ip.
	 * If some FileInstance is connected to fileConcept X and X has no more FileInstances,
	 * there are no more reason to store the fileConcept and it will be deleted as well.
	 * 
	 * @param ip
	 * @throws Exception 
	 */
	private void deleteFileInstances(InformationPackage ip) throws Exception{
		logger.info("Deleting file instances (and concepts) for " + ip);
		
		long start = System.currentTimeMillis();
		double total = 0;
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				List<FileInstance> instances = getFileInstance0(em, ip);
				total = instances.size();
				double count = 0;
				for(FileInstance instance : instances){
					deleteFileInstance(instance);
					count++;
					if(count % 100 == 0){
						double percent = ((count / total) * 100.0);
						logger.info(ip + df.format(percent) + "%");
					}
				}
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		//List<FileInstance> instances = getFileInstanceList(ip);
		//this.deleteFileInstanceList(instances);
		long diff = System.currentTimeMillis() - start;  
		logger.info("[items.size=" + total + ", time(ms)=" + diff + "]");
	}
	
	
	public void deleteFileInstance(FileInstance fileInstance) throws Exception{
		if(fileInstance == null) return;
		
		this.deleteFileInstanceProperties(fileInstance);
		
		//deleting the associated concept, if it has no more file instances
		FileConcept concept = getFileConcept(fileInstance.getFileConceptId());
		if(concept == null){
			try {
				throw new Exception("The File Instance has no concept " + fileInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if(getFileInstances(concept).size() <= 1){
			deleteDBEntry(concept);
		}
		
		//deleting the associated group, if it has no more file instances
		FileGroup group = getFileGroup(fileInstance.getFileGroupId());
		if(group != null){
			if(getFileInstances(group).size() <= 1){
				deleteDBEntry(group);
			}
		}
		
		//deleting the file instance		
		deleteDBEntry(fileInstance);
	}
	
	/*******************************************************/
	/* Locations */
	/*******************************************************/
	
	/**
	 * Returns all locations from the database
	 * @return
	 */
	public List<Location> getLocations(){
		long start = System.currentTimeMillis();
		List<Location> list = new ArrayList<Location>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from Location");
				list = (List<Location>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
    	long diff = System.currentTimeMillis() - start;  
		logger.debug("getLocations [size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list;
	}
	
	public Location getLocation(Long locationId){
		
		List<Location> list = new ArrayList<Location>();
		long start = System.currentTimeMillis();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from Location where id = :id");
				query.setParameter("id", locationId);
				list = (List<Location>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getLocation [locationId="+ locationId +", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	public ActivityOutput getActivityOutput(Long  ianusActivityId){
		long start = System.currentTimeMillis();
		List<ActivityOutput> list = new ArrayList<ActivityOutput>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {

				Query query = em.createQuery("from ActivityOutput where ianusActivityId = :ianusActivityId");
				query.setParameter("ianusActivityId", ianusActivityId);
				list = (List<ActivityOutput>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getActivityOutput [ianusActivityId="+ ianusActivityId +", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list.isEmpty() ? null : list.get(0);
	}
	
	public ActivityOutput getActivityOutput(Long wfipId, String activityId){
		long start = System.currentTimeMillis();
		List<ActivityOutput> list = new ArrayList<ActivityOutput>();
		EntityManager em = PersistenceManagerCore.getEntityManager();
		try {
			em.getTransaction().begin();
			try {

				Query query = em.createQuery("from ActivityOutput where activityId = :activityId AND wfipId = :wfipId");
				query.setParameter("activityId", activityId);
				query.setParameter("wfipId", wfipId);
				list = (List<ActivityOutput>)query.getResultList();
				em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive()) {
		            em.getTransaction().rollback();
		        }
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.debug("getActivityOutput [activityId="+ activityId +", size="+ list.size() +", time(ms)=" + diff + "]");
		
		return list.isEmpty() ? null : list.get(0);
	}
	
}