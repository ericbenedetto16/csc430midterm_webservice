package edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware;

import java.util.Base64;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.AuthenticationObject;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.HasherObject;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Session;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

public class AuthenticationMiddleware {
	// Authenticate a User Logging In
	public static AuthenticationObject auth(User user, String username, String salt, String password, SessionRepository sessionRepo) {
		byte[] byte_salt = Base64.getDecoder().decode(user.getSalt());
		HasherObject encoded = APIUtils.hasher(byte_salt, password);

		// Check If Password Is Correct
		if(user.getPassword().equals(encoded.getEncodedHash())) {
			// Delete Any Active Sessions in Database
			sessionRepo.deleteByUser(user);
			
			// FIXME: Does Not Check for Collisions with Existing Session Tokens
			// Create New Session for User
			Session session = new Session();
			session.setUser(user);
			String sessionToken = APIUtils.createToken();
			session.setToken(sessionToken);
			
			sessionRepo.save(session);
			
			return new AuthenticationObject(true, sessionToken);
		}
		
		return new AuthenticationObject(false, null);
	}
	
	// Authenticate a User On Other Routes
	public static User auth(String sessionToken, SessionRepository sessionRepo) {
		if(sessionToken == null) return null;
		
		Session session = sessionRepo.findByToken(sessionToken.replace("Bearer ", ""));
		if(session != null) return session.getUser();
		
		return null;
	}
}
