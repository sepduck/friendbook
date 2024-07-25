package com.friendbook.service;

import com.friendbook.entity.Groups;

import java.util.List;

public interface GroupSerive {
    Groups createGroup(String name, String description);

    void addMember(Long groupId, Long userId, String role);

    List<Groups> getUserGroups();

    Groups findGroupById(Long groupId);
}
