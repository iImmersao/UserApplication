package com.iimmersao.userapplication.services;

import com.iimmersao.springmimic.annotations.Service;
import com.iimmersao.springmimic.core.ConfigLoader;
import com.iimmersao.springmimic.web.PageRequest;
import com.iimmersao.userapplication.models.*;
import com.iimmersao.userapplication.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {
    private static final Logger log = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;

    private final ConfigLoader config;

    private final String dbType;

    public PetService(
            MySqlPetRepository mySqlPetRepository,
            H2PetRepository h2PetRepository,
            MongoPetRepository mongoPetRepository,
            ConfigLoader config) {

        this.config = config;
        this.dbType = getDbType();
        if (mySqlPetRepository != null && "mysql".equals(dbType)) {
            this.petRepository = mySqlPetRepository;
        } else if (h2PetRepository != null && "h2".equals(dbType)) {
            this.petRepository = h2PetRepository;
        } else if (mongoPetRepository != null && "mongodb".equals(dbType)) {
            this.petRepository = mongoPetRepository;
        } else {
            throw new IllegalStateException("No suitable UserRepository found. Check your database configuration.");
        }
    }

    private String getDbType() {
        return config.get("db.type", "mysql").trim().toLowerCase();
    }

    private Class getEntityClass() {
        return switch (dbType) {
            case "mysql" -> MySqlPetEntity.class;
            case "h2" -> H2PetEntity.class;
            case "mongodb" -> MongoPetEntity.class;
            default -> throw new IllegalStateException("Unsupported db.type: " + dbType);
        };
    }

    public Pet save(Pet dto) {
        BasePetEntity entity = PetMapper.toEntity(dto, dbType);
        petRepository.save(entity);
        return PetMapper.toDTO(entity);
    }

    public Optional<Pet> findById(String id) {
        Object typedId = convertId(id);
        Optional<?> entityOpt = petRepository.findById(typedId);
        return entityOpt.map(PetMapper::toDTO);
    }

    public List<Pet> findAll() {
        PageRequest p = new PageRequest();
        List<?> entities = petRepository.findAll(p);
        return entities.stream()
                .map(PetMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void update(Pet dto) {
        Object entity = PetMapper.toEntity(dto, dbType);
        petRepository.save(entity);
    }

    public void deleteById(String id) {
        Object typedId = convertId(id);
        petRepository.deleteById(typedId);
    }

    public void deleteAll() {
        petRepository.deleteAll();
    }

    public List<Pet> findAll(PageRequest pageRequest) {
        List<?> entities = petRepository.findAll(pageRequest);
        return entities.stream()
                .map(PetMapper::toDTO)
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
