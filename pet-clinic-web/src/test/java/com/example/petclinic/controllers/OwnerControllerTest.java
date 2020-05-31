package com.example.petclinic.controllers;

import com.example.petclinic.model.Owner;
import com.example.petclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    Set<Owner> owners;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        owners = new HashSet<>();
        owners.add(Owner.builder().id(1L).build());
        owners.add(Owner.builder().id(2L).build());

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showOwner() throws Exception {
        Long id = 1L;

        Owner owner = new Owner();
        owner.setId(id);

        when(ownerService.findById(id)).thenReturn(owner);

        mockMvc.perform(get("/owners/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("/owners/ownerDetails"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void testFindOwners() throws Exception {
        Owner owner = Owner.builder().build();

        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("/owners/findOwners"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void testProcessFindFormZero() throws Exception {
        Set<Owner> returnSet = new HashSet<>();

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(returnSet);

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("/owners/findOwners"));
    }

    @Test
    void testProcessFindFormOne() throws Exception {
        Long id = 1L;
        String lastName = "lastName";
        Owner owner = Owner.builder().id(id).lastName(lastName).build();

        Set<Owner> returnSet = new HashSet<>();
        returnSet.add(owner);

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(returnSet);

        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + id));
    }

    @Test
    void testProcessFindFormMany() throws Exception {
        String lastName = "lastName";
        Owner owner1 = Owner.builder().lastName(lastName).build();
        Owner owner2 = Owner.builder().lastName(lastName).build();

        Set<Owner> returnSet = new HashSet<>();
        returnSet.add(owner1);
        returnSet.add(owner2);

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(returnSet);

        mockMvc.perform(get("/owners")
                .param("lastName", lastName))
                .andExpect(status().isOk())
                .andExpect(view().name("/owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));
    }
}