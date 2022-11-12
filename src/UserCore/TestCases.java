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
public class TestCases {
    private final PrintStream originalOutput = System.out;
    private final InputStream originalInput = System.in;

    private static ArrayList<String> testInputs;
    private static ArrayList<String> testOutputs;

    private String input;
    private String output;

    private final User NULL = null;
    private static final FullBuyer buyer1 = new FullBuyer("Buyer", "sample@email.com", "12345");
    private static final FullSeller seller1 = new FullSeller("Seller", "sample@email.com", "12345");
    private static final FullBuyer buyer2 = new FullBuyer("Buyer2", "sample@email.com", "12345");
    private static final FullSeller seller2 = new FullSeller("Seller2", "sample@email.com", "12345");

    private static final File serBuy = new File("src/UserCore/UnitTestTxtFile/test_ser_buy");
    private static final File serSell = new File("src/UserCore/UnitTestTxtFile/test_ser_sell");
    private static final File serNames = new File("src/UserCore/UnitTestTxtFile/test_ser_names");
    private static final File serStores = new File("src/UserCore/UnitTestTxtFile/test_ser_stores");

    static {
        try {
            FullBuyer buyerTest = new FullBuyer("TestBuyer", "test@test.com", "123");
            FullSeller sellerTest = new FullSeller("TestSeller", "test@test.com", "123");
            Method m = PublicInformation.class.getDeclaredMethod("serializeToFiles",
                    File.class, File.class, File.class, File.class);
            m.setAccessible(true);
            m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Test(timeout = 1000)
    public void testDatePersistence() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method m = PublicInformation.class.getDeclaredMethod("initFromFiles",
                File.class, File.class, File.class, File.class);
        m.setAccessible(true);
        m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
        TestCase.assertTrue("persistence test buyer",
                PublicInformation.listOfUsersNames.contains("TestBuyer"));
        TestCase.assertTrue("persistence test seller",
                PublicInformation.listOfUsersNames.contains("TestSeller"));
        serBuy.delete();
        serSell.delete();
        serStores.delete();
        serNames.delete();
    }

    @Test(timeout = 1000)
    public void testLoginSuccess() throws InvalidPasswordException {
        TestCase.assertFalse("test initial user login status",
                buyer1.loginStatus());
        TestCase.assertFalse("test initial user login status",
                seller1.loginStatus());
        PublicInformation.login("Buyer", "12345");
        PublicInformation.login("Seller", "12345");
        TestCase.assertTrue("test initial user login status",
                buyer1.loginStatus());
        TestCase.assertTrue("test initial user login status",
                seller1.loginStatus());
    }

    @Test(timeout = 1000)
    public void testLogout() {
        PublicInformation.logout(buyer1);
        PublicInformation.logout(seller1);
        TestCase.assertFalse("test initial user login status",
                buyer1.loginStatus());
        TestCase.assertFalse("test initial user login status",
                seller1.loginStatus());
    }

    @Test(timeout = 1000, expected = InvalidPasswordException.class)
    public void testWrongPassword() throws InvalidPasswordException {
        PublicInformation.login("Buyer", "Wrong Password");
    }

    @Test(timeout = 1000, expected = IllegalUserNameException.class)
    public void testWrongUsername() throws IllegalUserNameException, InvalidPasswordException {
        PublicInformation.login("User", "he doesn't exists!");
    }

    @Test(timeout = 1000)
    public void testCreateStore() throws IllegalStoreNameException {
        TestCase.assertEquals("check wether the store list is empty",
                0, PublicInformation.listOfStores.size());
        seller1.createStore("new Store Opened!");
        TestCase.assertEquals("test whether listofstores has the store",
                1, PublicInformation.listOfStores.size());
    }

    @Test(timeout = 1000, expected = IllegalStoreNameException.class)
    public void testTakenStoreName() throws IllegalStoreNameException {
        seller1.createStore("new Store Opened!");
        seller2.createStore("new Store Opened!");
    }


    /**
     * Test buyer can message seller with toString check
     */
    @Test(timeout = 1000)
    public void testMessageToString() {
        buyer1.messageSeller(seller1, "message");
        String out = buyer1.printConversationTitles();

        String normalOut = out.substring(0, out.indexOf('\n'));
        String timeStamp = out.substring(out.indexOf('\n') + 1);

        TestCase.assertEquals("Buyer: Message", normalOut);
        TestCase.assertTrue("time stamp format test",
                timeStamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"));
    }
//
//    /**
//     * Test seller can message buyer with toFileString check
//     */
//    @Test(timeout = 1000)
//    public void testMessageToStringTwo() throws Exception {
//        Message m = new Message(seller1, buyer1, new File("src/UserCore/UnitTestTxtFile/message.txt"));
//        String out = m.toString();
//        String normalOut = out.substring(0, out.lastIndexOf('\n'));
//        String timeStamp = out.substring(out.lastIndexOf('\n') + 1);
//
//        TestCase.assertEquals("Seller: Hello!\n" +
//                "My name is xxx.", normalOut);
//        TestCase.assertTrue("time stamp format test",
//                timeStamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"));
//    }
//
//    /**
//     * test that seller message seller will throw exception
//     */
//    @Test(timeout = 1000, expected = IllegalTargetException.class)
//    public void testSellerToSeller() throws Exception {
//        Message m = new Message(seller1, seller2, "oops");
//    }
//
//    /**
//     * test that buyer message buyer will throw exception
//     */
//    @Test(timeout = 1000, expected = IllegalTargetException.class)
//    public void testBuyerToBuyer() throws Exception {
//        Message m = new Message(buyer1, buyer2, "oops");
//    }
//
//    /**
//     * test that user cannot message a user that doesn't exist
//     */
//    @Test(timeout = 1000, expected = NullPointerException.class)
//    public void testNullUser() throws Exception {
//        Message m = new Message(buyer1, NULL, "oops");
//    }
//
//    /**
//     * access to specific message by participant
//     */
//    @Test(timeout = 1000)
//    public void testAccessMessage() throws Exception {
//        Message m = new Message(buyer1, seller1, "aha");
//        Method getMessage = Message.class.getDeclaredMethod("getMessage", User.class);
//        getMessage.setAccessible(true);
//        String content = (String) getMessage.invoke(m, buyer1);
//        content = (String) getMessage.invoke(m, seller1);
//    }
//
//    /**
//     * test another user that's not a participant accessing the message
//     */
//    @Test(timeout = 1000, expected = IllegalUserAccessException.class)
//    public void testInvalidAccessMessage() throws Throwable {
//        Message m = new Message(buyer1, seller1, "aha");
//        Method getMessage = Message.class.getDeclaredMethod("getMessage", User.class);
//        getMessage.setAccessible(true);
//        try {
//            String content = (String) getMessage.invoke(m, seller2);
//        } catch (InvocationTargetException e) {
//            throw e.getCause();
//        }
//    }


}
