package com.tantalum.test.message.controllers;

import com.tantalum.test.message.exceptions.NoModificableMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class MessageControllerAdvice {

    @ExceptionHandler(NoModificableMessageException.class)
    void handleBadRequests(HttpServletResponse response, NoModificableMessageException ex) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
