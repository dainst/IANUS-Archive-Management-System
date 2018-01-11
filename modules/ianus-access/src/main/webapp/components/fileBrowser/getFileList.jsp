<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="de.ianus.access.web.utils.CFUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="de.ianus.ingest.core.bo.WorkflowIP"%>
<%@page import="de.ianus.ingest.core.Services"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%


String directory = request.getParameter("directory");

directory = URLEncoder.encode(directory, "ISO-8859-1"); // H%C3%A9l%C3%A8ne

directory = URLDecoder.decode(directory, "UTF-8"); // and finally : testüß

directory = CFUtils.stripSlashes(directory);

Long wfipId = sessionBean.getWfip().getId();

String clientWidth = "1600";
Cookie[] cookies = request.getCookies();
if(cookies != null) {
 	for (Cookie cookie : cookies)
   		if(cookie.getName().equals("clientWidth"))
   			clientWidth = cookie.getValue();
}


if(StringUtils.isNotEmpty(directory)){
	
	WorkflowIP dip = Services.getInstance().getDaoService().getWorkflowIP(wfipId, sessionBean.getWfipClass());
	if(dip == null){
		throw new Exception("An IP with id=" + wfipId + "has not been found");
	}
	CFUtils fileUtil = new CFUtils(dip, sessionBean);
	
	
	List<File> files = fileUtil.getFileList(directory);
	
	int i = 0;
	for(File file : files) {
		// construct a download URL
		String fileURL = CFUtils.getFileURL(directory + "/" + file.getName());
		%>
		<div class="fileItem">
		
			<table>
				<tr>
					<td class="left">
						<a data-fancybox="group1" href="javascript:;" data-src="#preview-<%= i %>">
							<%= fileUtil.getFileIcon(directory, file.getName()) %>
						</a>
					</td>
					<td class="center">
						<a data-fancybox="group2" href="javascript:;" data-src="#preview-<%= i %>">
							<%= file.getName() %>
						</a>
					</td>
					<td class="right">
						<p><%= CFUtils.getReadableFileSize(file) %></p>
						<p><a class="licensed" href="<%= fileURL %>">Download</a></p>
					</td>
				</tr>
			</table>
			
			<div id="preview-<%= i %>" class="previewOverlay" style="display: none">
				<div class="flex-wrapper">
					<div class="info">
						<p class="strong"><%= file.getName() %></p>
						<p>Dateigröße: <%= CFUtils.getReadableFileSize(file) %></p>
						
						<jsp:include page="../collectionViewLeft/license.jsp"/>
						<p><a href="<%= fileURL %>">Download file</a></p>
					</div>
					
					<div class="main">
						<%= fileUtil.getPreview(directory, file.getName(), clientWidth) %>
						
					</div>
				</div>
			</div>
		</div>
		<%
		i++;
	}
}
%>







