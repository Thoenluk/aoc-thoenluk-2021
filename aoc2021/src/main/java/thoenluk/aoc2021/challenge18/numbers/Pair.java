package thoenluk.aoc2021.challenge18.numbers;

import thoenluk.aoc2021.ut.Ut;

import java.util.Queue;

public class Pair implements SnailfishNumber {

    //---- Fields

    private SnailfishNumber left;
    private SnailfishNumber right;
    private Pair parent;


    //---- Constructors

    public Pair(Queue<Character> encodedData, Pair parent) {
        left = getNextNumberInEncodedData(encodedData);

        encodedData.remove(); // Always ,

        right = getNextNumberInEncodedData(encodedData);

        encodedData.remove(); // Always ]

        this.parent = parent;
    }

    public Pair(SnailfishNumber left, SnailfishNumber right, Pair parent) {
        this.left = left;
        left.setParent(this);

        this.right = right;
        right.setParent(this);

        this.parent = parent;
    }


    //---- Methods

    @Override
    public SnailfishNumber copy() {
        return new Pair(left.copy(), right.copy(), parent);
    }

    private SnailfishNumber getNextNumberInEncodedData(Queue<Character> encodedData) {
        Character initial = encodedData.remove();
        if (initial == '[') {
            return new Pair(encodedData, this);
        }
        else {
            return new RegularNumber(Ut.cachedGetNumericValue(initial), this);
        }
    }

    public Pair add(Pair other) {
        return new Pair(this, other, parent);
    }

    public Pair reduce() {
        SnailfishNumber indicator;
        do {
            indicator = checkForExploding(1);

            if (indicator == null) {
                indicator = checkForSplitting();
            }
        } while (indicator != null);
        return this;
    }

    @Override
    public SnailfishNumber checkForExploding(int nestingDepth) {
        if (nestingDepth > 4) {
            parent.explodeLeft((RegularNumber) left, this);
            parent.explodeRight((RegularNumber) right, this);
            return new RegularNumber(0, parent);
        }

        SnailfishNumber newLeft = left.checkForExploding(nestingDepth + 1);

        if (newLeft != null) {
            left = newLeft;
            return this;
        }

        SnailfishNumber newRight = right.checkForExploding(nestingDepth + 1);

        if (newRight != null) {
            right = newRight;
            return this;
        }

        return null;
    }

    private void explodeLeft(RegularNumber valueToAdd, Pair source) {
        if (left != source) {
            left.addToRightmostRegularNumber(valueToAdd);
        }
        else if (parent != null) {
            parent.explodeLeft(valueToAdd, this);
        }
    }

    @Override
    public void addToRightmostRegularNumber(RegularNumber value) {
        right.addToRightmostRegularNumber(value);
    }

    private void explodeRight(RegularNumber valueToAdd, Pair source) {
        if (right != source) {
            right.addToLeftmostRegularNumber(valueToAdd);
        }
        else if (parent != null) {
            parent.explodeRight(valueToAdd, this);
        }
    }

    @Override
    public void addToLeftmostRegularNumber(RegularNumber value) {
        left.addToLeftmostRegularNumber(value);
    }

    @Override
    public Pair checkForSplitting() {
        Pair newLeft = left.checkForSplitting();

        if (newLeft != null) {
            left = newLeft;
            return this;
        }

        Pair newRight = right.checkForSplitting();

        if (newRight != null) {
            right = newRight;
            return this;
        }

        return null;
    }

    @Override
    public long getMagnitude() {
        return 3 * left.getMagnitude() + 2 * right.getMagnitude();
    }

    @Override
    public void setParent(Pair parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return '[' + left.toString() + ',' + right.toString() + ']';
    }
}
