package com.example.petclinic.model;

import java.util.Set;

public class Owner extends Person {

    private Set<Pet> pets;

    public Set<Pet> getPets() {
        return pets;
    }

    private void setPets(Set<Pet> pets) {
        this.pets = pets;
    }
}
