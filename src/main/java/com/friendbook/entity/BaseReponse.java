package com.friendbook.entity;

import org.springframework.http.ResponseEntity;

public class BaseReponse {
    private MyResponse getMyResponse(Object data) {
        MyResponse myResponse = new MyResponse();
        myResponse.data = data;
        myResponse.message = "Success";
        myResponse.status = 200;
        return myResponse;
    }
    protected ResponseEntity<?> getResponseEntity(Object data) {
        return ResponseEntity.ok().body(getMyResponse(data));
    }
    protected ResponseEntity<?> getErrorResponseEntity(String message, int status) {
        MyResponse myResponse = new MyResponse();
        myResponse.status = status;
        myResponse.message = message;
        myResponse.data = null;
        return ResponseEntity.status(status).body(myResponse);
    }
}
