package com.divine.producerhub.repository;

import com.divine.producerhub.model.Beat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeatRepository extends JpaRepository<Beat, Long> {

    List<Beat> findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String title,
            String genre
    );
}