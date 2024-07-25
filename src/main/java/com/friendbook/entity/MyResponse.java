package com.friendbook.entity;

import lombok.Data;

@Data
public class MyResponse {
    public int status;
    public String message;
    public Object data;
}
