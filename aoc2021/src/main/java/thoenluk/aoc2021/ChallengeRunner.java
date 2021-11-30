package thoenluk.aoc2021;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static thoenluk.aoc2021.Ut.*;

/**
 *
 * @author Lukas Thöni lukas.thoeni@gmx.ch
 */
public class ChallengeRunner {
    
    public static void main(String[] args) throws Exception {
        println("Scanning for challenge folders...");
        final File[] challengeFolders = getChallengeFolders();

        printChallengeFolderIndices(challengeFolders);


        final int selectedChallenge = getSelectedChallengeFromUser(challengeFolders.length);

        testAndRunChristmasSaver(challengeFolders[selectedChallenge], selectedChallenge);
    }

    private static File[] getChallengeFolders() {
        final File currentFolder = new File("aoc2021");
        return currentFolder.listFiles(pathname -> pathname.isDirectory() && pathname.getName().matches("\\d+.*"));
    }

    private static void printChallengeFolderIndices(File[] challengeFolders) {
        println("Found " + challengeFolders.length + " challenges: ");
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < challengeFolders.length; i++) {
            output.append(i).append(":\t").append(challengeFolders[i].getName().replaceAll("\\d|\\s", "")).append("\n");
        }
        output.append("\n").append("Now choose one.");
        println(output.toString());
    }

    private static int getSelectedChallengeFromUser(int highestPossibleChallenge) {
        final Scanner userInput = new Scanner(System.in);
        int selectedChallenge = -1;
        while (selectedChallenge < 0) {
            selectedChallenge = userInput.nextInt();

            if (selectedChallenge < 0 || highestPossibleChallenge < selectedChallenge) {
                println("Only and exactly one of the above numbers shalt thou choose.");
                selectedChallenge = -1;
            }
        }
        return selectedChallenge;
    }

    private static void testAndRunChristmasSaver(File challengeFolder, int selectedChallenge) throws Exception {
        final ChristmasSaver christmasSaver = getChristmasSaverForChallenge(selectedChallenge);
        final File[] testInputs = challengeFolder.listFiles((File dir, String fileName) -> fileName.startsWith("test_input"));
        final File[] testOutputs = challengeFolder.listFiles((File dir, String fileName) -> fileName.startsWith("test_output"));
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
            final String actualOutput = christmasSaver.saveChristmas(testInputString);
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

        final File[] actualInputFiles = challengeFolder.listFiles((dir, name) -> name.equals("input.txt"));
        if (actualInputFiles == null) throw new AssertionError();
        if (actualInputFiles.length != 1) throw new AssertionError();
        println(christmasSaver.saveChristmas(Files.readString(actualInputFiles[0].toPath())));
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
}
