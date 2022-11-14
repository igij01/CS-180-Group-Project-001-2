import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * FileManipulate
 * <p>
 * a class that contains static methods to separate output and input from files used by Main Unit Test
 * <p>
 * Note: This class was written in September in one of my personal project
 *
 * @author Yulin Lin, 001
 * @version 1.0.0, 9/30/2022
 */
public class FileManipulate {

    /**
     * Separate input from arbitrary amount of <b>.txt</b> files
     *
     * @param files files to be taken in
     * @return {@code ArrayList<String>} with each entry contains all the input from a file
     * (separated by {@code System.lineSeparator()})
     */
    public static ArrayList<String> convertInput(File... files) {
        Scanner[] readers = convertFileToScanners(files);
        ArrayList<String> results = new ArrayList<>();
        for (Scanner scan : readers) {
            assert scan != null;
            if (isScannerEmpty(scan)) continue;
            StringBuilder result = new StringBuilder();
            scan.useDelimiter("\\n+");
            while (scan.hasNext()) {
                String line = scan.next().strip();
                if (line.startsWith("[")) {
                    String newline = line.replace("[", "").replace("]", "");
                    assert !newline.equals(line.replace("[", ""));
                    if (newline.equals(line.replace("[", ""))) continue;
                    result.append(newline).append(System.lineSeparator());
                }
            }
            scan.close();
            results.add(result.toString());
        }
        return results;
    }

    /**
     * Separate output from arbitrary amount of <b>.txt</b> files
     *
     * @param files files to be taken in
     * @return {@code ArrayList<String>} with each entry contains all the output from a file
     * (separated by \n)
     */
    public static ArrayList<String> convertOutput(File... files) {
        Scanner[] readers = convertFileToScanners(files);
        ArrayList<String> result = new ArrayList<>();
        for (Scanner scan : readers) {
            assert scan != null;
            if (isScannerEmpty(scan)) continue;
            StringBuilder resultFile = new StringBuilder();
            scan.useDelimiter("\\n");
            while (scan.hasNext()) {
                String line = scan.next().strip();
                if (line.startsWith("[")) continue;
                resultFile.append(line).append("\n");
            }
            scan.close();
            result.add(resultFile.toString());
        }
        return result;
    }

    /**
     * create a {@code Scanner} instance for all files passed in
     *
     * @param files file that need to be read
     * @return an array of {@code Scanner} containing all Scanner instance
     * for all the files
     */
    private static Scanner[] convertFileToScanners(File[] files) {
        assert files != null;
        Scanner[] readers = new Scanner[files.length];
        try {
            for (int i = 0; i < files.length; i++)
                if (files[i].exists())
                    readers[i] = new Scanner(files[i]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return readers;
    }

    /**
     * Check to see if {@code Scanner} instance is empty or
     * contains only blank characters
     *
     * @param scan {@code Scanner} instance to check if it's empty
     * @return true if it's empty
     */
    private static boolean isScannerEmpty(Scanner scan) {
        Pattern oldDelimiter = scan.delimiter();
        scan.useDelimiter("\\s{2,}|\\n+");
        boolean empty = !scan.hasNext();
        scan.useDelimiter(oldDelimiter);
        return empty;
    }
}
