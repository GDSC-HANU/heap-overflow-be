package hanu.gdsc.practiceProblem_problem.services.problem;

import hanu.gdsc.core_problem.domains.KB;
import hanu.gdsc.core_problem.domains.Millisecond;
import hanu.gdsc.core_problem.domains.ProgrammingLanguage;
import hanu.gdsc.core_problem.services.submission.SearchSubmissionService;
import hanu.gdsc.practiceProblem_problem.config.ServiceName;
import hanu.gdsc.practiceProblem_problem.domains.Difficulty;
import hanu.gdsc.practiceProblem_problem.domains.Problem;
import hanu.gdsc.practiceProblem_problem.repositories.problem.ProblemCountProjection;
import hanu.gdsc.practiceProblem_problem.repositories.problem.ProblemRepository;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.InvalidInputException;
import hanu.gdsc.share.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "PracticeProblem.SearchProblemServiceImpl")
@AllArgsConstructor
public class SearchProblemService {
    private hanu.gdsc.core_problem.services.problem.SearchProblemService searchCoreProblemProblemService;
    private ProblemRepository problemRepository;
    private SearchSubmissionService searchSubmissionService;


    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "output", description = "Data transfer object for PracticeProblem to render")
    public static class Output {
        @Schema(example = "123456789abcd12abc13")
        public Id id;
        @Schema(example = "HARD")
        public Difficulty difficulty;
        @Schema(example = "Sum Array")
        public String name;
        @Schema(example = "blablabla")
        public String description;
        @Schema(example = "112412421abcd12abc124214")
        public Id author;
        @Schema
        public List<OutputMemoryLimit> memoryLimits;
        @Schema
        public List<OutputTimeLimit> timeLimits;
        @Schema
        public List<ProgrammingLanguage> allowedProgrammingLanguages;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class OutputMemoryLimit {
        @Schema(example = "JAVA")
        public ProgrammingLanguage programmingLanguage;
        @Schema(example = "1000")
        public KB memoryLimit;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class OutputTimeLimit {
        @Schema(example = "JAVA")
        public ProgrammingLanguage programmingLanguage;
        @Schema(example = "1000")
        public Millisecond timeLimit;
    }

    @Data
    @Builder
    public static class OutputProgressData {
        private Difficulty difficulty;
        private Integer done;
        private Integer problems;
        private Double percentage;
    }

    public Output getById(Id practiceProblemId) throws NotFoundException {
        Problem practiceProblem = problemRepository.getById(practiceProblemId);
        if (practiceProblem == null) {
            throw new NotFoundException("Unknown problem");
        }
        hanu.gdsc.core_problem.domains.Problem coreProblem = searchCoreProblemProblemService.getById(
                practiceProblem.getCoreProblemProblemId(),
                ServiceName.serviceName
        );
        return toOutput(practiceProblem, coreProblem);
    }

    public List<Output> get(int page, int perPage) {
        List<Problem> practiceProblems = problemRepository.get(
                page,
                perPage
        );
        return toListOutPut(practiceProblems);
    }

    public List<Output> getRecommendProblem(int count) {
        List<Problem> recommendProblems = problemRepository.getRecommendProblem(count);
        return toListOutPut(recommendProblems);
    }

    private List<Output> toListOutPut(List<Problem> problems) {
        List<hanu.gdsc.core_problem.domains.Problem> coreProblems = searchCoreProblemProblemService.getByIds(
                problems.stream()
                        .map(Problem::getCoreProblemProblemId)
                        .collect(Collectors.toList()),
                ServiceName.serviceName
        );
        Map<Id, hanu.gdsc.core_problem.domains.Problem> coreProblemsIdMap = new HashMap<>();
        for (hanu.gdsc.core_problem.domains.Problem coreProb : coreProblems)
            coreProblemsIdMap.put(coreProb.getId(), coreProb);
        return problems.stream()
                .map(p -> toOutput(p, coreProblemsIdMap.get(p.getCoreProblemProblemId())))
                .collect(Collectors.toList());
    }

    private Output toOutput(Problem practiceProblem, hanu.gdsc.core_problem.domains.Problem coreProblem) {
        return new Output(
                practiceProblem.getId(),
                practiceProblem.getDifficulty(),
                coreProblem.getName(),
                coreProblem.getDescription(),
                coreProblem.getAuthor(),
                coreProblem.getMemoryLimits()
                        .stream()
                        .map(lim -> new OutputMemoryLimit(
                                lim.getProgrammingLanguage(),
                                lim.getMemoryLimit()
                        ))
                        .collect(Collectors.toList()),
                coreProblem.getTimeLimits()
                        .stream()
                        .map(lim -> new OutputTimeLimit(
                                lim.getProgrammingLanguage(),
                                lim.getTimeLimit()
                        ))
                        .collect(Collectors.toList()),
                coreProblem.getAllowedProgrammingLanguages()
        );
    }

    public List<OutputProgressData> getProgress(Id coderId) {
        List<Id> problemIds = searchSubmissionService.getAllProblemIdACByCoderId(coderId, ServiceName.serviceName).stream()
                .map(id -> {
                    try {
                        return new Id(id);
                    } catch (InvalidInputException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        Map<Difficulty, List<Problem>> problemMap = problemRepository.findByCoreProblemProblemIds(problemIds).stream()
                .collect(Collectors.groupingBy(Problem::getDifficulty));
        List<ProblemCountProjection> total = problemRepository.countProblemGroupByDifficulty();
        return total.stream()
                .map(count -> {
                    List<Problem> problems = problemMap.get(Difficulty.valueOf(count.getDifficulty()));
                    return OutputProgressData.builder()
                            .difficulty(Difficulty.valueOf(count.getDifficulty()))
                            .done(problems.size())
                            .problems(count.getAmount())
                            .percentage(Math.ceil((double) problems.size()/count.getAmount()*100))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public long countProblem() {
        return problemRepository.countProblem();
    }
}
