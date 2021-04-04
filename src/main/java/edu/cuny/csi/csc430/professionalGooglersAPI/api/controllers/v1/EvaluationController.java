package edu.cuny.csi.csc430.professionalGooglersAPI.api.controllers.v1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthenticationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.middleware.AuthorizationMiddleware;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.payloads.EvaluationPayload;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.EvaluationRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.FacultyRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.SessionRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.StudentRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.UserRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.Roles;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Evaluation;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Faculty;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Student;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

@RestController
@RequestMapping(value="/api/v1/evaluations", produces={"application/json"}) 
public class EvaluationController {
	@Autowired EvaluationRepository evaluationRepo;
	@Autowired SessionRepository sessionRepo;
	@Autowired UserRepository userRepo;
	@Autowired FacultyRepository facultyRepo;
	@Autowired StudentRepository studentRepo;
	
	@GetMapping("/")
	public String getAllEvaluations() {
		try {
			Iterable<Evaluation> evaluations = evaluationRepo.findAll();
			
			return APIUtils.JSONBuilder(true, "evaluations", evaluations);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
	
	@PostMapping("/")
	public String createEvaluation(@RequestHeader(value = "Authorization", required = false) String sessionToken, @RequestBody EvaluationPayload evaluationPayload) {
		try {
			User user = AuthenticationMiddleware.auth(sessionToken, sessionRepo);
			if(user == null) return APIUtils.LOGGED_OUT_JSON;
			
			if(!AuthorizationMiddleware.authorize(user, new HashSet<String>(Arrays.asList(Roles.STUDENT.getRole())))) return APIUtils.UNAUTHORIZED_JSON;

			Student student = studentRepo.findByUser(user);
			Optional<Faculty> teacher = facultyRepo.findById(evaluationPayload.getTeacherId());

			if(student == null || teacher.isEmpty() || evaluationPayload.getRating() == null) return APIUtils.BAD_REQUEST_JSON;
			
			Evaluation evaluation = new Evaluation();
			
			evaluation.setStudent(student);
			evaluation.setTeacher(teacher.get());
			evaluation.setRating(evaluationPayload.getRating());
			
			evaluation = evaluationRepo.save(evaluation);
		
			return APIUtils.JSONBuilder(true, "evaluation", evaluation);
		}catch(Exception e) {
			e.printStackTrace();
			
			return APIUtils.ERROR_JSON;
		}
	}
}
