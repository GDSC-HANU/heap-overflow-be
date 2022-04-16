package hanu.gdsc.coreProblem.domains;


import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.domains.IdentifiedDomainObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class MemoryLimit extends IdentifiedDomainObject {
    private ProgrammingLanguage programmingLanguage;
    private KB memoryLimit;

    public MemoryLimit(Id id, ProgrammingLanguage programmingLanguage, KB memoryLimit) {
        super(id);
        this.programmingLanguage = programmingLanguage;
        this.memoryLimit = memoryLimit;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateInput {
        public ProgrammingLanguage programmingLanguage;
        public KB memoryLimit;
    }

    public static MemoryLimit create(CreateInput input) {
        return new MemoryLimit(
                Id.generateRandom(),
                input.programmingLanguage,
                input.memoryLimit
        );
    }

    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

    public KB getMemoryLimit() {
        return memoryLimit;
    }
}
