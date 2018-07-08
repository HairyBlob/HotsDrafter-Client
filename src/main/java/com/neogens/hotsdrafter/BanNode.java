/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neogens.hotsdrafter;

import java.util.HashMap;
/**
 *
 * @author François Gourdeau
 */
public class BanNode extends Node {
    
    
    public BanNode(Node parent, DraftTree tree,SequenceState state, int level) {
        this.parent = parent;
        parent.children.put( Hero.OPEN, this);
        //this.state = new SequenceState(state.tree, state.moves);
        //lastState=state;
        this.tree=tree;
        this.move = Hero.OPEN;
     
        //this.state.moves[level] = move;
        //this.state.taken[move] = true;
        this.level = level;
            
        tree.searchActionDone = true;
     
        this.totalPlayout=1;
    
    }
   /*
    @Override
    public int getScenarioMove() { // dÃ©terminÃ© de faÃ§on dynamique
        Node node=this; 
        int target=Move.DraftTeamB;
        if (Move.sequence[node.level]==Move.BanTeamB){
            target=Move.DraftTeamA;
        }
       
        while (Move.sequence[node.level]!=target){
            node = node.getBestMove(node.lastState);
            if (node==null){
                System.err.println("arbre pas encore développé?");
                return Hero.OPEN; //arbre pas encore développé
            }
        }
        if (node.move==Hero.OPEN){
             System.err.println("getbestmove -1");
        }
        this.move=node.move;
        
        return move;
    }
    
    public float getTeamAWinProb() { // dÃ©terminÃ© de faÃ§on dynamique
        return teamAWinProb;
    }
    */

    @Override
     protected void adjustScore(float realScore,  HashMap<Integer, Node> children) {  // Le ban est transparent
         parent.adjustScore(realScore, children);
    }
}
