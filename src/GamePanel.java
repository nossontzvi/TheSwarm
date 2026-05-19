import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    public final int SCREEN_WIDTH = 1920;
    public final int SCREEN_HEIGHT = 1080;

    private Player player;
    private GameEngine engine;
    private KeyHandler keyHandler;

    private BufferedImage background;
    private BufferedImage slashEffect;

    public GamePanel(Player player, KeyHandler keyHandler) {
        this.player = player;
        this.keyHandler = keyHandler;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        loadImages();
    }

    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    private void loadImages() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/background.png"));
            slashEffect = ImageIO.read(getClass().getResourceAsStream("/slash.png"));
        } catch (Exception e) {
            System.out.println("Images not found in res folder yet.");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (background != null) {
            g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }

        if (engine == null) return;

        player.draw(g2);

        if (engine.attackActiveTime > 0 && slashEffect != null) {
            g2.drawImage(slashEffect, engine.attackHitbox.x, engine.attackHitbox.y, engine.attackHitbox.width, engine.attackHitbox.height, null);
        }

        for (Enemy enemy : engine.enemies) {
            enemy.draw(g2);
        }

        for (Particle p : engine.particles) {
            p.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.drawString("Score: " + engine.score, 30, 50);
        g2.setColor(Color.CYAN);
        g2.drawString("Level: " + engine.level, 30, 100);

        if (engine.isGameOver) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 120));
            g2.drawString("GAME OVER", 580, 500);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            g2.drawString("Press R to Restart", 740, 600);
        }
        else if (keyHandler.escPressed) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g2.setFont(new Font("Arial", Font.BOLD, 120));
            g2.setColor(Color.YELLOW);
            g2.drawString("PAUSED", 700, 540);
        }
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.drawString("HP: ", 1600, 50);
        for (int i = 0; i < player.currentHealth; i++) {
            g2.fillRect(1680 + (i * 40), 25, 30, 30);
        }

        if (engine.getHitCooldown() > 0 && engine.getHitCooldown() % 10 < 5) {
            g2.setColor(new Color(255, 0, 0, 100));

            int inset = 50;

            g2.fillRect(player.x + inset, player.y + inset, player.width - (inset * 2), player.height - (inset * 2));
        }

        g2.dispose();
    }
}