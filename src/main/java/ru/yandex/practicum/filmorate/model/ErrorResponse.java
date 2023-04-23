package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error, String message) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}