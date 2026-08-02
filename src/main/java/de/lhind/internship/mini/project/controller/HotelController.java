package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.entity.Hotel;
import de.lhind.internship.mini.project.service.HotelService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private static final Logger log = LogManager.getLogger(HotelController.class);

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        log.trace("POST /api/hotels -> {}", hotel);
        Hotel created = hotelService.createHotel(hotel);
        log.info("Hotel created successfully — id={}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

        //Hotel created = hotelService.createHotel(hotel);
        //return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        log.trace("GET /api/hotels");
        List<Hotel> hotels = hotelService.getAllHotels();
        log.info("Retrieved {} hotels", hotels.size());
        return ResponseEntity.ok(hotels);

        //return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        log.trace("GET /api/hotels/{}", id);
        Hotel hotel = hotelService.getHotelById(id);
        log.info("Retrieved hotel id={}", id);
        return ResponseEntity.ok(hotel);

        //return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @Valid @RequestBody Hotel hotel) {
        log.trace("PUT /api/hotels/{} -> {}", id, hotel);
        Hotel updated = hotelService.updateHotel(id, hotel);
        log.info("Hotel updated successfully — id={}", id);
        return ResponseEntity.ok(updated);

        //return ResponseEntity.ok(hotelService.updateHotel(id, hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        log.trace("DELETE /api/hotels/{}", id);
        hotelService.deleteHotel(id);
        log.warn("Hotel deleted — id={}", id);
        return ResponseEntity.noContent().build();

        //hotelService.deleteHotel(id);
        //return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchByCity(@RequestParam String city) {
        log.trace("GET /api/hotels/search -> city={}", city);
        List<Hotel> results = hotelService.searchHotelsByCity(city);
        log.info("Search returned {} hotels — city={}", results.size(), city);
        return ResponseEntity.ok(results);

        //return ResponseEntity.ok(hotelService.searchHotelsByCity(city));
    }
}