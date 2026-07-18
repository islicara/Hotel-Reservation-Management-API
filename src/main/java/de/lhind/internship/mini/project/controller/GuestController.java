package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.entity.Guest;
import de.lhind.internship.mini.project.entity.GuestProfile;
import de.lhind.internship.mini.project.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    public ResponseEntity<Guest> create(@Valid @RequestBody Guest guest) {
        return new ResponseEntity<>(guestService.createGuest(guest), HttpStatus.CREATED);
    }
    @GetMapping
    public List<Guest> getAll() {
        return guestService.getAllGuests();
    }

    @GetMapping("/{id}")
    public Guest getOne(@PathVariable Long id) {
        return guestService.getGuestById(id);
    }

    @PutMapping("/{id}")
    public Guest update(@PathVariable Long id, @Valid @RequestBody Guest guest) {
        return guestService.updateGuest(id, guest);
    }

    @PostMapping("/{guestId}/profile")
    public ResponseEntity<GuestProfile> createProfile(@PathVariable Long guestId, @RequestBody GuestProfile profile) {
        return new ResponseEntity<>(guestService.createProfile(guestId, profile), HttpStatus.CREATED);
    }
    @GetMapping("/{guestId}/profile")
    public GuestProfile getProfile(@PathVariable Long guestId) {
        return guestService.getProfileByGuestId(guestId);
    }
}
