package hanu.gdsc.infrastructure.coderAuth.repositories.session;

import javax.transaction.Transactional;

import hanu.gdsc.domain.coderAuth.models.Session;
import hanu.gdsc.domain.coderAuth.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hanu.gdsc.domain.share.models.Id;

@Repository
@Transactional
public class SessionRepositoryImpl implements SessionRepository {

    @Autowired
    private SessionJPARepository sessionJPARepository;

    @Override
    public void save(Session session) {
      sessionJPARepository.save(SessionEntity.toEntity(session));
    }

    @Override
    public Session getById(Id id) {
      return sessionJPARepository.getById(id.toString()).toDomain();
    }

    @Override
    public Session getByCoderId(Id coderId) {
      return sessionJPARepository.getByCoderId(coderId.toString()).toDomain();
    }

    @Override
    public void deleteById(Id id) {
      sessionJPARepository.deleteById(id.toString());
    }

    @Override
    public void deleteSession(Id coderId, Id sessionId) {
      sessionJPARepository.deleteSession(coderId.toString(), sessionId.toString());
    }
}
