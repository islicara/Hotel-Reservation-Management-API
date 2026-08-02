package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.entity.Guest;
import de.lhind.internship.mini.project.entity.GuestProfile;
import de.lhind.internship.mini.project.service.GuestService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private static final Logger log = LogManager.getLogger(GuestController.class);

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    public ResponseEntity<Guest> create(@Valid @RequestBody Guest guest) {
        log.trace("POST /api/guests -> {}", guest);
        Guest created = guestService.createGuest(guest);
        log.info("Guest created successfully — id={}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);

        //return new ResponseEntity<>(guestService.createGuest(guest), HttpStatus.CREATED);
    }
    @GetMapping
    public List<Guest> getAll() {
        log.trace("GET /api/guests");
        List<Guest> guests = guestService.getAllGuests();
        log.info("Retrieved {} guests", guests.size());
        return guests;

        //return guestService.getAllGuests();
    }

    @GetMapping("/{id}")
    public Guest getOne(@PathVariable Long id) {
        log.trace("GET /api/guests/{}", id);
        Guest guest = guestService.getGuestById(id);
        log.info("Retrieved guest id={}", id);
        return guest;

        //return guestService.getGuestById(id);
    }

    @PutMapping("/{id}")
    public Guest update(@PathVariable Long id, @Valid @RequestBody Guest guest) {
        log.trace("PUT /api/guests/{} -> {}", id, guest);
        Guest updated = guestService.updateGuest(id, guest);
        log.info("Guest updated successfully — id={}", id);
        return updated;

        //return guestService.updateGuest(id, guest);
    }

    @PostMapping("/{guestId}/profile")
    public ResponseEntity<GuestProfile> createProfile(@PathVariable Long guestId, @RequestBody GuestProfile profile) {
        log.trace("POST /api/guests/{}/profile -> {}", guestId, profile);
        GuestProfile created = guestService.createProfile(guestId, profile);
        log.info("Guest profile created — guestId={}", guestId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);

        //return new ResponseEntity<>(guestService.createProfile(guestId, profile), HttpStatus.CREATED);
    }
    @GetMapping("/{guestId}/profile")
    public GuestProfile getProfile(@PathVariable Long guestId) {
        log.trace("GET /api/guests/{}/profile", guestId);
        GuestProfile profile = guestService.getProfileByGuestId(guestId);
        log.info("Retrieved guest profile — guestId={}", guestId);
        return profile;

        //return guestService.getProfileByGuestId(guestId);
    }
}
