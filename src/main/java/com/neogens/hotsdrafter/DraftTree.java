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
         trialState = new SequenceState(this, userState);
          
         searchBan(0,5,userState,trialState);
         searchBan(1,4,userState,trialState);
         searchBan(2,5,userState,trialState);
         searchBan(3,4,userState,trialState);
         searchBan(9,13,userState,trialState);
         searchBan(10,11,userState,trialState);
           
        trialState = new SequenceState(this, userState);
        root.search(trialState);
        return trialState;

    }
    
    void searchBan(int ban, int banTarget, SequenceState userState,SequenceState trialState){
          if (userState.moves[ban] == Hero.OPEN) {
            trialState = new SequenceState(this, userState);
            root.search(trialState, banTarget);
            userState.moves[ban] = getBan(trialState,banTarget);
            userState.justification.put(userState.moves[ban], trialState.justification.get(trialState.moves[banTarget]));
            userState.taken[userState.moves[ban]] = true;
        } else {
            userState.taken[userState.moves[ban]] = true;
        }
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
