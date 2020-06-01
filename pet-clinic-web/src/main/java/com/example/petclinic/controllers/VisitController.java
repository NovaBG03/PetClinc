package com.example.petclinic.controllers;

import com.example.petclinic.model.Pet;
import com.example.petclinic.model.Visit;
import com.example.petclinic.services.PetService;
import com.example.petclinic.services.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
@Controller
public class VisitController {

    private final PetService petService;
    private final VisitService visitService;

    public VisitController(PetService petService, VisitService visitService) {
        this.petService = petService;
        this.visitService = visitService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable Long petId, Model model) {
        Pet pet = petService.findById(petId);

        Visit visit = Visit.builder().build();
        visit.setPet(pet);
        pet.getVisits().add(visit);

        model.addAttribute("pet", pet);

        return visit;
    }

    @GetMapping("/new")
    public String initNewVisitForm(Model model) {
        return "/pets/createOrUpdateVisitForm";
    }

    @PostMapping("/new")
    public String initNewVisitForm(@Valid Visit visit, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/pets/createOrUpdateVisitForm";
        }

        visitService.save(visit);

        return "redirect:/owners/{ownerId}";
    }
}
