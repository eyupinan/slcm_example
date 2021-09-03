package com.kartaca.slcm.admin.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PasswordException extends RuntimeException{
    public PasswordException(){
        super();
    }
    public PasswordException(Throwable cause){
        super("Masterpass private key password was incorrect",cause);
    }
}
