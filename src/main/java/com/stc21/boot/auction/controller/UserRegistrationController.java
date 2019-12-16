package com.stc21.boot.auction.controller;

import com.stc21.boot.auction.dto.UserRegistrationDto;
import com.stc21.boot.auction.entity.City;
import com.stc21.boot.auction.service.CityService;
import com.stc21.boot.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @Autowired
    private CityService cityService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        List<City> cities = cityService.getAllCities();
        model.addAttribute("cities", cities);
        return "register";
    }

    @PostMapping
    public String registerUserAccount(
            Model model,
            @ModelAttribute("user") @Valid UserRegistrationDto userRegistrationDto,
            BindingResult result) {

        userService
                .fieldsWithErrors(userRegistrationDto)
                .forEach(
                        fieldName->result.rejectValue(
                                fieldName,
                                null,
                                "Username with this " + fieldName + " already exist. Pick another one."));

        if (result.hasErrors()) {
            List<City> cities = cityService.getAllCities();
            model.addAttribute("cities", cities);
            return "register";
        }

        userService.save(userRegistrationDto);
        return "redirect:/register?success=true";
    }
}