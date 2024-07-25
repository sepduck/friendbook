package com.friendbook.repository;

import com.friendbook.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query(value = "SELECT *\n" +
            "FROM friendship\n" +
            "WHERE user_id = :userId\n" +
            "  AND friend_id = :friendId;", nativeQuery = true)
    Optional<Friendship> findByUserUserIdAndFriendUserId(@Param("userId") Long userId,
                                                         @Param("friendId") Long friendId);

    @Query(value = "SELECT *\n" +
            "FROM friendship\n" +
            "WHERE user_id = :userId\n" +
            "  AND friend_id = :friendId\n" +
            "  AND status = :status;", nativeQuery = true)
    Optional<Friendship> findByUserUserIdAndFriendUserIdAndStatus(@Param("userId") Long userId,
                                                                  @Param("friendId") Long friendId,
                                                                  @Param("status") String status);
}
