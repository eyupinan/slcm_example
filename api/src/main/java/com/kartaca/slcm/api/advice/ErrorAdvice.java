package com.kartaca.slcm.api.advice;

import com.kartaca.slcm.api.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ErrorAdvice {

    @ResponseBody
    @ExceptionHandler(value = { NotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(Exception ex) {
        return ex.getMessage();
    }
}
