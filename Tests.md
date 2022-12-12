Test 1 User Connects to the server
1. User runs the application
2. Clicks connect button 

<br>Expected : Login screen will appear prompting for user and password
<br>Result : Pass

Test 2 Register Account and Login to new account
1. User launches application and connects to server
2. User clicks register button
3. Enter your preferred username
4. Enter your preferred password
5. Enter your preferred email
6. Select your role in bottom left
7. Hit register
8. Hit logout button
9. Run application again
10. Connect to server
11. Enter new account username and password
12. Click Login

<br>Expected : Successful Login to empty main menu
<br>Result: Pass

Test 3 Register with username used before (Continuation from Test 2)
1. Open application
2. Connect to server and Click Register
3. Use the same username before and/or different password
4. Enter different email
5. Select role in bottom left
6. Click Register

<br>Expected: Error stating username has been taken
<br>Result: Pass

Test 4 Register and Select both buyer and Seller as your role
1. User launches application and connects to server
2. User clicks register button
3. Enter your preferred username
4. Enter your preferred password
5. Enter your preferred email
6. Select both roles in bottom left
7. Hit register

<br>Expected: Error message should show up saying select buyer or seller
<br>Resultt: Pass

Test 5 Register and enter invalid email formatting
1. User launches application and connects to server
2. User clicks register button
3. Enter your preferred username
4. Enter your preferred password
5. For email write a word with no @ symbol
6. Select one of the roles in the bottom left
7. Hit register

<br>Expected: Prompts to enter valid email
<br>Result: Pass

Test 6 User Login
1. User launches application and connects to server
2. Enter “buyer” as username
3. Enter “1” as password
4. Hit login button

<br>Expected : Verifies user credentials and loads homepage
<br>Result: Pass

Test 6 User Login Fail
1. User launches application and connects to server
2. Enter “buyer” as username
3. Enter “2” as password
4. Hit login button

<br>Expected : notify the user that the password is wrong
<br>Result: Pass

Test 7: Send Message
1. User launches application and connects to server
2. User enters username and password (username “buyer” and password “1”)
3. User hits login
4. User clicks search
5. User clicks selects drop down menu and selects “seller”
6. User clicks create new message to seller
7. User types message in field
7. User clicks send to seller
8. Logout and login again as “seller” (username “seller” and password “1”)
9. Click on “buyer”

<br>Expected: Message box should show message that was sent earlier
<br>Result: passed

Test 8: Delete and Edit Message
Steps:
1. User launches application.
2. User selects the username textbox.
3. User enters the username “buyer” via the keyboard.
4. User selects the password textbox and enters “1” via the keyboard.
5. User selects the "Login" button.
6. User selects the “Search” button
7. User selects “seller” from the drop down option
8. User selects the “Create New Message to seller” button
9. User types “Hello” into the message text box
10. User selects the “Send to seller” button
11. User selects the “Seller” option from the conversation bar on the left
12. User selects the “Hello” message
13. User selects the “Edit Message” button
14. User modifies the text field to “Hi”
15. User selects the “Finalize Edited Message” button
16. User selects the “Hi” message
17. User selects the “Delete Message” button
    
<br>Expected result: Application allows the user to edit and delete a message
    <br>Test Status: Passed.

Test 9 Exporting conversations
1. User launches application.
2. User selects the username textbox.
3. User enters the username “buyer” via the keyboard.
4. User selects the password textbox and enters “1” via the keyboard.
5. User selects the "Login" button.
6. User selects the “Search” button
7. User selects “seller” from the drop down option
8. User selects the “Create New Message to seller” button
9. Type in “Hello Seller” and click send to seller
10. On the top right of the frame  hit the export conversation button and then the export conversation with seller
11. Select a folder to copy conversation too by clicking on it for selection
12. Then click choose in the bottom right
13. Leave the application and navigate to the folder directory you selected
14. Find the export_conversations.csv and open it

<br>Expected: the file opens and holds the conversation
<br>Result: Pass

