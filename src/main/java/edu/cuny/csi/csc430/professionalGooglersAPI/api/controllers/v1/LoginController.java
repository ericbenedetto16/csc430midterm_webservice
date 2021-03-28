package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.Credentials;

@RestController
public class LoginController {
	@PostMapping("/api/v1/login") 
	public String postBody(@RequestBody Credentials loginCredentials) {
		return "Hello " + loginCredentials.getUserName();
	}
}