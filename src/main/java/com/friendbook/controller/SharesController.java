package com.friendbook.controller;

import com.friendbook.dto.SharesDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.service.SharesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share")
public class SharesController extends BaseReponse {
    @Autowired
    private SharesService sharesService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> saveShare(@PathVariable("postId") long postId, @RequestBody SharesDTO sharesDTO) {
        try {
            return getResponseEntity(sharesService.saveShare(postId, sharesDTO));
        } catch (Exception e) {
            return getErrorResponseEntity("Error while adding share", 500);
        }
    }
}
