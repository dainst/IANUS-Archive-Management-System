package de.ianus.ingest.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.ArchivalIP;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.bo.TransferP;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.ums.User;
import de.ianus.ingest.web.pages.ActionPage;
import de.ianus.ingest.web.pages.ActivityOutputPage;
import de.ianus.ingest.web.pages.AdminPage;
import de.ianus.ingest.web.pages.ArchivalListPage;
import de.ianus.ingest.web.pages.ArchivalPage;
import de.ianus.ingest.web.pages.CollectionFilePage;
import de.ianus.ingest.web.pages.CollectionIdPage;
import de.ianus.ingest.web.pages.ConversionPlanPage;
import de.ianus.ingest.web.pages.DisseminationListPage;
import de.ianus.ingest.web.pages.DisseminationPage;
import de.ianus.ingest.web.pages.FilesPage;
import de.ianus.ingest.web.pages.FilesPage.Right;
import de.ianus.ingest.web.pages.FilesPage.ViewLevel;
import de.ianus.ingest.web.pages.MemoryUsagePage;
import de.ianus.ingest.web.pages.MetadataEditorPage;
import de.ianus.ingest.web.pages.OverviewDCPage;
import de.ianus.ingest.web.pages.PreIngestReportPage;
import de.ianus.ingest.web.pages.ProcessEnginePage;
import de.ianus.ingest.web.pages.RsyncUploadPage;
import de.ianus.ingest.web.pages.SimpleUploadPage;
import de.ianus.ingest.web.pages.StoragePage;
import de.ianus.ingest.web.pages.SubmissionListPage;
import de.ianus.ingest.web.pages.SubmissionPage;
import de.ianus.ingest.web.pages.TimePage;
import de.ianus.ingest.web.pages.TransferListPage;
import de.ianus.ingest.web.pages.TransferPage;
import de.ianus.ingest.web.pages.UploadPage;
import de.ianus.ingest.web.pages.UserManagerPage;
import de.ianus.ingest.web.pages.metadata.ActorLogoPage;
import de.ianus.ingest.web.pages.metadata.ActorPage;
import de.ianus.ingest.web.pages.metadata.AlternativeRepresentationPage;
import de.ianus.ingest.web.pages.metadata.AssetUploadPage;
import de.ianus.ingest.web.pages.metadata.DataCollectionPage;
import de.ianus.ingest.web.pages.metadata.InitialMDPage;
import de.ianus.ingest.web.pages.metadata.PlacePage;
import de.ianus.ingest.web.pages.metadata.PublicationPage;
import de.ianus.ingest.web.pages.metadata.ReferencePage;
import de.ianus.ingest.web.pages.metadata.RelatedResourcePage;
import de.ianus.metadata.bo.DataCollection;

@ManagedBean(eager=true)
@SessionScoped
public class SessionBean extends AbstractBean{
	
	private static final Logger logger = LogManager.getLogger(SessionBean.class);
	
	private List<String> msgList = new ArrayList<String>();
	
	private User currentUser;
	private String password;
	private String userName;
	
	//Services
	private StoragePage storagePage = new StoragePage();
	private ProcessEnginePage processEnginePage = new ProcessEnginePage();
	
	//Utils
	private UploadPage uploadPage = new UploadPage();
	private RsyncUploadPage rsyncUploadPage = new RsyncUploadPage();
	private FilesPage filesPage = new FilesPage();
	private ActivityOutputPage activityOutputPage = new ActivityOutputPage();
	private SimpleUploadPage simpleUploadPage = new SimpleUploadPage();
	
	//IP List
	private OverviewDCPage overviewDCPage = new OverviewDCPage();
	private TransferListPage transferListPage = new TransferListPage();
	private SubmissionListPage submissionListPage = new SubmissionListPage();
	private ArchivalListPage archivalListPage = new ArchivalListPage();
	private DisseminationListPage disseminationListPage = new DisseminationListPage();
	
	//IPs
	private TransferPage transferPage = new TransferPage();
	private SubmissionPage submissionPage = new SubmissionPage();
	private ArchivalPage archivalPage = new ArchivalPage();
	private DisseminationPage disseminationPage = new DisseminationPage();
	
	private MetadataEditorPage metadataPage = new MetadataEditorPage();
	private DataCollectionPage dataCollectionPage = new DataCollectionPage();
	private InitialMDPage initialMDPage = new InitialMDPage();
	private PlacePage placePage = new PlacePage();
	
	private CollectionFilePage collectionFilePage = new CollectionFilePage();
	
