package com.divine.producerhub.service;

import com.divine.producerhub.model.Beat;
import com.divine.producerhub.repository.BeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return beatRepository.save(existingBeat);
    }

    public void deleteBeat(Long id) {
        Beat beat = getBeatById(id);
        beatRepository.delete(beat);
    }
}