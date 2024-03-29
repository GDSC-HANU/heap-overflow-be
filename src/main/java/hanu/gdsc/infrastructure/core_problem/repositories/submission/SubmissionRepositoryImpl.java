package hanu.gdsc.infrastructure.core_problem.repositories.submission;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.gdsc.domain.core_problem.models.Status;
import hanu.gdsc.domain.core_problem.models.Submission;
import hanu.gdsc.domain.core_problem.repositories.SubmissionRepository;
import hanu.gdsc.domain.share.models.DateTime;
import hanu.gdsc.domain.share.models.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubmissionRepositoryImpl implements SubmissionRepository {
    @Autowired
    private SubmissionJPARepository submissionJPARepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public List<Submission> get(int page, int perPage, Id problemId, Id coderId, String serviceToCreate) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("submittedAtMillis").descending());
        Page<SubmissionEntity> submissionsEntity;
        if (problemId != null && coderId != null) {
            submissionsEntity = submissionJPARepository.findByProblemIdAndCoderIdAndServiceToCreate(problemId.toString(),
                    coderId.toString(),
                    serviceToCreate,
                    pageable);
        } else if (problemId != null) {
            submissionsEntity = submissionJPARepository.findByProblemIdAndServiceToCreate(problemId.toString(),
                    serviceToCreate,
                    pageable);
        } else if (coderId != null) {
            submissionsEntity = submissionJPARepository.findByCoderIdAndServiceToCreate(coderId.toString(),
                    serviceToCreate,
                    pageable);
        } else {
            submissionsEntity = submissionJPARepository.findByServiceToCreate(serviceToCreate, pageable);
        }
        return submissionsEntity.getContent().stream()
                .map(s -> SubmissionEntity.toDomain(s, objectMapper))
                .collect(Collectors.toList());
    }

    @Override
    public List<Submission> get(int page, int perPage, List<Id> problemIds, Id coderId, String serviceToCreate) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("submittedAtMillis").descending());
        Page<SubmissionEntity> submissionsEntity;
        if (problemIds != null && coderId != null) {
            submissionsEntity = submissionJPARepository.findByProblemIdInAndCoderIdAndServiceToCreate(problemIds.stream()
                            .map(x -> x.toString())
                            .collect(Collectors.toList()),
                    coderId.toString(),
                    serviceToCreate,
                    pageable);
        } else if (problemIds != null) {
            submissionsEntity = submissionJPARepository.findByProblemIdInAndServiceToCreate(problemIds.stream()
                            .map(x -> x.toString())
                            .collect(Collectors.toList()),
                    serviceToCreate,
                    pageable);
        } else if (coderId != null) {
            submissionsEntity = submissionJPARepository.findByCoderIdAndServiceToCreate(coderId.toString(),
                    serviceToCreate,
                    pageable);
        } else {
            submissionsEntity = submissionJPARepository.findByServiceToCreate(serviceToCreate, pageable);
        }
        return submissionsEntity.getContent().stream()
                .map(s -> SubmissionEntity.toDomain(s, objectMapper))
                .collect(Collectors.toList());
    }

    @Override
    public Submission getById(Id id, String serviceToCreate) {
        try {
            SubmissionEntity submissionEntity = submissionJPARepository.getByIdAndServiceToCreate(id.toString(), serviceToCreate);
            if (submissionEntity == null) {
                return null;
            }
            return SubmissionEntity.toDomain(submissionEntity, objectMapper);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public void deleteAllByProblemId(Id problemId) {
        submissionJPARepository.deleteAllByProblemId(problemId.toString());
    }

    @Override
    public Submission getACSubmissionBefore(Id coderId, Id problemId, DateTime beforeTime) {
        final Page<SubmissionEntity> entities = submissionJPARepository
                .findByStatusAndSubmittedAtMillisLessThan(
                        Status.AC.name(),
                        beforeTime.toMillis(),
                        Pageable.ofSize(1)
                );
        if (entities.getContent().size() == 0)
            return null;
        return SubmissionEntity.toDomain(
                entities.getContent().get(0),
                objectMapper
        );
    }

    @Override
    public long countNotACSubmissionsBefore(Id coderId,
                                           Id problemId,
                                           String serviceToCreate,
                                           DateTime beforeTime) {
        return submissionJPARepository.countByCoderIdAndProblemIdAndServiceToCreateAndSubmittedAtMillisLessThanAndStatusNot(
                coderId.toString(),
                problemId.toString(),
                serviceToCreate,
                beforeTime.toMillis(),
                "AC"
        ); 
    }

}
