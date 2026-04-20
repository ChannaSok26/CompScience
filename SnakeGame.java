import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new GamePanel());
        frame.setVisible(true);
    }
}

class GamePanel extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private Deque<int[]> snake;

    public GamePanel() {
        setFocusable(true);
        setBackground(new Color(50, 50, 50));
        initializeSnake();
    }

    private void initializeSnake() {
        snake = new LinkedList<>();
        // Create 3-segment snake in center facing right
        snake.addFirst(new int[]{10, 10}); // Head
        snake.addLast(new int[]{9, 10});   // Middle
        snake.addLast(new int[]{8, 10});   // Tail
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw grid
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i <= GRID_WIDTH; i++) {
            g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        }
        for (int i = 0; i <= GRID_HEIGHT; i++) {
            g2d.drawLine(0, i * CELL_SIZE, GRID_WIDTH * CELL_SIZE, i * CELL_SIZE);
        }

        // Draw snake
        g2d.setColor(new Color(0, 200, 0));
        for (int[] segment : snake) {
            int x = segment[0] * CELL_SIZE;
            int y = segment[1] * CELL_SIZE;
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }
}
