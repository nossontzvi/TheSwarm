import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    public static final int SCREEN_HEIGHT = 1080;
    public static final int SCREEN_WIDTH = 1920;

    private final Player player;
    private final GameEngine gameEngine;
    private final KeyHandler keyHandler;
    private BufferedImage background;
    private BufferedImage slashEffect;
    private final WindowFrame bossFrame;

    private final Rectangle returnMenuBounds = new Rectangle(810, 650, 300, 70);
    private final Rectangle quitMenuBounds = new Rectangle(810, 750, 300, 70);
    private boolean returnHover = false;
    private boolean quitHover = false;

    private final Color overlayColor = new Color(0, 0, 0, 150);
    private final Color hitColor = new Color(255, 0, 0, 100);
    private final Font largeFont = new Font("Arial", Font.BOLD, 120);
    private final Font mediumFont = new Font("Arial", Font.BOLD, 50);
    private final Font smallFont = new Font("Arial", Font.BOLD, 36);

    public GamePanel(WindowFrame windowFrame) {
        this.bossFrame = windowFrame;
        keyHandler = new KeyHandler();
        player = new Player(960, 540, 5, keyHandler);
        gameEngine = new GameEngine(this, keyHandler, player);

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        loadImages();

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!gameEngine.isGameOver && !keyHandler.escPressed) return;
                double scaleX = (double) getWidth() / 1920.0;
                double scaleY = (double) getHeight() / 1080.0;
                int mx = (int) (e.getX() / scaleX);
                int my = (int) (e.getY() / scaleY);
                returnHover = returnMenuBounds.contains(mx, my);
                quitHover = quitMenuBounds.contains(mx, my);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameEngine.isGameOver && !keyHandler.escPressed) return;
                double scaleX = (double) getWidth() / 1920.0;
                double scaleY = (double) getHeight() / 1080.0;
                int mx = (int) (e.getX() / scaleX);
                int my = (int) (e.getY() / scaleY);
                if (returnMenuBounds.contains(mx, my)) {
                    gameEngine.returnToMenu();
                    bossFrame.showMenu();
                } else if (quitMenuBounds.contains(mx, my)) {
                    System.exit(0);
                }
            }
        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

    private void loadImages() {
        try {
            java.io.InputStream bgIs = getClass().getResourceAsStream("/background.png");
            if (bgIs != null) background = ImageIO.read(bgIs);
            java.io.InputStream slashIs = getClass().getResourceAsStream("/slash.png");
            if (slashIs != null) slashEffect = ImageIO.read(slashIs);
            Enemy.preloadImage();
            FastEnemy.preloadImage();
            EnemyLevel3.preloadImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    private void drawCenteredString(Graphics2D g2, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setFont(font);
        g2.drawString(text, x, y);
    }

    private void drawButton(Graphics2D g2, String text, Rectangle bounds, boolean isHover) {
        if (isHover) {
            g2.setColor(new Color(50, 55, 65));
        } else {
            g2.setColor(new Color(34, 37, 42));
        }
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g2.setColor(Color.CYAN);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        g2.setColor(Color.WHITE);
        drawCenteredString(g2, text, bounds, new Font("Arial", Font.BOLD, 30));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        double scaleX = (double) getWidth() / 1920.0;
        double scaleY = (double) getHeight() / 1080.0;
        g2.scale(scaleX, scaleY);

        if (background != null) {
            g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }

        if (gameEngine == null) return;

        for (Enemy enemy : gameEngine.enemies) {
            enemy.draw(g2);
        }

        player.draw(g2);

        if (gameEngine.getHitCooldown() > 0 && gameEngine.getHitCooldown() % 10 < 5) {
            g2.setColor(hitColor);
            int inset = 50;
            g2.fillRect(player.x + inset, player.y + inset, player.width - (inset * 2), player.height - (inset * 2));
        }

        if (gameEngine.attackActiveTime > 0 && slashEffect != null) {
            g2.drawImage(slashEffect, gameEngine.attackHitbox.x, gameEngine.attackHitbox.y, gameEngine.attackHitbox.width, gameEngine.attackHitbox.height, null);
        }

        for (Particle p : gameEngine.particles) {
            p.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(smallFont);
        g2.drawString("Score: " + gameEngine.score, 30, 50);
        g2.setColor(Color.CYAN);
        g2.drawString("Level: " + gameEngine.level, 30, 100);

        g2.setColor(Color.RED);
        g2.drawString("HP: ", 1700, 50);

        for (int i = 0; i < player.maxHealth; i++) {
            g2.drawRect(1780 + (i * 40), 25, 30, 30);
        }

        for (int i = 0; i < player.currentHealth; i++) {
            g2.fillRect(1780 + (i * 40), 25, 30, 30);
        }

        if (gameEngine.isGameOver) {
            g2.setColor(overlayColor);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g2.setColor(Color.RED);
            g2.setFont(largeFont);
            g2.drawString("GAME OVER", 600, 500);

            g2.setColor(Color.WHITE);
            g2.setFont(mediumFont);
            g2.drawString("Press 'R' to Restart", 720, 600);

            drawButton(g2, "Return to Menu", returnMenuBounds, returnHover);
            drawButton(g2, "QUIT", quitMenuBounds, quitHover);

        } else if (keyHandler.escPressed) {
            g2.setColor(overlayColor);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g2.setColor(Color.WHITE);
            g2.setFont(largeFont);
            g2.drawString("PAUSED", 700, 500);

            drawButton(g2, "Return to Menu", returnMenuBounds, returnHover);
            drawButton(g2, "QUIT", quitMenuBounds, quitHover);
        }

        Toolkit.getDefaultToolkit().sync();
    }
    
    public void showReturnToMenuButton() {
    }
    
    public void hideReturnToMenuButton() {
    }
}