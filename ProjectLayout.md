# Rough Layout of Project 4 Option 2

1. [ ] User Functionality - ___vacant___
   1. [x] Super class placeholder User(buyer and seller inherit from user) Lincoln
      1. include common data and methods that both buyer and the seller have
      2. User should have number of messages in one of the field and a static public method to increment message as a user construct a new Message
      3. Seller should have store array field
   2. [x] FullUser is a utility class that contains User. Arthur __This is the class that will interact with the user. So all
      functionality methods should be all public(besides helper methods)__
      1. Fields: User, list of Conversations that this user is associated with. _TODO: someway to notify the target
         about the conversation_
      2. login checks
      3. registration check - check email, check duplicate username - need list of username - PublicInformation
      4. message manipulation(create, edit, delete, print(as strings)
         1. When creating a message, make sure to call the receiver receive method!
         2. Need to create a reception method. Public Information will have a list of _FullSeller,_ so you can call the
            FullSeller's receive method.
      5. logouts(has to notify all fields that a user has loggout, serialize all data(_Can be implemented in Main.
         Awaits further discussion_)
      6. Blocking: can be 2 arrays - one block, another invisible assoc. with a __protected__ method that checks whether
         a user is invisible to them
   4. [ ] FullBuyer utility class(NOT Buyer.java) that takes in a buyer instance -
      1. Method that return a list of stores for user to select - PublicInformation
      2. Also can search for a specific seller (?WHAT?)
      3. list of stores by number of messages received - PublicInformation
      4. list of stores by the number of messages that particular customer has sent - PublicInformation
      5. Sorting algorithm - should be in PublicInformation as well
   5. [X] FullSeller extends FullUser(NOT Seller.java) that takes in a seller instance
      1. view a list of customers to select an individual to message - PublicInformation
      2. Sellers should be able to search for a specific customer to message. (This is fine) - can be implemented in
         either pub / FullSeller
      3. Data will include a list of customers with the number of messages they have sent
      4. Most common words in overall messages
      5. Sorting algorithm - should be in PublicInformation as well
2. [X] Message functionality - Yulin Lin
   1. [ ] Message portion
      1. [ ] Message Object each serves as basis of message transfer
         1. each contains a buyer and a seller
         2. each user should a collection of messages assoc. with them
         3. Participants, Message sender, timestamp, and contents.
         4. probably a good idea to implement is as packages so no need to search for every new conversation
            1. However, would need someway to sum them up
3. [X] Files - Merge with functionality
   1. [X] export message
      1. could be utility method reside in each message
   2. [X] import message
      1. again utility method
   3. Pretty light work load but would need a template at least from functionality group
4. [ ] Statistics - Samson - _look at user functionality for requirement_
   1. Prob. utility classes since they are pretty big
   2. [ ] Can be summed up into a single class or at least a super class with 2 children
      1. list of buyer/seller with number of message received/sent and message sent/common word
      2. sort
   3. probably the most challenging part
5. [x] Blocking - Arthur
   1. EASY!
   2. can be 2 simple arrays of block and invisible or a class
6. [ ] Connecting the dots
   1. [ ] Main class
      1. page for user login
      2. dashboard
         1. stats
         2. list of seller/buyers
         3. messages open
      3. Message
         1. messaging body
         2. options 
   2. could have some subclasses to sum different section together to make it more presentable

note:
- Data must persist regardless of whether or not a user is connected. If a user disconnects and reconnects, their data should still be present. If a user creates an account and then closes the application (it is no longer running), they should still be able to log in when running it again.
- Descriptive errors should appear as appropriate. For example, if someone tries to log in with an invalid account. The application should not crash under any circumstances. 
