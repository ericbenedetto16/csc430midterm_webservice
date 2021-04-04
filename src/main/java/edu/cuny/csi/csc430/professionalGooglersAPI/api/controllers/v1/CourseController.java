package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthenticationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthorizationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.CoursePayload;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.CourseRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.EnrollmentRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.FacultyRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.Roles;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Course;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Faculty;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Enrollment;

@RestController
@RequestMapping(value = "/api/v1/courses", produces = {"application/json"})
public class CourseController {
	@Autowired CourseRepository courseRepo;
	@Autowired FacultyRepository facultyRepo;
	@Autowired SessionRepository sessionRepo;
	@Autowired EnrollmentRepository enrollmentRepo;

	@GetMapping("/") 
	public String getAllCourses() {
		try {
			Iterable<Course> courses = courseRepo.findAll();
			
			return APIUtils.JSONBuilder(true, "courses", courses);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
 	}
	
	@PostMapping("/")
	public String createCourse(@RequestHeader(value = "Authorization", required = false) String sessionToken, @RequestBody CoursePayload coursePayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Course course = new Course();
			
			Faculty teacher = facultyRepo.findByUserId(coursePayload.getTeacher());
			
			if(teacher == null) return APIUtils.BAD_REQUEST_JSON;
			
			course.setName(coursePayload.getName());
			course.setTeacher(teacher);
			courseRepo.save(course);
			
			return APIUtils.JSONBuilder(true, "course", course);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@GetMapping("/{id}")
	public String getCourseByID(@PathVariable Integer id) {
		try {
			Optional<Course> course = courseRepo.findById(id);

			if(course.isEmpty()) return APIUtils.JSONBuilder(false, "course", null);
					
			return APIUtils.JSONBuilder(true, "course", course.get());
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PatchMapping("/{id}") 
	public String editCourseByID(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String sessionToken, @RequestBody CoursePayload coursePayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);
	
			if(course.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			Faculty teacher = facultyRepo.findByUserId(coursePayload.getTeacher());
			
			if(teacher == null) return APIUtils.BAD_REQUEST_JSON;
			
			Course modifiedCourse = course.get();
			
			modifiedCourse.setName(coursePayload.getName());
			modifiedCourse.setTeacher(teacher);
			modifiedCourse = courseRepo.save(course.get());
			
			return APIUtils.JSONBuilder(true, "course", modifiedCourse);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@DeleteMapping("/{id}")
	public String deleteCourseByID(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String sessionToken) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);

			if(course.isEmpty()) return APIUtils.JSONBuilder(false, "msg", "Course Not Found.");
			
			enrollmentRepo.deleteByCourse(course.get());
			courseRepo.deleteById(id);
			
			return APIUtils.JSONBuilder(true, "msg", "Course Successfully Deleted.");
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@GetMapping("/{id}/students")
	public String getStudentsByCourseID(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String sessionToken) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole(), Roles.FACULTY.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);
			
			if(course.isEmpty()) return APIUtils.JSONBuilder(false, "msg", "Course Not Found"); 
			
			Iterable<Enrollment> enrollments = enrollmentRepo.findByCourse(course.get());
			
			return APIUtils.JSONBuilder(true, "students", enrollments);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PatchMapping("/{id}/teacher")
	public String assignTeacherToCourseByID(@PathVariable Integer id, @RequestBody Integer teacherId, @RequestHeader(value = "Authorization", required = false) String sessionToken) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);
			if(course.isEmpty()) return APIUtils.JSONBuilder(false, "msg", "Course Not Found");			
			
			Optional<Faculty> teacher = facultyRepo.findById(teacherId);
			if(teacher.isEmpty()) return APIUtils.BAD_REQUEST_JSON;		
			
			course.get().setTeacher(teacher.get());
			
			return APIUtils.JSONBuilder(true, "teacher", course.get().getTeacher());
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
}
