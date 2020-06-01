package com.example.petclinic.formatters;

import com.example.petclinic.model.PetType;
import com.example.petclinic.services.PetTypeService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Set;

@Component
public class PetTypeFormatter implements Formatter<PetType> {

    private final PetTypeService petTypeService;

    public PetTypeFormatter(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @Override
    public PetType parse(String name, Locale locale) throws ParseException {
        Set<PetType> petTypes = petTypeService.findAll();

        for (PetType petType: petTypes) {
            if (petType.getName().equals(name)) {
                return petType;
            }
        }

        return null;
    }

    @Override
    public String print(PetType petType, Locale locale) {
        return petType.getName();
    }
}
