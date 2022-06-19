package hanu.gdsc.core_problem.services.problem;

import java.util.List;

import hanu.gdsc.core_problem.domains.KB;
import hanu.gdsc.core_problem.domains.Millisecond;
import hanu.gdsc.core_problem.domains.ProgrammingLanguage;
import hanu.gdsc.share.domains.Id;
import lombok.*;

public interface UpdateProblemService {
    @AllArgsConstructor
    @Getter
    public static class Input{
        public Id id;
        public String serviceToCreate;
        public String name;
        public String description;
        public List<UpdateMemoryLimitInput> memoryLimits;
        public List<UpdateTimeLimitInput> timeLimits;
        public List<ProgrammingLanguage> allowedProgrammingLanguages;
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class UpdateMemoryLimitInput {
        public KB memoryLimit;
        public ProgrammingLanguage programmingLanguage;
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class UpdateTimeLimitInput {
        public Millisecond timeLimit;
        public ProgrammingLanguage programmingLanguage;
    }

    public void update(Input input);
}
