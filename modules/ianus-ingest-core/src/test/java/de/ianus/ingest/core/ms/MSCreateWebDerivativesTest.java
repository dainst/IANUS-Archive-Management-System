package de.ianus.ingest.core.ms;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.ianus.ingest.core.processEngine.ms.utils.WebDerivativesGenerator;

public class MSCreateWebDerivativesTest {
	
		
	private String resources = "src/test/resources";
	
	
	
	/**
	 * This test grabs on the data folder from a mock-DIP and
	 * replicates it's structure into a not pre-existing directory. 
	 * Each file will be represented by a "thumb_filename.png" icon preview, 
	 * and "preview_<px-width>_filename.png" previews for several screen resolutions. 
	 * @throws Exception 
	 */
	@Test
	public void shouldCreateWebDerivatives() throws Exception {
		
		String dipFolder = "dip_storage";
		// Dip_id will be provided by the workflow information package object (WFIP), 
		// which we're going to mock in a way, that it returns the string "dip_id".
		String dipID = "dip_ID";
		String dataFolder = "data";
		String derivativesFolder = "web_derivatives";
		
		String dipPath = this.resources + "/fixtures/" + dipFolder + "/" + dipID;
		//String dipPath = "/data/ianus-test/dip_storage/dip_7";
		String dataPath = dipPath + "/" + dataFolder;
		String derivativesPath = dipPath + "/" + derivativesFolder;
		
		deleteFile(new File(derivativesPath));
		
		WebDerivativesGenerator generator = new WebDerivativesGenerator(null, dataPath, derivativesPath);
		try {
			generator.recurse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		assertTrue("Folder web_derivatives was not created",
				new File(derivativesPath).exists());
		assertTrue("Thumb Image was not created",
				new File(derivativesPath + "/Amis/A2/zz_additional_data/Photo/thumb_AAArC-HP0000119-1-PB-77-A2-0-F-11_7.jpg.png").exists());
		assertTrue("Preview Image (w400) was not created",
				new File(derivativesPath + "/Amis/A2/zz_additional_data/Photo/preview_400_AAArC-HP0000119-1-PB-77-A2-0-F-11_7.jpg.png").exists());
		assertTrue("Preview Image (w800) was not created",
				new File(derivativesPath + "/Amis/A2/zz_additional_data/Photo/preview_800_AAArC-HP0000119-1-PB-77-A2-0-F-11_7.jpg.png").exists());
		assertTrue("Preview Image (w1600) was not created",
						new File(derivativesPath + "/Amis/A2/zz_additional_data/Photo/preview_1600_AAArC-HP0000119-1-PB-77-A2-0-F-11_7.jpg.png").exists());
	}
	
	private static void deleteFile(File element) {
	    if (element.isDirectory()) {
	        for (File sub : element.listFiles()) {
	            deleteFile(sub);
	        }
	    }
	    element.delete();
	}
	
	
	
	@Test
	public void shouldSupportSetOfAcceptedImageFormats() {
		String required[] = new String[] {"jpg","jpeg","png","gif","bmp",
				"tiff","tif",
				"jpeg2000","jpeg 2000",	// TODO: test if .jpx/.jp2 files map to this format name
				"raw","psd"				// TODO: test for raw format files
				//"dng","cpt","svg","pdf","ps","eps"
				};
		String names[] = ImageIO.getReaderFormatNames();
		for(String format : required) {
			assertTrue("Implementation does not support " + format, Arrays.asList(names).contains(format));
		}
	}
}
