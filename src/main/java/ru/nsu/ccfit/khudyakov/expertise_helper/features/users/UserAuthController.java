package ru.nsu.ccfit.khudyakov.expertise_helper.features.users;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserAuthController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

}
