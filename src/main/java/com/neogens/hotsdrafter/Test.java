package com.neogens.hotsdrafter;

/**
 *
 * @author FranÃƒÂ§ois
 */
public class Test {

    public static void main(String[] args) throws Exception {

        HotsService s = new HotsService();

        int[] moves = new int[]{
            Hero.OPEN, // BanA    
            Hero.OPEN, // BanB
            Hero.OPEN, // BanA    
            Hero.OPEN, // BanB
            Hero.OPEN, // DraftA
            Hero.OPEN, Hero.OPEN, //DraftB
            Hero.OPEN, Hero.OPEN, // DraftA
            Hero.OPEN, // BanB  
            Hero.OPEN, // BanA
            Hero.OPEN, Hero.OPEN, //DraftB
            Hero.OPEN, Hero.OPEN, // DraftA
            Hero.OPEN //DraftB
        };

        moves = s.getSimulation(Maps.BRAXIS, moves);
        System.out.println("Simul:");
        for (int move : moves) {
            System.out.println(Hero.heroes[move]);
        }
        System.out.print("Eval:");
        System.out.println(s.getEval4TeamA(Maps.BRAXIS, new int[]{moves[4], moves[7], moves[8], moves[13], moves[14]},
                new int[]{moves[5], moves[6], moves[8], moves[12], moves[15]}));
    }

}
