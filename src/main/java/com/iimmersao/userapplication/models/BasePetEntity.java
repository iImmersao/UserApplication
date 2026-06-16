package com.iimmersao.userapplication.models;

public interface BasePetEntity<ID> {
    ID getId();
    void setId(ID id);
    String getPetname();
    void setPetname(String petname);
    String getUsername();
    void setUsername(String username);
}
