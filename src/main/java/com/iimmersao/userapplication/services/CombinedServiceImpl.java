package com.iimmersao.userapplication.services;

import com.iimmersao.springmimic.annotations.Service;
import com.iimmersao.springmimic.annotations.Transactional;
import com.iimmersao.userapplication.models.Pet;
import com.iimmersao.userapplication.models.User;

import java.util.Arrays;

@Service
public class CombinedServiceImpl implements CombinedService {
    private final PetService pets;
    private final UserService users;

    public CombinedServiceImpl(PetService pets, UserService users) {
        this.pets = pets;
        this.users = users;
    }

    @Transactional(rollbackFor = com.iimmersao.springmimic.exceptions.DatabaseException.class)
    public void createPetAndUser(Pet pet, User user) {
        pets.save(pet);
        users.save(user);
    }
}
