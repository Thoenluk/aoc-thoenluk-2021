package thoenluk.aoc2021.challenge18.numbers;

public interface SnailfishNumber {
    SnailfishNumber checkForExploding(int nestingDepth);
    void addToRightmostRegularNumber(RegularNumber value);
    void addToLeftmostRegularNumber(RegularNumber value);
    Pair checkForSplitting();
    long getMagnitude();
    SnailfishNumber copy();
    void setParent(Pair parent);
}
