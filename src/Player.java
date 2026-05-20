import javax.imageio.ImageIO;

public class Player extends Entity {
    private KeyHandler keyHandler;
    public int facingDirection = 1;
    public int maxHealth = 3;
    public int currentHealth = 3;

    public Player(int x, int y, int speed, KeyHandler keyHandler) {
        super(x, y, 288, 288, speed);
        this.keyHandler = keyHandler;
        getPlayerImage();

        this.hitOffsetX = 100;
        this.hitOffsetY = 80;
        this.hitWidth = 88;
        this.hitHeight = 148;
    }

    private void getPlayerImage() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/player.png"));
        } catch (Exception e) {
            System.out.println("Player image not found yet.");
        }
    }

    @Override
    public void update() {
        if (keyHandler.upPressed && y > 0) {
            y -= speed;
            facingDirection = 0;
        }
        if (keyHandler.downPressed && y < 1080 - height) {
            y += speed;
            facingDirection = 1;
        }
        if (keyHandler.leftPressed && x > 0) {
            x -= speed;
            facingDirection = 2;
        }
        if (keyHandler.rightPressed && x < 1920 - width) {
            x += speed;
            facingDirection = 3;
        }
    }
}