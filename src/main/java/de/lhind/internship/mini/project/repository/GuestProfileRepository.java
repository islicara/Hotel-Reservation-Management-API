package de.lhind.internship.mini.project.repository;

import de.lhind.internship.mini.project.entity.GuestProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestProfileRepository extends JpaRepository<GuestProfile, Long> {
    Optional<GuestProfile> findByGuestId(Long guestId);
}
