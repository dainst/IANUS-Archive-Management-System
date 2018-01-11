<%@page import="de.ianus.access.web.utils.CFUtils"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page contentType="application/json" %>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />


<%
response.setContentType("application/json");
response.setHeader("Content-Disposition", "inline");

CFUtils util = new CFUtils(sessionBean.getWfip(), sessionBean);


Long wfipId = sessionBean.getWfip().getId();


JSONArray json1 = util.getFolderJsonTree(wfipId);
out.print(json1.toJSONString());
%>