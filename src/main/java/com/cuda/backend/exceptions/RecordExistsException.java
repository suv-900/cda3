package com.cuda.backend.exceptions;

public class RecordExistsException extends RuntimeException{
   RecordExistsException(){
    super();
   }
   
   RecordExistsException(String s){
    super(s);
   }
}
