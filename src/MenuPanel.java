import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel {
    private BufferedImage bgImage;

    public MenuPanel(ActionListener startAction) {
        this.setPreferredSize(new Dimension(1920, 1080));
        this.setLayout(null);

        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/menu_bg.png"));
        } catch (Exception e) {
            System.out.println("menu_bg.png not found.");
        }

        JLabel titleLabel = new JLabel("THE SWARM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 120));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBounds(460, 200, 1000, 150);
        this.add(titleLabel);

        JLabel instLabel1 = new JLabel("Use W A S D to move. Press SPACE to attack.", SwingConstants.CENTER);
        instLabel1.setFont(new Font("Arial", Font.BOLD, 36));
        instLabel1.setForeground(Color.WHITE);
        instLabel1.setBounds(460, 450, 1000, 50);
        this.add(instLabel1);

        JLabel instLabel2 = new JLabel("Press ESCAPE to pause.", SwingConstants.CENTER);
        instLabel2.setFont(new Font("Arial", Font.BOLD, 36));
        instLabel2.setForeground(Color.WHITE);
        instLabel2.setBounds(460, 520, 1000, 50);
        this.add(instLabel2);

        JButton startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 48));
        startButton.setBounds(760, 650, 400, 100);
        startButton.setFocusPainted(false);
        startButton.addActionListener(startAction);
        this.add(startButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, 1920, 1080, null);

            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 1920, 1080);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 1920, 1080);
        }
    }
}