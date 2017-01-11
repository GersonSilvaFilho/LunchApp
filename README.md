# LunchApp

Android App written in Kotlin that uses MVP pattern.

## Featues
  - First user enter his name
  - Then user can see a list of near places provided by Google Places API
  - User can select voting for today, yesterday or tomorrow trought navigation drawer
  - User can select the place and see the address and a picture (if API provided)
  - User can vote to their favorite near restaurant
  - User can change username on Drawer as well
  - User can't vote after 13 PM
  - Users are notified everyday 13 PM that the voting has end
  - Implemented a small backend to receive votes (using googlePlacesID, username and date)
  - The minimum Android API is 10 o/
  
## Limitations
  - The user must have a connection, didn't provide any persistance of places
  - I used the Google Places API for currentLocation that should provide near places, but it's not retrieving restaurants everywhere.
  - User will be asked to enable GPS location
  - I just implemented some test cases because have no more time =(
  - I didn't have good results with the images from google places, so i used a default image on listing and when there are no images on detail
  
## Technologies
  - Used Kotlin language for the App and Java to implement some tests
  - Unit tests were written using JUnit and Mockito libs
  - UI tests were written using Espresso framwork
  - Used Retrofit 2.0 to consume Votes web api
  
## Could do better
  - I'm starting to study RxJava and Dagger for DI, but didn't have time to use in this project
  - Could have more testing =(
  - Didn't implement one time per week restaurant
  - Sorry about the UI, i'm terrible designer
