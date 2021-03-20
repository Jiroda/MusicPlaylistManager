package com.highspot.musicplaylistmanager.service;

import com.highspot.musicplaylistmanager.exception.InvalidPlaylistException;
import com.highspot.musicplaylistmanager.model.MusicData;
import com.highspot.musicplaylistmanager.model.Playlist;
import com.highspot.musicplaylistmanager.model.PlaylistStorageKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class MusicPlaylistManagerService implements IMusicManagerService{
    private static final HashMap<PlaylistStorageKey, List<String>> playlistStorage = new HashMap<>();
    private MusicData musicData;

    public MusicPlaylistManagerService(){}

    public void setMusicData(MusicData musicData) {
        this.musicData = musicData;
        List<Playlist> playlists = musicData.getPlaylists();
        if(!CollectionUtils.isEmpty(playlists)){
            for(Playlist playlist: playlists){
                String playlistId = playlist.getId();
                List<String> songIds = playlist.getSong_ids();
                String userId = playlist.getUser_id();
                PlaylistStorageKey playlistStorageKey = new PlaylistStorageKey(playlistId, userId);
                playlistStorage.put(playlistStorageKey, songIds);
            }
        }
    }

    /**
     * Adds a new playlist to the playlist storage
     * @param newPlaylist
     * @return int
     * @throws InvalidPlaylistException
     */
    @Override
    public int addNewPlaylist(Playlist newPlaylist) throws InvalidPlaylistException {
        if(newPlaylist==null || StringUtils.isBlank(newPlaylist.getId()) || StringUtils.isBlank(newPlaylist.getUser_id())){
            throw new InvalidPlaylistException("Invalid Playlist encountered! "+newPlaylist.toString());
        }
        String playlistId = newPlaylist.getId();
        List<String> songIds = newPlaylist.getSong_ids();
        String userId = newPlaylist.getUser_id();
        PlaylistStorageKey playlistStorageKey = new PlaylistStorageKey(playlistId, userId);
        if(!playlistStorage.containsKey(playlistStorageKey)){
            playlistStorage.put(playlistStorageKey, songIds);
        }
        return 1;
    }

    
    /** 
     * Removes a playlist from the playlist storage
     * @param playlistId
     * @param userId
     * @return int
     * @throws InvalidPlaylistException
     */
    @Override
    public int removePlaylist(String playlistId, String userId) throws InvalidPlaylistException {
        if(StringUtils.isBlank(playlistId) || StringUtils.isBlank(userId)){
            throw new InvalidPlaylistException("Invalid Playlist encountered! Playlist Id/UserID is null.");
        }
        PlaylistStorageKey playlistStorageKey = new PlaylistStorageKey(playlistId, userId);
        if(playlistStorage.containsKey(playlistStorageKey)){
            playlistStorage.remove(playlistStorageKey);
            return 1;
        }
        return -1;
    }

    
    /** 
     * Updates a list of playlist
     * @param newPlaylists
     * @return int
     * @throws InvalidPlaylistException
     */
    @Override
    public int updatePlayLists(List<Playlist> newPlaylists) throws InvalidPlaylistException {
        int numOfPlayListsUpdated =0;
        for(Playlist newPlaylist:newPlaylists){
            numOfPlayListsUpdated+=updatePlayList(newPlaylist);
        }
        return numOfPlayListsUpdated;
    }

    
    /**
     * Updates, inserts and removes the playlist from playlist storage, based on the input playlist data 
     * @param newPlaylist
     * @return int
     * @throws InvalidPlaylistException
     */
    @Override
    public int updatePlayList(Playlist newPlaylist) throws InvalidPlaylistException {
        if(newPlaylist==null || StringUtils.isBlank(newPlaylist.getId()) || StringUtils.isBlank(newPlaylist.getUser_id())){
            throw new InvalidPlaylistException("Invalid Playlist encountered! "+newPlaylist.toString());
        }
        String playlistId = newPlaylist.getId();
        List<String> newSongIds = newPlaylist.getSong_ids();
        String userId = newPlaylist.getUser_id();
        List<String> existingSongIds = findSongIdsByPlaylistIdAndUserId(playlistId, userId);
        //1. Empty song list in the change.json means that we need to delete the songs in the playlist
        if(newSongIds.size()==0){
            return removePlaylist(playlistId, userId);
        }
        //2. Playlist already exists update songs in playlist
        if(!CollectionUtils.isEmpty(existingSongIds)){
            if(removePlaylist(playlistId, userId)==1){
                return addNewPlaylist(newPlaylist);
            }else{
                return -1;
            }
        }else{
            //3. We just add the new playlist
            return addNewPlaylist(newPlaylist);
        }
    }

    
    /** 
     * Gets the updated music data to be written to the output JSON file
     * @return MusicData
     */
    @Override
    public MusicData getUpdatedMusicData(){
        List<Playlist> playlists = new ArrayList<>();
        if(!playlistStorage.isEmpty()){
            Map<PlaylistStorageKey, List<String>> playlistStorageSortedByKey = new TreeMap<>(playlistStorage);
            for(Map.Entry<PlaylistStorageKey, List<String>> entry : playlistStorageSortedByKey.entrySet()){
                PlaylistStorageKey key = entry.getKey();
                List<String> songIds = entry.getValue();
                Playlist playlist = new Playlist(key.getPlaylistId(), key.getUserId(), songIds);
                playlists.add(playlist);
            }
        }
        if(!CollectionUtils.isEmpty(playlists)){
            this.musicData.setPlaylists(playlists);
        }

        return this.musicData;
    }

    
    /** 
     * Finds the songs by playlistId and userId
     * @param playlistId
     * @param userId
     * @return List<String>
     */
    private List<String> findSongIdsByPlaylistIdAndUserId(String playlistId, String userId){
        PlaylistStorageKey playlistStorageKey = new PlaylistStorageKey(playlistId, userId);
        if(playlistStorage.containsKey(playlistStorageKey)){
            return playlistStorage.get(playlistStorageKey);
        }else{
            return new ArrayList<>();
        }
    }

}
