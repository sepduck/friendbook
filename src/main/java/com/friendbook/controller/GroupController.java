package com.friendbook.controller;

import com.friendbook.entity.BaseReponse;
import com.friendbook.entity.Groups;
import com.friendbook.service.GroupSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController extends BaseReponse {
    @Autowired
    private GroupSerive groupService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestParam String name,
                                         @RequestParam String description) {
       try {
           Groups group = groupService.createGroup(name, description);
           return getResponseEntity(group);
       }catch (Exception e) {
           return getErrorResponseEntity("Error create group", 500);
       }
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMember(@RequestParam Long groupId,
                                       @RequestParam Long userId,
                                       @RequestParam String role) {
        try {
            groupService.addMember(groupId, userId, role);
            return getResponseEntity("Member added successfully");
        }catch (Exception e) {
            return getErrorResponseEntity("Error add member to group", 500);
        }
    }

    @GetMapping("/myGroups")
    public ResponseEntity<?> getUserGroups() {
        try {
            List<Groups> groups = groupService.getUserGroups();
            return getResponseEntity(groups);
        }catch (Exception e) {
            return getErrorResponseEntity("Error get user groups", 500);
        }
    }
}
