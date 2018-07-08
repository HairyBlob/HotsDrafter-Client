/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neogens.hotsdrafter;

/**
 *
 * @author François Gourdeau
 */
public class DraftNode extends Node {
     public DraftNode(Node parent, DraftTree tree, SequenceState state, int move, int level) {
        this.parent = parent;
        parent.children.put(move, this);
        //this.state = new SequenceState(state.tree, state.moves);
              this.tree=tree;
        this.move = move;
       
        //lastState=state;
        state.moves[level] = move;
        //this.state.taken[move] = true;
        this.level = level;
            
        tree.searchActionDone = true;
      
        this.teamAEstimatedWinProb=(float) this.calculateEstimatedModelScore(state);// Le move est dÃ©jÃ  dans le state
       
        /*
        if (this.isLeaf()){ // c'est une feuille
          this.teamAWinProb=this.teamAEstimatedWinProb; 
          this.teamATotalWin=this.teamAWinProb;
          backPropWinRate((float) this.teamAWinProb, 1, level);
          explored=true;
        }
 
        this.totalPlayout=1;
                */
    }
}
