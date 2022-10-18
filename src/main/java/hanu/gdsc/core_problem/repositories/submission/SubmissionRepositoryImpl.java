package hanu.gdsc.core_problem.repositories.submission;

import hanu.gdsc.core_problem.domains.Submission;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.InvalidInputException;
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

    @Override
    public void save(Submission submission) {
        submissionJPARepository.save(SubmissionEntity.toEntity(submission));
    }

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
                .map(s -> SubmissionEntity.toDomain(s))
                .collect(Collectors.toList());
    }

    @Override
    public Submission getById(Id id, String serviceToCreate) {
        try {
            SubmissionEntity submissionEntity = submissionJPARepository.getByIdAndServiceToCreate(id.toString(), serviceToCreate);
            if (submissionEntity == null) {
                return null;
            }
            return SubmissionEntity.toDomain(submissionEntity);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public void deleteAllByProblemId(Id problemId) {
        submissionJPARepository.deleteAllByProblemId(problemId.toString());
    }

    @Override
    public List<Id> getAllProblemIdACByCoderId(Id coderId, String serviceToCreate) {
        List<String> problemIdsString = submissionJPARepository.getAllProblemIdACByCoderIdAndServiceToCreate(coderId.toString(), serviceToCreate);
        if(!problemIdsString.isEmpty()) {
            return problemIdsString.stream()
                    .map(problemId -> {
                        try {
                            return new Id(problemId);
                        } catch (InvalidInputException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

}
