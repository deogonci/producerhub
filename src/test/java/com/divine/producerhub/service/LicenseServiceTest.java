package com.divine.producerhub.service;

import com.divine.producerhub.model.Artist;
import com.divine.producerhub.model.Beat;
import com.divine.producerhub.model.BeatStatus;
import com.divine.producerhub.model.License;
import com.divine.producerhub.repository.ArtistRepository;
import com.divine.producerhub.repository.BeatRepository;
import com.divine.producerhub.repository.LicenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LicenseServiceTest {

    @Test
    void outstandingRevenueIncludesOnlyUnpaidPrices() {
        LicenseRepository repository = mock(LicenseRepository.class);
        LicenseService service = new LicenseService(repository, mock(BeatRepository.class), mock(ArtistRepository.class));
        License unpaid = license("Beat", "Artist", new BigDecimal("350.00"), false);
        License paid = license("Beat", "Artist", new BigDecimal("150.00"), true);
        when(repository.findAll()).thenReturn(List.of(unpaid, paid));

        assertEquals(new BigDecimal("350.00"), service.getOutstandingRevenue());
    }

    @Test
    void csvExportRespectsPaymentFilterAndEscapesSpreadsheetCells() {
        LicenseRepository repository = mock(LicenseRepository.class);
        LicenseService service = new LicenseService(repository, mock(BeatRepository.class), mock(ArtistRepository.class));
        License unpaid = license("Night, \"Drive\"", "=HYPERLINK()", new BigDecimal("350.00"), false);
        License paid = license("Other", "Artist", new BigDecimal("150.00"), true);
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(unpaid, paid));

        String csv = service.exportCsv("unpaid");

        assertTrue(csv.startsWith("Beat,Artist,Licence type,Price (EUR),Payment,Date\r\n"));
        assertTrue(csv.contains("\"Night, \"\"Drive\"\"\""));
        assertTrue(csv.contains("\"'=HYPERLINK()\""));
        assertTrue(csv.contains("\"350.00\",\"Unpaid\""));
        assertFalse(csv.contains("\"150.00\",\"Paid\""));
    }

    private License license(String title, String artistName, BigDecimal price, boolean paid) {
        Beat beat = new Beat(title, 120, "C Minor", "Trap", BeatStatus.AVAILABLE);
        Artist artist = new Artist();
        artist.setName(artistName);
        License license = new License();
        license.setBeat(beat);
        license.setArtist(artist);
        license.setLicenseType("Standard");
        license.setPrice(price);
        license.setPaid(paid);
        license.setLicensedAt(LocalDate.of(2026, 9, 30));
        return license;
    }
}
