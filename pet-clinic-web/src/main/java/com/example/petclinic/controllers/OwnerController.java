package com.example.petclinic.controllers;

import com.example.petclinic.model.Owner;
import com.example.petclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Set;

@RequestMapping("/owners")
@Controller
public class OwnerController {
    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "/owners/createOrUpdateOwnerForm";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setDisallowedFields(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }

    @GetMapping("/{id}")
    public ModelAndView showOwner(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/owners/ownerDetails");
        modelAndView.addObject("owner", ownerService.findById(id));
        return modelAndView;
    }

    @GetMapping("/find")
    public String findOwners(Model model) {
        Owner owner = Owner.builder().build();

        model.addAttribute("owner", owner);

        return "/owners/findOwners";
    }

    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model) {

        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        Set<Owner> foundOwners = ownerService.findAllByLastNameLike("%" + owner.getLastName() + "%");

        if (foundOwners.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return "/owners/findOwners";
        } else if (foundOwners.size() == 1) {
            Owner foundOwner = foundOwners.stream().findFirst().get();
            return "redirect:/owners/" + foundOwner.getId();
        } else {
            model.addAttribute("selections", foundOwners);
            return "/owners/ownersList";
        }
    }

    @GetMapping("/new")
    public String initCreationForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());

        return OWNERS_CREATE_OR_UPDATE_OWNER_FORM;
    }

    @PostMapping("/new")
    public String processCreationFrom(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return OWNERS_CREATE_OR_UPDATE_OWNER_FORM;
        }

        Owner savedOwner = ownerService.save(owner);

        return "redirect:/owners/" + savedOwner.getId();
    }

    @GetMapping("/{ownerId}/edit")
    public String initUpdateForm(@PathVariable Long ownerId,Model model) {
        model.addAttribute("owner", ownerService.findById(ownerId));

        return OWNERS_CREATE_OR_UPDATE_OWNER_FORM;
    }

    @PostMapping("/{ownerId}/edit")
    public String processUpdateFrom(@Valid Owner owner, BindingResult result, @PathVariable Long ownerId) {
        if (result.hasErrors()) {
            return OWNERS_CREATE_OR_UPDATE_OWNER_FORM;
        }

        owner.setId(ownerId);
        Owner savedOwner = ownerService.save(owner);

        return "redirect:/owners/" + savedOwner.getId();
    }
}
