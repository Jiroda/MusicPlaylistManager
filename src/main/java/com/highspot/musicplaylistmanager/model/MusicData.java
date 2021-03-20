package com.highspot.musicplaylistmanager.model;

import java.util.List;

public class MusicData {
    private List<User> users;
    private List<Playlist> playlists;
    private List<Song> songs;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }  
}
