package com.divine.producerhub.repository;

import com.divine.producerhub.model.Beat;
import com.divine.producerhub.model.BeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeatRepository extends JpaRepository<Beat, Long> {
    long countByStatus(BeatStatus status);
}
