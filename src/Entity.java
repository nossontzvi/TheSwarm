import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    protected int x, y, speed, width, height;
    protected BufferedImage sprite;
    public boolean isActive = true;

    protected int hitOffsetX = 0;
    protected int hitOffsetY = 0;
    protected int hitWidth;
    protected int hitHeight;

    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    public Entity(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.hitWidth = width;
        this.hitHeight = height;
    }

    public abstract void update();

    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x, y, width, height, null);
        }
    }

    public Rectangle getBounds() {
        bounds.setBounds(x + hitOffsetX, y + hitOffsetY, hitWidth, hitHeight);
        return bounds;
    }
}