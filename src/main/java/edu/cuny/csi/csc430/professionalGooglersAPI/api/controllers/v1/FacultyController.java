package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthenticationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthorizationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.FacultyPayload;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.EvaluationRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.FacultyRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.RoleRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.UserRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.HasherObject;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.Roles;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Evaluation;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Faculty;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Role;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

@RestController
@RequestMapping(value="/api/v1/faculty", produces={"application/json"})
public class FacultyController {
	@Autowired SessionRepository sessionRepo;
	@Autowired UserRepository userRepo;
	@Autowired FacultyRepository facultyRepo;
	@Autowired RoleRepository roleRepo;
	@Autowired EvaluationRepository evaluationRepo;
	
	@GetMapping("/") 
	public String getAllFaculty() {
		try {
			Iterable<Faculty> faculty = facultyRepo.findAll();
			
			return APIUtils.JSONBuilder(true, "faculty", faculty);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PostMapping("/")
	public String createFaculty(@RequestHeader(value = "Authorization", required = false) String sessionToken, @RequestBody FacultyPayload facultyPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			User facultyUser = new User();
			
			if(!facultyPayload.isValid()) return APIUtils.BAD_REQUEST_JSON;
			
			facultyUser.setEmail(facultyPayload.getEmail());
			facultyUser.setFirstName(facultyPayload.getFirstName());
			facultyUser.setLastName(facultyPayload.getLastName());
			
			Role facultyRole = roleRepo.findByRoleName(Roles.FACULTY.getRole());
			facultyUser.setRole(facultyRole);
			
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			
			HasherObject hasherObject = APIUtils.hasher(salt, facultyPayload.getPassword());
			facultyUser.setSalt(hasherObject.getEncodedSalt());
			facultyUser.setPassword(hasherObject.getEncodedHash());

			facultyUser = userRepo.save(facultyUser);
			
			Faculty faculty = new Faculty();
			faculty.setUser(facultyUser);
			faculty.setSalary(facultyPayload.getSalary());
			
			faculty = facultyRepo.save(faculty);
			
			return APIUtils.JSONBuilder(true, "faculty", faculty);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@GetMapping("/{id}") 
	public String getFacultyByID(@PathVariable Integer id) {
		try {
			Optional<Faculty> faculty = facultyRepo.findById(id);
			
			if(faculty.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			return APIUtils.JSONBuilder(true, "faculty", faculty);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@GetMapping("/{id}/evaluations") 
	public String getEvaluationsByTeacherID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;
			
			Iterable<Evaluation> evaluations = evaluationRepo.findByTeacherId(id);
			
			return APIUtils.JSONBuilder(true, "evaluations", evaluations);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PatchMapping("/{id}")
	public String editFacultyByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id, @RequestBody FacultyPayload facultyPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;

			Optional<Faculty> faculty = facultyRepo.findById(id);
			
			if(faculty.isEmpty() || !facultyPayload.isValidEdit()) return APIUtils.BAD_REQUEST_JSON;
			
			Faculty facultyMember = faculty.get();
			facultyMember.setSalary(facultyPayload.getSalary());
			facultyMember.setActive(facultyPayload.getActive());
			
			User facultyUser = facultyMember.getUser();			
			facultyUser.setEmail(facultyPayload.getEmail());
			facultyUser.setFirstName(facultyPayload.getFirstName());
			facultyUser.setLastName(facultyPayload.getLastName());
			
			facultyMember = facultyRepo.save(facultyMember);
			
			return APIUtils.JSONBuilder(true, "faculty", facultyMember);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@DeleteMapping("/{id}")
	public String deleteFacultyByID(@RequestHeader(value = "Authorization", required = false) String sessionToken, @PathVariable Integer id) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.ADMIN.getRole())))) return APIUtils.UNAUTHORIZED_JSON;

			Optional<Faculty> faculty = facultyRepo.findById(id);
			
			if(faculty.isEmpty()) return APIUtils.BAD_REQUEST_JSON;
			
			Faculty facultyMember = faculty.get();
			facultyMember.setActive(false);
			
			facultyMember = facultyRepo.save(facultyMember);
			
			return APIUtils.JSONBuilder(true, "msg", "Faculty Successfully De-Activated");
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
}
