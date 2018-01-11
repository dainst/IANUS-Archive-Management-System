<%@page import="de.ianus.ingest.core.bo.DisseminationIP"%>
<%@page import="de.ianus.ingest.core.bo.WorkflowIP"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page trimDirectiveWhitespaces="true" %>


<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
int dipListSizeOld = sessionBean.getAppBean().getDipList().size();
sessionBean.getAppBean().setDipList(Services.getInstance().getDaoService().getDipList(WorkflowIP.FINISHED, 10));
int dipListSizeNew = sessionBean.getAppBean().getDipList().size();

try {
	StringBuilder sb = new StringBuilder();
	sb.append("\n*************************************************\n");
	for(DisseminationIP dip : Services.getInstance().getDaoService().getDipList(null, -1)){
		sb.append(dip.getId() + "\t" + dip.getAbsolutePath() + "\n");
	}
	sb.append("\n*************************************************\n");
	System.out.println(sb.toString());
} catch (Exception e) {
	e.printStackTrace();
}
%>

<!DOCTYPE html>
<html>
<body>

<h1>This script updated the list of DIPs in the Application Session of the access module.</h1>
<p>Amount of DIP before update: <%=dipListSizeOld %>.</p>
<p>Amount of DIP after update: <%=dipListSizeNew %>.</p>

</body>
</html>

