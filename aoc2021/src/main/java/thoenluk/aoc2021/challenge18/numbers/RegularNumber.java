package thoenluk.aoc2021.challenge18.numbers;

public class RegularNumber implements SnailfishNumber {

    private int value;
    private Pair parent;

    public RegularNumber(int value, Pair parent) {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public SnailfishNumber checkForExploding(int nestingDepth) {
        return null;
    }

    @Override
    public void addToRightmostRegularNumber(RegularNumber other) {
        this.value += other.value;
    }

    @Override
    public void addToLeftmostRegularNumber(RegularNumber other) {
        this.value += other.value;
    }

    @Override
    public Pair checkForSplitting() {
        if (value >= 10) {
            RegularNumber newLeft = new RegularNumber(value / 2, null);
            RegularNumber newRight = new RegularNumber((value + 1) / 2, null);
            return new Pair(newLeft, newRight, parent);
        }

        return null;
    }

    @Override
    public long getMagnitude() {
        return value;
    }

    @Override
    public SnailfishNumber copy() {
        return new RegularNumber(value, parent);
    }

    @Override
    public void setParent(Pair parent) {
        this.parent = parent;
    }

    @Override
    public String toString() { return Integer.toString(value); }
}
