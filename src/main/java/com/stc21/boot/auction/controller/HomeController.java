package com.stc21.boot.auction.controller;

import com.stc21.boot.auction.dto.LotDto;
import com.stc21.boot.auction.dto.UserDto;
import com.stc21.boot.auction.entity.Lot;
import com.stc21.boot.auction.service.LotService;
import com.stc21.boot.auction.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping(path = "/")
public class HomeController {

    private final UserService userService;

    private final LotService lotService;

    public HomeController(UserService userService, LotService lotService) {
        this.userService = userService;
        this.lotService = lotService;
    }

    // возвращает результат постранично
    @GetMapping(path = "/")
    public String showHomePage(Model model, @RequestParam(defaultValue = "0") int page, Authentication token) {
        Page<LotDto> pagedHomePageLots;
/*
        if (token == null) {
            pagedHomePageLots = lotService.getPageOfHomePageLots(page);
        } else {
            pagedHomePageLots = lotService.getPageOfHomePageLots(page, token);
        }
*/
        pagedHomePageLots = lotService.getPageOfHomePageLots(page);
        model.addAttribute("lots", pagedHomePageLots);
        return "home";
    }

}
