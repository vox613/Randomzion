package com.stc21.boot.auction.controller;

import com.stc21.boot.auction.dto.CategoryDto;
import com.stc21.boot.auction.dto.CityDto;
import com.stc21.boot.auction.dto.ConditionDto;
import com.stc21.boot.auction.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static java.util.Set.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static int ENTRIES_PER_PAGE = 5;

    private final LotService lotService;
    private final UserService userService;
    private final PhotoService photoService;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final ConditionService conditionService;

    public DashboardController(LotService lotService, UserService userService, PhotoService photoService, CategoryService categoryService, CityService cityService, ConditionService conditionService) {
        this.lotService = lotService;
        this.userService = userService;
        this.photoService = photoService;
        this.categoryService = categoryService;
        this.cityService = cityService;
        this.conditionService = conditionService;
    }

    @GetMapping()
    public String displayEverything(
            Model model,
            @RequestParam(required = false) String section,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        Set<String> possibleSections = new HashSet<>();
        possibleSections.add("lots");
        possibleSections.add("users");
        possibleSections.add("photos");
        possibleSections.add("categories");
        possibleSections.add("cities");
        possibleSections.add("conditions");

        String currentSection = section != null && possibleSections.contains(section) ? section : "lots";
        int currentPage = Math.max(page.orElse(1), 1);
        int pageSize = Math.max(size.orElse(ENTRIES_PER_PAGE), 1);
        String sortField = sortBy != null ? sortBy : "id";
        Sort.Direction sortDirection = sortDir != null && sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        PageRequest defaultPageable = PageRequest.of(0, 5);
        Sort sort = Sort.by(sortDirection, sortField);
        PageRequest specificPageable = PageRequest.of(currentPage - 1, pageSize, sort);

        model.addAttribute("section", currentSection);
        model.addAttribute("sortBy", sortField);
        model.addAttribute("sortDir", sortDirection == Sort.Direction.ASC ? "asc" : "desc");

        model.addAttribute(      "lots",       lotService.getPaginatedEvenDeleted(currentSection.equals(      "lots") ? specificPageable : defaultPageable));
        model.addAttribute(     "users",      userService.getPaginatedEvenDeleted(currentSection.equals(     "users") ? specificPageable : defaultPageable));
        model.addAttribute(    "photos",     photoService.getPaginatedEvenDeleted(currentSection.equals(    "photos") ? specificPageable : defaultPageable));
        model.addAttribute("categories",  categoryService.getAllSortedEvenDeleted(currentSection.equals("categories") ? sort             : Sort.unsorted()));
        model.addAttribute(    "cities",      cityService.getAllSortedEvenDeleted(currentSection.equals(    "cities") ? sort             : Sort.unsorted()));
        model.addAttribute("conditions", conditionService.getAllSortedEvenDeleted(currentSection.equals("conditions") ? sort             : Sort.unsorted()));

        return "dashboard";
    }

    /**
     * Методы дублируются для уверенности, что несколько POST-запросов не будут переключать состояние как тумблер
     */
    @PostMapping(params = "action=delete")
    public String deleteAThing(
            @RequestParam String type,
            @RequestParam long id) {

        changeDeletedToFor(true, type, id);

        return "redirect:dashboard?section="+type;
    }

    @PostMapping(params = "action=return")
    public String returnAThing(
            @RequestParam String type,
            @RequestParam long id) {

        changeDeletedToFor(false, type, id);

        return "redirect:dashboard?section="+type;
    }

    private void changeDeletedToFor(boolean newValue, String type, long id) {
        switch (type) {
            case       "lots":       lotService.setDeletedTo(id, newValue); break;
            case      "users":      userService.setDeletedTo(id, newValue); break;
            case     "photos":     photoService.setDeletedTo(id, newValue); break;
            case "categories":  categoryService.setDeletedTo(id, newValue); break;
            case     "cities":      cityService.setDeletedTo(id, newValue); break;
            case "conditions": conditionService.setDeletedTo(id, newValue); break;
        }
    }

    @PostMapping(params = "action=add")
    private String addAThing(
            @RequestParam String type,
            @RequestParam String name) {

        switch (type) {
            case "categories":  categoryService.save(new CategoryDto(name));
            case     "cities":      cityService.save(new CityDto(name));
            case "conditions": conditionService.save(new ConditionDto(name));
        }

        return "redirect:dashboard?section="+type;
    }
}
