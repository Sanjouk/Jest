package model.cards;

import model.players.ScoreVisitorImpl;
import model.players.strategies.StrategyType;

public final class ExtensionRegistry {
    private ExtensionRegistry() {
    }

    public static ExtensionCard createByName(String name) {
        if (name == null) return null;

        return switch (name) {
            case "The Shield" -> new ExtensionCard(
                    "The Shield",
                    0,
                    "Protège contre les malus : vos cartes Carreau et Coeur ne valent plus de points négatifs.",
                    new CardEffect() {
                        @Override
                        public void applyOnVisit(ScoreVisitorImpl visitor) {
                            visitor.setFlag("NO_NEGATIVE_DIAMONDS", true);
                            visitor.setFlag("NO_NEGATIVE_HEARTS", true);
                        }
                    },
                    (strategy, jest) -> {
                        if (strategy == StrategyType.CAUTIOUS) return 1000;
                        return 0;
                    }
            );
            case "The Crown" -> new ExtensionCard(
                    "The Crown",
                    0,
                    "Un trésor royal : Ajoute 5 points directement à votre score final.",
                    new CardEffect() {
                        @Override
                        public int calculateBonus(ScoreVisitorImpl visitor) {
                            return 5;
                        }
                    },
                    (strategy, jest) -> 20
            );
            case "The Spy" -> new ExtensionCard(
                    "The Spy",
                    2,
                    "Un agent infiltré : Carte neutre de valeur 2.",
                    new CardEffect() {
                    },
                    (strategy, jest) -> 2
            );
            case "The Jester" -> new ExtensionCard(
                    "The Jester",
                    0,
                    "L'ami du Joker : Vaut +10 si vous avez le Joker, sinon -5.",
                    new CardEffect() {
                        @Override
                        public int calculateBonus(ScoreVisitorImpl visitor) {
                            return visitor.hasJoker() ? 10 : -5;
                        }
                    },
                    (strategy, jest) -> {
                        boolean hasJoker = jest.getCards().stream().anyMatch(c -> c instanceof Joker);

                        if (strategy == StrategyType.CAUTIOUS) return -100;
                        if (hasJoker) return 50;
                        return -5;
                    }
            );
            default -> null;
        };
    }
}
