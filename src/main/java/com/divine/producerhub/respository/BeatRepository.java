package com.divine.producerhub.repository;

import com.divine.producerhub.model.Beat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeatRepository extends JpaRepository<Beat, Long> {
}