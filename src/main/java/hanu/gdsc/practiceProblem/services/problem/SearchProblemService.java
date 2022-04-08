package hanu.gdsc.practiceProblem.services.problem;

import hanu.gdsc.practiceProblem.domains.Difficulty;
import hanu.gdsc.share.domains.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

public interface SearchProblemService {
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Output {
        public Id id;
        public long version;
        public Id coreProblemId;
        private List<Id> categoryIds;
        public Difficulty difficulty;
    }

    public Output getById(Id problemId);

    public List<Output> get(int page, int perPage);
}