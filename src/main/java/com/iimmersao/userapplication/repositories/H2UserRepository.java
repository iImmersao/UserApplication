package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.userapplication.models.H2UserEntity;

@Repository
public interface H2UserRepository extends UserRepository<H2UserEntity, Integer> {
}
