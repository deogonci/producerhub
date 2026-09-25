package com.divine.producerhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "beats")
public class Beat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Min(30)
    @Max(300)
    private int bpm;

    @NotBlank
    private String musicalKey;

    @NotBlank
    private String genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BeatStatus status;

    public Beat() {
    }

    public Beat(
            String title,
            int bpm,
            String musicalKey,
            String genre,
            BeatStatus status
    ) {
        this.title = title;
        this.bpm = bpm;
        this.musicalKey = musicalKey;
        this.genre = genre;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getMusicalKey() {
        return musicalKey;
    }

    public void setMusicalKey(String musicalKey) {
        this.musicalKey = musicalKey;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BeatStatus getStatus() {
        return status;
    }

    public void setStatus(BeatStatus status) {
        this.status = status;
    }
}