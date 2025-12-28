package view.gui;

import model.cards.Card;
import model.cards.Joker;
import model.cards.Suit;
import model.cards.SuitCard;

import javax.swing.*;
import java.awt.*;

public class CardComponent extends JPanel {
    private final Card card;
    private final boolean faceUp;
    private final boolean selectable;
    private boolean selected;
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;

    public CardComponent(Card card, boolean faceUp, boolean selectable) {
        this.card = card;
        this.faceUp = faceUp;
        this.selectable = selectable;
        this.selected = false;
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setOpaque(false);
    }

    public CardComponent(Card card, boolean faceUp) {
        this(card, faceUp, false);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (faceUp && card != null) {
            drawFaceUpCard(g2d);
        } else {
            drawFaceDownCard(g2d);
        }

        if (selected) {
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        }

        g2d.dispose();
    }

    private void drawFaceUpCard(Graphics2D g) {
        // Draw card background
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        if (card instanceof Joker) {
            drawJoker(g);
        } else if (card instanceof SuitCard) {
            drawSuitCard(g, (SuitCard) card);
        }
    }

    private void drawSuitCard(Graphics2D g, SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        String face = suitCard.getFace().toString();
        Color suitColor = (suit == Suit.HEARTS || suit == Suit.DIAMONDS) ? Color.RED : Color.BLACK;
        
        g.setColor(suitColor);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // Draw face value in top-left
        g.drawString(face, 8, 25);

        // Draw suit symbol
        String suitSymbol = getSuitSymbol(suit);
        Font symbolFont = new Font("Arial", Font.BOLD, 30);
        g.setFont(symbolFont);
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        FontMetrics fm = g.getFontMetrics();
        int symbolWidth = fm.stringWidth(suitSymbol);
        int symbolHeight = fm.getAscent();
        
        g.drawString(suitSymbol, centerX - symbolWidth / 2, centerY + symbolHeight / 2);

        // Draw face value in bottom-right (rotated)
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.rotate(Math.PI, centerX, centerY);
        g.drawString(face, 8, -getHeight() + 25);
        g.rotate(-Math.PI, centerX, centerY);
    }

    private void drawJoker(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        
        FontMetrics fm = g.getFontMetrics();
        String text = "JOKER";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        g.drawString(text, centerX - textWidth / 2, centerY - textHeight / 2);
        
        // Draw joker symbol
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String jokerSymbol = "🃏";
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(jokerSymbol);
        g.drawString(jokerSymbol, centerX - textWidth / 2, centerY + 20);
    }

    private void drawFaceDownCard(Graphics2D g) {
        // Draw card back
        g.setColor(new Color(0, 0, 139)); // Dark blue
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // Draw pattern
        g.setColor(new Color(0, 0, 200));
        for (int i = 0; i < getWidth(); i += 10) {
            for (int j = 0; j < getHeight(); j += 10) {
                if ((i + j) % 20 == 0) {
                    g.fillOval(i, j, 5, 5);
                }
            }
        }
    }

    private String getSuitSymbol(Suit suit) {
        return switch (suit) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };
    }

    public static int getCardWidth() {
        return CARD_WIDTH;
    }

    public static int getCardHeight() {
        return CARD_HEIGHT;
    }
}

