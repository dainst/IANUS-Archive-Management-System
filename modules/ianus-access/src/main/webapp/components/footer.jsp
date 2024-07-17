<%@page import="de.ianus.access.web.SessionBean"%>
<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />


<div id="footer" class="container">
	<div class="left">
		<p><a href="http://www.ianus-fdz.de/projects/uebersicht/wiki/Impressum">Impressum</a></p>
		<p><a href="mailto:ianus@dainst.de">ianus@dainst.de</a></p>
	</div>
	
	<div class="right">
       
        <div>
            <div>
            	<p>Koordination</p>
	            <div id="daitext">
	            	<a href="http://www.dainst.org" target="_blank">Deutsches Arch&auml;ologisches Institut</a>
	            </div>
               	<div id="dailogo">
               		<a href="http://www.dainst.org" target="_blank">
               			<img alt="DAI Logo" src="<%= sessionBean.getAppBean().getContext() %>/resources/images/DAI-Logo.png">
               		</a>
               	</div>
        	</div>
        </div>
        
        <div>
            <p>F&ouml;rderung</p>
            <p id="dfglogo">
            	<a href="http://www.dfg.de/index.jsp" target="_blank">
            		<img src="<%= sessionBean.getAppBean().getContext() %>/resources/images/DFG-Logo.png" alt="DFG Logo">
            	</a>
            </p>
        </div>
    </div>
    
    <div class="mid">
		<div>
			<p>&copy; 2019 IANUS</p>
	        <p>
		        <a href="http://creativecommons.org/licenses/by-sa/4.0/" target="_blank">                   
		            <img src="<%= sessionBean.getAppBean().getContext() %>/resources/images/CC-BY-SA.png" alt="Creative Commons Lizenzvertrag">
		        </a>
	        </p>
	    </div>
	</div>
	
    <a id="top-link" title="Zum Seitenanfang" href="#">&uarr;</a>
    <div id="piwik-optout">
        <div><span id="piwik-span" class="hidden">&times; </span>Datenschutz</div>
        <iframe id="piwik-frame" class="hidden" src="https://piwik.dainst.org/index.php?module=CoreAdminHome&action=optOut&idSite=6"></iframe>
    </div>
</div>