package com.neogens.hotsdrafter;
import com.opencsv.bean.CsvBindByName;

/**
 *
 * @author François Gourdeau
 */

public class ReplayCharacters {
    @CsvBindByName
     String replayID;

    @CsvBindByName
     String heroID;

    @CsvBindByName(column = "Is Winner")
     String isWinner;



}
