package com.divine.producerhub.controller;

import com.divine.producerhub.service.LicenseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Controller
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/licenses")
    public String showLicenses(@RequestParam(defaultValue = "all") String payment, Model model) {
        model.addAttribute("licenses", licenseService.getLicenses(payment));
        model.addAttribute("payment", payment);
        return "licenses/list";
    }

    @GetMapping("/licenses/export")
    public ResponseEntity<byte[]> exportLicenses(@RequestParam(defaultValue = "all") String payment) {
        // A UTF-8 BOM helps spreadsheet apps on Windows display artist names correctly.
        byte[] file = ("\uFEFF" + licenseService.exportCsv(payment)).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"producerhub-licenses.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(file);
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
            @RequestParam BigDecimal price,
            Model model
    ) {
        try {
            licenseService.createLicense(beatId, artistId, licenseType, price);
            return "redirect:/licenses";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("beats", licenseService.getAllBeats());
            model.addAttribute("artists", licenseService.getAllArtists());
            model.addAttribute("beatId", beatId);
            model.addAttribute("artistId", artistId);
            model.addAttribute("licenseType", licenseType);
            model.addAttribute("price", price);
            return "licenses/form";
        }
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

    @GetMapping("/licenses/{id}/edit")
    public String showEditLicenseForm(@PathVariable Long id, Model model) {
        model.addAttribute("license", licenseService.getLicenseById(id));
        model.addAttribute("beats", licenseService.getAllBeats());
        model.addAttribute("artists", licenseService.getAllArtists());
        return "licenses/edit";
    }

    @PostMapping("/licenses/{id}/edit")
    public String updateLicense(
            @PathVariable Long id,
            @RequestParam Long beatId,
            @RequestParam Long artistId,
            @RequestParam String licenseType,
            @RequestParam BigDecimal price,
            Model model
    ) {
        try {
            licenseService.updateLicense(id, beatId, artistId, licenseType, price);
            return "redirect:/licenses";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("license", licenseService.getLicenseById(id));
            model.addAttribute("beats", licenseService.getAllBeats());
            model.addAttribute("artists", licenseService.getAllArtists());
            model.addAttribute("beatId", beatId);
            model.addAttribute("artistId", artistId);
            model.addAttribute("licenseType", licenseType);
            model.addAttribute("price", price);
            return "licenses/edit";
        }
    }
}
