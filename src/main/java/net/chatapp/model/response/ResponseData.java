package net.chatapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResponseData {
    private Object responseObject;
    private String message;
    public ResponseData(Object responseObject, String message){
        this.responseObject = responseObject;
        this.message = message;
    }
}
