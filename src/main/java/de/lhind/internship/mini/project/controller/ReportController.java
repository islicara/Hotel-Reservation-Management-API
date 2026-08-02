package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.service.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger log = LogManager.getLogger(ReportController.class);

    private final ReservationService reservationService;

    public ReportController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/most-reserved-rooms")
    public List<Map<String, Object>> getMostReservedRooms() {
        log.trace("GET /api/reports/most-reserved-rooms");
        List<Map<String, Object>> report = reservationService.getMostReservedRoomsReport();
        log.info("Generated most-reserved-rooms report — {} entries", report.size());
        return report;

        //return reservationService.getMostReservedRoomsReport();
    }
}