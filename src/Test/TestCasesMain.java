import UserCore.FullBuyer;
import UserCore.FullSeller;
import UserCore.PublicInformation;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * TestCases
 * <p>
 * Run unit test based on the expected output from txt files
 * <p>
 * Note: This class is adapted from one of my personal project
 *
 * @author Yulin, Lin
 * @version 1.0.0, 11/14/2022
 */
@RunWith(Parameterized.class)
public class TestCasesMain extends TestCase {
    private final PrintStream originalOutput = System.out;
    private final InputStream originalInput = System.in;

    private static ArrayList<String> testInputs;
    private static ArrayList<String> testOutputs;

    private static final File serBuy = new File("src/Test/UnitTestTxtFile/test_ser_buy");
    private static final File serSell = new File("src/Test/UnitTestTxtFile/test_ser_sell");
    private static final File serNames = new File("src/Test/UnitTestTxtFile/test_ser_names");
    private static final File serStores = new File("src/Test/UnitTestTxtFile/test_ser_stores");

    private String input;
    private String output;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayInputStream testIn;

    @SuppressWarnings("FieldCanBeLocal")
    private ByteArrayOutputStream testOut;

    /*
     * initializing unit test for user config
     */
    static {
        System.out.println("Welcome to Unit Test Generator v1.0.0");

//        System.out.println("Please put in the names of the file you put the test cases in(separated by comma)");
        String[] files = ("SampleTestOne.txt, SampleTestTwo.txt, SampleTestThree.txt, SampleTestFour.txt, " +
                "SampleTestFive.txt").split(",\\s*");

        ArrayList<File> txtFiles = new ArrayList<>();
        for (String file : files) {
            file = "src//Test//UnitTestTxtFile//" + file;
            File txtFile = new File(file);
            if (!txtFile.exists()) {
                System.out.println(txtFile.getPath() + " doesn't exists. It will be ignored");
            } else {
                txtFiles.add(txtFile);
                System.out.println(txtFile.getPath() + " found! Adding it to test cases");
            }
        }
        testOutputs = FileManipulate.convertOutput(txtFiles.toArray(new File[0]));
        testInputs = FileManipulate.convertInput(txtFiles.toArray(new File[0]));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        FullBuyer buyer = new FullBuyer("Buyer", "sample@email.com", "1");
        FullSeller seller1 = new FullSeller("Seller", "sample@email.com", "11");
        FullBuyer buyer2 = new FullBuyer("Buyer2", "sample@email.com", "2");
        FullSeller seller2 = new FullSeller("Seller2", "sample@email.com", "22");
        Method m = PublicInformation.class.getDeclaredMethod("serializeToFiles",
                File.class, File.class, File.class, File.class);
        m.setAccessible(true);
        m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
    }

    /**
     * set the {@code System.out} to output to a preset {@code ByteArrayOutputStream} instance.
     *
     * <i>this method executes <b>before</b> each test cases</i>
     *
     * @throws Exception Eh, I don't know what exception it throws
     */
    @Before
    @Override
    public void setUp() throws Exception {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        Method m = PublicInformation.class.getDeclaredMethod("initFromFiles",
                File.class, File.class, File.class, File.class);
        m.setAccessible(true);
        m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
    }

    /**
     * set the {@code System.out} and {@code System.in} back to their original value
     *
     * <i>this method executes <b>after</b> each test cases</i>
     *
     * @throws Exception Eh, I don't know what exception it throws
     */
    @After
    @Override
    public void tearDown() throws Exception {
        System.setIn(originalInput);
        System.setOut(originalOutput);
        Method m = PublicInformation.class.getDeclaredMethod("serializeToFiles",
                File.class, File.class, File.class, File.class);
        m.setAccessible(true);
        m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
    }

    @AfterClass
    public static void deleteFiles() throws Exception {
        serBuy.delete();
        serSell.delete();
        serNames.delete();
        serStores.delete();
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

    /**
     * constructor used for {@code Parameterized.Parameters} which allows
     * the same test cases to run for different inputs and outputs.
     * <b>Do not instantiate this class using this constructor</b>
     *
     * @param expectedInput  expected input string
     * @param expectedOutput expected output string
     */
    public TestCasesMain(String expectedInput, String expectedOutput) {
        input = expectedInput;
        output = expectedOutput;
    }

    /**
     * instantiate the test cases with input and output
     *
     * @return {@code Collection} of inputs and outputs
     */
    @Parameterized.Parameters
    public static Collection tests() {
        assert testOutputs.size() == testInputs.size();

        Object[][] io = new Object[testOutputs.size()][2];
        for (int i = 0; i < io.length; i++) {
            io[i][0] = testInputs.get(i);
            io[i][1] = testOutputs.get(i);
        }

        return Arrays.asList(io);
    }

    /**
     * Test the main method with the expected inputs and outputs
     */
    @Test(timeout = 1000)
    public void testMain() {
        sendInput(input);
        Main.main(new String[]{"Debug"});

        String actualOutput = getOutput();

        assertEquals(output.trim(), actualOutput.trim());
    }
}
