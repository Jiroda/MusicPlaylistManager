package com.highspot.musicplaylistmanager.exception;

public class ExtractPlaylistChangesFromFileException extends Exception{
    private static final long serialVersionUID = 1L;

    public ExtractPlaylistChangesFromFileException(String message){
        super(message);
    }

    public ExtractPlaylistChangesFromFileException(String message, Throwable cause){
        super(message, cause);
    }
}
