import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel {
    private BufferedImage bgImage;
    private final Rectangle startButtonBounds = new Rectangle(760, 650, 400, 100);
    private final Rectangle quitButtonBounds = new Rectangle(760, 800, 400, 100);
    private boolean startHover = false;
    private boolean quitHover = false;
    public MenuPanel(ActionListener startAction) {
        this.setPreferredSize(new Dimension(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT));

        try {
            java.io.InputStream is = getClass().getResourceAsStream("/menu_bg.png");
            if (is != null) {
                bgImage = ImageIO.read(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                double scaleX = (double) getWidth() / 1920.0;
                double scaleY = (double) getHeight() / 1080.0;
                int mx = (int) (e.getX() / scaleX);
                int my = (int) (e.getY() / scaleY);
                startHover = startButtonBounds.contains(mx, my);
                quitHover = quitButtonBounds.contains(mx, my);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                double scaleX = (double) getWidth() / 1920.0;
                double scaleY = (double) getHeight() / 1080.0;
                int mx = (int) (e.getX() / scaleX);
                int my = (int) (e.getY() / scaleY);
                if (startButtonBounds.contains(mx, my)) {
                    startAction.actionPerformed(null);
                } else if (quitButtonBounds.contains(mx, my)) {
                    System.exit(0);
                }
            }
        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
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
        drawCenteredString(g2, text, bounds, new Font("Arial", Font.BOLD, 48));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        double scaleX = (double) getWidth() / 1920.0;
        double scaleY = (double) getHeight() / 1080.0;
        g2.scale(scaleX, scaleY);

        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, 1920, 1080, null);
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, 1920, 1080);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, 1920, 1080);
        }

        g2.setColor(Color.CYAN);
        drawCenteredString(g2, "THE SWARM", new Rectangle(460, 200, 1000, 150), new Font("Arial", Font.BOLD, 120));

        g2.setColor(Color.WHITE);
        drawCenteredString(g2, "Use W A S D to move. Press SPACE to attack.", new Rectangle(460, 450, 1000, 50), new Font("Arial", Font.BOLD, 36));
        drawCenteredString(g2, "Press ESC to pause.", new Rectangle(460, 520, 1000, 50), new Font("Arial", Font.BOLD, 36));

        drawButton(g2, "START GAME", startButtonBounds, startHover);
        drawButton(g2, "QUIT", quitButtonBounds, quitHover);
    }
}