package org.example.data;

import java.awt.*;
import java.io.Serializable;

public class PointData implements Serializable {
    private boolean isNewPoint;
    private Point point;

    public PointData(Point point, boolean isNewPoint) {
        this.point = point;
        this.isNewPoint = isNewPoint;
    }

    public boolean isNewPoint() {
        return isNewPoint;
    }

    public void setNewPoint(boolean newPoint) {
        isNewPoint = newPoint;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
