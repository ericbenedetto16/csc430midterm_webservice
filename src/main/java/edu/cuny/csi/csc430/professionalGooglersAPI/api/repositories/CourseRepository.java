package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Course;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Evaluation;

public interface CourseRepository extends CrudRepository<Course, Integer> {
	public Iterable<Course> findByTeacherId(Integer id);
}
