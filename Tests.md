Test 1 User Connects to the server
User runs the application
Clicks connect button
Expected : Login screen will appear prompting for user and password
Result : Pass

Test 2 Register Account and Login to new account
User launches application and connects to server
User clicks register button
Enter your preferred username
Enter your preferred password
Enter your preferred email
Select your role in bottom left
Hit register
Hit logout button
Run application again
 Connect to server
Enter new account username and password
 Click Login
Expected : Successful Login to empty main menu
Result: Pass

Test 3 Register with username used before (Continuation from Test 2)
Open application
Connect to server and Click Register
Use the same username before and/or different password
Enter different email
Select role in bottom left
Click Register
Expected: Error stating username has been taken
Result: Pass

Test 4 Register and Select both buyer and Seller as your role
User launches application and connects to server
User clicks register button
Enter your preferred username
Enter your preferred password
Enter your preferred email
Select both roles in bottom left
Hit register
		Expected: Error message should show up saying select buyer or seller
		Resultt: Pass

Test 5 Register and enter invalid email formatting
User launches application and connects to server
User clicks register button
Enter your preferred username
Enter your preferred password
For email write a word with no @ symbol
Select one of the roles in the bottom left
Hit register
		Expected: Prompts to enter valid email
		Result: Pass

Test 6 User Login
User launches application and connects to server
Enter “buyer” as username
Enter “1” as password
Hit login button
		Expected : Verifies user credentials and loads homepage
		Result: Pass

Test 6 User Login Fail
User launches application and connects to server
Enter “buyer” as username
Enter “2” as password
Hit login button
		Expected : notify the user that the password is wrong
		Result: Pass

Test 7: Send Message
User launches application and connects to server
User enters username and password (username “buyer” and password “1”)
User hits login
User clicks search
User clicks selects drop down menu and selects “seller”
User clicks create new message to seller
User types message in field
User clicks send to seller
Logout and login again as “seller” (username “seller” and password “1”)
Click on “buyer”
Expected: Message box should show message that was sent earlier
Result: passed

Test 8: Delete and Edit Message
Steps: 
User launches application. 
User selects the username textbox. 
User enters the username “buyer” via the keyboard. 
User selects the password textbox and enters “1” via the keyboard. 
User selects the "Login" button. 
User selects the “Search” button
User selects “seller” from the drop down option
User selects the “Create New Message to seller” button
User types “Hello” into the message text box
 User selects the “Send to seller” button
 User selects the “Seller” option from the conversation bar on the left
 User selects the “Hello” message
 User selects the “Edit Message” button
 User modifies the text field to “Hi” 
 User selects the “Finalize Edited Message” button
 User selects the “Hi” message
 User selects the “Delete Message” button
Expected result: Application allows the user to edit and delete a message
Test Status: Passed.

Test 9 Exporting conversations
User launches application. 
User selects the username textbox. 
User enters the username “buyer” via the keyboard. 
User selects the password textbox and enters “1” via the keyboard. 
User selects the "Login" button. 
User selects the “Search” button
User selects “seller” from the drop down option
User selects the “Create New Message to seller” button
Type in “Hello Seller” and click send to seller
 On the top right of the frame  hit the export conversation button and then the export conversation with seller
 Select a folder to copy conversation too by clicking on it for selection
 Then click choose in the bottom right
 Leave the application and navigate to the folder directory you selected
 Find the export_conversations.csv and open it 
Expected: the file opens and holds the conversation
Result: Pass

Test 10 Searching for a another buyer as a buyer
User launches application and connects to server
User clicks register button
Enter buyer2 as username
Enter “1” as password
Enter buyer2@gmail.com
Select buyer role in bottom left
Hit register
Click the search button and type in buyer
Click the search icon button
Expected: No seller found message
Result : Pass

Test 11 Searching for a seller as a seller
User launches application and connects to server
User clicks register button
Enter seller2 as username
Enter “1” as password
Enter seller2@gmail.com
Select seller role in bottom left
Hit register
Click the search button and type in seller
Click the search icon button
Expected: No buyer found message
Result: Pass


Test 12: Themes
Steps: 
User launches application. 
User selects the username textbox. 
User enters the username “buyer” via the keyboard. 
User selects the password textbox and enters “1” via the keyboard. 
User selects the "Login" button. 
User selects the “Search” button
User selects “seller” from the drop down option
User selects the “Create New Message to seller” button
User types “Theme Test” into the message text box
 User selects the “Send to seller” button
 User selects the “Seller” option from the conversation bar on the left
 User selects the “Themes” button
 User selects the “Coastal Reef” button
Expected result: Application changes the highlight of the messages and background to blue 
Test Status: Passed.

Test 13: Logout
User launches application and connects to server
Enter username and password
Click logout button
Expected result: Application closes and you will have to login again once the application is re-opened
Test Status: Passed.
		
		Test 14: Profile
User launches application and connects to server
Enter username and password
Click profile button
Expected Result: Menu with profile options should come up
Test Status: Passed

