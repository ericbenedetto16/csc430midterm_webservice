package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/evaluations", produces={"application/json"}) 
public class EvaluationController {
	@GetMapping("/")
	public String getAllEvaluations() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PostMapping("/")
	public String createEvaluation() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
}
