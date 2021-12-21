package thoenluk.aoc2021.challenge4;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge4.bingo.BingoBoard;
import thoenluk.aoc2021.ut.UtParsing;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.*;

public class GameMaster implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        List<String> lines = new LinkedList<>(Arrays.asList(UtStrings.splitMultilineString(input)));
        List<Integer> drawnNumbers = parseDrawnNumbers(lines.remove(0));
        lines.remove(0);

        Set<BingoBoard> boards = setupBoards(lines);

        for (int drawnNumber : drawnNumbers) {
            for (BingoBoard board : boards) {
                board.markNumber(drawnNumber);
                if (board.isWin()) {
                    return Integer.toString(drawnNumber * board.getScore());
                }
            }
        }

        return null;
    }

    @Override
    public String saveChristmasAgain(String input) {
        List<String> lines = new LinkedList<>(Arrays.asList(UtStrings.splitMultilineString(input)));
        List<Integer> drawnNumbers = parseDrawnNumbers(lines.remove(0));
        lines.remove(0);

        Set<BingoBoard> boards = setupBoards(lines);
        Iterator<BingoBoard> it;
        BingoBoard board;

        for (int drawnNumber : drawnNumbers) {
            it = boards.iterator();
            while (it.hasNext()) {
                board = it.next();
                board.markNumber(drawnNumber);
                if (board.isWin()) {
                    if (boards.size() == 1) {
                        return Integer.toString(drawnNumber * board.getScore());
                    }
                    it.remove();
                }
            }
        }

        return null;
    }

    private List<Integer> parseDrawnNumbers(String drawnNumbersLine) {
        List<Integer> drawnNumbers = new LinkedList<>();
        String[] drawnNumberTokens = drawnNumbersLine.split(",");
        for (String token : drawnNumberTokens) {
            drawnNumbers.add(UtParsing.cachedParseInt(token));
        }
        return drawnNumbers;
    }

    private Set<BingoBoard> setupBoards(List<String> inputLines) {
        HashSet<BingoBoard> boards = new HashSet<>();
        List<String> currentBoard = new LinkedList<>();

        for (String line : inputLines) {
            if (line.isBlank()) {
                boards.add(new BingoBoard(currentBoard.toArray(new String[0])));
                currentBoard.clear();
            }
            else {
                currentBoard.add(line);
            }
        }

        if (!currentBoard.isEmpty()) {
            boards.add(new BingoBoard(currentBoard.toArray(new String[0])));
        }
        return boards;
    }
}
