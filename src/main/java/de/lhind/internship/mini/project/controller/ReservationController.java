package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.dto.ReservationRequestDTO;
import de.lhind.internship.mini.project.entity.Reservation;
import de.lhind.internship.mini.project.entity.ReservationStatus;
import de.lhind.internship.mini.project.service.ReservationService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private static final Logger log = LogManager.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@Valid @RequestBody ReservationRequestDTO dto) {
        log.trace("POST /api/reservations -> {}", dto);
        Reservation created = reservationService.createReservation(dto);
        log.info("Reservation created successfully — id={}", created.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);

        //return new ResponseEntity<>(reservationService.createReservation(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Reservation> listAll() {
        log.trace("GET /api/reservations");
        List<Reservation> reservations = reservationService.getAllReservations();
        log.info("Retrieved {} reservations", reservations.size());
        return reservations;

        //return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getOne(@PathVariable Long id) {
        log.trace("GET /api/reservations/{}", id);
        Reservation reservation = reservationService.getReservationById(id);
        log.info("Retrieved reservation id={}", id);
        return reservation;

        //return reservationService.getReservationById(id);
    }

    @PatchMapping("/{id}/status")
    public Reservation changeStatus(@PathVariable Long id, @RequestParam ReservationStatus status) {
        log.trace("PATCH /api/reservations/{}/status -> {}", id, status);
        Reservation updated = reservationService.updateStatus(id, status);
        log.info("Reservation status updated — id={}, newStatus={}", id, status);
        return updated;

        //return reservationService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        log.trace("DELETE /api/reservations/{}", id);
        reservationService.cancelReservation(id);
        log.warn("Reservation cancelled — id={}", id);

        //reservationService.cancelReservation(id);
    }

    @GetMapping("/guest/{guestId}")
    public List<Reservation> getByGuest(@PathVariable Long guestId) {
        log.trace("GET /api/reservations/guest/{}", guestId);
        List<Reservation> reservations = reservationService.getReservationsByGuest(guestId);
        log.info("Retrieved {} reservations for guestId={}", reservations.size(), guestId);
        return reservations;

        //return reservationService.getReservationsByGuest(guestId);
    }
}