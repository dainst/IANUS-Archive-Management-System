/**
 * 
 */
package de.ianus.ingest.core.ms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.jopendocument.util.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import de.ianus.ingest.core.bo.ActivityOutput;
import de.ianus.ingest.core.bo.WorkflowIP;
import de.ianus.ingest.core.processEngine.ms.utils.SpecialCharactersUtils;

/**
 * @author hendrik
 *
 */
public class SpecialCharactersUtilsTest {
	
	private WorkflowIP wfip;
	private ActivityOutput output;
	
	
	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	
	@Before
	public void beforeTest() throws IOException, Exception {
		// mock objects
		this.wfip = mock(WorkflowIP.class);
		when(this.wfip.getAbsolutePath()).thenReturn("");
		when(this.wfip.getDataFolder()).thenReturn(this.folder.getRoot().getAbsolutePath() + "/data");
		
		this.output = mock(ActivityOutput.class);
	}
	
	
	@Test
	public void shouldRemoveForbiddenCharsFromAllPathes() throws Exception {
		// replicate the test data into the temporary folder
		File target = this.folder.newFolder("data");
		FileUtils.copyDirectory(new File("src/test/resources/SpecialCharacter_TestFiles/data"), target);
		
		// make sure the above operation was successful
		String data = this.wfip.getDataFolder();
		System.out.println(data);
		File dataFile = new File(data);
		
		System.out.println("TestFiles ready: " + ((dataFile.exists()) ? "TRUE" : "FALSE"));
		assertTrue(dataFile.exists());
		assertTrue(new File(this.wfip.getDataFolder() + "/Bilder diverse").exists());
		assertTrue(new File(this.wfip.getDataFolder() + "/Bilder diverse/Le erzeichen.jpg").exists());
		
		SpecialCharactersUtils util = new SpecialCharactersUtils(this.output);
		util.replaceForbidden(this.wfip, dataFile);
		
		assertFalse(new File(this.wfip.getDataFolder() + "/Bilder diverse").exists());	// make sure the empty, wrongly named folder has been removed
		assertTrue(new File(this.wfip.getDataFolder() + "/Bilder_diverse/Le_erzeichen.jpg").exists());
		assertTrue(new File(this.wfip.getDataFolder() + "/Bilder_diverse/Le_erzeichen.jpg").isFile());
		assertFalse(new File(this.wfip.getDataFolder() + "/Bilder diverse/Le erzeichen.jpg").exists());
		
	}
	
}
