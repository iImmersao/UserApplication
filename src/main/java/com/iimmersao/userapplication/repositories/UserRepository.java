package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.springmimic.repository.CrudRepository;
import com.iimmersao.springmimic.web.PageRequest;
import com.iimmersao.userapplication.models.User;


import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);
    User findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    long countByUsername(String username);
    long countByEmail(String email);

    List<User> findByUsernameContains(String partialName);
    List<User> findByUsernameContains(String partialName, PageRequest pageRequest);

    List<User> findAll(PageRequest pageRequest);

    void deleteAll();
}
