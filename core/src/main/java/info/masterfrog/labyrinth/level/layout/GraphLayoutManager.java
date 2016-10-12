package info.masterfrog.labyrinth.core.level.layout;

import info.masterfrog.labyrinth.core.level.model.Graph;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphLayoutManager {
    private static GraphLayoutManager instance;

    private GraphLayoutManager() {
        // do nothing
    }

    public static GraphLayoutManager getInstance() {
        if (instance == null) {
            instance = new GraphLayoutManager();
        }

        return instance;
    }

    public void generateRandomLayout(Graph g, Rectangle bounds) {
        // setup
        Set<Point> points = new HashSet<>();
        int x;
        int y;

        // generate layout one vertex at a time
        for (int i = 0; i < g.getVertexCount(); i++) {
            // try to generate a new random position for vertex i
            do {
                x = (int) Math.round(Math.random() * bounds.width);
                y = (int) Math.round(Math.random() * bounds.height);
            } while (points.contains(new Point(x, y)));

            // set vertex layout
            Point p = new Point(x, y);
            g.setVertexLayout(i, p);
            points.add(p);
        }
    }

    public void generateRandomGridLayout(Graph g, Rectangle bounds, double density) {
        // setup
        Map<Integer, Set<Integer>> xGridCoordinates = new HashMap<>();
        Map<Integer, Set<Integer>> yGridCoordinates = new HashMap<>();
        Set<Point> gridCoordinates = new HashSet<>();
        int xGridCoordinate;
        int yGridCoordinate;
        int xTranslationFactor;
        int yTranslationFactor;
        int x;
        int y;

        // generate grid coordinate possibilities
        for (int i = 1; i <= Math.ceil(g.getVertexCount() / density); i++) {
            xGridCoordinates.put(i, new HashSet<>());
            yGridCoordinates.put(i, new HashSet<>());
        }

        // calculate grid to bounds translation factors
        xTranslationFactor = bounds.width / ((int) Math.ceil(g.getVertexCount() / density) + 1);
        yTranslationFactor = bounds.height / ((int) Math.ceil(g.getVertexCount() / density) + 1);

        // set layout bounds
        g.setLayoutBounds(bounds);

        // generate layout one vertex at a time
        for (int i = 0; i < g.getVertexCount(); i++) {
            // generate a new random set of graph coordinates for vertex i
            do {
                do {
                    xGridCoordinate = (int) Math.ceil(Math.random() * (g.getVertexCount() / density));
                } while (xGridCoordinates.get(xGridCoordinate).size() >= density);
                do {
                    yGridCoordinate = (int) Math.ceil(Math.random() * (g.getVertexCount() / density));
                } while (yGridCoordinates.get(yGridCoordinate).size() >= density);
            } while (gridCoordinates.contains(new Point(xGridCoordinate, yGridCoordinate)));

            // record generated grid coordinates
            xGridCoordinates.get(xGridCoordinate).add(i);
            yGridCoordinates.get(yGridCoordinate).add(i);
            gridCoordinates.add(new Point(xGridCoordinate, yGridCoordinate));

            // calculate real coordinates via translation factors
            x = xGridCoordinate * xTranslationFactor;
            y = yGridCoordinate * yTranslationFactor;

            // set vertex layout
            g.setVertexLayout(i, new Point(x, y));
        }
    }
}
