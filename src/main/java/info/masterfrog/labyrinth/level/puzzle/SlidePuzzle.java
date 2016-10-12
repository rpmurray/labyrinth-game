package info.masterfrog.labyrinth.level.puzzle;

public class SlidePuzzle {
    private int width;
    private int height;
    private int emptyIdx;
    private int[] tiles;

    public SlidePuzzle(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new int[width * height];
        this.emptyIdx = tiles.length / 2;
        for (int i = 0; i < width * height; i++) {
            tiles[i] = i < emptyIdx ? i : i > emptyIdx ? i - 1 : -1;
        }
    }

    public SlidePuzzle randomize() {
        for (int i = tiles.length - 1; i > 0; i--) {
            int j = (int) Math.floor(Math.random() * i);
            int xi = i % tiles.length;
            int yi = (int) Math.floor(i / tiles.length);
            int xj = j % tiles.length;
            int yj = (int) Math.floor(j / tiles.length);
            swapTiles(xi, yi, xj, yj);
        }

        if (!isSolvable()) {
            if (emptyIdx <= 1) {
                swapTiles(tiles.length - 2, tiles.length - 1, tiles.length - 1, tiles.length - 1);
            } else {
                swapTiles(0, 0, 1, 0);
            }
        }

        return this;
    }

    public int[] getTiles() {
        return tiles;
    }

    private void swapTiles(int i, int j, int k, int l) {
        int temp = tiles[j * width + i];
        tiles[j * width + i] = tiles[l * width + k];
        tiles[l * width + k] = temp;
    }

    private int sumInversions() {
        int inversions = 0;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
            inversions += countInversions(i, j);
          }
        }

        return inversions;
      }

    private int countInversions(int i, int j) {
        int inversions = 0;
        int tileNum = j * width + i;
        int lastTile = tiles.length;
        int tileValue = tiles[j * width + i];
        for (int q = tileNum + 1; q < lastTile; ++q) {
          int k = q % width;
          int l = (int) Math.floor(q / height);

          int compValue = tiles[l * width + k];
          if (tileValue > compValue && tileValue != (lastTile - 1)) {
            ++inversions;
          }
        }

        return inversions;
    }

    private boolean isSolvable() {
        return (sumInversions() % 2 == 0);
    }
}
