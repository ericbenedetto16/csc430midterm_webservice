package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/students", produces= {"application/json"})
public class StudentController {
	@GetMapping("/")
	public String getAllStudents() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PostMapping("/")
	public String createStudent() {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@GetMapping("/{id}")
	public String getStudentByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PatchMapping("/{id}")
	public String editStudentByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@DeleteMapping("/{id}")
	public String deleteStudentByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PostMapping("/{id}/enroll")
	public String enrollStudentByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PostMapping("/{id}/unenroll")
	public String unenrollStudentByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PostMapping("/{id}/grade")
	public String gradeStudentByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
	@PatchMapping("/{id}/grade")
	public String editStudentGradeByID(@PathVariable String id) {
		return "{\"success\": true, \"msg\": \"ok\"}";
	}
	
}