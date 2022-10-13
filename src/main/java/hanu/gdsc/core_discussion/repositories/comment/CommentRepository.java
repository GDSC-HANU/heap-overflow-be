package hanu.gdsc.core_discussion.repositories.comment;

import hanu.gdsc.core_discussion.domains.Comment;
import hanu.gdsc.share.domains.Id;

import java.util.List;
import java.util.Set;

public interface CommentRepository {
    Set<Comment> findAllByPostIdAndServiceToCreate(Id postId, String serviceToCreate);
    void deleteAllByPostIds(List<Id> postIds);
}
