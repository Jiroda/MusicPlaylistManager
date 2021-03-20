package com.highspot.musicplaylistmanager.exception;

public class InvalidPlaylistException extends Exception{
    private static final long serialVersionUID = 1L;

    public InvalidPlaylistException(String message){
        super(message);
    }

    public InvalidPlaylistException(String message, Throwable cause){
        super(message, cause);
    }
}
