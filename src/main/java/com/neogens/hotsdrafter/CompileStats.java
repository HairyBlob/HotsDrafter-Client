/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neogens.hotsdrafter;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.Iterator;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author François Gourdeau
 */
public class CompileStats {

    private static final String REPLAYS_CSV_FILE_PATH = "C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter\\Models\\ReplayCharacters.csv";
    
    public static void main(String[] args) throws Exception {
        double[] pickRate = new double[Hero.heroes.length];
        //double[][] totalSynergy = new double[Hero.heroes.length][Hero.heroes.length];
        //double[][] countSynergy = new double[Hero.heroes.length][Hero.heroes.length];

        //double[][] totalOpposition = new double[Hero.heroes.length][Hero.heroes.length];
        //double[][] countOpposition = new double[Hero.heroes.length][Hero.heroes.length];

        int[] winners = new int[5];
        int[] losers = new int[5];
        int iteration = 0;

        String game;
        try (
            Reader reader = Files.newBufferedReader(Paths.get(REPLAYS_CSV_FILE_PATH));) {
            CsvToBean<ReplayCharacters> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(ReplayCharacters.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<ReplayCharacters> csvUserIterator = csvToBean.iterator();
            ReplayCharacters replayCharacters = csvUserIterator.next();
            while (csvUserIterator.hasNext()) {

                int x = 0;
                int y = 0;
                game = replayCharacters.replayID;
                do {
                    if ("1".equals(replayCharacters.isWinner)) {
                        winners[x++] = Integer.parseInt(replayCharacters.heroID)-1;
                    } else {
                        losers[y++] = Integer.parseInt(replayCharacters.heroID)-1;
                    }
                    pickRate[Integer.parseInt(replayCharacters.heroID)-1]++;
                    replayCharacters = csvUserIterator.next();
                } while (replayCharacters.replayID.equals(game) && csvUserIterator.hasNext());

                if (x != 5 || y != 5) {
                    System.out.println(iteration);
                }
                /*
                for (int i = 0; i < 5; i++) {
                    for (int j = i + 1; j < 5; j++) {
                        if (winners[i] == winners[j]) {
                            System.out.println(iteration);
                        }
                        totalSynergy[winners[i]][winners[j]]++;
                        countSynergy[winners[i]][winners[j]]++;
                        countSynergy[losers[i]][losers[j]]++;

                        totalSynergy[winners[j]][winners[i]]++;
                        countSynergy[winners[j]][winners[i]]++;
                        countSynergy[losers[j]][losers[i]]++;
                    }
                    for (int j = 1; j < 5; j++) {
                        totalOpposition[winners[i]][losers[j]]++;
                        countOpposition[winners[i]][losers[j]]++;
                        countOpposition[losers[i]][winners[j]]++;
                    }
                }*/
                if (0 == Math.floorMod(iteration++, 500)) {
                    System.out.println(iteration);
                }
                
                // 
                /*
                 System.out.println("Replay : " + replayCharacters.replayID);
                 System.out.println("Hero id : " + replayCharacters.heroID);
                 System.out.println("win : " + replayCharacters.isWinner);
                 System.out.println("==========================");*/
            }
        }
           
        for (int i=0;i<Hero.heroes.length;i++){
             pickRate[i]= pickRate[i]/iteration;
        }
        /*
        FileOutputStream fos = new FileOutputStream("C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter\\Models\\TotalSynergy.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(totalSynergy);
        
        fos = new FileOutputStream("C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter\\Models\\CountSynergy.dat");
        oos = new ObjectOutputStream(fos);
        oos.writeObject(countSynergy);
        
        fos = new FileOutputStream("C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter\\Models\\TotalOpposition.dat");
        oos = new ObjectOutputStream(fos);
        oos.writeObject(totalOpposition);
        
        fos = new FileOutputStream("C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter\\Models\\CountOpposition.dat");
        oos = new ObjectOutputStream(fos);
        oos.writeObject(countOpposition);
        */
        FileOutputStream fos = new FileOutputStream("C:\\Users\\François\\Documents\\NetBeansProjects\\HotsDrafter\\Models\\PickRate.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(pickRate);
    }

}