Test 15: Censored Words
User launches application and connects to server
Enter username and password
Click profile button
Type word you’d like to censor into text box
Click add censored word
Change censor pattern with any character you’d like
Exit out of profile menu
Send new message to other account containing the word you just censored
Click on profile
Type that same censored word back into the text bock
Click remove censored word
Exit out of profile
Expected Result: In the message box, the word you added to censored words should be replaced by the pattern you selected and after removing censored word, the word in the message box should be back to normal
Test Status: Passed

Test 16 Change censor pattern
User launches application and connects to server
Enter username and password (“buyer” and “1” respectively) and click login
Click search icon and type in seller


Click profile button
Type word you’d like to censor into text box
Click add censored word
Change censor pattern with any character you’d like
Exit out of profile menu
Send new message to other account containing the word you just censored
Click on profile
Type that same censored word back into the text bock
Click remove censored word
Exit out of profile



Test 17: Block and unblock user
User launches application and connects to server
Enter username and password (“buyer” and “1” respectively)
Click on profile button
Select the dropdown menu with the list of users
Select “seller”
Click add block user
Logout of buyer
Run the application again and login as seller this time (username “seller” and password “1”)
Send a message to buyer
Expected Result: Dialog should appear telling you that this user has blocked you
Test Status: Passed
Log out of seller
Run application again and log back in as buyer
Click profile
Click on the dropdown of users and select “seller”
Click unblock user
Log out
Run application again
Log back in as seller
Click on buyer
Click new message
Send message to buyer
Expected Result: Message should send with no dialog this time
Test Status: Passed

Test 18: Add and remove invisible user
User launches application and connects to server
Enter username and password (“buyer” and “1” respectively)
Click on profile button
Select the dropdown menu with the list of users
Select “seller”
Click add invisible user
Log out
Launch application
Log back in as seller this time (username “seller” and password “1”)
Click on the search button
Click on the dropdown menu of users
		Expected Result: “buyer” should not appear in the list of users
		Test Status: Pass
Log out
Launch application
Log in as buyer again
Click on profile
Click the dropdown for invisible users
Select “seller”
Click un-invisible user
Log out
Launch application
Login as seller again
Click on search button
Click dropdown menu of users
Expected Result: “buyer” should appear in list of users again
Test Status: Pass

		Test 19: Change Username
User launches application and connects to server
Enter username and password (“buyer” and “1” respectively)
Click login
Click profile
In change username field, type a new username
Click change username
Exit out of the profile page
Send a message to seller
Expected result: Your name should be listed as the username you changed it to when you send the message.
Test Status: Pass

Test 20: Change Email
User launches application and connects to server
Enter username and password (“buyer” and “1” respectively)
Click login
Click profile
In change email field, type a new email
Click change email
Expected Result: Email field at top of profile page should change to new email you chose
Test Status: Pass

Test 21: Delete Account
User launches application and connects to server
Click register
Create a new account with the following fields: username - test, password -1, email - test@test.com, buyer
Click register
Click on profile
Click on delete account
Enter password (“1”)
Exit out of the profile page
C

Test 19: Add store to a seller
User launches application and connects to server
Enter username and password (“seller” and “1” respectively)
Click Login
Click on profile button
Next to the add store button type in “store1”  and click add store button
Type in store2 in the same box and hit click add store button again
		Expected Result: store1 and store2 should be shown next to the add store textfield and button
		Result: Pass

Test 20 Message store continuation from task 19
Logout if you are logged in as a seller
Run application and connect to server
Enter user as “buyer” and “1” as username and password and click login
Click the search button and search for “seller”
After searching the stores will be on the side bar on the left
Select store1 and then “Create New Message to Store” button
Type in “hello” and then send to store  button
		Expected: The message should be sent to the owner of the store - “seller”
		Result: Pass	

Test 21: Two users cannot be logged in at same time
Run the application and connect to server
Click Register and enter “test” as username and “1” as password
Make the email test@gmail.com and select the role as buyer
Select register and you should be logged in
Open another computer/instance and run the application
Connect to server on the new instance
Attempt to login to the test account using the same credentials “test” and “1”
 Click login
Expected: Error message should pop up on the attempted login saying your account is already logged in somewhere else
Result : Pass

Test 22 Real Time Updates (Messaging) (continuation of Test 21) 
From the instance with the error message go and click register
Enter the username as “test2” and password as “1”
Make the role seller
Click on search and then select “test” which is the buyer you made in the last test
Now click the “Create the New Message to buyer from list” button and then type in Hi and then click send to test
Expected : The test client should instantly show the message as a real time update
Result: Pass

Test 23 Sending message to user that does not exist
Run the application and click connect button to connect to the server
Enter “buyer” and “1” as username and password respectively and click login
Click the search icon up top, replace the search bar with the text “randomusernamethatdoesntexist”
Click “Create New Message to Seller '' and type hi in the big text box that came up and then click the send to randomusernamethatdoesntexist button on the left.
Expected: Error Pane should pop up stating there was a SEND_MESSAGE_USER error
Test: Pass



