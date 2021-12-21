package thoenluk.aoc2021.challenge6;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.UtParsing;

import java.util.List;
import java.util.stream.LongStream;

public class FishMultiplier implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        return Long.toString(breedFish(input, 80));
    }

    @Override
    public String saveChristmasAgain(String input) {
        return Long.toString(breedFish(input, 256));
    }

    private long breedFish(String input, int days) {
        List<Integer> fish = UtParsing.commaSeparatedStringToIntegerList(input);
        long[] fishByInternalTimers = new long[9];
        for (Integer individualFish : fish) {
            fishByInternalTimers[individualFish]++;
        }

        long fishGivingBirth;
        int i, n;

        for (i = 0; i < days; i++) {
            fishGivingBirth = fishByInternalTimers[0];
            for (n = 0; n < fishByInternalTimers.length - 1; n++) {
                fishByInternalTimers[n] = fishByInternalTimers[n + 1];
            }
            fishByInternalTimers[6] += fishGivingBirth;
            fishByInternalTimers[8] = fishGivingBirth;
        }

        return LongStream.of(fishByInternalTimers)
                .sum();
    }
}
