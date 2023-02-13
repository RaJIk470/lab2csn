package org.example;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaintingServer implements Runnable {
    private ServerSocket socket;
    private static List<ObjectOutputStream> streams;

    public PaintingServer(int port) throws IOException {
        socket = new ServerSocket(port);
        streams = new ArrayList<>();
    }

    public void run() {
        try {
            Socket client = socket.accept();
            ObjectInputStream stream = new ObjectInputStream(client.getInputStream());
            streams.add(new ObjectOutputStream(client.getOutputStream()));

            while (true) {
                Point point = (Point)stream.readObject();
                System.out.println("Server received: " + point);
                for (var s : streams) {
                    if (!(Objects.equals(s, stream))) {
                        s.writeObject(point);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        PaintingServer paintingServer = new PaintingServer(8080);
        Thread thread = new Thread(paintingServer);
        thread.start();

    }
}
