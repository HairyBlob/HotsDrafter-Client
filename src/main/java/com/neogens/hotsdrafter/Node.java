package com.neogens.hotsdrafter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Collections;
import org.apache.commons.lang3.ArrayUtils;
/**
 *
 * @author François Gourdeau
 */

public abstract class Node {

    final int NOEVAL = -9999;
    final int ROOT = -1;
    final int nbPlayoutPrescribed = 1;
    final float scoreW = 1.0f;    
    final float expFactorW = 0.0f;

    DraftTree tree;
    public int move;
    int level;

    Node parent;
    Node bestChild = null;
    HashMap<Integer, Node> children=new HashMap<>();

    float teamATotalWin = 0;  //en additionant les probabilites
    long totalPlayout = 1;
    int[] nbEvalSimuls = new int[Hero.heroes.length];
    float[] totalEvalSimuls = new float[Hero.heroes.length];

    public float teamAEstimatedWinProb = NOEVAL;
    public float teamAPScore = NOEVAL;
    public float teamBPScore = NOEVAL;
    public float teamAWinProb = NOEVAL;

    boolean explored = false; // Tout les enfants sont explore
    boolean estimatedByChild = false;

    double scoreUCT = 0;
    int maxLevel;
   
    protected Node(){
        
    }
    Node createNode(Node parent, DraftTree tree, SequenceState state, int move, int level){
        if (Move.sequence[level]== Move.DraftTeamA||Move.sequence[level]== Move.DraftTeamB){
            return new DraftNode(parent,tree,state,move,level);
        } else   
        return new BanNode(parent,tree,state,level); 
    }
  
     boolean search(SequenceState targetState) {
         return search(targetState,targetState.moves.length-1);
     }
    boolean search(SequenceState targetState, int targetLevel) {
        if (explored) {
            return false;
        }
        Node child;
        child = getBestMove(targetState, true);

        if (child != null) {
            if (tree.debug) {
                System.out.println("Explore " + child.getHero() + " (" + (child.level) + ")");
            }
            if (child.getMove() != Hero.OPEN) {
                targetState.moves[level + 1] = child.getMove();
               
             
                targetState.taken[targetState.moves[level + 1]] = true;
                if (!Move.isBan(level+1)){ //Le ban est géré ailleurs
                     targetState.justification.put(child.move, this);
                }
            }
            if (child.level == targetLevel) {
                return true;
            }
            return child.search(targetState, targetLevel);
        } else {
            return false;
        }
    }

    Node getBestMove(SequenceState state) {
        return getBestMove(state,false);
    }

    Node getBestMove(SequenceState state, boolean toExplore) {
        //lastState=state;
        
        if (level >= Move.sequence.length - 1) {
            return null;
        }
        Node child;
        
        
        int childSequence = Move.sequence[level + 1];

        if (childSequence == Move.BanTeamA || childSequence == Move.BanTeamB) {
            if (children.isEmpty()) { //
                child = createNode(this, tree, state, Hero.OPEN, level + 1);

            } else {
                child = children.get(Hero.OPEN);
            } 
           // if (child.move==Hero.OPEN){
                     child.move = state.moves[level+1];
           // }
            return child;
        }
        
        int chosedMove=state.moves[level+1];
        if (chosedMove!=Hero.OPEN && !choosen (chosedMove, state.moves, level)){ // Noeud déjà déterminé
             if (children.containsKey(chosedMove)) {
                child = children.get(chosedMove);
            } else {
                child = createNode(this, tree, state,chosedMove,level + 1); 
                //System.err.println("Moeud introuvable");
               
            } 
           
            return child;
        }
      
        int choosenMove = getBestMoveUCT( state,  toExplore);
        if (children.containsKey(choosenMove)) {
            child = children.get(choosenMove);

        } else {
            System.err.println("Noeud inexistant");
            child = createNode(this, tree, state, choosenMove, level + 1);
        }
        return child;
    }
  
    boolean choosen(int move, int[] moves, int soFar){
        for (int i=0; i<=soFar && i<moves.length;i++){
            if (moves[i]==move){
                return true;
            } 
        }
        return false;
    }
       
