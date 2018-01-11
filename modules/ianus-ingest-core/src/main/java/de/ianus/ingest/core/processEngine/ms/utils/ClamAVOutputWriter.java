package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.DAOService;
import de.ianus.ingest.core.bo.RsyncUpload;


public class ClamAVOutputWriter implements Runnable {
	
	private DAOService daoService;
	private RsyncUpload upload;
	private Process process;
	private String logfile;
	private Long pid = null;
	
	private Long startTime;
	private Integer scannedFiles = 0;
	
	private Integer updateInterval = 50;	// update progress field in database every n files
	
	
	private static final Logger logger = LogManager.getLogger(ClamAVOutputWriter.class);
	
	
	
	@Override
	public void run() {
		try {
			writeClamAVOutStream();
			writeClamAVErrorStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ClamAVOutputWriter(DAOService daoService, RsyncUpload upload, Process process, String logfile) throws Exception {
		/* on retrieval of child process PID:
	     * https://stackoverflow.com/questions/4750470/how-to-get-pid-of-process-ive-just-started-within-java-program
	     * only available for Java 9:
	     * pid = @process.getPid();
	     */
		this.daoService = daoService;
		this.startTime = System.currentTimeMillis();
		this.process = process;
		this.upload = upload;
		this.logfile = logfile;
		
		try {
    		// on other platforms, the PID field is not present (eg. use handle on Win) 
	    	if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
    	        Field f = process.getClass().getDeclaredField("pid");
    	        f.setAccessible(true);
    	        this.pid = f.getLong(process);
    	        f.setAccessible(false);
    		}
	    }catch(Exception e) {
	    	this.pid = null;
	    }
		
		logger.info("Started virus scan with PID " + this.pid);
		
		upload.setScanPid(this.pid);
		upload.setStatus(RsyncUpload.State.virus_scan);
		upload.setScanStartTime(this.startTime);
		upload.setScannedFiles(0);
		this.daoService.saveDBEntry(upload);
	}
	
	
	private void writeClamAVOutStream() throws Exception {
		InputStream in = this.process.getInputStream();
		FileWriter fw = new FileWriter(this.logfile, false);	// create a new file (false)
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println("### Virus Scan Log after RsyncUpload #### \n");
		out.println("## InformationPackage-relative path: " + this.upload.getTarget() + "\n");
		out.println("### Virus scan standard Output for uploaded data:");
		
		Boolean summary = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while( (line = reader.readLine()) != null ) {
			if(!summary) {
				if(line.contains("--- SCAN SUMMARY ---")) summary = true;
				if(!summary) {
					if(!line.trim().equals("")) this.scannedFiles ++;
					if(this.scannedFiles % this.updateInterval == 0) {
						this.updateScanProgress();
					} 
					if(line.endsWith(": OK")) continue;
				}
				
			}
			out.println(line);
		}
		reader.close();
		out.close();
	}
	
	
	private void writeClamAVErrorStream() throws IOException {
		InputStream in = this.process.getErrorStream();
		FileWriter fw = new FileWriter(this.logfile, true);	// append to the file (true)
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println("\n### Virus scan Error Output for uploaded data:");
		
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		while ((line = input.readLine()) != null) {
			out.println(line);
		}
		input.close();
		out.close();
	}
	
	
	public Long getPid() {
		return this.pid;
	}
	
	
	private void updateScanProgress() throws Exception {
		this.upload.setScannedFiles(this.scannedFiles);
		this.daoService.saveDBEntry(this.upload);
	}
	
	
	
	
	
	

}
