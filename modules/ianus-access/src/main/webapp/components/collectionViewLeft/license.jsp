<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="de.ianus.access.web.utils.MDUtils" %>





<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
DataCollection dc = sessionBean.getDc();
MDUtils util = new MDUtils(dc, sessionBean);
Long wfipId = sessionBean.getWfip().getId();
String filePath = "web_assets/license_logo.png";
%>

<div class="item license">
    
    <h2>Lizenzhinweis</h2>
    
    <%
    String licenseName = dc.getLicenseName() + " " + dc.getLicenseVersion();
    if(!MDUtils.empty(util.getDataUriImage(wfipId, filePath, licenseName))) {
	    if(!MDUtils.empty(dc.getLicenseUrl())) { %>
		    <a href="<%= dc.getLicenseUrl() %>" target="_blank" class="no-bg">
		    	<%= util.getDataUriImage(wfipId, filePath, licenseName) %>
		    </a>
	    <% }else{ %>
	    	<%= util.getDataUriImage(wfipId, filePath, licenseName) %>
	    <% } %>
	    <br>
    <% }
    
    if(!MDUtils.empty(dc.getLicenseUrl())) { %>
	    <a href="<%= dc.getLicenseUrl() %>" class="license-link" target="_blank">
	    	Lizenzierung: <%= licenseName %>
	    </a>    
    <% }else{ %>
    	Lizenzierung: <%= licenseName %>
    <% } %>
</div>