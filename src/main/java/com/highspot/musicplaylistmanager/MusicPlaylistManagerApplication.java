package com.highspot.musicplaylistmanager;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.highspot.musicplaylistmanager.exception.ExtractMusicDataFromFileException;
import com.highspot.musicplaylistmanager.exception.ExtractPlaylistChangesFromFileException;
import com.highspot.musicplaylistmanager.exception.InvalidPlaylistException;
import com.highspot.musicplaylistmanager.exception.WriteToFileException;
import com.highspot.musicplaylistmanager.model.MusicData;
import com.highspot.musicplaylistmanager.model.PlayListChange;
import com.highspot.musicplaylistmanager.model.Playlist;
import com.highspot.musicplaylistmanager.service.MusicPlaylistManagerService;
import com.highspot.musicplaylistmanager.util.FileUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicPlaylistManagerApplication implements CommandLineRunner {

	@Autowired
	FileUtility fileUtility;

	@Autowired
	MusicPlaylistManagerService musicPlaylistManagerService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MusicPlaylistManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MusicPlaylistManagerApplication.class, args);
	}

	@Override
	public void run(String... args){
		long startTime = System.currentTimeMillis();
		if (args.length != 3) {
			LOGGER.error("The Application requires 3 non-optional command line arguments: [source json file path] , [change json file path] , [output json file path] ! Please retry executing the jar with the right parameters.");
			return;
		}

		LOGGER.info("Received {} command-line arguments: {}.", args.length, Arrays.toString(args));

		if (!fileUtility.isFilePathValid(args[0])) {
			LOGGER.error("Source file path provided " + args[0] + " is invalid!");
			return;
		}

		if (!fileUtility.isFilePathValid(args[1])) {
			LOGGER.error("Change file path provided " + args[1] + " is invalid!");
			return;
		}

		LOGGER.info("Change file path validated successfully.");

		String sourceFilePath = args[0];
		String changeFilePath = args[1];
		String outputFilePath = args[2];

		try {
			fileUtility.validateJSONSyntax(sourceFilePath);
			MusicData musicData = fileUtility.extractMusicDataFromSourceFile(sourceFilePath);
			LOGGER.info("Extracted music data from source file successfully.");

			fileUtility.validateJSONSyntax(changeFilePath);
			PlayListChange playlistChanges = fileUtility.extractPlaylistChangesFromChangeFile(changeFilePath);
			LOGGER.info("Extracted change data from change file successfully.");

			List<Playlist> newPlaylists = playlistChanges.getPlaylists();
			musicPlaylistManagerService.setMusicData(musicData);
			int updatedCount = musicPlaylistManagerService.updatePlayLists(newPlaylists);

			LOGGER.info("Updated " + updatedCount + " playlists successfully.");

			MusicData updatedMusicData = musicPlaylistManagerService.getUpdatedMusicData();
			fileUtility.writeMusicDataToOutputFile(updatedMusicData, outputFilePath);

			LOGGER.info("Created output JSON file with updated music data successfully.");
			LOGGER.info("Completed successfully in " + (System.currentTimeMillis() - startTime) + " millisecond(s)");

		} catch (JsonSyntaxException jsonSyntaxException) {
			LOGGER.error("JSON syntax exception encountered! Exception: " + jsonSyntaxException.getMessage());
		} catch (JsonIOException jsonIOException) {
			LOGGER.error("JSON IO exception encountered! Exception: " + jsonIOException.getMessage());
		} catch (ExtractMusicDataFromFileException extractMusicDataFromFileException) {
			LOGGER.error("Unable to extract music data from source file! Exception: " + extractMusicDataFromFileException.getMessage());
		} catch (ExtractPlaylistChangesFromFileException extractPlaylistChangesFromFileException) {
			LOGGER.error("Unable to extract playlist changes from change file! Exception: " + extractPlaylistChangesFromFileException.getMessage());
		} catch (WriteToFileException writeToFileException) {
			LOGGER.error("Unable to write to the updated music data to the output file! Exception: " + writeToFileException.getMessage());
		} catch (InvalidPlaylistException invalidPlaylistException) {
			LOGGER.error("InvalidPlaylistException encountered! Exception: " + invalidPlaylistException.getMessage());
		}
	}

}
