package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.userapplication.models.H2PetEntity;

@Repository
public interface H2PetRepository extends PetRepository<H2PetEntity, Integer> {
}