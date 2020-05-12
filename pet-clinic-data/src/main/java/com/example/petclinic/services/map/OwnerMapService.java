package com.example.petclinic.services.map;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.services.OwnerService;
import com.example.petclinic.services.PetService;
import com.example.petclinic.services.PetTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@Profile({"default", "map"})
public class OwnerMapService extends AbstractMapService<Owner, Long> implements OwnerService {

    private final PetService petService;
    private final PetTypeService petTypeService;

    public OwnerMapService(PetService petService, PetTypeService petTypeService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
    }

    @Override
    public Owner findByLastName(String lastName) {
        Set<Map.Entry<Long, Owner>> entities = this.map.entrySet();

        for (Map.Entry<Long, Owner> entry: entities){
            Owner owner = entry.getValue();

            if (owner.getLastName() == lastName) {
                return owner;
            }
        }

        return null;
    }

    @Override
    public Owner save(Owner object) {

        if (object != null) {

            if (object.getPets() != null) {
                object.getPets().forEach(pet -> {
                    PetType petType = pet.getPetType();
                    if (petType.getId() == null) {
                        PetType savedPetType = petTypeService.save(petType);
                        pet.setPetType(savedPetType);
                    }

                    if (pet.getId() == null) {
                        Pet savedPet = petService.save(pet);
                        pet.setId(savedPet.getId());
                    }
                });
            }

            return super.save(object);
        }
        else {
            throw new RuntimeException("Object cannot be null.");
        }

    }

    @Override
    public Owner findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Set<Owner> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(Owner object) {
        super.delete(object);
    }
}
