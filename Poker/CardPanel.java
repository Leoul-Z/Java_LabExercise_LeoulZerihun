import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CardPanel extends JPanel {

    private Card card;
    private boolean faceUp;
    private Image cardImage;

    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static final String ASSETS_DIR = "assets";

    public CardPanel(Card card, boolean faceUp) {
        this.card = card;
        this.faceUp = faceUp;
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setOpaque(false);
        loadCardImage();
    }

    private void loadCardImage() {
        String fileName;
        if (faceUp && card != null) {
            fileName = card.getRank().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".png";
        } else {
            fileName = "card_back.png";
        }

        File imgFile = new File(ASSETS_DIR, fileName);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
            cardImage = icon.getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cardImage != null) {
            g.drawImage(cardImage, 0, 0, CARD_WIDTH, CARD_HEIGHT, this);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.RED);
            g2.drawRoundRect(0, 0, CARD_WIDTH - 1, CARD_HEIGHT - 1, 12, 12);
            g2.drawString("Err", 10, 20);
        }
    }
}
