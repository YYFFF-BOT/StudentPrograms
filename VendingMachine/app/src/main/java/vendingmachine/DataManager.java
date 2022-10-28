/*
 * DataManager.java in Data Management Layer
 */
package vendingmachine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

// this class is used for parsing data from the json file stored the products
public class DataManager {
    private final String productsPath = "src/main/resources/products.json";
    private final String cashChangePath = "src/main/resources/cash_change.json";
    private final String creditCardsPath = "src/main/resources/credit_cards.json";
    private final String accountsPath = "src/main/resources/accounts.json";
    private HashMap<String, Product> products; // productId -> Product
    private HashMap<String, Cash> cashChange; // note -> Cash
    private HashMap<String, CreditCard> creditCards; // card_holder -> CreditCard
    private HashMap<String, Account> accounts; // username -> Account

    static String[] denominations = {
        "$100", "$50", "$20", "$10", "$5", // notes
        "$2", "$1", "50c", "20c", "10c", "5c" // coins
    };

    // Inner class
    class Product {
        String id;
        String name;
        String category;
        int quantity;
        double price;

        Product(String id, String name, String ca, int qu, double pr) {
            this.id = id;
            this.name = name;
            this.category = ca;
            this.quantity = qu;
            this.price = pr;
        }

        public Product(String id2, Product name2, String ca, int qu, double pr) {
        }

        String getId() {
            return this.id;
        }
        String getName() {
            return this.name;
        }
        String getCategory() {
            return this.category;
        }
        int getQuantity() {
            return this.quantity;
        }
        double getPrice() {
            return this.price;
        }
    }
    // Inner class
    class Cash {
        String denomination;
        Integer amount;

        Cash(String denom, Integer amt) {
            this.denomination = denom;
            this.amount = amt;
        }
        
        public String getDenomination() {
            return this.denomination;
        }
        public Integer getAmount() {
            return this.amount;
        }
    }  
    // Inner class
    class CreditCard {
        String cardHolder;
        String cardNumber;

        CreditCard(String holder, String number) {
            this.cardHolder = holder;
            this.cardNumber = number;
        }
        
        String getCardHolder() {
            return this.cardHolder;
        }
        String getCardNumber() {
            return this.cardNumber;
        }
    }
    // Inner class
    class Account {
        String username;
        String password;
        String role;
        ArrayList<CreditCard> cards;

        Account(String name, String pw, String role) {
            this.username = name;
            this.password = pw;
            this.role = role;
            this.cards = new ArrayList<CreditCard>();
        }
        Account(String name, String pw, String role, ArrayList<CreditCard> cards) {
            this.username = name;
            this.password = pw;
            this.role = role;
            this.cards = cards;
        }
        
        String getUsername() {
            return this.username;
        }
        String getPassWord() {
            return this.password;
        }
        String getRole() {
            return this.role;
        }
        ArrayList<CreditCard> getCards() {
            return this.cards;
        }
    }

    // Initialise DB from files
    public DataManager() {
        this.products = new HashMap<String, Product>();
        this.fetchProducts();

        this.cashChange = new HashMap<String, Cash>();
        this.fetchCashChange();

        this.creditCards = new HashMap<String, CreditCard>();
        this.fetchCreditCards();

        this.accounts = new HashMap<String, Account>();
        this.fetchAccounts();
    }

    /* ==============================================================
     * ======================== Products ============================
     * ==============================================================
     */

