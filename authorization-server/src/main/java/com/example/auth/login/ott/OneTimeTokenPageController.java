package com.example.auth.login.ott;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OneTimeTokenPageController {
    @GetMapping("/login/ott")
    public String ottLogin(@RequestParam(required = false) String token, Model model) {
        if (token != null) {
            model.addAttribute("tokenValue", token);
        }
        return "one-time-token-login";
    }
}