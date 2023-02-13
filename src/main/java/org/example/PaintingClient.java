package org.example;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PaintingClient {
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;

    public PaintingClient(String ip, int port) throws IOException {
        System.out.println("Trying to connect");
        socket = new Socket(ip, port);
        System.out.println("Connected");
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
    }

    public void writePoint(PointData pointData) throws IOException {
        System.out.println("Client sended: " + pointData);
        socketOut.writeObject(pointData);
    }

    public PointData receivePoint() throws IOException, ClassNotFoundException {
        return (PointData) socketIn.readObject();
    }

}

