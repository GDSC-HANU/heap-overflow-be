package hanu.gdsc.contest_contest.services.contest;

import hanu.gdsc.contest_contest.config.ServiceName;
import hanu.gdsc.contest_contest.domains.Contest;
import hanu.gdsc.contest_contest.domains.ContestProblem;
import hanu.gdsc.contest_contest.repositories.contest.ContestRepository;
import hanu.gdsc.core_problem.domains.MemoryLimit;
import hanu.gdsc.core_problem.domains.ProgrammingLanguage;
import hanu.gdsc.core_problem.domains.TimeLimit;
import hanu.gdsc.core_problem.services.problem.CreateProblemService;
import hanu.gdsc.core_problem.services.problem.DeleteProblemService;
import hanu.gdsc.share.domains.DateTime;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.BusinessLogicException;
import hanu.gdsc.share.exceptions.InvalidInputException;
import hanu.gdsc.share.exceptions.InvalidStateException;
import hanu.gdsc.share.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UpdateContestService {
    private final ContestRepository contestRepository;
    private final CreateProblemService createCoreProblemService;
    private final DeleteProblemService deleteCoreProblemService;


    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateProblemInput {
        public int ordinal;
        public int score;
        public String name;
        public String description;
        public List<MemoryLimit.CreateInputML> memoryLimits;
        public List<TimeLimit.CreateInputTL> timeLimits;
        public List<ProgrammingLanguage> allowedProgrammingLanguages;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {
        public Id id;
        public String name;
        public String description;
        public DateTime startAt;
        public DateTime endAt;
        public List<UpdateProblemInput> problems;
        public Id updatedBy;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void execute(Input input) throws InvalidInputException, InvalidStateException, NotFoundException {
        Contest contest = contestRepository.getById(input.id);
        if (contest == null) {
            throw new NotFoundException("Unknown contest.");
        }
        if (input.name != null) {
            contest.setName(input.name);
        }
        if (input.description != null) {
            contest.setDescription(input.name);
        }
        if (input.startAt != null && input.endAt != null) {
            contest.setStartAtAndEndAt(input.startAt, input.endAt);
        }
        if (input.problems != null) {
            deleteCoreProblemService.deleteMany(
                    contest.getProblems()
                            .stream()
                            .map(problem -> problem.getCoreProblemId())
                            .collect(Collectors.toList())
            );
            List<Id> coreProblemIds = createCoreProblemService
                    .createMany(input.problems
                            .stream()
                            .map(createProblemInput -> new CreateProblemService.Input(
                                    createProblemInput.name,
                                    createProblemInput.description,
                                    input.updatedBy,
                                    createProblemInput.memoryLimits,
                                    createProblemInput.timeLimits,
                                    createProblemInput.allowedProgrammingLanguages,
                                    ServiceName.serviceName
                            ))
                            .collect(Collectors.toList()));
            List<ContestProblem> contestProblems = new ArrayList<>();
            int i = 0;
            for (UpdateProblemInput updateProblemInput : input.problems) {
                contestProblems.add(ContestProblem.create(
                        updateProblemInput.ordinal,
                        coreProblemIds.get(i++),
                        updateProblemInput.score
                ));
            }
            contest.setProblems(contestProblems);
        }
        contestRepository.update(contest);
    }
}