    // Read from json file (products)
    public boolean fetchProducts() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray arr = (JSONArray) parser.parse(new FileReader(this.productsPath));
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                Product product = new Product(
                    (String) obj.get("id"),
                    (String) obj.get("name"),
                    (String) obj.get("category"),
                    Integer.valueOf((String) obj.get("quantity")),
                    Double.valueOf((String) obj.get("price"))
                );
                this.products.put(product.id, product);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Save Java data structures to json file (products)
    public boolean flushProducts() {
        try {
            JSONArray arr = new JSONArray();

            for (String id : this.products.keySet()) {
                Product product = this.products.get(id);
                
                JSONObject obj = new JSONObject();
                obj.put("id", product.getId());
                obj.put("name", product.getName());
                obj.put("category", product.getCategory());
                obj.put("quantity", String.format("%d", product.getQuantity()));
                obj.put("price", String.format("%.2f", product.getPrice()));

                arr.add(obj);
            }

            FileWriter fw = new FileWriter(productsPath);
            fw.write(arr.toJSONString());
            fw.flush();
            fw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public HashMap<String, Product> getProducts() {
        return this.products;
    }

    public boolean addProduct(String id, String name, String ca, int qu, double pr) {
        // NEED TO BE FIXED: CURRENTLY DOES NOT PERFORM FORMAT CHECKS
        
        Product newProd = new Product(id, name, ca, qu, pr);
        
        // Test if this new one can be added in
        if (this.products.containsKey(id)) {
            System.out.println("This product already exists.\n");
            return false;
        } else if (qu > 15) {
            System.out.println("This product's quantity cannot be added to the machine because it exceeds the maximum storage (15).\n");
            return false;
        } else if (qu < 0) {
            System.out.println("Negative quantity cannot be added.\n");
            return false;
        } else if (pr < 0){
            System.out.println("Negative price cannot be added.");
            return false;
        }

        this.products.put(id, newProd);
        this.flushProducts();
        return true;
    }

    // Print all products stored in RAM as Java data structures
    public boolean printProducts() {
        String header = "\n|=============================================================|\n"
                        + "| [id]   [name]            [category]      [quantity] [price] |\n"
                        + "|-------------------------------------------------------------|\n";
        HashMap<String, String> categories = new HashMap<String, String>(); // category -> product_list

        for (String id : this.products.keySet()) {
            Product product = this.products.get(id);
            String productString = (
                String.format("| %-6s %-17s %-18s %-9s %-5s |\n",
                    product.id,
                    product.name,
                    product.category,
                    product.quantity,
                    product.price
                )
            );

            if (categories.keySet().contains(product.category)) {
                String oldStr = categories.get(product.category);
                String newStr = oldStr + productString;
                categories.put(product.category, newStr);
            } else {
                String newStr = productString;
                categories.put(product.category, newStr);
            }

        }

        System.out.print(header);
        for (String category : categories.keySet()) {
            System.out.print(categories.get(category));
            System.out.println("|                                                             |");
        }
        System.out.println("|=============================================================|\n");

        System.out.printf("Cash buying command: %s\n", UserInputFormat.buy_cash);
        System.out.printf("Card buying command: %s\n", UserInputFormat.buy_card);
        System.out.println();

        return true;
    }

    /* ==============================================================
     * ======================= CashChange ===========================
     * ==============================================================
     */

    // Read from json file (cash_change)
    public boolean fetchCashChange() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray arr = (JSONArray) parser.parse(new FileReader(this.cashChangePath));
            JSONObject obj = (JSONObject) arr.get(0);
            for (String denom : denominations) {
                Cash cash = new Cash(denom, Integer.valueOf((String) obj.get(denom)));
                this.cashChange.put(denom, cash);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Save Java data structures to json file (cash_change)
    public boolean flushCashChange() {
        for (Map.Entry<String, Cash> entry : cashChange.entrySet()) {
            String denom = entry.getKey();
            int amount = entry.getValue().getAmount();
            boolean setChangeBool = setCashChange(denom, amount);
            if (!setChangeBool) {
                return false;
            }
        }
        return true;
    }

    public HashMap<String, Cash> getCashChange() {
        return this.cashChange;
    }

    public boolean setCashChange(String denomination, int amount) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray arr = (JSONArray) parser.parse(new FileReader(this.cashChangePath));
            JSONObject obj = (JSONObject) arr.get(0);
            obj.put(denomination, amount);

            FileWriter file = new FileWriter(cashChangePath);
            file.write(arr.toJSONString());
            file.flush();
            file.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean printCashChange() {
        System.out.println("[Cash Change] (denomination: amount)");
        for (String denomination : denominations) {
            Integer amount = this.cashChange.get(denomination).amount;
            System.out.println(denomination + ": " + amount);
        }
        System.out.println();

        return true;
    }

    /* ==============================================================
     * ======================= CreditCards ==========================
     * ==============================================================
     */

    // Read from json file (credit_cards)
    public boolean fetchCreditCards() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray arr = (JSONArray) parser.parse(new FileReader(this.creditCardsPath));
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String cardHolder = (String) obj.get("name");
                String cardNumber = (String) obj.get("number");
                CreditCard card = new CreditCard(cardHolder, cardNumber);
                this.creditCards.put(cardHolder, card);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Save Java data structures to json file (credit_cards)
    public boolean flushCreditCards(CreditCard newCC) {
        JSONParser jParser = new JSONParser();
        try {
            Object obj = jParser.parse(new FileReader(creditCardsPath));
            JSONArray jsonArray = (JSONArray)obj;

            JSONObject newCard = new JSONObject();
            newCard.put("name",newCC.getCardHolder());
            newCard.put("number", newCC.getCardNumber());

            jsonArray.add(newCard);

            FileWriter file = new FileWriter(creditCardsPath);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public HashMap<String, CreditCard> getCreditCards() {
        return this.creditCards;
    }

    public boolean addCreditCard(String cardHolder, String cardNumber) {
        CreditCard newCard = new CreditCard(cardHolder, cardNumber);
        return flushCreditCards(newCard);
    }

    public boolean printCreditCards() {
        System.out.println("[Credit Cards] (holder: number)");
        for (String cardHolder : this.creditCards.keySet()) {
            String cardNumber = this.creditCards.get(cardHolder).cardNumber;
            System.out.println(cardHolder + ": " + cardNumber);
        }
        System.out.println();

        return true;
    }

    /* ==============================================================
     * ========================= Accounts ===========================
     * ==============================================================
     */

    // Read from json file (accounts)
    public boolean fetchAccounts() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray arr = (JSONArray) parser.parse(new FileReader(this.accountsPath));
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String username = (String) obj.get("username");
                String password = (String) obj.get("password");
                String role = (String) obj.get("role");
                ArrayList<CreditCard> cards = new ArrayList<CreditCard>();
                Account account = new Account(username, password, role, cards);
                this.accounts.put(username, account);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Save Java data structures to json file (accounts)
    public boolean flushAccounts(Account newAccount) {
        JSONParser jParser = new JSONParser();
        try {
            Object obj = jParser.parse(new FileReader(accountsPath));
            JSONArray jsonArray = (JSONArray)obj;

            JSONObject newAcc = new JSONObject();
            newAcc.put("username", newAccount.getUsername());
            newAcc.put("password", newAccount.getPassWord());
            newAcc.put("role", newAccount.getRole());

            jsonArray.add(newAcc);

            FileWriter file = new FileWriter(accountsPath);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public HashMap<String, Account> getAccounts() {
        return this.accounts;
    }

    public boolean addAccount(String username, String password, String role) {
        Account newAccount = new Account(username, password, role, null);
        this.accounts.put(username, newAccount);
        boolean res = flushAccounts(newAccount);
        return res;
    }

    public boolean printAccounts() {
        // This can be converted to a more compact version using temporary data structure
        System.out.println("[Accounts] (username,password,role)");
        System.out.println("[Buyer]");
        for (String username : this.accounts.keySet()) {
            String password = this.accounts.get(username).password;
            String role = this.accounts.get(username).role;
            if (role.equals("buyer")) {
                System.out.println(username + "," + password + "," + role);
            }
        }
        System.out.println("[Seller]");
        for (String username : this.accounts.keySet()) {
            String password = this.accounts.get(username).password;
            String role = this.accounts.get(username).role;
            if (role.equals("seller")) {
                System.out.println(username + "," + password + "," + role);
            }
        }
        System.out.println("[Cashier]");
        for (String username : this.accounts.keySet()) {
            String password = this.accounts.get(username).password;
            String role = this.accounts.get(username).role;
            if (role.equals("cashier")) {
                System.out.println(username + "," + password + "," + role);
            }
        }
        System.out.println("[Owner]");
        for (String username : this.accounts.keySet()) {
            String password = this.accounts.get(username).password;
            String role = this.accounts.get(username).role;
            if (role.equals("owner")) {
                System.out.println(username + "," + password + "," + role);
            }
        }
        System.out.println();

        return true;
    }

    //TODO: remove account
    public boolean removeAccount(String username, String role){
        return false;
    }
}
