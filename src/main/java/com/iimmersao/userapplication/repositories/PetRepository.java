package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.repository.CrudRepository;
import com.iimmersao.springmimic.web.PageRequest;
import com.iimmersao.userapplication.models.BasePetEntity;

import java.util.List;

public interface PetRepository<T extends BasePetEntity<ID>, ID> extends CrudRepository<T, ID> {
    T findByPetname(String petname);
    T findByUsername(String username);

    boolean existsByPetname(String petname);
    boolean existsByUsername(String username);

    long countByPetname(String petname);
    long countByUsername(String username);

    List<T> findByPetnameContains(String partialName);
    List<T> findByPetnameContains(String partialName, PageRequest pageRequest);

    List<T> findAll(PageRequest pageRequest);

    void deleteAll();
}