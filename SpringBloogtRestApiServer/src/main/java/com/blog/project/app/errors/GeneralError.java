package com.blog.project.app.errors;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class GeneralError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public GeneralError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public GeneralError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
