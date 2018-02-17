# Spring Boot demo seed app for gateway, config server and OAuth 2.0 auth server.

## Introduction

## Real World Example

One of the best ways to understand a piece of code is to try and model it after something in the real world.  So let's put aside our coding hats for a moment and consider the following example...

You and your friends want to go bar hoping around town for St. Patrick's Day.  Let's think about this for a sec and how we can apply it to OAuth and OpenID Connect.

In order to be served alcohol, you need to have a valid ID (e.g. Driver's License).  First, you go to the DMV to get your ID, you bring a lot of required documents with you, wait in line, fill out the forms, pay the fee and then you have a License; you probably did this years ago, but humor me.  Great!  Now you are ready to go out with your friends.  So you walk to a bar, you wait in line, and now the bar tender checks your ID; when they do, they see you are of age and they put a stamp on your hand.  Excellent.  Now you are in the bar (phew what a journey) and you walk up to the bar tender and ask for a drink.  The bar tender checks for the stamp on your hand, sees it, and she brings you your drink; hurray!  Now your friends decide they are tired of this bar and it is time to go to another.  You pay your tab, leave the bar, go to the other where the bouncer at the new bar checks your ID again; after which, they place a new, different stamp on your hand.  This process repeats for the rest of the night until you and your friends decide to call a cab and go home (drink responsibly!)

## Code Example
Wow!  Now that was a good example if I do say so myself!  Now lets go back and look at the concepts that apply to OAuth 2.0 and OpenID Connect!

In this example,
1. Your ID/Driver's License is the ID Token.
2. The stamp on your hand is the Access Token.
3. The bar/bar tender is the Resource API.


The flows are,
1. Getting your Driver's license at the DMV is **Authentication**.  This is the most painful process and the result is an *ID Token*.
2. When the bouncer checks your ID, he/she is checking the ID Token and issuing an *Access Token*; the stamp on your hand.  This is **Authorization**.
3. When the bar tender checks the stamp on your hand, the Access Token, they are verifying that you are permitted to buy drinks; because they bar tender is very busy and it is easier and quicker to show the bar tender your hand rather than pull out your ID from your wallet.  This is **Access** and the bar tender is the *Resource API*.

Some finer details,
1. In the real world, a driver's license is usually valid for years.  For our use case, this is too long.  So we want to make our **ID Token valid for 10 hours**.  
2. In the real world, a hand stamp usually lasts 24 hours.  But for us, we want our **Access Token valid for 5 minutes**.
3. Also, we want *Authentication* to be the only way to get an *ID Token*.  So, ID tokens cannot be used to get a new ID Token; ergo an ID Token is NOT a **Refresh Token**.
4. Our Id token can be used to get other Access tokens for a period of 10 hours.  This allows us to have multiple APIs behind a single gateway and provide role based access to each of them.
5. We also want to keep track of some information about our ID token.  But since we cannot modify our ID token without redoing Authentication, we will nest our ID token into another JWT.  Our **Wallet Token**.
6. We have a couple of options to secure our *Wallet Token*.
  a. We can store it inside an HTTP Only Cookie.  Using this option would add overhead to requests from our UI, and put the burden of figuring out when to get a new access token on the gateway.  
  b. We can store it inside of Web Storage.  Using this option would decrease overhead on requests, but we would need to write code in the UI to determine when to refresh our access tokens.
7. In Gateway, we will make a filter to determine when the last time an ID Token was "used".  If that time is greater than **30 minutes**.  We will require the user to re-authenticate and get a new ID token.  We can use our **Wallet Token** to track this information.  Effectively, this will mean that a User must always re-authenticate after 10 hours or after 30 minutes of inactivity.
8. In Auth Server, we will add functionality to issue ID Tokens on successful authentication and access tokens on successful authorization.  As well as a filter to issue new access tokens when supplied with valid id tokens.
9. In Gateway, we will add a filter to check the Origin and Referer Headers on HTTP requests to prevent possible CSRF Attacks.
 a. If we decide to use an HTTP Only cookie to store our Wallet Token we must also use a CSRF Token cookie to prevent CSRF Attacks.
 b. If we decide to keep our Wallet Token inside Web Storage, we don't need to use a CSRF Token cookie.  We only need to verify the Origin and Referer Headers.
10. Since our **Wallet Token** is only going to be used server side, we can encrypt our wallet JWT using **JWE**.  For an extra layer of security.

There a lot of other minor details we can specify, but for now, this is a good starting point for our project.
## Sequence diagrams

Title: Basic Flow
User/UI->>Gateway: HTTP Basic Authentication
Gateway-->>Auth Server: Pass along authentication request
Auth Server-->>Auth Server: Validate authentication
Auth Server-->>Auth Server: Generate ID Token
Auth Server-->>Auth Server: Generate Wallet Token
Auth Server->>Gateway: Return Wallet Token
Gateway->>User/UI: Store Wallet Token in Web Storage
User/UI->>Auth Server: Request Access Token
Auth Server-->>Auth Server:  Validate Wallet Token
Auth Server-->>Auth Server:  Validate ID Token
Auth Server->>User/UI: Return Access Token
User/UI->>Resource API: Request Access To Resource API
Note right of Resource API: Request to Access Resource API \ndoes not go through Auth Server
