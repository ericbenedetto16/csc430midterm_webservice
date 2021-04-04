package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {

}
