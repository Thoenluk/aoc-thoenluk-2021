package thoenluk.aoc2021.challenge12.cave;

import java.util.HashSet;
import java.util.Set;

public class Cave {

    private final String name;
    private final boolean small;
    private final boolean start;
    private final boolean end;
    private final Set<Cave> connectedCaves;

    public Cave(String name) {
        this.name = name;
        this.small = Character.isLowerCase(name.charAt(0));
        this.start = name.equals("start");
        this.end = name.equals("end");
        this.connectedCaves = new HashSet<>();
    }

    public boolean isSmall() {
        return small;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }

    public void addConnection(Cave other) {
        connectedCaves.add(other);
    }

    public Set<Cave> getConnectedCaves() {
        return connectedCaves;
    }
}
