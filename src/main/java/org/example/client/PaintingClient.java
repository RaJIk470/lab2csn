package org.example.client;

import org.example.data.PointData;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    public void writePoint(PointData pointData) throws IOException {
        socketOut.writeObject(pointData);
    }

    public PointData receivePoint() throws IOException, ClassNotFoundException {
        return (PointData) socketIn.readObject();
    }

}

