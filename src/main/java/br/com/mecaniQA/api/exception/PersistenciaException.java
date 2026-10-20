package br.com.mecaniQA.api.exception;



public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String message) { super(message); }
    public PersistenciaException(String message, Throwable cause) { super(message, cause); }
}
