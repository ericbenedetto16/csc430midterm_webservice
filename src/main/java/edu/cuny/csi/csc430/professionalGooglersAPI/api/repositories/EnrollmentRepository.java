package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Course;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Enrollment;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Student;

public interface EnrollmentRepository extends CrudRepository<Enrollment, Integer> {
	public void deleteByCourse(Course course);
	
	public Iterable<Enrollment> findByCourse(Course course);
	public Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
