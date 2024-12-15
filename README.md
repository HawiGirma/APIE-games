# APIE-games

[OOP-Group 1.pdf](https://github.com/user-attachments/files/18121314/OOP-Group.1.pdf)

## Sudoku Game - Java GUI

### Overview

This Sudoku game, built with Java Swing for the GUI, lets users select difficulty, generate puzzles, solve them, and check their solutions. The project showcases basic object-oriented programming (OOP) principles and Java Swing components.

### Object-Oriented Programming Concepts

1. **Encapsulation**
   - The `SudokuGame` class encapsulates the game's data, managing the puzzle, grid, and user interactions within.
2. **Abstraction**

   - Methods like `solveSudoku()`, `checkSolution()`, and `generateSudoku()` abstract puzzle-solving and validation, allowing users to interact via buttons without knowing the internal workings.

3. **Inheritance**

   - The game inherits from `JFrame` for the main window and `JTextField` for each cell.

4. **Polymorphism**
   - Action listeners for buttons (Solve, Clear, Generate, Check) all use the `actionPerformed()` method to handle clicks polymorphically.

### Rules of Sudoku

- **Objective**: Fill the grid so each row, column, and subgrid contains digits 1–N (based on grid size).
- **Initial Grid**: Some cells are pre-filled. Fill in the empty cells.
- **No Repeated Numbers**: Each row, column, and subgrid must not repeat numbers.

### How to Play

1. **Start the Game**: Choose a difficulty: Easy (4x4), Medium (6x6), Hard (9x9).
2. **Game Features**:
   - **Grid Display**: View and edit cells.
   - **Solve**: Click "Solve" to auto-solve the puzzle.
   - **Clear**: Click "Clear" to reset the grid.
   - **Generate New Puzzle**: Click "Generate" to create a new puzzle.
   - **Check Solution**: Click "Check" to validate your solution.
3. **Interaction**: Enter numbers by clicking and typing in editable cells.
4. **Winning**: If the grid is correctly filled, a success message will appear.

### Requirements:

JDK 8+, Java IDE (e.g., IntelliJ IDEA, Eclipse).

## Sliding Puzzle - 15 Puzzle Game

### Overview

This game, implemented with Java Swing, involves a 4x4 grid where tiles must be rearranged to form a sorted order. The goal is to slide tiles into the empty space to solve the puzzle.

### OOP Concepts

1. **Encapsulation**: The `SlidingPuzzleGUI` class manages the game data (grid size, tile positions) and logic.
2. **Abstraction**: The tile movement and win condition checking are abstracted into methods like `isAdjacentToEmpty()` and `checkWinCondition()`.
3. **Inheritance**: The game extends `JFrame` for the window and uses `JButton` for tiles.
4. **Polymorphism**: `ActionListener` implementation with `TileClickListener` handles tile movements via the same method.

### How to Play

1. **Start**: A shuffled 4x4 grid opens, with tiles numbered 1–15.
2. **Move Tiles**: Click adjacent tiles to move them into the empty space.
3. **Shuffle**: Click "Shuffle" to shuffle tiles.
4. **Reset**: Click "Reset" to solve the puzzle.
5. **Win Condition**: A message appears when the puzzle is solved.

### Requirements:

JDK 8+, Java IDE.

## Maze Puzzle Game

### Overview

Navigate through a maze to reach the endpoint using arrow keys. The game includes 3 progressively harder levels.

### How to Play

1. **Game Start**: Choose the difficulty: Easy (8x8), Medium (12x12), or Hard (16x16).
2. **Movement**: Use arrow keys to move through open spaces.
3. **Objective**: Reach the bottom-right corner to complete the level.
4. **Levels**: Complete all 3 levels to win the game.

### OOP Concepts

- **Encapsulation**: Game logic is contained within `MazeGame`, including player movement and maze generation.
- **Inheritance**: `MazeGame` extends `JPanel` for GUI functionality.
- **Polymorphism**: The `paintComponent()` method and action listeners handle events polymorphically.
- **Abstraction**: Maze generation and movement rules are abstracted in methods like `generateMaze()`.

## Word Search Puzzle Game

### Overview

A word search game where players find words hidden within a grid of letters. Complete 3 rounds to win.

### How to Play

1. **Start**: A grid with hidden words and a list of words to find.
2. **Check Word**: Click "Check Word" to submit your guess.
3. **Objective**: Find all the words before time runs out.
4. **Rounds**: The game ends after 3 rounds.

### OOP Concepts

- **Encapsulation**: Game data (word list, grid) is encapsulated in `WordSearchGame`.
- **Inheritance**: `WordSearchGUI` inherits from `JFrame` for UI management.
- **Polymorphism**: Overridden `actionPerformed()` methods handle different button actions.
- **Abstraction**: Word placement and checking are abstracted into methods like `fillGridWithWords()`.
