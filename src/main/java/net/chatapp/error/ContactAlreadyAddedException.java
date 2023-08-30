package net.chatapp.error;

public class ContactAlreadyAddedException extends RuntimeException{

    public ContactAlreadyAddedException(){
        super("This contact is already added!");
    }

}
