package invaders;

import static invaders.Consts.GAMEHEIGHT;
import static invaders.Consts.GAMEWIDTH;

import engine.GameEngine;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync= true;
            Game game= new Game();
            GameEngine gameEng= new GameEngine("My game", GAMEWIDTH, GAMEHEIGHT, vSync, game);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
