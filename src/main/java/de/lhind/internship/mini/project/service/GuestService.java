package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.entity.Guest;
import de.lhind.internship.mini.project.entity.GuestProfile;
import de.lhind.internship.mini.project.exception.ResourceNotFoundException;
import de.lhind.internship.mini.project.repository.GuestProfileRepository;
import de.lhind.internship.mini.project.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestProfileRepository profileRepository;

    public GuestService(GuestRepository guestRepository, GuestProfileRepository profileRepository) {
        this.guestRepository = guestRepository;
        this.profileRepository = profileRepository;
    }

    public Guest createGuest(Guest guest) { return guestRepository.save(guest); }
    public List<Guest> getAllGuests() { return guestRepository.findAll(); }
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Guest with ID " + id + " not found"));
    }
    public Guest updateGuest(Long id, Guest updated) {
        Guest existing = getGuestById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPhoneNumber(updated.getPhoneNumber());
        return guestRepository.save(existing);
    }
    public GuestProfile createProfile(Long guestId, GuestProfile profile) {
        Guest guest = getGuestById(guestId);
        profile.setGuest(guest);
        return profileRepository.save(profile);
    }
    public GuestProfile getProfileByGuestId(Long guestId) {
        return profileRepository.findByGuestId(guestId).orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
}
