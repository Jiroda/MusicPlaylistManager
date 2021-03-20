package com.highspot.musicplaylistmanager.exceptions;

public class WriteToFileException extends Exception{
    private static final long serialVersionUID = 1L;

    public WriteToFileException(String message){
        super(message);
    }

    public WriteToFileException(String message, Throwable cause){
        super(message, cause);
    }
}
