package com.mycompany.hangmantestclient;

import Entity.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author Piotr Sm.
 */
public class HangmanClient {

    static String getUrl = "http://localhost:8080/HangmanWS/resources/game";
    static String newGameUrl = "http://localhost:8080/HangmanWS/resources/game/newGame";
    static Client client = ClientBuilder.newClient();
    static ObjectMapper mapper = new ObjectMapper();
    static WebTarget target;

    public static void main(String[] args) throws IOException {

        Game game;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to hangman! I will pick a word and you will "
                + "try to guess it character "
                + "by character."
                + "If you guess wrong 6 times, then I win.\nIf you can guess it before then, you win. ");

        boolean doYouWantToPlay = true;
        while (doYouWantToPlay) {
            game = creatNewGame();
            do {
                System.out.println("");
                System.out.println("Current guesses:");
                printCurrentGuess(game.getCurrentGuess());
                System.out.printf("You have %d tries left\n", game.getMaxTries() - game.getCurrentTry());
                System.out.println("Enter a character that you think is in the word");
                char guess = (sc.next().toLowerCase()).charAt(0);
                game = playGame(game.getGame_id(), guess);
            } while (!game.isGameOver());
            if (game.isWon()) {
                System.out.println("Congratulation! You won.");
            } else {
                System.out.println("Sorry, you lose.");
            }
            System.out.println();
            System.out.println("Do you want to play another game? Enter Y if you do.");
            Character response = (sc.next().toUpperCase()).charAt(0);
            doYouWantToPlay = (response == 'Y');
        }
        System.out.println("Bye bye");
    }

    private static Game creatNewGame() throws IOException {
        target = client.target(newGameUrl);
        Response response = target.request().get();
        String result = response.readEntity(String.class);
        return mapper.readValue(result, Game.class);

    }

    private static Game playGame(long game_id, char guess) throws IOException {
        target = client.target(getUrl);
        String inputData = "{\"game_id\":\"" + game_id + "\",\"letter\":\"" + guess + "\"}";
        Response response = target.request().post(Entity.entity(inputData, "application/json"));
        String result = response.readEntity(String.class);
        //System.out.println("From server: " + result);
        Game game = mapper.readValue(result, Game.class);
        return game;
    }

    private static List<Game> getAllGames() throws IOException {
        target = client.target(getUrl);
        Response response = target.request().get();
        String result = response.readEntity(String.class);
        return mapper.readValue(result, new TypeReference<List<Game>>() {
        });
    }

    private static void printCurrentGuess(StringBuilder currentGuess) {
        StringBuilder tempStrB = new StringBuilder();
        for (int i = 0; i < currentGuess.length()*2; i++) {
                if (i % 2 == 0) {
                tempStrB.append(currentGuess.charAt(i/2));
            } else {
                tempStrB.append(" ");
            }
        }
        System.out.println(tempStrB.toString());
    }
}
