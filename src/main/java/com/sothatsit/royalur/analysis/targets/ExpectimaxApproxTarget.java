package com.sothatsit.royalur.analysis.targets;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.simulation.Game;

/**
 * This target aims to generate data so we can approximate Expectimax depth 7.
 *
 * @author Raphaël Côté
 */
public class ExpectimaxApproxTarget extends Target {

    public static final String NAME = "ExpectimaxApprox";
    public static final String DESC = "This target aims to generate data so we can approximate Expectimax depth 7.";

    public ExpectimaxApproxTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {

        // Esquisse de ce que j'ai dans la tête
        /*
         * Générer un état du board valide aléatoire
         * Demander l'avis à EXPECTIMAX_DEPTH_7
         * Stocker cet avis (le score de chaque move)
         * 
         * Après plusieurs répétitions, enregistrer le résultat sous forme de tableau/json
         * Ptetre stocker seulement un move/score?
         * [Etat du board+score+pions restant] [qqchose qui identifie le pion à bouger] [qqchose qui identifie ou le pion va bouger] [label/score final d'expectimax]
         * 
         * 
         * Après discussion avec sothatsit:
         * Faire jouer Random vs. Random beaucoup de fois l'un contre l'autre et enregistrer le board à chaque move effectué.
         * Une fois ce dataset généré, utiliser Expectimax sur chaque possible move de chaque board enregistré et générer ce dataset final:
         * [board state] [my/other's (score, pawns to play)] [analyzed position] [dice roll (generate a row for 1 to 4)] [expectimax score/label]
         */
        AgentType lightType = AgentType.EXPECTIMAX_DEPTH_7;
        AgentType darkType = AgentType.RANDOM;
        Game game = new Game();

        game.reset();
        System.out.println(game);
        game.simulateOneMove(lightType.agent, darkType.agent);
        System.out.println(game);
        game.simulateOneMove(lightType.agent, darkType.agent);
        System.out.println(game);

        return new TargetResult(this) {
            @Override
            public void print() {
            }
        };
    }
}
