package hanu.gdsc.core_problem.services.problem;

import java.util.List;

import hanu.gdsc.core_problem.domains.*;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.InvalidInputException;
import hanu.gdsc.share.exceptions.NotFoundException;
import lombok.*;

public interface UpdateProblemService {
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input{
        public Id id;
        public String serviceToCreate;
        public String name;
        public String description;
        public List<MemoryLimit.CreateInputML> memoryLimits;
        public List<TimeLimit.CreateInputTL> timeLimits;
        public List<ProgrammingLanguage> allowedProgrammingLanguages;
    }

    public void update(Input input) throws NotFoundException, InvalidInputException;
}
