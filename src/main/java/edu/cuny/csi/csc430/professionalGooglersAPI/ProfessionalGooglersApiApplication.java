package edu.cuny.csi.csc430.professionalGooglersAPI;

import java.security.SecureRandom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.FacultyRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.RoleRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.StudentRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories.UserRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.APIUtils;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.HasherObject;
import edu.cuny.csi.csc430.professionalGooglersAPI.api.utils.Roles;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Faculty;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Role;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Student;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

@SpringBootApplication
public class ProfessionalGooglersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProfessionalGooglersApiApplication.class, args);
	}
	
	@Bean
	CommandLineRunner seeders(RoleRepository roleRepo, UserRepository userRepo, FacultyRepository facultyRepo, StudentRepository studentRepo) {
		return (args) -> {
			// Run Seeders to Create All Roles, an Admin, Teacher, Student, and Parent
			Role adminRole = roleRepo.findByRoleName(Roles.ADMIN.getRole());
			if(adminRole == null) {
				adminRole = new Role();
				adminRole.setName(Roles.ADMIN.getRole());
				adminRole = roleRepo.save(adminRole);
			}
			
			Role parentRole = roleRepo.findByRoleName(Roles.PARENT.getRole());
			if(parentRole == null) {
				parentRole = new Role();
				parentRole.setName(Roles.PARENT.getRole());
				parentRole = roleRepo.save(parentRole);
			}
			
			Role facultyRole = roleRepo.findByRoleName(Roles.FACULTY.getRole());
			if(facultyRole == null) {
				facultyRole = new Role();
				facultyRole.setName(Roles.FACULTY.getRole());
				facultyRole = roleRepo.save(facultyRole);
			}
			
			Role studentRole = roleRepo.findByRoleName(Roles.STUDENT.getRole());
			if(studentRole == null) {
				studentRole = new Role();
				studentRole.setName(Roles.STUDENT.getRole());
				studentRole = roleRepo.save(studentRole);
			}
			
			User adminUser = userRepo.findByFirstNameAndLastName("Admin", "Istrator");
			if(adminUser == null) {
				adminUser = new User();
				adminUser.setEmail("admin.istrator@schoolsystem.com");
				adminUser.setFirstName("Admin");
				adminUser.setLastName("Istrator");
				
				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes(salt);
				
				String password = "supersecurepassword";
				HasherObject encoded = APIUtils.hasher(salt, password);

				adminUser.setSalt(encoded.getEncodedSalt());
				adminUser.setPassword(encoded.getEncodedHash());
				
				adminUser.setRole(adminRole);
				adminUser = userRepo.save(adminUser);
			}
			
			User teacherUser = userRepo.findByFirstNameAndLastName("Teacher", "Dude");
			if(teacherUser == null) {
				teacherUser = new User();
				teacherUser.setEmail("teacher.dude@schoolsystem.com");
				teacherUser.setFirstName("Teacher");
				teacherUser.setLastName("Dude");
				
				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes(salt);
				
				String password = "teacherpassword";
				HasherObject encoded = APIUtils.hasher(salt, password);
				
				teacherUser.setSalt(encoded.getEncodedSalt());
				teacherUser.setPassword(encoded.getEncodedHash());
				
				teacherUser.setRole(facultyRole);
				teacherUser = userRepo.save(teacherUser);
			}
			
			Faculty teacherFaculty = facultyRepo.findByUserId(teacherUser.getId());
			if(teacherFaculty == null) {
				teacherFaculty = new Faculty();
				teacherFaculty.setUser(teacherUser);
				teacherFaculty.setSalary((double) 65000);
				
				teacherFaculty = facultyRepo.save(teacherFaculty);
			}
			
			User studentUser = userRepo.findByFirstNameAndLastName("Stu", "Dent");
			if(studentUser == null) {
				studentUser = new User();
				studentUser.setFirstName("Stu");
				studentUser.setLastName("Dent");
				studentUser.setEmail("stu.dent@schoolsystem.com");
				
				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes(salt);
				
				String password = "studentpassword";
				HasherObject encoded = APIUtils.hasher(salt, password);
				
				studentUser.setSalt(encoded.getEncodedSalt());
				studentUser.setPassword(encoded.getEncodedHash());
				
				studentUser.setRole(studentRole);
				studentUser = userRepo.save(studentUser);
			}

			User parentUser = userRepo.findByFirstNameAndLastName("Par", "Ent");
			if(parentUser == null) {
				parentUser = new User();
				parentUser.setFirstName("Par");
				parentUser.setLastName("Ent");
				parentUser.setEmail("par.ent@schoolsystem.com");
				
				SecureRandom random = new SecureRandom();
				byte[] salt = new byte[16];
				random.nextBytes(salt);
				
				String password = "parentpassword";
				HasherObject encoded = APIUtils.hasher(salt, password);
				
				parentUser.setSalt(encoded.getEncodedSalt());
				parentUser.setPassword(encoded.getEncodedHash());
				
				parentUser.setRole(parentRole);
				parentUser = userRepo.save(parentUser);
			}
			
			Student student = studentRepo.findByUser(studentUser);
			if(student == null) {
				student = new Student();
				student.setUser(studentUser);
				student.setParent(parentUser);
				student.setGrade(10);
				
				student = studentRepo.save(student);
			}
		};
	}
}
