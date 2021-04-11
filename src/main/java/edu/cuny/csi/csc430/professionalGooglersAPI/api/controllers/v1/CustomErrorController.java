package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RestController
public class CustomErrorController implements ErrorController {
	@RequestMapping("/error")
	public String handleError() {
		return "Requested Resource Not Found";
	}
	
	@Override
	public String getErrorPath() {
		return null;
	}
}
