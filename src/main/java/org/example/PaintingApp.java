package org.example;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.*;

public class PaintingApp extends JFrame implements MouseListener, MouseMotionListener, Runnable {
    private JPanel canvas;
    private Point lastPoint;
    private Color currentColor;
    private PaintingClient paintingClient;
    private Point lastReceivedPoint;
    public PaintingApp() throws IOException {
        paintingClient = new PaintingClient("127.0.0.1", 8080);
        setTitle("Painting App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new JPanel();
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        add(canvas, BorderLayout.CENTER);

        currentColor = Color.BLACK;

        pack();
        setVisible(true);
    }

    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
        try {
            paintingClient.writePoint(new PointData(lastPoint, true));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void mouseDragged(MouseEvent e) {
        Graphics g = canvas.getGraphics();
        g.setColor(currentColor);
        g.drawLine(lastPoint.x, lastPoint.y, e.getX(), e.getY());
        try {
            paintingClient.writePoint(new PointData(new Point(e.getX(), e.getY()), false));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        lastPoint = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public void run() {
        PointData pointData = null;
        try {
            while (true) {
                pointData = paintingClient.receivePoint();
                Point point = pointData.getPoint();
                if (pointData.isNewPoint())
                    lastReceivedPoint = point;
                System.out.println("received: " + point);
                Graphics g = canvas.getGraphics();
                g.setColor(currentColor);
                g.drawLine(lastReceivedPoint.x, lastReceivedPoint.y, point.x, point.y);
                lastReceivedPoint = point;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        PaintingApp app = new PaintingApp();
        Thread thread = new Thread(app);
        thread.start();
    }
}
