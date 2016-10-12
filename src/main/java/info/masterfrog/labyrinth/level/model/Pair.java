package info.masterfrog.labyrinth.level.model;

public class Pair<T> {
    private T x;
    private T y;

    public Pair(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?> pair = (Pair<?>) o;

        if (!x.equals(pair.x)) return false;
        return y.equals(pair.y);

    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
