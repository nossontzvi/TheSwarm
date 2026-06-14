import javax.imageio.ImageIO;
import java.io.IOException;

public class EnemyLevel3 extends Enemy {

    private static final int ENEMY_LEVEL_3_SPEED = 6;

    public EnemyLevel3(int x, int y, Player player) {
        super(x, y, ENEMY_LEVEL_3_SPEED, player);
    }

    @Override
    protected void getEnemyImage() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/enemy_level_3.png"));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}