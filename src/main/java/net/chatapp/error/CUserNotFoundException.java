package net.chatapp.error;

public class CUserNotFoundException extends RuntimeException{
    public CUserNotFoundException(String message){
        super(message);
    }
}
