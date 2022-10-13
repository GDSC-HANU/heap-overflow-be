package hanu.gdsc.practiceProblem_problemDiscussion.repositories.post;

import hanu.gdsc.practiceProblem_problemDiscussion.domains.Post;
import hanu.gdsc.share.domains.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "PracticeProblem.PostRepositoryImpl")
public class PostRepositoryImpl implements PostRepository {
    @Autowired
    private PPPostJPARepository pPPostJpaRepository;
    @Autowired
    private hanu.gdsc.core_discussion.repositories.post.PostRepository corePostRepository;

    @Override
    public void save(Post post) {
        pPPostJpaRepository.save(PPPostEntity.toEntity(post));
    }

    @Override
    public Post getById(Id id) {
        return PPPostEntity.toDomain(pPPostJpaRepository.getById(id.toString()));
    }

    @Override
    public List<Post> getPosts(Id problemId,
                               int page,
                               int perPage) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by("createdAtMillis").descending());
        Page<PPPostEntity> entities = pPPostJpaRepository
                .findByProblemId(
                        problemId.toString(),
                        pageable);
        return entities.getContent()
                .stream()
                .map(e -> PPPostEntity.toDomain(e))
                .collect(Collectors.toList());
    }

    @Override
    public long countPosts(Id problemId) {
        return pPPostJpaRepository
                .countByProblemId(problemId.toString());
    }

    @Override
    public void deleteAllByProblemId(Id problemId) {
        List<PPPostEntity> pPPostEntitys = pPPostJpaRepository.findByProblemId(problemId.toString());
        List<Id> corePostIds = pPPostEntitys.stream()
                        .map(postEntity -> new Id(postEntity.getCorePostId()))
                        .collect(Collectors.toList());
        //todo delete comment
        corePostRepository.deleteAllByIds(corePostIds);
        pPPostJpaRepository.deleteAllByProblemId(problemId.toString());
    }
}
