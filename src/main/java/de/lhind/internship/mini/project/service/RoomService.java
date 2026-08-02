package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.entity.Hotel;
import de.lhind.internship.mini.project.entity.Room;
import de.lhind.internship.mini.project.entity.RoomStatus;
import de.lhind.internship.mini.project.exception.ResourceNotFoundException;
import de.lhind.internship.mini.project.repository.HotelRepository;
import de.lhind.internship.mini.project.repository.RoomRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private static final Logger log = LogManager.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public Room addRoomToHotel(Long hotelId, Room room) {
        log.trace("Entering addRoomToHotel() — hotelId={}, room={}", hotelId, room);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> {
            log.error("Hotel not found — hotelId={}", hotelId);
            return new ResourceNotFoundException("Hotel not found");
        });
        room.setHotel(hotel);
        Room saved = roomRepository.save(room);
        log.debug("Room persisted — id={}, hotelId={}", saved.getId(), hotelId);
        return saved;

        //return roomRepository.save(room);
    }

    public List<Room> getRoomsByHotel(Long hotelId) {
        log.trace("Entering getRoomsByHotel() — hotelId={}", hotelId);
        return roomRepository.findByHotelId(hotelId);
    }

    public Room getRoomById(Long id) {
        log.trace("Entering getRoomById() — id={}", id);
        return roomRepository.findById(id).orElseThrow(() -> {
            log.trace("Entering getRoomById() — id={}", id);
            return new ResourceNotFoundException("Room with id: " + id + " not found");
        });
    }

    public Room updateRoom(Long id, Room updated) {
        log.trace("Entering updateRoom() — id={}", id);
        Room existing = getRoomById(id);
        existing.setRoomNumber(updated.getRoomNumber());
        existing.setRoomType(updated.getRoomType());
        existing.setCapacity(updated.getCapacity());
        existing.setPricePerNight(updated.getPricePerNight());
        existing.setStatus(updated.getStatus());
        Room saved = roomRepository.save(existing);
        log.debug("Room updated — id={}", saved.getId());
        return saved;

        //return roomRepository.save(existing);
    }

    public Room updateRoomStatus(Long id, RoomStatus status) {
        log.trace("Entering updateRoomStatus() — id={}, newStatus={}", id, status);
        Room existing = getRoomById(id);
        existing.setStatus(status);
        Room saved = roomRepository.save(existing);
        log.info("Room status updated — id={}, status={}", id, status);
        return saved;

        //return roomRepository.save(existing);
    }

    public void deleteRoom(Long id) {
        log.trace("Entering deleteRoom() — id={}", id);
        Room room = getRoomById(id);
        roomRepository.delete(room);
        log.warn("Room deleted — id={}", id);
    }

    public List<Room> searchRooms(Long hotelId, RoomStatus status) {
        log.trace("Entering searchRooms() — hotelId={}, status={}", hotelId, status);
        List<Room> results = roomRepository.findByHotelIdAndStatus(hotelId, status);
        log.debug("Search returned {} rooms — hotelId={}, status={}", results.size(), hotelId, status);
        return results;

        //return roomRepository.findByHotelIdAndStatus(hotelId, status);
    }
}
