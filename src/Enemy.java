import javax.imageio.ImageIO;

public class Enemy extends Entity {
    protected Player player;

    public Enemy(int x, int y, int speed, Player player) {
        super(x, y, 120, 120, speed);
        this.player = player;
        getEnemyImage();
    }

    protected void getEnemyImage() {
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/enemy.png"));
        } catch (Exception e) {
            System.out.println("Enemy image not found yet.");
        }
    }

    @Override
    public void update() {
        if (this.x < player.x) this.x += speed;
        if (this.x > player.x) this.x -= speed;
        if (this.y < player.y) this.y += speed;
        if (this.y > player.y) this.y -= speed;
    }
}