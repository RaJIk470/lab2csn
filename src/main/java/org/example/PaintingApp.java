package org.example;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.*;

public class PaintingApp extends JFrame implements MouseListener, MouseMotionListener, Runnable {
    private JPanel canvas;
    private JToolBar toolbar;
    private JButton pencilButton;
    private Point lastPoint;
    private Point lastReceivedPoint;
    private Color currentColor;
    private PaintingClient paintingClient;
    private PaintingServer paintingServer;
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

        toolbar = new JToolBar();
        add(toolbar, BorderLayout.NORTH);

        pencilButton = new JButton("Pencil");
        toolbar.add(pencilButton);

        currentColor = Color.BLACK;

        pack();
        setVisible(true);
    }

    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        Graphics g = canvas.getGraphics();
        g.setColor(currentColor);
        g.drawLine(lastPoint.x, lastPoint.y, e.getX(), e.getY());
        try {
            paintingClient.writePoint(new Point(e.getX(), e.getY()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        lastPoint = e.getPoint();
    }

    public void run() {
        Graphics g = canvas.getGraphics();
        g.setColor(currentColor);
        try {
            Point point = paintingClient.receivePoint();
            if (lastReceivedPoint == null)
                lastReceivedPoint = point;
            g.drawLine(lastReceivedPoint.x, lastReceivedPoint.y, point.x, point.y);
            lastReceivedPoint = point;
            paintingClient.writePoint(point);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static void main(String[] args) throws IOException {
        PaintingApp app = new PaintingApp();
        new Thread(app).start();
    }
}
