package com.highspot.musicplaylistmanager.exceptions;

public class ExtractMusicDataFromFileException extends Exception{
    private static final long serialVersionUID = 1L;

    public ExtractMusicDataFromFileException(String message){
        super(message);
    }

    public ExtractMusicDataFromFileException(String message, Throwable cause){
        super(message, cause);
    }
}
