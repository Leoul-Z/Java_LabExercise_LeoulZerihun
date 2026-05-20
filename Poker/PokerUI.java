import javax.swing.*;
import java.awt.*;

public class PokerUI extends JFrame {

    private PokerGame game;

    private JPanel playerCardPanel;
    private JPanel computerCardPanel;
    private JPanel communityCardPanel;
    private JLabel potLabel;
    private JLabel playerChips;
    private JLabel computerChips;
    private JLabel resultLabel;

    private JButton checkButton;
    private JButton raiseButton;
    private JButton foldButton;
    private JButton newRoundButton;

    public PokerUI() {
        game = new PokerGame();

        setTitle("Simple Poker Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(new JLabel("  Computer's Hand:"), BorderLayout.NORTH);
        computerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        topSection.add(computerCardPanel, BorderLayout.CENTER);

        JPanel centerSection = new JPanel(new BorderLayout());
        centerSection.add(new JLabel("  Community Cards:"), BorderLayout.NORTH);
        communityCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        centerSection.add(communityCardPanel, BorderLayout.CENTER);

        JPanel playerSection = new JPanel(new BorderLayout());
        playerSection.add(new JLabel("  Your Hand:"), BorderLayout.NORTH);
        playerCardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        playerSection.add(playerCardPanel, BorderLayout.CENTER);

        JPanel cardsArea = new JPanel(new GridLayout(3, 1));
        cardsArea.add(topSection);
        cardsArea.add(centerSection);
        cardsArea.add(playerSection);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));

        potLabel = new JLabel("Pot: 0", SwingConstants.CENTER);
        playerChips = new JLabel("", SwingConstants.CENTER);
        computerChips = new JLabel("", SwingConstants.CENTER);
        resultLabel = new JLabel("Welcome to Poker", SwingConstants.CENTER);

        JPanel infoRow = new JPanel(new GridLayout(1, 3));
        infoRow.add(playerChips);
        infoRow.add(potLabel);
        infoRow.add(computerChips);

        checkButton = new JButton("Check");
        raiseButton = new JButton("Raise");
        foldButton = new JButton("Fold");
        newRoundButton = new JButton("New Round");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(checkButton);
        buttonPanel.add(raiseButton);
        buttonPanel.add(foldButton);
        buttonPanel.add(newRoundButton);

        bottomPanel.add(infoRow);
        bottomPanel.add(resultLabel);
        bottomPanel.add(buttonPanel);

        add(cardsArea, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateUIData();

        checkButton.addActionListener(e -> {
            boolean success = game.playerAction("CHECK_CALL", 0);
            if (success) {
                resultLabel.setText(game.getLastAction());
                updateUIData();
                if (!game.isRoundOver()) {
                    triggerComputerTurn();
                }
            }
        });

        raiseButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this, "Enter raise amount:", "Raise",
                    JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    int amount = Integer.parseInt(input.trim());
                    if (amount <= 0) {
                        resultLabel.setText("Raise must be a positive number.");
                    } else if (!game.playerAction("RAISE", amount)) {
                        resultLabel.setText("Not enough chips to raise " + amount + "!");
                    } else {
                        resultLabel.setText(game.getLastAction());
                        updateUIData();
                        if (!game.isRoundOver()) {
                            triggerComputerTurn();
                        }
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid amount. Enter a number.");
                }
            }
        });

        foldButton.addActionListener(e -> {
            game.playerAction("FOLD", 0);
            updateUIData();
        });

        newRoundButton.addActionListener(e -> {
            game.startNewRound();
            resultLabel.setText("New Round Started. Ante posted.");
            updateUIData();
        });

        setVisible(true);
    }

    private void triggerComputerTurn() {
        checkButton.setEnabled(false);
        raiseButton.setEnabled(false);
        foldButton.setEnabled(false);
        resultLabel.setText("Computer is thinking...");

        Timer timer = new Timer(1500, e -> {
            game.computerAction();
            resultLabel.setText(game.getLastAction());
            updateUIData();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateUIData() {
        boolean roundOver = game.isRoundOver();
        boolean isPlayerTurn = game.isPlayerTurn();

        checkButton.setEnabled(!roundOver && isPlayerTurn);
        raiseButton.setEnabled(!roundOver && isPlayerTurn);
        foldButton.setEnabled(!roundOver && isPlayerTurn);

        // Update Check/Call button text dynamically
        if (!roundOver) {
            int toCall = game.getCurrentBet() - game.getPlayerBet();
            if (toCall > 0) {
                checkButton.setText("Call (" + toCall + ")");
            } else {
                checkButton.setText("Check");
            }
        } else {
            checkButton.setText("Check");
        }

        playerCardPanel.removeAll();
        for (Card card : game.getPlayer().getHand()) {
            playerCardPanel.add(new CardPanel(card, true));
        }

        computerCardPanel.removeAll();
        for (Card card : game.getComputer().getHand()) {
            computerCardPanel.add(new CardPanel(card, roundOver));
        }

        communityCardPanel.removeAll();
        for (Card card : game.getCommunityCards()) {
            communityCardPanel.add(new CardPanel(card, true));
        }

        potLabel.setText("Pot: " + game.getPot());
        playerChips.setText("Your Chips: " + game.getPlayer().getChips() + " (Bet: " + game.getPlayerBet() + ")");
        computerChips.setText("Computer Chips: " + game.getComputer().getChips() + " (Bet: " + game.getComputerBet() + ")");

        if (roundOver) {
            resultLabel.setText(game.determineWinner());
        }

        playerCardPanel.revalidate();
        playerCardPanel.repaint();
        computerCardPanel.revalidate();
        computerCardPanel.repaint();
        communityCardPanel.revalidate();
        communityCardPanel.repaint();
    }
}