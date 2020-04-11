package com.example.petclinic.services.map;

import com.example.petclinic.model.Pet;
import com.example.petclinic.services.CrudService;

public class PetServiceMap extends AbstractMapService<Pet, Long> implements CrudService<Pet, Long> {
    @Override
    public Pet save(Pet object) {
        return super.save(object.getId(), object);
    }
}
