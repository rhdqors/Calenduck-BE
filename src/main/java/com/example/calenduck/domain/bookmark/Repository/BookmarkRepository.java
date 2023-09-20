package com.example.calenduck.domain.bookmark.Repository;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user);
    List<Bookmark> findAllByMt20id(String mt20id);
    Bookmark findByUserAndMt20idAndReservationDate(User user, String mt20id, String reservationDate);

}
