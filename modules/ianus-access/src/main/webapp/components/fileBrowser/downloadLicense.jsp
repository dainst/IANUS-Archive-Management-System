<%@page import="de.ianus.metadata.bo.DataCollection"%>


<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
DataCollection dc = sessionBean.getDc();
%>


<div id="downloadLicense" style="display: none">
    
    <h2>Lizenzhinweis</h2>
    
	<a href="<%= dc.getLicenseUrl() %>" target="_blank" class="no-bg">
    	<img src="<%= sessionBean.getAppBean().getContext() %>/resources/images/CC-BY-SA.png"
    		alt="<%= dc.getLicenseName() + " " + dc.getLicenseVersion() %>">
    </a>
    
    <br>
    
    <a href="<%= dc.getLicenseUrl() %>" class="license-link" target="_blank">
    	<%= dc.getLicenseName() + " " + dc.getLicenseVersion() %>
    </a>
    
</div>
