package com.example.petclinic.services.springdatajpa;

import com.example.petclinic.model.Owner;
import com.example.petclinic.repositories.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    @Mock
    OwnerRepository ownerRepository;

    @InjectMocks
    OwnerSDJpaService service;

    Owner returnOwner;

    static final Long OWNER_ID = 1L;

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().id(OWNER_ID).build();
    }

    @Test
    void findByLastName() {
        Mockito.when(ownerRepository.findByLastName(Mockito.anyString())).thenReturn(returnOwner);

        Owner fundOwner = service.findByLastName("Georgiev");

        assertEquals(OWNER_ID, fundOwner.getId());

        Mockito.verify(ownerRepository).findByLastName(Mockito.anyString());
    }

    @Test
    void findById() {
        Mockito.when(ownerRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(returnOwner));

        Owner foundOwner = service.findById(OWNER_ID);

        assertEquals(OWNER_ID, foundOwner.getId());

        Mockito.verify(ownerRepository).findById(Mockito.anyLong());
    }

    @Test
    void findByNotExistingId() {
        Mockito.when(ownerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Owner foundOwner = service.findById(OWNER_ID);

        assertNull(foundOwner);
    }

    @Test
    void save() {
        Mockito.when(ownerRepository.save(returnOwner)).thenReturn(returnOwner);

        Owner savedOwner = service.save(returnOwner);

        assertNotNull(savedOwner);

        Mockito.verify(ownerRepository).save(returnOwner);
    }

    @Test
    void findAll() {
        Owner owner1 = Owner.builder().id(1L).build();
        Owner owner2 = Owner.builder().id(2L).build();

        Set<Owner> owners = new HashSet<>();
        owners.add(owner1);
        owners.add(owner2);

        Mockito.when(ownerRepository.findAll()).thenReturn(owners);

        Set<Owner> foundOwners = service.findAll();

        assertEquals(owners.size(), foundOwners.size());
    }

    @Test
    void delete() {
        service.delete(returnOwner);

        Mockito.verify(ownerRepository).delete(returnOwner);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        Mockito.verify(ownerRepository).deleteById(Mockito.anyLong());
    }
}