package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// Functionalities integrated into (i.e. moved to) buyLogic

/*
 * How to use this handler
 * 1. From the user input we get a command "1-50n,2-20n,3-10n,4-5n"
 *    Which are the cash type and the number
 * 2. Check whether the command is vaild by using isVaildCashCommand
 * 3. Converte this command to HashMap by using getCashAmountHashMap
 * 4. Converte this HashMap to value in Double by using getCashAmount
 * 5. Calculate the changes need to be given by using getChanges
 *    Here we will get a HashMap with the changes need to be given
 *    This is easy to print the details of chagnes
 * 6. Converte this HashMap to value in Double by using getCashAmount, we get the total chagnes
 */
public class CashHandler {
    // Avaliable cash format [0-9]*+ - +100n|50n|20n|10n|5n|2n|1n|50c|20c|5c
    // Avaliable cash amount 100.0,50.0,20.0,10.0,5.0,2.0,1.0,0.5,0.2,0.05
    private HashMap<String,Double> cashTypeDouble = new HashMap<>();
    private HashMap<Integer,String> cashTypeInt = new HashMap<>();
    // Use cents to avoid Double's accuracy problem
    private int[] cashAmount = {10000,5000,2000,1000,500,200,100,50,20,5};

    public CashHandler() {
        cashTypeDouble.put("100n", 100.0);
        cashTypeDouble.put("50n", 50.0);
        cashTypeDouble.put("20n", 20.0);
        cashTypeDouble.put("10n", 10.0);
        cashTypeDouble.put("5n", 5.0);
        cashTypeDouble.put("2n", 2.0);
        cashTypeDouble.put("1n", 1.0);
        cashTypeDouble.put("50c", 0.5);
        cashTypeDouble.put("20c", 0.2);
        cashTypeDouble.put("5c", 0.05);

        cashTypeInt.put(10000,"100n");
        cashTypeInt.put(5000,"50n");
        cashTypeInt.put(2000,"20n");
        cashTypeInt.put(1000,"10n");
        cashTypeInt.put(500,"5n");
        cashTypeInt.put(200,"2n");
        cashTypeInt.put(100,"1n");
        cashTypeInt.put(50,"50c");
        cashTypeInt.put(20,"20c");
        cashTypeInt.put(5,"5c");
    }

    public HashMap<String,Double> getCashType() {
        return this.cashTypeDouble;
    }

    // Check whether the cash format is vaild
    public boolean isVaildCashFormat(String format) {
        if (format == null) {
            System.out.println("Invalid cash format: Null");
            return false;
        }
        if (!format.contains("-")) {
            System.out.println("Invalid cash format: Missing '-'");
            return false;
        }
        if (format.split("-").length!=2) {
            System.out.println("Invalid cash format: Length incorrect");
            return false;
        }
        if (format.charAt(format.length()-1)=='-') {
            System.out.println("Invalid cash format: End with '-' ");
            return false;
        }

        String formatNumber = format.split("-")[0];
        String formatCashType = format.split("-")[1];

        if (!formatNumber.matches("[0-9]*")) {
            System.out.println("Invalid cash format: Number of cash incorrect");
            return false;
        }
        if (!formatCashType.matches("100n|50n|20n|10n|5n|2n|1n|50c|20c|5c")) {
            System.out.println("Invalid cash format: Invalid cash type ");
            return false;
        }

        return true;
    }

    // Check whether the input command is vaild
    public boolean isVaildCashCommand(String command) {
        if (command == null) {
            System.out.println("Invalid cash format: Null");
            return false;
        }
        if (command.charAt(command.length()-1)==',') {
            System.out.println("Invalid format");
            return false;
        }

        int i = 0;
        while (i < command.length()) {
            if (command.charAt(i) == ',' && command.charAt(i+1) == ',') {
                System.out.println("Invalid cash format: Null");
                return false;
            }
            i++;
        }

        String[] commands = command.split(",");
        for (String format:commands) {
            if (!isVaildCashFormat(format)) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String,Integer> getCashAmountHashMap(String command) {
        HashMap<String,Integer> cashHashMap = new HashMap<>();
        String[] commands = command.split(",");

        for (String format : commands) {
            String formatNumber = format.split("-")[0];
            Integer cashNumber = Integer.parseInt(formatNumber);
            String formatCashType = format.split("-")[1];
            cashHashMap.put(formatCashType, cashNumber);
        }

        return cashHashMap;
    }

    // Calculate the amount of cash
    // Must check whether is vaild before using this function
    public Double getCashAmount(HashMap<String,Integer> cashHashMap) {
        Double amount = (double) 0;
        Iterator<String> iter =  cashHashMap.keySet().iterator();

        while (iter.hasNext()) {
            String key = iter.next();
            Integer number = cashHashMap.get(key);
            amount += cashTypeDouble.get(key) * number;
        }

        return amount;
    }

    // The return type of this is a HashMap with number of different cash type
    // If
    public HashMap<String,Integer> getChanges(Double amount,Double paid) {
        int amountInt = (int)Math.round(amount*100);
        int paidInt = (int)Math.round(paid*100);
        ArrayList<Integer> changes = new ArrayList<>();
        HashMap<String,Integer> returnChanges = new HashMap<>();
        int change = paidInt - amountInt;
        boolean done = false;

        // This part is to calculate how many changes should be given
        while (!done) {
            if (change < 5) {
                done = true;
            }
            for (int cash : cashAmount) {
                if (cash <= change) {
                    change -= cash;
                    changes.add(cash);
                    break;
                }
            }
        }

        if (changes.size() == 1) {
            returnChanges.put(cashTypeInt.get(changes.get(0)), 1);
        }
        else if (changes.size() == 2) {
            if (changes.get(0) == changes.get(1)) {
                returnChanges.put(cashTypeInt.get(changes.get(0)), 2);
            } else {
                returnChanges.put(cashTypeInt.get(changes.get(0)), 1);
                returnChanges.put(cashTypeInt.get(changes.get(1)), 1);
            }
        }
        else {
            int current = 0;
            int next = 1;
            while (next < changes.size()) {
                if (!changes.get(current).equals(changes.get(next))) {
                    returnChanges.put(cashTypeInt.get(changes.get(current)), next-current);
                    current = next;
                    next++;

                }
                else if (changes.get(current).equals(changes.get(next))) {
                    next++;
                }

                if (next == changes.size()-1 && changes.get(next).equals(changes.get(next-1))) {
                    returnChanges.put(cashTypeInt.get(changes.get(next)), next-current+1);

                }
                else if (next == changes.size()-1 && !changes.get(next).equals(changes.get(next-1))) {
                    returnChanges.put(cashTypeInt.get(changes.get(next)), 1);
                }
            }
        }
        // This part is to convert changes to a hash map with entity cashType:number

        return returnChanges;
    }

    public void printChangesString(HashMap<String,Integer> returnChanges) {
        Iterator<String> iter =  returnChanges.keySet().iterator();
        Boolean first = true;

        while (iter.hasNext()) {
            if (!first) {
                System.out.print(',');
            }
            first = false;
            String key = iter.next();
            Integer number = returnChanges.get(key);
            System.out.print(number);
            System.out.print('-');
            System.out.print(key);
        }
    }
}
