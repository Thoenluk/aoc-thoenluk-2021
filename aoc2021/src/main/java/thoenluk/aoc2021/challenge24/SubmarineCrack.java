package thoenluk.aoc2021.challenge24;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge24.monad.MONAD;

public class SubmarineCrack implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        MONAD monad = new MONAD(input);
        return Long.toString(monad.getHighestValidModelNumber());
    }

    @Override
    public String saveChristmasAgain(String input) {
        MONAD monad = new MONAD(input);
        return Long.toString(monad.getLowestValidModelNumber());
    }
}
