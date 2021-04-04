package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Evaluation;

public interface EvaluationRepository extends CrudRepository<Evaluation, Integer> {
	public Iterable<Evaluation> findByTeacherId(Integer id);
}
