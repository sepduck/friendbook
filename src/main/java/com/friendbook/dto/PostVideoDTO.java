package com.friendbook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class PostVideoDTO {
    @JsonProperty("post_id")
    private long postId;

    @Size(min = 5, max = 255, message = "Tên file ảnh không được nhỏ hơn 5 và lớn hơn 200")
    @JsonProperty("video_url")
    private String videoUrl;
}
