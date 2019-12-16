package com.stc21.boot.auction.advice;

import com.stc21.boot.auction.exception.PageNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(PageNotFoundException.class)
    public String pageNotFound() {
        return "errors/pageNotFound";
    }
}
