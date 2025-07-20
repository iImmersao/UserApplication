package com.iimmersao.userapplication.models;

public class UserMapper {
    public static User toDTO(Object userEntity) {
        if (userEntity instanceof MySqlUserEntity u) {
            return new User(String.valueOf(u.getId()), u.getUsername(), u.getEmail());
        } else if (userEntity instanceof H2UserEntity u) {
            return new User(String.valueOf(u.getId()), u.getUsername(), u.getEmail());
        } else if (userEntity instanceof MongoUserEntity u) {
            return new User(u.getId(), u.getUsername(), u.getEmail());
        }
        throw new IllegalArgumentException("Unsupported entity type");
    }

    public static BaseUserEntity toEntity(User dto, String dbType) {
        if ("mysql".equalsIgnoreCase(dbType)) {
            MySqlUserEntity user = new MySqlUserEntity();
            if (dto.getId() != null) user.setId(Integer.parseInt(dto.getId()));
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            return user;
        } else if ("h2".equalsIgnoreCase(dbType)) {
            H2UserEntity user = new H2UserEntity();
            if (dto.getId() != null) user.setId(Integer.parseInt(dto.getId()));
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            return user;
        } else if ("mongodb".equalsIgnoreCase(dbType)) {
            MongoUserEntity user = new MongoUserEntity();
            user.setId(dto.getId());
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            return user;
        }
        throw new IllegalArgumentException("Unsupported dbType");
    }
}
