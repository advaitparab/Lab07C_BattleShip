public class Ship {
    private int size;
    private int hitCount;
    private int[][] coordinates; // each element: [row, col]

    public Ship(int size, int[][] coordinates) {
        this.size = size;
        this.coordinates = coordinates;
        this.hitCount = 0;
    }

    public boolean isAt(int row, int col) {
        for (int[] coord : coordinates) {
            if (coord[0] == row && coord[1] == col) return true;
        }
        return false;
    }

    public void hit() {
        hitCount++;
    }

    public boolean isSunk() {
        return hitCount >= size;
    }

    public int getSize() {
        return size;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }
}
