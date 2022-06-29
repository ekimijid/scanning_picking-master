package com.essers.scanning.views.utils;

public class FileInputException extends Exception {
    public FileInputException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
