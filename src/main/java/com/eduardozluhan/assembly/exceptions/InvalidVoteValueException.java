package com.eduardozluhan.assembly.exceptions;

public class InvalidVoteValueException extends Throwable {
    @Override
    public String getMessage() {
        return "Vote values must be \"Sim\" or \"Não\"";
    }
}
