import java.util.*;

public class ShipPlacer {
    private final int BOARD_SIZE = 10;
    private final int[] SHIP_SIZES = {5, 4, 3, 3, 2};

    public List<Ship> placeShips(boolean[][] occupied) {
        List<Ship> ships = new ArrayList<>();
        Random rand = new Random();

        for (int size : SHIP_SIZES) {
            boolean placed = false;

            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int row = rand.nextInt(BOARD_SIZE);
                int col = rand.nextInt(BOARD_SIZE);

                if (canPlace(size, row, col, horizontal, occupied)) {
                    int[][] coords = new int[size][2];
                    for (int i = 0; i < size; i++) {
                        int r = row + (horizontal ? 0 : i);
                        int c = col + (horizontal ? i : 0);
                        coords[i] = new int[]{r, c};
                        occupied[r][c] = true;
                    }
                    ships.add(new Ship(size, coords));
                    placed = true;
                }
            }
        }

        return ships;
    }

    private boolean canPlace(int size, int row, int col, boolean horizontal, boolean[][] occupied) {
        if (horizontal && col + size > BOARD_SIZE) return false;
        if (!horizontal && row + size > BOARD_SIZE) return false;

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (occupied[r][c]) return false;
        }

        return true;
    }
}
