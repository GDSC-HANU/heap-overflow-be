package hanu.gdsc.coder.repositories;

import hanu.gdsc.coder.domains.Coder;

import hanu.gdsc.share.domains.Id;
import hanu.gdsc.share.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CoderRepositoryImpl implements CoderRepository {
    @Autowired
    private CoderJpaRepository coderJpaRepository;

    @Override
    public void save(Coder coder) {
        coderJpaRepository.save(CoderEntity.fromDomains(coder));
    }

    @Override
    public List<Coder> get(int page, int perPage) {
        Page<CoderEntity> coderEntities = coderJpaRepository.findAll(
                PageRequest.of(page, perPage)
        );
        return coderEntities != null ? coderEntities.stream().map(x -> {
            try {
                return x.toDomain();
            } catch (InvalidInputException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()) : null;
    }

    @Override
    public Coder getById(Id id) throws InvalidInputException {
        CoderEntity coder = coderJpaRepository.getById(id.toString());
        return coder.toDomain();
    }
}
