# Packet Request Format Guide

This is a guide for the format to request the command from the server! A general form of the request is:
> requestType#param1, param2, param3, etc
>
Note that even though there will be error checking on the server part, all errors should be caught at the client side
unless it's impossible to do so(for example you need all the usernames to know whether a username is illegal, but you
can tell straight away that an email format is wrong without needing any information from the server, even though both
errors will be caught by the server, it will waste a cycle of IO communication)
<br>
A great example of the shortcoming of error detection is determining parameters numbers. Since some request allow comma
for the last parameter, it's impossible for the server to know whether you mean it to be 2 parameters or 1 parameter
with comma in there. Therefore, __ALWAYS double check that you have the correct amount of parameters passed in__ and
try to minimize error and spot them early, so you won't pass the wrong request to the server!

## Table of Request

### Table of user profile request
___*The request type is case-sensitive___

|     Request Type      |             Syntax              | comma allowed for the last param |                                         Special Exceptions                                         |
|:---------------------:|:-------------------------------:|:--------------------------------:|:--------------------------------------------------------------------------------------------------:|
|         login         |       username, password        |                y                 |                         InvalidPasswordException, IllegalUserNameException                         |
|       register        | role, username, email, password |                y                 | IllegalParameter(when the role is not buyer/seller), EmailFormatException,IllegalUserNameException |
|  displayUserProfile   |              NONE               |               n/a                |                                                n/a                                                 |
|                       |                                 |                                  |                                                                                                    |

## Response

### Exception response

All exception response will be in the form of
> !ExceptionClassName#RequestType#'message'
>
Note that if an instruction is blank or request typer cannot be determined, the request type would be "blank request"
or "wrong request"