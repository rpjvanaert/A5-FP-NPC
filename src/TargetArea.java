import java.awt.geom.Point2D;

public class TargetArea {
    private Point2D middlePoint;
    private Point2D[] coordinates;

    public TargetArea(Point2D middlePoint, Point2D[] coordinates) {
        this.middlePoint = middlePoint;
        this.coordinates = coordinates;
    }

    public Point2D getMiddlePoint() {
        return middlePoint;
    }
}
