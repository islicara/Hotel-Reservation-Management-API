package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.entity.Hotel;
import de.lhind.internship.mini.project.exception.ResourceNotFoundException;
import de.lhind.internship.mini.project.repository.HotelRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private static final Logger log = LogManager.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel createHotel(Hotel hotel) {
        log.trace("Entering createHotel() — {}", hotel);
        Hotel saved = hotelRepository.save(hotel);
        log.debug("Hotel persisted — id={}", saved.getId());
        return saved;

        //return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        log.trace("Entering getAllHotels()");
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        log.trace("Entering getHotelById() — id={}", id);
        return hotelRepository.findById(id)
                .orElseThrow(() -> {log.error("Hotel not found — id={}", id);
                    return new ResourceNotFoundException("Hotel with ID " + id + " not found");
                });

        //return hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel with ID " + id + " not found"));
    }

    public Hotel updateHotel(Long id, Hotel updated) {
        log.trace("Entering updateHotel() — id={}", id);
        Hotel existing = getHotelById(id);
        existing.setName(updated.getName());
        existing.setCity(updated.getCity());
        existing.setAddress(updated.getAddress());
        existing.setStarRating(updated.getStarRating());
        Hotel saved = hotelRepository.save(existing);
        log.debug("Hotel updated — id={}", saved.getId());
        return saved;

        //return hotelRepository.save(existing);
    }

    public void deleteHotel(Long id) {
        log.trace("Entering deleteHotel() — id={}", id);
        Hotel existing = getHotelById(id);
        hotelRepository.delete(existing);
        log.warn("Hotel deleted — id={}", id);
    }

    public List<Hotel> searchHotelsByCity(String city) {
        List<Hotel> results = hotelRepository.findByCityIgnoreCase(city);
        log.debug("Search returned {} hotels — city={}", results.size(), city);
        return results;

        //return hotelRepository.findByCityIgnoreCase(city);
    }
}