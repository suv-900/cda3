package com.cuda.backend.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cuda.backend.exceptions.RecordNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {RecordNotFoundException.class})
    public String handleNotFoundException(Exception e){
        return e.getMessage();
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public void handleBadRequest(Exception e){}
}
