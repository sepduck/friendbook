package com.friendbook.service.impl;

import com.friendbook.entity.GroupMenbership;
import com.friendbook.entity.Groups;
import com.friendbook.entity.Users;
import com.friendbook.repository.GroupMembershipRepository;
import com.friendbook.repository.GroupRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.GroupSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl extends BaseService implements GroupSerive {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;


    @Override
    public Groups createGroup(String groupName, String description) {
        Users owner = getCurrentUser(authenticationService);

        Groups groups = new Groups();
        groups.setGroupName(groupName);
        groups.setDescription(description);
        groups.setOwner(owner);

        groups = groupRepository.save(groups);

        // Add owner as a member
        GroupMenbership membership = new GroupMenbership();
        membership.setUsers(owner);
        membership.setGroups(groups);
        membership.setRole("OWNER");

        groupMembershipRepository.save(membership);

        return groups;
    }

    @Override
    public void addMember(Long groupId, Long userId, String role) {
        Groups group = findGroupById(groupId);
        Users user = authenticationService.getFindByUserId(userId);

        GroupMenbership membership = new GroupMenbership();
        membership.setUsers(user);
        membership.setGroups(group);
        membership.setRole(role);

        groupMembershipRepository.save(membership);
    }

    @Override
    public List<Groups> getUserGroups() {
        Users currentUser = getCurrentUser(authenticationService);
//        return groupRepository.findByMembersUserUserId(currentUser.getUserId());
        return groupRepository.findByMembershipsUsersUserId(currentUser.getUserId());
    }

    @Override
    public Groups findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found."));
    }
}
