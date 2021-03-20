package com.highspot.musicplaylistmanager.service;

import java.util.List;

import com.highspot.musicplaylistmanager.exception.InvalidPlaylistException;
import com.highspot.musicplaylistmanager.model.MusicData;
import com.highspot.musicplaylistmanager.model.Playlist;

public interface IMusicManagerService {
    public int addNewPlaylist(Playlist newPlaylist) throws InvalidPlaylistException;

    public int removePlaylist(String playlistId, String userId) throws InvalidPlaylistException;

    public int updatePlayLists(List<Playlist> newPlaylists) throws InvalidPlaylistException;

    public int updatePlayList(Playlist newPlaylist) throws InvalidPlaylistException;

    public MusicData getUpdatedMusicData();
}
