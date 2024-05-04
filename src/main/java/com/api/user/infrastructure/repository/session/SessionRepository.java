package com.api.user.infrastructure.repository.session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<SessionEntity, Long> {
}
