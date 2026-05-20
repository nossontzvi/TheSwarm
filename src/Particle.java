import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class Particle {
    public double x, y;
    public double velocityX, velocityY;
    public int life;
    public Color color;

    private static final AlphaComposite[] alphas = new AlphaComposite[21];
    static {
        for (int i = 0; i <= 20; i++) {
            alphas[i] = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, i / 20.0f));
        }
    }

    public Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.life = 20;
        this.velocityX = (Math.random() * 6) - 3;
        this.velocityY = (Math.random() * 6) - 3;
    }

    public void update() {
        x += velocityX;
        y += velocityY;
        life--;
    }

    public void draw(Graphics2D g2) {
        if (life < 0) return;

        g2.setComposite(alphas[life]);
        g2.setColor(color);
        g2.fillRect((int)x, (int)y, 6, 6);
        g2.setComposite(alphas[20]);
    }
}