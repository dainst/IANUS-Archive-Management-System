package de.ianus.ingest.core.processEngine.ms.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.ianus.ingest.core.bo.InformationPackage;

/**
 * Requirement:
 * - A manifest must use the same checksum algorithm like its tagmanifest file (if it exists).
 * @author jurzua
 * @author zoes
 *
 */
public class ChecksumManifest {
	
	private String algorithm;
	
	//<FilePath, Checksum>
	private Map<String, String> checksumMap;
	private File manifestFile = null;
	private File tagManifestFile = null;
	
	public ChecksumManifest(File file, InformationPackage ip, String unpackedFileNameAsFolder) throws Exception{
		this.manifestFile = file;
		this.algorithm = manifestFile.getName().replace("manifest-", "").replace(".txt", "");
		
		File tmp = new File(ip.getMetadataUploadFolder() + "/" + unpackedFileNameAsFolder + "/" + "tagmanifest-" + this.algorithm + ".txt");
		if(tmp.exists()){
			this.tagManifestFile = tmp;
		}
		this.checksumMap = parseManifestFile(manifestFile);
	}
	
	private Map<String, String> parseManifestFile(File file) throws FileNotFoundException, IOException{
		Map<String, String> rs = new HashMap<String, String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	//checksum space filePath
		    	String[] array = line.split("\\s+", 2);
		    	rs.put(array[1], array[0]);
		    }
		}
		return rs;
	}
	
	public VerificationResult verifyFile(File file, String relativePath) throws NoSuchAlgorithmException, IOException{
		
		VerificationResult result = new VerificationResult();
		result.filePath = relativePath;
		result.checksumManifestFile = this.checksumMap.get(relativePath);
		result.fileFound = (StringUtils.isNotEmpty(result.checksumManifestFile));
		 
		if(!result.fileFound){
			System.out.println("AA");
		}
		
		result.checksumCalculated = calculateChecksum(file);
		result.result = StringUtils.equals(result.checksumManifestFile, result.checksumCalculated);
		
		return result;
	}
	
	private String calculateChecksum(File file) throws NoSuchAlgorithmException, IOException{
		MessageDigest md = null;
		
		if(StringUtils.contains(ChecksumAlgorithm.MD5.bagItAlgorithm, this.algorithm)){
			md = MessageDigest.getInstance(ChecksumAlgorithm.MD5.javaSecurityAlgorithm);
		}else if(StringUtils.contains(ChecksumAlgorithm.SHA1.bagItAlgorithm, this.algorithm)){
			md = MessageDigest.getInstance(ChecksumAlgorithm.SHA1.javaSecurityAlgorithm);
		} 
		
		byte[] dataBytes = new byte[1024];
		FileInputStream fis = new FileInputStream(file);
		
		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
		}
		fis.close();
		byte[] mdbytes = md.digest();

		//convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < mdbytes.length; j++) {
				sb.append(
						Integer.toString((mdbytes[j] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	public class VerificationResult{
		public String checksumCalculated;
		public String checksumManifestFile;
		public String filePath;
		public boolean fileFound;
		public boolean result;
		
		@Override
		public String toString(){
			return "VerificationResult: " + filePath + " " + result + " [" + checksumCalculated + ", " + checksumManifestFile + "], fileFound: " + fileFound;
		}
		
	}
	
	public boolean verifyConsistency() throws FileNotFoundException, IOException, NoSuchAlgorithmException{
		if(tagManifestFile == null){
			return true;
		}
		return verifyConsistency0();
	}
	
	private boolean verifyConsistency0() throws FileNotFoundException, IOException, NoSuchAlgorithmException{
		Map<String, String> map = parseManifestFile(this.tagManifestFile);
		String checksumTagManifest = map.get("manifest-" + this.algorithm + ".txt");
		String calculatedChecksum = calculateChecksum(manifestFile);
		return StringUtils.equals(checksumTagManifest, calculatedChecksum);
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public Map<String, String> getChecksumMap() {
		return checksumMap;
	}

	public File getManifestFile() {
		return manifestFile;
	}

	public File getTagManifestFile() {
		return tagManifestFile;
	}
	
	
}
