package de.ianus.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import de.ianus.metadata.bo.AccessRight;
import de.ianus.metadata.bo.CollectionFile;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.Element;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.MDEntry;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.resource.Publication;
import de.ianus.metadata.bo.resource.Reference;
import de.ianus.metadata.bo.resource.RelatedResource;
import de.ianus.metadata.bo.utils.BOUtils;
import de.ianus.metadata.bo.utils.BOUtils.SourceClass;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.FileFormat;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.bo.utils.Language;
import de.ianus.metadata.bo.utils.Place;
import de.ianus.metadata.bo.utils.PreservationEvent;
import de.ianus.metadata.bo.utils.ResourceType;
import de.ianus.metadata.bo.utils.Software;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.Time;
import de.ianus.metadata.bo.utils.TimeSpan;


public class MetadataService {
	
	private static final Logger logger = LogManager.getLogger(MetadataService.class);
	
	private static MetadataService instance = null;
	
	public static MetadataService getInstance(){
		if(instance == null){
			instance = new MetadataService();
		}
		return instance;
	}
	
	public List<DataCollection> getLWDataCollectionByIanusIdentifier(String ianusIdentifier){
		
		long start = System.currentTimeMillis();
		List<DataCollection> rs = new ArrayList<DataCollection>();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				Query query = 
				em.createQuery("from Identifier where sourceClass = :sourceClass AND value = :value AND ( type = :collectionId OR type = :recordId)");
				
				query.setParameter("sourceClass", BOUtils.SourceClass.DataCollection);
				query.setParameter("collectionId", Identifier.Internal.ianus_collno.id);
				query.setParameter("recordId", Identifier.Internal.ianus_mdrecord.id);
				query.setParameter("value", ianusIdentifier);
				List<Identifier> idList = query.getResultList();
				
				for(Identifier item : idList){
					Query query0 = em.createQuery("from DataCollection where id = :id");
					query0.setParameter("id", item.getSourceId());
					List<DataCollection> dcList = query0.getResultList();
					if(!dcList.isEmpty()){
						rs.add(dcList.get(0));
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
		logger.info("[time(ms)=" + diff + "]");
		return rs;
	
		
	}
	
	public CloneResult cloneDataCollection0(DataCollection dc) throws Exception{
		DataCollection oldDc = getDataCollection(dc.getId());
		DataCollection newDc = new DataCollection(oldDc);
		CloneResult result = new CloneResult();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				this.saveDBEntry0(em, newDc);
				
				//this map stores the old actor ID and its corresponding new id for the new DC.
				Map<Long, Long> personMap = new HashMap<Long, Long>();
				Map<Long, Long> institutionMap = new HashMap<Long, Long>();
				
				for(Actor entry : oldDc.getActorMap().values()){
					if(entry instanceof Person){
						Person oldPerson = (Person)entry;
						Person newPerson = Person.clone(oldPerson, newDc);
						this.saveDBEntry0(em, newPerson);
						personMap.put(oldPerson.getId(), newPerson.getId());
						
						for(ElementOfList elemOfList : oldPerson.getSubDisciplineList()){
							ElementOfList newElemOfList = ElementOfList.clone(elemOfList, newPerson.getId());
							this.saveDBEntry0(em, newElemOfList);
						}
						
						for(Identifier oldId : oldPerson.getExternalIdList()){
							Identifier newId = Identifier.clone(oldId, newPerson.getId());
							this.saveDBEntry0(em, newId);
						}
						result.actorMap.put(oldPerson, newPerson);
						
					}else if(entry instanceof Institution){
						Institution oldInstitution = (Institution)entry;
						Institution newInstitution = Institution.clone(oldInstitution, newDc);
						this.saveDBEntry0(em, newInstitution);
						institutionMap.put(oldInstitution.getId(), newInstitution.getId());
						
						for(Identifier oldId : oldInstitution.getExternalIdList()){
							Identifier newId = Identifier.clone(oldId, oldInstitution.getId());
							this.saveDBEntry0(em, newId);
						}
						result.actorMap.put(oldInstitution, newInstitution);
					}
				}
				
				
				for(Set<TextAttribute> set : oldDc.getAttributeMap().values()){
					for(TextAttribute entry : set){
						TextAttribute newEntry = TextAttribute.clone(entry, newDc.getId());
						this.saveDBEntry0(em, newEntry);
					}
				}
				
				for(AccessRight entry : oldDc.getAccessRightList()){
					AccessRight newEntry = AccessRight.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(ElementOfList entry : oldDc.getMainDisciplineList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getSubDisciplineList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getMainContentList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getSubContentList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getMainMethodList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getSubMethodList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getDataCategoryList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getClassificationList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getResourceTypeList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				for(ElementOfList entry : oldDc.getReasonDataProtectionList()){
					ElementOfList newEntry = ElementOfList.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(Time entry : oldDc.getTimeList()){
					Time newEntry = Time.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
					for(TextAttribute textAtt : entry.getCommentList()){
						TextAttribute newTextAtt = TextAttribute.clone(textAtt, newEntry.getId());
						this.saveDBEntry0(em, newTextAtt);
					}
					
					if(entry.getMainPeriod() != null){
						ElementOfList newMainPeriod = ElementOfList.clone(entry.getMainPeriod(), newEntry.getId());
						this.saveDBEntry0(em, newMainPeriod);
					}
					
					if(entry.getSubPeriod() != null){
						ElementOfList newSubPeriod = ElementOfList.clone(entry.getSubPeriod(), newEntry.getId());
						this.saveDBEntry0(em, newSubPeriod);	
					}
				}
				
				for(Language entry : oldDc.getCollectionLanguageList()){
					Language newEntry = Language.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(TimeSpan entry : oldDc.getDataGenerationList()){
					TimeSpan newEntry = TimeSpan.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(ActorRole entry : oldDc.getRoleList()){
					Long newActorId = getNewActorId(personMap, institutionMap, entry.getActorId(), entry.getActorClass());
					ActorRole newEntry = ActorRole.clone(entry, newActorId, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(Identifier entry : oldDc.getInternalIdList()){
					Identifier newEntry = Identifier.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(Identifier entry : oldDc.getExternalIdList()){
					Identifier newEntry = Identifier.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
				}
				
				for(Place entry : oldDc.getPlaceList()){
					Place newEntry = Place.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newEntry);
					
					for(Set<TextAttribute> set : entry.getAttributeMap().values()){
						for(TextAttribute textAtt : set){
							TextAttribute newTextAtt = TextAttribute.clone(textAtt, newEntry.getId());
							this.saveDBEntry0(em, newTextAtt);
						}
					}
					
					//here we clone the identifier list. In form is called Gazetteer
					for(ElementOfList oldId : entry.getIdentifierList()){
						ElementOfList newId = ElementOfList.clone(oldId, newEntry.getId());
						this.saveDBEntry0(em, newId);
					}
					
				}
				
				for(Reference entry : oldDc.getReferenceList()){
					Reference newItem = Reference.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newItem);
				}
				
				for(Language entry : oldDc.getMetadataLanguageList()){
					Language newItem = Language.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newItem);
				}
				
				for(RelatedResource entry : oldDc.getRelatedResourceList()){
					RelatedResource newItem = RelatedResource.clone(entry, newDc.getId());
					this.saveDBEntry0(em, newItem);
					
					Identifier newId = Identifier.clone(entry.getIdentifier(), newItem.getId());
					this.saveDBEntry0(em, newId);
					
					Institution newInstitution = Institution.clone(entry.getInstitution(), newItem);
					this.saveDBEntry0(em, newInstitution);
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
		result.dc = newDc;
		return result;
	}
	
	private Long getNewActorId(Map<Long, Long> personMap, Map<Long, Long> institutionMap, Long oldActorId, BOUtils.ActorClass actorClass){
		if(BOUtils.ActorClass.Person.equals(actorClass)){
			return personMap.get(oldActorId);
		}else if(BOUtils.ActorClass.Institution.equals(actorClass)){
			return institutionMap.get(oldActorId);
		}
		return null;
	}

	public void deleteDataCollection(DataCollection dc) throws Exception{
		
		if(dc == null){
			throw new Exception("Exception trying to delete a DataCollection. DataCollection is null.");
		}
		DataCollection dc0 = getDataCollection(dc.getId());
		if(dc0 == null){
			throw new Exception("Exception trying to delete a DataCollection. DataCollection with ID=" + dc.getId() + " does not exist in the DB");
		}
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				for(Set<TextAttribute> set : dc0.getAttributeMap().values()){
					for(TextAttribute entry : set){
						deleteDBEntry0(em, entry);
					}
				}
				for(AccessRight entry : dc0.getAccessRightList()){
					deleteDBEntry0(em, entry);
				}
				for(Actor entry : dc0.getActorMap().values()){
					deleteDBEntry0(em, entry);
				}
				
				for(ElementOfList entry : dc0.getMainDisciplineList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getSubDisciplineList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getMainContentList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getSubContentList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getMainMethodList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getSubMethodList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getDataCategoryList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getClassificationList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getResourceTypeList()){
					deleteDBEntry0(em, entry);
				}
				for(ElementOfList entry : dc0.getReasonDataProtectionList()){
					deleteDBEntry0(em, entry);
				}
				for(Time entry : dc0.getTimeList()){
					deleteDBEntry0(em, entry);
				}
				for(Language entry : dc0.getCollectionLanguageList()){
					deleteDBEntry0(em, entry);
				}
				for(TimeSpan entry : dc0.getDataGenerationList()){
					deleteDBEntry0(em, entry);
				}
				
				//**
				for(ActorRole entry : dc0.getRoleList()){
					deleteDBEntry0(em, entry);
				}
				for(Identifier entry : dc0.getInternalIdList()){
					deleteDBEntry0(em, entry);
				}
				for(Identifier entry : dc0.getExternalIdList()){
					deleteDBEntry0(em, entry);
				}
				for(Place entry : dc0.getPlaceList()){
					deleteDBEntry0(em, entry);
				}
				for(Reference entry : dc0.getReferenceList()){
					deleteDBEntry0(em, entry);
				}
				for(Language entry : dc0.getMetadataLanguageList()){
					deleteDBEntry0(em, entry);
				}
				for(RelatedResource entry : dc0.getRelatedResourceList()){
					deleteDBEntry0(em, entry);
				}
				
				deleteDBEntry0(em, dc0);
				
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
	
	public void saveDataCollection(DataCollection dc) throws Exception {
		logger.info("Prepering to save " + dc.toString());
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				saveDataCollection0(em, dc);
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
		logger.info("Saving " + dc.toString());
	}
	
	public List<Publication> getPublicationList() throws Exception{
		long start = System.currentTimeMillis();
		List<Publication> list = new ArrayList<Publication>();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from Publication");
				list = query.getResultList();
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
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}
	
	public List<Reference> getReferenceListRelated2Publication(Long pubId) throws Exception{
		long start = System.currentTimeMillis();
		List<Reference> list = new ArrayList<Reference>();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				//XXX
				Query query = em.createQuery("from Reference where publicationId = :publicationId");
				query.setParameter("publicationId", pubId);
				list = query.getResultList();
				for(Reference ref : list){
					ref.setPublication(getPublication(em, ref.getPublicationId()));
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
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}
	
	public List<Publication> searchPublication(String term){
		List<Publication> rs = new ArrayList<Publication>();
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			
			FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
			em.getTransaction().begin();
			try {
				
				//String term = searchArray2String(array);
				QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Publication.class).get();
				org.apache.lucene.search.Query luceneQuery = qb
						.keyword().wildcard()
						.onFields("author", "title", "collectionTitle", "collectionEditor", "publisher", "city")
						.matching("*" + term + "*")
						.createQuery();
				
				// wrap Lucene query in a javax.persistence.Query
				Query jpaQuery =
				    fullTextEntityManager.createFullTextQuery(luceneQuery, Publication.class);

				// execute search
				rs = jpaQuery.getResultList();
				
				logger.info("Searching [" + term + "] size: " + rs.size());
				
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
		return rs;
	}
	
	public void updateIndices(){
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String searchArray2String(String[] array){
		StringBuilder sb = new StringBuilder();
		
		for(String term : array){
			if(StringUtils.isNotEmpty(term)){
				if(sb.length() > 0){
					sb.append(" ");
				}
				sb.append(term);
			}
		}
		return sb.toString();
	}
	
	public void saveEntry(MDEntry entry) throws Exception {
		logger.info("Prepering to save " + entry.toString());
		
		if(entry instanceof ElementOfList || entry instanceof Identifier){
			Element item = (Element)entry;
			if(!item.isConsistent()){
				logger.error(item.toString());
				throw new Exception(entry.getClass().getName() + " is not consistent. Either the contentType, the sourceClass or the sourceId is/are Empty");				
			}
		}
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				saveDBEntry0(em, entry);
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
		logger.info("Saving " + entry.toString());
	}
	
	public void deletePlace(Place place) throws Exception{
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				for(TextAttribute att : place.getAccuracyDescriptionList()){
					deleteDBEntry0(em, att);
				}
				for(TextAttribute att : place.getCityList()){
					deleteDBEntry0(em, att);
				}
				for(TextAttribute att : place.getCountryList()){
					deleteDBEntry0(em, att);
				}
				for(TextAttribute att : place.getFreeDescriptionList()){
					deleteDBEntry0(em, att);
				}
				for(TextAttribute att : place.getGreaterRegionList()){
					deleteDBEntry0(em, att);
				}
				for(TextAttribute att : place.getHistoricalNameList()){
					deleteDBEntry0(em, att);
				}
				for(ElementOfList ele : place.getIdentifierList()){
					deleteDBEntry0(em, ele);
				}
				deleteDBEntry0(em, place);
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
	
	private void deleteDBEntry0(EntityManager em, MDEntry entry) throws Exception {
		em.remove(em.contains(entry) ? entry : em.merge(entry));
		logger.info("Entry removed: " + entry.toString());
	}
	
	/**
	 * This method remove an MDEntry from the database.
	 * @param entry
	 * @throws Exception when the entry is not persistent
	 */
	public void deleteDBEntry(MDEntry entry) throws Exception {
		
		if(!entry.isPersistent()){
			throw new Exception("It is not possible to delete a entry that is not persistent");
		}
		
		EntityManager em = PersistenceManager.getEntityManager();
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
		logger.info("entry removed: " + entry.toString());
	}
	
	/**
	 * This method removes all instance of the PersonRole, whose type meets the input <i>type</i>.
	 * @param dc
	 * @param person
	 * @param type
	 * @throws Exception
	 */
	public void deleteRoles4SourceAndActor(IANUSEntity entity, Actor actor, ActorRole.Type roleType) throws Exception{
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				BOUtils.SourceClass sourceClass = (entity instanceof DataCollection) ? BOUtils.SourceClass.DataCollection : BOUtils.SourceClass.CollectionFile;
				Set<ActorRole> roleList = getRoleList4SourceAndActor(em, entity.getId(), sourceClass, actor);
				for(ActorRole role : roleList){
					if(role.getTypeId().equals(roleType.id)){
						em.remove(em.contains(role) ? role : em.merge(role));
						logger.info("Deleting " + role.toString());	
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
	}
	
	/**
	 * This method deletes a actor and all its roles
	 * @param dc
	 * @param actor
	 * @param roleType
	 * @throws Exception
	 */
	public void deleteActor(Actor actor) throws Exception{
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				Set<ActorRole> roleList = getRoleList4Actor(em, actor);
				for(ActorRole role : roleList){
					em.remove(em.contains(role) ? role : em.merge(role));
					logger.info("Deleting " + role.toString());
				}
				em.remove(em.contains(actor) ? actor : em.merge(actor));
				logger.info("Deleting " + actor.toString());
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
	
	private void saveDataCollection0(EntityManager em, DataCollection dc) throws Exception{
		saveDBEntry0(em, dc);
		
	}
	
	private void saveDBEntry0(EntityManager em, MDEntry entry) throws Exception {
		boolean isPersistent = entry.isPersistent();
		Date timeStamp = new Date();
		if(!isPersistent){
			entry.setCreationTime(timeStamp);	
		}
		entry.setLastChange(timeStamp);
		//em.saveOrUpdate(entry);
		if(!entry.isPersistent()){
			em.persist(entry);
		}else{
			em.merge(entry);
		}
	    
	    logger.info("Entry saved: " + entry.toString());
	}
	
	public Set<PreservationEvent> getPreservationEventList(IANUSEntity entity) throws Exception{
		long start = System.currentTimeMillis();
		Set<PreservationEvent> list = new LinkedHashSet<PreservationEvent>();		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getPreservationEventList0(em, entity);
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
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}
	
	public Set<Actor> getActorList(DataCollection dc) throws Exception{
		long start = System.currentTimeMillis();
		Set<Actor> list = new LinkedHashSet<Actor>();		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getActorList(em, dc.getId(), BOUtils.SourceClass.DataCollection);
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
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}
	
	public Set<ActorRole> getRoleList4Actor(Actor actor) throws Exception{
		long start = System.currentTimeMillis();
		Set<ActorRole> list = new LinkedHashSet<ActorRole>();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				list = getRoleList4Actor(em, actor);
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
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}
	
	/*
	public Set<Publication> getPublicationList(DataCollection dc) throws Exception{
		long start = System.currentTimeMillis();
		Set<Publication> list;
		
		final EntityManager em = MDHibernateUtil.getSession();
		try {
			final Transaction transaction = em.beginTransaction();
			try {
				list = getPublicationList(em, dc.getId());
			} catch (Exception e) {
				transaction.rollback();
			    throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally{
			MDHibernateUtil.closeSession();
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}*/
	
	private MDEntry getMDEntry(Long id, Class classObject) throws Exception{
		long start = System.currentTimeMillis();
		MDEntry result = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				Query query = em.createQuery("from " +  classObject.getName() + " where id = :id");
				query.setParameter("id", id);
				List<MDEntry> list = (List<MDEntry>)query.getResultList();
				result = (list.isEmpty()) ? null : list.get(0);
				
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
		logger.info("[time(ms)=" + diff + "]");
		return result;
	}
	
	public DataCollection getDataCollection(Long dcId) throws Exception{
		long start = System.currentTimeMillis();
		DataCollection dc = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				
				Query query = em.createQuery("from DataCollection where id = :id");
				query.setParameter("id", dcId);
				List<DataCollection> list = (List<DataCollection>)query.getResultList();
				dc = (list.isEmpty()) ? null : list.get(0);
				if(dc != null){
					getDcContent(em, dc);
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
		logger.info("[time(ms)=" + diff + "]");
		return dc;
	}
	
	public Set<CollectionFile> getCollectionFileList(Long dcId) throws Exception{
		long start = System.currentTimeMillis();
		Set<CollectionFile> list = new LinkedHashSet<CollectionFile>();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				list = getCollectionFileList0(em, dcId, BOUtils.SourceClass.DataCollection);
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
		logger.info("[time(ms)=" + diff + "]");
		return list;
	}
	
	public CollectionFile getCollectionFile(Long fileId) throws Exception{
		long start = System.currentTimeMillis();
		CollectionFile file = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				file = getCollectionFile0(em, fileId);
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
		logger.info("[time(ms)=" + diff + "]");
		return file;
	}
	
	public Actor getActor(Long actorId, BOUtils.ActorClass actorClass) throws Exception{
		long start = System.currentTimeMillis();
		Actor actor = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				actor = getActor(em, actorId, actorClass);
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
		logger.info("[time(ms)=" + diff + "]");
		return actor;
	}
	
	public Time getTime(Long timeId) throws Exception{
		long start = System.currentTimeMillis();
		Time time = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				time = getTime(em, timeId);
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
		logger.info("[time(ms)=" + diff + "]");
		return time;
	}
	
	private void getDcContent(EntityManager em, DataCollection dc) throws Exception{
		 
		//Title
		dc.getAttributeMap().put(TextAttribute.ContentType.title, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.title));
		dc.getAttributeMap().put(TextAttribute.ContentType.alternativeTitle, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.alternativeTitle));
		
		//Description
		dc.getAttributeMap().put(TextAttribute.ContentType.summary, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.summary));
		dc.getAttributeMap().put(TextAttribute.ContentType.projectDescription, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.projectDescription));
		dc.getAttributeMap().put(TextAttribute.ContentType.dataCollectionDescription, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.dataCollectionDescription));
		dc.getAttributeMap().put(TextAttribute.ContentType.descriptionDataProtection, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.descriptionDataProtection));
		
		//loading url list
		//dc.setUrlList(getTextAttList(em, dc, TextAttribute.ContentType.url));
		dc.getAttributeMap().put(TextAttribute.ContentType.url, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.url));
		
		dc.getAttributeMap().put(TextAttribute.ContentType.motivation, getTextAttList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TextAttribute.ContentType.motivation));
		
		//Loading keywords
		dc.setMainDisciplineList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.mainDiscipline));
		dc.setSubDisciplineList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.subDiscipline));
		dc.setMainContentList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.mainContent));
		dc.setSubContentList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.subContent));
		dc.setMainMethodList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.mainMethod));
		dc.setSubMethodList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.subMethod));
		dc.setDataCategoryList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.dataCategory));
		dc.setClassificationList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.classification));
		dc.setResourceTypeList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.resourceType));
		dc.setReasonDataProtectionList(getElementsOfList(em, dc.getId(), BOUtils.SourceClass.DataCollection, ElementOfList.ContentType.reasonDataProtection));
		
		//loading access rights
		dc.setAccessRightList(getAccessRights(em, dc));
		
		//loading data languages
		dc.setCollectionLanguageList(getLanList(em, dc.getId(), BOUtils.SourceClass.DataCollection, Language.ContentType.collection_language));
		
		//loading resource types
		//dc.setResourceTypeList(getResourceTypeList(em, dc));
		
		//loading file formats
		//replace by TextAttribute.dataCategory
		//dc.setFileFormatList(getFileFormatList(em, dc.getId(), BOUtils.SourceClass.DataCollection));
		
		//dc.setAlternativeRepresentationList(getAlternativeRepresentationList(em, dc.getId(), BOUtils.SourceClass.DataCollection));
		
		dc.setTimeList(getTimeList(em, dc.getId(), BOUtils.SourceClass.DataCollection));
		
		dc.setActorList(getActorList(em, dc.getId(), BOUtils.SourceClass.DataCollection));
		
		dc.setDataGenerationList(getTimeSpanList(em, dc.getId(), BOUtils.SourceClass.DataCollection, TimeSpan.ContentType.data_generation));
		
		//IANUSEntity *************
		
		//loading person
		dc.setRoleList(getRoleList4Source(em, dc.getId(), BOUtils.SourceClass.DataCollection));
		
		//loading internal ids
		dc.setInternalIdList(getIdentifierList(em, dc.getId(), BOUtils.SourceClass.DataCollection, Identifier.ContentType.internal_id));
				
		//loading external ids
		dc.setExternalIdList(getIdentifierList(em, dc.getId(), BOUtils.SourceClass.DataCollection, Identifier.ContentType.external_id));
			
		//loading places
		dc.setPlaceList(getPlaceList(em, dc.getId(), BOUtils.SourceClass.DataCollection));
		
		//loading references
		dc.setReferenceList(getReferencesList(em, dc.getId(), SourceClass.DataCollection));
		
		//loading metadata languages
		dc.setMetadataLanguageList(getLanList(em, dc.getId(), BOUtils.SourceClass.DataCollection, Language.ContentType.metadata_language));
		
		//loading related sources
		dc.setRelatedResourceList(getRelatedResourceList(em, dc.getId(), SourceClass.DataCollection));
	}
	
	private CollectionFile getCollectionFile0(EntityManager em, Long id) throws Exception{
		long start = System.currentTimeMillis();
		Query query = em.createQuery("from CollectionFile where id = :id");
		query.setParameter("id", id);
		List<CollectionFile> list = query.getResultList();
		
		if(!list.isEmpty()){
			getCollectionFileContent(em, list.get(0));
		}
		long diff = System.currentTimeMillis() - start;
		logger.info("Execution [ms=" + diff + ", size=" + list.size() + "]");
		return (list.isEmpty()) ? null : list.get(0);
	}
	
	
	private Set<CollectionFile> getCollectionFileList0(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		long start = System.currentTimeMillis();
		Query query = em.createQuery("from CollectionFile where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<CollectionFile> list = new LinkedHashSet<CollectionFile>(query.getResultList());
		for(CollectionFile file : list){
			getCollectionFileContent(em, file);
		}
		long diff = System.currentTimeMillis() - start;
		logger.info("Execution [ms=" + diff + ", size=" + list.size() + "]");
		return list;
	}
	
	private void getCollectionFileContent(EntityManager em, CollectionFile file) throws Exception{
		file.getAttributeMap().put(TextAttribute.ContentType.longDescription, getTextAttList(em, file.getId(), BOUtils.SourceClass.CollectionFile, TextAttribute.ContentType.longDescription));
		file.getAttributeMap().put(TextAttribute.ContentType.shortDescription, getTextAttList(em, file.getId(), BOUtils.SourceClass.CollectionFile, TextAttribute.ContentType.shortDescription));
		file.setKeywordList(getElementsOfList(em, file.getId(), BOUtils.SourceClass.CollectionFile, ElementOfList.ContentType.keyword));
		file.setSoftwareList(getSoftwareList(em, file.getId(), BOUtils.SourceClass.CollectionFile));
		
		//IANUSEntity *************
		
		//loading person
		file.setRoleList(getRoleList4Source(em, file.getId(), BOUtils.SourceClass.CollectionFile));
		
		//loading internal ids
		file.setInternalIdList(getIdentifierList(em, file.getId(), BOUtils.SourceClass.CollectionFile, Identifier.ContentType.internal_id));
				
		//loading external ids
		file.setExternalIdList(getIdentifierList(em, file.getId(), BOUtils.SourceClass.CollectionFile, Identifier.ContentType.external_id));
			
		//loading places
		file.setPlaceList(getPlaceList(em, file.getId(), BOUtils.SourceClass.CollectionFile));
		
		//loading references
		file.setReferenceList(getReferencesList(em, file.getId(), SourceClass.CollectionFile));
		
		//loading related sources
		file.setRelatedResourceList(getRelatedResourceList(em, file.getId(), SourceClass.CollectionFile));
		
		//loading metadata languages
		file.setMetadataLanguageList(getLanList(em, file.getId(), BOUtils.SourceClass.CollectionFile, Language.ContentType.metadata_language));
	}
	
	private Set<Software> getSoftwareList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from Software where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<Software> list = new LinkedHashSet<Software>(query.getResultList());
		
		return list;
	}
	
	private Set<Reference> getReferencesList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from Reference where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<Reference> list = new LinkedHashSet<Reference>(query.getResultList());
		for(Reference ref : list){
			ref.setPublication(getPublication(em, ref.getPublicationId()));
		}
		return list;
	}
	
	private Set<Time> getTimeList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from Time where sourceId = :sourceId AND sourceClass = :sourceClass ORDER by id ASC");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<Time> list = new LinkedHashSet<Time>(query.getResultList());
		
		for(Time time : list){
			Set<ElementOfList> mainPeriodList = getElementsOfList(em, time.getId(), BOUtils.SourceClass.Time, ElementOfList.ContentType.mainPeriod);
			Set<ElementOfList> subPeriodList = getElementsOfList(em, time.getId(), BOUtils.SourceClass.Time, ElementOfList.ContentType.subPeriod);
			
			time.setMainPeriod((mainPeriodList.isEmpty()? null : mainPeriodList.iterator().next()));
			time.setSubPeriod((subPeriodList.isEmpty()? null : subPeriodList.iterator().next()));
			
			time.setCommentList(getTextAttList(em, time.getId(), BOUtils.SourceClass.Time, TextAttribute.ContentType.comment));
		}
		
		return list;
	}
	
	private Time getTime(EntityManager em, Long id) throws Exception{
		Query query = em.createQuery("from Time where id = :id");
		query.setParameter("id", id);
		Set<Time> list = new LinkedHashSet<Time>(query.getResultList());
		
		Time time = null;
		
		if(!list.isEmpty()){
			time = list.iterator().next();
			Set<ElementOfList> mainPeriodList = getElementsOfList(em, time.getId(), BOUtils.SourceClass.Time, ElementOfList.ContentType.mainPeriod);
			Set<ElementOfList> subPeriodList = getElementsOfList(em, time.getId(), BOUtils.SourceClass.Time, ElementOfList.ContentType.subPeriod);
			
			time.setMainPeriod((mainPeriodList.isEmpty()? null : mainPeriodList.iterator().next()));
			time.setSubPeriod((subPeriodList.isEmpty()? null : subPeriodList.iterator().next()));
			
			time.setCommentList(getTextAttList(em, time.getId(), BOUtils.SourceClass.Time, TextAttribute.ContentType.comment));
		}
		return time;
	}
	
	private Set<PreservationEvent> getPreservationEventList0(EntityManager em, IANUSEntity entity) throws Exception{
		Query query = em.createQuery("from PreservationEvent where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", entity.getId());
		if(entity instanceof DataCollection){
			query.setParameter("sourceClass", BOUtils.SourceClass.DataCollection);
		}else{
			query.setParameter("sourceClass", BOUtils.SourceClass.CollectionFile);
		}
		Set<PreservationEvent> list = new LinkedHashSet<PreservationEvent>(query.getResultList());
		
		for(PreservationEvent event : list){
			event.setSoftwareList(getSoftwareList(em, entity.getId(), BOUtils.SourceClass.PreservationEvent));
		}
		
		return list;
	}
	
	private Set<RelatedResource> getRelatedResourceList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from RelatedResource where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<RelatedResource> list = new LinkedHashSet<RelatedResource>(query.getResultList());
		
		for(RelatedResource item : list){
			Set<Identifier> idList = getIdentifierList(em, item.getId(), BOUtils.SourceClass.RelatedResource, Identifier.ContentType.id);
			item.setIdentifier((idList.isEmpty()) ? new Identifier() : idList.iterator().next());
			
			Set<Institution> instList = getInstitutionList(em, item.getId(), BOUtils.SourceClass.RelatedResource);
			Institution inst = (instList.isEmpty()) ? new Institution() : instList.iterator().next();
			item.setInstitution(inst);
			
		}
		
		return list;
	}
	
	public Publication getPublication(Long id) throws Exception{
		long start = System.currentTimeMillis();
		Publication pub = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				pub = getPublication(em, id);
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
		logger.info("[time(ms)=" + diff + "]");
		return pub;		
	}
	
	public Place getPlace(Long id) throws Exception{
		long start = System.currentTimeMillis();
		Place place = null;
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				place = getPlace(em, id);
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
		logger.info("[time(ms)=" + diff + "]");
		return place;		
	}
	
	private Publication getPublication(EntityManager em, Long id) throws Exception{
		Query query = em.createQuery("from Publication where id = :id");
		query.setParameter("id", id);
		List<Publication> list = query.getResultList();
		if(list.isEmpty()){
			throw new Exception("The Publication has not been found id=" + id);
		}
		Publication pub = list.get(0);
		
		pub.setPidList(getIdentifierList(em, pub.getId(), BOUtils.SourceClass.Publication, Identifier.ContentType.pid));
		//pub.setPidList(getElementsOfList(em, pub.getId(), BOUtils.SourceClass.Publication, ElementOfList.ContentType.pid));
		return pub;
	}
	
	private Set<Place> getPlaceList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from Place where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<Place> list = new LinkedHashSet<Place>(query.getResultList());
		for(Place place : list){
			place.getAttributeMap().put(TextAttribute.ContentType.accuracyDescription, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.accuracyDescription));
			place.getAttributeMap().put(TextAttribute.ContentType.greaterRegion, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.greaterRegion));
			place.getAttributeMap().put(TextAttribute.ContentType.country, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.country));
			place.getAttributeMap().put(TextAttribute.ContentType.regionProvince, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.regionProvince));
			place.getAttributeMap().put(TextAttribute.ContentType.city, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.city));
			
			place.getAttributeMap().put(TextAttribute.ContentType.historicalName, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.historicalName));
			place.getAttributeMap().put(TextAttribute.ContentType.freeDescription, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.freeDescription));
			place.getAttributeMap().put(TextAttribute.ContentType.typeOfArea, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.typeOfArea));
			
			place.setIdentifierList(getElementsOfList(em, place.getId(), BOUtils.SourceClass.Place, ElementOfList.ContentType.identifier));
		}
		return list;
	}
	
	private Place getPlace(EntityManager em, Long placeId) throws Exception{
		Query query = em.createQuery("from Place where id = :id");
		query.setParameter("id", placeId);
		Set<Place> list = new LinkedHashSet<Place>(query.getResultList());
		Place place = null;
		if(!list.isEmpty()){
			place = list.iterator().next();
			place.getAttributeMap().put(TextAttribute.ContentType.accuracyDescription, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.accuracyDescription));
			place.getAttributeMap().put(TextAttribute.ContentType.greaterRegion, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.greaterRegion));
			place.getAttributeMap().put(TextAttribute.ContentType.country, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.country));
			place.getAttributeMap().put(TextAttribute.ContentType.regionProvince, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.regionProvince));
			place.getAttributeMap().put(TextAttribute.ContentType.city, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.city));
			
			place.getAttributeMap().put(TextAttribute.ContentType.historicalName, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.historicalName));
			place.getAttributeMap().put(TextAttribute.ContentType.freeDescription, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.freeDescription));
			place.getAttributeMap().put(TextAttribute.ContentType.typeOfArea, getTextAttList(em, place.getId(), BOUtils.SourceClass.Place, TextAttribute.ContentType.typeOfArea));
			
			place.setIdentifierList(getElementsOfList(em, place.getId(), BOUtils.SourceClass.Place, ElementOfList.ContentType.identifier));
		}
		return place;
	}
	
	private Set<Identifier> getIdentifierList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass, Identifier.ContentType contentType){
		Query query = em.createQuery("from Identifier where sourceId = :sourceId AND sourceClass = :sourceClass AND contentType = :contentType");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		query.setParameter("contentType", contentType);
		//return (List<ForeignId>)query.getResultList();
		return new LinkedHashSet<Identifier>(query.getResultList());
	}
	
	private Set<FileFormat> getFileFormatList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass){
		Query query = em.createQuery("from FileFormat where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		return new LinkedHashSet<FileFormat>(query.getResultList());
	}
	
	private Set<ResourceType> getResourceTypeList(EntityManager em, DataCollection dc){
		Query query = em.createQuery("from ResourceType where dcId = :dcId");
		query.setParameter("dcId", dc.getId());
		//return (List<ResourceType>)query.getResultList();
		return new LinkedHashSet<ResourceType>(query.getResultList());
	}
	
	private Set<Language> getLanList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass, Language.ContentType contentType){
		Query query = em.createQuery("from Language where sourceId = :sourceId AND sourceClass = :sourceClass AND contentType = :contentType");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		query.setParameter("contentType", contentType);
		//return (List<Language>)query.getResultList();
		return new LinkedHashSet<Language>(query.getResultList());
	}
	
	private Set<AccessRight> getAccessRights(EntityManager em, DataCollection dc){
		Query query = em.createQuery("from AccessRight where dcId = :dcId");
		query.setParameter("dcId", dc.getId());
		return new LinkedHashSet<AccessRight>(query.getResultList());
	}
	
	private Set<ActorRole> getRoleList4Actor(EntityManager em, Actor actor) throws Exception{
		Query query = em.createQuery("from ActorRole where actorId = :actorId AND actorClass = :actorClass");
		query.setParameter("actorId", actor.getId());
		query.setParameter("actorClass", actor.getActorClass());
		return new LinkedHashSet<ActorRole>(query.getResultList());
	}
	
	private Set<ActorRole> getRoleList4Source(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass){
		Query query = em.createQuery("from ActorRole where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		return new LinkedHashSet<ActorRole>(query.getResultList());
	}
	
	private Set<ActorRole> getRoleList4SourceAndActor(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass, Actor actor) throws Exception{
		Query query = em.createQuery("from ActorRole where sourceId = :sourceId AND sourceClass = :sourceClass AND actorId = :actorId AND actorClass = :actorClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		query.setParameter("actorId", actor.getId());
		query.setParameter("actorClass", actor.getActorClass());
		return new LinkedHashSet<ActorRole>(query.getResultList());
	}
	
	private Set<TimeSpan> getTimeSpanList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass, TimeSpan.ContentType contentType){
		Query query = em.createQuery("from TimeSpan where sourceId = :sourceId AND sourceClass = :sourceClass AND contentType = :contentType");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		query.setParameter("contentType", contentType);
		return new LinkedHashSet<TimeSpan>(query.getResultList());
	}
	
	private Actor getActor(EntityManager em, Long actorId, BOUtils.ActorClass actorClass) throws Exception{
		long start = System.currentTimeMillis();
		
		Actor actor = null;
		if(BOUtils.ActorClass.Person.equals(actorClass)){
			actor = getPerson(em, actorId);
		}else if(BOUtils.ActorClass.Institution.equals(actorClass)){
			actor = getInstitution(em, actorId);
		}
		
		long diff = System.currentTimeMillis() - start;  
		logger.info("[time(ms)=" + diff + "]");
		return actor;
	}
	
	
	private Set<Actor> getActorList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Set<Actor> list = new LinkedHashSet<Actor>();
		list.addAll(getPersonList(em, sourceId, sourceClass));
		list.addAll(getInstitutionList(em, sourceId, sourceClass));
		return list;
	}
	
	private Set<Person> getPersonList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from Person where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<Person> list = new LinkedHashSet<Person>(query.getResultList());
		for(Person item : list){
			item = loadPersonContain(em, item);
		}
		return list;
	}
	
	private Person getPerson(EntityManager em, Long id) throws Exception{
		Query query = em.createQuery("from Person where id = :id");
		query.setParameter("id", id);
		List<Person> list = query.getResultList();
		Person person = null;
		if(!list.isEmpty()){
			person = list.get(0);
			person = loadPersonContain(em, person);
		}
		return person;
	}
	
	private Person loadPersonContain(EntityManager em, Person person) throws Exception{
		
		person.setSubDisciplineList(getElementsOfList(em, person.getId(), BOUtils.SourceClass.Person, ElementOfList.ContentType.subDiscipline));
		person.setExternalIdList(getIdentifierList(em, person.getId(), BOUtils.SourceClass.Person, Identifier.ContentType.external_id));
		//loading the roles only for display issues
		person.setRoleList(getRoleList4Actor(em, person));
		
		return person;
	}
	
	private Set<Institution> getInstitutionList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass) throws Exception{
		Query query = em.createQuery("from Institution where sourceId = :sourceId AND sourceClass = :sourceClass");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		Set<Institution> list = new LinkedHashSet<Institution>(query.getResultList());
		for(Institution item : list){
			item.setExternalIdList(getIdentifierList(em, item.getId(), BOUtils.SourceClass.Person, Identifier.ContentType.external_id));
			item.setRoleList(getRoleList4Actor(em, item));
		}
		return list;
	}
	
	private Institution getInstitution(EntityManager em, Long id) throws Exception{
		Query query = em.createQuery("from Institution where id = :id");
		query.setParameter("id", id);
		List<Institution> list = query.getResultList();
		Institution item = null;
		if(!list.isEmpty()){
			item = list.get(0);
			item.setExternalIdList(getIdentifierList(em, item.getId(), BOUtils.SourceClass.Institution, Identifier.ContentType.external_id));
			item.setRoleList(getRoleList4Actor(em, item));
		}
		return item;
	}
	
	private Set<TextAttribute> getTextAttList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass, TextAttribute.ContentType contentType) throws Exception{
		Query query = em.createQuery("from TextAttribute where sourceId = :sourceId AND sourceClass = :sourceClass AND contentType = :contentType");
		
		query.setParameter("sourceId", sourceId);
		query.setParameter("contentType", contentType);
		query.setParameter("sourceClass", sourceClass);
		
		/*
		if(source instanceof DataCollection){
			query.setParameter("sourceClass", BOUtils.SourceClass.DataCollection);
		}else if(source instanceof Place){
			query.setParameter("sourceClass", BOUtils.SourceClass.Place);
		}else if(source instanceof CollectionFile){
			query.setParameter("sourceClass", BOUtils.SourceClass.CollectionFile);
		}else if(source instanceof Publication){
			query.setParameter("sourceClass", BOUtils.SourceClass.Publication);
		}else if(source instanceof Institution){
			query.setParameter("sourceClass", BOUtils.SourceClass.Institution);
		}else{
			throw new Exception("Source Class not supported: " + source.getClass().getName());
		}*/
		
		Set<TextAttribute> list = new LinkedHashSet<TextAttribute>(query.getResultList());
		return list;
	}
	
	private Set<ElementOfList> getElementsOfList(EntityManager em, Long sourceId, BOUtils.SourceClass sourceClass, ElementOfList.ContentType contentType) throws Exception{
		Query query = em.createQuery("from ElementOfList where sourceId = :sourceId AND sourceClass = :sourceClass AND contentType = :contentType");
		query.setParameter("sourceId", sourceId);
		query.setParameter("sourceClass", sourceClass);
		query.setParameter("contentType", contentType);
		
		Set<ElementOfList> list = new LinkedHashSet<ElementOfList>(query.getResultList());
		return list;
	}
	
	public List<DataCollection> getLWDataCollectionList(){
		long start = System.currentTimeMillis();
		List<DataCollection> list = new ArrayList<DataCollection>();
		
		EntityManager em = PersistenceManager.getEntityManager();
		try {
			em.getTransaction().begin();
			try {
				Query query = em.createQuery("from DataCollection");
				list = (List<DataCollection>)query.getResultList();
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
		logger.info("[size="+ list.size() +", time(ms)=" + diff + "]");
		return list;
	}
	
	public class CloneResult{
		private DataCollection dc;
		private Map<Actor, Actor> actorMap = new HashMap<Actor, Actor>();
		
		public CloneResult(){}

		public DataCollection getDc() {
			return dc;
		}

		public Map<Actor, Actor> getActorMap() {
			return actorMap;
		}
	}
	
}
