package com.divine.producerhub.service;

import com.divine.producerhub.model.Artist;
import com.divine.producerhub.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }

    public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found: " + id));
    }

    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }
}