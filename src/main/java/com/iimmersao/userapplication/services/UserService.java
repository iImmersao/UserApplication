package com.iimmersao.userapplication.services;

import com.iimmersao.springmimic.annotations.Inject;
import com.iimmersao.springmimic.annotations.Service;
import com.iimmersao.springmimic.core.ConfigLoader;
import com.iimmersao.springmimic.database.DatabaseClient;
import com.iimmersao.springmimic.web.PageRequest;
import com.iimmersao.userapplication.models.*;
import com.iimmersao.userapplication.repositories.H2UserRepository;
import com.iimmersao.userapplication.repositories.MongoUserRepository;
import com.iimmersao.userapplication.repositories.MySqlUserRepository;
import com.iimmersao.userapplication.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

//    @Inject
//    private DatabaseClient databaseClient;

    private final UserRepository userRepository;

    private final ConfigLoader config;

    private final String dbType;

    public UserService(
            MySqlUserRepository mySqlUserRepository,
            H2UserRepository h2UserRepository,
            MongoUserRepository mongoUserRepository,
            ConfigLoader config) {

        this.config = config;
        this.dbType = getDbType();
        if (mySqlUserRepository != null && "mysql".equals(dbType)) {
            this.userRepository = mySqlUserRepository;
        } else if (h2UserRepository != null && "h2".equals(dbType)) {
            this.userRepository = h2UserRepository;
        } else if (mongoUserRepository != null && "mongodb".equals(dbType)) {
            this.userRepository = mongoUserRepository;
        } else {
            throw new IllegalStateException("No suitable UserRepository found. Check your database configuration.");
        }
    }

    private String getDbType() {
        return config.get("db.type", "mysql").trim().toLowerCase();
    }

    private Class getEntityClass() {
        return switch (dbType) {
            case "mysql" -> MySqlUserEntity.class;
            case "h2" -> H2UserEntity.class;
            case "mongodb" -> MongoUserEntity.class;
            default -> throw new IllegalStateException("Unsupported db.type: " + dbType);
        };
    }

//    public User save(User dto) {
//        Object entity = UserMapper.toEntity(dto, getDbType());
//        databaseClient.save(entity);
//        return UserMapper.toDTO(entity);
//    }

    public User save(User dto) {
        BaseUserEntity entity = UserMapper.toEntity(dto, dbType);
        userRepository.save(entity);
        return UserMapper.toDTO(entity);
    }

//    public Optional<User> findById(String id) {
//        Object typedId = convertId(id);
//        Optional<?> entityOpt = databaseClient.findById(getEntityClass(), typedId);
//        return entityOpt.map(UserMapper::toDTO);
//    }

    public Optional<User> findById(String id) {
        Object typedId = convertId(id);
        Optional<?> entityOpt = userRepository.findById(typedId);
        return entityOpt.map(UserMapper::toDTO);
    }

    public List<User> findAll() {
        PageRequest p = new PageRequest();
        List<?> entities = userRepository.findAll(p);
        return entities.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void update(User dto) {
        Object entity = UserMapper.toEntity(dto, dbType);
        userRepository.save(entity);
    }

    public void deleteById(String id) {
        Object typedId = convertId(id);
        userRepository.deleteById(typedId);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public List<User> findAll(PageRequest pageRequest) {
        List<?> entities = userRepository.findAll(pageRequest);
        return entities.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Convert String id to the appropriate type for the selected backend
    private Object convertId(String id) {
        return switch (dbType) {
            case "mysql" -> Integer.parseInt(id);
            case "h2" -> Integer.parseInt(id);
            case "mongodb" -> id; // remains a String
            default -> throw new IllegalStateException("Unsupported db.type: " + dbType);
        };
    }
}
