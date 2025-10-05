package com.task.dogs.exception;

public class ResourceNotFoundException extends RuntimeException{
     public ResourceNotFoundException(String message) {
        super(message);
    }
}
