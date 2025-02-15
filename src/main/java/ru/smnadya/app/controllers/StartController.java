package ru.smnadya.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static ru.smnadya.app.controllers.api.BaseControllerApiPath.BCAP_HOME;

@Controller
@RequestMapping(BCAP_HOME)
public class StartController {

    @GetMapping()
    public String hello() {
        return "main";
    }

}
