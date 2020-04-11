package com.example.petclinic.services.map;

import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapService<T, ID> {

    protected Map<ID, T> map = new HashMap<ID, T>();

    public Set<T> findAll() {
        return (Set<T>) map.values();
    }

    public T findById(ID id) {
        return map.get(id);
    }

    public T save(ID id, T object) {
        map.put(id, object);

        return object;
    }

    public void delete(T object) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(object));
    }

    public void deleteById(ID id) {
        map.remove(id);
    }
}
