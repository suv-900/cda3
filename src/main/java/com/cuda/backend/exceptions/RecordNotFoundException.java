package com.cuda.backend.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String msg){
        super(msg);
    }

    public RecordNotFoundException(){
        super("not found");
    }
}
