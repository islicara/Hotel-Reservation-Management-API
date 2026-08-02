package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.entity.Guest;
import de.lhind.internship.mini.project.entity.GuestProfile;
import de.lhind.internship.mini.project.exception.ResourceNotFoundException;
import de.lhind.internship.mini.project.repository.GuestProfileRepository;
import de.lhind.internship.mini.project.repository.GuestRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private static final Logger log = LogManager.getLogger(GuestService.class);

    private final GuestRepository guestRepository;
    private final GuestProfileRepository profileRepository;

    public GuestService(GuestRepository guestRepository, GuestProfileRepository profileRepository) {
        this.guestRepository = guestRepository;
        this.profileRepository = profileRepository;
    }

    public Guest createGuest(Guest guest) {
        log.trace("Entering createGuest() — {}", guest);
        Guest saved = guestRepository.save(guest);
        log.debug("Guest persisted — id={}", saved.getId());
        return saved;

        //return guestRepository.save(guest);
    }

    public List<Guest> getAllGuests() {
        log.trace("Entering getAllGuests()");
        return guestRepository.findAll();
    }

    public Guest getGuestById(Long id) {
        log.trace("Entering getGuestById() — id={}", id);
        return guestRepository.findById(id)
                .orElseThrow(() -> {log.error("Guest not found — id={}", id);
                    return new ResourceNotFoundException("Guest with ID " + id + " not found");
                });

        //return guestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Guest with ID " + id + " not found"));
    }

    public Guest updateGuest(Long id, Guest updated) {
        log.trace("Entering updateGuest() — id={}", id);
        Guest existing = getGuestById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPhoneNumber(updated.getPhoneNumber());
        Guest saved = guestRepository.save(existing);
        log.debug("Guest updated — id={}", saved.getId());
        return saved;

        //return guestRepository.save(existing);
    }

    public GuestProfile createProfile(Long guestId, GuestProfile profile) {
        log.trace("Entering createProfile() — guestId={}", guestId);
        Guest guest = getGuestById(guestId);
        profile.setGuest(guest);
        GuestProfile saved = profileRepository.save(profile);
        log.debug("Guest profile persisted — guestId={}", guestId);
        return saved;

        //return profileRepository.save(profile);
    }

    public GuestProfile getProfileByGuestId(Long guestId) {
        log.trace("Entering getProfileByGuestId() — guestId={}", guestId);
        return profileRepository.findByGuestId(guestId)
                .orElseThrow(() -> {log.error("Guest profile not found — guestId={}", guestId);
                    return new ResourceNotFoundException("Profile not found");
                });

        //return profileRepository.findByGuestId(guestId).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
}
