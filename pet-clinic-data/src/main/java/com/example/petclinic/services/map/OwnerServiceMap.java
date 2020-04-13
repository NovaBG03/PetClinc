package com.example.petclinic.services.map;

import com.example.petclinic.model.Owner;
import com.example.petclinic.services.OwnerService;

import java.util.Map;
import java.util.Set;

public class OwnerServiceMap extends AbstractMapService<Owner, Long> implements OwnerService {

    @Override
    public Owner save(Owner object) {
        return super.save(object.getId(), object);
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
}
