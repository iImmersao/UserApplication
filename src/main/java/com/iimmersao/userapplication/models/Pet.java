package com.iimmersao.userapplication.models;

import com.iimmersao.springmimic.annotations.*;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;

    @Column(name = "petname")
    private String petname;

    @Column(name = "username")
    private String username;

    public Pet() {}

    public Pet(String petname, String username) {
        this.petname = petname;
        this.username = username;
    }

    public Pet(String id, String petname, String username) {
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
