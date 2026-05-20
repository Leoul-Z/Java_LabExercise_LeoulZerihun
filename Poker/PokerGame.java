import java.util.ArrayList;

public class PokerGame {

    private Deck deck;
    private Player player;
    private Player computer;
    private ArrayList<Card> communityCards;
    private int pot;
    private boolean roundOver;

    private int playerBet;
    private int computerBet;
    private int currentBet;
    private boolean playerTurn;
    private boolean playerActed;
    private boolean computerActed;
    private String lastAction;
    private String winnerResult = "";

    public PokerGame() {
        player = new Player("You", 1000);
        computer = new Player("Computer", 1000);
        startNewRound();
    }

    public void startNewRound() {
        deck = new Deck();
        deck.shuffle();

        player.clearHand();
        computer.clearHand();

        communityCards = new ArrayList<>();
        pot = 0;
        roundOver = false;
        winnerResult = "";

        // Take a 10-chip ante from both players to start the pot
        int ante = 10;
        if (player.getChips() >= ante && computer.getChips() >= ante) {
            player.removeChips(ante);
            computer.removeChips(ante);
            pot = ante * 2;
        } else {
            int minAnte = Math.min(player.getChips(), computer.getChips());
            player.removeChips(minAnte);
            computer.removeChips(minAnte);
            pot = minAnte * 2;
        }

        playerBet = 0;
        computerBet = 0;
        currentBet = 0;
        playerTurn = true;
        playerActed = false;
        computerActed = false;
        lastAction = "New round started. Ante posted.";

        for (int i = 0; i < 2; i++) {
            Card playerCard = deck.dealCard();
            Card computerCard = deck.dealCard();
            if (playerCard != null) player.addCard(playerCard);
            if (computerCard != null) computer.addCard(computerCard);
        }

        for (int i = 0; i < 5; i++) {
            Card card = deck.dealCard();
            if (card != null) communityCards.add(card);
        }
    }

    public boolean playerAction(String actionType, int raiseAmount) {
        if (roundOver || !playerTurn) return false;

        if (actionType.equals("CHECK_CALL")) {
            int toCall = currentBet - playerBet;
            if (toCall == 0) {
                playerActed = true;
                lastAction = "You checked.";
            } else {
                if (player.getChips() >= toCall) {
                    player.removeChips(toCall);
                    playerBet = currentBet;
                    pot += toCall;
                    playerActed = true;
                    lastAction = "You called " + toCall + ".";
                } else {
                    int allIn = player.getChips();
                    player.removeChips(allIn);
                    playerBet += allIn;
                    pot += allIn;
                    playerActed = true;
                    lastAction = "You called all-in with " + allIn + ".";
                }
            }
        } else if (actionType.equals("RAISE")) {
            if (raiseAmount <= 0) return false;
            int targetBet = currentBet + raiseAmount;
            int additionalNeeded = targetBet - playerBet;

            if (player.getChips() >= additionalNeeded) {
                player.removeChips(additionalNeeded);
                playerBet = targetBet;
                currentBet = targetBet;
                pot += additionalNeeded;
                playerActed = true;
                computerActed = false; // Computer must respond to the raise
                lastAction = "You raised by " + raiseAmount + " (Total bet: " + targetBet + ").";
            } else {
                return false;
            }
        } else if (actionType.equals("FOLD")) {
            player.fold();
            lastAction = "You folded.";
            determineWinner();
            return true;
        } else {
            return false;
        }

        playerTurn = false;
        checkBettingRoundStatus();
        return true;
    }

    public void computerAction() {
        if (roundOver || playerTurn) return;

        ArrayList<Card> computerCards = new ArrayList<>();
        computerCards.addAll(computer.getHand());
        computerCards.addAll(communityCards);
        int rank = HandEvaluator.evaluate(computerCards);

        int toCall = currentBet - computerBet;
        int chips = computer.getChips();

        if (toCall == 0) {
            double rand = Math.random();
            if (rank >= 4 && rand < 0.6) {
                int raiseAmount = Math.max(20, pot / 2);
                raiseAmount = (raiseAmount / 10) * 10;
                raiseAmount = Math.min(raiseAmount, chips);
                if (raiseAmount > 0) {
                    executeComputerRaise(raiseAmount);
                } else {
                    executeComputerCheck();
                }
            } else if ((rank == 2 || rank == 3) && rand < 0.25) {
                int raiseAmount = 20;
                raiseAmount = Math.min(raiseAmount, chips);
                if (raiseAmount > 0) {
                    executeComputerRaise(raiseAmount);
                } else {
                    executeComputerCheck();
                }
            } else {
                executeComputerCheck();
            }
        } else {
            double rand = Math.random();
            double callPercent = (double) toCall / (chips + toCall);

            if (rank >= 4) {
                if (rand < 0.3 && chips > toCall) {
                    int raiseAmount = Math.max(20, pot / 2);
                    raiseAmount = (raiseAmount / 10) * 10;
                    raiseAmount = Math.min(raiseAmount, chips - toCall);
                    if (raiseAmount > 0) {
                        executeComputerRaise(raiseAmount);
                    } else {
                        executeComputerCall(toCall);
                    }
                } else {
                    executeComputerCall(toCall);
                }
            } else if (rank == 3) {
                if (callPercent > 0.6 && rand < 0.15) {
                    executeComputerFold();
                } else if (rand < 0.15 && chips > toCall) {
                    int raiseAmount = 20;
                    raiseAmount = Math.min(raiseAmount, chips - toCall);
                    if (raiseAmount > 0) {
                        executeComputerRaise(raiseAmount);
                    } else {
                        executeComputerCall(toCall);
                    }
                } else {
                    executeComputerCall(toCall);
                }
            } else if (rank == 2) {
                if (callPercent > 0.4 && rand < 0.6) {
                    executeComputerFold();
                } else if (callPercent > 0.2 && rand < 0.3) {
                    executeComputerFold();
                } else {
                    executeComputerCall(toCall);
                }
            } else {
                if (toCall <= 20 && rand < 0.15) {
                    executeComputerCall(toCall);
                } else {
                    executeComputerFold();
                }
            }
        }

        playerTurn = true;
        checkBettingRoundStatus();
    }

