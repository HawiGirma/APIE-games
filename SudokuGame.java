import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGame {

    private int gridSize; // Sudoku grid size (modifiable based on level)
    private int subgridSize; // Size of subgrids

    private JFrame frame;
    private JTextField[][] cells; // Stores the players' values
    private int[][] solutionGrid; // Holds the complete solution
    private int[][] puzzleGrid;   // Holds the generated puzzle

    public SudokuGame() {
        String[] levels = {"Easy (4x4)", "Medium (6x6)", "Hard (9x9)"};
        String level = (String) JOptionPane.showInputDialog(null, "Select difficulty level:", "Level Selection", JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);

        if (level == null) {
            System.exit(0); // Exit if no level is chosen
        }

        switch (level) {
            case "Easy (4x4)":
                gridSize = 4;
                subgridSize = 2;
                break;
            case "Medium (6x6)":
                gridSize = 6;
                subgridSize = 2;
                break;
            case "Hard (9x9)":
                gridSize = 9;
                subgridSize = 3;
                break;
        }

        frame = new JFrame("Sudoku Solver - " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create Sudoku grid
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize));
        cells = new JTextField[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                gridPanel.add(cells[i][j]);
            }
        }

        frame.add(gridPanel, BorderLayout.CENTER);

        // Add buttons
        JButton solveButton = new JButton("Solve");
        JButton clearButton = new JButton("Clear");
        JButton generateButton = new JButton("Generate");
        JButton checkButton = new JButton("Check");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(checkButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSudoku();
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSolution();
            }
        });

        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private void solveSudoku() {
        int[][] grid = new int[gridSize][gridSize];
        try {
            // Parse the input grid
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    String text = cells[i][j].getText().trim();
                    if (!text.isEmpty()) {
                        int value = Integer.parseInt(text);
                        if (value < 1 || value > gridSize) {
                            throw new NumberFormatException("Invalid number at cell (" + (i + 1) + ", " + (j + 1) + ").");
                        }
                        grid[i][j] = value;
                    } else {
                        grid[i][j] = 0;
                    }
                }
            }

            // Solve the grid
            if (solve(grid)) {
                // Update the grid
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        cells[i][j].setText(String.valueOf(grid[i][j]));
                    }
                }
                JOptionPane.showMessageDialog(frame, "Sudoku solved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No solution exists for the given Sudoku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                cells[i][j].setText("");
                cells[i][j].setEditable(true);
            }
        }
    }

    private void generateSudoku() {
        solutionGrid = new int[gridSize][gridSize];
        puzzleGrid = new int[gridSize][gridSize];
        solve(solutionGrid); // Generate a complete solution

        // Copy solution to puzzleGrid and remove numbers to create a puzzle
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                puzzleGrid[i][j] = solutionGrid[i][j];
                if (Math.random() < 0.5) {
                    puzzleGrid[i][j] = 0;
                }
            }
        }

        // Display the puzzle
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (puzzleGrid[i][j] != 0) {
                    cells[i][j].setText(String.valueOf(puzzleGrid[i][j]));
                    cells[i][j].setEditable(false);
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                }
            }
        }
    }

    private void checkSolution() {
        try {
            // Check if the grid is completely filled
            boolean isCompleted = true;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    String text = cells[i][j].getText().trim();
                    if (text.isEmpty()) {
                        isCompleted = false;
                        break;
                    }
                }
                if (!isCompleted) {
                    break;
                }
            }

            if (!isCompleted) {
                throw new Exception("Please complete the Sudoku before checking.");
            }

            // Validate input values
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    String text = cells[i][j].getText().trim();
                    if (!text.isEmpty()) {
                        int value = Integer.parseInt(text);
                        if (value < 1 || value > gridSize) {
                            throw new NumberFormatException("Invalid input at cell (" + (i + 1) + ", " + (j + 1) + ").");
                        }
                    }
                }
            }

            // Check if the solution is correct
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    String text = cells[i][j].getText().trim();
                    if (!text.isEmpty()) {
                        int value = Integer.parseInt(text);
                        if (value != solutionGrid[i][j]) {
                            JOptionPane.showMessageDialog(frame, "Incorrect solution.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(frame, "Congratulations! The solution is correct.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean solve(int[][] grid) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= gridSize; num++) {
                        if (isValidMove(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solve(grid)) {
                                return true;
                            }
                            grid[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid number found
                }
            }
        }
        return true; // Solved
    }

    private boolean isValidMove(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < gridSize; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }

        int subGridRowStart = (row / subgridSize) * subgridSize;
        int subGridColStart = (col / subgridSize) * subgridSize;
        for (int i = 0; i < subgridSize; i++) {
            for (int j = 0; j < subgridSize; j++) {
                if (grid[subGridRowStart + i][subGridColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
