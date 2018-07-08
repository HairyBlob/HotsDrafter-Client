package com.neogens.hotsdrafter;

/**
 *
 * @author François Gourdeau
 */
class Move {
    static final int DraftTeamA= 0;
    static final int DraftTeamB= 1;
    static final int BanTeamA= 2; 
    static final int BanTeamB= 3;

    static final int[] sequence={BanTeamA,BanTeamB,DraftTeamA,DraftTeamB,DraftTeamB,DraftTeamA,DraftTeamA,BanTeamB,BanTeamA,DraftTeamB,DraftTeamB,DraftTeamA,DraftTeamA,DraftTeamB};
    static final String[] sequenceDesc = {"A","B","A X","B X"};

    static int team(int sequence) {
        return sequence%2;
    }
    
    static boolean isBan(int level) {
        return sequence[level]==BanTeamA||sequence[level]==BanTeamB;
    }
    Hero hero;
    float evaluation;
    double score;
    float totalWin;
    int   totalPlayout;
    int level;
    
    public Move(Hero hero){
        this.hero=hero;
        
    }
    public Move(Hero hero,double score){
        this.hero=hero;
        this.score=score;

        
    }
 
   
}
