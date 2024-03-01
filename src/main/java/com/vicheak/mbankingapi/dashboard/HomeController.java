package com.vicheak.mbankingapi.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String viewIndex(){
        return "/index";
    }

    @GetMapping("/index")
    public String viewHome(){
        return "/index";
    }

}
