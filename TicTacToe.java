import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 700;

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    JButton restartButton = new JButton("Restart");
    JButton closeButton = new JButton("Close");

    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;

    int selectedRow = 0;
    int selectedCol = 0;

    boolean gameStarted = false; // New flag to check if the game has started

    TicTacToe() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top Label (Stylish)
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 48));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);
        textLabel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        // Game Board
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel, BorderLayout.CENTER);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(e -> {
                    if (gameOver) return;
                    JButton clickedTile = (JButton) e.getSource();
                    if (clickedTile.getText().equals("")) {
                        clickedTile.setText(currentPlayer);
                        turns++;
                        checkWinner();
                        if (!gameOver) {
                            currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                            textLabel.setText(currentPlayer + "'s turn..");
                        }
                    }
                });
            }
        }

        // Bottom Panel with Restart and Close
        restartButton.setFont(new Font("Arial", Font.PLAIN, 28));
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> restartGame());

        closeButton.setFont(new Font("Arial", Font.PLAIN, 28));
        closeButton.setFocusable(false);
        closeButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(restartButton);
        bottomPanel.add(closeButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Keyboard
        setupKeyboardControls();
        highlightSelectedTile();
    }

    void setupKeyboardControls() {
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // Only allow keyboard input if the game has started
                if (!gameStarted) {
                    gameStarted = true;
                    textLabel.setText(currentPlayer + "'s turn.."); // Change text after first key press
                }

                if (!gameOver) {  // Allow key events only when the game is not over
                    int key = e.getKeyCode();
                    switch (key) {
                        case KeyEvent.VK_UP -> selectedRow = (selectedRow + 2) % 3;
                        case KeyEvent.VK_DOWN -> selectedRow = (selectedRow + 1) % 3;
                        case KeyEvent.VK_LEFT -> selectedCol = (selectedCol + 2) % 3;
                        case KeyEvent.VK_RIGHT -> selectedCol = (selectedCol + 1) % 3;
                        case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                            JButton selected = board[selectedRow][selectedCol];
                            if (selected.getText().equals("")) {
                                selected.setText(currentPlayer);
                                turns++;
                                checkWinner();
                                if (!gameOver) {
                                    currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                                    textLabel.setText(currentPlayer + "'s turn..");
                                }
                            }
                        }
                    }
                    highlightSelectedTile();
                }
            }
        });
    }

    void highlightSelectedTile() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        }
        board[selectedRow][selectedCol].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
    }

    void checkWinner() {
        // Horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().equals("")) continue;
            if (board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                JButton[] win = { board[r][0], board[r][1], board[r][2] };
                for (JButton tile : win) setWinner(tile);
                gameOver = true;
                showWinner(currentPlayer);
                startTileCelebration(win);
                return;
            }
        }

        // Vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().equals("")) continue;
            if (board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                JButton[] win = { board[0][c], board[1][c], board[2][c] };
                for (JButton tile : win) setWinner(tile);
                gameOver = true;
                showWinner(currentPlayer);
                startTileCelebration(win);
                return;
            }
        }

        // Diagonal
        if (!board[0][0].getText().equals("") &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            JButton[] win = { board[0][0], board[1][1], board[2][2] };
            for (JButton tile : win) setWinner(tile);
            gameOver = true;
            showWinner(currentPlayer);
            startTileCelebration(win);
            return;
        }

        // Anti-diagonal
        if (!board[0][2].getText().equals("") &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            JButton[] win = { board[0][2], board[1][1], board[2][0] };
            for (JButton tile : win) setWinner(tile);
            gameOver = true;
            showWinner(currentPlayer);
            startTileCelebration(win);
            return;
        }

        // Tie
        if (turns == 9) {
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    setTile(board[r][c]);
            textLabel.setText("It's a tie!");
            textLabel.setBackground(Color.ORANGE);
            textLabel.setForeground(Color.BLACK);
            gameOver = true;
        }
    }

    void showWinner(String player) {
        textLabel.setText(player + " Wins!");
        textLabel.setBackground(Color.GREEN.darker());
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.BOLD, 55));
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.GREEN);
        tile.setBackground(Color.GRAY);
    }

    void setTile(JButton tile) {
        tile.setForeground(Color.ORANGE);
        tile.setBackground(Color.GRAY);
    }

    void startTileCelebration(JButton[] winningTiles) {
        Timer timer = new Timer(250, null);
        final int[] count = { 0 };
        Color[] flashColors = { Color.MAGENTA, Color.YELLOW, Color.CYAN };

        timer.addActionListener(e -> {
            Color color = flashColors[count[0] % flashColors.length];
            for (JButton tile : winningTiles) {
                tile.setBackground(color);
            }
            count[0]++;
            if (count[0] >= 9) {
                timer.stop();
                for (JButton tile : winningTiles) {
                    tile.setBackground(Color.GRAY);
                }
            }
        });
        timer.start();
    }

    void restartGame() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(Color.darkGray);
                board[r][c].setForeground(Color.white);
                board[r][c].setEnabled(true);
                board[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }

        currentPlayer = playerX;
        turns = 0;
        gameOver = false;
        boardPanel.setBackground(Color.darkGray);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 48));
        selectedRow = 0;
        selectedCol = 0;
        highlightSelectedTile();
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}