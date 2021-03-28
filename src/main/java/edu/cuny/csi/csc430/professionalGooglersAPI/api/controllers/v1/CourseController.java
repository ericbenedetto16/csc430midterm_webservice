package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/courses", produces={"application/json"})
public class CourseController {
	@GetMapping("/") 
	public String getAllCourses() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@GetMapping("/{id}") 
	public String getCourseByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PatchMapping("/{id}") 
	public String editCourseByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@DeleteMapping("/{id}") 
	public String deleteCourseByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@GetMapping("/{id}/students") 
	public String getStudentsByCourseID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PatchMapping("/{id}/teacher") 
	public String assignTeacherToCourseByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
}
