# MusicPlaylistManager
<img src="https://i.imgur.com/6bI3R7z.jpg" alt="Chewbacca" width="40" height="40" align="center"/> This is a Spring Boot application to manage music playlists

####  Notes about the application
* The app needs Java version 8 inorder to run.
* I have used the gson library in order to serialize and deserialize the JSON files.
* I have assumed that the change file is in JSON format and contains only the changes made to the playlists.
* I have provided a sample change.json file containing addition, removal and update to the playlists. 
* In the case of deletion, the song_ids JSON array will be empty.
  ```javascript
      {
        "id" : "3",
        "user_id" : "7",
        "song_ids" : []
      },
  ```
* In the case of update the song ids present in the change file will represent the final state of the playlist.
* In order to avoid iterating through the Arraylist of playlists for each change to a playlist present in the changes file, 
  I have decided to go with a HashMap with the playlistId and UserId as a complex key and the List of songs as the value. 
  This would cut short the complexity from O(N) to O(1) (N is the total number of playlists) when it comes to fetching the songs based on userId and playlistId 
  and another O(1) to remove and add/update values in the map.
* Once all the changes are applied, reconversion from hashmap to JSON will take O(K) complexity where K is the number of entries in the hashmap.
* The source code and the tests are available for this project under https://github.com/Jiroda/MusicPlaylistManager/tree/feature/src

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

### Sacalability Discussion
* If the change file grows very large in the future / our mixtape libray has become so big.

  * I would consider storing the data inside a document based Database such as MongoDB or Cassandra rather than leaving it inside an in-memory hashmap.
  * My reasoning behind this suggestion is that, the data in itself is not heavily structured that it needs to fit a relational model/schema, 
    SQL databases is not always needed.
  * The query time needs to be fast and eliminating unnecessary JOINs will definitely prove to be useful when it comes to data updates and retrievals.
  * MongoDB and Cassandra are shown to be highly efficient in terms of retrieval and updates of bulk data especially when it comes to unstructured data (i.e, JSON docs)

* If we scale this application to million users concurrently hitting the system and are continously making changes to the playlists.

  * I would favor availability and partition tolerance from the CAP theorem and settle with eventual consistency.
  * Horizontal scaling / sharding will be prefered as opposed to vertical scaling. 
    Replicating the data across multiple hosts can be considered inorder to ensure high availability.
  * The ideal protocol would be HTTP and the CRUD operations on the playlists can be built as RESTful apis behind an API gateway which could then be load balanced.
  * REST resources will make access to data much more efficient, PUT POST and DELETE methods could be leveraged.
  * Each operation needs to be transactional to ensure the data is persisted correctly.
  * The unique identifier in this change file will be the playlistIds which inturn could have 1 to many songIds. Each playlist id is associated to an userId
    Going by the userId and then by playlistId could make data retrieval and updates much faster.
  * Usage of a message queue such as Apache Kafka or RabbitMQ could be considered for a Publisher-Subscriber model in case of a stream of changes hitting the system.
  * Consistent hashing is a must in order to service requests in a distributed way.

* If we need the application to be highly secure
 
  * I would consider an AuthZ and AuthN for added security. OAuth is wellknown and we can whitelist clients based on client Ids to avoid unauthorized access.
    The above features could very well be built in the REST web service layer
