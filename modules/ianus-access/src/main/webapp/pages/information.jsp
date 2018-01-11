<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@page import="de.ianus.access.web.SessionBean"%>
<jsp:useBean id="sessionBean" class="de.ianus.access.web.SessionBean" scope="session" />
<% request.setAttribute("context", sessionBean.getAppBean().getContext()); %>




<t:mainTemplate>

	<jsp:attribute name="header">
      	<jsp:include page="../components/headerHome.jsp" />
    </jsp:attribute>
    
    <jsp:attribute name="footer">
      	<jsp:include page="../components/footer.jsp" />
    </jsp:attribute>
	
	
    
    <jsp:body>
    	<div class="row">
    <div class="col-md-9">
        
            <h2 id="willkommen">Willkommen beim Datenportal von IANUS</h2>
            
            <p>Auf diesen Seiten präsentieren wir Ihnen die ersten kuratierten und archivierten Datensammlungen, die über die Menüpunkte <b>Projektübersicht</b>, <b>Metadaten</b> und <b>Daten</b> erschlossen werden. Technisch und funktional handelt es sich noch um eine frühe Version (0.2) des Datenportals, die in den nächsten Monaten weiterentwickelt wird. Sukzessive werden Sie hier weitere Datenbestände aus altertumwissenschaftlichen Projekten finden.</p>
            <p>Wenn Sie eine Datensammlung ausgewählt haben, finden Sie unter <b>Projektübersicht</b> die wichtigsten Hauptinformationen:</p>
            <ul>
                <li>beteiligte Primärforscher und Institutionen</li>
                <li>Untersuchungsgebiet und geographische Bezüge</li>
                <li>ausführliche Beschreibungen des jeweiligen Projektes und seiner Forschungsdaten</li>
                <li>thematische Schlagwörter</li>
                <li>die wichtigsten Publikationen</li>
                <li>Lizenzangaben, wie die Daten nachgenutzt werden können</li>
            </ul>
            <p>Alle weiteren Informationen, die wir von Datengebern zu einer Datensammlung erhalten haben, werden vollständig unter <b>Metadaten</b> aufgeführt.</p>
            <p>Wenn Sie Dateien aus einer Datensammlung herunterladen möchten, können Sie sich die einzelnen digitalen Objekte (Bilder, Textdokumente, Tabellen, GIS, Bibliografien etc.) unter dem Menüpunkt <b>Daten</b> mit einem Vorschaubild und zugehörigen Metainfomationen anzeigen lassen, bevor Sie sich für einen Download entscheiden.</p>
            <p>In dieser Version des Datenportals werden bis auf Weiteres nur Forschungsdaten ohne Zugriffsbeschränkungen unter einer sogenannte offenen <a href="https://creativecommons.org/choose/">Creative-Commons-Lizenz</a> veröffentlicht. Dementsprechend werden die Daten &ndash; je nach Festlegung durch den Dateneigentümer &ndash; entweder mit einer <b>CC-BY</b> oder einer <b>CC-BY-SA</b>-Lizenz zur Verfügung gestellt.</p>
            <ul>
                <li>Eine <a href="https://creativecommons.org/licenses/by/3.0/deed.de" target="_blank">CC-BY-Lizenz</a> bedeutet, dass eine Person, die Daten nachnutzt, den Urheber der Daten zitieren muss</li>
                <li>Eine <a href="https://creativecommons.org/licenses/by-sa/3.0/deed.de" target="_blank">CC-BY-SA-Lizenz</a> bedeutet zusätzlich, dass ein neues Werk, das auf wiederverwendeten Daten basiert, nur unter derselben Lizenz wie die Originaldaten verbreitet werden darf (also wiederum CC-BY-SA)</li>
            </ul>
            <p>Die Rechte an den Daten liegen bei den jeweiligen Dateneigentümern, d.h. entweder bei individuellen Wissenschaftlern oder bei Institutionen. IANUS hat lediglich die nicht-exklusive Berechtigung, die Daten zu archivieren sowie online und kostenfrei zur Nachnutzung zur Verfügung zu stellen. Sie können alle Daten im Datenportal nachnutzen, um z.B. mit diesen neue Forschungsprojekte zu begründen, Sie müssen lediglich die Daten entsprechend den Lizenzen weiterverwenden. Wenn Sie die Datensammlung nur zitieren und keine Daten nachnutzen wollen, verwenden Sie unsere Zitationshinweise, die für jede Datensammlung links am Rand (unter Projektübersicht und Metadaten) angegeben wird.</p>
            
            <p id="version-info">
            	Das Datenportal in der Version 0.2 soll Ihnen einen ersten Eindruck von der basalen 
            	Bereitstellung von digitalen altertumswissenschaftlichen Forschungsdaten durch IANUS vermitteln. 
            	Viele Funktionalitäten sind darin noch nicht umgesetzt, die aber für 
            	die weitere Entwicklung geplant sind und sukzessive realisiert werden sollen, etwa:
            </p>
            <ul>
                <li>ein Feld für die Suche</li>
                <li>
                	einen geschützten Bereich mit individuellem Login, 
                	um - dort wo notwendig und durch die Dateneigentümer gewünscht - 
                	individuelle Zugriffsbedingungen zu berücksichtigen und Forschungsdaten 
                	zugriffsbasiert schützen zu können
                </li>
                <li>weitere Formen der Navigation durch einzelne Datenbestände</li>
                <li>einen direkten Upload-Bereich für Datengeber</li>
                <li>u.v.m.</li>
            </ul>
            <p>
            	Für Fragen zum Datenportal wenden Sie sich bitte an 
            	<a href="mailto:ianus@dainst.de">ianus@dainst.de</a>. 
            	Außerdem können Sie an der Verbesserung und Entwicklung des Portals beitragen, wenn Sie uns Ihre Meinung, Anregungen und konstruktive Kritik oder Fehler mitteilen. Hierfür wurde ein 
            	<a href="https://www.ianus-fdz.de/feedback" target="_blank">Feedback-Formular</a> eingerichtet.
            </p>
            <p>
            	Wir wünschen Ihnen viel Vergnügen beim Stöbern in unseren stetig wachsenden 
            	Datensammlungen und hoffen, dass wir Ihnen schon jetzt durch den offenen Zugang 
            	Ihre Forschungsarbeit erleichtern können.
            </p>
            <p>Ihr IANUS-Team</p>


            <a name="datennutzer"></a><h2>Informationen für Nutzer des Datenportals</h2>
            <h3>Was Sie im Datenportal finden?</h3>
            
            <p>Bei IANUS werden digitale Forschungsdaten, also Textdokumente, Tabellen, Fotos, Zeichnungen, Messdaten, GIS-Dateien, 3D-Daten u. v. m., die andere Forscher, Projekte und Institutionen erzeugt haben, archiviert und in diesem Datenportal bereitgestellt. Für die Qualität und Richtigkeit, die Auswahl und Vollständigkeit der fachwissenschaftlichen Informationen in einer Datensammlung sind daher die Primärforscher bzw. Dateneigentümer verantwortlich. IANUS führt im Rahmen der Kuratierung und Aufbereitung lediglich technische Maßnahmen durch, um die Verständlichkeit von Daten zu erhöhen, eine einheitliche Präsentation zu gewährleisten und eine qualitätvolle Erschließung anbieten zu können.</p>
            <p>Entsprechend der Archivierungs- und Erhaltungsstrategie von IANUS ist das primäre Ziel der Archivierung von Forschungsdaten der Erhalt von einmaligen, nicht reproduzierbaren Fachinhalten und nicht der Erhalt von Funktionen ursprünglich verwendeter Softwaresysteme oder Anwendungen. Damit wird bis zu einem bestimmten Umfang bewusst der Verlust von Funktionalitäten oder Darstellungsformen (z. B. bei Datenbanken, GIS-Systemen, 3D-Daten, Webapplikationen) durch die Erhaltungsmaßnahmen in Kauf genommen. Die im Datenportal bereitgestellten Daten sind daher hinsichtlich einer langfristigen technischen Interpretierbarkeit und einer semantischen Nachvollziehbarkeit optimiert und müssen je nach Nachnutzungsfall in lokale Systeme und Anwendungen individuell integriert werden. Hierbei unterstützt Sie IANUS gerne!</p>

            <h3>Was Sie nicht im Datenportal finden?</h3>
            <p>IANUS ist ein Forschungsdatenzentrum, das auf Archäologie und Altertumswissenschaften spezialisiert ist. Datensammlungen aus anderen Fachbereichen werden folglich, sofern sie nicht mit den Altertumswissenschaften verwandt sind (wie z. B. die Archäozoologie), nicht von IANUS angenommen oder nachgewiesen. Siehe hierzu ausführlich die Sammlungsstrategie von IANUS (collection policy) bei den Downloads rechts.</p>
            <p>Das Datenportal stellt keine Publikationsplattform im klassischen Sinne dar, in dem lediglich PDF-Dateien von Artikeln, Monografien u.ä. zum Download abgeben und veröffentlich werden. Je nach Datengeber können aber bereits publizierte Forschungsergebnisse in einer Datensammlung enthalten sein, um eine Einheit von Forschungsrohdaten und Auswertungsergebnissen zu erzielen. Wenn dies nicht erfolgt, werden alternativ Verweise auf zugehörige und bereits verfügbare Online-Ressourcen (z. B. universitäre Publikationssysteme) hergestellt.</p>

            
            <h3>Sie möchten die Daten im IANUS-Datenportal nachnutzen?</h3>
            
            <p>Dann beachten Sie bitte:</p>
            <ul>
                <li>dass eine Zustimmung zur Nachnutzungsvereinbarung dafür Voraussetzung ist, dieser muss vor dem ersten Downlad aktiv zugestimmt werden</li>
                <li>die korrekte Zitierung von nachgenutzten Daten, zu finden in jeder Datensammlung unter der Überschrift "Zitierhinweise"</li>
                <li>Bei Weiternutzung von Daten die unter der CC-BY-SA-Lizenz zugänglich sind, müssen neue Ergebnisse und Werke, die aus diesen abgeleitet sind, unter derselben Lizenz öffentlich zur Verfügung gestellt werden</li>
                <ul>
                    <li>Eine kommerzielle Nutzung des auf diesem Weg entstandenen Werks kann nicht verboten werden (CC-BY-NC ist nicht möglich)</li>
                    <li>Eine Weiterbearbeitung der abgeleiteten Werkes muss gestattet werden (CC-BY-ND ist nicht möglich)</li>
                    <li>Das Werk kann nicht durch offenere Lizenzen zur Verfügung gestellt werden (CC-BY ist nicht möglich)</li>
                </ul>
                <li>Bei Weiternutzung von Daten die unter der CC-BY-Lizenz zugänglich sind, muss der Autor/die Autoren genannt werden. Weitere Einschränkungen bestehen nicht.</li>
            </ul>

            <h3>An wen wende ich mich bei weiteren Fragen und Problemen?</h3>
            <p>Wenn Sie inhaltliche Fragen zu einer Datensammlung haben, kontaktieren Sie bitte die Person, die bei jeder Datensammlung als Ansprechperson angegeben ist. Bei allgemeinen Fragen zum Datenportal oder bei technischen Problemen wenden Sie sich bitte an das IANUS-Team, indem Sie eine E-Mail an <a href="mailto:ianus@dainst.de">ianus@dainst.de</a> schreiben. Wir beraten Sie gerne und freuen uns auf Ihre Rückmeldungen!</p>


            <a name="dateneigentuemer"></a><h2>Informationen für Dateneigentümer</h2>
            <h3>Sie besitzen eine Datensammlung, die archiviert und veröffentlich werden soll?</h3>
            
            <p>
            	Da die DFG-Förderung zum Ende des Jahres 2017 endete, 
            	werden bis auf weiteres keine Daten zur Archivierung mehr angenommen (Stand Dezember 2017).
            </p>

            <h3>Welche Rahmenbedingungen sollten die Datensammlungen erfüllen?</h3>
            <ul>
                <li>Die rechtlichen Fragen sollten geklärt sein: Wem gehören die Daten? Sind Sie berechtigt, die Daten an IANUS weiterzugeben?</li>
                <li>Die zu archivierenden Daten dürfen sich nicht mehr ändern - auch wenn ggf. eine Auswertung und Interpretation noch aussteht.</li>
                <li>Da in dem aktuellen Datenportal noch keine Möglichkeiten zur Zugriffsbeschränkung implementiert sind, sollte mindestens ein Teil der Datensammlung frei verfügbar gemacht werden können (unter den Lizenzen CC-BY oder CC-BY-SA)</li>
            </ul>
            <p>Die DFG positioniert sich zur Auswahl der Lizenzierung von online bereitgestellten altertumswissenschaftlichen Forschungsdaten wie folgt:</p>
            <ul>
                <li>Präferiert: Die DFG empfiehlt klar die Lizensierung von altertumswissenschaftlichen Forschungsdaten aus DFG-Projekten unter einer CC-BY-SA-Lizenz.</li>
                <li>Akzeptiert: Alternativ können Forschungsdaten aus DFG-Projekten aber auch unter einer CC-BY-Lizenz zur Nachnutzung bereitgestellt werden.</li>
            </ul>

            <h3>Welche Vorbereitungen sind notwendig?</h3>
            <ul>
                <li>Die Daten sollten so beschrieben sein, dass sowohl die Datenkuratoren von IANUS als auch potenzielle Nachnutzer diese verstehen können.</li>
                <li>Sie sollten in bestimmten akzeptierten oder präferrierten Formaten vorliegen oder in diese umgewandelt werden, damit sie von IANUS technisch kuratiert werden können. Ausführliche Informationen dazu finden Sie in den <a href="https://www.ianus-fdz.de/it-empfehlungen/archivierung">IT-Empfehlungen</a></li>
                <li>Es sollte eine sinnvolle Auswahl der zu archivierenden Daten getroffen werden.</li>
                <li>Die Benennung und Strukturierung von Ordnern und Dateien sollte nachvollziehbar sein.</li>
                <li>Personenbezogene Angaben sollten vor der Abgabe gelöscht oder anonymisiert werden.</li>
                <li>Es sollte sichergestellt sein, dass die Daten frei von Viren oder Schadsoftware sind und keine Zugriffsbeschränkungen besitzen oder aber Passwörter vorliegen.</li>
                <li>Es sollte ein Ansprechpartner festgelegt werden, der die erforderlichen Metadaten liefern kann und für Rückfragen, die im Laufe der weiteren Aufbereitung auftreten, zur Verfügung steht.</li>
            </ul>

            <h3>Worin liegen die Vorteile der Archivierung und offenen Bereitstellung von Forschungsdaten?</h3>
            <p>
            	Neben der Tatsache, dass es sich um eine zentrale 
            	<a href="http://www.dfg.de/download/pdf/dfg_im_profil/reden_stellungnahmen/download/empfehlung_wiss_praxis_1310.pdf" target="_blank">
            		Empfehlung der DFG zur Sicherung guter wissenschafticher Praxis
            	</a>
            	handelt, gibt es weitere Vorteile.
            </p>
            <p>Mehrwert für ForscherInnen:</p>
            <ul>
                <li>häufigere Zitation der eigenen Forschungsleistung</li>
                <li>neue zitierfähige Publikationsmöglichkeit</li>
                <li>Forschungsdaten werden für die Zukunft gesichert und nutzbar gehalten</li>
                <li>es können neue Kollaborationen / Kooperationen entstehen</li>
                <li>höhere wissenschaftliche Aufmerksamkeit und berufliche Anerkennung</li>
                <li>Erfüllung von Auflagen von Drittmittelgebern</li>
            </ul>
            <p>Mehrwert für die wissenschaftliche Fachgemeinschaft:</p>
            <ul>
                <li>passende Forschungsdaten können schneller und einfacher aufgefunden werden</li>
                <li>bessere Nachvollziehbarkeit von Methodik und Ergebnissen</li>
                <li>neue Forschungsfragen und -methoden werden ermöglicht</li>
                <li>Vermeidung teurer Zweiterhebungsverfahren</li>
                <li>Nachnutzung von Forschungsdaten, die zur Ausbildung und Weiterqualifizierung genutzt werden können</li>
            </ul>
        
    </div>
    <div class="col-md-3">
        
            <h2>Downloads</h2>
            <ul>
                <li><a href="${context}/resources/documents/Nutzungsbedingungen.pdf" target="_blank">Nutzungsvereinbarung</a></li>
                <li><a href="${context}/resources/documents/Nutzungsbedingungen_annotiert.pdf" target="_blank">Nutzungsvereinbarung mit Erklärungen</a></li>
                <li><a href="${context}/resources/documents/Sammlungsstrategie.pdf" target="_blank">Sammlungsstrategie</a></li>
                <li><a href="${context}/resources/documents/Metadaten-Uebersicht.pdf" target="_blank">Metadaten-Übersicht</a></li>
                <li><a href="${context}/resources/documents/Metadaten-Formulare.zip">Metadaten-Formulare</a></li>
                <li><a href="${context}/resources/documents/Datenuebergabevertrag.pdf" target="_blank">Datenübergabevertrag</a></li>
                <li><a href="${context}/resources/documents/Datenuebergabevertrag_annotiert.pdf" target="_blank">Datenübergabevertrag mit Erklärungen</a></li>
                <li><a href="${context}/resources/documents/Datenschutzerklaerung.pdf" target="_blank">Datenschutzerklärung</a></li>
                <li><a href="${context}/resources/documents/Entgelt-und-Leistungsverzeichnis.pdf" target="_blank">Entgelt- und Leistungsverzeichnis</a></li>
                <li><a href="${context}/resources/documents/Nachfolgeplan.pdf" target="_blank">Nachfolgeplan</a></li>
            </ul>
            <h2>Weiterführende Links</h2>
            <ul>
                <li><a href="https://www.ianus-fdz.de/it-empfehlungen/archivierung" title="Vorgaben zur Archivierung bei IANUS" target="_blank">Vorgaben zur Archivierung bei IANUS</a></li>
                <li><a href="https://www.ianus-fdz.de/it-empfehlungen/datenmanagement" title="Datenmanagement" target="_blank">Datenmanagement</a></li>
                <li><a href="https://www.ianus-fdz.de/it-empfehlungen/glossar" title="Glossar IT-Empfehlungen" target="_blank">Glossar IT-Empfehlungen</a></li>
            </ul>
            
            <br>
            <br>
            <h2>Koordination</h2>
            <div>
                <a href="https://www.dainst.org/de/" class="image-link" target="_blank">
                	<img src="${context}/resources/images/DAI-Logo-Text.png" alt="Logo DAI">
                </a>
            </div>
            <h2>Förderung</h2>
            <div>
                <a href="http://www.dfg.de/index.jsp" class="image-link" target="_blank">
                	<img src="${context}/resources/images/DFG-Logo.png" alt="Logo DFG">
                </a>
            </div>

        
    </div>
</div>
    </jsp:body>
    
</t:mainTemplate>