import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
  int[][] map = {
    {1, 1, 1, 1, 1},
    {1, 0, 0, 0, 1},
    {1, 0, 0, 0, 1},
    {1, 1, 1, 1, 1}
  };

  int tileSize = 100;

  @Override
  protected void paintComponent(Graphics g) {
    
    super.paintComponent(g);

    for (int y = 0; y < map.length; y++) {

      for (int x = 0; x < map[y].length; x++) {

        if (map[y][x] == 1) {

          g.setColor(Color.WHITE);
          g.fillRect(
            x * tileSize,
            y * tileSize,
            tileSize,
            tileSize
          );
        }
      }
    }
  }

  public static void main(String[] args) {
    JFrame window = new JFrame("2D Raycaster Map");
    Main game = new Main();
    window.add(game);
    window.setSize(800, 600);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setVisible(true);
  }
}
  
