import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    public static final int SCREEN_HEIGHT = 1080;
    public static final int SCREEN_WIDTH = 1920;

    private Player player;
    private GameEngine gameEngine;
    private KeyHandler keyHandler;
    private BufferedImage background;
    private BufferedImage slashEffect;
    private WindowFrame bossFrame;

    private JButton returnToMenuButton = new JButton("Return to Menu");

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

        setLayout(null);

        returnToMenuButton.setFont(new Font("Arial", Font.BOLD, 30));
        returnToMenuButton.setBackground(new Color(34, 37, 42));
        returnToMenuButton.setForeground(Color.WHITE);
        returnToMenuButton.setBounds(810, 650, 300, 70);
        this.returnToMenuButton.setVisible(false);
        this.add(returnToMenuButton);

        returnToMenuButton.addActionListener(e -> {
            gameEngine.returnToMenu();
            bossFrame.showMenu();
        });




        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        loadImages();
    }

    private void loadImages() {
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/background.png"));
            slashEffect = ImageIO.read(getClass().getResourceAsStream("/slash.png"));
        } catch (Exception e) {
            System.out.println("Images not found in res folder yet.");
        }
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (background != null) {
            g2.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }

        if (gameEngine == null) return;

        synchronized (gameEngine.enemies) {
            for (Enemy enemy : gameEngine.enemies) {
                enemy.draw(g2);
            }
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

        synchronized (gameEngine.particles) {
            for (Particle p : gameEngine.particles) {
                p.draw(g2);
            }
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

        } else if (keyHandler.escPressed) {
            g2.setColor(overlayColor);
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g2.setColor(Color.WHITE);
            g2.setFont(largeFont);
            g2.drawString("PAUSED", 700, 500);


        }

//        g2.dispose();
    }
    public void showReturnToMenuButton(){
        returnToMenuButton.setVisible(true);
    }
    public void hideReturnToMenuButton(){
        returnToMenuButton.setVisible(false);
    }
}