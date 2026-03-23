package com.github.ahmed0427.mylang;

class EvaluationException extends RuntimeException {
    final Token token;

    EvaluationException(Token token, String message) {
        super(message);
        this.token = token;
    } 
}
