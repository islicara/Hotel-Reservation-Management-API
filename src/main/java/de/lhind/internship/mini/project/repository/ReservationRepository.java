package de.lhind.internship.mini.project.repository;

import de.lhind.internship.mini.project.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGuestId(Long guestId);

    @Query("""
        SELECT COUNT(r) 
        FROM Reservation r 
        WHERE r.room.id = :roomId 
          AND r.status <> 'CANCELLED' 
          AND r.checkInDate < :checkOutDate 
          AND r.checkOutDate > :checkInDate
    """)
    long countOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

    @Query(value = """
        SELECT r.room_id, COUNT(r.id) as res_count 
        FROM reservations r 
        GROUP BY r.room_id 
        ORDER BY res_count DESC 
        LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findMostReservedRoomIdsNative();
}
