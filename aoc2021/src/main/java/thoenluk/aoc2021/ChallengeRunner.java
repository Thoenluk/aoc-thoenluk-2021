package thoenluk.aoc2021;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import static thoenluk.aoc2021.Ut.*;

/**
 *
 * @author Lukas Thöni lukas.thoeni@gmx.ch
 */
public class ChallengeRunner {


    private final static Scanner USER_INPUT = new Scanner(System.in);
    private final static String FIRST_CHALLENGE_SUFFIX = "1";
    private final static String SECOND_CHALLENGE_SUFFIX = "2";

    public static void main(String[] args) throws Exception {
        println("Scanning for challenge folders...");
        final File[] challengeFolders = getChallengeFolders();

        printChallengeFolderIndices(challengeFolders);


        final int selectedChallenge = getSelectedChallengeFromUser(challengeFolders.length);

        testAndRunChristmasSaver(challengeFolders[selectedChallenge], selectedChallenge);
    }

    private static File[] getChallengeFolders() {
        final File currentFolder = new File("aoc2021");
        return currentFolder.listFiles(pathname -> pathname.isDirectory() && pathname.getName().matches("\\d+ .+"));
    }

    private static void printChallengeFolderIndices(File[] challengeFolders) {
        println("Found " + challengeFolders.length + " challenges: ");
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < challengeFolders.length; i++) {
            output.append(i).append(":\t").append(challengeFolders[i].getName().replaceAll("\\d\\s+", "")).append("\n");
        }
        output.append("\n").append("Now choose one.");
        println(output.toString());
    }

    private static int getSelectedChallengeFromUser(int highestPossibleChallenge) {
        int selectedChallenge = -1;
        while (selectedChallenge < 0) {
            selectedChallenge = USER_INPUT.nextInt();

            if (selectedChallenge < 0 || highestPossibleChallenge < selectedChallenge) {
                println("Only and exactly one of the above numbers shalt thou choose.");
                selectedChallenge = -1;
            }
        }
        return selectedChallenge;
    }

    private static void testAndRunChristmasSaver(File challengeFolder, int selectedChallenge) throws Exception {
        final ChristmasSaver christmasSaver = getChristmasSaverForChallenge(selectedChallenge);

        testChristmasSaver(challengeFolder, christmasSaver::saveChristmas, FIRST_CHALLENGE_SUFFIX);

        final File[] actualInputFiles = challengeFolder.listFiles((dir, name) -> name.equals("input.txt"));
        if (actualInputFiles == null) throw new AssertionError();
        if (actualInputFiles.length != 1) throw new AssertionError();
        String input = Files.readString(actualInputFiles[0].toPath());
        println("Determined the result for the first challenge is:");
        long millisBeforeStart = System.currentTimeMillis();
        println(christmasSaver.saveChristmas(input));
        println("And did it in " + (System.currentTimeMillis() - millisBeforeStart) + "ms!");

        println("What fun that was. Running second challenge...");

        testChristmasSaver(challengeFolder, christmasSaver::saveChristmasAgain, SECOND_CHALLENGE_SUFFIX);
        println("Determined the result for the second challenge is:");
        millisBeforeStart = System.currentTimeMillis();
        println(christmasSaver.saveChristmasAgain(input));
        println("And did it in " + (System.currentTimeMillis() - millisBeforeStart) + "ms!");
    }

    // I do not fear what this method does; I fear what kind of further automation I'll think up next year.
    private static ChristmasSaver getChristmasSaverForChallenge(int challenge) throws Exception {
        final File challengeClassFolder = new File("aoc2021\\src\\main\\java\\thoenluk\\aoc2021\\challenge" + challenge);
        if ((!challengeClassFolder.isDirectory())) throw new AssertionError();
        final File[] potentialChallengeClasses = challengeClassFolder.listFiles(file -> file.isFile() && file.getName().endsWith(".java"));
        if ((Objects.requireNonNull(potentialChallengeClasses).length != 1)) throw new AssertionError();
        final String challengeClassPath = potentialChallengeClasses[0].getPath();
        
        final String challengeClassPackageName = 
                challengeClassPath.substring(22, challengeClassPath.length() - 5)
                        .replaceAll("\\\\", ".");
        final Class<? extends ChristmasSaver> challengeClass;
        try {
            challengeClass = Class.forName(challengeClassPackageName).asSubclass(ChristmasSaver.class);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Could not find Christmas saver for this challenge :<", e);
        }
        try {
            return challengeClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new Exception("Could not construct Christmas saver for this challenge :<", e);
        }
    }

    private static void testChristmasSaver(File challengeFolder, UnaryOperator<String> savingMethod, String challengeSuffix) throws IOException {
        final String inputPrefix = "test" + challengeSuffix + "_input";
        final String outputPrefix = "test" + challengeSuffix + "_output";
        final File[] testInputs = challengeFolder.listFiles((File dir, String fileName) -> fileName.startsWith(inputPrefix));
        final File[] testOutputs = challengeFolder.listFiles((File dir, String fileName) -> fileName.startsWith(outputPrefix));
        if (testInputs == null) throw new AssertionError();
        if (testOutputs == null) throw new AssertionError();
        if ((testInputs.length != testOutputs.length)) throw new AssertionError();

        Arrays.sort(testInputs);
        Arrays.sort(testOutputs);
        for (int i = 0; i < testInputs.length; i++) {
            final File testInput = testInputs[i];
            final File testOutput = testOutputs[i];

            print("Running test " + testInput.getName() + "... ");
            final String testInputString = Files.readString(testInput.toPath());
            final String testOutputString = Files.readString(testOutput.toPath());
            final String actualOutput = savingMethod.apply(testInputString);
            if (!actualOutput.equals(testOutputString)) {
                StringBuilder message = new StringBuilder();
                message.append("Failed test ").append(testInput.getName()).append("!\n")
                        .append("Input was:\n")
                        .append(testInputString).append("\n\n")
                        .append("And expected output was:\n")
                        .append(testOutputString).append("\n\n")
                        .append("But actual output was:\n")
                        .append(actualOutput);
                println(message);
                throw new AssertionError();
            }
            println("Matched " + testOutput.getName());
        }
    }
}
