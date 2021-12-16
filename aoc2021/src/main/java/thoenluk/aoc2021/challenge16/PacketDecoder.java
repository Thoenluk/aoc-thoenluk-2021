package thoenluk.aoc2021.challenge16;

import thoenluk.aoc2021.ChristmasSaver;
import thoenluk.aoc2021.challenge16.packet.Packet;
import thoenluk.aoc2021.ut.Ut;

import java.util.*;

import static thoenluk.aoc2021.challenge16.packet.Packet.MINIMUM_PACKET_LENGTH;

public class PacketDecoder implements ChristmasSaver {
    @Override
    public String saveChristmas(String input) {
        return Integer.toString(parseInput(input).getVersionSum());
    }

    @Override
    public String saveChristmasAgain(String input) {
        return Long.toString(parseInput(input).getValue());
    }

    private Packet parseInput(String input) {
        final LinkedList<Character> encodedData = new LinkedList<>();
        for (Character token : input.toCharArray()) {
            final int intValue = Ut.cachedGetNumericValue(token);
            final String binary = String.format("%4s", Integer.toBinaryString(intValue)).replace(' ', '0');

            Collections.addAll(encodedData,
                    binary.chars()
                            .mapToObj(c -> (char) c)
                            .toArray(Character[]::new));
        }

        return new Packet(encodedData);
    }
}
