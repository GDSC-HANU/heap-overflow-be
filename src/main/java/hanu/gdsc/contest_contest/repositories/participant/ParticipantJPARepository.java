package hanu.gdsc.contest_contest.repositories.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantJPARepository extends JpaRepository<ParticipantEntity, String> {
    public List<ParticipantEntity> findByContestId();
}
