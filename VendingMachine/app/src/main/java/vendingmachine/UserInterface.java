/*
 * UserInterface.java in Presentation Logic Layer
 */
package vendingmachine;

import java.util.*;
import java.util.Timer;

public class UserInterface {
    public ProcessingLogic PL;
    public UserInputChecker checker;
    public List<String> availableCommands = Arrays.asList(
        "exit",
        "help",
        "register",
        "login",
        "logout",
        "list",
        "buy",
        "cancel",
        "add",
        "del",
        "update"
    );
    public String commonCommands = (
        ("  " + UserInputFormat.exit + "\n")
      + ("  " + UserInputFormat.help + "\n")
      + ("  " + UserInputFormat.register + "\n")
      + ("  " + UserInputFormat.login +  "\n")
      + ("  " + UserInputFormat.logout + "\n")
    );
    public String buyerCommands = (
          ("  " + UserInputFormat.list_products + "\n")
        + ("  " + UserInputFormat.buy_cash + "\n")
        + ("  " + UserInputFormat.buy_card + "\n")
        + ("  " + UserInputFormat.cancel + "\n")
    );
    public String sellerCommands = (
          ("  " + UserInputFormat.list_products_available + "\n")
        + ("  " + UserInputFormat.list_products_sold + "\n")
        + ("  " + UserInputFormat.add_product + "\n")
        + ("  " + UserInputFormat.del_product + "\n")
    );
    public String cashierCommands = (
          ("  " + UserInputFormat.list_change + "\n")
        + ("  " + UserInputFormat.list_transactions_done + "\n")
        + ("  " + UserInputFormat.update_change + "\n")
    );
    public String ownerCommands = (
          ("  " + UserInputFormat.list_accounts + "\n")
        + ("  " + UserInputFormat.list_transactions_cancelled + "\n")
        + ("  " + UserInputFormat.add_account + "\n")
        + ("  " + UserInputFormat.del_account + "\n")
    );
    public String verboseInformation = (
        "\nSemantics (similar to BASH):\n"
        + "  [ABC]\n"
        + "    - ABC is optional.\n"
        + "  <ABC>\n"
        + "    - Enter a meaningful option related to ABC.\n"
        + "  (ABC | DEF)\n"
        + "    - Enter either ABC or DEF (not both).\n"
        + "\n"
        + "Breakdowns (Option RegEx Description):\n"
        + "  Username " + UserInputFormat.Username + "\n"
        + "    - Alphabetical username."
        + "  Password " + UserInputFormat.Password + "\n"
        + "    - Alphanumeric password."
        + "  Role " + UserInputFormat.Role + "\n"
        + "    - One from 'buyer', 'seller', 'cashier', and 'owner'."
        + "  ProductId " + UserInputFormat.ProductId + "\n"
        + "    - Three lowercase alphabetic al characters.\n"
        + "  ProductName " + UserInputFormat.ProductName + "\n"
        + "    - Alphabetical characters.\n"
        + "  Category " + UserInputFormat.Category + "\n"
        + "    - Alphabetical characters.\n"
        + "  Quantity " + UserInputFormat.Quantity + "\n"
        + "    - Integer in base10.\n"
        + "  Price " + UserInputFormat.Price + "\n"
        + "    - Notes (dollars) followed by coins (cents).\n"
        + "  Denomination " + UserInputFormat.Denomination + "\n"
        + "    - Notes (dollars) or coins (cents).\n"
        + "  CardHolder " + UserInputFormat.CardHolder + "\n"
        + "    - Titlecase cardholder name.\n"
        + "  CardNumber " + UserInputFormat.CardNumber + "\n"
        + "    - Five digits.\n"
    );

    public UserInterface() {
        this.PL = new ProcessingLogic();
        this.checker = new UserInputChecker(availableCommands);
    }

    public void help(String[] split) {
        String str = "help\n";

        String role = this.PL.getRole();
        switch (role) {
            case "buyer":
                str = (
                    "\nSupported commands for buyer:\n"
                    + this.commonCommands
                    + this.buyerCommands
                );
                break;
            case "seller":
                str = (
                    "\nSupported commands for seller:\n"
                    + this.commonCommands
                    + this.sellerCommands
                );
                break;
            case "cashier":
                str = (
                    "\nSupported commands for cashier:\n"
                    + this.commonCommands
                    + this.cashierCommands
                );
                break;
            case "owner":
                str = (
                    "\nSupported commands for owner:\n"
                    + this.commonCommands
                    + this.buyerCommands
                    + this.sellerCommands
                    + this.cashierCommands
                    + this.ownerCommands
                );
                break;
            default:
                str = String.format("Invalid role: %s\n", role);
                break;
        }

        if (split.length == 2 && split[1].equals("verbose")) {
            str += this.verboseInformation;
        }

        System.out.println(str);
    }

    public void register(String[] split) {
        this.PL.registerLogic(split);
    }

    public void login(String[] split) {
        this.PL.loginLogic(split);
    }

    public void logout(String[] split) {
        this.PL.logoutLogic(split);
    }

