package com.highspot.musicplaylistmanager.model;

public class PlaylistStorageKey implements Comparable<PlaylistStorageKey>{
    private String playlistId;
    private String userId;

    public PlaylistStorageKey(String playlistId, String userId) {
        this.playlistId = playlistId;
        this.userId = userId;
    }

    public String getPlaylistId() {
        return playlistId;
    }
    public String getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playlistId == null) ? 0 : playlistId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlaylistStorageKey other = (PlaylistStorageKey) obj;
        if (playlistId == null) {
            if (other.playlistId != null)
                return false;
        } else if (!playlistId.equals(other.playlistId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PlaylistStorageKey [playlistId=" + playlistId + ", userId=" + userId + "]";
    }

    
    /** 
     * Ascending order by playlist id then by user id incase the playlist id is the same
     * @param playlistStorageKey
     * @return int
     */
    @Override
    public int compareTo(PlaylistStorageKey playlistStorageKey) {
        int playlistId1 = Integer.valueOf(this.playlistId);
        int playlistId2 =  Integer.valueOf(playlistStorageKey.playlistId);
        if(playlistId1!=playlistId2){
            return (int)(playlistId1 - playlistId2);
        }else{
            return (int)(Integer.valueOf(this.userId) - Integer.valueOf(playlistStorageKey.userId));
        }
    }
}
