package thoenluk.aoc2021.challenge16.packet;

import thoenluk.aoc2021.ut.Ut;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Packet {

    //---- Statics

    private static final int LITERAL_VALUE = 4;
    private static final int TOTAL_LENGTH_IN_BITS = 0;
    public static final int MINIMUM_PACKET_LENGTH = 11;


    //---- Fields

    private final int version;
    private final int typeID;
    private final List<Packet> subPackets;
    private final long value;


    //---- Constructor

    public Packet(Queue<Character> encodedData) {
        subPackets = new LinkedList<>();

        version = convertBitsToNumber(encodedData, 3);
        typeID = convertBitsToNumber(encodedData, 3);


        if (typeID != LITERAL_VALUE) {
            if (convertBitsToNumber(encodedData, 1) == TOTAL_LENGTH_IN_BITS) {
                final Queue<Character> subPacketData = new LinkedList<>();
                final int totalLength = convertBitsToNumber(encodedData, 15);

                for (int i = 0; i < totalLength; i++) {
                    subPacketData.add(encodedData.remove());
                }

                while (subPacketData.size() >= MINIMUM_PACKET_LENGTH) {
                    subPackets.add(new Packet(subPacketData));
                }
            } else {
                final int totalPackets = convertBitsToNumber(encodedData, 11);

                for (int i = 0; i < totalPackets; i++) {
                    subPackets.add(new Packet(encodedData));
                }
            }
        }

        value = switch (typeID) {
            case 0: yield sum();
            case 1: yield product();
            case 2: yield min();
            case 3: yield max();
            case 4: yield readLiteralValue(encodedData);
            case 5: yield greaterThan();
            case 6: yield lessThan();
            case 7: yield equal();
            default:
                throw new IllegalStateException("Unexpected value: " + typeID);
        };
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

        return Ut.cachedParseInt(stringRepresentation.toString(), 2);
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

        return Ut.cachedParseLong(stringRepresentation.toString(), 2);
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
}
