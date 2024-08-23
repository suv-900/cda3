package com.cuda.backend.exceptions;

public class ErrorCreatingTransaction extends RuntimeException{
   ErrorCreatingTransaction(){
    super();
   }
   
   ErrorCreatingTransaction(String msg){
    super(msg);
   }
}
