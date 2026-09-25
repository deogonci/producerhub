package com.divine.producerhub.controller;

import com.divine.producerhub.model.Beat;
import com.divine.producerhub.model.BeatStatus;
import com.divine.producerhub.service.BeatService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BeatController {

    private final BeatService beatService;

    public BeatController(BeatService beatService) {
        this.beatService = beatService;
    }

    @GetMapping("/beats")
    public String showBeats(Model model) {
        model.addAttribute("beats", beatService.getAllBeats());

        return "beats/list";
    }

    @GetMapping("/beats/new")
    public String showAddBeatForm(Model model) {
        Beat beat = new Beat();
        beat.setStatus(BeatStatus.AVAILABLE);

        model.addAttribute("beat", beat);
        model.addAttribute("statuses", BeatStatus.values());

        return "beats/form";
    }

    @PostMapping("/beats")
    public String saveBeat(
            @Valid @ModelAttribute("beat") Beat beat,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", BeatStatus.values());

            return "beats/form";
        }

        beatService.saveBeat(beat);

        return "redirect:/beats";
    }
}