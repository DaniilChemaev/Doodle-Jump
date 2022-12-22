package com.chemaev.client;

import com.chemaev.Game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GameClientThread implements Runnable {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final GameClient client;
    private final Game game = Game.getInstance();
    private boolean isWorking = true;

    public GameClientThread(BufferedReader input, BufferedWriter output, GameClient client) {
        this.input = input;
        this.output = output;
        this.client = client;
    }

    public BufferedReader getInput() {
        return input;
    }

    public BufferedWriter getOutput() {
        return output;
    }

    public void stop() {
        isWorking = false;
    }

    @Override
    public void run() {
        try {
            while (isWorking) {
                String message = input.readLine();

                if (message != null) {
                    String[] inputMessageArray = message.split(" ");
                    if (inputMessageArray[0].equals("new")) {
                        double x = Double.parseDouble(inputMessageArray[2]);
                        double y = Double.parseDouble(inputMessageArray[3]);
                        game.createNewOps(inputMessageArray[1], x, y);
                    }

                    if (inputMessageArray[0].equals("move")) {
                        String name = inputMessageArray[1];
                        double x = Double.parseDouble(inputMessageArray[2]);
                        double y = Double.parseDouble(inputMessageArray[3]);
                        int height = Integer.parseInt(inputMessageArray[4]);
                        game.moveOps(name, x, y, height);
                    }

                    if (inputMessageArray[0].equals("shoot")) {
                        boolean isRight = false;
                        String rotation = inputMessageArray[2];
                        if (rotation.equals("right")) {
                            isRight = true;
                        }

                        int damage = Integer.parseInt(inputMessageArray[3]);
                        double x = Double.parseDouble(inputMessageArray[4]);
                        double y = Double.parseDouble(inputMessageArray[5]);

//                        game.createBullet(isRight, damage, x, y);
                    }

                    if (inputMessageArray[0].equals("dead")) {
//                        game.deleteOpp(inputMessageArray[1]);
                    }

                    if (inputMessageArray[0].equals("win")) {
                        game.showWinMenu(inputMessageArray[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