    private void executeComputerCheck() {
        computerActed = true;
        lastAction = "Computer checked.";
    }

    private void executeComputerCall(int toCall) {
        if (computer.getChips() >= toCall) {
            computer.removeChips(toCall);
            computerBet = currentBet;
            pot += toCall;
            computerActed = true;
            lastAction = "Computer called " + toCall + ".";
        } else {
            int allIn = computer.getChips();
            computer.removeChips(allIn);
            computerBet += allIn;
            pot += allIn;
            computerActed = true;
            lastAction = "Computer called all-in with " + allIn + ".";
        }
    }

    private void executeComputerRaise(int raiseAmount) {
        int targetBet = currentBet + raiseAmount;
        int additionalNeeded = targetBet - computerBet;

        if (computer.getChips() >= additionalNeeded) {
            computer.removeChips(additionalNeeded);
            computerBet = targetBet;
            currentBet = targetBet;
            pot += additionalNeeded;
            computerActed = true;
            playerActed = false; // Player must respond to the raise
            lastAction = "Computer raised by " + raiseAmount + " (Total bet: " + targetBet + ").";
        } else {
            int toCall = currentBet - computerBet;
            executeComputerCall(toCall);
        }
    }

    private void executeComputerFold() {
        computer.fold();
        lastAction = "Computer folded.";
        determineWinner();
    }

    private void checkBettingRoundStatus() {
        if (roundOver) return;

        if (playerActed && computerActed && playerBet == computerBet) {
            determineWinner();
        } else if ((player.getChips() == 0 || computer.getChips() == 0) && playerBet == computerBet) {
            determineWinner();
        }
    }

    public String determineWinner() {
        if (roundOver) return winnerResult;

        roundOver = true;

        if (player.isFolded()) {
            computer.addChips(pot);
            pot = 0;
            winnerResult = "You Folded! Computer Wins.";
            return winnerResult;
        }

        if (computer.isFolded()) {
            player.addChips(pot);
            pot = 0;
            winnerResult = "Computer Folded! You Win.";
            return winnerResult;
        }

        ArrayList<Card> playerCards = new ArrayList<>();
        ArrayList<Card> computerCards = new ArrayList<>();

        playerCards.addAll(player.getHand());
        playerCards.addAll(communityCards);

        computerCards.addAll(computer.getHand());
        computerCards.addAll(communityCards);

        int playerRank = HandEvaluator.evaluate(playerCards);
        int computerRank = HandEvaluator.evaluate(computerCards);

        if (playerRank > computerRank) {
            player.addChips(pot);
            pot = 0;
            winnerResult = "You Win! (" + HandEvaluator.getHandName(playerRank) + ")";
        } else if (computerRank > playerRank) {
            computer.addChips(pot);
            pot = 0;
            winnerResult = "Computer Wins! (" + HandEvaluator.getHandName(computerRank) + ")";
        } else {
            int playerHigh = HandEvaluator.getHighCard(playerCards);
            int computerHigh = HandEvaluator.getHighCard(computerCards);

            if (playerHigh > computerHigh) {
                player.addChips(pot);
                pot = 0;
                winnerResult = "You Win! (" + HandEvaluator.getHandName(playerRank) + ", High Card)";
            } else if (computerHigh > playerHigh) {
                computer.addChips(pot);
                pot = 0;
                winnerResult = "Computer Wins! (" + HandEvaluator.getHandName(computerRank) + ", High Card)";
            } else {
                int half = pot / 2;
                int remainder = pot % 2;
                player.addChips(half + remainder);
                computer.addChips(half);
                pot = 0;
                winnerResult = "Draw! Pot split evenly.";
            }
        }
        return winnerResult;
    }

    public boolean isRoundOver() {
        return roundOver;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getComputer() {
        return computer;
    }

    public ArrayList<Card> getCommunityCards() {
        return communityCards;
    }

    public int getPot() {
        return pot;
    }

    public int getPlayerBet() {
        return playerBet;
    }

    public int getComputerBet() {
        return computerBet;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public String getLastAction() {
        return lastAction;
    }
}