	private ActorPage actorPage = new ActorPage();
	private ActorLogoPage actorLogoPage = new ActorLogoPage();
	private PublicationPage publicationPage = new PublicationPage();
	private ReferencePage referencePage = new ReferencePage();
	private TimePage timePage = new TimePage();
	private RelatedResourcePage relatedResourcePage = new RelatedResourcePage();
	private AlternativeRepresentationPage alternativeRepresentationPage = new AlternativeRepresentationPage();
	
	private CollectionIdPage collectionIdPage = new CollectionIdPage();
	
	//SIP
	private PreIngestReportPage preIngestReportPage = new PreIngestReportPage();
	
	private AdminPage adminPage = new AdminPage();

	private AssetUploadPage assetUploadPage = new AssetUploadPage();
	
	private MemoryUsagePage memoryUsagePage = new MemoryUsagePage();
	
	private UserManagerPage userManagerPage = new UserManagerPage();
	
	private ConversionPlanPage conversionPlanPage = new ConversionPlanPage(); 
	private ActionPage actionPage = new ActionPage();
	
	
	public SessionBean(){
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");
			sb.append("Starting SessionBean!!!\n");
			sb.append("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");
			logger.info(sb.toString());
			
			this.storagePage.load();
			this.overviewDCPage.load();
			
			List<User> list = Services.getInstance().getDaoService().getUserList();
			if(list.isEmpty()){
				logger.info("Creating default user 'ianus_user'");
				User user = new User();
				user.setFirstName("ianus");
				user.setLastName("ianus");
				user.setUserName("IANUS-Test");
				user.setPassword("xxxxx");
				Services.getInstance().getDaoService().saveDBEntry(user);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLoggedIn(){
		return (this.currentUser != null);
	}
	
	public String getStatusIcon(InformationPackage ip){
		String status = ((WorkflowIP)ip).getState();
		if(StringUtils.equals(status, WorkflowIP.NOT_STARTED)){
			return getAppBean().getImgWFNoStarted();
		}else if(StringUtils.equals(status, WorkflowIP.IN_PROGRESS)){
			return getAppBean().getImgWorking();
		}else if(StringUtils.equals(status, WorkflowIP.FINISHED)){
			return getAppBean().getImgWFFinished();
		}else if(StringUtils.equals(status, WorkflowIP.FINISHED_ERRORS)){
			return getAppBean().getImgWFFinishedWithErrors();
		}	
		return null;
	}
	
	public boolean isGuestUser(){
		return (this.currentUser != null && StringUtils.equals(this.currentUser.getRole(), User.UserRole.guest.toString()));
	}
	
	public void updateCollectionTitelInIP(DataCollection dc0) throws Exception{
		DataCollection dc = Services.getInstance().getMDService().getDataCollection(dc0.getId());
		List<WorkflowIP> list = Services.getInstance().getDaoService().getWorkflowPackageList(dc.getId(), null);
		if(!list.isEmpty()){
			WorkflowIP ip = list.get(0);
			ip.setCollectionLabel(dc.getCollectionLabel());
			ip.setCollectionIdentifier(dc.getCollectionIdentifier());
			Services.getInstance().getDaoService().saveDBEntry(ip);
			this.getOverviewDCPage().load();
		}
		if(list.size() > 1){
			logger.error("The following DC has " + list.size() + " IP associated. It should have only one. " + dc.toString());
		}
	}
	
	public void listenerLogin(ActionEvent event){
		if(StringUtils.isNotEmpty(this.password) && StringUtils.isNotEmpty(this.userName)){
			this.currentUser = Services.getInstance().getDaoService().getUser(userName, password);
			this.userName = null;
			this.password = null;
			if(currentUser == null){
				addMsg("User and/or password was not correct");
			}
		}
	}
	
	public void listenerLogout(ActionEvent event){
		this.currentUser = null;
		
		System.out.println(FacesContext.getCurrentInstance().getViewRoot().getViewId());
		
		
		ConfigurableNavigationHandler configNavHandler = (ConfigurableNavigationHandler)FacesContext.getCurrentInstance().getApplication().getNavigationHandler(); //assumes you already have an instance of FacesContext, named ctxt
		
		NavigationCase navCase = configNavHandler.getNavigationCase(FacesContext.getCurrentInstance(),null, null);

		 //String toViewId = navCase.getToViewId(ctx); // the <to-view-id>
		System.out.println("");
		
	}
	

	/**
	 * Mapping page names to the according page loader...
	 * 
	 * Also have a look on the method ActivityWrapper.getUserSupport(), which maps the activity name to the 
	 * page name!
	 * 
	 * @param pageName
	 * @param ip
	 * @throws Exception
	 */
	public void loadPage(String pageName, WorkflowIP ip) throws Exception {
		switch(pageName) {
		case UploadPage.PAGE_NAME:
			this.getUploadPage().load(ip);
			break;
		case FilesPage.PAGE_NAME: 
			if(ip instanceof TransferP){
				this.getFilesPage().load(ip, ViewLevel.project, Right.download);
			}else if(ip instanceof SubmissionIP){
				this.getFilesPage().load(ip, ViewLevel.project, Right.move, Right.download, Right.move, Right.delete, Right.update);
			}else if(ip instanceof ArchivalIP){
				this.getFilesPage().load(ip, ViewLevel.project, Right.move, Right.download, Right.move, Right.delete, Right.update);
			}
			break;
		case DataCollectionPage.PAGE_NAME:
			DataCollection dc = Services.getInstance().getMDService().getDataCollection(ip.getMetadataId());
			this.getDataCollectionPage().load(ip, dc, dc);
			break;
		case InitialMDPage.PAGE_NAME:
			this.getInitialMDPage().loadDataCollection(ip, ip.getMetadataId());
			break;
		case CollectionIdPage.PAGE_NAME:
			this.getCollectionIdPage().loadDataCollection(ip, ip.getMetadataId());
			break;
		case PreIngestReportPage.PAGE_NAME:
			this.getPreIngestReportPage().load(ip);
			break;
		case AssetUploadPage.PAGE_NAME:
			this.getAssetUploadPage().load(ip.getDataCollection(), ip);
			break;
		case SimpleUploadPage.PAGE_NAME:
			this.getSimpleUploadPage().load(ip);
		}
	}
	
	
	
	public void listenerCloseMessageDialog(AjaxBehaviorEvent event){
		this.msgList = new ArrayList<String>();
	}
	
	public void listenerCloseMessageDialog(ActionEvent event){
		this.msgList = new ArrayList<String>();
	}
	
	public boolean isShowMessageDialog(){
		return msgList != null && !msgList.isEmpty();
	}
	
	public int getResult(){
		return 10;
	}

	public List<String> getMsgList() {
		return msgList;
	}


	public void setMsgList(List<String> msgList) {
		this.msgList = msgList;
	}

	public StoragePage getStoragePage() {
		return storagePage;
	}
	
	public void setStoragePage(StoragePage storagePage) {
		this.storagePage = storagePage;
	}
	
	public UploadPage getUploadPage() {
		return uploadPage;
	}

	public RsyncUploadPage getRsyncUploadPage() {
		return rsyncUploadPage;
	}

	public void setUploadPage(UploadPage uploadPage) {
		this.uploadPage = uploadPage;
	}
	
	public FilesPage getFilesPage() {
		return filesPage;
	}
	
	public void setFilesPage(FilesPage filesPage) {
		this.filesPage = filesPage;
	}
	
	public SimpleUploadPage getSimpleUploadPage() {
		return this.simpleUploadPage;
	}
	
	public void setSimpleUploadPage(SimpleUploadPage sup) {
		this.simpleUploadPage = sup;
	}

	public ProcessEnginePage getProcessEnginePage() {
		return processEnginePage;
	}

	public void setProcessEnginePage(ProcessEnginePage processEnginePage) {
		this.processEnginePage = processEnginePage;
	}

	public OverviewDCPage getOverviewDCPage() {
		return overviewDCPage;
	}

	public void setOverviewDCPage(OverviewDCPage overviewDCPage) {
		this.overviewDCPage = overviewDCPage;
	}
	
	
	public TransferListPage getTransferListPage() {
		return this.transferListPage;
	}
	
	public void setTransferListPage(TransferListPage transferListPage) {
		this.transferListPage = transferListPage;
	}

	public SubmissionListPage getSubmissionListPage() {
		return submissionListPage;
	}

	public void setSubmissionListPage(SubmissionListPage submissionListPage) {
		this.submissionListPage = submissionListPage;
	}

	public TransferPage getTransferPage() {
		return transferPage;
	}

	public void setTransferPage(TransferPage transferPage) {
		this.transferPage = transferPage;
	}

	public SubmissionPage getSubmissionPage() {
		return submissionPage;
	}

	public void setSubmissionPage(SubmissionPage submissionPage) {
		this.submissionPage = submissionPage;
	}


	public ActivityOutputPage getActivityOutputPage() {
		return activityOutputPage;
	}


	public void setActivityOutputPage(ActivityOutputPage activityOutputPage) {
		this.activityOutputPage = activityOutputPage;
	}


	public MetadataEditorPage getMetadataPage() {
		return metadataPage;
	}


	public void setMetadataPage(MetadataEditorPage metadataPage) {
		this.metadataPage = metadataPage;
	}
	
	public DataCollectionPage getDataCollectionPage() {
		return dataCollectionPage;
	}


	public void setDataCollectionPage(DataCollectionPage dataCollectionPage) {
		this.dataCollectionPage = dataCollectionPage;
	}


	public PlacePage getPlacePage() {
		return placePage;
	}


	public void setPlacePage(PlacePage placePage) {
		this.placePage = placePage;
	}

	public CollectionFilePage getCollectionFilePage() {
		return collectionFilePage;
	}
	
	public void setCollectionFilePage(CollectionFilePage collectionFilePage) {
		this.collectionFilePage = collectionFilePage;
	}

	public ActorPage getActorPage() {
		return actorPage;
	}

	public ActorLogoPage getActorLogoPage() {
		return actorLogoPage;
	}

	public void setActorPage(ActorPage actorPage) {
		this.actorPage = actorPage;
	}

	public PublicationPage getPublicationPage() {
		return publicationPage;
	}

	public void setPublicationPage(PublicationPage publicationPage) {
		this.publicationPage = publicationPage;
	}

	public ReferencePage getReferencePage() {
		return referencePage;
	}

	public void setReferencePage(ReferencePage referencePage) {
		this.referencePage = referencePage;
	}
	public RelatedResourcePage getRelatedResourcePage() {
		return relatedResourcePage;
	}
	public void setRelatedResourcePage(RelatedResourcePage relatedResourcePage) {
		this.relatedResourcePage = relatedResourcePage;
	}
	public AlternativeRepresentationPage getAlternativeRepresentationPage() {
		return alternativeRepresentationPage;
	}

	public void setAlternativeRepresentationPage(AlternativeRepresentationPage alternativeRepresentationPage) {
		this.alternativeRepresentationPage = alternativeRepresentationPage;
	}
	public InitialMDPage getInitialMDPage() {
		return initialMDPage;
	}
	public void setInitialMDPage(InitialMDPage initialMDPage) {
		this.initialMDPage = initialMDPage;
	}

	public DisseminationListPage getDisseminationListPage() {
		return disseminationListPage;
	}
	public void setDisseminationListPage(DisseminationListPage disseminationListPage) {
		this.disseminationListPage = disseminationListPage;
	}
	public TimePage getTimePage() {
		return timePage;
	}
	public void setTimePage(TimePage timePage) {
		this.timePage = timePage;
	}
	
	public PreIngestReportPage getPreIngestReportPage() {
		return preIngestReportPage;
	}
	
	public AssetUploadPage getAssetUploadPage() {
		return assetUploadPage ;
	}
	
	public void setAssetUploadPage(AssetUploadPage page) {
		this.assetUploadPage = page;
	}

	public void setPreIngestReportPage(PreIngestReportPage preIngestReportPage) {
		this.preIngestReportPage = preIngestReportPage;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public AdminPage getAdminPage() {
		return adminPage;
	}

	public void setAdminPage(AdminPage adminPage) {
		this.adminPage = adminPage;
	}	

	public ArchivalPage getArchivalPage() {
		return archivalPage;
	}

	public void setArchivalPage(ArchivalPage archivalPage) {
		this.archivalPage = archivalPage;
	}

	public CollectionIdPage getCollectionIdPage() {
		return collectionIdPage;
	}

	public void setCollectionIdPage(CollectionIdPage collectionIdPage) {
		this.collectionIdPage = collectionIdPage;
	}

	public MemoryUsagePage getMemoryUsagePage() {
		return memoryUsagePage;
	}

	public void setMemoryUsagePage(MemoryUsagePage memoryUsagePage) {
		this.memoryUsagePage = memoryUsagePage;
	}

	public UserManagerPage getUserManagerPage() {
		return userManagerPage;
	}

	public void setUserManagerPage(UserManagerPage userManagerPage) {
		this.userManagerPage = userManagerPage;
	}

	public ArchivalListPage getArchivalListPage() {
		return archivalListPage;
	}

	public void setArchivalListPage(ArchivalListPage archivalListPage) {
		this.archivalListPage = archivalListPage;
	}

	public DisseminationPage getDisseminationPage() {
		return disseminationPage;
	}

	public void setDisseminationPage(DisseminationPage disseminationPage) {
		this.disseminationPage = disseminationPage;
	}

	public ConversionPlanPage getConversionPlanPage() {
		return conversionPlanPage;
	}

	public void setConversionPlanPage(ConversionPlanPage conversionPlanPage) {
		this.conversionPlanPage = conversionPlanPage;
	}

	public ActionPage getActionPage() {
		return actionPage;
	}

	public void setActionPage(ActionPage actionPage) {
		this.actionPage = actionPage;
	}
}
