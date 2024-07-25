package com.friendbook.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class GenderDTO {
    @NotBlank(message = "Giới tính không được để trống")
    @Size(min = 2, max = 20, message = "Giới tính phải có độ dài tối thiểu là 2 ký tự và tối đa là 20 ký tự.")
    private String genderName;
}
