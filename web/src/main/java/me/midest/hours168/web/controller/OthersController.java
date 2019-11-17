package me.midest.hours168.web.controller;

import me.midest.hours168.web.util.Mappings;
import me.midest.hours168.web.util.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OthersController {

    @GetMapping(Mappings.ABOUT)
    public String about(){
        return ViewNames.ABOUT;
    }

}
