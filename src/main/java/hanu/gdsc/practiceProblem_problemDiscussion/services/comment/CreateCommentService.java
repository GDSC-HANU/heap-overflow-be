package hanu.gdsc.practiceProblem_problemDiscussion.services.comment;

import hanu.gdsc.practiceProblem_problemDiscussion.config.ServiceName;
import hanu.gdsc.practiceProblem_problemDiscussion.domains.Post;
import hanu.gdsc.practiceProblem_problemDiscussion.repositories.post.PostRepository;
import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class CreateCommentService {
    private PostRepository postRepository;
    private hanu.gdsc.core_discussion.services.comment.CreateCommentService createCoreDiscussionCommentService;

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {
        public Id postId;
        public Id author;
        public String content;
        public Id parentCommentId;
    }

    public Id execute(Input input) throws NotFoundException {
        Post post = postRepository.getById(input.postId);
        if (post == null)
            throw new NotFoundException("Unknown post");
        return createCoreDiscussionCommentService.create(new hanu.gdsc.core_discussion.services.comment.CreateCommentService.Input(
                input.author,
                input.content,
                input.parentCommentId,
                ServiceName.serviceName,
                post.getCorePostId()
        ));
    }
}
