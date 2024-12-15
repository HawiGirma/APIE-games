import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class WordSearchGame {

    public static class WordSearchGUI {
        private JFrame frame;
        private JTextField[][] grid;
        private ArrayList<String> wordList;
        private char[][] wordGrid;
        private int gridSize = 10;
        private int score = 0; // Track the score

        public WordSearchGUI() {
            frame = new JFrame("Word Search Puzzle");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Generate grid and words
            wordList = new ArrayList<>();
            wordList.add("JAVA");
            wordList.add("PUZZLE");
            wordList.add("GAME");
            wordList.add("SWING");

            wordGrid = new char[gridSize][gridSize];
            fillGridWithWords();

            // Create GUI grid
            JPanel gridPanel = new JPanel();
            gridPanel.setLayout(new GridLayout(gridSize, gridSize));
            grid = new JTextField[gridSize][gridSize];

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    grid[i][j] = new JTextField(String.valueOf(wordGrid[i][j]));
                    grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                    grid[i][j].setFont(new Font("Arial", Font.BOLD, 16));
                    grid[i][j].setEditable(false);
                    gridPanel.add(grid[i][j]);
                }
            }

            frame.add(gridPanel, BorderLayout.CENTER);

            // Add buttons
            JButton checkButton = new JButton("Check Word");
            JButton resetButton = new JButton("Reset");
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(checkButton);
            buttonPanel.add(resetButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Add instructions
            JTextArea instructions = new JTextArea("Find the hidden words in the grid!");
            instructions.setEditable(false);
            instructions.setFont(new Font("Arial", Font.ITALIC, 14));
            frame.add(instructions, BorderLayout.NORTH);

            // Add action listeners
            checkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkWord();
                }
            });

            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetGrid();
                }
            });

            frame.setSize(600, 600);
            frame.setVisible(true);
        }

        private void fillGridWithWords() {
            Random random = new Random();

            // Place words randomly in the grid
            for (String word : wordList) {
                boolean placed = false;

                while (!placed) {
                    int row = random.nextInt(gridSize);
                    int col = random.nextInt(gridSize);
                    int direction = random.nextInt(2); // 0 = horizontal, 1 = vertical

                    if (direction == 0) {
                        // Horizontal placement
                        if (col + word.length() <= gridSize) {
                            boolean canPlace = true;
                            for (int i = 0; i < word.length(); i++) {
                                if (wordGrid[row][col + i] != '\u0000' && wordGrid[row][col + i] != word.charAt(i)) {
                                    canPlace = false;
                                    break;
                                }
                            }
                            if (canPlace) {
                                for (int i = 0; i < word.length(); i++) {
                                    wordGrid[row][col + i] = word.charAt(i);
                                }
                                placed = true;
                            }
                        }
                    } else {
                        // Vertical placement
                        if (row + word.length() <= gridSize) {
                            boolean canPlace = true;
                            for (int i = 0; i < word.length(); i++) {
                                if (wordGrid[row + i][col] != '\u0000' && wordGrid[row + i][col] != word.charAt(i)) {
                                    canPlace = false;
                                    break;
                                }
                            }
                            if (canPlace) {
                                for (int i = 0; i < word.length(); i++) {
                                    wordGrid[row + i][col] = word.charAt(i);
                                }
                                placed = true;
                            }
                        }
                    }
                }
            }

            // Fill remaining cells with random letters
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (wordGrid[i][j] == '\u0000') {
                        wordGrid[i][j] = (char) ('A' + random.nextInt(26));
                    }
                }
            }
        }

        private void checkWord() {
            String inputWord = JOptionPane.showInputDialog(frame, "Enter the word you found:");
            if (inputWord == null || inputWord.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid word.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            inputWord = inputWord.toUpperCase();

            boolean wordFound = false;
            ArrayList<int[]> foundCells = new ArrayList<>(); // To store the grid cells of the found word

            // Check if the word exists in the grid
            for (String word : wordList) {
                if (inputWord.equals(word)) {
                    wordFound = true;
                    foundCells = findWordInGrid(word); // Get the positions of the word
                    break;
                }
            }

            if (wordFound) {
                score++; // Increase score when the word is correct
                // Highlight the cells of the found word
                for (int[] cell : foundCells) {
                    grid[cell[0]][cell[1]].setBackground(Color.YELLOW); // Highlight the cell
                }

                JOptionPane.showMessageDialog(frame, "Congratulations! You found a word!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (score == wordList.size()) {
                    JOptionPane.showMessageDialog(frame, "Congratulations! You have found all the words and won!", "You Win!", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "The word " + inputWord + " is not in the grid.", "Try Again", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Method to find the coordinates of the word in the grid
        private ArrayList<int[]> findWordInGrid(String word) {
            ArrayList<int[]> positions = new ArrayList<>();
            for (int row = 0; row < gridSize; row++) {
                for (int col = 0; col < gridSize; col++) {
                    if (checkWordAtPosition(row, col, word)) {
                        for (int i = 0; i < word.length(); i++) {
                            positions.add(new int[]{row, col + i}); // Add horizontal positions
                        }
                        return positions;
                    }
                    if (checkWordAtPositionVertical(row, col, word)) {
                        for (int i = 0; i < word.length(); i++) {
                            positions.add(new int[]{row + i, col}); // Add vertical positions
                        }
                        return positions;
                    }
                }
            }
            return positions;
        }

        private boolean checkWordAtPosition(int row, int col, String word) {
            if (col + word.length() <= gridSize) {
                for (int i = 0; i < word.length(); i++) {
                    if (wordGrid[row][col + i] != word.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private boolean checkWordAtPositionVertical(int row, int col, String word) {
            if (row + word.length() <= gridSize) {
                for (int i = 0; i < word.length(); i++) {
                    if (wordGrid[row + i][col] != word.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private void resetGrid() {
            wordGrid = new char[gridSize][gridSize];
            fillGridWithWords();
            score = 0; // Reset score on grid reset

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    grid[i][j].setText(String.valueOf(wordGrid[i][j]));
                    grid[i][j].setBackground(Color.WHITE); // Reset the background color
                }
            }

            JOptionPane.showMessageDialog(frame, "The grid has been reset.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordSearchGUI::new);
    }
}
