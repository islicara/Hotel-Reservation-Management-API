package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.dto.ReservationRequestDTO;
import de.lhind.internship.mini.project.entity.Reservation;
import de.lhind.internship.mini.project.entity.ReservationStatus;
import de.lhind.internship.mini.project.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@Valid @RequestBody ReservationRequestDTO dto) {
        return new ResponseEntity<>(reservationService.createReservation(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Reservation> listAll() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getOne(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @PatchMapping("/{id}/status")
    public Reservation changeStatus(@PathVariable Long id, @RequestParam ReservationStatus status) {
        return reservationService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping("/guest/{guestId}")
    public List<Reservation> getByGuest(@PathVariable Long guestId) {
        return reservationService.getReservationsByGuest(guestId);
    }
}