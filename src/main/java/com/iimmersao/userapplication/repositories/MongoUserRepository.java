package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.userapplication.models.MongoUserEntity;

@Repository
public interface MongoUserRepository extends UserRepository<MongoUserEntity, String> {
}
