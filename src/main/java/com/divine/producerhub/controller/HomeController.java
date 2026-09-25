package com.divine.producerhub.controller;

import com.divine.producerhub.model.BeatStatus;
import com.divine.producerhub.service.BeatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BeatService beatService;

    public HomeController(BeatService beatService) {
        this.beatService = beatService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalBeats", beatService.countAllBeats());

        model.addAttribute(
                "availableBeats",
                beatService.countBeatsByStatus(BeatStatus.AVAILABLE)
        );

        model.addAttribute(
                "licensedBeats",
                beatService.countBeatsByStatus(BeatStatus.LICENSED)
        );

        model.addAttribute(
                "soldBeats",
                beatService.countBeatsByStatus(BeatStatus.SOLD)
        );

        return "index";
    }
}