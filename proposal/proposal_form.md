# School of Computing

# CA326 Year 3 Project Proposal Form

**Project Title:** ​TastyTravel

**Student 1 Name:** Michael Savage <br/>
**ID Number:** ​ 17313526

**Student 2 Name:** ​ Gerard Slowey <br/>
**ID Number:** ​ 17349433

**Staff Member Consulted:** ​ Mark Roantree

## Project Description:
**An android app that allows two users to view food and drink amenities located equally
close to both parties based on travel time (e.g. bus, tram, train, walking, cycling etc.).**

The app will implement a ​ **search and sort algorithm** ​which interacts with​ ​the transport for
Ireland ​ **(TFI) API** ​ for public transport options. The algorithm will retrieve the public transport
options offered by TFI along with walking and cycling options to offer to the user a number of
travel options (probably a top three recommended selection) relevant to the location of both
locations of the user of the app and their friend.
We will use the ​ **OpenStreetMap API** ​ to calculate walking and cycling times as well as
displaying the relevant food and drink available at the supplied locations from our search and
sort algorithm. <br/>

To refine the users search for food and drink further, we will allow the user to have a
**predefined list** ​ of food and drink that they prefer which will affect the search results and
display to the user only the relevant results which match their search requirements, therefore
saving them time and preventing frustration.
Furthermore the user will have the option of ​ **specifying the method of travel** ​they prefer to
use at a specific time, for example, if it is raining and they want to meet their friends, they
may prefer to travel by bus instead of walking. However, their friend may have their bike with
them and they prefer to cycle. One or more of the parties can leave this option blank and the
relevant options will still be returned. <br/>

Supplying the preferred mode of transport, the type of food that both users want to eat to our
search and sort algorithm should return to the user a finite list of meeting places that both
parties will enjoy. We believe this should simplify the ritual of ‘meeting for lunch’ and
satisfying the question “where do you want to eat?” which has plagued people since we have
such a broad range of dining experiences at our reach.

## Division of Work ​:
Each member will have responsibilities working in front-end and back-end. <br/>

**Michael** <br/>
_UI Design_: Colours, animation, app layout, fonts. <br/>
_Content Management_: Text and Images, About Section.

**Gerard** <br/>
_SQL Integration_: Storing user prefered food and drink. <br/>
_User Account Creation and Management_: Allowing user to create account which stores their recent searches and saved locations.

**Pair Programming** <br/>
_Search and Sort Algorithm_ <br/>
_Incorporating with Google Maps and TFI API_ <br/>
_SQL Implementation_ <br/>

## Programming Languages:
Java

## Programming Tool(s):
Android Studio IDE, Transport For Ireland API, Google Maps API, Javac Compiler, Firebase
Database, JUnit Unit testing framework.


## Learning Challenges:
1. Neither of us have built an android app before.
    - Getting used to the android studio IDE, its features and layout.
    - Refreshing our Java skills, since Python has been our primary language this academic year.
    - Adhering to OO Design Standards.
    - Developing an android app that is compatible with various android phones, bug testing on and supporting different architectures.
2. Developing a user login system and account creation.
    - Storing the information securely and easily.
3. Learning to use Google Maps and TFI API
    - Extracting information and displaying it in a manner that is aesthetically
       pleasing while easy to understand.
4. Implementing the Search and Sort Algorithm.
    - Parameter passing and search performance.

## Hardware / Software Platform:
Linux, Android Devices.

## Special Hardware / Software Requirements:
None

