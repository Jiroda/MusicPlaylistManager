package com.highspot.musicplaylistmanager.model;

public class Song {
    private String id;
    private String artist;
    private String title;

    public Song(String id, String artist, String title) {
        this.id = id;
        this.artist = artist;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }
}
