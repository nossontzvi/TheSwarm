import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;

public class GameEngine {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;
    private Player player;
    private SoundManager soundManager;
    private int hitCooldown = 0;
    private boolean isRunning;
    public List<Enemy> enemies = new ArrayList<>();
    public List<Particle> particles = new ArrayList<>();

    public int getHitCooldown() {
        return hitCooldown;
    }

    public int score = 0;
    public int level = 1;
    public boolean isGameOver = false;

    private int spawnTimer = 0;
    private int attackCooldown = 0;
    public int attackActiveTime = 0;
    public Rectangle attackHitbox = new Rectangle(0, 0, 0, 0);

    public GameEngine(GamePanel gamePanel, KeyHandler keyHandler, Player player) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.player = player;
        this.soundManager = new SoundManager();

        this.soundManager.loadSound("bgm", "/bgm.wav", 1);
        this.soundManager.loadSound("slash", "/slash.wav", 5);
    }

    public void startGameThread() {
        isRunning = true;
        soundManager.loop("bgm");
        new Thread(() -> {
            while (isRunning) {
                if (!keyHandler.escPressed && !isGameOver) {
                    updateGameLogic();
                } else if (isGameOver && keyHandler.rPressed) {
                    restartGame();
                }

                gamePanel.repaint();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateGameLogic() {
        player.update();
        handleSpawning();
        handleCombat();
        updateLists();
        checkPlayerCollision();

        if (hitCooldown > 0) hitCooldown--;

        level = (score / 1000) + 1;
    }

    private void handleSpawning() {
        spawnTimer++;
        int spawnRate = Math.max(10, 60 - (level * 5));

        if (spawnTimer >= spawnRate) {
            int randomX = (int)(Math.random() * 1920);
            int enemyType = (int)(Math.random() * 10);

            synchronized (enemies) {
                if (level >= 3 && enemyType == 8) {
                    enemies.add(new EnemyLevel3(randomX, -50, player));
                } else if (enemyType >= 8) {
                    enemies.add(new FastEnemy(randomX, -50, player));
                } else {
                    enemies.add(new Enemy(randomX, -50, 2, player));
                }
            }
            spawnTimer = 0;
        }
    }

    private void handleCombat() {
        if (attackCooldown > 0) attackCooldown--;
        if (attackActiveTime > 0) {
            attackActiveTime--;
        } else {
            attackHitbox.setBounds(0,0,0,0);
        }

        if (keyHandler.spacePressed && attackCooldown == 0) {
            attackCooldown = 30;
            attackActiveTime = 10;

            soundManager.play("slash");
            int atkX = player.x;
            int atkY = player.y;
            int offset = 150;

            if (player.facingDirection == 0) atkY -= offset;
            if (player.facingDirection == 1) atkY += offset;
            if (player.facingDirection == 2) atkX -= offset;
            if (player.facingDirection == 3) atkX += offset;

            attackHitbox.setBounds(atkX, atkY, 288, 288);

            for (Enemy enemy : enemies) {
                if (attackHitbox.intersects(enemy.getBounds())) {
                    enemy.isActive = false;
                    score += 100;
                    spawnParticles(enemy.x + 40, enemy.y + 40);
                }
            }
        }
    }

    private void updateLists() {
        synchronized (enemies) {
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                enemy.update();
                if (!enemy.isActive) {
                    enemyIterator.remove();
                }
            }
        }

        synchronized (particles) {
            Iterator<Particle> particleIterator = particles.iterator();
            while (particleIterator.hasNext()) {
                Particle p = particleIterator.next();
                p.update();
                if (p.life <= 0) {
                    particleIterator.remove();
                }
            }
        }
    }


    private void checkPlayerCollision() {
        if (hitCooldown > 0) return;

        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                player.currentHealth--;
                enemy.isActive = false;
                hitCooldown = 60;

                if (player.currentHealth <= 0) {
                    isGameOver = true;
                    gamePanel.showReturnToMenuButton();
                }
                break;
            }
        }
    }

    private void restartGame() {
        synchronized (enemies) { enemies.clear(); }
        synchronized (particles) { particles.clear(); }
        score = 0;
        level = 1;
        player.x = 960;
        player.y = 540;
        player.currentHealth = player.maxHealth;
        isGameOver = false;

        gamePanel.hideReturnToMenuButton();
    }

    public void returnToMenu(){
        isRunning = false;

    }

    private void spawnParticles(int x, int y) {
        synchronized (particles) {
            for (int i = 0; i < 15; i++) {
                particles.add(new Particle(x, y, Color.GREEN));
            }
        }
    }

}