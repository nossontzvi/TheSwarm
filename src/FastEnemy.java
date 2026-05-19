import javax.imageio.ImageIO;

public class FastEnemy extends Enemy {
    public FastEnemy(int x, int y, Player player) {
        super(x, y, 4, player);
    }

    @Override
    protected void getEnemyImage() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/enemy_fast.png"));
        } catch (Exception e) {
            System.out.println("Fast enemy image not found yet.");
        }
    }
}