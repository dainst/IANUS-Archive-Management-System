package de.ianus.ingest.web.pages.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.web.pages.component.DcComponentPage;
import de.ianus.ingest.web.pages.component.ManyCheckboxEOL;
import de.ianus.ingest.web.utils.SelectItemComparator;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.IANUSEntity;
import de.ianus.metadata.bo.actor.Actor;
import de.ianus.metadata.bo.actor.ActorRole;
import de.ianus.metadata.bo.actor.Institution;
import de.ianus.metadata.bo.actor.Person;
import de.ianus.metadata.bo.utils.ElementOfList;
import de.ianus.metadata.bo.utils.Identifier;
import de.ianus.metadata.utils.DisciplineList;

public class ActorPage extends DcComponentPage{
	
	public static String PAGE_NAME = "actor";
	
	public static List<SelectItem> roleGerList = new ArrayList<SelectItem>();
	public static List<SelectItem> roleEngList = new ArrayList<SelectItem>();
	
	static{
		
		for(ActorRole.Type item : ActorRole.Type.values()){
			roleGerList.add(new SelectItem(item.id, item.labelGer));
			roleEngList.add(new SelectItem(item.id, item.labelEng));
		}
		Collections.sort(roleGerList,new SelectItemComparator());
		Collections.sort(roleEngList,new SelectItemComparator());
	}
	
	private Set<Actor> actorList = new LinkedHashSet<Actor>();
	private Actor actor;
	private ManyCheckboxEOL mainDisciplineCheckbox;
	
	private String externalIdType;
	private Identifier externalId = new Identifier();
	private Set<Identifier> currentExternalIdList = new LinkedHashSet<Identifier>();
	
	private ActorRole role;
	private Set<ActorRole> currentRoleList = new HashSet<ActorRole>();
	
