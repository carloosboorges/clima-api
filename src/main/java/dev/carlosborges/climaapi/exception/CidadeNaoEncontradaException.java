package dev.carlosborges.climaapi.exception;

public class CidadeNaoEncontradaException extends RuntimeException {

    public CidadeNaoEncontradaException(String message) {
        super(message);
    }
}
