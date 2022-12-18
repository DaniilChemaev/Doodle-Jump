package com.chemaev.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class GameClient {
    private Socket socket;
    private GameClientThread gameThread;

    public void stop() {
        gameThread.stop();
    }

    public boolean sendMessage(String message) {
        boolean isSuccessful = false;

        try {
            gameThread.getOutput().write(message);
            gameThread.getOutput().flush();

            isSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public void start() throws IOException {
        socket = new Socket("127.0.0.1", 5555);

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        gameThread = new GameClientThread(input, output, this);

        new Thread(gameThread).start();
    }
}
