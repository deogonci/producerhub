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
        return licenseRepository.findAll();
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
}