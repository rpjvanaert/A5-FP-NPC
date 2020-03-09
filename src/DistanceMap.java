public class DistanceMap {
    private String mapName;
    private int[][] distanceMap;

    public DistanceMap(String mapName, int[][] distanceMap) {
        this.mapName = mapName;
        this.distanceMap = distanceMap;
    }

    public int[][] getDistanceMap() {
        return distanceMap;
    }

    public String getMapName() {
        return mapName;
    }
}