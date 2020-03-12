import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;

public class DistanceMap {
    private String mapName;
    private int[][] map;
    private Queue<Point2D> queue;
    private Boolean[][] visited;
    private Boolean[][] walkableMap;
    private TargetArea target;

    private final int size = 100;

    public TargetArea getTarget() {
        return target;
    }

    public Boolean[][] getWalkableMap() {
        return walkableMap;
    }

    /**
     * Makes a distanceMap which can be used to find the shortest path
     * @param mapName The name of the map
     * @param targetArea The point from which the DistanceMap starts (the 0 point)
     * @param walkableMap A map used to know if the surface is walkable for the NPC's
     */
    public DistanceMap(String mapName, TargetArea targetArea, WalkableMap walkableMap) {
        this.mapName = mapName;
        this.map = new int[size][size]; //todo uitlezen uit json
        this.visited = new Boolean[size][size];
        this.walkableMap = walkableMap.getMap();
        this.target = targetArea;

        this.queue = new LinkedList<>();
        this.queue.offer(targetArea.getMiddlePoint());
        this.map[(int)targetArea.getMiddlePoint().getX()][(int)targetArea.getMiddlePoint().getY()] = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.visited[i][j] = false;
            }
        }

        this.visited[(int)targetArea.getMiddlePoint().getX()][(int)targetArea.getMiddlePoint().getY()] = true;

        /**
         * The Breadth-First Search Algorithm
         * Loops trough every accessible point in the map and gives it the corresponding distance value
         */
        while (!this.queue.isEmpty()) {
            Point2D currentPoint = this.queue.poll();
            int currentX = (int) currentPoint.getX();
            int currentY = (int) currentPoint.getY();
            //north
            try {
                if (this.visited[currentX][currentY + 1] == false && this.walkableMap[currentX][currentY + 1] == true) {
                    this.map[currentX][currentY + 1] = this.map[currentX][currentY] + 1;
                    this.queue.offer(new Point2D.Double(currentX, currentY + 1));
                    this.visited[currentX][currentY + 1] = true;
                }
            } catch (Exception Ignore) {}
            //east
            try {
                if (this.visited[currentX + 1][currentY] == false && this.walkableMap[currentX + 1][currentY] == true) {
                    this.map[currentX + 1][currentY] = this.map[currentX][currentY] + 1;
                    this.queue.offer(new Point2D.Double(currentX + 1, currentY));
                    this.visited[currentX + 1][currentY] = true;
                }
            } catch (Exception Ignore) {}
            //south
            try {
                if (this.visited[currentX][currentY - 1] == false && this.walkableMap[currentX][currentY - 1] == true) {
                    this.map[currentX][currentY - 1] = this.map[currentX][currentY] + 1;
                    this.queue.offer(new Point2D.Double(currentX, currentY - 1));
                    this.visited[currentX][currentY - 1] = true;
                }
            } catch (Exception Ignore) {}
            //west
            try {
                if (this.visited[currentX - 1][currentY] == false && this.walkableMap[currentX - 1][currentY] == true) {
                    this.map[currentX - 1][currentY] = this.map[currentX][currentY] + 1;
                    this.queue.offer(new Point2D.Double(currentX - 1, currentY));
                    this.visited[currentX - 1][currentY] = true;
                }
            } catch (Exception Ignore) {}
        }
    }

    public int[][] getMap() {
        return map;
    }

    public String getMapName() {
        return mapName;
    }
}
