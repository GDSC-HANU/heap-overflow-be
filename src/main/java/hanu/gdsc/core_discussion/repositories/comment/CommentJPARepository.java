package hanu.gdsc.core_discussion.repositories.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;


public interface CommentJPARepository extends JpaRepository<CommentEntity, String> {
    Set<CommentEntity> findAllByPostIdAndServiceToCreate(String postId, String serviceToCreate);
    @Query("DELETE FROM CommentEntity c WHERE c.postId in (:postIds)")
    void deleteAllByPostIds(Iterable<String> postIds);
}
