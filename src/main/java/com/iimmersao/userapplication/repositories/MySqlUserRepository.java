package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.userapplication.models.MySqlUserEntity;

@Repository
public interface MySqlUserRepository extends UserRepository<MySqlUserEntity, Integer> {
}
