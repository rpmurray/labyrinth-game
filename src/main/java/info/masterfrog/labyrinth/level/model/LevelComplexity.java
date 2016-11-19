package info.masterfrog.labyrinth.level.model;

public class LevelComplexity {
    private int roomCount;
    private int puzzleScale;

    public LevelComplexity(int roomCount, int puzzleScale) {
        this.roomCount = roomCount;
        this.puzzleScale = puzzleScale;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public int getPuzzleScale() {
        return puzzleScale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LevelComplexity)) return false;

        LevelComplexity that = (LevelComplexity) o;

        if (roomCount != that.roomCount) return false;
        return puzzleScale == that.puzzleScale;

    }

    @Override
    public int hashCode() {
        int result = roomCount;
        result = 31 * result + puzzleScale;
        return result;
    }

    @Override
    public String toString() {
        return "LevelComplexity{" +
            "roomCount=" + roomCount +
            ", puzzleScale=" + puzzleScale +
            '}';
    }
}
