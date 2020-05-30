package com.example.petclinic.controllers;

import com.example.petclinic.model.Owner;
import com.example.petclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RequestMapping("/owners")
@Controller
public class OwnerController {

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
    public String processFindForm(Model model, Owner owner) {

        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        Set<Owner> foundOwners = ownerService.findAllByLastNameLike("%" + owner.getLastName() + "%");

        if (foundOwners.isEmpty()) {
            return "/owners/findOwners";
        }
        else if (foundOwners.size() == 1) {
            Owner foundOwner = foundOwners.stream().findFirst().get();
            return "redirect:/owners/" + foundOwner.getId();
        }
        else {
            model.addAttribute("selections", foundOwners);
            return "/owners/ownersList";
        }
    }
}
