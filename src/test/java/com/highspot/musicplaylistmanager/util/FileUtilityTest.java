package com.highspot.musicplaylistmanager.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.highspot.musicplaylistmanager.exceptions.ExtractMusicDataFromFileException;
import com.highspot.musicplaylistmanager.exceptions.ExtractPlaylistChangesFromFileException;
import com.highspot.musicplaylistmanager.exceptions.WriteToFileException;
import com.highspot.musicplaylistmanager.model.MusicData;
import com.highspot.musicplaylistmanager.model.PlayListChange;
import com.highspot.musicplaylistmanager.model.Playlist;
import com.highspot.musicplaylistmanager.model.Song;
import com.highspot.musicplaylistmanager.model.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUtilityTest {
    
    @Autowired
    FileUtility fileUtility;

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

    @Test
    public void testIsFilePathValid(){
        boolean result = fileUtility.isFilePathValid("src/main/resources/json/mixtape.json");
        Assert.assertTrue(result);
    }

    @Test
    public void testExtractMusicDataFromSourceFile(){
        MusicData result;
        try {
            result = fileUtility.extractMusicDataFromSourceFile("src/main/resources/json/mixtape.json");
            Assert.assertNotNull(result);
        } catch (ExtractMusicDataFromFileException e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void testExtractMusicDataFromSourceFile_failure(){
        try {
            fileUtility.extractMusicDataFromSourceFile("src/main/resources/json/mixtape1.json");
        } catch (ExtractMusicDataFromFileException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testExtractPlaylistChangesFromChangesFile(){
        PlayListChange result;
        try {
            result = fileUtility.extractPlaylistChangesFromChangesFile("src/main/resources/json/change.json");
            Assert.assertNotNull(result);
        } catch (ExtractPlaylistChangesFromFileException e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void testExtractPlaylistChangesFromChangesFile_failure(){
        try {
            fileUtility.extractPlaylistChangesFromChangesFile("src/main/resources/json/change1.json");
        } catch (ExtractPlaylistChangesFromFileException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testWriteMusicDataToOutputFile(){
        try {
            fileUtility.writeMusicDataToOutputFile(musicData, "src/main/resources/json/output.json");
            Assert.assertTrue(true);
        } catch (WriteToFileException e) {
            Assert.assertFalse(true);
        }
    }

    @Test
    public void testValidateJSONSyntax(){
        try {
            fileUtility.validateJSONSyntax("src/main/resources/json/invalidfile.json");
        } catch(JsonSyntaxException e){
            Assert.assertTrue(true);
        }
    }
    
}
