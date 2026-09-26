package com.divine.producerhub.controller;

import com.divine.producerhub.service.LicenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@Controller
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/licenses")
    public String showLicenses(Model model) {
        model.addAttribute("licenses", licenseService.getAllLicenses());
        return "licenses/list";
    }

    @GetMapping("/licenses/new")
    public String showNewLicenseForm(Model model) {
        model.addAttribute("beats", licenseService.getAllBeats());
        model.addAttribute("artists", licenseService.getAllArtists());
        return "licenses/form";
    }

    @PostMapping("/licenses")
    public String createLicense(
            @RequestParam Long beatId,
            @RequestParam Long artistId,
            @RequestParam String licenseType,
            @RequestParam BigDecimal price
    ) {
        licenseService.createLicense(beatId, artistId, licenseType, price);
        return "redirect:/licenses";
    }

    @GetMapping("/licenses/{id}/delete")
    public String showDeleteLicensePage(@PathVariable Long id, Model model) {
        model.addAttribute("license", licenseService.getLicenseById(id));
        return "licenses/delete";
    }

    @PostMapping("/licenses/{id}/delete")
    public String deleteLicense(@PathVariable Long id) {
        licenseService.deleteLicense(id);
        return "redirect:/licenses";
    }

    @PostMapping("/licenses/{id}/paid")
    public String markPaid(@PathVariable Long id) {
        licenseService.setPaid(id, true);
        return "redirect:/licenses";
    }

    @PostMapping("/licenses/{id}/unpaid")
    public String markUnpaid(@PathVariable Long id) {
        licenseService.setPaid(id, false);
        return "redirect:/licenses";
    }
}