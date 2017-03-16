Console Client application in Java to consume RESTful web service of HangmanWS.
 
Tools:
NetBeans IDE 8.2
jdk1.8.0_121
Maven

Server side Rest methods:
1. GET /HangmanWS/resources/game - gives all the games in Json format
2. GET /HangmanWS/resources/game/newGame - creates new Game with random word, gives it id and returns the new Game in Json format
3. POST /HangmanWS/resources/game with data in Json format e.g. {"game_id":"1","letter":"h"} make guess of the letter in the game