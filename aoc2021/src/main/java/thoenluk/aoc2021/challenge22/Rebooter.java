package thoenluk.aoc2021.challenge22;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.*;

import java.util.*;

public class Rebooter implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        List<String> smallInstructions = new LinkedList<>();

        for (String line : lines) {
            final int x1 = UtParsing.cachedParseInt(line.split("x=|\\.\\.")[1]);
            if (-50 <= x1 && x1 <= 50) {
                smallInstructions.add(line);
            }
        }

        return Long.toString(rebootReactor(smallInstructions));
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        List<String> instructions = new LinkedList<>(Arrays.asList(lines));
        return Long.toString(rebootReactor(instructions));
    }

    private long rebootReactor(List<String> lines) {
        Set<ThreeDArea> onRegions = new HashSet<>();

        for (String line : lines) {
            final boolean isOnInstruction = line.charAt(1) == 'n';
            final String[] numberTokens = line.replaceAll("[^-\\d]+", " ").trim().split(" ");

            final int x1 = UtParsing.cachedParseInt(numberTokens[0]);
            final int x2 = UtParsing.cachedParseInt(numberTokens[1]);
            final int y1 = UtParsing.cachedParseInt(numberTokens[2]);
            final int y2 = UtParsing.cachedParseInt(numberTokens[3]);
            final int z1 = UtParsing.cachedParseInt(numberTokens[4]);
            final int z2 = UtParsing.cachedParseInt(numberTokens[5]);

            final ThreeDPosition bottomLeft = new ThreeDPosition(
                        Math.min(x1, x2),
                        Math.min(y1, y2),
                        Math.min(z1, z2)
                    );

            final ThreeDPosition topRight = new ThreeDPosition(
                        Math.max(x1, x2),
                        Math.max(y1, y2),
                        Math.max(z1, z2)
                    );

            final ThreeDArea region = new ThreeDArea(bottomLeft, topRight);

            if (isOnInstruction) {
                Set<ThreeDArea> regionPieces = new HashSet<>();
                regionPieces.add(region);
                Set<ThreeDArea> nextIteration;

                for (ThreeDArea onRegion : onRegions) {
                    nextIteration = new HashSet<>();

                    for (ThreeDArea piece : regionPieces) {
                        nextIteration.addAll(piece.subtract(onRegion));
                    }

                    regionPieces = nextIteration;
                }

                onRegions.addAll(regionPieces);
            }
            else {
                final Set<ThreeDArea> pieces = new HashSet<>();

                for(ThreeDArea onRegion : onRegions) {
                    pieces.addAll(onRegion.subtract(region));
                }

                onRegions = pieces;
            }
        }

        return getOnCubes(onRegions);
    }

    private long getOnCubes(Set<ThreeDArea> onRegions) {
        long onCubes = 0;

        for (ThreeDArea onRegion : onRegions) {
            onCubes += onRegion.getVolume();
        }

        return onCubes;
    }
}
