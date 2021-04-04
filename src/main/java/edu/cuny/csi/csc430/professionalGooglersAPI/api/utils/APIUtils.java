package edu.cuny.csi.csc430.professionalGooglersAPI.api.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIUtils {
	public static final String LOGGED_OUT_JSON = "{\"success\": false, \"msg\": \"You Must Be Logged In to Access This Resource\"}";
	public static final String UNAUTHORIZED_JSON = "{\"success\": false, \"msg\": \"You Are Not Authorized to Access This Resource\"}";
	public static final String ERROR_JSON = "{\"success\": false, \"msg\": \"Error.\"}";
	public static final String BAD_REQUEST_JSON = "{\"success\": false, \"msg\": \"Bad Request.\"}";
	
	public static HasherObject hasher(byte[] salt, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			
			String encodedSalt = Base64.getEncoder().encodeToString(salt);
			String encodedHash = Base64.getEncoder().encodeToString(hash);
			
			return new HasherObject(encodedSalt, encodedHash);
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new HasherObject("", "");
		}
	}
	
	public static String createToken() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[24];
	    secureRandom.nextBytes(randomBytes);
	    
	    return Base64.getEncoder().encodeToString(randomBytes);
	}
	
	public static String baseJSON(Boolean success, String secondAttribute) {
		return "{\"success\": " + success + ", \"" + secondAttribute + "\":";
	}
	
	public static String baseJSON(Boolean success, String secondAttribute, Object o) {
		try {
			return "{\"success\": " + success + ", \"" + secondAttribute + "\":" + new ObjectMapper().writeValueAsString(o) + "}";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return baseJSON(false, "msg") + "Error While Parsing JSON" + "}";
		}
	}
}
