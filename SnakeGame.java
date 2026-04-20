import javax.swing.*;

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
    public GamePanel() {
        setFocusable(true);
    }
}
