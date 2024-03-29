package hanu.gdsc.infrastructure.practiceProblem_problem.controllers.problem;

import hanu.gdsc.domain.coderAuth.services.access.AuthorizeService;
import hanu.gdsc.domain.core_problem.models.MemoryLimit;
import hanu.gdsc.domain.core_problem.models.ProgrammingLanguage;
import hanu.gdsc.domain.core_problem.models.TimeLimit;
import hanu.gdsc.domain.practiceProblem_problem.models.Difficulty;
import hanu.gdsc.infrastructure.share.controller.ControllerHandler;
import hanu.gdsc.domain.share.models.Id;
import hanu.gdsc.domain.practiceProblem_problem.services.problem.UpdateProblemService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component(value = "PracticeProblem.UpdateProblemService")
@Tag(name = "Practice Problem - Problem" , description = "Rest-API endpoint for Practice Problem")
public class UpdateProblemController {
    @Autowired
    private UpdateProblemService updateProblemService;
    @Autowired
    private AuthorizeService authorizeService;

    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "Update", description = "Data transfer object for PracticeProblem to update")
    public static class InputUpdate {
        @Schema(description = "specify the difficulty of problem", example = "EASY", required = true)
        public Difficulty difficulty;
        @Schema(description = "specify the name of problem", example = "Sum Array", required = true)
        public String name;
        @Schema(description = "specify the description of problem", example = "blablalbalba", required = true)
        public String description;
        public List<MemoryLimit.CreateInputML> memoryLimits;
        public List<TimeLimit.CreateInputTL> timeLimits;
        public List<ProgrammingLanguage> allowedProgrammingLanguages;
    }

    @PutMapping("/practiceProblem/problem/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody InputUpdate input,
                                    @RequestHeader("access-token") String token) {
        return ControllerHandler.handle(() -> {
            authorizeService.authorize(token);
            updateProblemService.update(new UpdateProblemService.InputUpdate(
                    new Id(id),
                    input.difficulty,
                    input.name,
                    input.description,
                    input.memoryLimits,
                    input.timeLimits,
                    input.allowedProgrammingLanguages
            ));
            return new ControllerHandler.Result(
                    "Success",
                    null
            );
        });
    }
}
