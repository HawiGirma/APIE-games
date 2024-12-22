import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MazePuzzle {

    public static class MazeGame extends JPanel {
        private int mazeSize;
        private int[][] maze;
        private int playerX, playerY;  // Player's position
        private int endX, endY;        // End position
        private JButton resetButton, nextLevelButton, quitButton;
        private String difficulty;
        private int levelCount = 0;  // Track the number of completed levels

        public MazeGame(String difficulty) {
            this.difficulty = difficulty;
            setMazeProperties();
            maze = new int[mazeSize][mazeSize];
            playerX = 1;
            playerY = 1;
            endX = mazeSize - 2;
            endY = mazeSize - 2;

            // Generate maze
            generateMaze();

            // Set up key listener for movement
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    movePlayer(e);
                }
            });

            // Set up buttons
            resetButton = new JButton("Reset");
            nextLevelButton = new JButton("Next Level");
            quitButton = new JButton("Quit");

            // Button actions
            resetButton.addActionListener(e -> {
                resetGame();
                requestFocusInWindow(); // Re-focus to the game panel after reset
            });
            nextLevelButton.addActionListener(e -> {
                nextLevel();
                requestFocusInWindow(); // Re-focus to the game panel after next level
            });
            quitButton.addActionListener(e -> quitGame());

            // Set up button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(resetButton);
            buttonPanel.add(nextLevelButton);
            buttonPanel.add(quitButton);
            setLayout(new BorderLayout());
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void setMazeProperties() {
            switch (difficulty) {
                case "Easy":
                    mazeSize = 8;
                    break;
                case "Medium":
                    mazeSize = 12;
                    break;
                case "Hard":
                    mazeSize = 16;
                    break;
            }
        }

        private void generateMaze() {
            // Fill the maze with walls (1)
            for (int i = 0; i < mazeSize; i++) {
                for (int j = 0; j < mazeSize; j++) {
                    maze[i][j] = 1; // All walls
                }
            }

            // Create paths (0)
            for (int i = 1; i < mazeSize - 1; i++) {
                for (int j = 1; j < mazeSize - 1; j++) {
                    maze[i][j] = 0; // Open space
                }
            }

            // Set some random walls to add complexity
            int wallCount = (int) (mazeSize * mazeSize * 0.3); // Adjust wall density based on difficulty
            for (int i = 0; i < wallCount; i++) {
                int x = (int) (Math.random() * mazeSize);
                int y = (int) (Math.random() * mazeSize);
                if (maze[x][y] == 0) { // Don't overwrite existing paths
                    maze[x][y] = 1; // Place a wall
                }
            }

            // Set start and end points
            maze[playerX][playerY] = 0;
            maze[endX][endY] = 0;
        }

        private void movePlayer(KeyEvent e) {
            int key = e.getKeyCode();
            int newX = playerX, newY = playerY;

            if (key == KeyEvent.VK_UP) {
                newX = playerX - 1;
            } else if (key == KeyEvent.VK_DOWN) {
                newX = playerX + 1;
            } else if (key == KeyEvent.VK_LEFT) {
                newY = playerY - 1;
            } else if (key == KeyEvent.VK_RIGHT) {
                newY = playerY + 1;
            }

            // Check if the new position is a valid move
            if (newX >= 0 && newX < mazeSize && newY >= 0 && newY < mazeSize && maze[newX][newY] == 0) {
                playerX = newX;
                playerY = newY;
            }

            // Check if the player reached the end
            if (playerX == endX && playerY == endY) {
                JOptionPane.showMessageDialog(this, "You won! You reached the end!");
                nextLevel();
            }

            repaint();
        }

        private void resetGame() {
            playerX = 1;
            playerY = 1;
            generateMaze();
            repaint();
        }

        private void nextLevel() {
            levelCount++;
            if (levelCount >= 3) {
                JOptionPane.showMessageDialog(this, "Congratulations! You have completed all levels!", "You Won!", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);  // Exit the game after completing 3 levels
            } else {
                // Increase the maze size for the next level
                mazeSize += 2;  // Increase size by 2 for more difficulty
                maze = new int[mazeSize][mazeSize];
                playerX = 1;
                playerY = 1;
                endX = mazeSize - 2;
                endY = mazeSize - 2;
                generateMaze();
                repaint();
            }
        }

        private void quitGame() {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);  // Exit the game if the user confirms
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the maze
            for (int i = 0; i < mazeSize; i++) {
                for (int j = 0; j < mazeSize; j++) {
                    if (maze[i][j] == 1) {
                        g.setColor(Color.BLACK); // Wall
                    } else {
                        g.setColor(Color.WHITE); // Path
                    }
                    g.fillRect(j * 30, i * 30, 30, 30);
                    g.setColor(Color.GRAY);
                    g.drawRect(j * 30, i * 30, 30, 30);
                }
            }

            // Draw the player
            g.setColor(Color.RED);
            g.fillRect(playerY * 30, playerX * 30, 30, 30);

            // Draw the end point
            g.setColor(Color.GREEN);
            g.fillRect(endY * 30, endX * 30, 30, 30);
        }
    }

    public static void main(String[] args) {
        // Show difficulty selection dialog
        String[] options = {"Easy", "Medium", "Hard"};
        String difficulty = (String) JOptionPane.showInputDialog(
                null,
                "Choose Difficulty Level:",
                "Maze Puzzle - Select Difficulty",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        // Start the game with the selected difficulty
        if (difficulty != null) {
            JFrame frame = new JFrame("Maze Puzzle");
            MazeGame mazeGame = new MazeGame(difficulty);
            frame.add(mazeGame);
            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }
}
