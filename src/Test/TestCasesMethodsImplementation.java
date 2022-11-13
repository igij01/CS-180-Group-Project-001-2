import MessageCore.Conversation;
import MessageCore.IllegalTargetException;
import MessageCore.IllegalUserAccessException;
import UserCore.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
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
public class TestCasesMethodsImplementation {
    private final User NULL = null;
    private static FullBuyer buyer1;
    private static FullSeller seller1;
    private static FullBuyer buyer2;
    private static FullSeller seller2;

    private static Method getUserMethod;

    static {
        try {
            getUserMethod = FullUser.class.getDeclaredMethod("getUser");
            getUserMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static final File serBuy = new File("src/Test/UnitTestTxtFile/test_ser_buy");
    private static final File serSell = new File("src/Test/UnitTestTxtFile/test_ser_sell");
    private static final File serNames = new File("src/Test/UnitTestTxtFile/test_ser_names");
    private static final File serStores = new File("src/Test/UnitTestTxtFile/test_ser_stores");

    static {
        try {
            @SuppressWarnings("unused")
            FullBuyer buyerTest = new FullBuyer("TestBuyer", "test@test.com", "123");
            @SuppressWarnings("unused")
            FullSeller sellerTest = new FullSeller("TestSeller", "test@test.com", "123");
            Method m = PublicInformation.class.getDeclaredMethod("serializeToFiles",
                    File.class, File.class, File.class, File.class);
            m.setAccessible(true);
            m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * set the {@code System.out} to output to a preset {@code ByteArrayOutputStream} instance.
     * <p>
     * <i>this method executes <b>before</b> each test cases</i>
     */
    @Before
    public void setUp() {
        buyer1 = new FullBuyer("Buyer", "sample@email.com", "12345");
        seller1 = new FullSeller("Seller", "sample@email.com", "12345");
        buyer2 = new FullBuyer("Buyer2", "sample@email.com", "12345");
        seller2 = new FullSeller("Seller2", "sample@email.com", "12345");
    }

    /**
     * set the {@code System.out} and {@code System.in} back to their original value
     * <p>
     * <i>this method executes <b>after</b> each test cases</i>
     *
     * @throws Exception Eh, I don't know what exception it throws
     */
    @After
    public void tearDown() throws Exception {
        //dump the whole lists
        Method m = PublicInformation.class.getDeclaredMethod("deconstruct");
        m.setAccessible(true);
        m.invoke(PublicInformation.class);
    }


    @Test(timeout = 1000)
    public void testDataPersistence() throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method m = PublicInformation.class.getDeclaredMethod("initFromFiles",
                File.class, File.class, File.class, File.class);
        m.setAccessible(true);
        m.invoke(PublicInformation.class, serBuy, serSell, serStores, serNames);
        TestCase.assertTrue("persistence test buyer",
                PublicInformation.listOfUsersNames.contains("TestBuyer"));
        TestCase.assertTrue("persistence test seller",
                PublicInformation.listOfUsersNames.contains("TestSeller"));
        if (!(serBuy.delete() && serSell.delete() && serStores.delete() && serNames.delete()))
            throw new RuntimeException("Problem with deleting serialize files used for testing!");
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
    public void testLogout() throws InvalidPasswordException {
        TestCase.assertFalse("test initial user login status",
                buyer1.loginStatus());
        TestCase.assertFalse("test initial user login status",
                seller1.loginStatus());
        PublicInformation.login("Buyer", "12345");
        PublicInformation.login("Seller", "12345");
        buyer1.logout();
        seller1.logout();
        TestCase.assertFalse("test logout Buyer",
                buyer1.loginStatus());
        TestCase.assertFalse("test logout Seller",
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
        TestCase.assertEquals("check whether the store list is empty",
                0, PublicInformation.listOfStores.size());
        seller1.createStore("new Store Opened!");
        TestCase.assertEquals("test whether list of stores has the store",
                1, PublicInformation.listOfStores.size());
    }

    @Test(timeout = 1000, expected = IllegalStoreNameException.class)
    public void testTakenStoreName() throws IllegalStoreNameException {
        seller1.createStore("new Store Opened!");
        seller2.createStore("new Store Opened!");
    }

    @Test(timeout = 1000)
    public void testMessageBuyerToSeller() {
        buyer1.messageSeller(seller1, "message");
    }

    @Test(timeout = 1000, expected = IllegalTargetException.class)
    public void testMessageBuyerToBuyer() {
        buyer1.createMessage(buyer1, "message");
    }

    @Test(timeout = 1000, expected = IllegalUserAccessException.class)
    @SuppressWarnings("unchecked")
    public void testMessagesAccess() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        buyer1.messageSeller(seller1, "message");
        Field f = FullUser.class.getDeclaredField("conversations");
        f.setAccessible(true);
        ArrayList<Conversation> list = (ArrayList<Conversation>) f.get(buyer1);
        list.get(0).toStringConversation((User) getUserMethod.invoke(buyer2));
    }

    @Test(timeout = 1000)
    public void testEditMessage() {
        buyer1.messageSeller(seller1, "message");
        buyer1.editMessage(0, 0, "new message");
        String out = seller1.printConversation(0);
        String contentExcludingTimeStamp = out.substring(0, out.lastIndexOf('\n'));
        TestCase.assertEquals("0\t*Buyer: new message", contentExcludingTimeStamp);
    }

    @Test(timeout = 1000)
    public void testDeleteMessage() {
        buyer1.messageSeller(seller1, "message");
        buyer1.deleteMessage(0, 0);
        TestCase.assertEquals("", buyer1.printConversation(0));
        String out = seller1.printConversation(0);
        String contentExcludingTimeStamp = out.substring(0, out.lastIndexOf('\n'));
        TestCase.assertEquals("0\t*Buyer: message", contentExcludingTimeStamp);
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
