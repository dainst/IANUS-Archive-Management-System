<%@page import="de.ianus.metadata.bo.actor.Person"%>
<%@page import="de.ianus.access.web.utils.MDUtils"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<% MDUtils util = new MDUtils(sessionBean.getDc(), sessionBean); %>


<div class="item">
    <h2>Prim&auml;rforscher</h2>
    <%= util.getFullNameAuthorList() %>
</div>