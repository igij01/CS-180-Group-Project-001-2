package UserCore;

import MessageCore.IllegalTargetException;
import MessageCore.IllegalUserAccessException;
import MessageCore.Message;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * TestCases
 * <p>
 * Run unit test based on the expected output
 *
 * @author Yulin Lin, 001
 * @version 11/5/2022
 */
public class TestCasesMessageCore {
    private final PrintStream originalOutput = System.out;
    private final InputStream originalInput = System.in;

    private static ArrayList<String> testInputs;
    private static ArrayList<String> testOutputs;

    private String input;
    private String output;

    private final User NULL = null;
    private static final Buyer buyer1 = new Buyer("Buyer", "sample@email.com", "12345");
    private static final Seller seller1 = new Seller("Seller", "sample@email.com", "12345");
    private static final Buyer buyer2 = new Buyer("Buyer2", "sample@email.com", "12345");
    private static final Seller seller2 = new Seller("Seller2", "sample@email.com", "12345");

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


    /**
     * Test buyer can message seller with toString check
     */
    @Test(timeout = 1000)
    public void testMessageToString() {
        Message m = new Message(buyer1, seller1, "Message");
        String out = m.toString();
        String normalOut = out.substring(0, out.indexOf('\n'));
        String timeStamp = out.substring(out.indexOf('\n') + 1);

        TestCase.assertEquals("Buyer: Message", normalOut);
        TestCase.assertTrue("time stamp format test",
                timeStamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"));
    }

    /**
     * Test seller can message buyer with toFileString check
     */
    @Test(timeout = 1000)
    public void testMessageToStringTwo() throws Exception {
        Message m = new Message(seller1, buyer1, new File("src/UserCore/UnitTestTxtFile/message.txt"));
        String out = m.toString();
        String normalOut = out.substring(0, out.lastIndexOf('\n'));
        String timeStamp = out.substring(out.lastIndexOf('\n') + 1);

        TestCase.assertEquals("Seller: Hello!\n" +
                "My name is xxx.", normalOut);
        TestCase.assertTrue("time stamp format test",
                timeStamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"));
    }

    /**
     * test that seller message seller will throw exception
     */
    @Test(timeout = 1000, expected = IllegalTargetException.class)
    public void testSellerToSeller() throws Exception {
        Message m = new Message(seller1, seller2, "oops");
    }

    /**
     * test that buyer message buyer will throw exception
     */
    @Test(timeout = 1000, expected = IllegalTargetException.class)
    public void testBuyerToBuyer() throws Exception {
        Message m = new Message(buyer1, buyer2, "oops");
    }

    /**
     * test that user cannot message a user that doesn't exist
     */
    @Test(timeout = 1000, expected = NullPointerException.class)
    public void testNullUser() throws Exception {
        Message m = new Message(buyer1, NULL, "oops");
    }

    /**
     * access to specific message by participant
     */
    @Test(timeout = 1000)
    public void testAccessMessage() throws Exception {
        Message m = new Message(buyer1, seller1, "aha");
        Method getMessage = Message.class.getDeclaredMethod("getMessage", User.class);
        getMessage.setAccessible(true);
        String content = (String) getMessage.invoke(m, buyer1);
        content = (String) getMessage.invoke(m, seller1);
    }

    /**
     * test another user that's not a participant accessing the message
     */
    @Test(timeout = 1000, expected = IllegalUserAccessException.class)
    public void testInvalidAccessMessage() throws Throwable {
        Message m = new Message(buyer1, seller1, "aha");
        Method getMessage = Message.class.getDeclaredMethod("getMessage", User.class);
        getMessage.setAccessible(true);
        try {
            String content = (String) getMessage.invoke(m, seller2);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }


}
