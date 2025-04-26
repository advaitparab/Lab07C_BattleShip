import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class BattleShipApp extends JFrame {
    private final int BOARD_SIZE = 10;
    private JButton[][] gridButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private boolean[][] occupied = new boolean[BOARD_SIZE][BOARD_SIZE];
    private List<Ship> ships;
    private GameStats stats;

    private JLabel missLabel = new JLabel("Misses in a Row: 0");
    private JLabel strikeLabel = new JLabel("Strikes: 0");
    private JLabel totalMissLabel = new JLabel("Total Misses: 0");
    private JLabel totalHitLabel = new JLabel("Total Hits: 0");

    public BattleShipApp() {
        super("Battleship - Lab 07C");

        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton cell = new JButton("~");
                cell.setBackground(Color.CYAN);
                final int r = row;
                final int c = col;

                cell.addActionListener(e -> handleClick(r, c));
                gridButtons[row][col] = cell;
                boardPanel.add(cell);
            }
        }

        JPanel controlPanel = new JPanel();
        JButton playAgain = new JButton("Play Again");
        JButton quit = new JButton("Quit");
        playAgain.addActionListener(e -> resetGame());
        quit.addActionListener(e -> System.exit(0));
        controlPanel.add(playAgain);
        controlPanel.add(quit);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2));
        statsPanel.add(missLabel);
        statsPanel.add(strikeLabel);
        statsPanel.add(totalMissLabel);
        statsPanel.add(totalHitLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(statsPanel, BorderLayout.NORTH);

        resetGame();

        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void resetGame() {
        stats = new GameStats();
        occupied = new boolean[BOARD_SIZE][BOARD_SIZE];

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                gridButtons[r][c].setText("~");
                gridButtons[r][c].setEnabled(true);
                gridButtons[r][c].setBackground(Color.CYAN);
            }
        }

        ships = new ShipPlacer().placeShips(occupied);
        updateStats();
    }

    private void handleClick(int row, int col) {
        JButton button = gridButtons[row][col];
        button.setEnabled(false);

        boolean hit = false;
        for (Ship ship : ships) {
            if (ship.isAt(row, col)) {
                ship.hit();
                hit = true;
                stats.totalHit++;
                stats.missCount = 0;

                button.setText("X");
                button.setBackground(Color.RED);

                if (ship.isSunk()) {
                    JOptionPane.showMessageDialog(this, "You sunk a ship of size " + ship.getSize() + "!");
                }
                break;
            }
        }

        if (!hit) {
            button.setText("M");
            button.setBackground(Color.YELLOW);
            stats.missCount++;
            stats.totalMiss++;

            if (stats.missCount == 5) {
                stats.strikeCount++;
                stats.missCount = 0;
                JOptionPane.showMessageDialog(this, "Strike " + stats.strikeCount + "!");
            }
        }

        updateStats();
        checkGameEnd();
    }

    private void updateStats() {
        missLabel.setText("Misses in a Row: " + stats.missCount);
        strikeLabel.setText("Strikes: " + stats.strikeCount);
        totalMissLabel.setText("Total Misses: " + stats.totalMiss);
        totalHitLabel.setText("Total Hits: " + stats.totalHit);
    }

    private void checkGameEnd() {
        if (stats.strikeCount >= 3) {
            JOptionPane.showMessageDialog(this, "You lost the game. All 3 strikes used.");
            askReplay();
        }

        boolean allSunk = true;
        for (Ship s : ships) {
            if (!s.isSunk()) {
                allSunk = false;
                break;
            }
        }

        if (allSunk) {
            JOptionPane.showMessageDialog(this, "You WIN! All ships sunk.");
            askReplay();
        }
    }

    private void askReplay() {
        int response = JOptionPane.showConfirmDialog(this, "Play again?", "Replay", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleShipApp::new);
    }
}
