<%@page import="de.ianus.metadata.bo.DataCollection" %>
<%@page import="de.ianus.access.web.utils.CFUtils"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />



<%
DataCollection dc = sessionBean.getDc();
CFUtils util = new CFUtils(sessionBean.getWfip(), sessionBean); 
MDUtils mutil = new MDUtils(sessionBean.getDc(), sessionBean);
%>



<h2 id="technicalDetail">Statistische Angaben</h2>


<div>
	<% if(!MDUtils.empty(mutil.getResourceTypeList())) { %>
		<%= mutil.getResourceTypeList() %>
	<%}
	if(!MDUtils.empty(mutil.getDataCategoryList())) { %>
		<%= mutil.getDataCategoryList() %>
	<%} %>
	
	<%= util.getFileSystemStats() %>
</div>