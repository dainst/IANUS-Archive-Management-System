package de.ianus.ingest.core.processEngine.ms.utils;

public enum ChecksumAlgorithm{
	
	MD5 ("md5", "MD5"), SHA1 ("sha1", "SHA-1"), SHA256 ("sha256", "SHA-256"), SHA512 ("sha512", "SHA-512");
	
	public final String bagItAlgorithm;
	public final String javaSecurityAlgorithm;
	
	ChecksumAlgorithm(String bagItAlgorithm, String javaSecurityAlgorithm){
		this.bagItAlgorithm = bagItAlgorithm;
		this.javaSecurityAlgorithm = javaSecurityAlgorithm;
	}

	public String bagit(){
		return bagItAlgorithm;
	}
	
	public String javaSecurityAlg(){
		return javaSecurityAlgorithm;
	}
	
}
