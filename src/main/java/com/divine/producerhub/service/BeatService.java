package com.divine.producerhub.service;

import com.divine.producerhub.model.Beat;
import com.divine.producerhub.model.BeatStatus;
import com.divine.producerhub.repository.BeatRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class BeatService {

    private final BeatRepository beatRepository;

    public BeatService(BeatRepository beatRepository) {
        this.beatRepository = beatRepository;
    }

    public List<Beat> getAllBeats() {
        return beatRepository.findAll();
    }

    public Beat saveBeat(Beat beat) {
        return beatRepository.save(beat);
    }

    public Beat getBeatById(Long id) {
        return beatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Beat not found with ID: " + id
                ));
    }

    public Beat updateBeat(Long id, Beat updatedBeat) {
        Beat existingBeat = getBeatById(id);

        existingBeat.setTitle(updatedBeat.getTitle());
        existingBeat.setBpm(updatedBeat.getBpm());
        existingBeat.setMusicalKey(updatedBeat.getMusicalKey());
        existingBeat.setGenre(updatedBeat.getGenre());
        existingBeat.setStatus(updatedBeat.getStatus());

        if (updatedBeat.getAudioFilename() != null) {
            existingBeat.setAudioFilename(updatedBeat.getAudioFilename());
        }

        return beatRepository.save(existingBeat);
    }

    public void deleteBeat(Long id) {
        Beat beat = getBeatById(id);
        beatRepository.delete(beat);
    }

    public List<Beat> searchBeats(String search, BeatStatus status) {
        String query = search == null ? "" : search.strip().toLowerCase(Locale.ROOT);

        return beatRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(beat -> status == null || beat.getStatus() == status)
                .filter(beat -> query.isEmpty()
                        || containsIgnoreCase(beat.getTitle(), query)
                        || containsIgnoreCase(beat.getGenre(), query))
                .toList();
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    public long countAllBeats() {
        return beatRepository.count();
    }

    public long countBeatsByStatus(BeatStatus status) {
        return beatRepository.countByStatus(status);
    }
}
