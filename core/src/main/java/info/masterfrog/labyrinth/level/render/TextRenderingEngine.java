package info.masterfrog.labyrinth.core.level.render;

import java.awt.*;

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
