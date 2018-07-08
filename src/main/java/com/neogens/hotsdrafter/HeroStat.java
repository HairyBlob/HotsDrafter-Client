package com.neogens.hotsdrafter;

/**
 *
 * @author François Gourdeau
 */
public class HeroStat {

    public int hero;
    public int nbEvalSimuls;
    public float totalEvalSimuls;

    HeroStat(int hero,float totalEvalSimuls,int nbEvalSimuls) {
        this.hero = hero;
        this.nbEvalSimuls = nbEvalSimuls;
        this.totalEvalSimuls = totalEvalSimuls;
    }

    public String getHero() {
        return Hero.heroes[hero];
    }

    public void setHero(int hero) {
        this.hero = hero;
    }

    public int getNbEvalSimuls() {
        return nbEvalSimuls;
    }

    public void setNbEvalSimuls(int nbEvalSimuls) {
        this.nbEvalSimuls = nbEvalSimuls;
    }

    public float getTotalEvalSimuls() {
        return totalEvalSimuls;
    }

    public void setTotalEvalSimuls(float totalEvalSimuls) {
        this.totalEvalSimuls = totalEvalSimuls;
    }
    
     public float getRatio() {
        return totalEvalSimuls/nbEvalSimuls;
    }

}
