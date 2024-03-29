package hanu.gdsc.infrastructure.practiceProblem_problem.controllers.problem;

import hanu.gdsc.domain.coderAuth.services.access.AuthorizeService;
import hanu.gdsc.domain.core_problem.models.ProgrammingLanguage;
import hanu.gdsc.domain.core_problem.services.submit.SubmitService;
import hanu.gdsc.infrastructure.share.controller.ControllerHandler;
import hanu.gdsc.domain.share.models.Id;
import hanu.gdsc.domain.practiceProblem_problem.services.problem.SubmitProblemService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Practice Problem - Problem" , description = "Rest-API endpoint for Practice Problem")
public class SubmitProblemController {
    @Autowired
    private SubmitProblemService submitProblemService;
    @Autowired
    private AuthorizeService authorizeService;

    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "Submit", description = "Data transfer object for PracticeProblem to submit" )
    public static class InputSubmit {
        @Schema(description = "specify the code of problem", example = "String", required = true)
        public String code;
        @Schema(description = "specify the programming language of problem", example = "JAVA", required = true)
        public ProgrammingLanguage programmingLanguage;
    }

    @PostMapping("/practiceProblem/problem/{id}/submit")
    public ResponseEntity<?> submit(@RequestBody InputSubmit input, @PathVariable String id,
                                    @RequestHeader("access-token") String token) {
        return ControllerHandler.handle(() -> {
            Id coderId = authorizeService.authorize(token);
            SubmitService.Output output = submitProblemService
                    .submit(new SubmitProblemService.Input(
                            coderId,
                            new Id(id),
                            input.code,
                            input.programmingLanguage
                    ));
            return new ControllerHandler.Result(
                    "Success",
                    output
            );
        });
    }
}
