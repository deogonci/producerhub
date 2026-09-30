package com.divine.producerhub.repository;

import com.divine.producerhub.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
    boolean existsByArtist_Id(Long artistId);

    boolean existsByBeat_Id(Long beatId);
}
