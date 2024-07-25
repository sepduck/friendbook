package com.friendbook.service;

import com.friendbook.dto.SharesDTO;
import com.friendbook.entity.Shares;

public interface SharesService {
    Shares saveShare(long postId, SharesDTO sharesDTO);

    Shares updateShare(long postId, long shareId, SharesDTO sharesDTO);

    void deleteShare(long shareId);

    Shares findByShareId(long shareId);
}
