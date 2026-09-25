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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BeatController {

    private final BeatService beatService;

    public BeatController(BeatService beatService) {
        this.beatService = beatService;
    }

    @GetMapping("/beats")
    public String showBeats(
            @RequestParam(required = false) String search,
            Model model
    ) {
        model.addAttribute("beats", beatService.searchBeats(search));
        model.addAttribute("search", search);

        return "beats/list";
    }

    @GetMapping("/beats/new")
    public String showAddBeatForm(Model model) {
        Beat beat = new Beat();
        beat.setStatus(BeatStatus.AVAILABLE);

        model.addAttribute("beat", beat);
        model.addAttribute("statuses", BeatStatus.values());
        model.addAttribute("pageTitle", "Add Beat");
        model.addAttribute("formAction", "/beats");

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

    @GetMapping("/beats/{id}/edit")
    public String showEditBeatForm(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("beat", beatService.getBeatById(id));
        model.addAttribute("statuses", BeatStatus.values());
        model.addAttribute("pageTitle", "Edit Beat");
        model.addAttribute("formAction", "/beats/" + id);

        return "beats/form";
    }

    @PostMapping("/beats/{id}")
    public String updateBeat(
            @PathVariable Long id,
            @Valid @ModelAttribute("beat") Beat beat,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", BeatStatus.values());
            model.addAttribute("pageTitle", "Edit Beat");
            model.addAttribute("formAction", "/beats/" + id);

            return "beats/form";
        }

        beatService.updateBeat(id, beat);

        return "redirect:/beats";
    }

    @GetMapping("/beats/{id}/delete")
    public String showDeleteConfirmation(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("beat", beatService.getBeatById(id));

        return "beats/delete";
    }

    @PostMapping("/beats/{id}/delete")
    public String deleteBeat(@PathVariable Long id) {
        beatService.deleteBeat(id);

        return "redirect:/beats";
    }
}