    public int getBestMoveUCT(SequenceState state, boolean toExplore){
        Node child;
        int childSequence = Move.sequence[level + 1];

        int choosenMove = Hero.OPEN;
        double bestScore = -100;
        double score;

        for (int moveToTest = 0; moveToTest < Hero.heroes.length; moveToTest++) {
            // Conditions ***
            if (state.taken[moveToTest]) { // dÃ©jÃ  pris
                continue;
            }
            if (Hero.CHO == moveToTest) { // le coup doit Ãªtre le premier de deux
                switch (childSequence) {
                    case Move.DraftTeamA:
                    case Move.DraftTeamB:
                        if ((level < Move.sequence.length - 1) || childSequence != Move.sequence[level + 2]) {
                            continue;
                        }
                }
            }
            if (Hero.GALL == moveToTest) { // cho doit Ãªtre le hÃ©ro prÃ©cÃ©dent
                if (getMove() != Hero.CHO) {
                    continue;
                }
            }
            score = NOEVAL;
            if (children.containsKey(moveToTest)) {
                child = children.get(moveToTest);
                if (child.explored && toExplore) {
                    continue;
                }
                if (!toExplore && child.getTeamAWinProb() == NOEVAL) {
                    continue;
                }
            } else {
                if (!toExplore) {
                    continue;
                }
                child = createNode(this, tree, state, moveToTest, level + 1);
            }
            score = (child.getTeamAWinProb() != NOEVAL) ? child.getTeamAWinProb() : (child.getTeamAEstimatedWinProb() != NOEVAL ? child.getTeamAEstimatedWinProb() : child.teamATotalWin / child.totalPlayout);

            if (childSequence == Move.BanTeamB || childSequence == Move.DraftTeamB) {  // On cherche le plus petit
                score = 1 - score;
            }

            if (children.containsKey(moveToTest)) {
                child = children.get(moveToTest);
           }
           if (toExplore) { // Sinon on veux le meilleur rÃ©sultat Ã  jour
                score = child.scoreUCT;
            }
            if (score > bestScore) {
                choosenMove = moveToTest;
                bestScore = score;
            }
        }
        return choosenMove;
    }
    
    double calculateEstimatedModelScore(SequenceState state) {
        double totalScore = 0;
        int[] teamASlot = new int[0];
        int[] teamBSlot = new int[0];
        int[] banASlot = new int[0];
        int[] banBSlot = new int[0];
        boolean realDraft=true;

        for (int i = 0; i < Move.sequence.length; i++) { //preparer avec move deja faits
            //boolean ownTeam = Move.team(Move.sequence[level])== Move.team(Move.sequence[i]);
            if (realDraft || ((0== Move.team(Move.sequence[i])&& tree.powA)) || ((1== Move.team(Move.sequence[i])&& tree.powB))){
                if (!isDoublon(state.moves, i)) {
                    if (state.moves[i] != Hero.OPEN) {
                        switch (Move.sequence[i]) {
                            case Move.BanTeamB:
                                banBSlot = ArrayUtils.add(banBSlot, state.moves[i]);
                                break;
                            case Move.DraftTeamA:
                                teamASlot = ArrayUtils.add(teamASlot, state.moves[i]);
                                break;
                            case Move.BanTeamA:
                                banASlot = ArrayUtils.add(banASlot, state.moves[i]);
                                break;
                            case Move.DraftTeamB:
                                teamBSlot = ArrayUtils.add(teamBSlot, state.moves[i]);
                        }
                    }
                } else {
                    if (Move.sequence[i]!=Move.BanTeamB &&Move.sequence[i]!=Move.BanTeamA){
                        realDraft = false;
                    }
                    
                }
            }
        }
        totalScore = DraftTree.evaluator.getEstimatedEval(teamASlot, teamBSlot,banASlot,banBSlot,tree.map);
        totalScore=totalScore/tree.basePlausability; // ajustement plausability META
        this.teamAEstimatedWinProb=(float) totalScore;
        setTeamAPScore(DraftTree.evaluator.getEstimatedTeamPlausability(teamASlot));
        setTeamBPScore(DraftTree.evaluator.getEstimatedTeamPlausability(teamBSlot));
        if (Move.sequence[level]==Move.DraftTeamA){
             this.scoreUCT=totalScore*getTeamAPScore();
        } 
        if (Move.sequence[level]==Move.DraftTeamB){
             this.scoreUCT=(1-totalScore)*getTeamBPScore();
        } 
      
        return totalScore;
    }
  /*
    private void reportEvalSimuls(int[] teamASlot, int[] teamBSlot, float probWin) {
        for (int i = 0; i < 5; i++) {  // rapporter les stats     
            totalEvalSimuls[teamASlot[i]] += probWin;
            nbEvalSimuls[teamASlot[i]]++;
            backpropHeroWinRate(teamASlot[i], probWin);
            totalEvalSimuls[teamBSlot[i]] += 1 - probWin;
            nbEvalSimuls[teamBSlot[i]]++;
            backpropHeroWinRate(teamBSlot[i], 1 - probWin);
        }
    }
*/