    public void list(String[] split) {
        this.PL.listLogic(split);
    }

    public void buy(String[] split) {
        this.PL.buyLogic(split);
    }

    public void run(Scanner sc) {
        System.out.println("\nWelcome to Vending Machine!");
        System.out.println("Enter 'help' to get help for command syntax.\n");
        System.out.print(String.format("%s(%s) $ ", this.PL.getUser(), this.PL.getRole()));

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] split = input.split(" ");

            // Check user input format
            // String result = this.checker.commandFormatCheck(split);
            // if (!result.equals("valid")) {
                //     System.out.println(result + "\n");
            // }

            // // Print DB
            // this.PL.DM.printProducts();
            // this.PL.DM.printCashChange();
            // this.PL.DM.printCreditCards();
            // this.PL.DM.printAccounts();

            // Use equals or equalsIgnoreCase
            if (split[0].equals("exit")) {
                System.out.println("Bye!\n");
                break;
            } else if (split[0].equals("help")) {
                this.help(split);
            } else if (split[0].equals("register")) {
                this.register(split);
            } else if (split[0].equals("login")) {
                this.login(split);
            } else if (split[0].equals("logout")) {
                this.logout(split);
            } else if (split[0].equals("list")) {
                this.list(split);
            } else if (split[0].equals("buy")) {
                this.buy(split);
            } else if (split[0].equals("cancel")) {
                System.out.println("Not supported yet\n");
            } else if (split[0].equals("add")) {
                System.out.println("Not supported yet\n");
            } else if (split[0].equals("del")) {
                System.out.println("Not supported yet\n");
            } else if (split[0].equals("update")) {
                System.out.println("Not supported yet\n");
            } else {
                System.out.println("Invalid command\n");
            }

            System.out.print(String.format("%s(%s) $ ", this.PL.getUser(), this.PL.getRole()));
        }

        sc.close();
        return;
    }

    // Functionalities integrated into (i.e. moved to) buyLogic
    /*
    public void buyAlt(String[] userInput) {
        // 0 if invalid (no cash/card specified), 1 if cash, 2 if card
        int paymentBool = 0;
        String[] paymentType = {"cash","card"};

        HashMap<String, Double> currencyMap = new HashMap<String, Double>();
        currencyMap.put("50", 50.0);
        currencyMap.put("20", 20.0);
        currencyMap.put("10", 10.0);
        currencyMap.put("5", 5.0);
        currencyMap.put("2", 2.0);
        currencyMap.put("1", 1.0);
        currencyMap.put("50c", 0.5);
        currencyMap.put("20c", 0.2);
        currencyMap.put("10c", 0.1);
        currencyMap.put("5c", 0.05);

        HashMap<String, Integer> currencyUsedMap = new HashMap<String, Integer>();
        currencyUsedMap.put("50", 0);
        currencyUsedMap.put("20", 0);
        currencyUsedMap.put("10", 0);
        currencyUsedMap.put("5", 0);
        currencyUsedMap.put("2", 0);
        currencyUsedMap.put("1", 0);
        currencyUsedMap.put("50c", 0);
        currencyUsedMap.put("20c", 0);
        currencyUsedMap.put("10c", 0);
        currencyUsedMap.put("5c", 0);

        List<String> payList = new ArrayList<>(Arrays.asList(paymentType));
        String[] tempSplit;

        double cashCount = 0;
        double productCostCount = 0;

            List<String> inputList = Arrays.asList(userInput);
            for (String splitInput : inputList){
                if (payList.contains(splitInput) && paymentBool == 0) {
                    for (int i = 0; i < 2; i++) {
                        if (splitInput.equalsIgnoreCase(payList.get(i))){
                            paymentBool += i+1;
                        }
                    }
                }
                else if (payList.contains(splitInput) && paymentBool != 0){
                    //invalid input multiple payment types i.e. buyer cash cash or buyer card cash, etc.
                }
                else {
                    tempSplit = splitInput.split("*", 2);
                    if (tempSplit.length == 2){
                        if (Integer.valueOf(tempSplit[1]) > 0){
                            //check if currency
                            if(currencyMap.containsKey(tempSplit[0])){
                                cashCount += (currencyMap.get(tempSplit[0]) * (Integer.valueOf(tempSplit[1])));
                                currencyUsedMap.put(tempSplit[0], currencyUsedMap.get(tempSplit[0]) + (1 * Integer.valueOf(tempSplit[1])));
                            }
                            // check if product TODO
                            else if (tempSplit[0] == "1"){
                                productCostCount += 1.0;
                            } else {
                                //invalid cash/product
                                return;
                            }
                        } else {
                            //invalid input on amount of product/cash
                            return;
                        }
                    } else {
                        //card validity check
                    }
                }
            }
            if (paymentBool == 1 && productCostCount > cashCount){
                return;
            } else if (paymentBool == 2){
                //successful return of card
                return;
            }
            // TODO:
            // Adding cash to database
            // Calculating change
            // return breakdown upon successful
    }
    */
}