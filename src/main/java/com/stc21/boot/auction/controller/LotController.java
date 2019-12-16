package com.stc21.boot.auction.controller;

import com.stc21.boot.auction.dto.LotDto;
import com.stc21.boot.auction.exception.NotEnoughMoneyException;
import com.stc21.boot.auction.exception.PageNotFoundException;
import com.stc21.boot.auction.repository.UserRepository;
import com.stc21.boot.auction.service.LotService;
import com.stc21.boot.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/lot")
public class LotController {

    @Autowired
    private LotService lotService;

    @Autowired
    private UserService userService;


    @GetMapping()
    public String showLotPage(Model model,
                              @RequestParam(name = "id") Long id) {
        LotDto lotDto = lotService.findById(id);
        if (lotDto == null)
            throw new PageNotFoundException();
        else {
            model.addAttribute("lot", lotDto);
            return "lot";
        }
    }

/*    @PostMapping(path = "/{lotId}")
    public String showLotPage(Model model, @RequestParam(defaultValue = "1") long lotId) {
        LotDto currLot = lotService.findByLotId(lotId);
        model.addAttribute("currentLot", currLot);
        return "lot";
    }*/

    @PostMapping(path = "/buy")
    public String buyLot(Model model,
                         @AuthenticationPrincipal Authentication token,
                         @RequestParam(defaultValue = "1") long lotId) {
        String username = token.getName();

        userService.findByUsername(username);
        try{
            lotService.sale(username, lotService.findById(lotId));
        } catch (NotEnoughMoneyException e){
            // TODO показать покупателю, что недостатчно денег
        }
//        model.addAttribute("currentUser", username);
        return "redirect:/";
    }
}
