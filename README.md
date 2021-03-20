# MusicPlaylistManager
<img src="https://i.imgur.com/6bI3R7z.jpg" alt="Chewbacca" width="40" height="40" align="center"/> This is a Spring Boot application to manage music playlists

####  Notes about the application
* The app needs Java version 8 inorder to run.
* I have used the gson library in order to serialize and deserialize the JSON files.
* I have assumed that the change file is in JSON format and contains only the changes made to the playlists.
* I have provided a sample change.json file containing addition, removal and update to the playlists. 
* In the case of deletion, the song_ids JSON array will be empty.
* In the case of update the song ids present in the change file will represent the final state of the playlist.
* In order to avoid iterating through the Arraylist of playlists for each change to a playlist present in the changes file, 
  I have decided to go with a HashMap with the playlistId and UserId as a complex key and the List of songs as the value. 
  This would cut short the complexity from O(N) to O(1) (N is the total number of playlists) when it comes to fetching the songs based on userId and playlistId 
  and another O(1) to remove and add/update values in the map.
* Once all the changes are applied, reconversion from hashmap to JSON will take O(K) complexity where K is the number of entries in the hashmap.

####  How to run the application
* Download the project.zip file found here https://github.com/Jiroda/MusicPlaylistManager/blob/feature/project.zip
* The spring boot application takes 3 parameters as shown below:
```
1. Path to the source JSON file 
   /Users/skalidass/project/files/mixtape.json or C:\project\files\mixtape.json
2. Path to the change JSON file 
   /Users/skalidass/project/files/change.json or C:\project\files\change.json
3. Path to the output JSON file
   /Users/skalidass/project/files/output.json or C:\project\files\output.json
```
![how to run jar](https://github.com/Jiroda/MusicPlaylistManager/blob/feature/run-jar.png)
