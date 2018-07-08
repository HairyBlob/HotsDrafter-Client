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
public class HotsService {

    DraftTree tree;
    
    int[] getSimulation(int map, int[] moves) throws Exception{    
         tree = new DraftTree(map); 
         SequenceState state=new SequenceState (tree,moves);
         return tree.getSimulation(state).moves;
    }
   
    float getEval4TeamA(int map, int[] teamA, int[]teamB) throws Exception{
        TeamEvaluator teamEvaluator = new TeamEvaluator();
        return teamEvaluator.getEstimatedEval(teamA, teamB, new int[0], new int[0], map);
    }
}
