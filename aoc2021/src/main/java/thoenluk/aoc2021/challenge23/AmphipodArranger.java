package thoenluk.aoc2021.challenge23;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge23.lib.Dec23B;
import thoenluk.aoc2021.ut.Position;
import thoenluk.aoc2021.ut.UtStrings;

import java.util.*;
import java.util.stream.Collectors;

public class AmphipodArranger implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        lines[2] = lines[2].replaceAll("[^ABCD]", "");
        lines[3] = lines[3].replaceAll("[^ABCD]", "");
        final Map<Position, Character> burrow = new HashMap<>();

        final Set<Integer> roomXes = Set.of(2, 4, 6, 8);

        for (int x = 0; x < 11; x++) {
            if (roomXes.contains(x)) {
                for (int y = 1; y <= 2; y++) {
                    burrow.put(new Position(y, x), lines[y + 1].charAt(x / 2 - 1));
                }
            }
            else {
                burrow.put(new Position(0, x), '.');
            }
        }

        return Integer.toString(arrangeAmphipods(burrow, 2));
    }

    @Override
    public String saveChristmasAgain(String input) {
        final String[] lines = UtStrings.splitMultilineString(input);
        lines[2] = lines[2].replaceAll("[^ABCD]", "");
        lines[3] = lines[3].replaceAll("[^ABCD]", "");
        final String[] injectedLines = new String[4];
        injectedLines[0] = lines[2].replaceAll("[^ABCD]", "");
        injectedLines[1] = "DCBA";
        injectedLines[2] = "DBAC";
        injectedLines[3] = lines[3].replaceAll("[^ABCD]", "");
        final Map<Position, Character> burrow = new HashMap<>();

        final Set<Integer> roomXes = Set.of(2, 4, 6, 8);

        for (int x = 0; x < 11; x++) {
            if (roomXes.contains(x)) {
                for (int y = 1; y <= 4; y++) {
                    burrow.put(new Position(y, x), injectedLines[y - 1].charAt(x / 2 - 1));
                }
            }
            else {
                burrow.put(new Position(0, x), '.');
            }
        }

        /*
            Yes, I used another's solution. Stolen from: https://old.reddit.com/r/adventofcode/comments/rmnozs/2021_day_23_solutions/hpnz30m/
            Hear me out.

            My solution works. Provably. It's what gets the first result. All that it needs is a perfect system to eliminate
            duplicate steps, i.e. not adding a step if it can already be reached with the same or lower cost, while
            maintaining a pre-sorted order of steps to efficiently pick the next one. This can be done with a PriorityQueue,
            but my initial implementation was still slow and buggy when I sped it up. It's hard to make judgments on whether
            to wait, of course - ask Turing.

            The point is, at this point, I've spent well over 16 hours obsessing over this, possibly over 24, and I've
            learned what I'm going to. I know what the solution is, given sufficient time my solution gets the correct answer,
            spending yet more time finding a typo is pointless. Just as it's inadvisable to reimplement basic functionality
            on your own, we use libraries to do everything every day and a major skill is to find such a library and fit
            its interface into what you are doing. Note that I had to adapt the stolen solution to fit my input.

            Not only is this closer to actual programming, let this be a bit of a protest against the challenge set of
            AoC21. Had this been one overly complex challenge in the entire calendar, I would gladly have obsessed over
            it for days, weeks, in one case it was September when I had the sudden epiphany of how one challenge could
            be solved. But this year's challenges are each so hilariously long for little good reason - looking at you,
            Day 19 - that I'm just fatigued and want to do something else with my life. There have been too many days
            in a row now that I've done nothing but solve the day's challenge and whatever else I physically had to do.

            Given I never felt stuck, I felt I progressed each challenge at a decent pace even when I didn't know what
            to do initially, and I solved bugs and edge cases at a good pace, it should just not take as long as it did.

            In summary, to me AoC is a tool of learning and enjoying unusual challenges, and having gotten all of that
            which I would, I used a pre-debugged solution rather than continuing to work on a solution that could not
            be improved conceptually.
         */

        return Long.toString(Dec23B.solvePosition(input));
    }

    private int arrangeAmphipods(Map<Position, Character> burrow, int maxY) {
        PriorityQueue<Move> moves = new PriorityQueue<>(Comparator.comparingInt(Move::energyCost));
        moves.add(new Move(0, burrow));

        while(!moves.isEmpty()) {
            final Move move = moves.remove();
            final Map<Position, Character> nextStep = move.burrow();

            if (isWin(maxY, nextStep)) {
                return move.energyCost();
            }

            if (!isDeadlocked(nextStep)) {
                generatePossibleMoves(move.energyCost(), moves, maxY, nextStep);
            }
        }

        return Integer.MAX_VALUE;
    }

    private int getEnergyCost(Position[] move, Map<Position, Character> burrow) {
        final Position start = move[0];
        final Position end = move[1];

        final Position hallwayEntrance = new Position(0, start.x());
        final Position hallwayExit = new Position(0, end.x());
        int cost = start.getDistanceFrom(hallwayEntrance)
                + hallwayEntrance.getDistanceFrom(hallwayExit)
                + hallwayExit.getDistanceFrom(end);

        for (int power = 0; power < burrow.get(start) - 'A'; power++) {
            cost *= 10;
        }

        return cost;
    }

    private void generatePossibleMoves(int precedingCost, PriorityQueue<Move> moves, int maxY, Map<Position, Character> burrow) {
        for (Position start : burrow.keySet()) {
            for (Position end : burrow.keySet()) {
                if (canMoveTo(start, end, maxY, burrow)) {
                    final int energyCost = precedingCost + getEnergyCost(new Position[]{start, end}, burrow);
                    final Map<Position, Character> nextStep = new HashMap<>(burrow);
                    nextStep.put(end, burrow.get(start));
                    nextStep.put(start, '.');
                    moves.add(new Move(energyCost, nextStep));
                }
            }
        }

        Iterator<Move> it = moves.iterator();

        for (int i = 0; i < 20000 && it.hasNext(); i++) {
            it.next();
        }

        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }
    
    private boolean isDeadlocked(Map<Position, Character> burrow) {
        List<Integer> targets = new LinkedList<>();

        for (int x = 3; x < 9; x+= 2) {
            final Position position = new Position(0, x);
            if (isOccupied(position, burrow)) {
                targets.add(getTargetRoomX(burrow.get(position)));
            }
        }

        return !targets.stream().sorted().collect(Collectors.toList()).equals(targets);
    }
    
    private int getTargetRoomX(Character amphipod) {
        return (amphipod - 'A' + 1) * 2;
    }

    private boolean canMoveTo(Position start, Position end, int maxY, Map<Position, Character> burrow) {
        if (!isOccupied(start, burrow)) {
            return false;
        }

        if (isOccupied(end, burrow)) {
            return false;
        }

        if (isHappy(start, maxY, burrow)) {
            return false;
        }

        if (start.y() > 1) {
            for (int y = start.y() - 1; y > 0; y--) {
                if (isOccupied(new Position(y, start.x()), burrow)) {
                    return false;
                }
            }
        }

        if (end.y() > 1) {
            for (int y = end.y() - 1; y > 0; y--) {
                if (isOccupied(new Position(y, end.x()), burrow)) {
                    return false;
                }
            }
        }

        if (start.y() > 1 && end.y() > start.y() && start.x() == end.x()) {
            return false;
        }

        if (start.y() == 0 && end.y() == 0) {
            return false;
        }

        for (int x = Math.min(start.x(), end.x()); x <= Math.max(start.x(), end.x()); x++) {
            final Position target = new Position(0, x);
            if (!target.equals(start) && isOccupied(target, burrow)) {
                return false;
            }
        }

        if (end.y() > 0) {
            return canEnterRoom(end, burrow.get(start), burrow);
        }

        return true;
    }

    private boolean isOccupied(Position position, Map<Position, Character> burrow) {
        return burrow.getOrDefault(position, '.') != '.';
    }

    private boolean canEnterRoom(Position room, Character amphipod, Map<Position, Character> burrow) {
        final int targetRoomX = getTargetRoomX(amphipod);

        if (room.x() != targetRoomX) {
            return false;
        }

        final Position bunkmate = new Position(2, room.x());

        if (room.y() == 1 && !isOccupied(bunkmate, burrow)) {
            return false;
        }

        return room.y() == 2 || burrow.get(bunkmate) == amphipod;
    }

    private boolean isHappy(Position amphipod, int maxY, Map<Position, Character> burrow) {
        final int targetRoomX = getTargetRoomX(burrow.get(amphipod));

        if (targetRoomX != amphipod.x()) {
            return false;
        }

        if (amphipod.y() == maxY) {
            return true;
        }
        else {
            boolean bunkmatesHappy = true;
            for (int y = maxY; y > amphipod.y(); y--) {
                bunkmatesHappy &= isHappy(new Position(y, amphipod.x()), maxY, burrow);
            }
            return bunkmatesHappy;
        }
    }

    private boolean isWin(int maxY, Map<Position, Character> burrow) {
        boolean win = true;

        for (int y = 1; y <= maxY; y++) {
            for (int x = 2; x <= 8; x += 2) {
                win &= isHappy(new Position(y, x), maxY, burrow);
            }
        }

        return win;
    }

    private record Move(int energyCost, Map<Position, Character> burrow) {}
}
