package thoenluk.aoc2021.challenge0;

import thoenluk.aoc2021.ChristmasSaver;

/**
 *
 * @author Lukas Thöni lukas.thoeni@gmx.ch
 */
public class Uppercaser implements ChristmasSaver {

    @Override
    public String saveChristmas(String input) {
        return input.toUpperCase();
    }
    
}
