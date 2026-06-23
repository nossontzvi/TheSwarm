import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Enemy extends Entity {
    protected Player player;
    private static BufferedImage cachedSprite;

    public Enemy(int x, int y, int speed, Player player) {
        super(x, y, 120, 120, speed);
        this.player = player;
        getEnemyImage();
    }

    public static void preloadImage() {
        if (cachedSprite == null) {
            try {
                java.io.InputStream is = Enemy.class.getResourceAsStream("/enemy.png");
                if (is != null) cachedSprite = ImageIO.read(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void getEnemyImage() {
        preloadImage();
        sprite = cachedSprite;
    }

    @Override
    public void update() {
        if (this.x < player.x) this.x += speed;
        if (this.x > player.x) this.x -= speed;
        if (this.y < player.y) this.y += speed;
        if (this.y > player.y) this.y -= speed;
    }
}