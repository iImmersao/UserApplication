package com.iimmersao.userapplication.models;

import com.iimmersao.springmimic.annotations.*;

@Entity
@Table(name = "pets")
public class H2PetEntity implements BasePetEntity<Integer> {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "petname")
    private String petname;

    @Column(name = "username")
    private String username;

    public H2PetEntity() {}

    public H2PetEntity(String petname, String username) {
        this.petname = petname;
        this.username = username;
    }

    public H2PetEntity(Integer id, String petname, String username) {
        this.id = id;
        this.petname = petname;
        this.username = username;
    }

    // Getters and setters
    public Integer getId() { return id; }

    @Override
    public void setId(Integer integer) {

    }

    public void setId(int id) { this.id = id; }

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

