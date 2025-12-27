package model.cards;

import model.players.Jest;
import model.players.strategies.StrategyType;

import java.io.Serializable;
import java.util.function.BiFunction;

public class ExtensionCard extends Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int faceValue;
    private String description;
    
    private transient CardEffect effect; // Pour le score
    
    // NOUVEAU : La logique pour les Bots
    // Fonction qui prend (StrategyType, Jest) et retourne un Integer (score d'intérêt)
    private transient BiFunction<StrategyType, Jest, Integer> aiHeuristic;

    public ExtensionCard(String name, int faceValue, String description, 
                         CardEffect effect, 
                         BiFunction<StrategyType, Jest, Integer> aiHeuristic) {
        super(false);
        this.name = name;
        this.faceValue = faceValue;
        this.description = description;
        this.effect = effect;
        this.aiHeuristic = aiHeuristic;
    }

    public int getAIValue(StrategyType strategy, Jest currentJest) {
        if (aiHeuristic == null) return 0;
        return aiHeuristic.apply(strategy, currentJest);
    }

    public CardEffect getEffect() { return effect; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public int getFaceValue() { return this.faceValue; }

    @Override
    public int getSuitValue() { return 0; }

    @Override
    public String toString() { return "[" + name + " (Ext)]"; }

    private Object readResolve() {
        ExtensionCard resolved = ExtensionRegistry.createByName(this.name);
        if (resolved == null) {
            resolved = new ExtensionCard(this.name, this.faceValue, this.description, new CardEffect() {}, null);
        }
        resolved.isTrophy = this.isTrophy;
        resolved.trophyType = this.trophyType;
        resolved.trophySuit = this.trophySuit;
        resolved.trophyFace = this.trophyFace;
        return resolved;
    }
}