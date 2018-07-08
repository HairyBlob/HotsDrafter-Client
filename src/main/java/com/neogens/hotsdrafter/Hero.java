/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neogens.hotsdrafter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FranÃ§ois
 */
public class Hero {

    int hero;

    Hero(int hero) {
        this.hero = hero;
    }

    static String[] heroes = new String[]{"ABATHUR", "ANUBARAK", "ARTHAS", "AZMODAN", "BRIGHTWING", "CHEN", "DIABLO", "ETC", "FALSTAD",
        "GAZLOWE", "ILIDAN", "JAINA", "JOHANNA", "KAELTHAS", "KERRIGAN", "KHARAZIM", "LEORIC", "LILI", "MALFURION", "MURADIN", "MURKY",
        "NAZEEBO", "NOVA", "RAYNOR", "REHGAR", "HAMMER", "SONYA", "STITCHES", "SYLVANAS", "TASSADAR", "BUTCHER", "VIKINGS", "THRALL",
        "TYCHUS", "TYRAEL", "TYRANDE", "UTHER", "VALLA", "ZAGARA", "ZERATUL", "REXXAR", "MORALES", "ARTANIS", "CHO", "GALL", "LUNARA",
        "GREYMANE", "LIMING", "XUL", "DEHAKA", "TRACER", "CHROMIE", "MEDIVH", "GULDAN", "AURIEL", "ALARAK", "ZARYA", "SAMURO", "VARIAN",
        "RAGNAROS", "ZULJIN", "VALEERA", "LUCIO", "PROBIUS", "CASSIA", "GENJI", "DVA", "MALTHAEL", "STUKOV", "GARROSH", "KELTHUZAD", "ANA",
        "JUNKRAT", "ALEXSTRASZA", "HANZO", "BLAZE", "MAIEV", "FENIX", "DECKARD", "YREL"};

    static double[] pickRate;
    static double totalPickRate;

    static {
        ObjectInputStream iis;
        try {

            iis = new ObjectInputStream(Hero.class.getResourceAsStream("PickRate.dat"));
            pickRate = (double[]) iis.readObject();

            totalPickRate = 0;
            for (int i = 0; i < pickRate.length; i++) {
                totalPickRate += pickRate[i];
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FXMLClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static final int OPEN = -1;
    static final int ABATHUR = 0;
    static final int ANUBARAK = 1;
    static final int ARTHAS = 2;
    static final int AZMODAN = 3;
    static final int BRIGHTWING = 4;
    static final int CHEN = 5;
    static final int DIABLO = 6;
    static final int ETC = 7;
    static final int FALSTAD = 8;
    static final int GAZLOWE = 9;
    static final int ILIDAN = 10;
    static final int JAINA = 11;
    static final int JOHANNA = 12;
    static final int KAELTHAS = 13;
    static final int KERRIGAN = 14;
    static final int KHARAZIM = 15;
    static final int LEORIC = 16;
    static final int LILI = 17;
    static final int MALFURION = 18;
    static final int MURADIN = 19;
    static final int MURKY = 20;
    static final int NAZEEBO = 21;
    static final int NOVA = 22;
    static final int RAYNOR = 23;
    static final int REHGAR = 24;
    static final int HAMMER = 25;
    static final int SONYA = 26;
    static final int STITCHES = 27;
    static final int SYLVANAS = 28;
    static final int TASSADAR = 29;
    static final int BUTCHER = 30;
    static final int VIKINGS = 31;
    static final int THRALL = 32;
    static final int TYCHUS = 33;
    static final int TYRAEL = 34;
    static final int TYRANDE = 35;
    static final int UTHER = 36;
    static final int VALLA = 37;
    static final int ZAGARA = 38;
    static final int ZERATUL = 39;
    static final int REXXAR = 40;
    static final int MORALES = 41;
    static final int ARTANIS = 42;
    static final int CHO = 43;
    static final int GALL = 44;
    static final int LUNARA = 45;
    static final int GREYMANE = 46;
    static final int LIMING = 47;
    static final int XUL = 48;
    static final int DEHAKA = 49;
    static final int TRACER = 50;
    static final int CHROMIE = 51;
    static final int MEDIVH = 52;
    static final int GULDAN = 53;
    static final int AURIEL = 54;
    static final int ALARAK = 55;
    static final int ZARYA = 56;
    static final int SAMURO = 57;
    static final int VARIAN = 58;
    static final int RAGNAROS = 59;
    static final int ZULJIN = 60;
    static final int VALEERA = 61;
    static final int LUCIO = 62;
    static final int PROBIUS = 63;
    static final int CASSIA = 64;
    static final int GENJI = 65;
    static final int DVA = 66;
    static final int MALTHAEL = 67;
    static final int STUKOV = 68;
    static final int GARROSH = 69;
    static final int KELTHUZAD = 70;
    static final int ANA = 71;
    static final int JUNKRAT = 72;
    static final int ALEXSTRASZA = 73;
    static final int HANZO = 74;
    static final int BLAZE = 75;
    static final int MAIEV = 76;
    static final int FENIX = 77;
    static final int DECKARD = 78;
    static final int YREL = 79;
    
    class Team {
        Hero[] heroes;
        Team(Hero[] heroes){
            this.heroes=heroes;           
        }
    }
}
