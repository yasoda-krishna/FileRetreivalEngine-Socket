# Java Program for File Retrieval Engine using sockets

The **Client** serves as the interface for users to interact with the file retrieval engine. It connects to the server and sends commands for indexing files and searching for specific queries. Users can use commands such as:
 - index <path> to request the indexing of files located at a specified path.
 - search <query> to perform a search operation with a given query.
 - quit to terminate the client application.
 - The client communicates with the server to process these commands and displays the responses to the user.

The **IndexStore** is a server-side component responsible for managing the global index. It maintains a ConcurrentHashMap where keys are words found within the documents, and values are another map tracking the documents paths and the occurrence count of each word. The updateIndex method incorporates the counts from a newly indexed file into the global index, while the search method allows for efficient retrieval of documents based on query terms.

The **ProcessingEngine** handles the logic for indexing files and searching the index. It leverages multithreading to index files concurrently and utilizes the IndexStore for storing and querying the index. The indexing process involves walking through the specified directory, filtering for text files, and processing each file to update the global index. The search functionality allows for querying this index to find documents that match the search terms, ranking them by relevance.

The **Server** manages incoming connections from clients, processing requests for indexing and searching. It creates instances of the ProcessingEngine to handle these operations and responds to clients with the results of their queries. The server is designed to handle multiple clients simultaneously, ensuring efficient and concurrent processing of requests.

# Execution commands

**command to compile** : javac -d bin src/main/java/org/vishal/client/*.java src/main/java/org/vishal/server/*.java 

**command to execute** Mandatory to execute Server first 
java -cp bin server/Server

java -cp bin client/Client


**options**
index <path>  - Enter relative path with the index
search <query> - Enter the valid query
quit  - Exit the program
