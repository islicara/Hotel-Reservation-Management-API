package de.lhind.internship.mini.project.service;

import de.lhind.internship.mini.project.dto.ReservationRequestDTO;
import de.lhind.internship.mini.project.entity.*;
import de.lhind.internship.mini.project.exception.*;
import de.lhind.internship.mini.project.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationService {

    private static final Logger log = LogManager.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public Reservation createReservation(ReservationRequestDTO dto) {
        log.trace("Entering createReservation() — roomId={}, guestId={}", dto.getRoomId(), dto.getGuestId());

        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            log.error("Invalid dates — checkIn={}, checkOut={}", dto.getCheckInDate(), dto.getCheckOutDate());
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> {
                    log.error("Room not found — roomId={}", dto.getRoomId());
                    return new ResourceNotFoundException("Room not found");
                });

        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> {
                        log.error("Guest not found — guestId={}", dto.getGuestId());
                        return new ResourceNotFoundException("Guest not found");
                        });

        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            log.warn("Reservation rejected, room under maintenance — roomId={}", dto.getRoomId());
            throw new RoomNotAvailableException("Room is under maintenance and cannot be reserved.");
        }

        if (room.getCapacity() < dto.getNumberOfGuests()) {
            log.warn("Reservation rejected, capacity exceeded — roomId={}, capacity={}, requested={}", dto.getRoomId(), room.getCapacity(), dto.getNumberOfGuests());
            throw new IllegalArgumentException("Room capacity exceeded.");
        }

        long overlaps = reservationRepository.countOverlappingReservations(dto.getRoomId(), dto.getCheckInDate(), dto.getCheckOutDate());
        if (overlaps > 0) {
            log.warn("Reservation rejected, overlapping block found — roomId={}, overlaps={}", dto.getRoomId(), overlaps);
            throw new RoomNotAvailableException("Room has overlapping reservation blocks.");
        }

        long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        log.debug("Calculated total price — roomId={}, nights={}, total={}", dto.getRoomId(), nights, total);

        Reservation res = new Reservation();
        res.setRoom(room);
        res.setGuest(guest);
        res.setCheckInDate(dto.getCheckInDate());
        res.setCheckOutDate(dto.getCheckOutDate());
        res.setNumberOfGuests(dto.getNumberOfGuests());
        res.setTotalPrice(total);
        res.setStatus(ReservationStatus.CONFIRMED);

        Reservation saved = reservationRepository.save(res);
        log.info("Reservation created successfully — id={}, roomId={}, guestId={}", saved.getId(), dto.getRoomId(), dto.getGuestId());
        return saved;

        //return reservationRepository.save(res);
    }

    public List<Reservation> getAllReservations() {
        log.trace("Entering getAllReservations()");
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        log.trace("Entering getReservationById() — id={}", id);
        return reservationRepository.findById(id)
                .orElseThrow(() -> {log.error("Reservation not found — id={}", id);
                    return new ResourceNotFoundException("Reservation not found");
                });

        //return reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    public Reservation updateStatus(Long id, ReservationStatus status) {
        log.trace("Entering updateStatus() — id={}, newStatus={}", id, status);
        Reservation res = getReservationById(id);
        res.setStatus(status);
        Reservation saved = reservationRepository.save(res);
        log.info("Reservation status updated — id={}, status={}", id, status);
        return saved;

        //return reservationRepository.save(res);
    }

    public void cancelReservation(Long id) {
        log.trace("Entering cancelReservation() — id={}", id);
        Reservation res = getReservationById(id);
        res.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(res);
        log.warn("Reservation cancelled — id={}", id);
    }

    public List<Reservation> getReservationsByGuest(Long guestId) {
        log.trace("Entering getReservationsByGuest() — guestId={}", guestId);
        return reservationRepository.findByGuestId(guestId);
    }

    public List<Map<String, Object>> getMostReservedRoomsReport() {
        log.trace("Entering getMostReservedRoomsReport()");
        List<Object[]> rawData = reservationRepository.findMostReservedRoomIdsNative();
        List<Map<String, Object>> report = new ArrayList<>();
        for (Object[] row : rawData) {
            Map<String, Object> map = new HashMap<>();
            map.put("roomId", row[0]);
            map.put("reservationCount", row[1]);
            report.add(map);
        }
        log.debug("Generated report with {} entries", report.size());
        return report;
    }
}