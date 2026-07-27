package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.entity.Hotel;
import de.lhind.internship.mini.project.exception.ResourceNotFoundException;
import de.lhind.internship.mini.project.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel createHotel(Hotel hotel) { return hotelRepository.save(hotel); }
    public List<Hotel> getAllHotels() { return hotelRepository.findAll(); }
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with ID " + id + " not found"));
    }
    public Hotel updateHotel(Long id, Hotel updated) {
        Hotel existing = getHotelById(id);
        existing.setName(updated.getName());
        existing.setCity(updated.getCity());
        existing.setAddress(updated.getAddress());
        existing.setStarRating(updated.getStarRating());
        return hotelRepository.save(existing);
    }
    public void deleteHotel(Long id) {
        Hotel existing = getHotelById(id);
        hotelRepository.delete(existing);
    }
    public List<Hotel> searchHotelsByCity(String city) { return hotelRepository.findByCityIgnoreCase(city); }
}