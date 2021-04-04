package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.EnrollmentPayload;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.StudentPayload;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.CourseRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.EnrollmentRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.RoleRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.StudentRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.UserRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.HasherObject;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.Roles;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Course;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Enrollment;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Role;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Student;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

@RestController
@RequestMapping(value="/api/v1/students", produces={"application/json"})
public class StudentController {
	@Autowired StudentRepository studentRepo;
	@Autowired SessionRepository sessionRepo;
	@Autowired RoleRepository roleRepo;
	@Autowired UserRepository userRepo;
	@Autowired CourseRepository courseRepo;
	@Autowired EnrollmentRepository enrollmentRepo;
	
	@GetMapping("/")
	public String getAllStudents() {
		try {
			Iterable<Student> students = studentRepo.findAll();
			
			return APIUtils.JSONBuilder(true, "students", students);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PostMapping("/")
	public String createStudent(@RequestHeader(value = "Authorization", required = false) String sessionToken, @RequestBody StudentPayload studentPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			if(!studentPayload.isValid()) return APIUtils.BAD_REQUEST_JSON;
			
			Role studentRole = roleRepo.findByRoleName(Roles.STUDENT.getRole());
			Role parentRole = roleRepo.findByRoleName(Roles.PARENT.getRole());
			
			User studentUser = new User();
			studentUser.setRole(studentRole);
			studentUser.setFirstName(studentPayload.getStudent().getFirstName());
			studentUser.setLastName(studentPayload.getStudent().getLastName());
			studentUser.setEmail(studentPayload.getStudent().getEmail());
			
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			
			HasherObject hash = APIUtils.hasher(salt, studentPayload.getStudent().getPassword());
			studentUser.setSalt(hash.getEncodedSalt());
			studentUser.setPassword(hash.getEncodedHash());
			
			studentUser = userRepo.save(studentUser);

			User parentUser = new User();
			parentUser.setRole(parentRole);
			parentUser.setFirstName(studentPayload.getParent().getFirstName());
			parentUser.setLastName(studentPayload.getParent().getLastName());
			parentUser.setEmail(studentPayload.getParent().getEmail());
			
			random.nextBytes(salt);
			
			hash = APIUtils.hasher(salt, studentPayload.getParent().getPassword());
			parentUser.setSalt(hash.getEncodedSalt());
			parentUser.setPassword(hash.getEncodedHash());
			
			parentUser = userRepo.save(parentUser);
			
			Student student = new Student();
			student.setUser(studentUser);
			student.setParent(parentUser);
			student.setGrade(studentPayload.getGrade());
			
			student = studentRepo.save(student);
			
			return APIUtils.JSONBuilder(true, "student", student);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@GetMapping("/{id}")
	public String getStudentByID(@PathVariable Integer id) {
		try {
			Optional<Student> students = studentRepo.findById(id);
			
			return APIUtils.JSONBuilder(true, "students", students);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PatchMapping("/{id}")
	public String editStudentByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id, @RequestBody StudentPayload studentPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole(), Roles.PARENT.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Optional<Student> studentObj = studentRepo.findById(id);
			
			if(studentObj.isEmpty() || !studentPayload.isValid()) return APIUtils.BAD_REQUEST_JSON;
			
			Student student = studentObj.get();
			student.setGrade(studentPayload.getGrade());
			
			User studentUser = student.getUser();
			studentUser.setFirstName(studentPayload.getStudent().getFirstName());
			studentUser.setLastName(studentPayload.getStudent().getLastName());
			studentUser.setEmail(studentPayload.getStudent().getEmail());
			
			studentUser = userRepo.save(studentUser);

			// FIXME: Does Not Check if Parent Included in Payload is Equal to Current Parent
			// This Can Cause Problems if the Parent Needs to Be Changed Instead of Updated
			User parent = student.getParent();
			parent.setFirstName(studentPayload.getParent().getFirstName());
			parent.setLastName(studentPayload.getParent().getLastName());
			parent.setEmail(studentPayload.getParent().getEmail());
			
			parent = userRepo.save(parent);
			
			student = studentRepo.save(student);
			
			return APIUtils.JSONBuilder(true, "student", student);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PostMapping("/{id}/enroll")
	public String enrollStudentByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id, @RequestBody EnrollmentPayload enrollmentPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole(), Roles.STUDENT.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			if(!enrollmentPayload.isValid()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Student> student = studentRepo.findById(id);
			Optional<Course> course = courseRepo.findById(enrollmentPayload.getCourseId());
			
			if(student.isEmpty() || course.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			Enrollment enrollment = new Enrollment();
			enrollment.setCourse(course.get());
			enrollment.setStudent(student.get());
			
			enrollment = enrollmentRepo.save(enrollment);
			
			return APIUtils.JSONBuilder(true, "msg", "Student Successfully Enrolled");
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PostMapping("/{id}/unenroll")
	public String unenrollStudentByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id, @RequestBody EnrollmentPayload enrollmentPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole(), Roles.STUDENT.getRole(), Roles.FACULTY.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			if(!enrollmentPayload.isValid()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Student> student = studentRepo.findById(id);
			Optional<Course> course = courseRepo.findById(enrollmentPayload.getCourseId());
			
			if(student.isEmpty() || course.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Enrollment> enrollment = enrollmentRepo.findByStudentAndCourse(student.get(), course.get());
			
			if(enrollment.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			enrollmentRepo.delete(enrollment.get());
			
			return APIUtils.JSONBuilder(true, "msg", "Student Successfully Unenrolled");
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PostMapping("/{id}/grade")
	public String gradeStudentByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id, @RequestBody EnrollmentPayload enrollmentPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.FACULTY.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			if(!enrollmentPayload.isValidGrade()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Student> student = studentRepo.findById(id);
			Optional<Course> course = courseRepo.findById(enrollmentPayload.getCourseId());
			
			if(student.isEmpty() || course.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Enrollment> enrollment = enrollmentRepo.findByStudentAndCourse(student.get(), course.get());
			
			if(enrollment.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			if(enrollment.get().getGrade() != null) return APIUtils.BAD_REQUEST_JSON;
			
			enrollment.get().setGrade(enrollmentPayload.getGrade());
			
			return APIUtils.JSONBuilder(true, "msg", "Grade Successfully Submitted");
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PatchMapping("/{id}/grade")
	public String editStudentGradeByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id, @RequestBody EnrollmentPayload enrollmentPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole(), Roles.FACULTY.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			if(!enrollmentPayload.isValidGrade()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Student> student = studentRepo.findById(id);
			Optional<Course> course = courseRepo.findById(enrollmentPayload.getCourseId());
			
			if(student.isEmpty() || course.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			Optional<Enrollment> enrollment = enrollmentRepo.findByStudentAndCourse(student.get(), course.get());
			
			if(enrollment.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			if(enrollment.get().getGrade() == null) return APIUtils.BAD_REQUEST_JSON;
			
			enrollment.get().setGrade(enrollmentPayload.getGrade());
			
			return APIUtils.JSONBuilder(true, "msg", "Grade Successfully Submitted.");
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
}