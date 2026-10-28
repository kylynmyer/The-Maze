import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener {
  // Map representation (1 = wall, 0 = empty space)
  int[][] map = {
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},
    {1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
    {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
    {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}

  };
  
  int tileSize = 30;
  // Death mechanics
  double deathDim = 1.0;
  boolean deathCountDown = false;
  String deathTimer = "Death incoming: " + "5s";
  // Task description
  String stepCount = "(Step " + tutorialStep + "/" + totalTutorialSteps + ")";
  String taskDescription = "Task: Finish the tutorial";
  String taskHint = "Press 'A' and 'D' to look left and right " + stepCount;
  String taskHint2 = "Press 'W' and 'S' to move forward and backward " + stepCount;
  String taskHint3 = "Find the key to complete the tutorial " + stepCount;
  String taskHint2 = "Press 'W' and 'S' to move forward and backward";
  String taskHint3 = "Find the key to complete the tutorial";
  boolean tutorialCompleted = false;
  int tutorialStep = 0;
  int totalTutorialSteps = 0;
// Player position
  double playerX = 2.0;
  double playerY = 1.5;
  double playerAngle = 0.0;
  double playerRad = 0.15;
  double playerSize = 0.3;
// Player direction vector
  double dirX = 1.0;
  double dirY = 0.0;
  double fov = Math.toRadians(60);
// Camera plane (perpendicular to direction)
  double planeX = 0.0;
  double planeY = 0.66;
  // Movement
  boolean up;
  boolean down;
  boolean left;
  boolean right;
  // Items
  boolean dropped = true;
  double itemX = 1.5;
  double itemY = 2.5;
  double itemZ = 0.0;
  double itemSize = 0.5;
  int keyCount = 0;
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
      }
    }
    // Draws player
    g.setColor(Color.RED);
    g.fillOval(
      (int)(playerX * tileSize - 10),
      (int)(playerY * tileSize - 10),
      20,
      20
    );

    // Draws item
    if (dropped) {
      g.setColor(Color.GREEN);
      g.fillOval(
        (int)(itemX * tileSize - 10),
        (int)(itemY * tileSize - 10),
        10,
        10
      );
    }

    // RAY
    int rayCount = 100;
    double[] wallDistances = new double[rayCount];

    for(int i=0; i<rayCount; i++){
      
      // The angle of this ray
      double rayAngle = playerAngle - fov / 2 + fov * i / (rayCount - 1);

      // Direction the ray travels
      double rayX = Math.cos(rayAngle);
      double rayY = Math.sin(rayAngle);

      // Start the ray at the player
      double rayPosX = playerX;
      double rayPosY = playerY;

      // How far the ray has traveled
      double rayDistance = 0;

      // Size of each ray step
      double rayStep = 0.02;

      // Move the ray forward until it hits a wall
      while (!isWall(rayPosX, rayPosY)) {
          rayPosX += rayX * rayStep;
          rayPosY += rayY * rayStep;

          rayDistance += rayStep;
      }
      wallDistances[i] = rayDistance;

      double wallHeight = 300 / rayDistance;

      int wallTop = 300 - (int)(wallHeight / 2);
      int wallBottom = 300 + (int)(wallHeight / 2);

      int brightness = (int)(255 / (1 + rayDistance * 0.3));

      brightness = Math.max(0, Math.min(255 , brightness));

      Color wallColor = new Color(brightness, brightness, brightness);

      g.setColor(wallColor);

      g.fillRect(
        600 + i * 5,
        wallTop,
        5,
        (int)wallHeight
      );

      // Draw the ray
      g.setColor(Color.YELLOW);

      g.drawLine(
          (int)(playerX * tileSize),
          (int)(playerY * tileSize),
          (int)(rayPosX * tileSize),
          (int)(rayPosY * tileSize)
      );
    }
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 20));
    g.drawString(taskDescription, 20, 30);
    g.drawString(taskHint, 20, 60);
    g.drawString("Press 'W' and 'S' to move forward and backward", 20, 90);
    g.drawString("Find the key", 20, 120);
    if(dropped){
      // pos relative to player
      double dx = itemX - playerX;
      double dy = itemY - playerY;
      //distance to item
      double distance = Math.sqrt(dx * dx + dy * dy);
      // angle to item
      double angleToItem = Math.atan2(dy, dx);
      double relativeAngle = angleToItem - playerAngle;
      // normalize relative angle to [-pi, pi]
      while (relativeAngle > Math.PI){
        relativeAngle -= 2 * Math.PI;
      }
      while (relativeAngle < -Math.PI){
        relativeAngle += 2 * Math.PI;
      }
      // check if item is in FOV
      if (Math.abs(relativeAngle) < fov / 2 && distance > 0.1){
        //which wall ray lines up with the item
        int rayIndex = (int)((relativeAngle + fov / 2) / fov * rayCount);
        rayIndex = Math.max(0, Math.min(rayCount - 1, rayIndex));
        //only draw if the wall is farther away than the key
        if(distance < wallDistances[rayIndex]){
          // converts the relative angle to screen position
          int screenX = (int)(600 + (relativeAngle + fov / 2) / fov * 500);
          // converts the distance to screen size
          int screenSize = (int)(100 / distance);
          screenSize = Math.max(4, Math.min(60, screenSize));
          //bobbing effect
          int bobbing = (int)(Math.sin(itemZ) * 8);
          // centers the item on the screen
          int screenY = 300 - screenSize / 2 + bobbing;
          //Shading based on distance
          int brightness = (int)(255 / (1 + distance * 0.3));
          brightness = Math.max(0, Math.min(255, brightness));
          //draws the item on the screen
          g.setColor(new Color(0, brightness, 0));
          g.fillOval(
            screenX - screenSize / 4,
            screenY,
            screenSize / 2,
            screenSize / 2
          );
          g.fillRect(
            screenX,
            screenY + screenSize / 4,
            screenSize / 2,
            Math.max(2, screenSize / 8)
          );
          g.fillRect(
            screenX + screenSize / 3,
            screenY + screenSize / 4,
            Math.max(2, screenSize / 8),
            screenSize / 3
          );
        }
      }
    }
    if(deathDim < 1.0){
      Graphics2D g2 = (Graphics2D) g.create();
      int alpha = (int)(255 * (1.0 - deathDim));
      g2.setColor(new Color(0, 0, 0, alpha));
      g2.fillRect(0, 0, getWidth(), getHeight());
      g2.dispose();
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
    double speed = 0.03;
    double rotationSpeed = 0.05;

    // Rotate the player
    if (left) {
      playerAngle -= rotationSpeed;
    }

    if (right) {
      playerAngle += rotationSpeed;
    }

    // Calculate movement direction
    double moveX = Math.cos(playerAngle) * speed;
    double moveY = Math.sin(playerAngle) * speed;

    double newX = playerX;
    double newY = playerY;

    // Move forward
    if (up) {
      newX += moveX;
      newY += moveY;
    }

    // Move backward
    if (down) {
      newX -= moveX;
      newY -= moveY;
    }

    // Check X movement separately
    if (!isWall(newX, playerY)) {
      playerX = newX;
    }

    // Check Y movement separately
    if (!isWall(playerX, newY)) {
      playerY = newY;
    }
    if(dropped){
      itemZ += 0.08;
    }
    if(playerX > itemX - 0.15 && playerX < itemX + 0.15 && playerY > itemY - 0.15 && playerY < itemY + 0.15){
      dropped = false;
    }
    if(!dropped && keyCount == 0){
      map[1][5] = 0;
      itemX = 6.5;
      itemY = 3.5;
      dropped = true;
      keyCount++;
    }else if(keyCount == 1 && !dropped){
      map[4][8] = 0;
      itemX = 10.5;
      itemY = 7.5;
      dropped = true;
      keyCount++;
      deathCountDown = true;
    }else if(keyCount == 2 && !dropped){
      map[6][6] = 0;
      dropped = true;
      keyCount = 0;
      deathCountDown = false;
      deathDim = 1.0;
    }
    if(deathCountDown && deathDim > 0.0){
      deathDim = Math.max(0.0, deathDim - 0.001);
    }
    if(keyCount == 0){
      totalTutorialSteps = 2;
      if(tutorialStep == 2){
        tutorialCompleted = true;
      }
        if(left){
          tutorialStep++;
        }else if(right){
          tutorialStep++;
        }
        
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
    window.setSize(1115, 600);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLocationRelativeTo(null);
    window.setVisible(true);
    game.requestFocusInWindow();
    game.startGame();
  }

}