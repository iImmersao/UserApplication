package com.iimmersao.userapplication.models;

public class PetMapper {
    public static Pet toDTO(Object petEntity) {
        if (petEntity instanceof MySqlPetEntity u) {
            return new Pet(String.valueOf(u.getId()), u.getPetname(),u.getUsername());
        } else if (petEntity instanceof H2PetEntity u) {
            return new Pet(String.valueOf(u.getId()), u.getPetname(), u.getUsername());
        } else if (petEntity instanceof MongoPetEntity u) {
            return new Pet(u.getId(), u.getPetname(), u.getUsername());
        }
        throw new IllegalArgumentException("Unsupported entity type");
    }

    public static BasePetEntity toEntity(Pet dto, String dbType) {
        if ("mysql".equalsIgnoreCase(dbType)) {
            MySqlPetEntity pet = new MySqlPetEntity();
            if (dto.getId() != null) pet.setId(Integer.parseInt(dto.getId()));
            pet.setPetname(dto.getPetname());
            pet.setUsername(dto.getUsername());
            return pet;
        } else if ("h2".equalsIgnoreCase(dbType)) {
            H2PetEntity pet = new H2PetEntity();
            if (dto.getId() != null) pet.setId(Integer.parseInt(dto.getId()));
            pet.setPetname(dto.getPetname());
            pet.setUsername(dto.getUsername());
            return pet;
        } else if ("mongodb".equalsIgnoreCase(dbType)) {
            MongoPetEntity pet = new MongoPetEntity();
            pet.setId(dto.getId());
            pet.setPetname(dto.getPetname());
            pet.setUsername(dto.getUsername());
            return pet;
            /*
             */
        }
        throw new IllegalArgumentException("Unsupported dbType");
    }
}
