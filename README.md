# marketplace messaging system
## Notice
This is the group project for CS 180(Project 4 and 5). Lab 001 - Team 2
## Welcome!
This GitHub repository contains code for a marketplace messaging app. It has features include: 
* Allow communication between buyers and sellers with an account
* Sellers can create stores
* Ability to create, edit, and delete messages 
* Buyers can view a list of stores and select one to message, or they can choose to search for a seller's username 
to message directly
* Sellers can view a list of buyers and select one to message, or they can choose to search for a buyer's username
* Users can also send text files as messages and download their conversations as a csv file
* Buyers can view a dashboard of stores by number of messages they received or number of message they send
* Sellers can view a dashboard of their own stores and number of messages they have send and most common words in them 
for each stores
* Users can also block another user or become invisible to them
* Conversations with new messages will be marked and automatically put to the top
<br><br>
New features update from Project 5:
* A server-client model implementation
* A server that's event-driven and capable of handling multiple clients
* A client that sends the requests to the server according to client needs(also event-driven)
* GUI class that renders the screen(Also event-driven)
* ___REAL TIME UPDATE (Woo Hoo!!!)___

## Run the app!

If you are using an IDE like Intellij, you can simply download the project, configure the Java JDK(recommend the newest
Java JDK), add a run configuration for server (ServerCore.java) and client (Init.java) (PS: make sure to make the client
configuration allows for multiple instances if you want to test real-time update in one machine), and, finally
put it in your ide and run the Main class for console integration and run both the server and client config for 
server-client model and a GUI(It's BEAUTIFUL!)

## Additional Class Description from Project 5
### Server
#### ServerCore

server core is the main server code that handles IO between the server and the client. It uses Java.nio.channels package
that provides a ServerSocketChannel and SocketChannel(for ServerSocket and Socket from java.net) and a multiplexer 
called Selector that allow for event-driven code for the channels. The channels have been configured into non-blocking 
mode(the server does not wait until a IO operation is complete, it moves on) that minimizes response time from the 
server

##### Dependencies
* MessageSystem as field
* DataPacket: uses its method
* import Java.nio package
* ArrayBlockingQueue for thread-safe queue and de-queue (write operation)

#### Message System(and UserProfile, PublicInfo, MessageFunctionalites)

Basically an abstraction of the Message system (the core packages) so it's easier for server to process

##### Dependencies
* A LOT! Like the whole UserCore and MessageCore packets, but they are separated into several classes:
  * UserProfile
  * PublicInfo
  * MessageFunctionalities
* NotificationFactory(uses its static method to tell the server to also send this packet to another client)

#### NotificationFactory

Spawns a thread which allows one client to send messages to another client or broadcast to all.
<br>
The way it does this is by looping through all the keys in the selector and find the desired one, or just send it to 
everyone
##### Dependencies
* ServerCore (Complement ServerCore)
* import Thread

### Protocol
#### Enums
* ProtocolRequestTypes that contains all the types of request to the server
* ProtocolResponseTypes that contains all the types of normal response from the server
* ProtocolErrorType that contains all the exceptions caused by the client, each constant corresponding to a class of 
exception
#### DataPacket

The packet that client uses to send request to the server. It supplies a static method of serializing itself and another
static method for deserializing itself

##### Attributes
* ProtocolRequestTypes type
* String[] of parameters

##### Dependencies
* implements Externalizable to allow for custom serialization instead of using Reflection by default; way faster 
serialization

#### ResponsePacket

The packet by the server to send normal responses to the client. It supplies a static method of serializing itself

##### Attributes
* ProtocolResponseTypes type
* String[] of responses

##### Dependencies
* implements Externalizable to allow for custom serialization instead of using Reflection by default; way faster
  serialization

#### ErrorPacket

The packet by the server to send abnormal responses(exceptions) to the client. 
It supplies a static method of serializing itself

##### Attributes
* ProtocolErrorTypes error_type that corresponds to an exception
* ProtocolResponseTypes request_type that caused the error
* String of error message

##### Dependencies
* implements Externalizable to allow for custom serialization instead of using Reflection by default; way faster
  serialization

#### PacketDeserializer

Used by client to deserialize the response packet or error packet. Uses ByteArrayInputStream and ObjectInputStream to 
deserialize the bytes and store the remaking bytes into a byte[] for the next deserialization

### Client
#### ClientCore

server core is the main client code that handles IO between the server and the client. Also uses all the Java.nio fun 
stuff.
<br>
It also supplies method that allows listeners to read from read queue and allows client to add to the write queue 
anytime

##### Dependencies
* PacketDeserializer as field
* import Java.nio package
* ArrayBlockingQueue for thread-safe queue and de-queue (both read and write)

#### PacketAssembler

Supply a static method to assemble the Request Packet and serialize it, so it's ready for the client to send to the 
server. (There's also a method that convert a array string to an array...I put it there because I am too lazy to create
another class for it :> )

#### Listener

A basic Listener that listens for a specific response or an error caused by a specific request

##### Dependencies
* Protocol Package
* import Thread
* ClientCore as field

#### Atrtibutes
* ProtocolResponseType response
* ProtocolRequestType errorRequest
* ClientCore client

#### GUI (Init, Login, Register, GUI.Profile)

Basically renders the GUI for the client, send packet using PacketAssembler, and supplies methods for AsyncListener to 
call to rerender the screen once a response or an error is received

##### Dependencies
* Client and Protocol packet
* Javax.swing library

#### GUI.AsyncListener

An Async Listener that runs in the background that listens for all packets received from client core and invoke the 
corresponding methods in the EDT to rerender the screen with new information

##### Dependencies
* extends Javax.swing.SwingWorker<T,V>
* Protocol Packet to access the infos 

## Class Description
### UserCore
#### User.java and its children

User and its children Buyer and Seller are served as a stand in class for classes other than UserCore to use.
They only contain attributes the makes up a user - the username, password, email, and role - which constitute a
unique identifier for other classes to use them, and status indicator login status and waiting for deletion status.
User classes does not have any utility methods; it only has protected simple
getters and setters for classes in UserCore and public static getters for username, roles, login status which are public
information that can be shared. User also has a equal method and toString method to help aid other classes to identify
the user. **User and its children has protected constructors to prevent unauthorized instantiation.**
<p>
Buyer and Seller inherit the User class, they are there to simplify user creation as they don't need to take Role as 
one of its parameter during creation and limit the fields that use them to only contain buyer/seller instances.

#### Store.java

A class that represent store and contains attributes of a store include owner, store names, and number of message
received by each customer

##### Test Done To Verify Functionalities

##### TestCasesMethodsImplementation

* testCreateStore
* testTakenStoreName

### MessageCore

#### Message

The message class represents the individual messages each user send in a conversation.

##### Dependencies

* Uses User class as field

##### Attributes

* sender
* receiver
* content of the message
##### Functionalities
The message manipulation functionalities starts here and gets encapsulated by each class that depends on Message
* Message Creation, editing, and deletion
  * Deletion will only delete the message for the user requesting the deletion
  * If both sender and the participant delete the message, the deletion method will notify the caller that this instance
can be destroyed to conserve memory. 
* CSV export and string export
* Notify the target that this a new message
* Limit the access to all the functionalities to only the sender and the buyer
  * This effort is also repeated in higher classes like conversation which again limit the access to the 2 participants
and again in FullUser by not allowing messages to pass around to other user to use
##### Test Done To Verify Functionalities
###### TestCasesMethodsImplementation
* testEditMessage
* testDeleteMessage
* testMessagesAccess

#### Conversation
The Conversation class represents a collection of messages between a seller and a buyer
##### Dependencies
* Uses Buyer class as a field
* Uses Seller class as a field
* Uses ArrayList of Messages as field
##### Attributes
* buyer
* seller
* collection of messages
* a flag to indicate whether there's a new message for seller
* a flag to indicate whether there's a new message for buyer
##### Functionalities
The conversation encapsulate all messages functionalities plus additional include: 
* a method that automatically puts a message when a participant deleted his/her account
* ability to notify the classes implement it there is a new message for seller/buyer
* automatically puts new messages to the top
* Limit the conversation to only be between a buyer and a seller
##### Test Done To Verify Functionalities
###### TestCasesMethodsImplementation
* testEditMessage
* testDeleteMessage
* testMessagesAccess
* testNewMessagesInConversation
* testMessageBuyerToSeller
* testMessageBuyerToBuyer
* testMessageSellerToSeller

### UserCore

#### FullUser and its children

FullUser and its children combine User classes attributes and message functionalities and provide an interface
for user to interact.

#### FullUser

Full User class represent common attributes and functionalities that each Buyer and Seller share like:

* Attributes in User - username, password, email, login status, waiting for deletion status
* message creation, edition, deletion
* Provide a way for user to interact with their profile

##### Dependencies

* Uses User class as field
* Uses Conversation class as field
* Uses Public Information class methods

##### Attributes

* User
* list of conversations
* list of blocked user
* list of make invisible user
* list of filtered word
* censoring character
* flag for censoring mode

##### Functionalities

The Full User implement all conversation functionalities plus additional include:

* converting files to string for message manipulations
* provide a way to notify and link 2 users in the conversation
* Print a list of conversation by the titled by the other parties' name for user to select
* Display the conversation based on user selection
* Provide a password check method to verify user's identity
* Print the profile information
* Uses export to csv methods in conversation to output csv to a file designated by the user

##### Test Done To Verify Functionalities

* TestCasesMain

###### TestCasesMethodsImplementation

* testFilterMessages
* testInvisible
* testLoginSuccess
* testWrongUsername
* testBlocking
* testWrongPassword
* testLogout
*

#### FullBuyer

FullBuyer class represents attributes and functionalities associated with the buyer

##### Dependencies

* Uses Full User as parent
* Uses Public Information Methods

##### Attributes

* list of store messaged and the frequency list

##### Functionalities

* create a buyer
* message store
* message seller
* view buyer dashboard

##### Test Done To Verify Functionalities

* TestCasesMain

###### TestCasesMethodsImplementation

* testBuyerDashBoard

#### FullSeller

FullSeller class represents attributes and functionalities associated with the seller

##### Dependencies

* Uses Full User as parent
* Uses Public Information Methods

##### Attributes

* a static list of stop words in English to filter out stop words for common word method

##### Functionalities

* create a seller
* create a store
* message buyer
* view seller dashboard

##### Test Done To Verify Functionalities

* TestCasesMain

###### TestCasesMethodsImplementation

* testSellerDashBoard
* testMostFrequentWord
* testCreateStore

#### Public Information

Public information contains a collection of static utility methods for accessing and exchanging public information
include all the sellers, buyers, stores, and used usernames

##### Dependencies

* Uses Full Seller as field
* Uses Full Buyer as field
* Uses Store as field

##### Attributes

* a static list of all the Full Buyers
* a static list of all the Full Sellers
* a static list of all the usernames in
* a static list of stores

##### Functionalities

* Data persistence by serializing all static lists
* Translate a user instance to FullUser
* provide a global login and logout method
* Find the owner of a store
* Sort all stores by the messages they received
* Method to check for duplication during account creation and edition
* Search for seller and buyer by usernames
* Find the seller, buyer, and store by putting in the exact name for them
* Delete user and recover user
* And various unsafe debugging methods

##### Test Done To Verify Functionalities

* TestCasesMain

###### TestCasesMethodsImplementation

* testAccountDeletion
* testAccountRecovery
* testDataPersistence

# list of students submit part of assignment
* Yulin Lin - submit codes
* Teresa Wan - submit report
