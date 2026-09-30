package com.divine.producerhub.service;

import com.divine.producerhub.model.Artist;
import com.divine.producerhub.model.Beat;
import com.divine.producerhub.model.License;
import com.divine.producerhub.repository.ArtistRepository;
import com.divine.producerhub.repository.BeatRepository;
import com.divine.producerhub.repository.LicenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final BeatRepository beatRepository;
    private final ArtistRepository artistRepository;

    public LicenseService(
            LicenseRepository licenseRepository,
            BeatRepository beatRepository,
            ArtistRepository artistRepository
    ) {
        this.licenseRepository = licenseRepository;
        this.beatRepository = beatRepository;
        this.artistRepository = artistRepository;
    }

    public List<License> getAllLicenses() {
        return licenseRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<License> getLicenses(String payment) {
        return getAllLicenses().stream()
                .filter(license -> switch (payment) {
                    case "paid" -> Boolean.TRUE.equals(license.getPaid());
                    case "unpaid" -> !Boolean.TRUE.equals(license.getPaid());
                    default -> true;
                })
                .toList();
    }

    public List<Beat> getAllBeats() {
        return beatRepository.findAll();
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public void createLicense(
            Long beatId,
            Long artistId,
            String licenseType,
            BigDecimal price
    ) {
        validateLicense(licenseType, price);

        Beat beat = beatRepository.findById(beatId)
                .orElseThrow(() -> new IllegalArgumentException("Beat not found: " + beatId));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + artistId));

        License license = new License();
        license.setBeat(beat);
        license.setArtist(artist);
        license.setLicenseType(licenseType);
        license.setPrice(price);
        license.setLicensedAt(LocalDate.now());

        licenseRepository.save(license);
    }

    public License getLicenseById(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Licence not found: " + id));
    }

    public void deleteLicense(Long id) {
        licenseRepository.deleteById(id);
    }

    public boolean hasLicensesForArtist(Long artistId) {
        return licenseRepository.existsByArtist_Id(artistId);
    }

    public boolean hasLicensesForBeat(Long beatId) {
        return licenseRepository.existsByBeat_Id(beatId);
    }

    public long countLicenses() {
        return licenseRepository.count();
    }

    public BigDecimal getTotalRevenue() {
        return licenseRepository.findAll().stream()
                .map(License::getPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setPaid(Long id, boolean paid) {
        License license = getLicenseById(id);
        license.setPaid(paid);
        licenseRepository.save(license);
    }

    public BigDecimal getReceivedRevenue() {
        return licenseRepository.findAll().stream()
                .filter(license -> Boolean.TRUE.equals(license.getPaid()))
                .map(License::getPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getOutstandingRevenue() {
        return licenseRepository.findAll().stream()
                .filter(license -> !Boolean.TRUE.equals(license.getPaid()))
                .map(License::getPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String exportCsv(String payment) {
        StringBuilder csv = new StringBuilder("Beat,Artist,Licence type,Price (EUR),Payment,Date\r\n");

        for (License license : getLicenses(payment)) {
            csv.append(String.join(",",
                    csvCell(license.getBeat().getTitle()),
                    csvCell(license.getArtist().getName()),
                    csvCell(license.getLicenseType()),
                    csvCell(license.getPrice() == null ? "" : license.getPrice().toPlainString()),
                    csvCell(Boolean.TRUE.equals(license.getPaid()) ? "Paid" : "Unpaid"),
                    csvCell(license.getLicensedAt() == null ? "" : license.getLicensedAt().toString())
            )).append("\r\n");
        }

        return csv.toString();
    }

    private String csvCell(String value) {
        String safe = value == null ? "" : value.replace("\r", " ").replace("\n", " ");
        String trimmed = safe.stripLeading();

        // Spreadsheet programs can evaluate cells beginning with these characters as formulas.
        if (!trimmed.isEmpty() && "=+-@".indexOf(trimmed.charAt(0)) >= 0) {
            safe = "'" + safe;
        }

        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }

    public void updateLicense(
            Long id,
            Long beatId,
            Long artistId,
            String licenseType,
            BigDecimal price
    ) {
        validateLicense(licenseType, price);

        License license = getLicenseById(id);

        Beat beat = beatRepository.findById(beatId)
                .orElseThrow(() -> new IllegalArgumentException("Beat not found: " + beatId));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + artistId));

        license.setBeat(beat);
        license.setArtist(artist);
        license.setLicenseType(licenseType);
        license.setPrice(price);

        licenseRepository.save(license);
    }

    private void validateLicense(String licenseType, BigDecimal price) {
        if (licenseType == null || licenseType.isBlank()) {
            throw new IllegalArgumentException("Enter a licence type.");
        }

        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Price must be zero or more.");
        }

        if (price.scale() > 2 || price.compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("Enter a price up to €99,999,999.99 with no more than two decimal places.");
        }
    }
}
