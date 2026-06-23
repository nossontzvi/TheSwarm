import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FastEnemy extends Enemy {
    private static BufferedImage cachedFastSprite;

    public FastEnemy(int x, int y, Player player) {
        super(x, y, 4, player);
    }

    public static void preloadImage() {
        if (cachedFastSprite == null) {
            try {
                java.io.InputStream is = FastEnemy.class.getResourceAsStream("/enemy_fast.png");
                if (is != null) cachedFastSprite = ImageIO.read(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void getEnemyImage() {
        preloadImage();
        sprite = cachedFastSprite;
    }
}