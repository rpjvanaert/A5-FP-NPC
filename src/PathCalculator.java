import java.awt.geom.Point2D;

public class PathCalculator {

    public static Point2D nextTarget(Point2D currPos, String mapName){
        // TODO: Not hardcode this 32 tilesize and retrieve from tile logic
        int Xindex = (int) Math.floor(currPos.getX() / 32.0);
        int Yindex = (int) Math.floor(currPos.getY() / 32.0);

        DistanceMap distanceMap = Simulator.getDistanceMap(mapName);
        System.out.println(distanceMap.getTarget().getMiddlePoint());
        Point2D middlePointCoords = new Point2D.Double(distanceMap.getTarget().getMiddlePoint().getX() * 32, distanceMap.getTarget().getMiddlePoint().getY() * 32);
        if(middlePointCoords.distance(currPos) <= 32){
            return new Point2D.Double(-1,-1);
        }

        int lowest = Integer.MAX_VALUE;
        int lowestIndexX = Integer.MAX_VALUE;
        int lowestIndexY = Integer.MAX_VALUE;

        for (int yOffset = -1; yOffset <= 1; yOffset++) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                int currX = Xindex + xOffset;
                int currY = Yindex + yOffset;
                if (currX > -1 && currX < 99 && currY > -1 && currY < 99) {
                    int value = distanceMap.getMap()[currX][currY];
                    if(value < lowest){
                        lowest = value;
                        lowestIndexX = currX;
                        lowestIndexY = currY;
                    }
                }
            }
        }

        return new Point2D.Double(lowestIndexX * 32 + 16, lowestIndexY * 32 + 16);
    }
}