package thoenluk.aoc2021.challenge16.packet;

import thoenluk.aoc2021.ut.UtParsing;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static thoenluk.aoc2021.ut.UtStrings.println;

public class Packet {

    //---- Statics


    private static final int SUM = 0;
    private static final int PRODUCT = 1;
    private static final int MIN = 2;
    private static final int MAX = 3;
    private static final int LITERAL_VALUE = 4;
    private static final int GREATER_THAN = 5;
    private static final int LESS_THAN = 6;
    private static final int EQUAL = 7;
    private static final int TOTAL_LENGTH_IN_BITS = 0;
    public static final int MINIMUM_PACKET_LENGTH = 11;


    //---- Fields

    private final int version;
    private final List<Packet> subPackets;
    private final long value;
    private final int typeID;


    //---- Constructor

    public Packet(Queue<Character> encodedData, PacketToJavaTranspiler transpiler) {
        subPackets = new LinkedList<>();

        version = convertBitsToNumber(encodedData, 3);
        typeID = convertBitsToNumber(encodedData, 3);

        if (typeID != LITERAL_VALUE) {

            appendArgumentListOpener(transpiler);

            if (convertBitsToNumber(encodedData, 1) == TOTAL_LENGTH_IN_BITS) {
                final Queue<Character> subPacketData = new LinkedList<>();
                final int totalLength = convertBitsToNumber(encodedData, 15);

                for (int i = 0; i < totalLength; i++) {
                    subPacketData.add(encodedData.remove());
                }

                while (subPacketData.size() >= MINIMUM_PACKET_LENGTH) {
                    subPackets.add(new Packet(subPacketData, transpiler));

                    appendArgumentSeparator(transpiler, subPacketData.isEmpty());
                }

            } else {
                final int totalPackets = convertBitsToNumber(encodedData, 11);

                for (int i = 0; i < totalPackets; i++) {
                    subPackets.add(new Packet(encodedData, transpiler));

                    appendArgumentSeparator(transpiler, i == totalPackets - 1);
                }
            }

            appendArgumentListTerminator(transpiler);
        }

        value = switch (typeID) {
            case SUM: yield sum();
            case PRODUCT: yield product();
            case MIN: yield min();
            case MAX: yield max();
            case LITERAL_VALUE: yield readLiteralValue(encodedData);
            case GREATER_THAN: yield greaterThan();
            case LESS_THAN: yield lessThan();
            case EQUAL: yield equal();
            default:
                throw new IllegalStateException("Unexpected value: " + typeID);
        };

        if (typeID == LITERAL_VALUE) {
            transpiler.append(value + "L");
        }
    }


    //---- Methods

    public int getVersionSum() {
        int sum = version;

        for (Packet subPacket : subPackets) {
            sum += subPacket.getVersionSum();
        }

        return sum;
    }

    private int convertBitsToNumber(Queue<Character> encodedData, int length) {
        final StringBuilder stringRepresentation = new StringBuilder();

        gatherBits(encodedData, length, stringRepresentation);

        return UtParsing.cachedParseInt(stringRepresentation.toString(), 2);
    }

    private void gatherBits(Queue<Character> encodedData, int length, StringBuilder target) {
        for (int i = 0; i < length; i++) {
            target.append(encodedData.remove());
        }
    }

    public long getValue() {
        return value;
    }

    private long sum() {
        return subPackets.stream()
                .mapToLong(Packet::getValue)
                .sum();
    }

    private long product() {
        return subPackets.stream()
                .mapToLong(Packet::getValue)
                .reduce(1, (product, value) -> product * value);
    }

    private long min() {
        return subPackets.stream()
                .mapToLong(Packet::getValue)
                .min()
                .orElseThrow();
    }

    private long max() {
        return subPackets.stream()
                .mapToLong(Packet::getValue)
                .max()
                .orElseThrow();
    }

    private long readLiteralValue(Queue<Character> encodedData) {
        final StringBuilder stringRepresentation = new StringBuilder();

        while (encodedData.remove().equals('1')) {
            gatherBits(encodedData, 4, stringRepresentation);
        }

        gatherBits(encodedData, 4, stringRepresentation);

        return UtParsing.cachedParseLong(stringRepresentation.toString(), 2);
    }

    private long greaterThan() {
        return subPackets.get(0).getValue() > subPackets.get(1).getValue() ? 1 : 0;
    }

    private long lessThan() {
        return subPackets.get(0).getValue() < subPackets.get(1).getValue() ? 1 : 0;
    }

    private long equal() {
        return subPackets.get(0).getValue() == subPackets.get(1).getValue() ? 1 : 0;
    }

    private void appendArgumentListOpener(PacketToJavaTranspiler transpiler) {
        if (typeID == MIN) {
            transpiler.append("min");
        }
        else if (typeID == MAX) {
            transpiler.append("max");
        }

        transpiler.append("(")
                .indent()
                .newLine();
    }

    private void appendArgumentSeparator(PacketToJavaTranspiler transpiler, boolean isLastItem) {
        if (isLastItem) {
            transpiler.outdent();
        }
        else {
            switch (typeID) {
                case SUM -> transpiler.newLine().append("+");
                case PRODUCT -> transpiler.newLine().append("*");
                case MIN, MAX -> transpiler.append(",");
                case GREATER_THAN -> transpiler.newLine().append(">");
                case LESS_THAN -> transpiler.newLine().append("<");
                case EQUAL -> transpiler.newLine().append("==");
            }
        }

        transpiler.newLine();
    }

    private void appendArgumentListTerminator(PacketToJavaTranspiler transpiler) {
        if (typeID == GREATER_THAN || typeID == LESS_THAN || typeID == EQUAL) {
            transpiler.append("? 1 : 0");
        }

        transpiler.append(")");
    }
}
