package com.friendbook.controller;

import com.friendbook.dto.LikeDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController extends BaseReponse {
    @Autowired
    private LikeService likeService;
    @PostMapping("/{postId}")
    public ResponseEntity<?> likeOrUnlike(@PathVariable("postId") long postId){
        try {
            likeService.likeOrUnlikePost(postId);
            return getResponseEntity("Like success");
        }catch (Exception e){
            return getErrorResponseEntity("Error while adding like", 500);
        }
    }
}
