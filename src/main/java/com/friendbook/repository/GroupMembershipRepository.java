package com.friendbook.repository;

import com.friendbook.entity.GroupMenbership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMenbership, Long> {
    List<GroupMenbership> findByGroupsGroupId(Long groupId);

    List<GroupMenbership> findByUsersUserIdAndGroupsGroupId(Long userId, Long groupId);
}
