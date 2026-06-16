package com.iimmersao.userapplication.services;

import com.iimmersao.userapplication.models.Pet;
import com.iimmersao.userapplication.models.User;

public interface CombinedService {
    void createPetAndUser(Pet pet, User user);
}
