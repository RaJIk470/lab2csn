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
    private static List<PointData> points;

    public PaintingServer(int port) throws IOException {
        points = new ArrayList<>();
        socket = new ServerSocket(port);
        outputs = new ArrayList<>();
    }

    public synchronized void sendToAll(PointData pointData, ObjectOutputStream output) throws IOException {
        for (var s : outputs) {
            if (s == null) continue;
            if (!(output.equals(s))) {
                s.writeObject(pointData);
            }
        }
    }

    private void writePointsData(ObjectOutputStream output) throws IOException {
        for (var data : points) {
            output.writeObject(data);
        }
    }
    public void listen() throws IOException {
        while (true) {
            Socket client = socket.accept();
            System.out.println("Accepted client");
            Thread thread = new Thread(() -> {
                ObjectOutputStream output = null;
                try {
                    ObjectInputStream input = new ObjectInputStream(client.getInputStream());
                    output = new ObjectOutputStream(client.getOutputStream());
                    outputs.add(output);
                    writePointsData(output);
                    while (true) {
                        PointData pointData = (PointData) input.readObject();
                        points.add(pointData);
                        System.out.println("Server received: " + pointData.getPoint());
                        sendToAll(pointData, output);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    outputs.remove(output);
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
