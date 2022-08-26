package hanu.gdsc.core_discussion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJPARepository extends JpaRepository<PPostEntity, String> {

    PPostEntity getByIdAndServiceToCreate(String id, String serviceToCreate);

    List<PPostEntity> findByIdInAndServiceToCreate(List<String> ids, String serviceToCreate);
}