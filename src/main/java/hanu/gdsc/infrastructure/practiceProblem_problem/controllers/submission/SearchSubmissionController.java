package hanu.gdsc.infrastructure.practiceProblem_problem.controllers.submission;

import hanu.gdsc.infrastructure.share.controller.ControllerHandler;
import hanu.gdsc.domain.share.models.Id;
import hanu.gdsc.domain.practiceProblem_problem.services.submission.SearchPracticeProblemSubmissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Practice Problem - Submission" , description = "Rest-API endpoint for Practice Problem")
public class SearchSubmissionController {
    @Autowired
    private SearchPracticeProblemSubmissionService searchPracticeProblemSubmissionService;

    @GetMapping("/practiceProblem/submission")
    public ResponseEntity<?> get(@RequestParam int page, @RequestParam int perPage,
                                 @RequestParam(required = false, name = "problemId") String problemId,
                                 @RequestParam(required = false, name = "coderId") String coderId) {
        return ControllerHandler.handle(() -> {
            Id problem = problemId == null ? null : new Id(problemId);
            Id coder = coderId == null ? null : new Id(coderId);
            List<SearchPracticeProblemSubmissionService.Output> output = searchPracticeProblemSubmissionService.get(page, perPage, problem, coder);
            return new ControllerHandler.Result(
                    "Success",
                    output
            );
        });
    }

    @GetMapping("/practiceProblem/submission/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ControllerHandler.handle(() -> {
            SearchPracticeProblemSubmissionService.Output output = searchPracticeProblemSubmissionService.getById(new Id(id));
            return new ControllerHandler.Result(
                    "Success",
                    output
            );
        });
    }
}
