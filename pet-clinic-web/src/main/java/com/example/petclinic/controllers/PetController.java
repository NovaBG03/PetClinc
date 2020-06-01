package com.example.petclinic.controllers;

import com.example.petclinic.model.Owner;
import com.example.petclinic.model.Pet;
import com.example.petclinic.model.PetType;
import com.example.petclinic.services.OwnerService;
import com.example.petclinic.services.PetService;
import com.example.petclinic.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping("/owners/{ownerId}")
@Controller
public class PetController {
    private static final String PETS_CREATE_OR_UPDATE_PET_FORM = "/pets/createOrUpdatePetForm";

    private final PetService petService;
    private final PetTypeService petTypeService;
    private final OwnerService ownerService;

    public PetController(PetService petService, PetTypeService petTypeService, OwnerService ownerService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
        this.ownerService = ownerService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, Model model) {
        Pet pet = Pet.builder().owner(owner).build();
        owner.getPets().add(pet);
        model.addAttribute("pet", pet);

        return PETS_CREATE_OR_UPDATE_PET_FORM;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult bindingResult, Model model) {
        if (StringUtils.hasLength(pet.getName())
                && pet.isNew()
                && (owner.getPet(pet.getName(), true) != null)) {
            bindingResult.rejectValue("name", "duplicate", "already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pet", pet);
            return PETS_CREATE_OR_UPDATE_PET_FORM;
        }

        pet.setOwner(owner);
        petService.save(pet);

        return "redirect:/owners/" + owner.getId();
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable Long petId, Model model) {
        Pet pet = petService.findById(petId);
        model.addAttribute("pet", pet);

        return PETS_CREATE_OR_UPDATE_PET_FORM;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            //pet.setOwner(owner);
            model.addAttribute("pet", pet);
            return PETS_CREATE_OR_UPDATE_PET_FORM;
        }

        pet.setOwner(owner);
        petService.save(pet);

        return "redirect:/owners/" + owner.getId();
    }
}
