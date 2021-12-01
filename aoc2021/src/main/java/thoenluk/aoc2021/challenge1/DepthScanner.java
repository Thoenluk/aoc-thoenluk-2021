package thoenluk.aoc2021.challenge1;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.Ut;

import java.util.List;

public class DepthScanner implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final List<Integer> depths = Ut.stringToIntegerList(input);
        int previousDepth = Integer.MAX_VALUE;
        int largerMeasurements = 0;

        for (Integer depth : depths) {
            if (depth > previousDepth) {
                largerMeasurements++;
            }
            previousDepth = depth;
        }

        return Integer.toString(largerMeasurements);
    }

    @Override
    public String saveChristmasAgain(String input) {
        final List<Integer> depths = Ut.stringToIntegerList(input);

        // This operation is technically pointless as I know depths will be an ArrayList. But since the API only specifies
        // it will be a list and random access on a LinkedList would be very bad, instead convert it to array.
        final Integer[] depthsAsArray = depths.toArray(new Integer[0]);
        int largerMeasurements = 0;

        for (int i = 3; i < depthsAsArray.length; i++) {
            if (depthsAsArray[i] > depthsAsArray[i - 3]) {
                largerMeasurements++;
            }
        }

        return Integer.toString(largerMeasurements);
    }
}
