import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class EnemyLevel3 extends Enemy {
    private static final int ENEMY_LEVEL_3_SPEED = 6;
    private static BufferedImage cachedLevel3Sprite;

    public EnemyLevel3(int x, int y, Player player) {
        super(x, y, ENEMY_LEVEL_3_SPEED, player);
    }

    public static void preloadImage() {
        if (cachedLevel3Sprite == null) {
            try {
                java.io.InputStream is = EnemyLevel3.class.getResourceAsStream("/enemy_level_3.png");
                if (is != null) cachedLevel3Sprite = ImageIO.read(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void getEnemyImage() {
        preloadImage();
        sprite = cachedLevel3Sprite;
    }
}