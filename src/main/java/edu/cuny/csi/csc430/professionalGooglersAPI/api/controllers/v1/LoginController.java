package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.Credentials;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.UserRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.AuthenticationObject;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthenticationMiddleware;

@RestController
@RequestMapping(value="/api/v1/login", produces={"application/json"})
public class LoginController {
	@Autowired UserRepository userRepo;
	@Autowired SessionRepository sessionRepo;
	
	@PostMapping("/")
	public String postBody(@RequestBody Credentials loginCredentials) {
		try {
			User user = userRepo.findByEmail(loginCredentials.getUserName());
			
			if(user == null) return APIUtils.baseJSON(false, "msg") + "\"Invalid Username or Password.\"";
			
			AuthenticationObject authObject = AuthenticationMiddleware.auth(user, loginCredentials.getUserName(), user.getSalt(), loginCredentials.getPassword(), sessionRepo);
			
			if(authObject.getSuccess()) {
				return APIUtils.baseJSON(true, "access_token") + "\"" + authObject.getSessionToken() + "\"";
			}
			
			return APIUtils.baseJSON(false, "msg") + "\"Invalid Username or Password.\"";
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "msg") + "\"Error\"";
		}
	}
}