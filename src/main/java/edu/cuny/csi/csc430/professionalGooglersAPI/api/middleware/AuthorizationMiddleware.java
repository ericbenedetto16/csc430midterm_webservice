package edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware;

import java.util.HashSet;

import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

public class AuthorizationMiddleware {
	public static Boolean authorize(User user, HashSet<String> allowedRoles) {
		if(allowedRoles.contains(user.getRole().getRoleName())) {
			return true;
		}
		return false;
	}
}
