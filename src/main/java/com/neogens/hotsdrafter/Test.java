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
        System.out.println(s.getEval4TeamA(Maps.BRAXIS, new int[]{moves[2], moves[5], moves[6], moves[11], moves[12]},
                new int[]{moves[3], moves[4], moves[6], moves[10], moves[13]}));
    }

}