Test 10 Searching for a another buyer as a buyer
1. User launches application and connects to server
2. User clicks register button
3. Enter buyer2 as username
4. Enter “1” as password
5. Enter buyer2@gmail.com
6. Select buyer role in bottom left
7. Hit register
8. Click the search button and type in buyer
9. Click the search icon button

<br>Expected: No seller found message
<br>Result : Pass

Test 11 Searching for a seller as a seller
1. User launches application and connects to server
2. User clicks register button
3. Enter seller2 as username
4. Enter “1” as password
5. Enter seller2@gmail.com
6. Select seller role in bottom left
7. Hit register
8. Click the search button and type in seller
9. Click the search icon button

<br>Expected: No buyer found message
<br>Result: Pass


Test 12: Themes
Steps:
1. User launches application.
2. User selects the username textbox.
3. User enters the username “buyer” via the keyboard.
4. User selects the password textbox and enters “1” via the keyboard.
5. User selects the "Login" button.
6. User selects the “Search” button
7. User selects “seller” from the drop down option
8. User selects the “Create New Message to seller” button
9. User types “Theme Test” into the message text box
10. User selects the “Send to seller” button
11. User selects the “Seller” option from the conversation bar on the left
12. User selects the “Themes” button
13. User selects the “Coastal Reef” button 

<br>Expected result: Application changes the highlight of the messages and background to blue
<br>Test Status: Passed.

Test 13: Logout
1. User launches application and connects to server
2. Enter username and password
3. Click logout button

<br>Expected result: Application closes and you will have to login again once the application is re-opened
<br>Test Status: Passed.

		Test 14: Profile
User launches application and connects to server
1. Enter username and password
2. Click profile button 

<br>Expected Result: Menu with profile options should come up
   <br>Test Status: Passed

Test 15: Censored Words
1. User launches application and connects to server
2. Enter username and password
3. Click profile button
4. Type word you’d like to censor into text box
5. Click add censored word
6. Change censor pattern with any character you’d like
7. Exit out of profile menu
8. Send new message to other account containing the word you just censored
9. Click on profile
10. Type that same censored word back into the text bock
11. Click remove censored word
12. Exit out of profile 

<br>Expected Result: In the message box, the word you added to censored words should be replaced by the pattern you selected and after removing censored word, the word in the message box should be back to normal
<br>Test Status: Passed

Test 16 Change censor pattern
1. User launches application and connects to server
2. Enter username and password (“buyer” and “1” respectively) and click login
3. Click search icon and type in seller
4. Click profile button
5. Type word you’d like to censor into text box
6. Click add censored word
7. Change censor pattern with any character you’d like
8. Exit out of profile menu
9. Send new message to other account containing the word you just censored
10. Click on profile
11. Type that same censored word back into the text bock
12. Click remove censored word
13. Exit out of profile
<br>Expected Result: You have changed censor patter
<br>Test Status: Passed


Test 17: Block and unblock user
1. User launches application and connects to server
2. Enter username and password (“buyer” and “1” respectively)
3. Click on profile button
4. Select the dropdown menu with the list of users
5. Select “seller”
6. Click add block user
7. Logout of buyer
8. Run the application again and login as seller this time (username “seller” and password “1”)
9. Send a message to buyer

<br>Expected Result: Dialog should appear telling you that this user has blocked you
<br>Test Status: Passed

1. Log out of seller
2. Run application again and log back in as buyer
3. Click profile
4. Click on the dropdown of users and select “seller”
5. Click unblock user
6. Log out
7. Run application again
8. Log back in as seller
9. Click on buyer
10. Click new message
11. Send message to buyer

<br>Expected Result: Message should send with no dialog this time
<br>Test Status: Passed

Test 18: Add and remove invisible user
1. User launches application and connects to server
2. Enter username and password (“buyer” and “1” respectively)
3. Click on profile button
4. Select the dropdown menu with the list of users
5. Select “seller”
6. Click add invisible user
7. Log out
8. Launch application
9. Log back in as seller this time (username “seller” and password “1”)
10. Click on the search button
11. Click on the dropdown menu of users

