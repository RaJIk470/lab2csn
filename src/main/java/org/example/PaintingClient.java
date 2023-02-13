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
        socket = new Socket(ip, port);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
    }

    public void writePoint(Point point) throws IOException {
        System.out.println("Client sended: " + point);
        socketOut.writeObject(point);
    }

    public Point receivePoint() throws IOException, ClassNotFoundException {
        return (Point) socketIn.readObject();
    }

}

