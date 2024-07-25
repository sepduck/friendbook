package com.friendbook.controller;

import com.friendbook.dto.CommentsDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class ConmentsController extends BaseReponse {
    @Autowired
    private CommentsService commentsService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> saveComment(@PathVariable("postId") long postId,
                                         @RequestBody CommentsDTO commentsDTO) {
        try {
            return getResponseEntity(commentsService.saveComments(postId, commentsDTO));
        } catch (Exception e) {
            return getErrorResponseEntity("Error while adding comment", 500);
        }
    }

    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("postId") long postId,
                                           @RequestBody CommentsDTO commentsDTO,
                                           @PathVariable("commentId") long commentId) {
        try {
            return getResponseEntity(commentsService.updateComments(postId, commentId, commentsDTO));
        } catch (Exception e) {
            return getErrorResponseEntity("Error while updating comment", 500);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") long commentId) {
        try {
            commentsService.deleteComments(commentId);
            return getResponseEntity("Deleted comment successfully");
        } catch (Exception e) {
            return getErrorResponseEntity("Error while deleting comment", 500);
        }
    }
}
