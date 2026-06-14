import javax.swing.*;
import java.awt.*;

public class WindowFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;

    public WindowFrame() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        gamePanel = new GamePanel(this);
        menuPanel = new MenuPanel(e -> startGame());

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        this.add(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("The Swarm");
        this.pack();
        this.setLocationRelativeTo(null);

        this.addKeyListener(gamePanel.getKeyHandler());
        this.setFocusable(true);
        this.setVisible(true);
    }

    private void startGame() {
        cardLayout.show(mainPanel, "Game");
        this.requestFocusInWindow();
        gamePanel.getGameEngine().startGameThread();
    }
    public void showMenu(){
        cardLayout.show(mainPanel, "Menu");
        this.requestFocusInWindow();
    }
}