package com.example.voting.exception;

public class UniqueConstraintException extends IllegalRequestDataException {
    public UniqueConstraintException(String msg) {
        super(msg);
    }
}
