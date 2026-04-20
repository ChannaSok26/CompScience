import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private static final int TIMER_DELAY = 150;
    
    private Deque<int[]> snake;
    private int dirX = 1;  // Direction: moving right
    private int dirY = 0;
    private int nextDirX = 1;  // Next direction (for buffering key presses)
    private int nextDirY = 0;
    private int[] food;
    private int score = 0;
    private boolean gameOver = false;
    private Random random = new Random();

    public GamePanel() {
        setFocusable(true);
        setBackground(new Color(50, 50, 50));
        initializeSnake();
        spawnFood();
        setupKeyListener();
        startTimer();
    }

    private void initializeSnake() {
        snake = new LinkedList<>();
        // Create 3-segment snake in center facing right
        snake.addFirst(new int[]{10, 10}); // Head
        snake.addLast(new int[]{9, 10});   // Middle
        snake.addLast(new int[]{8, 10});   // Tail
    }

    private void spawnFood() {
        int foodX, foodY;
        boolean validPosition;
        do {
            validPosition = true;
            foodX = random.nextInt(GRID_WIDTH);
            foodY = random.nextInt(GRID_HEIGHT);
            
            // Check if position is occupied by snake
            for (int[] segment : snake) {
                if (segment[0] == foodX && segment[1] == foodY) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
        
        food = new int[]{foodX, foodY};
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
                    reset();
                    return;
                }
                
                if (gameOver) return;
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (dirY == 0) {  // Can't reverse
                            nextDirX = 0;
                            nextDirY = -1;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (dirY == 0) {  // Can't reverse
                            nextDirX = 0;
                            nextDirY = 1;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (dirX == 0) {  // Can't reverse
                            nextDirX = -1;
                            nextDirY = 0;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (dirX == 0) {  // Can't reverse
                            nextDirX = 1;
                            nextDirY = 0;
                        }
                        break;
                }
            }
        });
    }

    private void reset() {
        snake.clear();
        initializeSnake();
        dirX = 1;
        dirY = 0;
        nextDirX = 1;
        nextDirY = 0;
        score = 0;
        gameOver = false;
        spawnFood();
        repaint();
    }

    private void startTimer() {
        javax.swing.Timer timer = new javax.swing.Timer(TIMER_DELAY, e -> {
            if (!gameOver) {
                moveSnake();
            }
        });
        timer.start();
    }

    private void moveSnake() {
        // Update direction from queued input
        dirX = nextDirX;
        dirY = nextDirY;

        // Calculate new head position
        int[] head = snake.getFirst();
        int newHeadX = (head[0] + dirX) % GRID_WIDTH;
        int newHeadY = (head[1] + dirY) % GRID_HEIGHT;

        // Handle negative wrapping
        if (newHeadX < 0) newHeadX = GRID_WIDTH - 1;
        if (newHeadY < 0) newHeadY = GRID_HEIGHT - 1;

        // Check collision with own body
        for (int[] segment : snake) {
            if (segment[0] == newHeadX && segment[1] == newHeadY) {
                gameOver = true;
                repaint();
                return;
            }
        }

        // Add new head
        snake.addFirst(new int[]{newHeadX, newHeadY});

        // Check if food eaten
        if (newHeadX == food[0] && newHeadY == food[1]) {
            score += 10;
            spawnFood();
        } else {
            // Remove tail only if no food eaten
            snake.removeLast();
        }

        repaint();
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

        // Draw food
        if (food != null) {
            g2d.setColor(new Color(255, 100, 100));
            int x = food[0] * CELL_SIZE;
            int y = food[1] * CELL_SIZE;
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // Draw snake
        g2d.setColor(new Color(0, 200, 0));
        for (int[] segment : snake) {
            int x = segment[0] * CELL_SIZE;
            int y = segment[1] * CELL_SIZE;
            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // Draw score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Score: " + score, 10, 20);

        // Draw game over message
        if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g2d.getFontMetrics();
            String gameOverText = "GAME OVER";
            int textX = (getWidth() - fm.stringWidth(gameOverText)) / 2;
            int textY = getHeight() / 2 - 30;
            g2d.drawString(gameOverText, textX, textY);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String scoreText = "Final Score: " + score;
            fm = g2d.getFontMetrics();
            textX = (getWidth() - fm.stringWidth(scoreText)) / 2;
            g2d.drawString(scoreText, textX, textY + 50);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            String resetText = "Press R to Play Again";
            fm = g2d.getFontMetrics();
            textX = (getWidth() - fm.stringWidth(resetText)) / 2;
            g2d.drawString(resetText, textX, textY + 100);
        }
    }
}
