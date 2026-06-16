package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.userapplication.models.MySqlPetEntity;

@Repository
public interface MySqlPetRepository extends PetRepository<MySqlPetEntity, Integer> {
}