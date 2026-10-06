import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener {
  // Map representation (1 = wall, 0 = empty space)
  int[][] map = {
    {1, 1, 1, 1, 1},
    {1, 0, 0, 0, 1},
    {1, 0, 1, 0, 1},
    {1, 0, 0, 0, 1},
    {1, 1, 1, 1, 1}
  };
  
  int tileSize = 100;
// Player position
  double playerX = 2.0;
  double playerY = 1.5;
  double playerAngle = 0.0;
  double playerRad = 0.15;
// Player direction vector
  double dirX = 1.0;
  double dirY = 0.0;
  double fov = 0.0;
// Camera plane (perpendicular to direction)
  double planeX = 0.0;
  double planeY = 0.66;
  // Movement
  boolean up;
  boolean down;
  boolean left;
  boolean right;
  // collision
  boolean isWall(double x, double y) {
    // Check the left side
    if (map[(int)(y - playerRad)][(int)(x - playerRad)] == 1) {
        return true;
    }

    if (map[(int)(y - playerRad)][(int)(x + playerRad)] == 1) {
        return true;
    }

    if (map[(int)(y + playerRad)][(int)(x - playerRad)] == 1) {
        return true;
    }

    if (map[(int)(y + playerRad)][(int)(x + playerRad)] == 1) {
        return true;
    }

    return false;
  }
  double distance = 0.0;
  public Main() {
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(map[0].length * tileSize, map.length * tileSize));
    setFocusable(true);
    addKeyListener(this);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (int y = 0; y < map.length; y++) {
      for (int x = 0; x < map[y].length; x++) {
        if (map[y][x] == 1) {
          g.setColor(Color.WHITE);
          g.fillRect(
            x * tileSize + 1,
            y * tileSize + 1,
            tileSize - 1,
            tileSize - 1
          );
        }
        // Draws player
        g.setColor(Color.RED);
        g.fillOval(
          (int)(playerX * tileSize - 10),
          (int)(playerY * tileSize - 10),
          20,
          20
        );
        g.setColor(Color.YELLOW);
        int playerScreenX = (int)(playerX * tileSize);
        int playerScreenY = (int)(playerY * tileSize);
        int dirLineLength = 30;
        int directX = (int)(Math.cos(playerAngle) * dirLineLength);
        int directY = (int)(Math.sin(playerAngle) * dirLineLength);
        g.drawLine(
          playerScreenX,
          playerScreenY,
          playerScreenX + directX,
          playerScreenY + directY
        );
      
        double rayAngle = playerAngle;
        double rayX = Math.cos(rayAngle);
        double rayY = Math.sin(rayAngle);
        double rayDistance = 0.0;
        double rayStep = 0.01;
        double rayPosX = playerX;
        double rayPosY = playerY;
        while(!isWall(rayPosX, rayPosY)) {
          rayPosX += rayX * rayStep;
          rayPosY += rayY * rayStep;
          rayDistance += rayStep;
        }
        g.setColor(Color.YELLOW);
        g.drawLine(
          (int)(playerX * tileSize),
          (int)(playerY * tileSize),
          (int)(playerX * tileSize),
          (int)(playerY * tileSize)
        );
      }
    }
  }
  // Checks if you pressed the key to move the player
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_W) {
      up = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_S) {
      down = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_A) {
      left = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_D) {
      right = true;
    }
  }
// checks if the key was let go to stop moving the player
  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_W) {
      up = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_S) {
      down = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_A) {
      left = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_D) {
      right = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
  //updates the player position when keys pressed
  void update() {
    double speed = 0.05;
    double rotationSpeed = 0.1;
    
    if (left) {
      playerAngle -= rotationSpeed;
    }
    if (right) {
      playerAngle += rotationSpeed;
    }
    double cosAngle = Math.cos(playerAngle) * speed;
    double sinAngle = Math.sin(playerAngle) * speed;
    double newX = playerX;
    double newY = playerY;
    if (up) {
      newX += cosAngle;
      newY += sinAngle;
    }
    if (down) {
      newX -= cosAngle;
      newY -= sinAngle;
    }
    // Check for collisions before updating player position
    if (!isWall(newX, newY)) {
      playerX = newX;
      playerY = newY;
    }
  }
  // Starts the game
  public void startGame() {
    while (true) {
      update();
      repaint();
      try {
        Thread.sleep(16); // ~60 FPS
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
// Creates the main window and starts the game
  public static void main(String[] args) {
    JFrame window = new JFrame("2D Raycaster Map");
    Main game = new Main();
    window.setContentPane(game);
    window.pack();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLocationRelativeTo(null);
    window.setVisible(true);
    game.requestFocusInWindow();
    game.startGame();
  }

}