    void backPropWinRate(float incTotalWins, long incTotalPlayout, int maxLevel) {

        if (parent != null) {
            parent.adjustScore(getTeamAWinProb());
            parent.totalPlayout += incTotalPlayout;
            parent.teamATotalWin += incTotalWins;
            parent.maxLevel = maxLevel;
            parent.backPropWinRate(incTotalWins, incTotalPlayout, maxLevel);
        }
    }

     protected void adjustScore(float realScore){
         adjustScore(realScore, children);

     }
    protected void adjustScore(float realScore,  HashMap<Integer, Node> children) {
        if (tree.debug) {
            System.out.print("adjustScore " + (level == ROOT ? "root" : Hero.heroes[getMove()]) + " " + realScore + " (" + level + ")");
        }
        if (realScore != NOEVAL) {
            this.setTeamAWinProb(realScore);
        }

        for (Node child : children.values()) {
            switch (Move.sequence[level + 1]) {
                case Move.BanTeamA:
                case Move.DraftTeamA:
                    if (realScore != NOEVAL &&  child.getTeamAWinProb()!=NOEVAL) {
                        if (this.getTeamAWinProb() < child.getTeamAWinProb()) {  //System.out.println(" up "+this.teamAWinProb);
                            this.setTeamAWinProb(child.getTeamAWinProb());
                            bestChild = child;
                        }
                    }
                    break;
                case Move.BanTeamB:
                case Move.DraftTeamB:
                    if (realScore != NOEVAL && child.getTeamAWinProb()!=NOEVAL) {
                        if (this.getTeamAWinProb() > child.getTeamAWinProb()) { //System.out.println(" down "+this.teamAWinProb);
                            this.setTeamAWinProb(child.getTeamAWinProb());
                            bestChild = child;
                        }
                    }
            }
        }
    }
/*
    private void backpropHeroWinRate(int hero, float probWin) {
        if (parent != null) {
            parent.totalEvalSimuls[hero] += probWin;
            parent.nbEvalSimuls[hero]++;
            parent.backpropHeroWinRate(hero, probWin);
        }
    }
*/
    boolean isLeaf(){      
       return level == Move.sequence.length - 1;
    }
    
    void printTeams(int[] teamASlot, int[] teamBSlot) {
        System.out.print("Team A: ");
        for (int j = 0; j < 5; j++) {
            System.out.print(Hero.heroes[teamASlot[j]] + " ");
        }

        System.out.print(" - ");
        System.out.print("Team B: ");
        for (int j = 0; j < 5; j++) {
            System.out.print(Hero.heroes[teamBSlot[j]] + " ");
        }
        System.out.println();
    }

    void printStat() {
        System.out.println(level + "->\t" + padRight(Hero.heroes[getMove()], 15) + "\t" + scoreUCT + "\t" + (teamATotalWin / totalPlayout) + "\t(" + teamATotalWin + "/" + totalPlayout + ") --" + this.maxLevel);
    }

