package hanu.gdsc.practiceProblem.services.category;

import hanu.gdsc.share.domains.Id;

public interface CreateCategoryService {
    public static class Input {
        public String name;
    }
    public Id create(Input input);
}
