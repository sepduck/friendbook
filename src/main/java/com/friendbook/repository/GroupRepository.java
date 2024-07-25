package com.friendbook.repository;

import com.friendbook.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Long> {
    List<Groups> findByMembershipsUsersUserId(Long userId);
}
