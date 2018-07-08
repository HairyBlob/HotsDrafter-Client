package com.neogens.hotsdrafter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author François Gourdeau
 */
public class DraftTree {

    public boolean debug = false;

    public boolean searchActionDone;
   
    static TeamEvaluator evaluator;
    public int map;
    double basePlausability;

 
    RootNode root;
    boolean powA;
    boolean powB;
 
    DraftTree(int map) throws Exception {
        this.map = map;
        evaluator = new TeamEvaluator();
        root = new RootNode(this);
        basePlausability=evaluator.getEstimatedTeamPlausability(new int[]{});
        
    }
 
    boolean search(SequenceState state) throws Exception {
       searchActionDone=false;    
       root.search(  new SequenceState(this,state.moves)); // éviter d'écraser l'original
       return searchActionDone;
    }
   
    List<Node> getScenario(SequenceState targetState) {
         List<Node> scenario = new ArrayList();

        Node node = root.getBestMove(targetState);
        while (node != null) {
           
            scenario.add(node);
           if (node.move!=Hero.OPEN){ // ban
               targetState.moves[node.level]=node.move;
               targetState.taken[targetState.moves[node.level]] = true;
           } 
            node = node.getBestMove(targetState);
        }
        return scenario;
    }

    public SequenceState getSimulation(SequenceState userState) {

         SequenceState trialState;
         
        if (userState.moves[0] == Hero.OPEN) {
            trialState = new SequenceState(this, userState);
            root.search(trialState, 3);

            //userState.moves[0] = trialState.moves[3];
            userState.moves[0] = getBan(trialState,3);

            userState.justification.put(userState.moves[0], trialState.justification.get(trialState.moves[3]));

            userState.taken[userState.moves[0]] = true;


        } else {
            userState.taken[userState.moves[0]] = true;
        }

        if (userState.moves[1] == Hero.OPEN) {
            trialState = new SequenceState(this, userState);
            root.search(trialState, 2);
            //userState.moves[1] = trialState.moves[2];
            userState.moves[1] = getBan(trialState,2);

            userState.justification.put(userState.moves[1], trialState.justification.get(trialState.moves[2]));
            userState.taken[userState.moves[1]] = true;

        } else {
            userState.taken[userState.moves[1]] = true;
        }

        if (userState.moves[7] == Hero.OPEN) {
            trialState = new SequenceState(this, userState);
            root.search(trialState, 11);
            //  userState.moves[7] = trialState.moves[11];
            userState.moves[7] = getBan(trialState,11);

            userState.justification.put(userState.moves[7], trialState.justification.get(trialState.moves[11]));
            userState.taken[userState.moves[7]] = true;


        } else {
            userState.taken[userState.moves[7]] = true;
        }

        if (userState.moves[8] == Hero.OPEN) {
            trialState = new SequenceState(this, userState);
            root.search(trialState, 9);
            // userState.moves[8] = trialState.moves[9]; 
            userState.moves[8] = getBan(trialState,9);

            userState.justification.put(userState.moves[8], trialState.justification.get(trialState.moves[9]));
            userState.taken[userState.moves[8]] = true;

        } else {
            userState.taken[userState.moves[8]] = true;

        }
        trialState = new SequenceState(this, userState);
        root.search(trialState);
        return trialState;

    }
    
    int getBan(SequenceState trialState,int level) {
        int[] moves = new int[trialState.moves.length];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = -1;
        }
        for (int i = 0; i < level; i++) {
            moves[i] = trialState.moves[i];
        }
        SequenceState state = new SequenceState(this, moves);
        return trialState.justification.get(trialState.moves[level]).getBestMoveUCT(state, true);
    }
}
