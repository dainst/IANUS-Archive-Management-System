<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="de.ianus.ingest.core.bo.DisseminationIP"%>
<%@page import="de.ianus.access.web.ApplicationBean"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />




<h1 id="pagetitle">Übersicht Forschungsdaten</h1>

<div id="collectionsList" class="row">
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9">
		<ul>
			<% 
			for(DisseminationIP dip : sessionBean.getAppBean().getDipList()){ 
				DataCollection dc = Services.getInstance().getMDService().getDataCollection(dip.getMetadataId());
				MDUtils util = new MDUtils(dc, sessionBean);
				%>
				<li>
					
					<div class="preview">
						<a href="<%= sessionBean.getAppBean().getContext() %>/pages/collectionView.jsp?dipId=<%= dip.getId()%>">
							<img src="<%= ApplicationBean.getInstance().getContext() + "/components/getCollectionPreview.jsp?filePath=" + dip.getWebAssetsFolder() + "/preview.png" %>" alt="preview image">
						</a>
					</div>
					<div class="title">
						<h2>
							<a href="<%= sessionBean.getAppBean().getContext() %>/pages/collectionView.jsp?dipId=<%= dip.getId()%>">
								<%= util.getTextAttributeValue(TextAttribute.ContentType.title) %>
							</a>
						</h2>
					</div>
					<div class="information-header">
						<%= util.getProjectInformationHeader() %>
					</div>
					<div class="description">
						<%= MDUtils.shortText(util.getTextAttributeValue(TextAttribute.ContentType.summary), 170) %>
						<a href="<%= sessionBean.getAppBean().getContext() %>/pages/collectionView.jsp?dipId=<%= dip.getId()%>">mehr...</a>
					</div>
					
				</li>			
			<%}%>
			
		</ul>
	</div>
	
	
	<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
		
		<h2>Aktuelles</h2>
		<p>
			Dieses Datenportal liegt in einer frühen technischen und inhaltlichen Version vor. 
			Unsere weiteren Planungen werden
			<a href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#version-info"
				title="Datenportal Planung">hier</a> beschrieben.
		</p>
	
		<p>
			IANUS nimmt aktuell aufgrund des Auslaufens der DFG-Förderung keine Datensammlungen mehr an 
			(Stand: Dezember 2017).
			Weitere Details <a
				href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#dateneigentuemer"
				title="Daten archivieren">hier</a>.
		</p>
	
		<p>
			Wie Sie die Daten herunterladen, nachnutzen und zitieren können,
			erfahren Sie <a href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#datennutzer"
				title="Daten nutzen">hier</a>.
		</p>
	
	</div>
</div>

