package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/faculty", produces= {"application/json"})
public class FacultyController {
	@GetMapping("/") 
	public String getAllFaculty() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PostMapping("/")
	public String createFaculty() {
		return "{\"success\": true, \"msg\": \"this is going to create a faculty member\"}";
	}
	
	
	@GetMapping("/{id}") 
	public String getFacultyByID(@PathVariable Integer id) {
		return "{\"success\": true, \"msg\": \"this is the faculty/:id route\", \"id\": " + id + "}";
	}
	
	@PatchMapping("/{id}")
	public String editFacultyByID(@PathVariable Integer id) {
		return "{\"success\": true, \"msg\": \"this will edit faculty by id\"}";
	}
	
	@DeleteMapping("/{id}")
	public String deleteFacultyByID(@PathVariable Integer id) {
		return "{\"success\": true, \"msg\": \"this will delete faculty by id\"}";
	}
}
