package com.divine.producerhub.service;

import com.divine.producerhub.model.Beat;
import com.divine.producerhub.model.BeatStatus;
import com.divine.producerhub.repository.BeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BeatServiceTest {

    @Test
    void searchCombinesTextAndStatusAndIgnoresCase() {
        BeatRepository repository = mock(BeatRepository.class);
        BeatService service = new BeatService(repository);
        Beat availableTrap = new Beat("Midnight Drive", 140, "C Minor", "Trap", BeatStatus.AVAILABLE);
        Beat soldTrap = new Beat("Another", 120, "A Minor", "Trap", BeatStatus.SOLD);
        Beat availableRnb = new Beat("Morning", 90, "G Major", "R&B", BeatStatus.AVAILABLE);
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(availableTrap, soldTrap, availableRnb));

        assertEquals(List.of(availableTrap), service.searchBeats(" tRaP ", BeatStatus.AVAILABLE));
        assertEquals(List.of(availableTrap), service.searchBeats("MIDNIGHT", BeatStatus.AVAILABLE));
        assertEquals(List.of(availableTrap, availableRnb), service.searchBeats("", BeatStatus.AVAILABLE));
    }
}
