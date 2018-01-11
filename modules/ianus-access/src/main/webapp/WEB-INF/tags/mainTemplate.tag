<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@tag import="de.ianus.access.web.SessionBean"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<jsp:setProperty name="sessionBean" property="pageContext" value="${pageContext}" />

<%
HttpServletResponse httpResponse = (HttpServletResponse) response;
httpResponse.addHeader("Cache-Control", "max-age=1800");
/*
httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
httpResponse.setDateHeader("Expires", 0); // Proxies.
*/
%>


<!DOCTYPE html>
<html lang="de">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
		<!-- turned off due to problems with mobile devices, as width is limited to >= 475px -->
		<!-- <meta name="viewport" content="width=device-width, initial-scale=1"> -->
		
		<title>IANUS Datenportal</title>
		<meta name="description" content="Forschungsdatenarchiv der Archäologie und Altertumswissenschaften">
		<meta name="author" content="IANUS">
		
		<link href="<%= sessionBean.getAppBean().getContext() %>/resources/images/favicon.ico" 	type="image/x-icon" rel="icon">
		<link href="<%= sessionBean.getAppBean().getContext() %>/resources/images/favicon.ico" 	type="image/x-icon" rel="shortcut icon">
		
		
		<!-- ########### jQuery v2.0.2 ##############  -->
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/jquery.min.js"></script>
		
		<!--<script src="//code.jquery.com/jquery-3.1.1.min.js"></script>-->
		
		<!-- ########### bootstrap ##############  -->
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/bootstrap/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/bootstrap/ie10-viewport-bug-workaround.js"></script>
		
		
		<!-- ########### jsTree Library ##############  -->
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/jsTree/jstree.min.js"></script>
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/jsTree/jquery.hotkeys.js"></script>
		<link href="<%= sessionBean.getAppBean().getContext() %>/resources/js/jsTree/style.min.css" rel="stylesheet" type="text/css" media="screen" />
		
		<!-- ########### fancyBox Library ##############  -->
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/fancyBox3.0/jquery.fancybox.min.js"></script>
		<link href="<%= sessionBean.getAppBean().getContext() %>/resources/js/fancyBox3.0/jquery.fancybox.min.css" rel="stylesheet" type="text/css" media="screen" />
		
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/js.cookie.js"></script>
		
		
		<!-- ########### leaflet ##############  -->
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/leaflet/leaflet.js"></script>
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/leaflet/wicket.js"></script>
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/leaflet/wicket-leaflet.js"></script>
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/leaflet/leaflet-fullscreen.min.js"></script>
		<link href="<%= sessionBean.getAppBean().getContext() %>/resources/css/leaflet/leaflet.css" rel="stylesheet" type="text/css" media="screen" />
		<link href="<%= sessionBean.getAppBean().getContext() %>/resources/css/leaflet/leaflet-fullscreen.css" rel="stylesheet" type="text/css" media="screen" />
		
		
		<!-- ########### application styles ##############  -->
		<link href="<%= sessionBean.getAppBean().getVersionedUrl("/resources/css/styles.css") %>" type="text/css" rel="stylesheet">
		
		
		<!-- contextURL is used to display static assets from collectionNavbar.js (ie. nutzungsvereinbarung) -->
		<script type="text/javascript"> var contextURL = '<%= sessionBean.getAppBean().getContext() %>'; </script>
		
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getVersionedUrl("/resources/js/common.js") %>"></script>
    	
    	
	</head>
	
  	<body>

		<div id="account" class="container">
			<ul>
				<li>
				</li>
			</ul>
		</div>
		
		<jsp:invoke fragment="header"/>
		  	
		<div id="content" class="container">
      		<jsp:doBody/>
    	</div>
    			
		<div class="container">
			<div class="ribbon up"><div></div></div>
		</div>
		
		
		<jsp:invoke fragment="footer"/>
		
		<!-- ================================================== -->
		 
		
		
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/toplink.js"></script>
		<script type="text/javascript" src="<%= sessionBean.getAppBean().getContext() %>/resources/js/piwikbutton.js"></script>
		
    	
	</body>
</html>