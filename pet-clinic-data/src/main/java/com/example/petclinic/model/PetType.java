package com.example.petclinic.model;

import java.util.Set;

public class PetType extends BaseEntity{

    private String name;
    private Set<Pet> pets;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }
}
