package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author jurzua, hschmeer
 *
 */
public class ClamAVOutput  extends CmdOutput{

	public static Map<Integer, String> returnCodes;
	
	static{
		returnCodes = new HashMap<Integer, String>();
		returnCodes.put(0, "No virus found.");
		returnCodes.put(1, "Virus(es) found.");
		returnCodes.put(2, "Some error(s) occured.");
	}

	public Integer knownViruses;
	public String  engineVersion;
	public Integer scannedDirectories;
	public Integer scannedFiles = 0;
	public Integer infectedFiles = 0;
	public String  dataScanned;
	public String  dataRead;
	public String  time;
	
	private Long startTime;
	
	
	public ClamAVOutput(Process process) throws IOException, InterruptedException {
		super(process);
		this.startTime = System.currentTimeMillis();
		loadClamAVOutput();
	}
	
	private void loadClamAVOutput() throws IOException{
		String line=null;
		Boolean summary = false;
		BufferedReader reader = new BufferedReader(new StringReader(this.standardOutput));
		while( (line = reader.readLine()) != null ) {
			if(!summary) {
				this.scannedFiles++;
				if(line.contains("--- SCAN SUMMARY ---")) {
					summary = true;
				}
			}else{
				String value = null;
				if((value=startsWith(line, "Known viruses: ")) != null){
					this.knownViruses = Integer.parseInt(value);
				}else if((value=startsWith(line, "Engine version: ")) != null){
					this.engineVersion = value;
				}else if((value=startsWith(line, "Scanned directories: ")) != null){
					this.scannedDirectories = Integer.parseInt(value);
				}else if((value=startsWith(line, "Scanned files: ")) != null){
					this.scannedFiles = Integer.parseInt(value);
				}else if((value=startsWith(line, "Infected files: ")) != null){
					this.infectedFiles = Integer.parseInt(value);
				}else if((value=startsWith(line, "Data scanned: ")) != null){
					this.dataScanned = value;
				}else if((value=startsWith(line, "Data read: ")) != null){
					this.dataRead = value;
				}else if((value=startsWith(line, "Time: ")) != null){
					this.time = value;
				}
			}
		}
		reader.close();
	}
	
	private String startsWith(String input, String prefix){
		if(StringUtils.startsWith(input, prefix)){
			return input.replace(prefix, "");
		}
		return null;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("Scanned files: " + this.scannedFiles + "\n");
		sb.append("Infected files: " + this.infectedFiles + "\n");
		sb.append("\n");
		
		return sb.toString();
	}
}
