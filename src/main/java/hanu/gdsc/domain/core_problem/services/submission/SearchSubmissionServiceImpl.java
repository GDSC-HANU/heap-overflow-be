package hanu.gdsc.domain.core_problem.services.submission;

import hanu.gdsc.domain.core_problem.models.RunningSubmission;
import hanu.gdsc.domain.core_problem.models.Submission;
import hanu.gdsc.domain.core_problem.repositories.RunningSubmissionRepository;
import hanu.gdsc.domain.core_problem.repositories.SubmissionRepository;
import hanu.gdsc.domain.practiceProblem_problem.exceptions.SubmissionIsBeingJudgedException;
import hanu.gdsc.domain.share.exceptions.NotFoundException;
import hanu.gdsc.domain.share.models.Id;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchSubmissionServiceImpl implements SearchSubmissionService {
    private SubmissionRepository submissionRepository;
    private RunningSubmissionRepository runningSubmissionRepository;

    @Override
    public List<Submission> getByProblemIdAndCoderId(int page, int perPage, Id problemId, Id coderId, String serviceToCreate) {
        return submissionRepository.get(page, perPage, problemId, coderId, serviceToCreate);
    }

    @Override
    public List<Submission> getByProblemIdsAndCoderId(int page, int perPage, List<Id> problemIds, Id coderId, String serviceToCreate) {
        return submissionRepository.get(page,
                perPage,
                problemIds,
                coderId,
                serviceToCreate);
    }

    @Override
    public Submission getById(Id id, String serviceToCreate) throws SubmissionIsBeingJudgedException, NotFoundException {
        Submission submission = submissionRepository.getById(id, serviceToCreate);
        if (submission == null) {
            RunningSubmission runningSubmission = runningSubmissionRepository.getById(id, serviceToCreate);
            if (runningSubmission != null) {
                throw new SubmissionIsBeingJudgedException();
            }
            throw new NotFoundException("Submission not found");
        }
        return submission;
    }
}
