import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class SlidingPuzzle {

    public static class SlidingPuzzleGUI {

        private final int gridSize = 4; // 4x4 grid which corresponds to 15 tile puzzle
        private JButton[][] buttons;
        private int emptyRow, emptyCol; // Position of the empty tile
        private JFrame frame;

        public SlidingPuzzleGUI() {
            frame = new JFrame("Sliding Puzzle - 15 Puzzle");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());//Sets the layout of the frame to BorderLayout, which organizes components into different sections (center, north, south, etc.).
            JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
            buttons = new JButton[gridSize][gridSize];// used to make the tiles.

            initializeGrid(gridPanel);

            JButton shuffleButton = new JButton("Shuffle");
            JButton resetButton = new JButton("Reset");

            shuffleButton.addActionListener(e -> shuffleGrid());
            resetButton.addActionListener(e -> resetGrid());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(shuffleButton);
            buttonPanel.add(resetButton);

            frame.add(gridPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setSize(500, 500);
            frame.setVisible(true);
        }

        private void initializeGrid(JPanel gridPanel) {
            // Populate grid with numbers 1-15 and an empty space
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int i = 1; i < gridSize * gridSize; i++) {
                numbers.add(i);
            }
            numbers.add(0); // 0 represents the empty tile

            emptyRow = gridSize - 1;
            emptyCol = gridSize - 1;

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int value = numbers.get(i * gridSize + j);
                    JButton button = new JButton(value == 0 ? "" : String.valueOf(value));

                    button.setFont(new Font("Arial", Font.BOLD, 24));
                    button.setFocusable(false);

                    button.addActionListener(new TileClickListener(i, j));

                    buttons[i][j] = button;
                    gridPanel.add(button);
                }
            }
        }

        private void shuffleGrid() {
            ArrayList<Integer> numbers = new ArrayList<>(); //<integer> indicates that the "ArrayList" will hold integer objects.
            for (int i = 1; i < gridSize * gridSize; i++) {
                numbers.add(i); //adds from 1-15.
            }
            numbers.add(0); //0 is added at the end to represent the empty tile

            do {
                Collections.shuffle(numbers);
            } while (!isSolvable(numbers));//the condition will ensure the shuffle produces solvable puzzle.
            // Assigning shuffle numbers to the grid
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int value = numbers.get(i * gridSize + j); //retriving shuffled values. 
                    buttons[i][j].setText(value == 0 ? "" : String.valueOf(value));//assigning the value if it's 0 then empty string for empty tile is added. 
                    if (value == 0) {//f the tile is empty (value 0), the emptyRow and emptyCol variables are updated to the current position (i, j) to keep track of the empty tile's location.
                        emptyRow = i;
                        emptyCol = j;
                    }
                }
            }
        }

        private boolean isSolvable(ArrayList<Integer> numbers) {
            int inversions = 0;

            for (int i = 0; i < numbers.size(); i++) {
                for (int j = i + 1; j < numbers.size(); j++) {
                    int a = numbers.get(i);
                    int b = numbers.get(j);
                    if (a > 0 && b > 0 && a > b) {
                        inversions++;//iversion occurs when lagrge number comes before small number. 
                    }
                }
            }
// If the puzzle grid is odd in size (like a 3x3 grid), the puzzle is solvable if the number of inversions is even.
// If the puzzle grid is even (like a 4x4 grid), we have to consider both the number of inversions and the row position of the empty tile. Specifically:
            boolean evenGrid = gridSize % 2 == 0;
            if (evenGrid) {
                int emptyRowFromBottom = gridSize - (emptyRow + 1);
                return (inversions + emptyRowFromBottom) % 2 == 0;
            } else {
                return inversions % 2 == 0;
            }
        }

        private void resetGrid() {
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int value = i * gridSize + j + 1;//The formula i * gridSize + j calculates the position of the number in a one-dimensional array that would represent the grid if it were flattened out into a single row (ignoring the empty space).
                    if (value < gridSize * gridSize) {
                        buttons[i][j].setText(String.valueOf(value));
                    } else {
                        buttons[i][j].setText("");
                        emptyRow = i;
                        emptyCol = j;
                    }
                }
            }
        }

        private void checkWinCondition() {
            int value = 1;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    String text = buttons[i][j].getText();
                    if (i == gridSize - 1 && j == gridSize - 1) {//empty tile comes at last then the puzzle is won if not it's not. 
                        if (!text.isEmpty()) { //if there is a text in the tile then it returns
                            return;
                        }
                    } else if (!text.equals(String.valueOf(value))) {
                        return;
                    }
                    value++;
                }
            }
            JOptionPane.showMessageDialog(frame, "Congratulations! You solved the puzzle!", "Victory", JOptionPane.INFORMATION_MESSAGE);
        }

        private class TileClickListener implements ActionListener {

            private int row;
            private int col;

            public TileClickListener(int row, int col) {
                this.row = row;
                this.col = col;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdjacentToEmpty(row, col)) {
                    buttons[emptyRow][emptyCol].setText(buttons[row][col].getText());
                    buttons[row][col].setText("");
                    emptyRow = row;
                    emptyCol = col;

                    checkWinCondition();
                }
            }

            private boolean isAdjacentToEmpty(int r, int c) {
                return (Math.abs(r - emptyRow) == 1 && c == emptyCol) || (Math.abs(c - emptyCol) == 1 && r == emptyRow);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SlidingPuzzleGUI::new);
    }
}
