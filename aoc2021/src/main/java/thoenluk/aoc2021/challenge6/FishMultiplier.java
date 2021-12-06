package thoenluk.aoc2021.challenge6;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Ut;

import java.util.List;
import java.util.stream.LongStream;

public class FishMultiplier implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        List<Integer> fish = Ut.commaSeparatedStringToIntegerList(input);
        long[] fishByInternalTimers = new long[9];
        for (Integer individualFish : fish) {
            fishByInternalTimers[individualFish]++;
        }

        long fishGivingBirth;
        int i, n;

        for (i = 0; i < 80; i++) {
            fishGivingBirth = fishByInternalTimers[0];
            for (n = 0; n < fishByInternalTimers.length - 1; n++) {
                fishByInternalTimers[n] = fishByInternalTimers[n + 1];
            }
            fishByInternalTimers[6] += fishGivingBirth;
            fishByInternalTimers[8] = fishGivingBirth;
        }

        return Long.toString(
                LongStream.of(fishByInternalTimers)
                .sum());
    }

    @Override
    public String saveChristmasAgain(String input) {
        List<Integer> fish = Ut.commaSeparatedStringToIntegerList(input);
        long[] fishByInternalTimers = new long[9];
        for (Integer individualFish : fish) {
            fishByInternalTimers[individualFish]++;
        }

        long fishGivingBirth;
        int i, n;

        for (i = 0; i < 256; i++) {
            fishGivingBirth = fishByInternalTimers[0];
            for (n = 0; n < fishByInternalTimers.length - 1; n++) {
                fishByInternalTimers[n] = fishByInternalTimers[n + 1];
            }
            fishByInternalTimers[6] += fishGivingBirth;
            fishByInternalTimers[8] = fishGivingBirth;
        }

        return Long.toString(
                LongStream.of(fishByInternalTimers)
                        .sum());
    }
}
