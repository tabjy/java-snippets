package com.tabjy.snippets.expression_compiler;

public class IllegalSyntaxException extends Exception {
    public IllegalSyntaxException() {
        super();
    }

    public IllegalSyntaxException(String message) {
        super(message);
    }

    public IllegalSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalSyntaxException(Throwable cause) {
        super(cause);
    }
}