<br>Expected Result: “buyer” should not appear in the list of users
<br>Test Status: Pass

1. Log out
2. Launch application
3. Log in as buyer again
4. Click on profile
5. Click the dropdown for invisible users
6. Select “seller”
7. Click un-invisible user
8. Log out
9. Launch application
10. Login as seller again
11. Click on search button
12. Click dropdown menu of users

<br>Expected Result: “buyer” should appear in list of users again
<br>Test Status: Pass

		Test 19: Change Username
1. User launches application and connects to server
2. Enter username and password (“buyer” and “1” respectively)
3. Click login
4. Click profile
5. In change username field, type a new username
6. Click change username
7. Exit out of the profile page
8. Send a message to seller

<br>Expected result: Your name should be listed as the username you changed it to when you send the message.
<br>Test Status: Pass

Test 20: Change Email
1. User launches application and connects to server
2. Enter username and password (“buyer” and “1” respectively)
3. Click login
4. Click profile
5. In change email field, type a new email
6. Click change email

<br>Expected Result: Email field at top of profile page should change to new email you chose
<br>Test Status: Pass

Test 21: Delete Account
1. User launches application and connects to server
2. Click register
3. Create a new account with the following fields: username - test, password -1, email - test@test.com, buyer
4. Click register
5. Click on profile
6. Click on delete account
7. Enter password (“1”)
8. Exit out of the profile page
9. Click logout
10. Launch application again
11. Try to login with the test account you created

<br>Expected Result: You should get an error that the username does not exist
<br>Test Status: Pass

Test 22: Add store to a seller
1. User launches application and connects to server
2. Enter username and password (“seller” and “1” respectively)
3. Click Login
4. Click on profile button
5. Next to the add store button type in “store1”  and click add store button
6. Type in store2 in the same box and hit click add store button again 

<br>Expected Result: store1 and store2 should be shown next to the add store textfield and button
<br>Result: Pass

Test 23 Message store continuation from task 19
1. Logout if you are logged in as a seller
2. Run application and connect to server
3. Enter user as “buyer” and “1” as username and password and click login
4. Click the search button and search for “seller”
5. After searching the stores will be on the side bar on the left
6. Select store1 and then “Create New Message to Store” button
7. Type in “hello” and then send to store  button

<br>Expected: The message should be sent to the owner of the store - “seller”
<br>Result: Pass

Test 24: Two users cannot be logged in at same time
1. Run the application and connect to server
2. Click Register and enter “test” as username and “1” as password
3. Make the email test@gmail.com and select the role as buyer
4. Select register and you should be logged in
5. Open another computer/instance and run the application
6. Connect to server on the new instance
7. Attempt to login to the test account using the same credentials “test” and “1”
8. Click login

<br>Expected: Error message should pop up on the attempted login saying your account is already logged in somewhere else
<br>Result : Pass

Test 25 Real Time Updates (Messaging) (continuation of Test 21)
1. From the instance with the error message go and click register
2. Enter the username as “test2” and password as “1”
3. Make the role seller
4. Click on search and then select “test” which is the buyer you made in the last test
5. Now click the “Create the New Message to buyer from list” button and then type in Hi and then click send to test

<br>Expected : The test client should instantly show the message as a real time update
<br>Result: Pass

Test 23 Sending message to user that does not exist
1. Run the application and click connect button to connect to the server
2. Enter “buyer” and “1” as username and password respectively and click login
3. Click the search icon up top, replace the search bar with the text “randomusernamethatdoesntexist”
4. Click “Create New Message to Seller '' and type hi in the big text box that came up and then click the send to randomusernamethatdoesntexist button on the left.

<br>Expected: Error Pane should pop up stating there was a SEND_MESSAGE_USER error  <br>
<br>Test: Pass



