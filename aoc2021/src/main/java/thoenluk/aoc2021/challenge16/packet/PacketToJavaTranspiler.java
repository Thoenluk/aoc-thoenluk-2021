package thoenluk.aoc2021.challenge16.packet;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PacketToJavaTranspiler {

    //---- Statics

    public static final PacketToJavaTranspiler INSTANCE = new PacketToJavaTranspiler();

    private static final String SINGLE_INDENT = "    ";

    //---- Fields

    private StringBuilder program;
    private int indentation;
    private int programID;


    //---- Private constructor; Use INSTANCE to obtain instance

    private PacketToJavaTranspiler() {
        programID = 0;
    }

    public PacketToJavaTranspiler init() {
        program = new StringBuilder();
        indentation = 0;
        programID++;

        append("package thoenluk.aoc2021.challenge16.packet.transpiled;")
        .newLine().newLine();
        append("public class TranspiledPacket").append(programID).append(" {").indent()
            .newLine().append("private static long min(long... args) {").indent()
                .newLine().append("long min = Long.MAX_VALUE;")
                .newLine().append("for (long arg : args) {").indent()
                    .newLine().append("min = Math.min(min, arg);").outdent()
                .newLine().append("}")
                .newLine().append("return min;").outdent()
            .newLine().append("}")
            .newLine()
            .newLine().append("private static long max(long... args) {").indent()
                .newLine().append("long max = Long.MIN_VALUE;")
                .newLine().append("for (long arg : args) {").indent()
                    .newLine().append("max = Math.max(max, arg);").outdent()
                .newLine().append("}")
                .newLine().append("return max;").outdent()
            .newLine().append("}")
            .newLine()
            .newLine().append("public static long getValue() {").indent()
                .newLine().append("return ");

        return this;
    }

    public PacketToJavaTranspiler finish() {
                append(";").outdent()
            .newLine().append("}").outdent()
        .newLine().append("}");
        return this;
    }

    public PacketToJavaTranspiler append(Object toAppend) {
        program.append(toAppend);
        return this;
    }

    public PacketToJavaTranspiler newLine() {
        program.append("\n");
        program.append(SINGLE_INDENT.repeat(Math.max(0, indentation)));
        return this;
    }

    public PacketToJavaTranspiler indent() {
        indentation++;
        return this;
    }

    public PacketToJavaTranspiler outdent() {
        if (indentation <= 0) throw new AssertionError("Can't outdent while this transpiler is already at no indentation!");
        indentation--;
        return this;
    }

    public PacketToJavaTranspiler writeToFile() {
        Path path = Paths.get("aoc2021/src/main/java/thoenluk/aoc2021/challenge16/packet/transpiled", "TranspiledPacket" + programID + ".java");
        try {
            Files.write(path, program.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Couldn't write program: " + e);
        }
        return this;
    }

    @Override
    public String toString() {
        return program.toString();
    }
}
