import javax.swing.*;
import java.awt.*;

public class WindowFrame extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    GamePanel gamePanel;
    MenuPanel menuPanel;
    GameEngine gameEngine;
    KeyHandler keyHandler;
    Player player;

    public WindowFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        keyHandler = new KeyHandler();
        player = new Player(960, 540, 5, keyHandler);
        gamePanel = new GamePanel(player, keyHandler);
        gameEngine = new GameEngine(gamePanel, keyHandler, player);
        gamePanel.setEngine(gameEngine);

        menuPanel = new MenuPanel(e -> startGame());

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        this.add(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("The Swarm");
        this.pack();
        this.setLocationRelativeTo(null);

        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.setVisible(true);
    }

    private void startGame() {
        cardLayout.show(mainPanel, "Game");
        this.requestFocusInWindow();
        gameEngine.startGameThread();
    }
}