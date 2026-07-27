package de.lhind.internship.mini.project.repository;

import de.lhind.internship.mini.project.entity.Room;
import de.lhind.internship.mini.project.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelIdAndStatus(Long hotelId, RoomStatus status);
    List<Room> findByHotelId(Long hotelId);

    @Query(value = """
            SELECT r.* FROM rooms r
            JOIN reservations res ON r.id = res.room_id
            GROUP BY r.id
            ORDER BY COUNT(res.id) DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Room> findMostReservedRooms();
}
