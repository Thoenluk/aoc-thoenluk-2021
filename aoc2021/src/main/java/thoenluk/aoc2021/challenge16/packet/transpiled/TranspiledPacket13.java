package thoenluk.aoc2021.challenge16.packet.transpiled;

public class TranspiledPacket13 {
    private static long min(long... args) {
        long min = Long.MAX_VALUE;
        for (long arg : args) {
            min = Math.min(min, arg);
        }
        return min;
    }
    
    private static long max(long... args) {
        long max = Long.MIN_VALUE;
        for (long arg : args) {
            max = Math.max(max, arg);
        }
        return max;
    }
    
    public static long getValue() {
        return (
            1L
            +
            2L
        );
    }
}