package hanu.gdsc.practiceProblem_problemDiscussion.services.post;

import hanu.gdsc.practiceProblem_problemDiscussion.domains.Post;
import hanu.gdsc.practiceProblem_problemDiscussion.repositories.post.PostRepository;
import hanu.gdsc.practiceProblem_problemDiscussion.services.core_post.SearchCorePostService;
import hanu.gdsc.share.domains.DateTime;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.error.NotFoundError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SearchPostService {
    private final SearchCorePostService searchCorePostService;
    private final PostRepository postRepository;

    @Getter
    @AllArgsConstructor
    public static class Output {
        private Id id;
        private Id problemId;
        private String title;
        private Id author;
        private DateTime createdAt;
        private DateTime updatedAt;
        private String content;

    }

    public Output getById(Id id) {
        Post post = postRepository.getById(id);
        if (post == null)
            throw new NotFoundError("Unknown post");
        hanu.gdsc.core_discussion.domains.Post corePost = searchCorePostService.getById(post.getCorePostId());
        return new Output(
                id,
                post.getProblemId(),
                corePost.getTitle(),
                corePost.getAuthor(),
                corePost.getCreatedAt(),
                corePost.getUpdatedAt(),
                corePost.getContent()
        );
    }
}
