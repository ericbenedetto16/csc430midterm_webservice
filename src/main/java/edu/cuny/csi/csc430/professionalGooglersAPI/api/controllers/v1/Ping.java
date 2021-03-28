package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
	@GetMapping(value="/ping", produces={"application/json"}) 
	public String sendJSON() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
}