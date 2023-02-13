package org.example;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaintingServer {
    private ServerSocket socket;
    private static List<ObjectOutputStream> outputs;

    public PaintingServer(int port) throws IOException {
        socket = new ServerSocket(port);
        outputs = new ArrayList<>();
    }

    public synchronized void sendToAll(PointData pointData, ObjectOutputStream output) throws IOException {
        for (var s : outputs) {
            if (!(output.equals(s))) {
                s.writeObject(pointData);
            }
        }
    }
    public void listen() throws IOException {
        while (true) {
            Socket client = socket.accept();
            System.out.println("Accepted client");
            Thread thread = new Thread(() -> {
                try {
                    ObjectInputStream input = new ObjectInputStream(client.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                    outputs.add(output);
                    while (true) {
                        PointData pointData = (PointData)input.readObject();
                        System.out.println("Server received: " + pointData.getPoint());
                        sendToAll(pointData, output);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            });
            thread.start();
        }


    }

    public static void main(String[] args) throws IOException {
        PaintingServer paintingServer = new PaintingServer(8080);
        try {
            paintingServer.listen();
        } catch (Exception e) {

        }
    }
}
