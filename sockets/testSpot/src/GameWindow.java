/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author xuandat7
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {
    private ImagePanel imagePanel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private int score;
    private int level;
    private int totalTime = 5 * 60 * 1000; // 5 minutes (300000 ms)
    private Timer gameTimer;
    private JPanel gamePanel;
    private JPanel menuPanel;

    public GameWindow() {
        setTitle("Spot the Difference");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenuPanel();
        createGamePanel();

        add(menuPanel);

        setVisible(true);
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        JLabel titleLabel = new JLabel("Spot the Difference");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton singlePlayerButton = new JButton("Single Player");
        singlePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        singlePlayerButton.setMaximumSize(new Dimension(200, 50));

        JButton multiPlayerButton = new JButton("Play with Friend");
        multiPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        multiPlayerButton.setMaximumSize(new Dimension(200, 50));

        singlePlayerButton.addActionListener(e -> startSinglePlayerGame());
        multiPlayerButton.addActionListener(e -> startMultiPlayerGame());

        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        menuPanel.add(singlePlayerButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(multiPlayerButton);
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        
        timerLabel = new JLabel("Time: 5:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        topPanel.add(scoreLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        imagePanel = new ImagePanel();

        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(imagePanel, BorderLayout.CENTER);

        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (level == 0) {
                    showTutorialMessage();
                } else {
                    Point clickPoint = e.getPoint();
                    imagePanel.checkDifference(clickPoint);
                    if (imagePanel.allDifferencesFound()) {
                        score++;
                        scoreLabel.setText("Score: " + score);
                        if (level < 15) {
                            JOptionPane.showMessageDialog(null, "Level " + level + " completed! Moving to next level!");
                            level++;
                            loadNextLevel();
                        } else {
                            JOptionPane.showMessageDialog(null, "Congratulations! You completed all levels!");
                            endGame();
                        }
                    }
                }
            }
        });
    }

    private void startSinglePlayerGame() {
        getContentPane().removeAll();
        getContentPane().add(gamePanel);
        revalidate();
        repaint();

        score = 0;
        level = 0;
        totalTime = 5 * 60 * 1000;
        scoreLabel.setText("Score: " + score);
        startTimer();
        loadNextLevel();
    }

    private void startMultiPlayerGame() {
        // Implement multiplayer game logic here
        JOptionPane.showMessageDialog(this, "Multiplayer mode is not implemented yet.");
    }

    private void showTutorialMessage() {
        JOptionPane.showMessageDialog(this, "Hãy bấm vào các điểm bạn cho là khác biệt.");
        int option = JOptionPane.showConfirmDialog(this, "Bạn đã sẵn sàng chơi?", "Bắt đầu trò chơi", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            level = 1;
            loadNextLevel();
        }
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            totalTime -= 1000;
            int minutes = totalTime / 60000;
            int seconds = (totalTime / 1000) % 60;
            timerLabel.setText("Time: " + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);

            if (totalTime <= 0) {
                ((Timer) e.getSource()).stop();
                endGame();
            }
        });
        gameTimer.start();
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Game Over! Your final score: " + score);
        int option = JOptionPane.showConfirmDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            getContentPane().removeAll();
            getContentPane().add(menuPanel);
            revalidate();
            repaint();
        }
    }

    private void loadNextLevel() {
        try {
            imagePanel.loadLevel(level);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading level data: " + ex.getMessage());
        }
    }

    private void resetGame() {
        score = 0;
        level = 0;
        totalTime = 5 * 60 * 1000;
        scoreLabel.setText("Score: " + score);
        timerLabel.setText("Time: 5:00");
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        startTimer();
        loadNextLevel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::new);
    }
}