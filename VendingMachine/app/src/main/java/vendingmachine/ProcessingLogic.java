/*
 * ProcessingLogic.java in Processing Logic Layer
 */
package vendingmachine;

import java.util.HashMap;

import org.checkerframework.checker.units.qual.h;

import vendingmachine.DataManager.Account;

import java.util.regex.Pattern;

public class ProcessingLogic {
    public DataManager DM;
    private String user; // Current user
    private String role; // Current user's role
    private DataCashStorage cashStorage;
    private HashMap<String,Integer> tempCashInputRecord;
    private HashMap<String,Integer> tempCashReturnRecord;
    public ProcessingLogic() {
        this.DM = new DataManager();
        this.cashStorage = new DataCashStorage();
        this.tempCashInputRecord = new HashMap<>();
        this.tempCashReturnRecord = new HashMap<>();
        this.user = "Anonymous"; // Default user
        this.role = "buyer"; // Default role
        cashStorage.reSet(10);
        resetInputRecord();
        resetChangeRecord();
    }
    public void resetChangeRecord(){
        tempCashReturnRecord.clear();
    }
    public void resetInputRecord(){
        tempCashInputRecord.clear();
    }

    public String getUser() {
        return this.user;
    }

    public String getRole() {
        return this.role;
    }

    // Return true for valid command; false for invalid command
    public boolean registerLogic(String[] split) {
        if (split.length < 4) {
            System.out.println("Not enough arguments: register\n");
            System.out.printf("Correct register command: %s\n", UserInputFormat.register);
            System.out.println("Please type 'help verbose' for detailed command formats\n");
            return false;
        } else if (!split[1].matches(UserInputFormat.Username)) {
            System.out.println("Invalid Username format\n");
            System.out.printf("Correct register command: %s\n", UserInputFormat.register);
            System.out.println("Please type 'help verbose' for detailed command formats\n");
            return true;
        } else if (!split[2].matches(UserInputFormat.Password)) {
            System.out.println("Invalid Password format\n");
            System.out.printf("Correct register command: %s\n", UserInputFormat.register);
            System.out.println("Please type 'help verbose' for detailed command formats\n");
            return true;
        } else if (!split[3].matches(UserInputFormat.Role)) {
            System.out.println("Invalid Role format\n");
            System.out.printf("Correct register command: %s\n", UserInputFormat.register);
            System.out.println("Please type 'help verbose' for detailed command formats\n");
            return true;
        }

        if (this.DM.getAccounts().containsKey(split[1])) {
            System.out.println("Username already exixsts: Please use another username\n");
            return true;
        }

        this.DM.addAccount(split[1], split[2], split[3]);
        // Save to DB here

        System.out.printf("Registration successful: Welcome %s!\n\n", split[1]);

        return true;
    }

    // Return true for valid command; false for invalid command
    public boolean loginLogic(String[] split) {
        if (split.length != 3) { 
            System.out.println("Invalid login details\n");
            System.out.printf("Correct login command: %s\n", UserInputFormat.login);
            System.out.println();
            return false;
        } else if (!this.DM.getAccounts().containsKey(split[1])) {
            System.out.println("Invalid login details\n");
            System.out.printf("Correct login command: %s\n", UserInputFormat.login);
            System.out.println();
            return false;
        } else if (!this.DM.getAccounts().get(split[1]).password.equals(split[2])) {
            System.out.println("Invalid login details\n");
            System.out.printf("Correct login command: %s\n", UserInputFormat.login);
            System.out.println();
            return false;
        }

        Account acc = this.DM.getAccounts().get(split[1]);
        this.user = acc.getUsername();
        this.role = acc.getRole();
        
        System.out.printf("Successful login: Welcome %s!\n\n", this.user);

        return true;
    }

    // Return true for valid command; false for invalid command
    public boolean logoutLogic(String[] split) {
        if (split.length != 1) {
            System.out.println("Invalid logout format\n");
            System.out.printf("Correct logout command: %s\n\n", UserInputFormat.logout);
            return false;
        }
        
        user = "Anonymous";
        role = "buyer";

        System.out.println("Successfully logged out!\n");
        return true;
    }

