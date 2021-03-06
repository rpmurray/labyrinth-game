package info.masterfrog.labyrinth.level.render;

import java.awt.Graphics2D;
import java.awt.Point;

public class TextRenderingEngine implements RenderingEngine {
    String text;
    Point position;

    public TextRenderingEngine() {
        this.text = "";
        this.position = new Point(0, 0);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawString(text, position.x, position.y);
    }
}
