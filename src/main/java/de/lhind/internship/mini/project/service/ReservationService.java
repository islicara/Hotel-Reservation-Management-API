package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.dto.ReservationRequestDTO;
import de.lhind.internship.mini.project.entity.*;
import de.lhind.internship.mini.project.exception.*;
import de.lhind.internship.mini.project.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public Reservation createReservation(ReservationRequestDTO dto) {
        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new RoomNotAvailableException("Room is under maintenance and cannot be reserved.");
        }

        if (room.getCapacity() < dto.getNumberOfGuests()) {
            throw new IllegalArgumentException("Room capacity exceeded.");
        }

        long overlaps = reservationRepository.countOverlappingReservations(dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
        if (overlaps > 0) {
            throw new RoomNotAvailableException("Room has overlapping reservation blocks.");
        }

        long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Reservation res = new Reservation();
        res.setRoom(room);
        res.setGuest(guest);
        res.setCheckInDate(dto.getCheckInDate());
        res.setCheckOutDate(dto.getCheckOutDate());
        res.setNumberOfGuests(dto.getNumberOfGuests());
        res.setTotalPrice(total);
        res.setStatus(ReservationStatus.CONFIRMED);

        return reservationRepository.save(res);
    }

    public List<Reservation> getAllReservations() { return reservationRepository.findAll(); }
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }
    public Reservation updateStatus(Long id, ReservationStatus status) {
        Reservation res = getReservationById(id);
        res.setStatus(status);
        return reservationRepository.save(res);
    }
    public void cancelReservation(Long id) {
        Reservation res = getReservationById(id);
        res.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(res);
    }
    public List<Reservation> getReservationsByGuest(Long guestId) { return reservationRepository.findByGuestId(guestId); }

    public List<Map<String, Object>> getMostReservedRoomsReport() {
        List<Object[]> rawData = reservationRepository.findMostReservedRoomIdsNative();
        List<Map<String, Object>> report = new ArrayList<>();
        for (Object[] row : rawData) {
            Map<String, Object> map = new HashMap<>();
            map.put("roomId", row[0]);
            map.put("reservationCount", row[1]);
            report.add(map);
        }
        return report;
    }
}