package com.neogens.hotsdrafter;

import java.util.HashMap;

/**
 *
 * @author François Gourdeau
 */

public class SequenceState {

    public int[] moves;
    
    HashMap<Integer, Node> justification=new HashMap<>();

    public boolean[] taken=new boolean[Hero.heroes.length];
    public DraftTree tree;

    public SequenceState(DraftTree tree) {
        this.tree = tree;
        moves = new int[Move.sequence.length];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = -1;
        }
    }
    public SequenceState(DraftTree tree, int[] moves) {
        this.tree = tree;
        this.moves = moves.clone();
        for (int move : moves) {
            if (move == Hero.OPEN) {
               return;
            }
            taken[move] = true;
        }
    }
    
    public SequenceState(DraftTree tree, SequenceState state) {
        this.tree = tree;
        this.justification=state.justification;
        this.moves = state.moves.clone();
        for (int move : moves) {
            if (move == Hero.OPEN) {
               return;
            }
            taken[move] = true;
        }
    }
}
