package com.highspot.musicplaylistmanager.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.highspot.musicplaylistmanager.exception.ExtractMusicDataFromFileException;
import com.highspot.musicplaylistmanager.exception.ExtractPlaylistChangesFromFileException;
import com.highspot.musicplaylistmanager.exception.WriteToFileException;
import com.highspot.musicplaylistmanager.model.MusicData;
import com.highspot.musicplaylistmanager.model.PlayListChange;

import org.springframework.stereotype.Component;

@Component
public class FileUtility {
	private Gson gson = new Gson();
	private static final TypeAdapter<JsonObject> strictGsonObjectAdapter = new Gson().getAdapter(JsonObject.class);
	private static final String JSON_FILE_EXTENSION = ".json";

	public FileUtility() {
	}

	/**
	 * @param filePathString
	 * @return boolean
	 */
	public boolean isFilePathValid(String filePathString) {
		if (filePathString.endsWith(JSON_FILE_EXTENSION)) {
			File f = new File(filePathString);
			if (f.exists() && !f.isDirectory()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param sourceFilePath
	 * @return MusicData
	 * @throws ExtractMusicDataFromFileException
	 */
	public MusicData extractMusicDataFromSourceFile(String sourceFilePath) throws ExtractMusicDataFromFileException {
		MusicData musicData = null;
		try (Reader reader = new FileReader(sourceFilePath)) {
			musicData = getMusicDataFromFile(reader, sourceFilePath);
		} catch (IOException e) {
			throw new ExtractMusicDataFromFileException("Unable to extract music data from the source file! Filepath: "
					+ sourceFilePath + ". Exception: " + e.getMessage());
		}
		return musicData;
	}

	/**
	 * @param changesFilePath
	 * @return PlayListChange
	 * @throws ExtractPlaylistChangesFromFileException
	 */
	public PlayListChange extractPlaylistChangesFromChangeFile(String changesFilePath)
			throws ExtractPlaylistChangesFromFileException {
		PlayListChange playlistChanges = null;
		try (Reader reader = new FileReader(changesFilePath)) {
			playlistChanges = getPlaylistChangesFromFile(reader, changesFilePath);
		} catch (IOException e) {
			throw new ExtractPlaylistChangesFromFileException(
					"Unable to extract playlist changes from the changes file! Filepath: " + changesFilePath
							+ ". Exception: " + e.getMessage());
		}
		return playlistChanges;
	}

	/**
	 * @param reader
	 * @param sourceFilePath
	 * @return MusicData
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public MusicData getMusicDataFromFile(Reader reader, String sourceFilePath) throws JsonSyntaxException, JsonIOException {
		MusicData musicData = gson.fromJson(reader, MusicData.class);
		return musicData;
	}

	/**
	 * @param reader
	 * @param changesFilePath
	 * @return PlayListChange
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public PlayListChange getPlaylistChangesFromFile(Reader reader, String changesFilePath)
			throws JsonSyntaxException, JsonIOException {
		PlayListChange playlistChanges = gson.fromJson(reader, PlayListChange.class);
		return playlistChanges;
	}

	/**
	 * @param musicData
	 * @param outputFilepath
	 * @throws WriteToFileException
	 */
	public void writeMusicDataToOutputFile(MusicData musicData, String outputFilepath) throws WriteToFileException {
		try (FileWriter writer = new FileWriter(outputFilepath)) {
			gson.toJson(musicData, writer);
		} catch (IOException e) {
			throw new WriteToFileException(
					"Unable to write music data as JSON to the file! Exception: " + e.getMessage());
		}
	}

	/**
	 * @param filePath
	 * @throws JsonSyntaxException
	 */
	public void validateJSONSyntax(String filePath) throws JsonSyntaxException{
		try {
			try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
				JsonObject result = strictGsonObjectAdapter.read(reader);
				reader.hasNext(); // throws on multiple top level values
			}
		} catch (IOException e) {
			throw new JsonSyntaxException("Error in file " + filePath + ". Exception: " + e);
		}
	}
}
