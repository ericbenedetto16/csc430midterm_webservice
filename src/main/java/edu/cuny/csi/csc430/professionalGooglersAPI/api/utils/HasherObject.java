package edu.cuny.csi.csc430.professionalGooglersAPI.api.utils;

public class HasherObject {
	private String encodedSalt;
	private String encodedHash;
	
	HasherObject(String encodedSalt, String encodedHash) {
		this.encodedSalt = encodedSalt;
		this.encodedHash = encodedHash;
	}
	
	public String getEncodedSalt() {
		return encodedSalt;
	}
	
	public String getEncodedHash() {
		return encodedHash;
	}
}
