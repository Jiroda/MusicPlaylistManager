package com.highspot.musicplaylistmanager;

import com.highspot.musicplaylistmanager.exceptions.InvalidPlaylistException;
import com.highspot.musicplaylistmanager.model.MusicData;
import com.highspot.musicplaylistmanager.model.Playlist;
import com.highspot.musicplaylistmanager.model.Song;
import com.highspot.musicplaylistmanager.model.User;
import com.highspot.musicplaylistmanager.service.MusicPlaylistManagerService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MusicPlaylistManagerServiceTest {

    @Autowired
    MusicPlaylistManagerService musicPlaylistManagerService;

    private static MusicData musicData;

    @BeforeClass
    public static void beforeAllTestMethods() {
        musicData = new MusicData();
        List<Song> songs = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<Playlist> playlists = new ArrayList<>();
        songs.add(new Song("1","Camila Cabello","Never Be the Same"));
        songs.add(new Song("2","Zedd", "The Middle"));
        musicData.setSongs(songs);
        users.add(new User("1", "Albin Jaye"));
        users.add(new User("2", "Dipika Crescentia"));
        musicData.setUsers(users);
        List<String> songIds = new ArrayList<String>();
        songIds.add("1");
        songIds.add("2");
        playlists.add(new Playlist("1", "1", songIds));
        playlists.add(new Playlist("2", "2", songIds));
        musicData.setPlaylists(playlists);
    }

    @Before
    public void init(){
        musicPlaylistManagerService.setMusicData(musicData);
    }
    
    @Test
    public void testAddNewPlayList() {
        List<String> songs = new ArrayList<>();
        songs.add("21");
        Playlist playlist = new Playlist("1", "2", songs);
        try {
            int count = musicPlaylistManagerService.addNewPlaylist(playlist);
            Assert.assertTrue(count==1);
        } catch (InvalidPlaylistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemovePlayList() {
        try {
            int count = musicPlaylistManagerService.removePlaylist("1", "2");
            Assert.assertTrue(count==1);
        } catch (InvalidPlaylistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdatePlayLists(){
        List<Playlist> newPlaylists = new ArrayList<>();
        List<String> songIds = new ArrayList<String>();
        songIds.add("1");
        songIds.add("2");
        newPlaylists.add(new Playlist("1", "1", songIds));
        newPlaylists.add(new Playlist("2", "2", songIds));
        try {
            int updateCount = musicPlaylistManagerService.updatePlayLists(newPlaylists);
            Assert.assertEquals(updateCount, 2);
        } catch (InvalidPlaylistException e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void testGetUpdatedMusicData(){
        MusicData updatedMusicData = musicPlaylistManagerService.getUpdatedMusicData();
        Assert.assertTrue(updatedMusicData.getPlaylists().size()==2);
    }


}
