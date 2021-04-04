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
			
			return APIUtils.baseJSON(true, "courses", courses);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "courses") + "[]}";
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
			
			if(teacher == null) return APIUtils.baseJSON(false, "msg") + "\"Bad Request.\"}";
			
			course.setName(coursePayload.getName());
			course.setTeacher(teacher);
			courseRepo.save(course);
			
			return APIUtils.baseJSON(true, "course", course);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "course") + null + "}";
		}
	}
	
	@GetMapping("/{id}")
	public String getCourseByID(@PathVariable Integer id) {
		try {
			Optional<Course> course = courseRepo.findById(id);

			return course.isEmpty() ? APIUtils.baseJSON(false, "course") + null + "}": APIUtils.baseJSON(true, "course", course.get());
		}catch(Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	@PatchMapping("/{id}") 
	public String editCourseByID(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String sessionToken, @RequestBody CoursePayload coursePayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);
	
			if(course.isEmpty()) return APIUtils.baseJSON(false, "msg") + "\"Bad Request.\"}";
			
			Faculty teacher = facultyRepo.findByUserId(coursePayload.getTeacher());
			
			if(teacher == null) return APIUtils.baseJSON(false, "msg") + "\"Bad Request.\"}";
			
			Course modifiedCourse = course.get();
			
			modifiedCourse.setName(coursePayload.getName());
			modifiedCourse.setTeacher(teacher);
			modifiedCourse = courseRepo.save(course.get());
			
			return APIUtils.baseJSON(true, "course", modifiedCourse);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "msg") + "\"Error\"}";
		}
	}
	
	@DeleteMapping("/{id}")
	public String deleteCourseByID(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String sessionToken) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);

			if(course.isEmpty()) return APIUtils.baseJSON(false, "msg") + "\"Course Not Found.\"}";
			
			enrollmentRepo.deleteByCourse(course.get());
			courseRepo.deleteById(id);
			
			return APIUtils.baseJSON(true, "msg") + "\"Course Sucessfully Deleted\"}";
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "msg") + "\"Error\"}";
		}
	}
	
	@GetMapping("/{id}/students")
	public String getStudentsByCourseID(@PathVariable Integer id, @RequestHeader(value = "Authorization", required = false) String sessionToken) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole(), Roles.FACULTY.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);
			
			if(course.isEmpty()) return APIUtils.baseJSON(false, "msg") + "\"Course Not Found.\"}"; 
			
			Iterable<Enrollment> enrollments = enrollmentRepo.findByCourse(course.get());
			
			return APIUtils.baseJSON(true, "students", enrollments);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "msg") + "\"Error\"}";
		}
	}
	
	@PatchMapping("/{id}/teacher")
	public String assignTeacherToCourseByID(@PathVariable Integer id, @RequestBody Integer teacherId, @RequestHeader(value = "Authorization", required = false) String sessionToken) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Course> course = courseRepo.findById(id);
			if(course.isEmpty()) return APIUtils.baseJSON(false, "msg") + "\"Course Not Found.\"}";			
			
			Optional<Faculty> teacher = facultyRepo.findById(teacherId);
			if(teacher.isEmpty()) return APIUtils.baseJSON(false, "msg") + "\"Bad Request.\"}";			
			
			course.get().setTeacher(teacher.get());
			
			return APIUtils.baseJSON(true, "teacher", course.get().getTeacher());
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.baseJSON(false, "msg") + "\"Error\"}";
		}
	}
}
