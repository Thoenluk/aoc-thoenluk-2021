package thoenluk.aoc2021.challenge20;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.HashMap;
import java.util.Map;

import static thoenluk.aoc2021.ut.Position.NeighbourDirection.OMNIDIRECTIONAL_AND_SELF;

public class ImageEnhancer implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        return Integer.toString(solveForIterationCount(input, 2));
    }

    @Override
    public String saveChristmasAgain(String input) {
        return Integer.toString(solveForIterationCount(input, 50));
    }

    private int solveForIterationCount(String input, int iterationCount) {
        final String[] data = UtStrings.splitStringWithEmptyLines(input);

        final int[] enhancementAlgorithm = parseEnhancementAlgorithm(data[0]);
        Map<Position, Integer> image = parseImage(data[1], iterationCount);

        for (int i = 0; i < iterationCount; i++) {
            Map<Position, Integer> newImage = new HashMap<>();

            for (Position pixel : image.keySet()) {
                newImage.put(pixel,
                        enhancementAlgorithm[
                                getOutputPixelIndex(pixel, image, enhancementAlgorithm[0] == 0 ? 0 : i % 2)
                        ]
                );
            }

            image = newImage;
        }

        int litPixels = 0;

        for (Integer pixelValue : image.values()) {
            litPixels += pixelValue;
        }

        return litPixels;
    }

    private int[] parseEnhancementAlgorithm(String algorithmData) {
        final int[] enhancementAlgorithm = new int[algorithmData.length()];

        for (int i = 0; i < algorithmData.length(); i++) {
            enhancementAlgorithm[i] = algorithmData.charAt(i) == '#' ? 1 : 0;
        }

        return enhancementAlgorithm;
    }

    private Map<Position, Integer> parseImage(String imageData, int padding) {
        final String[] lines = UtStrings.splitMultilineString(imageData);
        final Map<Position, Integer> image = new HashMap<>();

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                image.put(new Position(y, x), lines[y].charAt(x) == '#' ? 1 : 0);
            }
        }

        for (int y = -padding; y < lines.length + padding; y++) {
            for (int x = -padding; x < lines.length + padding; x++) {
                image.putIfAbsent(new Position(y, x), 0);
            }
        }

        return image;
    }

    private int getOutputPixelIndex(Position pixel, Map<Position, Integer> image, int defaultValue) {
        int outputPixelIndex = 0;

        for (Position neighbour : pixel.getNeighbours(OMNIDIRECTIONAL_AND_SELF)) {
            outputPixelIndex *= 2;
            outputPixelIndex += image.getOrDefault(neighbour, defaultValue);
        }

        return outputPixelIndex;
    }
}
