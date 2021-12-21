package thoenluk.aoc2021.challenge21;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.ut.UtMath;
import thoenluk.aoc2021.ut.UtParsing;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.HashMap;
import java.util.Map;

public class DiceAirquotesRoller implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        int playerOnePosition = UtParsing.cachedParseInt(lines[0].split(": ")[1]);
        int playerTwoPosition = UtParsing.cachedParseInt(lines[1].split(": ")[1]);

        return Integer.toString(simulateDeterministicGame(playerOnePosition, playerTwoPosition));
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        int playerOnePosition = UtParsing.cachedParseInt(lines[0].split(": ")[1]);
        int playerTwoPosition = UtParsing.cachedParseInt(lines[1].split(": ")[1]);
        final Map<Integer, Integer> universesCreatedByTotalRoll = new HashMap<>();

        for (int firstRoll = 1; firstRoll < 4; firstRoll++) {
            for (int secondRoll = 1; secondRoll < 4; secondRoll++) {
                for (int thirdRoll = 1; thirdRoll < 4; thirdRoll++) {
                    universesCreatedByTotalRoll.compute(firstRoll + secondRoll + thirdRoll, (k, v) -> v == null ? 1 : v + 1);
                }
            }
        }

        final long[] winningMultiverses = simulateQuantumGame(playerOnePosition, 0, playerTwoPosition,
                                                    0, true, universesCreatedByTotalRoll);

        return Long.toString(Math.max(winningMultiverses[0], winningMultiverses[1]));
    }

    private int simulateDeterministicGame(int playerOnePosition, int playerTwoPosition) {
        final int[] playerOneScores = new int[10];
        int playerOneCycleScore = 0;

        for (int cycle = 0; cycle < 10; cycle++) {
            final int roll = (((cycle * 6) * 3) + 6);
            playerOnePosition += roll;
            playerOnePosition = UtMath.wrap(playerOnePosition, 10);
            playerOneScores[cycle] = playerOnePosition;
            playerOneCycleScore += playerOnePosition;
        }

        final int[] playerTwoScores = new int[10];
        int playerTwoCycleScore = 0;

        for (int cycle = 0; cycle < 10; cycle++) {
            final int roll = ((((cycle * 6) + 3) * 3) + 6);
            playerTwoPosition += roll;
            playerTwoPosition = UtMath.wrap(playerTwoPosition, 10);
            playerTwoScores[cycle] = playerTwoPosition;
            playerTwoCycleScore += playerTwoPosition;
        }

        final int playerOneNearWinCycles = 1000 / playerOneCycleScore;
        final int playerTwoNearWinCycles = 1000 / playerTwoCycleScore;

        final int cyclesToAdvance = Math.min(playerOneNearWinCycles, playerTwoNearWinCycles);

        int playerOneScore = cyclesToAdvance * playerOneCycleScore;
        int playerTwoScore = cyclesToAdvance * playerTwoCycleScore;
        int turn = 0;

        while (true) {
            playerOneScore += playerOneScores[turn];

            if (playerOneScore >= 1000) {
                return (cyclesToAdvance * 60 + turn * 6 + 3) * playerTwoScore;
            }

            playerTwoScore += playerTwoScores[turn];

            if (playerTwoScore >= 1000) {
                return (cyclesToAdvance * 60 + turn * 6 + 6) * playerTwoScore;
            }

            turn++;
        }

        /*
        The board wraps around after 10 spaces, therefore we can calculate everything here in log10 space. This makes
        die rolls cyclical after 10 turns, i.e. 30 rolls of the die. You may go around the board 9 times when rolling
        31 + 32 + 33, but you still land six spaces ahead of where you started.

        See below how players move in this 10 turn cycle.
        For player 1, every cycle of 10 turns is completely identical, making this something of a boring game as you only
        ever visit 3 spaces. Player 2 gets to have more fun, visiting 6 spaces in all. As they move a total of 25 spaces
        every 10 turns, their starting space flips to the other side of the board every turn.

        There is therefore little need to simulate the actual game: Determine who will come within one 20-turn cycle of
        reaching the goal first, set the scores, then play out the endgame. As scores are pseudorandom enough within
        these cycles, it's easier to play it out than predict it, though I'm sure you could.

        Player 1:

        1 2 3 = 6
        7 8 9 = 4
        13 14 15 = 2
        19 20 21 = 0
        25 26 27 = 8

        1: 7 + 1 + 3 + 3 + 1 = 15
        2: 8 + 2 + 4 + 4 + 2 = 20
        3: 9 + 3 + 5 + 5 + 3 = 25
        4: 10 + 4 + 6 + 6 + 4 = 30
        5: 1 + 5 + 7 + 7 + 5 = 25
        6: 2 + 6 + 8 + 8 + 6 = 30
        7: 3 + 7 + 9 + 9 + 7 = 35
        8: 4 + 8 + 10 + 10 + 8 = 40
        9: 5 + 9 + 1 + 1 + 9 = 25
        10: 6 + 10 + 2 + 2 + 10 = 30

        Player 2:

        4 5 6 = 5
        10 11 12 = 3
        16 17 18 = 1
        22 23 24 = 9
        28 29 30 = 7

        1: 6 + 9 + 10 + 9 + 6 = 40 (+ 15)
        2: 7 + 10 + 1 + 10 + 7 = 35 (+ 20)
        3: 8 + 1 + 2 + 1 + 8  = 20 (+ 25)
        4: 9 + 2 + 3 + 2 + 9 = 25 (+ 30)
        5: 10 + 3 + 4 + 3 + 10 = 30 (+ 35)
        6: 1 + 4 + 5 + 4 + 1 = 15 (+ 40)
        7: 2 + 5 + 6 + 5 + 2 = 20 (+ 35)
        8: 3 + 6 + 7 + 6 + 3 = 25 (+ 20)
        9: 4 + 7 + 8 + 7 + 4 = 30 (+ 25)
        10: 5 + 8 + 9 + 8 + 5 = 35 (+ 30)
         */
    }

    private long[] simulateQuantumGame(int playerOnePosition, int playerOneScore, int playerTwoPosition, int playerTwoScore,
                                     boolean playerOneIsActive, Map<Integer, Integer> universesCreatedByRoll) {
        final long[] winningUniverses = new long[]{0, 0};

        for (Integer roll : universesCreatedByRoll.keySet()) {
            final int universesCreated = universesCreatedByRoll.get(roll);
            final int playerOneSuperposition;
            final int playerOneSuperScore;
            final int playerTwoSuperposition;
            final int playerTwoSuperScore;

            if (playerOneIsActive) {
                playerOneSuperposition = UtMath.wrap(playerOnePosition + roll, 10);
                playerOneSuperScore = playerOneScore + playerOneSuperposition;
                playerTwoSuperposition = playerTwoPosition;
                playerTwoSuperScore = playerTwoScore;
            }
            else {
                playerOneSuperposition = playerOnePosition;
                playerOneSuperScore = playerOneScore;
                playerTwoSuperposition = UtMath.wrap(playerTwoPosition + roll, 10);
                playerTwoSuperScore = playerTwoScore + playerTwoSuperposition;
            }

            if (playerOneSuperScore >= 21) {
                winningUniverses[0] += universesCreated;
            }
            else if (playerTwoSuperScore >= 21) {
                winningUniverses[1] += universesCreated;
            }
            else {
                final long[] subUniverses = simulateQuantumGame(playerOneSuperposition,
                        playerOneSuperScore,
                        playerTwoSuperposition,
                        playerTwoSuperScore,
                        !playerOneIsActive,
                        universesCreatedByRoll);
                winningUniverses[0] += universesCreated * subUniverses[0];
                winningUniverses[1] += universesCreated * subUniverses[1];
            }
        }

        return winningUniverses;
    }
}
