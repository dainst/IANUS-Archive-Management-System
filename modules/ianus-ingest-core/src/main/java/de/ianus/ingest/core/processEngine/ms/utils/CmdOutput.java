package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * 
 * @author jurzua
 *
 */
public class CmdOutput {
	
	public String standardOutput;
	public String errorOutput;
	public int exitValue;
	private Long pid = null;
	
	public CmdOutput(Process process) throws IOException{
		this.standardOutput = loadStream(process.getInputStream());
		this.errorOutput = loadStream(process.getErrorStream());
		this.exitValue = process.exitValue();
		
		// on retrieval of child process PID:
	    // https://stackoverflow.com/questions/4750470/how-to-get-pid-of-process-ive-just-started-within-java-program
	    // only available for Java 9:
	    //pid = @process.getPid();
	    
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
	}
	
	public String loadStream(InputStream in) throws IOException{
		 String line;
		 StringBuilder sb = new StringBuilder();
		 BufferedReader input = new BufferedReader(new InputStreamReader(in));
		 while ((line = input.readLine()) != null) {
		      sb.append(line + "\n");
		 }
		 input.close();
		 return sb.toString();
	}
	
	public Long getPid() {
		return this.pid;
	}
	
}
