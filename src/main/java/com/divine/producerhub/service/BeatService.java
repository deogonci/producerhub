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
}