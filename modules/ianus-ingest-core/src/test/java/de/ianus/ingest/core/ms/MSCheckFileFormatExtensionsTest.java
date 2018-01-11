package de.ianus.ingest.core.ms;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.ianus.ingest.core.processEngine.ms.utils.CheckFileFormatExtensionsUtils;


/**
 * This test class grabs on the data from MS_TestFiles mock folder on the project. 
 * test all files, compare extensions of files with list of formats. 
 * and give the test result.
 * 
 * 
 * @author MR
 *
 */
@RunWith(Parameterized.class)
public class MSCheckFileFormatExtensionsTest {
	
	private String inputFile;
	private String expected;
	private String actual;
	
	private String resources = "src/test/resources";
	private String dipFolder = "dip_storage";
	private String dipID = "dip_ID";
	private String dataFolder = "data";
	
	
	private String dataPath = this.resources + "/"+ "MS_TestFiles" +"/" + this.dipFolder + "/" + this.dipID + "/" + this.dataFolder + "/" ;
	private CheckFileFormatExtensionsUtils checker;
	private Map<String, List<String>> result;
	

	
	public MSCheckFileFormatExtensionsTest(String input, String output){
		this.inputFile = input;
		this.expected = output;
	}
	
	@BeforeClass
	public static void startTheTest(){
		System.out.println("File Format Extenstion Test START");
		System.out.println();
	}
	
	@Parameters
	public static Collection<String[]> testCollections(){
		
		String expectedOutputs [][] = {	{"free_doc_File.doc", "Files with accepted file formats"},
										{"Protected_doc_File.doc", "Files with accepted file formats"},
										{"free_pdf_File.pdf", "Files with accepted file formats"},
										{"Protected_pdf_File.pdf", "Files with accepted file formats"},
										{"free_xls_File.xls", "Files with accepted file formats"},
										{"Protected_xls_File.xls", "Files with accepted file formats"},
										{"image1.jpeg", "Files with accepted file formats"},
										
										{"free_docx_File.docx", "Files with preferred file formats"},
										{"Protected_docx_File.docx", "Files with preferred file formats"},
										{"free_ODS_File.ods", "Files with preferred file formats"},
										{"Protected_ODS_File.ods", "Files with preferred file formats"},
										{"free_ODT_File.odt", "Files with preferred file formats"},
										{"Protected_ODT_File.odt", "Files with preferred file formats"},
										{"free_xlsx_File.xlsx", "Files with preferred file formats"},
										{"Protected_xlsx_File.xlsx", "Files with preferred file formats"},
										
										{"free_Zip_File.zip", "Files with not accepted file formats"},
										{"Protected_Zip_File.zip", "Files with not accepted file formats"}
										
									}; 
										
		return Arrays.asList(expectedOutputs);  
		
	}

	@Before
	public void setup() throws Exception{
		
		checker = new CheckFileFormatExtensionsUtils(dataPath + inputFile);
		
		result = checker.scanFileList();
		
		if(!result.isEmpty()){
			
			for(String fileList : result.keySet()){
				
				actual = fileList;
			}
		}
	}
	
	@Test
	public void testAccessRestrictionRecognitionFile() throws Exception{
		
		assertEquals(getExpected(),getActual());
	}
	
	@AfterClass
	public static void endTheTest(){
		System.out.println();
		System.out.println("File Format Extenstion Test DONE");
	}
	
	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

}
