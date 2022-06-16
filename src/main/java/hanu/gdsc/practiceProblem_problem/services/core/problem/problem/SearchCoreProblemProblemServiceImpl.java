package hanu.gdsc.practiceProblem_problem.services.core.problem.problem;

import hanu.gdsc.core_problem.services.problem.SearchProblemService;
import hanu.gdsc.practiceProblem_problem.config.ServiceName;
import hanu.gdsc.share.domains.Id;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchCoreProblemProblemServiceImpl implements SearchCoreProblemProblemService {
    private final SearchProblemService searchCoreProblemService;

    @Override
    public SearchProblemService.Output getById(Id id) {
        return searchCoreProblemService.getById(id, ServiceName.serviceName);
    }

    @Override
    public List<SearchProblemService.Output> getByIds(List<Id> ids) {
        return searchCoreProblemService.getByIds(ids, ServiceName.serviceName);
    }
}