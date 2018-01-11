/**
 * 
 */
package de.ianus.ingest.core.processEngine.ms.fits;

import java.io.File;

import de.ianus.ingest.core.utils.CoreProperties;

/**
 * @author hendrik
 * 
 * An adapter to the FITS CLI tool, 
 * as the integration of the genuine FITS Java API is problematic. 
 *
 */
public class Fits {
	
	
	private String fitsPath = "/usr/share/fits/fits-1.2.0/fits.sh";
	
	public static final String XML_NAMESPACE = "http://hul.harvard.edu/ois/xml/ns/fits/fits_output";
	
	public static String VERSION = "1.2.0";
	
	private CoreProperties props;
	
	public Fits() {
		this.props = new CoreProperties();
		this.fitsPath = this.props.get("fits.path");
	}
	
	
	public FitsOutput examine(File file) throws Exception {
		if(!new File(fitsPath).exists()) {
			throw new Exception("FITS Binary not found!");
		}
		
		String[] cmd = {fitsPath, "-i", file.getAbsolutePath()};
		
		Process process = Runtime.getRuntime().exec(cmd);
		FitsOutput fitsOutput = new FitsOutput(process);
	    
	    if(process.waitFor() == 0)
	    	return fitsOutput;
	    return null;
	}
	
	
}
