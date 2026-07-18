package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReservationService reservationService;

    public ReportController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/most-reserved-rooms")
    public List<Map<String, Object>> getMostReservedRooms() {
        return reservationService.getMostReservedRoomsReport();
    }
}