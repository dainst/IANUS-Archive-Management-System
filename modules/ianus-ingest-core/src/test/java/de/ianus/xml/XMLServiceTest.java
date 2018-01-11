package de.ianus.xml;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.xml.XMLService;
import de.ianus.metadata.bo.DataCollection;
import de.ianus.metadata.bo.utils.TextAttribute;
import de.ianus.metadata.bo.utils.TextAttribute.ContentType;


public class XMLServiceTest {
	
	private DataCollection dc;
	private WorkflowIP wfip;
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	@Before
	public void beforeTest() throws IOException, Exception {
		// mock the wfip
		this.wfip = mock(WorkflowIP.class);
		when(this.wfip.getId()).thenReturn((long)1);
		when(this.wfip.getMetadataId()).thenReturn((long)7551);
		when(this.wfip.getMetadataFolder()).thenReturn(this.folder.newFolder("metadata").getAbsolutePath());
		when(this.wfip.getDataFolder()).thenReturn(this.folder.newFolder("data").getAbsolutePath());
		
		// mock the data collection
		this.dc = new DataCollection();
		Set<TextAttribute> titles = new LinkedHashSet<TextAttribute>();
		TextAttribute title = new TextAttribute(ContentType.title, "eng");
		title.setValue("testtitle");
		titles.add(title);
		this.dc.setAttribute(ContentType.title, titles);
		this.dc.setLastChangeMetadata(new Date());
	}
	
	@Test
	public void shouldCreateMetsFile() throws Exception {
		XMLService xmlService = new XMLService(this.wfip, this.dc);
		xmlService.createMETS();
	}
	
	
	@Test
	public void shouldCreatePremisFile() throws Exception {
		XMLService xmlService = new XMLService(this.wfip, this.dc);
		xmlService.createPREMIS();
	}
	
}