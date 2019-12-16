package com.stc21.boot.auction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Page Not Found")
public class PageNotFoundException extends RuntimeException {}
