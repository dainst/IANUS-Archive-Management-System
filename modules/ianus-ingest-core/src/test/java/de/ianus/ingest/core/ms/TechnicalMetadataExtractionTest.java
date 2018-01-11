package de.ianus.ingest.core.ms;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.bo.files.FileInstance;
import de.ianus.ingest.core.processEngine.ms.utils.TechnicalMetadataExtractionProcessor;

/**
 * @author hendrik
 *
 */
public class TechnicalMetadataExtractionTest {
	
	private WorkflowIP wfip;
	private FileInstance fi;
	private ActivityOutput output;
	
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	
	@Before
	public void beforeTest() throws IOException, Exception {
		// mock objects
		this.wfip = mock(WorkflowIP.class);
		when(this.wfip.getAbsolutePath()).thenReturn("");
		when(this.wfip.getDataFolder()).thenReturn("src/test/resources");
		
		this.fi = mock(FileInstance.class);
		when(this.fi.getId()).thenReturn((long) 4711);
		String path = "fixtures/dip_storage/dip_ID/data/Amis/!README_BildMetadaten.pdf";
		when(this.fi.getLocation()).thenReturn(path);
		DAOService dao = null;
		doNothing().when(this.fi).save(dao);
		
		this.output = mock(ActivityOutput.class);
		doNothing().when(this.output).errorPrint(null);;
	}
	
	
	@Test
	public void shouldExtractTechnicalMetadata() throws Exception {
		TechnicalMetadataExtractionProcessor proc = new TechnicalMetadataExtractionProcessor(this.wfip, this.output);
		proc.storeFileInfo(this.fi);
		// no assertions required, test passed if running without exceptions
	}
}
