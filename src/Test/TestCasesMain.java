import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class TestCasesMain {
    private final PrintStream originalOutput = System.out;
    private final InputStream originalInput = System.in;

    private static ArrayList<String> testInputs;
    private static ArrayList<String> testOutputs;

    private String input;
    private String output;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayInputStream testIn;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayOutputStream testOut;


    /**
     * set the {@code System.out} to output to a preset {@code ByteArrayOutputStream} instance.
     *
     * <i>this method executes <b>before</b> each test cases</i>
     *
     * @throws Exception Eh, I don't know what exception it throws
     */
    @Before
    public void setUp() throws Exception {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    /**
     * set the {@code System.out} and {@code System.in} back to their original value
     *
     * <i>this method executes <b>after</b> each test cases</i>
     *
     * @throws Exception Eh, I don't know what exception it throws
     */
    @After
    public void tearDown() throws Exception {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }

    /**
     * get the output from the program to be tested
     *
     * @return the output from the program to be tested
     */
    private String getOutput() {
        return testOut.toString().replaceAll("\r\n", "\n");
    }

    /**
     * send the input into the program as {@code ByteArrayInputStream}
     *
     * @param str the input String
     */
    private void sendInput(String str) {
        testIn = new ByteArrayInputStream(str.getBytes());
        System.setIn(testIn);
    }
}
