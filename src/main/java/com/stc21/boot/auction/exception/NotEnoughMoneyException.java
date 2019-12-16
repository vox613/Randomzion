package com.stc21.boot.auction.exception;

public class NotEnoughMoneyException extends Exception {

    private static final long serialVersionUID = -3128681006635769411L;

    public NotEnoughMoneyException(String message) {
        super(message);
    }

}