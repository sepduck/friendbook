package com.friendbook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;

@Data
@Builder
public class UserImageDTO {
    @JsonProperty("user_id")
    private long userId;

    @Size(min = 5, max = 255, message = "Tên file ảnh không được nhỏ hơn 5 và lớn hơn 200")
    @JsonProperty("image_url")
    private String imageUrl;
}
