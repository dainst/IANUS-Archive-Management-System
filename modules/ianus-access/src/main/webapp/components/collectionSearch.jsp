<%@page import="de.ianus.access.web.utils.MDUtils"%>
<%@page import="de.ianus.metadata.bo.utils.TextAttribute"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="de.ianus.metadata.bo.DataCollection"%>
<%@page import="de.ianus.ingest.core.bo.DisseminationIP"%>
<%@page import="de.ianus.access.web.ApplicationBean"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<!-- SearchBar -->
<div class="form-group" id = 'searchBar' style = "width: 20vw; padding: 1vw; float:right;">
     <div class="input-group">
	      <input id="searchField" class="form-control typeahead" placeholder="Suche..." autofocus="autofocus" name="q" type="text">
	      <div class="submit-btn input-group-btn">
	          <button type="submit" class="btn btn-primary" style = "background-color: #164194;" onclick = 'setSearchTermAsGetParameter();'>
	          &nbsp;<span class="glyphicon glyphicon-search"></span>&nbsp;</button>
	      </div>
  	</div>
</div>



