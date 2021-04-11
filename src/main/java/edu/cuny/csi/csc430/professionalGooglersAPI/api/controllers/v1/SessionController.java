package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthenticationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/token", produces={"application/json"})
public class SessionController {
    @Autowired SessionRepository sessionRepo;

	@PostMapping("/")
	public String validateSession(@RequestHeader(value = "Authorization", required = false) String sessionToken) {
        try {
            User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
            if(user == null) return APIUtils.LOGGED_OUT_JSON;
            
            return APIUtils.JSONBuilder(true, "user", user);
        }catch(Exception e) {
            e.printStackTrace();

            return APIUtils.ERROR_JSON;
        }
	}
}