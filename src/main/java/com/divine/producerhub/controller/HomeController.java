package com.divine.producerhub.controller;

import com.divine.producerhub.model.BeatStatus;
import com.divine.producerhub.service.BeatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.divine.producerhub.service.ArtistService;
import com.divine.producerhub.service.LicenseService;

@Controller
public class HomeController {

    private final BeatService beatService;
    private final ArtistService artistService;
    private final LicenseService licenseService;

    public HomeController(
            BeatService beatService,
            ArtistService artistService,
            LicenseService licenseService
    ) {
        this.beatService = beatService;
        this.artistService = artistService;
        this.licenseService = licenseService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalBeats", beatService.countAllBeats());
        model.addAttribute("artistCount", artistService.countArtists());
        model.addAttribute("licenseCount", licenseService.countLicenses());
        model.addAttribute("totalRevenue", licenseService.getTotalRevenue());
        model.addAttribute("receivedRevenue", licenseService.getReceivedRevenue());

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