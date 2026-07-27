package de.lhind.internship.mini.project.repository;

import de.lhind.internship.mini.project.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    boolean existsByEmail(String email);
}