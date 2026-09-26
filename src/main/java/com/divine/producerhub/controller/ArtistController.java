package com.divine.producerhub.controller;

import com.divine.producerhub.model.Artist;
import com.divine.producerhub.service.ArtistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/artists")
    public String showArtists(Model model) {
        model.addAttribute("artists", artistService.getAllArtists());
        return "artists/list";
    }

    @GetMapping("/artists/new")
    public String showNewArtistForm(Model model) {
        model.addAttribute("artist", new Artist());
        return "artists/form";
    }

    @PostMapping("/artists")
    public String saveArtist(@ModelAttribute Artist artist) {
        artistService.saveArtist(artist);
        return "redirect:/artists";
    }

    @GetMapping("/artists/{id}/edit")
    public String showEditArtistForm(@PathVariable Long id, Model model) {
        model.addAttribute("artist", artistService.getArtistById(id));
        return "artists/form";
    }

    @GetMapping("/artists/{id}/delete")
    public String showDeleteArtistPage(@PathVariable Long id, Model model) {
        model.addAttribute("artist", artistService.getArtistById(id));
        return "artists/delete";
    }

    @PostMapping("/artists/{id}/delete")
    public String deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return "redirect:/artists";
    }
}