	public String actionShowAllActors(){
		try {
			this.actor = null;
			this.actorList = Services.getInstance().getMDService().getActorList(this.getDcBase());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionEditActor(){
		try {
			this.load(this.getDcBase(), this.getSource(), (Actor)getRequestBean("actor"));
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionAddPerson(){
		try {
			this.load(getDcBase(), getDcSource(), new Person());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionAddInstitution(){
		try {
			this.load(getDcBase(), getDcSource(), new Institution());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public boolean isShowTable(){
		return this.actor == null && !this.actorList.isEmpty();
	}
	
	public void loadDataCollection(DataCollection dc) throws Exception{
		super.load(dc, null);
		this.load0();
	}
	
	public Person getPerson(){
		return (this.actor instanceof Person) ? (Person)actor : null;
	}
	
	public Institution getInstitution(){
		return (this.actor instanceof Institution) ? (Institution)actor : null;
	}
	
	public String getPageTitle(){
		return actor.getClass().getSimpleName();
	}
	
	public void load(DataCollection dcBase, IANUSEntity source, Actor actor) throws Exception{
		super.load(dcBase, source);
		this.actor = (actor.isPersistent()) ? Services.getInstance().getMDService().getActor(actor.getId(), actor.getActorClass()) : actor;
		this.role = new ActorRole();
		this.load0();
	}
	
	private void load0() throws Exception{
		
		this.actorList = Services.getInstance().getMDService().getActorList(getDcBase());
		
		this.role = (this.role == null) ? new ActorRole() : role;
		
		this.externalId.setType(Identifier.PersonType.FREE.id);
		
		//loading sub discipline
		if(actor instanceof Institution){
			this.mainDisciplineCheckbox = null;
		}else if(actor instanceof Person){
			this.mainDisciplineCheckbox = new ManyCheckboxEOL(this.actor, DisciplineList.LIST_ID_SUB, ElementOfList.ContentType.subDiscipline);
		}
		
		if(this.actor.isPersistent()){
			//loading external identifier
			this.currentExternalIdList = new LinkedHashSet<Identifier>(this.actor.getExternalIdList());
			//loading role list
			this.currentRoleList = Services.getInstance().getMDService().getRoleList4Actor(actor);
		}else{
			this.currentExternalIdList = new LinkedHashSet<Identifier>();
			this.currentRoleList = new HashSet<ActorRole>();
		}
		
	}
	
	public String actionAllActors(){
		try {
			this.actor = null;
			this.loadDataCollection(this.getDcBase());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeleteRole(){
		try {
			ActorRole role0 = (ActorRole)getRequestBean("role");
			this.currentRoleList.remove(role0);
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	public String actionDeleteActor(){
		try {
			Actor actor0 = (Actor)getRequestBean("actor");
			Services.getInstance().getMDService().deleteActor(actor0);;
			this.getDcBase().getActorMap().remove(actor0.getId());
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	/**
	 * This method adds a new ActorRole to the current actor. The actor can have several instances of the same role type.
	 * @return
	 */
	public String actionAddRole(){
		try {
			
			this.role.setActor(this.actor);
			this.role.setSource(this.getSource());
			this.currentRoleList.add(role);
			this.role = new ActorRole();
			
		} catch (Exception e) {
			addInternalError(e);
		}
		
		return PAGE_NAME;
	}
	
	public String actionResetRole(){
		this.role = new ActorRole();
		return PAGE_NAME;
	}
	
	/**
	 * This method saves the current actor and reloads the actor list for the current data collection.
	 * @return
	 */
	public String actionSave(){
		try {
			//this.actor.setDcId(this.getDcBase().getId());
			this.actor.setSource(this.getDcBase());
			Services.getInstance().getMDService().saveEntry(this.actor);
			if(this.actor instanceof Person){
				this.mainDisciplineCheckbox.save();
			}
			this.getDcBase().getActorMap().put(this.actor.getId(), this.actor);
			
			this.saveExternalIdentifierList();
			this.saveRoleList();
			
			this.load(this.getDcBase(), this.getSource(),this.actor);
			
		} catch (Exception e) {
			addInternalError(e);
		}
		return PAGE_NAME;
	}
	
	private void saveExternalIdentifierList() throws Exception{
		for(Identifier id : this.currentExternalIdList){
			id.setContentType(Identifier.ContentType.external_id);
			id.setSource(this.actor);
			Services.getInstance().getMDService().saveEntry(id);
			this.actor.getExternalIdList().add(id);
		}
		
		for(Identifier id : this.actor.getExternalIdList()){
			if(!this.currentExternalIdList.contains(id)){
				Services.getInstance().getMDService().deleteDBEntry(id);
			}
		}
	}
	
	private void saveRoleList() throws Exception{
		
		for(ActorRole currentRole0 : this.currentRoleList){
			currentRole0.setActor(this.actor);
			Services.getInstance().getMDService().saveEntry(currentRole0);
		}
		
		Set<ActorRole> oldRoleList = Services.getInstance().getMDService().getRoleList4Actor(actor);
		for(ActorRole oldRole0 : oldRoleList){
			//if(!this.currentRoleList.contains(oldRole0)){
			if(!this.currentListContainsRole(oldRole0)){
				Services.getInstance().getMDService().deleteDBEntry(oldRole0);
			}
		}
	}
	
	/**
	 * This method is used to replace: this.currentRoleList.contains(oldRole0), because it is not working.
	 * Check equals and hasHash methods.
	 * @return
	 */
	private boolean currentListContainsRole(ActorRole oldRole0){
		for(ActorRole role : this.currentRoleList){
			if(role.getId().equals(oldRole0.getId())){
				return true;
			}
		}
		return false;
	}
	
	public void listenerDeleteExternalId(){
		Identifier identifier = (Identifier)getRequestBean("externalId");
		this.currentExternalIdList.remove(identifier);
	}
	
	public boolean isShowExternalIdentifierFreeText(){
		return (StringUtils.equals(this.externalId.getType(), Identifier.PersonType.FREE.id));
	}
	
	public String actionAddExternalId(){
		
		this.externalId.setType(StringUtils.equals(this.externalId.getType(), Identifier.PersonType.FREE.id) ? this.externalIdType : this.externalId.getType());
		this.currentExternalIdList.add(this.externalId);
		this.externalId = new Identifier();
		this.externalId.setType(Identifier.PersonType.FREE.id);
		this.externalIdType = null;
		
		return PAGE_NAME;
	}
	
	public String actionDeletePerson(){
		return PAGE_NAME;
	}
	
	public String actionSelect(){
		this.actor = (Actor)getRequestBean("actor");
		return PAGE_NAME;
	}
	
	public Set<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(Set<Actor> actorList) {
		this.actorList = actorList;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public ActorRole getRole() {
		return role;
	}

	public void setRole(ActorRole role) {
		this.role = role;
	}

	public ManyCheckboxEOL getMainDisciplineCheckbox() {
		return mainDisciplineCheckbox;
	}

	public void setMainDisciplineCheckbox(ManyCheckboxEOL mainDisciplineCheckbox) {
		this.mainDisciplineCheckbox = mainDisciplineCheckbox;
	}

	public Identifier getExternalId() {
		return externalId;
	}

	public void setExternalId(Identifier externalId) {
		this.externalId = externalId;
	}

	public String getExternalIdType() {
		return externalIdType;
	}

	public void setExternalIdType(String externalIdType) {
		this.externalIdType = externalIdType;
	}

	public Set<Identifier> getCurrentExternalIdList() {
		return currentExternalIdList;
	}

	public void setCurrentExternalIdList(Set<Identifier> currentExternalIdList) {
		this.currentExternalIdList = currentExternalIdList;
	}

	public Set<ActorRole> getCurrentRoleList() {
		return currentRoleList;
	}

	public void setCurrentRoleList(Set<ActorRole> currentRoleList) {
		this.currentRoleList = currentRoleList;
	}
	
	public List<SelectItem> getRoleGerList(){
		return roleGerList;
	}
	
	public List<SelectItem> getRoleEngList(){
		return roleEngList;
	}
}


