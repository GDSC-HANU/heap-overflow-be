package hanu.gdsc.domain.contest.services.submissionEvent;

import hanu.gdsc.domain.contest.config.ContestServiceName;
import hanu.gdsc.domain.contest.models.Contest;
import hanu.gdsc.domain.contest.models.ContestProblem;
import hanu.gdsc.domain.contest.models.Participant;
import hanu.gdsc.domain.contest.repositories.ContestRepository;
import hanu.gdsc.domain.contest.repositories.ParticipantRepository;
import hanu.gdsc.domain.core_problem.models.Status;
import hanu.gdsc.domain.core_problem.models.SubmissionEvent;
import hanu.gdsc.domain.core_problem.repositories.SubmissionRepository;

public class ProcessSubmissionEventService {
    private ContestRepository contestRepository;
    private ParticipantRepository participantRepository;
    private SubmissionRepository submissionRepository;

    public void process(SubmissionEvent submissionEvent, Runnable ack) {
        if (submissionEvent.getStatus().equals(Status.AC)) {
            final Contest contest = contestRepository.getContestContainsCoreProblemId(
                    submissionEvent.getProblemId()
            );
            final Participant participant = participantRepository.getByCoderIdAndContestId(
                    submissionEvent.getCoderId(),
                    contest.getId()
            );
            final int notACSubmissionsBeforeCount = submissionRepository.countNotACSubmissionsBefore(
                    submissionEvent.getCoderId(),
                    submissionEvent.getProblemId(),
                    ContestServiceName.serviceName,
                    submissionEvent.getSubmittedAt()
            );
            final ContestProblem contestProblem = contest.getProblem(submissionEvent.getProblemId());
            final double score = contest.calculateScoreForSubmission(
                    contestProblem.getOrdinal(),
                    submissionEvent.getSubmittedAt(),
                    notACSubmissionsBeforeCount,
                    submissionEvent.getStatus(),
                    submissionEvent.getPassedTestCasesCount(),
                    submissionEvent.getTotalTestCasesCount()
            );
            participant.updateProblemScore(score, contestProblem.getOrdinal());
        }
        ack.run();
    }
}
