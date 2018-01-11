package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.IOException;

import org.apache.log4j.Logger;


/**
 * Install clamAV (manual mode): sudo apt-get install clamav
 * 
 * 
 * Configure the proxy here:
 * /etc/clamav/freshclam.conf
 * 
 * @author jurzua
 *
 */
public class ClamAVUtils {

	private static Logger logger = Logger.getLogger(ClamAVUtils.class);
	
	/*
	 * freshclam is operated by a daemon, which updates the definitions once per hour
	 * check status: service clamav-freshclam status
	 * settings (updates 24 times per day): /etc/clamav/freshclam.conf
	 */
	
	public static ClamAVOutput execute(String pathToScanFolder) throws Exception{
		return scan(pathToScanFolder);
	}
	
	
	public static ClamAVOutput scan(String directory) throws IOException, InterruptedException{
		String[] cmd = {"clamscan", "-r", "--remove=yes", "--suppress-ok-results", directory};
		
		logger.info("Executing: clamscan on " + directory);
		
	    Process pb = Runtime.getRuntime().exec(cmd);
	    pb.waitFor();

	    ClamAVOutput output = new ClamAVOutput(pb);
	    
	    return output;
	}
	
	public static void main(String[] args){
		try {
			String out = scan("/home/jurzua/Downloads/").toString();
			System.out.println(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
