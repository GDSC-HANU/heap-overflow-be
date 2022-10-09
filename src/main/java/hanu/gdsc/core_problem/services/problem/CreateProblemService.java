package hanu.gdsc.core_problem.services.problem;

import hanu.gdsc.core_problem.domains.MemoryLimit;
import hanu.gdsc.core_problem.domains.ProgrammingLanguage;
import hanu.gdsc.core_problem.domains.TimeLimit;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public interface CreateProblemService {
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {
        public String name;
        public String description;
        public Id author;
        List<MemoryLimit.CreateInputML> memoryLimits;
        List<TimeLimit.CreateInputTL> timeLimits;
        public List<ProgrammingLanguage> allowedProgrammingLanguages;
        public String serviceToCreate;
    }

    public Id create(Input input) throws InvalidInputException;

    public List<Id> createMany(List<Input> inputs) throws InvalidInputException;
}
