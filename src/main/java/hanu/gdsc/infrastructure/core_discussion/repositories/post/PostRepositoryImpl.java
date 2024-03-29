package hanu.gdsc.infrastructure.core_discussion.repositories.post;

import hanu.gdsc.domain.core_discussion.models.Post;
import hanu.gdsc.domain.core_discussion.repositories.CommentRepository;
import hanu.gdsc.domain.core_discussion.repositories.PostRepository;
import hanu.gdsc.domain.share.models.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {
    @Autowired
    private PostJPARepository postJPARepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Id save(Post post) {
        postJPARepository.save(PPostEntity.toEntity(post));
        return post.getId();
    }

    @Override
    public Post getById(Id id, String serviceToCreate) {
        try {
            return PPostEntity.toDomain(postJPARepository.getByIdAndServiceToCreate(id.toString(), serviceToCreate));
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<Post> getByIds(List<Id> ids, String serviceToCreate) {
        return postJPARepository.findByIdInAndServiceToCreate(
                        ids.stream()
                                .map(id -> id.toString())
                                .collect(Collectors.toList()),
                        serviceToCreate
                ).stream()
                .map(p -> PPostEntity.toDomain(p))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByIds(List<Id> corePostIds) {

        commentRepository.deleteAllByPostIds(corePostIds);
        postJPARepository.deleteAllByIdIn(corePostIds.stream().map(Id::toString).collect(Collectors.toList()));
    }
}
