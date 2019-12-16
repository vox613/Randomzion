package com.stc21.boot.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


// контроллер для обработки всех запросов по кнопке Login
@Controller
@RequestMapping(path = "/login")
public class LoginController {

    @GetMapping
    public String showLoginPage(Model model) {
        return "login";
    }

    // успешная аутентификация
    // success = true -> главная страницу
    // success = false -> страница логина
    @PostMapping
    public String processLoginPage(
            @RequestParam(name = "success") Boolean success,
            Model model) {
        if (success)
            return "redirect:/";
        return "redirect:/login";
    }
}
