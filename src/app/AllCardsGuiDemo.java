package app;

import model.cards.Card;
import model.cards.ExtensionCard;
import model.cards.Face;
import model.cards.Joker;
import model.cards.Suit;
import model.cards.SuitCard;
import model.game.ExtensionManager;
import view.gui.CardComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AllCardsGuiDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("All Cards GUI Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLayout(new BorderLayout());

            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
            cardsPanel.setBackground(new Color(0, 100, 0));

            // 1) Standard suit cards
            for (Suit suit : Suit.values()) {
                for (Face face : Face.values()) {
                    Card c = new SuitCard(false, suit, face);
                    cardsPanel.add(new CardComponent(c, true, false));
                }
            }

            // 2) Joker
            cardsPanel.add(new CardComponent(new Joker(false), true, false));

            // 3) Extension cards
            ArrayList<ExtensionCard> extensions = ExtensionManager.getAvailableExtensions();
            for (ExtensionCard ext : extensions) {
                cardsPanel.add(new CardComponent(ext, true, false));
            }

            JScrollPane scrollPane = new JScrollPane(cardsPanel);
            scrollPane.setBorder(BorderFactory.createTitledBorder("All Cards"));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
