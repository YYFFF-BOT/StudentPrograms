package vendingmachine;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;

class CashHandlerTest {
    @BeforeAll
    public static void initialiseCashHandler(){
        CashHandler handler = new CashHandler();
        HashMap<String,Double> cashTypes = new HashMap<>();
        cashTypes.put("100n", 100.0);
        cashTypes.put("50n", 50.0);
        cashTypes.put("20n", 20.0);
        cashTypes.put("10n", 10.0);
        cashTypes.put("5n", 5.0);
        cashTypes.put("2n", 2.0);
        cashTypes.put("1n", 1.0);
        cashTypes.put("50c", 0.5);
        cashTypes.put("20c", 0.2);
        cashTypes.put("5c", 0.05);
        assertEquals(handler.getCashType(), cashTypes);
    }

    @Test
    public void testIsVaildCashFormat(){
        CashHandler handler = new CashHandler();
        String formatNull = null;
        assertFalse(handler.isVaildCashFormat(formatNull));
        String formatMissingSymbol = "100";
        assertFalse(handler.isVaildCashFormat(formatMissingSymbol));
        String formatOneSymbol = "-";
        assertFalse(handler.isVaildCashFormat(formatOneSymbol));
        String formatNotEnoughLength = "10-5n-";
        assertFalse(handler.isVaildCashFormat(formatNotEnoughLength));
        String formatLengthTooLong = "10-10n-100";
        assertFalse(handler.isVaildCashFormat(formatLengthTooLong));
        String formatNotNumber = "a-10n";
        assertFalse(handler.isVaildCashFormat(formatNotNumber));
        String formatNotAcceptCash = "10-10c";
        assertFalse(handler.isVaildCashFormat(formatNotAcceptCash));

        String formatAccepetOne = "10-10n";
        assertTrue(handler.isVaildCashFormat(formatAccepetOne));
        String formatAccepetTwo = "1000-100n";
        assertTrue(handler.isVaildCashFormat(formatAccepetTwo));
    }

    @Test
    public void testIsVaildCashCommand(){
        CashHandler handler = new CashHandler();
        String formatNull = null;
        assertFalse(handler.isVaildCashCommand(formatNull));
        String formatWrongFormatOne = "10-5n,,100-10n";
        assertFalse(handler.isVaildCashCommand(formatWrongFormatOne));
        String formatWrongFormatTwo = "10-5n-";
        assertFalse(handler.isVaildCashCommand(formatWrongFormatTwo));
        String formatWrongFormatThree = "10-50n,,100-10n,1-5c";
        assertFalse(handler.isVaildCashCommand(formatWrongFormatThree));
        String formatAccepetOne = "10-10n";
        assertTrue(handler.isVaildCashCommand(formatAccepetOne));
        String formatAccepetTwo = "1000-100n";
        assertTrue(handler.isVaildCashCommand(formatAccepetTwo));
    
    }
    
    @Test
    public void testGetCashAmountHashMap(){
        CashHandler handler = new CashHandler();
        HashMap<String,Integer> cashHashMapOne = new HashMap<>();
        cashHashMapOne.put("50c", 1);
        assertTrue(handler.getCashAmountHashMap("1-50c").equals(cashHashMapOne));
        cashHashMapOne.put("100n", 5);
        assertTrue(handler.getCashAmountHashMap("1-50c,5-100n").equals(cashHashMapOne));
        cashHashMapOne.put("100n", 2);
        assertTrue(handler.getCashAmountHashMap("1-50c,2-100n").equals(cashHashMapOne));
        cashHashMapOne.put("10n", 50);
        assertTrue(handler.getCashAmountHashMap("1-50c,2-100n,50-10n").equals(cashHashMapOne));
    }

    @Test
    public void testGetCashAmount(){
        CashHandler handler = new CashHandler();
        HashMap<String,Integer> cashHashMap = new HashMap<>();
        cashHashMap.put("100n", 1);
        Double amount = 100.0;
        assertTrue(handler.getCashAmount(cashHashMap).equals(amount));
        cashHashMap.put("10n", 1);
        amount += 10;
        assertTrue(handler.getCashAmount(cashHashMap).equals(amount));
        cashHashMap.put("5c", 1);
        amount+=0.05;
        assertTrue(handler.getCashAmount(cashHashMap).equals(amount));
        cashHashMap.put("5c", 2);
        amount+=0.05;
        assertTrue(handler.getCashAmount(cashHashMap).equals(amount));
    }

    @Test
    public void testGetChanges(){
        CashHandler handler = new CashHandler();
        HashMap<String,Integer> changeHashMap = new HashMap<>();
        Double amount = 0.0;
        Double paid = 0.0;
        changeHashMap.put("10n", 1);
        amount = 8.0;
        paid = 18.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("10n", 1);
        changeHashMap.put("5n", 1);
        amount = 5.0;
        paid = 20.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("50c", 1);
        amount = 4.5;
        paid = 5.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("1n", 1);
        amount = 4.5;
        paid = 5.5;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("50n", 1);
        changeHashMap.put("20n", 2);
        changeHashMap.put("5n", 1);
        changeHashMap.put("50c", 1);
        amount = 4.5;
        paid = 100.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();
        
        changeHashMap.put("20n", 2);
        changeHashMap.put("2n", 2);
        changeHashMap.put("50c", 1);
        amount = 5.5;
        paid = 50.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("50c", 1);
        changeHashMap.put("2n", 1);
        changeHashMap.put("5n", 1);
        amount = 2.5;
        paid = 10.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("20n", 2);
        changeHashMap.put("50n", 1);
        changeHashMap.put("50c", 1);
        changeHashMap.put("2n", 1);
        changeHashMap.put("5n", 1);
        amount = 2.5;
        paid = 100.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("20n", 2);
        changeHashMap.put("50n", 1);
        changeHashMap.put("50c", 1);
        changeHashMap.put("2n", 2);
        changeHashMap.put("5n", 1);
        amount = 0.5;
        paid = 100.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("10n", 1);
        changeHashMap.put("2n", 1);
        changeHashMap.put("50c", 1);
        amount = 2.5;
        paid = 15.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();

        changeHashMap.put("100n", 9);
        amount = 100.0;
        paid = 1000.0;
        assertTrue(handler.getChanges(amount, paid).equals(changeHashMap));
        changeHashMap.clear();
    }
}
