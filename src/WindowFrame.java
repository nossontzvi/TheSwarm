import javax.swing.*;
import java.awt.*;

public class WindowFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final GamePanel gamePanel;

    public WindowFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        gamePanel = new GamePanel(this);
        MenuPanel menuPanel = new MenuPanel(e -> startGame());

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        this.setUndecorated(true);
        this.add(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("The Swarm");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.addKeyListener(gamePanel.getKeyHandler());
        this.setFocusable(true);
        this.setVisible(true);
    }

    private void startGame() {
        cardLayout.show(mainPanel, "Game");
        this.requestFocusInWindow();
        gamePanel.getGameEngine().startGameThread();
        gamePanel.getGameEngine().restartGame();
    }
    public void showMenu(){
        cardLayout.show(mainPanel, "Menu");
        this.requestFocusInWindow();
    }
}