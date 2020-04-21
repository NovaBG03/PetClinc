package com.example.petclinic.services.map;

import com.example.petclinic.model.Speciality;
import com.example.petclinic.services.SpecialityService;
import org.springframework.stereotype.Service;

@Service
public class SpecialityServiceMap extends AbstractMapService<Speciality, Long> implements SpecialityService {
}
