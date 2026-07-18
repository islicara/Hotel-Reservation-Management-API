package de.lhind.internship.mini.project.controller;

import de.lhind.internship.mini.project.entity.Room;
import de.lhind.internship.mini.project.entity.RoomStatus;
import de.lhind.internship.mini.project.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) { this.roomService = roomService; }

    @PostMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<Room> create(@PathVariable Long hotelId, @Valid @RequestBody Room room) {
        return new ResponseEntity<>(roomService.addRoomToHotel(hotelId, room), HttpStatus.CREATED);
    }
    @GetMapping("/hotels/{hotelId}/rooms")
    public List<Room> getByHotel(@PathVariable Long hotelId) {
        return roomService.getRoomsByHotel(hotelId);
    }

    @GetMapping("/rooms/{id}")
    public Room getOne(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PutMapping("/rooms/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    @PatchMapping("/rooms/{id}/status")
    public Room patchStatus(@PathVariable Long id, @RequestParam RoomStatus status) {
        return roomService.updateRoomStatus(id, status);
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    @GetMapping("/rooms/search")
    public List<Room> search(@RequestParam Long hotelId, @RequestParam RoomStatus status) {
        return roomService.searchRooms(hotelId, status);
    }
}
