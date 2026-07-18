package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.entity.Hotel;
import de.lhind.internship.mini.project.entity.Room;
import de.lhind.internship.mini.project.entity.RoomStatus;
import de.lhind.internship.mini.project.exception.ResourceNotFoundException;
import de.lhind.internship.mini.project.repository.HotelRepository;
import de.lhind.internship.mini.project.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public Room addRoomToHotel(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public List<Room> getRoomsByHotel(Long hotelId) { return roomRepository.findByHotelId(hotelId); }
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room with id: " + id + " not found"));
    }
    public Room updateRoom(Long id, Room updated) {
        Room existing = getRoomById(id);
        existing.setRoomNumber(updated.getRoomNumber());
        existing.setRoomType(updated.getRoomType());
        existing.setCapacity(updated.getCapacity());
        existing.setPricePerNight(updated.getPricePerNight());
        existing.setStatus(updated.getStatus());
        return roomRepository.save(existing);
    }
    public Room updateRoomStatus(Long id, RoomStatus status) {
        Room existing = getRoomById(id);
        existing.setStatus(status);
        return roomRepository.save(existing);
    }
    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }
    public List<Room> searchRooms(Long hotelId, RoomStatus status) { return roomRepository.findByHotelIdAndStatus(hotelId, status); }
}
