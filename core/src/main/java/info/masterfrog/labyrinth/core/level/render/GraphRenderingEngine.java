package info.masterfrog.labyrinth.core.level.render;

import info.masterfrog.labyrinth.core.level.model.Graph;
import info.masterfrog.labyrinth.core.level.model.Pair;

import java.awt.*;

public class GraphRenderingEngine implements RenderingEngine {
    private Graph graph;
    private int vertexDiameter;
    private int edgeWidth;

    public GraphRenderingEngine(int edgeWidth, int vertexDiameter) {
        this(new Graph(0), edgeWidth, vertexDiameter);
    }

    public GraphRenderingEngine(Graph graph, int edgeWidth, int vertexDiameter) {
        this.graph = graph;
        this.edgeWidth = edgeWidth;
        this.vertexDiameter = vertexDiameter;
    }

    public void setEdgeWidth(int edgeWidth) {
        this.edgeWidth = edgeWidth;
    }

    public void setVertexDiameter(int vertexDiameter) {
        this.vertexDiameter = vertexDiameter;
    }

    public void draw(Graphics2D graphics2D) {
        // render vertices
        drawVertices(graphics2D);

        // render edges
        drawEdges(graphics2D);
    }

    private void drawVertices(Graphics2D graphics2D) {
        // render each vertex
        for (int v = 0; v < graph.getVertexCount(); v++) {
            // setup
            Point p = graph.getVertexLayout(v);
            int x = p.x - (vertexDiameter / 2);
            int y = p.y - (vertexDiameter / 2);
            int w = vertexDiameter;
            int h = vertexDiameter;

            // draw
            float rgb = (float) Math.random() * 0.25f + 0.25f;
            graphics2D.setColor(new Color(rgb, rgb, rgb, 1.0f));
            graphics2D.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            graphics2D.drawOval(x, y, w, h);
        }
    }

    private void drawEdges(Graphics2D graphics2D) {
        // render each edge
        for (Pair<Integer> e : graph.getEdges()) {
            // setup
            int v1 = e.getX();
            int v2 = e.getY();
            Point p1 = graph.getVertexLayout(v1);
            Point p2 = graph.getVertexLayout(v2);

            // determine relative positions and adjust assignments so p1.x <= p2.x per typical orthogonal graphing
            if ((p1.x > p2.x) || (p1.x == p2.x && p1.y > p2.y)) {
                Point tmp = p1;
                p1 = p2;
                p2 = tmp;
            }

            // calculate renderable line segment for edge
            int x1;
            int x2;
            int y1;
            int y2;
            double xDiff = p1.x - p2.x;
            double yDiff = p1.y - p2.y;
            int sign = p1.y <= p2.y ? 1 : -1;
            double paddingScale = vertexDiameter;
            //double length = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
            if (xDiff == 0) {
                x1 = p1.x;
                y1 = p1.y + (int) (sign * paddingScale);
                x2 = p2.x;
                y2 = p2.y - (int) (sign * paddingScale);
            } else {
                double slope = yDiff / xDiff;
                double angle = Math.atan(slope);
                int xPadding = (int) Math.floor(paddingScale * Math.cos(angle));
                int yPadding = (int) Math.floor(paddingScale * Math.sin(angle));
                x1 = p1.x + xPadding;
                y1 = p1.y + yPadding;
                x2 = p2.x - xPadding;
                y2 = p2.y - yPadding;
            }

            // set up color, stroke, etc.
            graphics2D.setColor(
                new Color(
                    (float) Math.random() * 0.25f + 0.25f,
                    (float) Math.random() * 0.25f + 0.25f,
                    (float) Math.random() * 0.25f + 0.25f,
                    1.0f
                )
            );
            graphics2D.setStroke(new BasicStroke(edgeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // draw
            graphics2D.drawLine(x1, y1, x2, y2);
        }
    }

    public void setGraph(Graph g) {
        this.graph = g;
    }
}
