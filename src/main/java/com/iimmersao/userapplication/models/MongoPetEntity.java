package com.iimmersao.userapplication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iimmersao.springmimic.annotations.*;

@Entity
@Table(name = "pets")
public class MongoPetEntity implements BasePetEntity<String> {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @JsonProperty("_id")
    private String id;

    @Column(name = "petname")
    private String petname;

    @Column(name = "username")
    private String username;

    public MongoPetEntity() {}

    public MongoPetEntity(String petname, String username) {
        this.petname = petname;
        this.username = username;
    }

    public MongoPetEntity(String id, String petname, String username) {
        this.id = id;
        this.petname = petname;
        this.username = username;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return String.format("Pet{id=%s, petname='%s', username='%s'}", id, petname, username);
    }
}
