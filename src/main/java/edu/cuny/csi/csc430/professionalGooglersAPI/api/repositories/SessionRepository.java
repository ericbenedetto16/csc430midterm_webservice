package edu.cuny.csi.csc430.professionalGooglersAPI.api.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.Session;
import edu.cuny.csi.csc430.professionalGooglersAPI.db.models.User;

public interface SessionRepository extends CrudRepository<Session, Integer> {
	@Transactional
	void deleteByUser(User user);
	
	Session findByToken(String token);
}
