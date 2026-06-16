package com.iimmersao.userapplication.repositories;

import com.iimmersao.springmimic.annotations.Repository;
import com.iimmersao.userapplication.models.MongoPetEntity;

@Repository
public interface MongoPetRepository extends PetRepository<MongoPetEntity, String> {
}