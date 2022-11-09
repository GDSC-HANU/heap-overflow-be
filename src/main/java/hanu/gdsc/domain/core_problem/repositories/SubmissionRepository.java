package hanu.gdsc.domain.core_problem.repositories;

import java.util.List;

import hanu.gdsc.domain.core_problem.models.Submission;
import hanu.gdsc.domain.share.models.Id;

public interface SubmissionRepository {
    public List<Submission> get(int page, int perPage, Id problemId, Id coderId, String serviceToCreate);
    public Submission getById(Id id, String serviceToCreate);
    public void deleteAllByProblemId(Id problemId);
    public Submission getByProblemIdAndCoderId(Id problemId, Id coderId);
}
