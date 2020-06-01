package com.example.petclinic.controllers;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.services.OwnerService;
import com.example.petclinic.services.PetService;
import com.example.petclinic.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    PetService petService;

    @Mock
    PetTypeService petTypeService;

    @Mock
    OwnerService ownerService;

    @InjectMocks
    PetController controller;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testInitCreationForm() throws Exception {
        Long ownerId = 1L;
        Owner owner = Owner.builder().id(ownerId).build();

        when(ownerService.findById(ownerId)).thenReturn(owner);

        mvc.perform(get("/owners/" + ownerId + "/pets/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/pets/createOrUpdatePetForm"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(model().attributeExists("types"));
    }

    @Test
    void testProcessCreationForm() throws Exception {
        Long ownerId = 1L;
        Owner owner = Owner.builder().id(ownerId).build();

        Pet pet = Pet.builder().build();
        pet.setOwner(owner);
        owner.getPets().add(pet);

        when(ownerService.findById(ownerId)).thenReturn(owner);

        mvc.perform(post("/owners/" + ownerId + "/pets/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ownerId));

        verify(petService).save(any());
    }

    @Test
    void testInitUpdateForm() throws Exception {
        Long ownerId = 1L;
        Owner owner = Owner.builder().id(ownerId).build();

        Long petId = 2L;
        Pet savedPet = Pet.builder().id(petId).build();

        when(ownerService.findById(ownerId)).thenReturn(owner);
        when(petService.findById(petId)).thenReturn(savedPet);

        mvc.perform(get("/owners/" + ownerId + "/pets/" + petId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("/pets/createOrUpdatePetForm"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(model().attributeExists("types"));
    }

    @Test
    void testProcessUpdateForm() throws Exception {
        Long ownerId = 1L;
        Owner owner = Owner.builder().id(ownerId).build();

        Long petId = 2L;
        Pet pet = Pet.builder().id(petId).build();
        pet.setOwner(owner);
        owner.getPets().add(pet);

        when(ownerService.findById(ownerId)).thenReturn(owner);

        mvc.perform(post("/owners/" + ownerId + "/pets/" + petId + "/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ownerId));
    }
}
