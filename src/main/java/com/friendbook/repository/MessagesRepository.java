package com.friendbook.repository;

import com.friendbook.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    @Query(value = "SELECT *\n" +
            "FROM messages\n" +
            "WHERE (sender_id = :userId AND receiver_id = :friendId)\n" +
            "   OR (sender_id = :friendId AND receiver_id = :userId)", nativeQuery = true)
    List<Messages> findBySenderIdUserIdAndReceiverIdUserIdOrReceiverIdUserIdAndSenderIdUserId(
            @Param("userId") Long userId,
            @Param("friendId") Long friendId1,
            @Param("friendId") Long friendId2);

    @Query(value = "SELECT *\n" +
            "FROM messages\n" +
            "WHERE (sender_id = :userId AND receiver_id = :friendId)\n" +
            "   OR (sender_id = :friendId AND receiver_id = :userId)", nativeQuery = true)
    List<Messages> findMessagesBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
