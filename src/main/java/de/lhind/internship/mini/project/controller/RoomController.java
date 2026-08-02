package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.entity.Room;
import de.lhind.internship.mini.project.entity.RoomStatus;
import de.lhind.internship.mini.project.service.RoomService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController {

    private static final Logger log = LogManager.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) { this.roomService = roomService; }

    @PostMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<Room> create(@PathVariable Long hotelId, @Valid @RequestBody Room room) {
        log.trace("POST /api/hotels/{}/rooms -> {}", hotelId, room);
        Room created = roomService.addRoomToHotel(hotelId, room);
        log.info("Room created successfully — id={}, hotelId={}", created.getId(), hotelId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);

        // return new ResponseEntity<>(roomService.addRoomToHotel(hotelId, room), HttpStatus.CREATED);
    }
    @GetMapping("/hotels/{hotelId}/rooms")
    public List<Room> getByHotel(@PathVariable Long hotelId) {
        log.trace("GET /api/hotels/{}/rooms", hotelId);
        List<Room> rooms = roomService.getRoomsByHotel(hotelId);
        log.info("Retrieved {} rooms for hotelId={}", rooms.size(), hotelId);
        return rooms;

        //return roomService.getRoomsByHotel(hotelId);
    }

    @GetMapping("/rooms/{id}")
    public Room getOne(@PathVariable Long id) {
        log.trace("GET /api/rooms/{}", id);
        Room room = roomService.getRoomById(id);
        log.info("Retrieved room id={}", id);
        return room;

        //return roomService.getRoomById(id);
    }

    @PutMapping("/rooms/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody Room room) {
        log.trace("PUT /api/rooms/{} -> {}", id, room);
        Room updated = roomService.updateRoom(id, room);
        log.info("Room updated successfully — id={}", id);
        return updated;

        //return roomService.updateRoom(id, room);
    }

    @PatchMapping("/rooms/{id}/status")
    public Room patchStatus(@PathVariable Long id, @RequestParam RoomStatus status) {
        log.trace("PATCH /api/rooms/{}/status -> {}", id, status);
        Room updated = roomService.updateRoomStatus(id, status);
        log.info("Room status updated — id={}, newStatus={}", id, status);
        return updated;

        //return roomService.updateRoomStatus(id, status);
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.trace("DELETE /api/rooms/{}", id);
        roomService.deleteRoom(id);
        log.warn("Room deleted — id={}", id);

        //roomService.deleteRoom(id);
    }

    @GetMapping("/rooms/search")
    public List<Room> search(@RequestParam Long hotelId, @RequestParam RoomStatus status) {
        log.trace("GET /api/rooms/search -> hotelId={}, status={}", hotelId, status);
        List<Room> results = roomService.searchRooms(hotelId, status);
        log.info("Search returned {} rooms — hotelId={}, status={}", results.size(), hotelId, status);
        return results;

        //return roomService.searchRooms(hotelId, status);
    }
}
