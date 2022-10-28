package vendingmachine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataCashStorage {
    private HashMap<String,Integer> cashStorage;
    private final Integer maxStorage = 500000;
    private final HashMap<String,Integer> cashStorageInt;
    public DataCashStorage() {
        this.cashStorage = new HashMap<String,Integer>();
        cashStorage.put("$100", 0);
        cashStorage.put("$50", 0);
        cashStorage.put("$20", 0);
        cashStorage.put("$10", 0);
        cashStorage.put("$5", 0);
        cashStorage.put("$2", 0);
        cashStorage.put("$1", 0);
        cashStorage.put("50c", 0);
        cashStorage.put("20c", 0);
        cashStorage.put("10c", 0);
        cashStorage.put("5c", 0);

        this.cashStorageInt = new HashMap<>();
        cashStorageInt.put("$100", 10000);
        cashStorageInt.put("$50", 5000);
        cashStorageInt.put("$20", 2000);
        cashStorageInt.put("$10", 1000);
        cashStorageInt.put("$5", 500);
        cashStorageInt.put("$2", 200);
        cashStorageInt.put("$1", 100);
        cashStorageInt.put("50c", 50);
        cashStorageInt.put("20c", 20);
        cashStorageInt.put("10c", 10);
        cashStorageInt.put("5c", 5);
    }

    void reSet(Integer initNumber) {
        for(String key:cashStorage.keySet()){
            cashStorage.put(key, initNumber);
        }
    }

    public HashMap<String,Integer> checkStorage(){
        return this.cashStorage;
    }
    Integer checkRemaining(String denomination) {
        return cashStorage.get(denomination);
    }

    Integer checkCapicity(String denomination){
        return maxStorage - cashStorage.get(denomination);
    }
    // The format of denomination is the format of cash type
    public Boolean addDenomination(String denomination, Integer number) {
        if (!(denomination.matches(UserInputFormat.Dollors)||denomination.matches(UserInputFormat.Cents))) {
            System.out.printf("Add fail : Denomination %s is not recognized\n",denomination);
            return false;
        }
        if(number == 0){
            return true;
        }
        if(number < 0){
            System.out.println("Can not add negetive number of cash");
        }
        Integer inStock = checkRemaining(denomination);
        if (number + inStock > maxStorage) {
            System.out.printf("Add fail : No enough capacity!\nPlease contact the maneger\n");
            return false;
        }

        cashStorage.put(denomination,inStock+number);
        return true;
    }

    public Boolean getDenomination(String denomination, Integer number) {
        if (!(denomination.matches(UserInputFormat.Dollors)||denomination.matches(UserInputFormat.Cents))) {
            System.out.printf("Add fail : This denomination is not recognized\n");
            return false;
        }
        if(number == 0){
            return true;
        }
        if(number < 0){
            System.out.println("Can not add negetive number of cash");
        }
        Integer inStock = checkRemaining(denomination);
        // Get successfully
        if (number <= inStock) {
            cashStorage.put(denomination, inStock-number);
            return true;
        } else {
            System.out.printf("Return fail : No enough cash!\n");
            return false;
        }
    }

    public String getStorageReport(){
        String report = "The details of cash storage:\n";
        report += String.format("$100 : %s \n",cashStorage.get("$100"));
        report += String.format("$50  : %s \n",cashStorage.get("$50"));
        report += String.format("$20  : %s \n",cashStorage.get("$20"));
        report += String.format("$10  : %s \n",cashStorage.get("$10"));
        report += String.format("$5   : %s \n",cashStorage.get("$5"));
        report += String.format("$2   : %s \n",cashStorage.get("$2"));
        report += String.format("$1   : %s \n",cashStorage.get("$1"));
        report += String.format("50c  : %s \n",cashStorage.get("50c"));
        report += String.format("10c  : %s \n",cashStorage.get("10c"));
        report += String.format("5c   : %s \n",cashStorage.get("5c"));
        return report;
    }

    public Double getStorageAmountDouble(){
        Double amount = 0.0;
        for(String key:cashStorage.keySet()){
            amount += cashStorage.get(key) * cashStorageInt.get(key);
        }
        amount /= 100;
        
        return amount;
    }


}
