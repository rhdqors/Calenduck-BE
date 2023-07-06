package com.example.calenduck.domain.bookmark.Repository;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user);
    List<Bookmark> findAllByMt20id(String mt20id);
    Optional<Bookmark> findByUserAndMt20id(User user, String mt20id);
    Optional<Bookmark> findByUser(User user);
    Optional<Bookmark> findBymt20id(String mt20id);
    Bookmark findByUserAndMt20idAndReservationDate(User user, String mt20id, String reservationDate);

    boolean existsByUserAndMt20id(User user, String mt20id);
    void deleteByUserAndMt20id(User user, String mt20id);

    List<String> findAllByUserAndAlarm(User user, String alarm);


}
