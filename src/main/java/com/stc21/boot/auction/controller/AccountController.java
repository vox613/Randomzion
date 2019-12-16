package com.stc21.boot.auction.controller;

import com.stc21.boot.auction.dto.UserDto;
import com.stc21.boot.auction.entity.Lot;
import com.stc21.boot.auction.entity.Purchase;
import com.stc21.boot.auction.entity.User;
import com.stc21.boot.auction.repository.PurchaseRepository;
import com.stc21.boot.auction.service.LotService;
import com.stc21.boot.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/account")
public class AccountController {

    LotService lotService;
    UserService userService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public AccountController(LotService lotService, UserService userService) {
        this.lotService = lotService;
        this.userService = userService;
    }

    @GetMapping()
    public String showLotsPage(Model model,
                               @AuthenticationPrincipal Authentication token) {
        List<Lot> userLots = lotService.getAllLotsByUsername(token);
        List<Lot> allLots = lotService.getAllLots();
        List<UserDto> allUsers = userService.getAllUsers();
        UserDto currentUser = userService.findByUsername(token.getName());
        List<Purchase> allByBuyer = purchaseRepository.findAllByBuyer(userService.convertToEntity(currentUser));
        model.addAttribute("lots", userLots);
        model.addAttribute("user", currentUser);
        model.addAttribute("purchases", allByBuyer);
        return "account";
    }
}