    // Return true for valid command; false for invalid command
    public boolean listLogic(String[] split) {
        if (!this.role.matches(UserInputFormat.Role)) {
            System.out.println("Invalid current role\n");
            return false;

        } else if (this.role.equals("buyer")) {
            if (split.length < 2) {
                System.out.println("Not enough arguments: list\n");
                System.out.printf("Correct list_products command: %s\n", UserInputFormat.list_products);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            } else if (!split[1].equals("products")) {
                System.out.println("Invalid argument: list\n");
                System.out.printf("Correct list_products command: %s\n", UserInputFormat.list_products);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            }
            return this.DM.printProducts();

        } else if (this.role.equals("seller")) {

            return false;

        } else if (this.role.equals("cashier")) {

            return false;

        } else if (this.role.equals("owner")) {

            return false;

        } else {
            System.out.println("Invalid current role\n");
            return false;
        }
    }

    // Return true for valid command; false for invalid command
    public boolean buyLogic(String[] split) {
        if (split.length < 4) {
            System.out.println("Not enough arguments: buy\n");
            System.out.printf("Correct cash buy command: %s\n", UserInputFormat.buy_cash);
            System.out.printf("Correct card buy command: %s\n", UserInputFormat.buy_card);
            System.out.println("Please type 'help verbose' for detailed command formats\n");
            return false;
        } else if (split[split.length - 1].equals("cancel")) {
            System.out.println("Cancelled your transaction!\n");
            return true;
        }

        double priceSum = 0;
        boolean cash = false;
        int index = 1;
        while (true) {
            if (split[index].equals("cash") && index >= 2) {
                cash = true;
                index++;
                break;
            } else if (split[index].equals("card") && index >= 2) {
                cash = false;
                index++;
                break;
            }

            if (index == split.length - 1) {
                System.out.println("Payment method not given: Please use cash or card\n");
                System.out.printf("Correct cash buy command: %s\n", UserInputFormat.buy_cash);
                System.out.printf("Correct card buy command: %s\n", UserInputFormat.buy_card);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            } else if (!split[index].matches(UserInputFormat.ProductId + "\\*" + UserInputFormat.Quantity)) {
                System.out.printf("Invalid format: %s\n\n", split[index]);
                System.out.printf("Correct cash buy command: %s\n", UserInputFormat.buy_cash);
                System.out.printf("Correct card buy command: %s\n", UserInputFormat.buy_card);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            } else if (!this.DM.getProducts().containsKey(split[index].split("\\*")[0])) {
                System.out.printf("No such product: %s\n\n", split[index]);
                return false;
            } else if (this.DM.getProducts().get(split[index].split("\\*")[0]).quantity < Integer.valueOf(split[index].split("\\*")[1])) {
                System.out.printf("Not enough stock: %s\n\n", split[index]);
                return false;
            }

            priceSum += this.DM.getProducts().get(split[index].split("\\*")[0]).price * Integer.valueOf(split[index].split("\\*")[1]);
            priceSum = Double.valueOf(String.format("%.2f", priceSum));

            index++;
        }

        if (cash) {
            
            double payment = 0;
            while (index < split.length) {
                if (!split[index].matches(UserInputFormat.Denomination + "\\*" + UserInputFormat.Quantity)) {
                    System.out.printf("Invalid format: %s\n\n", split[index]);
                    System.out.printf("Correct cash buy command: %s\n", UserInputFormat.buy_cash);
                    System.out.println("Please type 'help verbose' for detailed command formats\n");
                    return false;
                }

                double value = 0;

                String denomination = split[index].split("\\*")[0];
                if (denomination.matches(UserInputFormat.Dollors)) {
                    value = Double.valueOf(denomination.substring(1, denomination.length()));
                } else { // matches UserInputFormat.Cents
                    value = Double.valueOf(denomination.substring(0, denomination.length() - 1)) / 100;
                }
                value = Double.valueOf(String.format("%.2f", value));

                int amount = Integer.valueOf(split[index].split("\\*")[1]);
                if(cashStorage.checkCapicity(denomination)<amount){
                    System.out.printf("Sorry, there are not enough space for %s*%d\n",denomination,amount);
                    return false;
                }
                payment += value * amount;
                tempCashInputRecord.put(denomination, amount);
                payment = Double.valueOf(String.format("%.2f", payment));

                index++;
            }

            if (payment < priceSum) {
                System.out.println("Not enough payment\n");
                return false;
            }

            double change = payment - priceSum;
            String changeStr = String.format("Change %.2f - %.2f = %.2f\n", payment, priceSum, change);
            String breakdown = "";
            if (change > 0) {
                if(change>cashStorage.getStorageAmountDouble()){
                    System.out.println("There is no enough cash in the machine for return, please contact the maneger.");
                    return false;
                }
                breakdown = "Change breakdown:";

                int[] dollars = {100, 50, 20, 10, 5, 2, 1};
                for (int dollar : dollars) {
                    if (change >= dollar) {
                        String changeDeno = String.format("$%d",dollar);
                        Integer amount = (int) ((int)(change * 100) / (int)(dollar * 100));
                        if(cashStorage.checkRemaining(changeDeno)==0){
                            System.out.println("Sorry, the cash to return is not enough");
                            return false;
                        }
                        if(cashStorage.checkRemaining(changeDeno)>=amount){
                            breakdown += String.format(" ($%d * %d)", dollar, amount);
                            tempCashReturnRecord.put(changeDeno, amount);
                            change %= dollar;
                            change = Double.valueOf(String.format("%.2f", change));
                        }
                        else{
                            amount = cashStorage.checkRemaining(changeDeno);
                            breakdown += String.format(" ($%d * %d)", dollar, amount);
                            change -= dollar*amount;
                            change = Double.valueOf(String.format("%.2f", change));
                        }
                        
                    }

                    if (change <= 0) {
                        break;
                    }
                }

                int changeCents = (int) (change * 100);
                int[] cents = {50, 20, 10, 5};
                for (int cent : cents) {
                    if (changeCents >= cent) {
                        String changeCent = String.format("$%s",cent);
                        Integer amount = (int) changeCents/cent;
                        if(cashStorage.checkRemaining(changeCent)==0){
                            System.out.println("Sorry, the cash to return is not enough");
                            return false;
                        }
                        if(cashStorage.checkRemaining(changeCent)>=amount){
                            breakdown += String.format(" (%dc * %dc)", cent, amount);
                            tempCashReturnRecord.put(changeCent,  amount);
                            changeCents %= cent;
                        }else{
                            amount = cashStorage.checkRemaining(changeCent);
                            breakdown += String.format(" (%dc * %dc)", cent, amount);
                            tempCashReturnRecord.put(changeCent,  amount);
                            changeCents -= cent*amount;
                        }
                    }

                    if (changeCents <= 0) {
                        break;
                    }
                }

                breakdown += "\n";
            }
            // System.out.println(cashStorage.checkStorage());
            // System.out.println(tempCashInputRecord);
            for(String key:tempCashInputRecord.keySet()){
                cashStorage.addDenomination(key, tempCashInputRecord.get(key));
            }
            
            // System.out.println(cashStorage.checkStorage());
            // System.out.println(tempCashReturnRecord);
            for(String key:tempCashReturnRecord.keySet()){
                cashStorage.getDenomination(key,tempCashReturnRecord.get(key));
            }
            //System.out.println(cashStorage.checkStorage());
            System.out.print(changeStr);
            System.out.print(breakdown);
            System.out.println();

            return true;

        } else { // card
            if (split.length - index != 2) {
                System.out.println("Invalid card info length\n");
                System.out.printf("Correct card buy command: %s\n", UserInputFormat.buy_card);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            } else if (!this.DM.getCreditCards().containsKey(split[index])) {
                System.out.println("Invalid card details\n"); // Invalid card holder
                System.out.printf("Correct card buy command: %s\n", UserInputFormat.buy_card);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            } else if (!this.DM.getCreditCards().get(split[index]).cardNumber.equals(split[index+1])) {
                System.out.println("Invalid card details\n"); // Invalid card number
                System.out.printf("Correct card buy command: %s\n", UserInputFormat.buy_card);
                System.out.println("Please type 'help verbose' for detailed command formats\n");
                return false;
            } else {
                System.out.printf("Transaction completed: Valid card: Paid $%.2f\n\n", priceSum);
                return true;
            }
        }
    }

    public boolean addProductLogic(String[] split){
        // test if the String is full of digits
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if(split.length < 6){
            System.out.println("Not enough arguments: add product\n");
            System.out.printf("Correct list_products command: %s\n", UserInputFormat.add_product);
            System.out.println("Please type 'help verbose' for detailed command formats\n");
        }
        else if (this.DM.getProducts().containsKey(split[1])){
            System.out.println("This product exist. You cannot added it twice.");
            return false;
        }
        else if (!pattern.matcher(split[2]).matches()){
            return true;
        }
        
        return false;
    }

    // Call your class methods (related to processing logic) here:
}
