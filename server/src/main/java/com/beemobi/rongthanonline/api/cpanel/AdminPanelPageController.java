package com.beemobi.rongthanonline.api.cpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelPageController {

    @GetMapping(path = "/cpanel")
    public String page() {
        return "forward:/cpanel/index.html";
    }
}
