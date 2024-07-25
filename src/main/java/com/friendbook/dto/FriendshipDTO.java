package com.friendbook.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDTO {
    private Long userId; // ID của người gửi yêu cầu kết bạn
    private Long friendId; // ID của người nhận yêu cầu kết bạn
    private String status; // Trạng thái của yêu cầu kết bạn
}
