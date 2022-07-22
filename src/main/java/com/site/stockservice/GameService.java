package com.site.stockservice;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class GameService {

    private Game game;
    private int index;

    public GameService() {
        this.game = Game
                .builder()
                .gameArray(new String[]{" "," "," "," "," "," "," "," "," "})
                .winner("nobody")
                .build();
        this.index = 0;
    }

    public void createNewGame() {
        game = Game
                .builder()
                .gameArray(new String[]{" "," "," "," "," "," "," "," "," "})
                .winner("nobody")
                .build();
        index = 0;
    }

    public void setSymbol(int id) {
        String x = "X";
        String o = "O";
        if (index % 2 == 0 && game.getGameArray()[id].equals(" ")) {
            game.getGameArray()[id] = x;
            index++;
        }
        if (index % 2 == 1 && game.getGameArray()[id].equals(" ")){
            game.getGameArray()[id] = o;
            index++;
        }
    }

    public Flux<Game> geFluxGame() {
        return Flux.interval(Duration.ofMillis(500)).map(aLong -> resultGame());
    }

    private Game resultGame() {
        for (int i = 0; i < 3; i++) {
            if (game.getGameArray()[i].equals(game.getGameArray()[i + 1])
                    && game.getGameArray()[i + 1].equals(game.getGameArray()[i + 2])
                    && !game.getGameArray()[i].equals(" ")) {
                game.setWinner(" " + game.getGameArray()[i]);
            }
            if (game.getGameArray()[i].equals(game.getGameArray()[i + 3])
                    && game.getGameArray()[i + 3].equals(game.getGameArray()[i + 6])
                    && !game.getGameArray()[i].equals(" ")) {
                game.setWinner(" " + game.getGameArray()[i]);
            }
        }
        if (game.getGameArray()[0].equals(game.getGameArray()[4])
                && game.getGameArray()[4].equals(game.getGameArray()[8])
                && !game.getGameArray()[0].equals(" ")) {
            game.setWinner(" " + game.getGameArray()[0]);
        }
        if (game.getGameArray()[2].equals(game.getGameArray()[4])
                && game.getGameArray()[4].equals(game.getGameArray()[6])
                && !game.getGameArray()[2].equals(" ")) {
            game.setWinner(" " + game.getGameArray()[2]);
        }
        return game;
    }
}
