<%@page import="com.hp.hpl.jena.vocabulary.DC"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="de.ianus.ingest.core.bo.DisseminationIP"%>
<%@page import="de.ianus.access.web.ApplicationBean"%>
<%@page import ="org.apache.commons.collections.CollectionUtils"%>
<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<h1 id="pagetitle">Übersicht Forschungsdaten</h1>

<%
// get searchTerm:
String searchTerm = request.getParameter("q");
%>

<div id="collectionsList" class="row">
	<div class="col-lg-9 col-md-9 col-sm-9 col-xs-9">
		<ul> 
			<%	// get data as dipList
				List<DisseminationIP> dipList = sessionBean.getAppBean().getDipListbySearchTerm(searchTerm);
			
				if(CollectionUtils.isEmpty(dipList)) {
					
					// show no-result-notification:
					%><p>Keine Forschungsdaten vorhanden. Überprüfen Sie bitte Ihre Sucheingabe.</p> <%
				}
				else {
					
					// show data:
					for(DisseminationIP dip : sessionBean.getAppBean().getDipListbySearchTerm(searchTerm)){ 					// list of IDs as key to data	
			
						DataCollection dc = Services.getInstance().getMDService().getDataCollection(dip.getMetadataId());		// collections stored in metadata.DataCollection
						MDUtils util = new MDUtils(dc, sessionBean);															// utilities to retrieve and handle futher metadata
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
					<%}
				};%>
			
		</ul>
	</div>
	
	
	<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">
		
		<p>
			<font color = "red">!!!</font color> <b> IANUS nimmt wieder Daten an</b>. Seit dem Auslaufen der DFG-Förderung gab es eine lange Pause.
			Details zum Archivieren mit IANUS finden Sie 
			<a style="font-weight:bold" href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#dateneigentuemer"
				title="Informationen für Dateneigentuemer">hier</a>. 
		</p>
		
		<p>
			Dieses Datenportal wird ständig weiterentwickelt. 
			Unsere weiteren Planungen werden
			<a style="font-weight:bold" href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#version-info"
				title="Datenportal Planung">hier</a> beschrieben.
		</p>
	

		<p>
			Wie Sie die Daten herunterladen, nachnutzen und zitieren können,
			erfahren Sie <a style="font-weight:bold" href="<%= sessionBean.getAppBean().getContext() %>/pages/information.jsp#datennutzer"
				title="Daten nutzen">hier</a>.
		</p>
	
	</div>
</div>

