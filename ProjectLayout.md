# Rough Layout of Project 4 Option 2

1. [ ] User - ___vacant___
   1. [ ] Super class User
      1. include common data and methods that both buyer and the seller have
   2. [ ] Buyer extends User
   3. [ ] Seller extends User
2. [ ] Account functionality - ___vacant___
   1. [ ] Login Portion
      1. [ ] usrname & pwd class/interface
         1. each user should have their usrnam and pwd 
   2. [ ] Message portion
      1. [ ] Message Object each serves as basis of message transfer
         1. each contains a buyer and a seller
         2. each user should a collection of messages assoc. with them
         3. Participants, Message sender, timestamp, and contents.
         4. probably a good idea to implement is as packages so no need to search for every new conversation
            1. However, would need someway to sum them up
3. [ ] Files - ___vacant___
   1. [ ] export message
      1. could be utility method reside in each message
   2. [ ] import message 
      1. again utility method 
   3. Pretty light work load but would need a template at least from functionality group
4. [ ] Statistics - ___vacant___
   1. Prob. utility classes since they are pretty big
   2. [ ] Can be summed up into a single class or at least a super class with 2 children
      1. list of buyer/seller with number of message received/sent and message sent/common word
      2. sort
   3. probably the most challenging part
5. [ ] Blocking - ___vacant___
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