    void printStats() {
        System.out.println("Alternatives:");
        ArrayList<Move> result = new ArrayList<>();

        for (Node node : children.values()) {
           // Move move = new Move(new Hero(node.state.moves[node.level]));
            Move move = new Move(new Hero(node.getMove()));
            move.evaluation = (float) node.teamATotalWin / node.totalPlayout;
            move.score = node.scoreUCT;
            result.add(move);

        }
        Collections.sort(result, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                if (o1.evaluation == o2.evaluation) {
                    return 0;
                }
                return o1.evaluation > o2.evaluation ? -1 : 1;
            }
        });

        for (Move move : result) {
            System.out.println(padRight(Hero.heroes[move.hero.hero], 15) + "\t" + move.evaluation + "\t" + move.score);

        }
    }

    public String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    // Getter et setter pour TableView
   // public int getMove(SequenceState state) {
    //    return getMove();
   // }

     public int getMove() {
        return move;
    }
    public int getScenarioMove(){
        return getMove();
    }
    public void setMove(int move) {
        this.move = move;
    }

    public String getHero() {
        if (getMove()==-1){
            return "-";
        }
        return Hero.heroes[getMove()];
    }

    public int getNbPlayoutPrescribed() {
        return nbPlayoutPrescribed;
    }

    public float getExpFactor() {
        return expFactorW;
    }

    public int[] getNbEvalSimuls() {
        return nbEvalSimuls;
    }

    public void setNbEvalSimuls(int[] nbEvalSimuls) {
        this.nbEvalSimuls = nbEvalSimuls;
    }

    public float[] getTotalEvalSimuls() {
        return totalEvalSimuls;
    }

    public void setTotalEvalSimuls(float[] totalEvalSimuls) {
        this.totalEvalSimuls = totalEvalSimuls;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public HashMap<Integer, Node> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Integer, Node> children) {
        this.children = children;
    }

    public double getScoreUCT() {
        return scoreUCT;
    }

     public String getScoreUCTDesc() {
        double score = getScoreUCT();
        if (score==0){
            return "-";
        }
        return ""+score;
    }
    public void setScoreUCT(double scoreUCT) {
        this.scoreUCT = scoreUCT;
    }

   
    public float getEstimatedScore() {
        return getTeamAEstimatedWinProb();
    }

    public String getEstimatedScoreDesc() 
    {
        float score = (float) (getTeamAEstimatedWinProb());
        if (score==NOEVAL){
            return "-";
        }
        return ""+score*tree.basePlausability;
    }
    public void setEstimatedScore(float estimatedScore) {
        this.setTeamAEstimatedWinProb(estimatedScore);
    }

    public float getRealScore() {
        return getTeamAWinProb();
    } 
    
    public String getRealScoreDesc() {
        if (getTeamAWinProb()==NOEVAL){
            return "-";
        }
        return ""+getTeamAWinProb();
    }
    

    public void setRealScore(float realScore) {
        this.setTeamAWinProb(realScore);
    }

    public float getTotalWin() {
        return teamATotalWin;
    }

    public void setTotalWin(float totalWin) {
        this.teamATotalWin = totalWin;
    }

    public long getTotalPlayout() {
        return totalPlayout;
    }

    public void setTotalPlayout(long totalPlayout) {
        this.totalPlayout = totalPlayout;
    }

    public int getLevel() {
        return level;
    }
    public String getLevelSeq() {
        return level+" - "+Move.sequenceDesc[Move.sequence[level]];
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public float getTeamAEstimatedWinProb() {
        return teamAEstimatedWinProb;
    }

    public void setTeamAEstimatedWinProb(float teamAEstimatedWinProb) {
        this.teamAEstimatedWinProb = teamAEstimatedWinProb;
    }

    public float getTeamAWinProb() {
        return teamAWinProb;
    }

    public void setTeamAWinProb(float teamAWinProb) {
        this.teamAWinProb = teamAWinProb;
    }

    public float getTeamAPScore() {
        return teamAPScore;
    }

    public void setTeamAPScore(float teamAPScore) {
        this.teamAPScore = teamAPScore;
    }

    public float getTeamBPScore() {
        return teamBPScore;
    }

    public void setTeamBPScore(float teamBPScore) {
        this.teamBPScore = teamBPScore;
    }
 public String getTeamAPScoreDesc() 
    {
        float score = getTeamAPScore();
        if (score==NOEVAL){
            return "-";
        }
        return ""+score;
    }
  public String getTeamBPScoreDesc() 
    {
        float score = getTeamBPScore();
        if (score==NOEVAL){
            return "-";
        }
        return ""+score;
    }

    private boolean isDoublon(int[] moves, int i) {
        for (int j=i-1;j>0;--j){
            if (moves[i]==moves[j]){
                return true;
            }              
        }
        return false;
    }   
}
