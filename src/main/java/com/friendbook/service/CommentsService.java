package com.friendbook.service;

import com.friendbook.dto.CommentsDTO;
import com.friendbook.entity.Comments;

import java.util.List;

public interface CommentsService {
    Comments saveComments(long postId, CommentsDTO commentsDTO);

    Comments updateComments(long postId, long commentId, CommentsDTO commentsDTO);

    void deleteComments(long commentId);

    List<CommentsDTO> getComments();

    Comments findByCommentId(long commentId);
}
